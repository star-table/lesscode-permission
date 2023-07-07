package com.polaris.lesscode.permission.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.polaris.lesscode.app.internal.enums.AppType;
import com.polaris.lesscode.app.internal.feign.AppProvider;
import com.polaris.lesscode.app.internal.req.IsProjectMemberBatchReq;
import com.polaris.lesscode.app.internal.resp.AppResp;
import com.polaris.lesscode.consts.CommonConsts;
import com.polaris.lesscode.dc.internal.api.DataCenterApi;
import com.polaris.lesscode.dc.internal.dsl.*;
import com.polaris.lesscode.exception.BusinessException;
import com.polaris.lesscode.form.internal.enums.FieldTypeEnums;
import com.polaris.lesscode.form.internal.sula.FieldParam;
import com.polaris.lesscode.gotable.internal.req.ReadSummeryTableIdRequest;
import com.polaris.lesscode.gotable.internal.resp.ReadSummeryTableIdResp;
import com.polaris.lesscode.permission.annotation.AutoCache;
import com.polaris.lesscode.permission.bo.Event;
import com.polaris.lesscode.permission.bo.PerGroupEvent;
import com.polaris.lesscode.permission.config.RedisConfig;
import com.polaris.lesscode.permission.constant.CacheConsts;
import com.polaris.lesscode.permission.constant.PermissionConstant;
import com.polaris.lesscode.permission.constant.ThreadPools;
import com.polaris.lesscode.permission.entity.PerAppPermissionConfig;
import com.polaris.lesscode.permission.entity.PerAppPermissionGroup;
import com.polaris.lesscode.permission.entity.PerAppPermissionMember;
import com.polaris.lesscode.permission.internal.enums.*;
import com.polaris.lesscode.permission.internal.model.OrgUserPermissionContext;
import com.polaris.lesscode.permission.internal.model.UserPermissionVO;
import com.polaris.lesscode.permission.internal.model.bo.*;
import com.polaris.lesscode.permission.internal.model.req.GetAppAuthorityBatchReq;
import com.polaris.lesscode.permission.internal.model.req.GetDataAuthorityBatchReq;
import com.polaris.lesscode.permission.internal.model.resp.AppAuthorityResp;
import com.polaris.lesscode.permission.internal.model.resp.AppPerGroupListItem;
import com.polaris.lesscode.permission.internal.model.resp.AppPerGroupListResp;
import com.polaris.lesscode.permission.internal.model.resp.MemberGroupMappingsResp;
import com.polaris.lesscode.permission.internal.service.impl.AppPermissionGroupMergeServiceImpl;
import com.polaris.lesscode.permission.mapper.AppPermissionConfigMapper;
import com.polaris.lesscode.permission.mapper.AppPermissionGroupMapper;
import com.polaris.lesscode.permission.mapper.AppPermissionMemberMapper;
import com.polaris.lesscode.permission.model.req.AppPermissionGroupInfoListReq;
import com.polaris.lesscode.permission.model.req.MemberSwitchGroupReq;
import com.polaris.lesscode.permission.model.req.SaveAppPermissionGroupReq;
import com.polaris.lesscode.permission.model.resp.AppPerBaseConfigResp;
import com.polaris.lesscode.permission.model.resp.AppPerGroupInfoResp;
import com.polaris.lesscode.permission.rest.PermissionResultCode;
import com.polaris.lesscode.permission.service.*;
import com.polaris.lesscode.permission.utils.PerBaseConfigUtils;
import com.polaris.lesscode.permission.utils.PerMemberUtils;
import com.polaris.lesscode.util.*;
import com.polaris.lesscode.vo.BaseResultCode;
import com.polaris.lesscode.vo.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.*;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用权限组 业务逻辑实现
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 15:56
 */
@Slf4j
@Service
public class AppPermissionServiceImpl extends ServiceImpl<AppPermissionGroupMapper, PerAppPermissionGroup>
        implements AppPermissionService {
    private final AppPermissionMemberMapper permissionMemberMapper;
    private final AppPermissionGroupMapper appPermissionGroupMapper;
    private final UserService userService;
    private final AppService appService;
    private final AppPermissionConfigMapper appPermissionConfigMapper;
    private final AppProvider appProvider;
    private final DataCenterApi dataCenterApi;
    private final GoTableService goTableService;
    private final AppPermissionGroupMergeServiceImpl appPermissionGroupMergeService;
    private final GoPushService goPushService;
    private final RedisConfig redisUtil;

    public AppPermissionServiceImpl(AppPermissionMemberMapper permissionMemberMapper, AppPermissionGroupMapper appPermissionGroupMapper,
                                    UserService userService, AppService appService, AppPermissionConfigMapper appPermissionConfigMapper,
                                    AppProvider appProvider, DataCenterApi dataCenterApi,
                                    GoTableService goTableService, GoPushService goPushService,
                                    AppPermissionGroupMergeServiceImpl appPermissionGroupMergeService, RedisConfig redisUtil) {
        this.permissionMemberMapper = permissionMemberMapper;
        this.appPermissionGroupMapper = appPermissionGroupMapper;
        this.userService = userService;
        this.appService = appService;
        this.appPermissionConfigMapper = appPermissionConfigMapper;
        this.appProvider = appProvider;
        this.dataCenterApi = dataCenterApi;
        this.goTableService = goTableService;
        this.goPushService = goPushService;
        this.appPermissionGroupMergeService = appPermissionGroupMergeService;
        this.redisUtil = redisUtil;
    }

    public AppAuthorityResp getPublicTemplateAppAuthority(Long orgId, Long appId, Long userId){
        AppAuthorityResp appAuthority = new AppAuthorityResp();
        appAuthority.setAppOwner(false);
        appAuthority.setAppId(appId);
        appAuthority.setLangCode(null);
        appAuthority.setSysAdmin(false);
        appAuthority.setOrgOwner(false);
        appAuthority.setSubAdmin(false);
        appAuthority.setOutCollaborator(false); // userPermissionVO.getIsOutCollaborator() 有可能为null
        appAuthority.setAppAuth(new ArrayList<>());
        appAuthority.setViewAuth(new HashMap<>());
        appAuthority.setDataAuth(new ArrayList<>());
        appAuthority.setFieldAuth(new HashMap<>());
        appAuthority.setOptAuth(new HashSet<>());
        appAuthority.setTableAuth(new HashSet<>());
        appAuthority.setHasAppRootPermission(false);
        return appAuthority;
    }

    @Override
    @AutoCache( value = CacheConsts.APP_AUTH_CACHE_KEY, type = AutoCache.CacheType.STRING, expire = 5 )
    public AppAuthorityResp getAppAuthority(Long orgId, Long appId, Long tableId, Long userId, Boolean isContainCollaborator, List<Long> collaboratorRoleIds) {
        if (Objects.equals(orgId, PermissionConstant.PUBLIC_TEMPLATE_ORG_ID) && Objects.equals(userId, PermissionConstant.PUBLIC_TEMPLATE_USER_ID)) {
            return getPublicTemplateAppAuthority(orgId, appId, userId);
        }
        AppResp appResp = appService.getApp(orgId, appId);
        if (appResp == null) {
            throw new BusinessException(ResultCode.APP_NOT_EXIST);
        }

        // 获取继承的应用权限
        AppResp extendsAppResp = appProvider.getAuthExtendsApp(appId).getData();
        if (extendsAppResp == null) {
            throw new BusinessException(ResultCode.APP_NOT_EXIST);
        }
        boolean isExtends = ! Objects.equals(appId, extendsAppResp.getId());
        log.info("appId {}, extendsApp {}, isExtends {}", appId, extendsAppResp.getId(), isExtends);
        appId = extendsAppResp.getId();

        UserPermissionVO userPermissionVO = userService.getUserPermission(orgId, userId);
        List<PerAppPermissionGroup> permissionGroupList = getAppPermissionGroupsByUser(orgId, appId,
                userId, userPermissionVO.getRefDeptIds(),
                userPermissionVO.getRefRoleIds());

        if (isExtends && CollectionUtils.isNotEmpty(permissionGroupList)) {
            permissionGroupList.removeIf(per -> Objects.isNull(AppPerDefaultGroupLangCode.forValue(per.getLangCode())));
        }

        boolean isProjectMember = appProvider.isProjectMember(appId, orgId, userId).getData();
        if (!isExtends) {
            log.info("appId {}, isProjectMember {}, permissionGroupList {}", appId, isProjectMember, permissionGroupList);
            if (CollectionUtils.isEmpty(permissionGroupList) && isProjectMember) {
                List<PerAppPermissionGroup> groups = getAppPermissionGroupList(orgId, appId);
                Map<String, PerAppPermissionGroup> groupMap = MapUtils.toMap(PerAppPermissionGroup::getLangCode, groups);
                PerAppPermissionGroup defaultGroup = groupMap.get(AppPerDefaultGroupLangCode.PROJECT_MEMBER.getCode());
                if (defaultGroup == null) {
                    defaultGroup = groupMap.get(AppPerDefaultGroupLangCode.EDIT.getCode());
                }
                if (defaultGroup != null) {
                    // 如果项目id不等于0，说明是北极星的产物
                    if (! Objects.equals(appResp.getProjectId(), 0L)){
                        defaultGroup.setOptAuth(GsonUtils.toJson(AppPerDefaultGroupLangCode.PROJECT_MEMBER.getDefaultOptAuth().get()));
                        defaultGroup.setTableAuth(GsonUtils.toJson(AppPerDefaultGroupLangCode.PROJECT_MEMBER.getDefaultTableAuth().get()));
                        defaultGroup.setColumnAuth("{}");
                    }
                    permissionGroupList = Collections.singletonList(defaultGroup);
                }
            }
        }


        if (isContainCollaborator) {
            // 2022-01-12：聚合协作人权限
            // 需求来源：产品经理
            boolean hasEditorRole = false;
            Set<Long> roleIdSet = new HashSet<>();
            if (collaboratorRoleIds == null) {
                LocalTime startTime = LocalTime.now();
                List<String> roleIdStrs = goTableService.getUserAppCollaboratorRoles(orgId, userId, appResp.getId());
                LocalTime endTime = LocalTime.now();
                log.info("getUserAppCollaboratorRoles orgId {}, userId {}, appId {}, roleIds {}, cost {}s", orgId, userId, appId, roleIdStrs, Duration.between(startTime, endTime).getNano()/1000000000f);
                collaboratorRoleIds = new ArrayList<Long>();
                for (String roleIdStr : roleIdStrs) {
                    Long roleId = Long.parseLong(roleIdStr);
                    if (roleId == -1) {
                        hasEditorRole = true;
                    }
                    roleIdSet.add(roleId);
                    collaboratorRoleIds.add(roleId);
                }
            }
            if (CollectionUtils.isNotEmpty(collaboratorRoleIds)) {
                if (Objects.isNull(permissionGroupList)) {
                    permissionGroupList = new ArrayList<>();
                } else {
                    permissionGroupList = new ArrayList<>(permissionGroupList);
                }
                List<PerAppPermissionGroup> groups;
                if (hasEditorRole) {
                    List<PerAppPermissionGroup> allGroups = getAppPermissionGroupList(orgId, appId);
                    groups = new ArrayList<>();
                    for (PerAppPermissionGroup group : allGroups) {
                        if (roleIdSet.contains(group.getId())) {
                            groups.add(group);
                            continue;
                        }

                        if (hasEditorRole && (
                                Objects.equals(group.getLangCode(), AppPerDefaultGroupLangCode.EDIT.getCode()) ||
                                Objects.equals(group.getLangCode(), AppPerDefaultGroupLangCode.PROJECT_MEMBER.getCode()))) {
                            groups.add(group);
                            hasEditorRole = false;
                            continue;
                        }
                    }
                } else {
                    groups = getAppPermissionGroupList(orgId, appId, collaboratorRoleIds);
                }
                if (CollectionUtils.isNotEmpty(groups)) {
                    for (PerAppPermissionGroup group: groups) {
                        group.setCollaborator(true);
                        if (Objects.equals(group.getLangCode(), AppPerDefaultGroupLangCode.EDIT.getCode()) && ! Objects.equals(appResp.getProjectId(), 0L)) {
                            group.setOptAuth(GsonUtils.toJson(AppPerDefaultGroupLangCode.PROJECT_MEMBER.getDefaultOptAuth().get()));
                            group.setTableAuth(GsonUtils.toJson(AppPerDefaultGroupLangCode.PROJECT_MEMBER.getDefaultTableAuth().get()));
                            group.setColumnAuth("{}");
                        }
                    }
                    permissionGroupList.addAll(groups);
                }
            }
        }

        // 过滤空group
        List<PerAppPermissionGroup> filterNullList = new ArrayList<>();
        for (PerAppPermissionGroup group: permissionGroupList) {
            if (group != null){
                filterNullList.add(group);
            }
        }
        permissionGroupList = filterNullList;

        AppAuthorityResp appAuthority = new AppAuthorityResp();
        appAuthority.setAppOwner(isProjectMember && Objects.equals(appResp.getCreator(), userId));
        appAuthority.setAppId(appId);
        appAuthority.setLangCode(appPermissionGroupMergeService.mergeLangCode(permissionGroupList));
        appAuthority.setSysAdmin(userPermissionVO.getIsSysAdmin());
        appAuthority.setOrgOwner(userPermissionVO.getIsOrgOwner());
        appAuthority.setSubAdmin(userPermissionVO.getIsSubAdmin());
        appAuthority.setCollaborator(appPermissionGroupMergeService.mergeIsCollaborator(permissionGroupList));
        appAuthority.setOutCollaborator(Objects.equals(userPermissionVO.getIsOutCollaborator(), true)); // userPermissionVO.getIsOutCollaborator() 有可能为null
        appAuthority.setAppAuth(userPermissionVO.getManageApps());
        appAuthority.setViewAuth(appPermissionGroupMergeService.mergeViewAuth(permissionGroupList));
        appAuthority.setDataAuth(appPermissionGroupMergeService.mergeDataAuth(permissionGroupList));
        appAuthority.setFieldAuth(appPermissionGroupMergeService.mergeFieldAuth(permissionGroupList));
        appAuthority.setOptAuth(appPermissionGroupMergeService.mergeOptAuth(permissionGroupList));
        appAuthority.setTableAuth(appPermissionGroupMergeService.mergeTableAuth(permissionGroupList));
        appAuthority.setHasAppRootPermission(appAuthority.hasAppRootPermission());

        // 返回当前用户所有的权限组
        List<AppPerGroupListItem> perGroups = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(permissionGroupList)){
            for (PerAppPermissionGroup permissionGroup: permissionGroupList){
                AppPerGroupListItem item = new AppPerGroupListItem();
                item.setId(permissionGroup.getId());
                item.setName(permissionGroup.getName());
                String groupLangCode = permissionGroup.getLangCode();
                if (Objects.equals(groupLangCode, AppPerDefaultGroupLangCode.DASHBOARD_ADMINISTRATOR.getCode())) {
                    groupLangCode = AppPerDefaultGroupLangCode.OWNER.getCode();
                }
                item.setLangCode(groupLangCode);
                item.setRemake(permissionGroup.getRemake());
                perGroups.add(item);
            }
        }
        appAuthority.setPerGroups(perGroups);

        // 如果是管理员
        // 包括普通管理员和组织管理员，任务模块不受管理员组选择管理应用的逻辑的影响
        if (appAuthority.hasAppRootPermission() || (appAuthority.isSubAdmin() && Objects.equals(appResp.getType(),AppType.SUMMARY.getCode()))){
            AppPerDefaultGroupLangCode langCode = AppPerDefaultGroupLangCode.FORM_ADMINISTRATOR;
            if (Objects.equals(appResp.getType(), AppType.PROJECT.getCode()) ||
                    (Objects.equals(appResp.getType(), AppType.DASHBOARD.getCode()) ||
                            !Objects.equals(appResp.getProjectId(), 0L))){
                langCode = AppPerDefaultGroupLangCode.OWNER;
            }
            AppPerGroupListItem item = new AppPerGroupListItem();
            item.setName(langCode.getName());
            item.setRemake(langCode.getDesc());
            item.setLangCode(langCode.getCode());
            appAuthority.setPerGroups(Collections.singletonList(item));

            appAuthority.setViewAuth(new HashMap<>());
            appAuthority.setFieldAuth(new HashMap<>());
            appAuthority.setOptAuth(new HashSet<>(langCode.getDefaultOptAuth().get()));
            appAuthority.setTableAuth(new HashSet<>(langCode.getDefaultTableAuth().get()));
        }
        return appAuthority;
    }

    public AppAuthorityResp getDataAuthority(Long orgId, Long appId, Long userId, Long dataId) {
        GetDataAuthorityBatchReq req = new GetDataAuthorityBatchReq();
        req.setOrgId(orgId);
        req.setUserId(userId);
        req.setAppId(appId);
        req.setDataIds(Collections.singletonList(dataId));
        return getDataAuthorityBatch(req).get(dataId);
    }

    private AppAuthorityResp mergePermissionGroups(Long appId, List<PerAppPermissionGroup> permissionGroupList){
        AppAuthorityResp appAuthority = new AppAuthorityResp();
        // 这里不关心组织权限及应用权限
        appAuthority.setAppOwner(false);
        appAuthority.setAppId(appId);
        appAuthority.setLangCode(null);
        appAuthority.setSysAdmin(false);
        appAuthority.setOrgOwner(false);
        appAuthority.setSubAdmin(false);
        appAuthority.setOutCollaborator(false);
        appAuthority.setHasAppRootPermission(false);
        appAuthority.setViewAuth(appPermissionGroupMergeService.mergeViewAuth(permissionGroupList));
        appAuthority.setDataAuth(appPermissionGroupMergeService.mergeDataAuth(permissionGroupList));
        appAuthority.setFieldAuth(appPermissionGroupMergeService.mergeFieldAuth(permissionGroupList));
        appAuthority.setOptAuth(appPermissionGroupMergeService.mergeOptAuth(permissionGroupList));
        appAuthority.setTableAuth(appPermissionGroupMergeService.mergeTableAuth(permissionGroupList));
        return appAuthority;
    }

    @Override
    public Map<Long, AppAuthorityResp> getDataAuthorityBatch(GetDataAuthorityBatchReq getDataAuthorityBatchReq) {
        UserPermissionVO userPermissionVO = userService.getUserPermission(getDataAuthorityBatchReq.getOrgId(), getDataAuthorityBatchReq.getUserId());
        Set<String> members = new HashSet<>();
        members.add("U_" + getDataAuthorityBatchReq.getUserId());
        members.add("D_0");
        if (CollectionUtils.isNotEmpty(userPermissionVO.getRefDeptIds())){
            for (Long deptId: userPermissionVO.getRefDeptIds()){
                members.add("D_" + deptId);
            }
        }
        if (CollectionUtils.isNotEmpty(userPermissionVO.getRefRoleIds())){
            for (Long roleId: userPermissionVO.getRefRoleIds()){
                members.add("R_" + roleId);
            }
        }

        ReadSummeryTableIdResp summeryTableResp = goTableService.readSummeryTableId(new ReadSummeryTableIdRequest(getDataAuthorityBatchReq.getOrgId()),
                getDataAuthorityBatchReq.getOrgId(), getDataAuthorityBatchReq.getUserId());

        String tableName = SqlUtil.wrapperTableName(getDataAuthorityBatchReq.getOrgId(), summeryTableResp.getTableId());
        Map<Long, AppAuthorityResp> results = new HashMap<>();
        List<Map<String, Object>> dataList = dataCenterApi.query(DataSourceUtil.getDsId(), DataSourceUtil.getDbId(),
                Query.select().from(new Table(tableName)).where(Conditions.in("id", getDataAuthorityBatchReq.getDataIds()
        ))).getData();
        if (CollectionUtils.isEmpty(dataList)){
            return results;
        }
        Object tableIdObject = dataList.get(0).get("tableId");
        long tableId = 0L;
        if (tableIdObject instanceof String && !"".equals(tableIdObject)) {
            tableId = Long.parseLong((String) tableIdObject);
        }

        Map<String, FieldParam> fieldParams = goTableService.readSchemaByAppId(getDataAuthorityBatchReq.getAppId(),
                getDataAuthorityBatchReq.getOrgId(),tableId,getDataAuthorityBatchReq.getUserId());

        Map<String, Long> fieldPermissionMap = new HashMap<>();
        for (Map.Entry<String, FieldParam> entry: fieldParams.entrySet()){
            Map<String, Object> props = entry.getValue().getField().getProps();
            if (Objects.equals(FieldTypeEnums.USER.getFormFieldType(), entry.getValue().getField().getType()) ||
                    Objects.equals(FieldTypeEnums.WORK_HOUR.getFormFieldType(), entry.getValue().getField().getType())) {
                if (! MapUtils.isEmpty(props) && props.containsKey("collaboratorRoles")){
                    Object collaboratorRolesObj = props.get("collaboratorRoles");
                    if (collaboratorRolesObj instanceof List && CollectionUtils.isNotEmpty((List<?>) collaboratorRolesObj)){
                        String permissionIdObj = String.valueOf(((List<?>) collaboratorRolesObj).get(0));
                        if (StringUtils.isNumeric(permissionIdObj) || Objects.equals(permissionIdObj, "-1")){
                            fieldPermissionMap.put(entry.getKey(), Long.parseLong(permissionIdObj));
                        }
                    }
                }
            }
        }
        if (MapUtils.isEmpty(fieldPermissionMap)){
            return results;
        }
        List<PerAppPermissionGroup> appPermissionGroups = appPermissionGroupMapper.selectList(new LambdaQueryWrapper<PerAppPermissionGroup>()
                .eq(PerAppPermissionGroup::getOrgId, getDataAuthorityBatchReq.getOrgId())
                .eq(PerAppPermissionGroup::getDelFlag, CommonConsts.FALSE)
                .eq(PerAppPermissionGroup::getAppId, getDataAuthorityBatchReq.getAppId())
//                .in(PerAppPermissionGroup::getId, fieldPermissionMap.values())
        );
        Map<Long, PerAppPermissionGroup> appPermissionGroupMap = MapUtils.toMap(PerAppPermissionGroup::getId, appPermissionGroups);
        for (PerAppPermissionGroup appPermissionGroup : appPermissionGroups) {
            if (appPermissionGroup.getLangCode().equals(AppPerDefaultGroupLangCode.PROJECT_MEMBER.getCode())) {
                //-1默认是编辑者
                appPermissionGroupMap.put(-1l, appPermissionGroup);
            }
        }

        Map<Long, List<Long>> dataPermissionMap = new HashMap<>();
        for (Map<String, Object> data: dataList){
            for (Map.Entry<String, Long> fieldPermission: fieldPermissionMap.entrySet()){
                String key = fieldPermission.getKey();
                Long permissionId = fieldPermission.getValue();
                Object value = data.get(key);
                List<Long> permissionIds = dataPermissionMap.computeIfAbsent(Long.parseLong(String.valueOf(data.get("id"))), k -> new ArrayList<>());
                if (value instanceof Collection){
                    if (((Collection<?>) value).stream().anyMatch(members::contains)){
                        permissionIds.add(permissionId);
                    }
                } else if (value instanceof Map) {
                    Map m = (Map) value;
                    if (m.containsKey("collaboratorIds")) {
                        Object ids = m.get("collaboratorIds");
                        if (ids instanceof Collection) {
                            if (((Collection<?>) ids).stream().anyMatch(members::contains)){
                                permissionIds.add(permissionId);
                            }
                        }
                    }
                }
            }
        }

        for (Map.Entry<Long, List<Long>> dataPermission: dataPermissionMap.entrySet()){
            List<PerAppPermissionGroup> groups = new ArrayList<>();
            for (Long perId: dataPermission.getValue()){
                groups.add(appPermissionGroupMap.get(perId));
            }
            results.put(dataPermission.getKey(), mergePermissionGroups(getDataAuthorityBatchReq.getAppId(), groups));
        }
        return results;
    }

    @Override
    public Map<Long, AppAuthorityResp> getAppAuthorityBatch(GetAppAuthorityBatchReq req) {
        Map<Long, AppAuthorityResp> results = new HashMap<>();
        if (CollectionUtils.isEmpty(req.getAppIds())) {
            return new HashMap<>();
        }
        List<AppResp> appRespList;
        if (req.getAppIds().size() > 0) {
            appRespList = appService.getAppList(req.getOrgId(), req.getAppIds());
        } else {
            appRespList = appService.getAppList(req.getOrgId());
        }
        if (CollectionUtils.isEmpty(appRespList)) {
            return new HashMap<>();
        }
        Map<Long, Long> appProjectIds = new HashMap<>();
        List<Long> appIds = new ArrayList<>();
        for (AppResp appResp: appRespList) {
            appIds.add(appResp.getId());
            if (appResp.getProjectId() > 0) {
                appProjectIds.put(appResp.getProjectId(), appResp.getId());
            }
        }

        UserPermissionVO userPermissionVO = userService.getUserPermission(req.getOrgId(), req.getUserId());
        List<PerAppPermissionGroup> permissionGroupLists = getAppPermissionGroupsByUserAndApps(req.getOrgId(), req.getAppIds(),
                req.getUserId(), userPermissionVO.getRefDeptIds(),
                userPermissionVO.getRefRoleIds());
        Map<Long, List<PerAppPermissionGroup>> permissionGroupListMap = permissionGroupLists.stream().collect(Collectors.groupingBy(PerAppPermissionGroup::getAppId));

        List<PerAppPermissionGroup> groupList = getAppPermissionGroupListByAppIds(req.getOrgId(), req.getAppIds());
        Map<Long, List<PerAppPermissionGroup>> groupListMap = groupList.stream().collect(Collectors.groupingBy(PerAppPermissionGroup::getAppId));

        IsProjectMemberBatchReq isProjectMemberBatchReq = new IsProjectMemberBatchReq();
        isProjectMemberBatchReq.setAppIds(appIds);
        isProjectMemberBatchReq.setOrgId(req.getOrgId());
        isProjectMemberBatchReq.setUserId(req.getUserId());
        isProjectMemberBatchReq.setRefDeptIds(userPermissionVO.getRefDeptIds());
        isProjectMemberBatchReq.setRefRoleIds(userPermissionVO.getRefRoleIds());
        isProjectMemberBatchReq.setAppProjectIds(appProjectIds);
        Map<Long, Boolean> isProjectMembers = appProvider.isProjectMemberBatch(isProjectMemberBatchReq).getData();

        for (AppResp appResp: appRespList) {
            Long appId = appResp.getId();
            // 获取继承的应用权限
            AppResp extendsAppResp = appProvider.getAuthExtendsApp(appId).getData();
            if (extendsAppResp == null) {
                throw new BusinessException(ResultCode.APP_NOT_EXIST);
            }
            boolean isExtends = ! Objects.equals(extendsAppResp.getId(), appId);
            appId = extendsAppResp.getId();

            List<PerAppPermissionGroup> permissionGroupList = permissionGroupListMap.get(appResp.getId());
            if (isExtends && CollectionUtils.isNotEmpty(permissionGroupList)) {
                permissionGroupList.removeIf(per -> Objects.isNull(AppPerDefaultGroupLangCode.forValue(per.getLangCode())));
            }

            boolean isProjectMember = false;
            if (isProjectMembers.containsKey(appResp.getId())) {
                isProjectMember = isProjectMembers.get(appResp.getId());
            }
            if (!isExtends) {
                if (CollectionUtils.isEmpty(permissionGroupList) && isProjectMember) {
                    List<PerAppPermissionGroup> groups = groupListMap.get(appResp.getId());
                    if (CollectionUtils.isNotEmpty(groups)) {
                        Map<String, PerAppPermissionGroup> groupMap = MapUtils.toMap(PerAppPermissionGroup::getLangCode, groups);
                        if (Objects.equals(appResp.getType(), AppType.PROJECT.getCode())) {
                            permissionGroupList = Collections.singletonList(groupMap.get(AppPerDefaultGroupLangCode.PROJECT_MEMBER.getCode()));
                        } else {
                            permissionGroupList = Collections.singletonList(groupMap.get(AppPerDefaultGroupLangCode.READ.getCode()));
                        }
                    }
                }
            }

            AppAuthorityResp appAuthority = new AppAuthorityResp();
            appAuthority.setAppOwner(isProjectMember && Objects.equals(appResp.getCreator(), req.getUserId()));
            appAuthority.setAppId(appId);
            appAuthority.setLangCode(appPermissionGroupMergeService.mergeLangCode(permissionGroupList));
            appAuthority.setSysAdmin(userPermissionVO.getIsSysAdmin());
            appAuthority.setOrgOwner(userPermissionVO.getIsOrgOwner());
            appAuthority.setSubAdmin(userPermissionVO.getIsSubAdmin());
            appAuthority.setOutCollaborator(Objects.equals(userPermissionVO.getIsOutCollaborator(), true)); // userPermissionVO.getIsOutCollaborator() 有可能为null
            appAuthority.setAppAuth(userPermissionVO.getManageApps());
            appAuthority.setViewAuth(appPermissionGroupMergeService.mergeViewAuth(permissionGroupList));
            appAuthority.setDataAuth(appPermissionGroupMergeService.mergeDataAuth(permissionGroupList));
            appAuthority.setFieldAuth(appPermissionGroupMergeService.mergeFieldAuth(permissionGroupList));
            appAuthority.setOptAuth(appPermissionGroupMergeService.mergeOptAuth(permissionGroupList));
            appAuthority.setTableAuth(appPermissionGroupMergeService.mergeTableAuth(permissionGroupList));
            appAuthority.setHasAppRootPermission(appAuthority.hasAppRootPermission());

            // 返回当前用户所有的权限组
            List<AppPerGroupListItem> perGroups = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(permissionGroupList)) {
                for (PerAppPermissionGroup permissionGroup: permissionGroupList) {
                    AppPerGroupListItem item = new AppPerGroupListItem();
                    item.setId(permissionGroup.getId());
                    item.setName(permissionGroup.getName());
                    item.setLangCode(permissionGroup.getLangCode());
                    item.setRemake(permissionGroup.getRemake());
                    perGroups.add(item);
                }
            }
            appAuthority.setPerGroups(perGroups);

            // 如果是管理员
            if (appAuthority.hasAppRootPermission()) {
                AppPerDefaultGroupLangCode langCode = AppPerDefaultGroupLangCode.FORM_ADMINISTRATOR;
                if (Objects.equals(appResp.getType(), AppType.PROJECT.getCode())) {
                    langCode = AppPerDefaultGroupLangCode.OWNER;
                }
                AppPerGroupListItem item = new AppPerGroupListItem();
                item.setName(langCode.getName());
                item.setRemake(langCode.getDesc());
                item.setLangCode(langCode.getCode());
                appAuthority.setPerGroups(Collections.singletonList(item));
                appAuthority.setViewAuth(new HashMap<>());
                appAuthority.setFieldAuth(new HashMap<>());
                appAuthority.setOptAuth(new HashSet<>(langCode.getDefaultOptAuth().get()));
                appAuthority.setTableAuth(new HashSet<>(langCode.getDefaultTableAuth().get()));
            }
            results.put(appResp.getId(), appAuthority);
        }
        return results;
    }

    @Override
    public Map<Long, AppAuthorityResp> getDataAuthorities(GetDataAuthorityBatchReq getDataAuthorityBatchReq) {
        // 获取目标应用id
        AppResp appResp = appProvider.getAppInfo(getDataAuthorityBatchReq.getOrgId(), getDataAuthorityBatchReq.getAppId()).getData();
        if (appResp == null){
            throw new BusinessException(PermissionResultCode.APP_NOT_EXIST_OR_DELETED);
        }
        Long targetAppId = appResp.getId();
        if (appResp.getExtendsId() != null && appResp.getExtendsId() > 0){
            targetAppId = appResp.getExtendsId();
        }
        // 查询目标应用表单
//        AppFormResp appFormResp = appFormApi.getFormByAppId(targetAppId).getData();
        dataCenterApi.query(DataSourceUtil.getDsId(), DataSourceUtil.getDbId(), Query.select());

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPermissionGroup(Long orgId, Long operatorId, Long appId, SaveAppPermissionGroupReq req) throws BusinessException {
        log.info("[新增应用权限组]-> orgId:{}   operatorId:{}   appId:{}   req:{}", orgId, operatorId, appId, req);
        AppAuthorityResp appAuthorityResp = getAppAuthority(orgId, appId, null, operatorId, true, null);
        if(! appAuthorityResp.hasAppRootPermission() && ! appAuthorityResp.hasAppOptAuth(OperateAuthCode.PERMISSION_PRO_ROLE_CREATE.getCode())){
            throw new BusinessException(BaseResultCode.FORBIDDEN_ACCESS);
        }

        // 获取应用
        AppResp appResp = appService.getApp(orgId, appId);
        if (Objects.isNull(appResp)) {
            throw new BusinessException(PermissionResultCode.APP_NOT_EXIST_OR_DELETED);
        }
        // 确定要使用的应用权限基础配置
        PerAppPermissionConfig permissionConfig = getAppPermissionConfig(orgId, appId, appResp.getType());
        if (Objects.isNull(permissionConfig)) {
            throw new BusinessException(PermissionResultCode.PER_APP_BASE_CONFIG_NOT_MATCHED);
        }

        // 暂不校验配置矛盾
        // checkAndFilterParam(req, permissionConfig);

        PerAppPermissionGroup permissionGroup = new PerAppPermissionGroup();
        // 创建的为自定义类型
        permissionGroup.setLangCode("");
        permissionGroup.setName(req.getName());
        permissionGroup.setRemake(req.getRemake());
        permissionGroup.setOptAuth(JsonUtils.toJson(req.getOptAuth()));
        permissionGroup.setTableAuth(JsonUtils.toJson(req.getTableAuth()));
        permissionGroup.setColumnAuth(JsonUtils.toJson(req.getFieldAuth()));
        permissionGroup.setDataAuth(JsonUtils.toJson(req.getDataAuth()));
        permissionGroup.setViewAuth(MapUtils.isEmpty(req.getViewAuth()) ? "{}" : JsonUtils.toJson(req.getViewAuth()));
        permissionGroup.setOrgId(orgId);
        permissionGroup.setAppId(appId);
        permissionGroup.setCreator(operatorId);
        permissionGroup.setUpdator(operatorId);
        baseMapper.insert(permissionGroup);

        PerAppPermissionMember newPermissionMember = new PerAppPermissionMember();
        newPermissionMember.setOrgId(orgId);
        newPermissionMember.setAppId(appId);
        newPermissionMember.setPermissionGroupId(permissionGroup.getId());
        newPermissionMember.setUserIds(PermissionConstant.EMPTY_ARRAY_JSON);
        newPermissionMember.setDeptIds(PermissionConstant.EMPTY_ARRAY_JSON);
        newPermissionMember.setRoleIds(PermissionConstant.EMPTY_ARRAY_JSON);
        newPermissionMember.setCreator(operatorId);
        newPermissionMember.setUpdator(operatorId);
        permissionMemberMapper.insert(newPermissionMember);

        // MQTT 推送
        ThreadPools.POOLS.execute(() -> {
            PerAppPermissionGroup perGroup = getAssertNotDeleteGroup(orgId, appId, permissionGroup.getId());
            PerGroupEvent groupEvent = new PerGroupEvent();
            groupEvent.setOrgId(orgId);
            groupEvent.setAppId(appId);
            groupEvent.setProjectId(appResp.getProjectId());
            groupEvent.setUserId(operatorId);
            groupEvent.setGroupId(perGroup.getId());
            groupEvent.setNewData(convertPerGroupToResp(perGroup));
            Event event = new Event();
            event.setCategory(PermissionConstant.EventCategoryPerGroup);
            event.setType(PermissionConstant.EventTypePerGroupRefresh);
            event.setTimestamp(System.currentTimeMillis() * 1000000L);
            event.setPayload(groupEvent);
            try {
                goPushService.pushMqtt(orgId, appResp.getProjectId(), event);
            } catch (JsonProcessingException e) {
            }
        });

        return permissionGroup.getId();
    }


    @Override
    public boolean updatePermissionGroup(Long orgId, Long operatorId, Long appId, Long groupId,
                                         SaveAppPermissionGroupReq req) throws BusinessException {
        log.info("[修改应用权限组信息]-> orgId:{} operatorId:{} appId:{} groupId:{} req:{}", orgId,
                operatorId, appId, groupId, req);

        AppAuthorityResp appAuthorityResp = getAppAuthority(orgId, appId, null, operatorId, true, null);
        if (! appAuthorityResp.hasAppRootPermission()
                && ! appAuthorityResp.hasAppOptAuth(OperateAuthCode.PERMISSION_PRO_ROLE_MODIFY.getCode())
                && ! appAuthorityResp.hasAppOptAuth(OperateAuthCode.PERMISSION_PRO_ROLE_MODIFYPERMISSION.getCode())) {
            throw new BusinessException(BaseResultCode.FORBIDDEN_ACCESS);
        }

        // 获取应用
        AppResp appResp = appService.getApp(orgId, appId);
        if (Objects.isNull(appResp)) {
            throw new BusinessException(PermissionResultCode.APP_NOT_EXIST_OR_DELETED);
        }
        PerAppPermissionConfig permissionConfig = getAppPermissionConfig(orgId, appId, appResp.getType());
        if (Objects.isNull(permissionConfig)) {
            throw new BusinessException(PermissionResultCode.PER_APP_BASE_CONFIG_NOT_MATCHED);
        }

        PerAppPermissionGroup permissionGroup = getAssertNotDeleteGroup(orgId, appId, groupId);
        if (Objects.equals(permissionGroup.getReadOnly(), PermissionConstant.YES)) {
            throw new BusinessException(PermissionResultCode.PER_APP_READ_ONLY);
        }
//        checkAndFilterParam(req, permissionConfig);

        if (appAuthorityResp.hasAppRootPermission() || appAuthorityResp.hasAppOptAuth(OperateAuthCode.PERMISSION_PRO_ROLE_MODIFY.getCode())) {
            permissionGroup.setName(req.getName());
            permissionGroup.setRemake(req.getRemake());
        }
        if (appAuthorityResp.hasAppRootPermission() || appAuthorityResp.hasAppOptAuth(OperateAuthCode.PERMISSION_PRO_ROLE_MODIFYPERMISSION.getCode())) {
            permissionGroup.setOptAuth(JsonUtils.toJson(req.getOptAuth()));
            permissionGroup.setTableAuth(JsonUtils.toJson(req.getTableAuth()));
            permissionGroup.setColumnAuth(JsonUtils.toJson(req.getFieldAuth()));
            permissionGroup.setDataAuth(JsonUtils.toJson(req.getDataAuth()));
            permissionGroup.setViewAuth(MapUtils.isEmpty(req.getViewAuth()) ? "{}" : JsonUtils.toJson(req.getViewAuth()));
        }
        permissionGroup.setUpdator(operatorId);

        boolean succ = baseMapper.updateById(permissionGroup) > 0;
        if (succ) {
            // MQTT 推送
            ThreadPools.POOLS.execute(() -> {
                PerGroupEvent groupEvent = new PerGroupEvent();
                groupEvent.setOrgId(orgId);
                groupEvent.setAppId(appId);
                groupEvent.setProjectId(appResp.getProjectId());
                groupEvent.setUserId(operatorId);
                groupEvent.setGroupId(permissionGroup.getId());
                groupEvent.setNewData(convertPerGroupToResp(permissionGroup));
                Event event = new Event();
                event.setCategory(PermissionConstant.EventCategoryPerGroup);
                event.setType(PermissionConstant.EventTypePerGroupRefresh);
                event.setTimestamp(System.currentTimeMillis() * 1000000L);
                event.setPayload(groupEvent);
                try {
                    goPushService.pushMqtt(orgId, appResp.getProjectId(), event);
                } catch (JsonProcessingException e) {
                }
            });
        }
        return succ;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermissionGroup(Long orgId, Long operatorId, Long appId, Long groupId)
            throws BusinessException {
        log.info("[删除应用权限组]-> orgId:{} operatorId:{} appId:{} groupId:{}", orgId, operatorId, appId, groupId);

        AppAuthorityResp appAuthorityResp = getAppAuthority(orgId, appId, null, operatorId, true, null);
        if (! appAuthorityResp.hasAppRootPermission() && ! appAuthorityResp.hasAppOptAuth(OperateAuthCode.PERMISSION_PRO_ROLE_DELETE.getCode())) {
            throw new BusinessException(BaseResultCode.FORBIDDEN_ACCESS);
        }

        PerAppPermissionGroup permissionGroup = getAssertNotDeleteGroup(orgId, appId, groupId);
        if (Objects.equals(permissionGroup.getReadOnly(), PermissionConstant.YES)) {
            throw new BusinessException(PermissionResultCode.PER_APP_READ_ONLY);
        }

        // 先删除权限组成员关系
        permissionMemberMapper.delete(new LambdaUpdateWrapper<PerAppPermissionMember>().eq(
                PerAppPermissionMember::getPermissionGroupId, permissionGroup.getId()));
        // 删除权限组
        boolean succ = removeById(permissionGroup);
        if (succ) {
            // MQTT 推送
            ThreadPools.POOLS.execute(() -> {
                AppResp appResp = appService.getApp(orgId, appId);
                if (Objects.isNull(appResp)) {
                    throw new BusinessException(PermissionResultCode.APP_NOT_EXIST_OR_DELETED);
                }
                PerGroupEvent groupEvent = new PerGroupEvent();
                groupEvent.setOrgId(orgId);
                groupEvent.setAppId(appId);
                groupEvent.setProjectId(appResp.getProjectId());
                groupEvent.setUserId(operatorId);
                groupEvent.setGroupId(groupId);
                Event event = new Event();
                event.setCategory(PermissionConstant.EventCategoryPerGroup);
                event.setType(PermissionConstant.EventTypePerGroupDeleted);
                event.setTimestamp(System.currentTimeMillis() * 1000000L);
                event.setPayload(groupEvent);
                try {
                    goPushService.pushMqtt(orgId, appResp.getProjectId(), event);
                } catch (JsonProcessingException e) {
                }
            });
        }
        return succ;
    }

    /**
     * 校验数据权限
     *
     * @param dataAuth 待验证数据权限
     * @throws BusinessException {@link ResultCode#PARAM_ERROR} 参数错误
     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_REF_INVALID} 条件关联方式无效
     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_METHOD_INVALID} 过滤方法无效
     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_DATA_TYPE_INVALID} 字段类型无效
     */
    private void checkDataAuth(DataAuthBo dataAuth) throws BusinessException {
        if (Objects.isNull(dataAuth) || Objects.isNull(dataAuth.getRel())) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        // 条件关联方式
        if (Objects.isNull(ConditionRelationMode.forValue(dataAuth.getRel()))) {
            throw new BusinessException(PermissionResultCode.PER_APP_DATA_AUTH_REF_INVALID);
        }
        dataAuth.getCond().forEach(cond -> {
            // 过滤方法
            if (Objects.isNull(FieldFilterMethod.forValue(cond.getMethod()))) {
                throw new BusinessException(PermissionResultCode.PER_APP_DATA_AUTH_METHOD_INVALID);
            }
            // 字段类型
            if (Objects.isNull(FieldTypeEnums.formatOrNull(cond.getType()))) {
                throw new BusinessException(PermissionResultCode.PER_APP_DATA_AUTH_DATA_TYPE_INVALID);
            }
        });
    }

    /**
     * 获取成员
     *
     * @param members 待验证成员
     * @throws BusinessException {@link PermissionResultCode#PER_APP_MEMBER_CODE_INVALID}  成员类型无效
     */
    private Map<String, List<Long>> getPermissionMembersMap(List<PermissionMembersItemBo> members)
            throws BusinessException {
        Map<String, List<Long>> map = new HashMap<>();
        members.forEach(e -> {
            if (Objects.isNull(PermissionMemberCode.forValue(e.getK()))) {
                throw new BusinessException(PermissionResultCode.PER_APP_MEMBER_CODE_INVALID);
            }
            map.computeIfAbsent(e.getK(), k -> new ArrayList<>()).add(e.getV());
        });
        return map;
    }

    @Override
    public PerAppPermissionGroup getAssertNotDeleteGroup(Long orgId, Long appId, Long perGroupId) throws BusinessException {
        PerAppPermissionGroup group = baseMapper.selectOne(new LambdaQueryWrapper<PerAppPermissionGroup>()
                .eq(PerAppPermissionGroup::getOrgId, orgId)
                .eq(PerAppPermissionGroup::getAppId, appId)
                .eq(PerAppPermissionGroup::getId, perGroupId)
        );
        if (Objects.isNull(group)) {
            throw new BusinessException(PermissionResultCode.PER_APP_NOT_EXIST);
        }
        return group;
    }

    @Override
    public List<PerAppPermissionGroup> getAssertNotDeleteGroups(Long orgId, Long appId, List<Long> perGroupIds) throws BusinessException {
        List<PerAppPermissionGroup> groups = baseMapper.selectList(new LambdaQueryWrapper<PerAppPermissionGroup>()
                .eq(PerAppPermissionGroup::getOrgId, orgId)
                .eq(PerAppPermissionGroup::getAppId, appId)
                .in(PerAppPermissionGroup::getId, perGroupIds)
        );
        if (Objects.isNull(groups) || groups.size() < perGroupIds.size()) {
            throw new BusinessException(PermissionResultCode.PER_APP_NOT_EXIST);
        }
        return groups;
    }

    @Override
    public List<PerAppPermissionGroup> getAssertNotDeleteGroups(Long orgId, Long appId) throws BusinessException {
        List<PerAppPermissionGroup> groups = baseMapper.selectList(new LambdaQueryWrapper<PerAppPermissionGroup>()
                .eq(PerAppPermissionGroup::getOrgId, orgId)
                .eq(PerAppPermissionGroup::getAppId, appId)
        );
        return groups;
    }

    @Override
    public boolean memberSwitchGroup(Long orgId, Long operatorId, Long appId, Long groupId, MemberSwitchGroupReq req) throws BusinessException {
        AppAuthorityResp appAuthorityResp = getAppAuthority(orgId, appId, null, operatorId, true, null);
        if (! appAuthorityResp.hasAppRootPermission() && ! appAuthorityResp.hasAppOptAuth(OperateAuthCode.PERMISSION_PRO_MEMBER_MODIFY.getCode())) {
            throw new BusinessException(BaseResultCode.FORBIDDEN_ACCESS);
        }

        Map<Long, PerAppPermissionGroup> groups = MapUtils.toMap(PerAppPermissionGroup::getId, getAppPermissionGroupList(orgId, appId));
        List<PerAppPermissionMember> permissionMemberList = getPermissionGroupMemberList(orgId, appId);
        List<PerAppPermissionMember> updatedPermissionMemberList = new ArrayList<>();
        boolean addOwner = false;
        boolean removeOwner = false;
        // 解除已有的绑定
        if (CollectionUtils.isNotEmpty(permissionMemberList)) {
            for (PerAppPermissionMember member: permissionMemberList) {
                String idsJson = "";
                if (Objects.equals(req.getMember().getK(), "U")) {
                    idsJson = member.getUserIds();
                } else if (Objects.equals(req.getMember().getK(), "D")) {
                    idsJson = member.getDeptIds();
                } else {
                    idsJson = member.getRoleIds();
                }
                // 如果为要切换的组，则加入当前成员
                if (Objects.equals(member.getPermissionGroupId(), groupId)) {
                    PerAppPermissionGroup currentGroup = groups.get(groupId);
                    if (currentGroup != null && Objects.equals(currentGroup.getLangCode(), AppPerDefaultGroupLangCode.OWNER.getCode())) {
                        addOwner = true;
                    }
                    if (StringUtils.isBlank(idsJson)) {
                        idsJson = JSON.toJSONString(Collections.singleton(req.getMember().getV()));
                    } else {
                        Set<Long> list = new HashSet<>(JSON.parseArray(idsJson, Long.class));
                        list.add(req.getMember().getV());
                        idsJson = JSON.toJSONString(list);
                    }
                    PerAppPermissionMember updated = new PerAppPermissionMember();
                    updated.setId(member.getId());
                    if (Objects.equals(req.getMember().getK(), "U")) {
                        updated.setUserIds(idsJson);
                        member.setUserIds(updated.getUserIds());
                    } else if (Objects.equals(req.getMember().getK(), "D")) {
                        updated.setDeptIds(idsJson);
                        member.setDeptIds(updated.getDeptIds());
                    } else {
                        updated.setRoleIds(idsJson);
                        member.setRoleIds(updated.getRoleIds());
                    }
                    permissionMemberMapper.updateById(updated);
                    updatedPermissionMemberList.add(member);
                } else {
                    if (! StringUtils.isBlank(idsJson)) {
                        List<Long> list = JSON.parseArray(idsJson, Long.class);
                        if (list.contains(req.getMember().getV())) {
                            PerAppPermissionGroup oldGroup = groups.get(member.getPermissionGroupId());
                            if (oldGroup != null && Objects.equals(oldGroup.getLangCode(), AppPerDefaultGroupLangCode.OWNER.getCode())) {
                                removeOwner = true;
                            }
                            list.remove(req.getMember().getV());
                            PerAppPermissionMember updated = new PerAppPermissionMember();
                            updated.setId(member.getId());
                            if (Objects.equals(req.getMember().getK(), "U")) {
                                updated.setUserIds(JSON.toJSONString(list));
                                member.setUserIds(updated.getUserIds());
                            } else if (Objects.equals(req.getMember().getK(), "D")) {
                                updated.setDeptIds(JSON.toJSONString(list));
                                member.setDeptIds(updated.getDeptIds());
                            } else {
                                updated.setRoleIds(JSON.toJSONString(list));
                                member.setRoleIds(updated.getRoleIds());
                            }
                            permissionMemberMapper.updateById(updated);
                            updatedPermissionMemberList.add(member);
                        }
                    }
                }
            }
        }

        // 只针对用户
        if ((addOwner && removeOwner) || ! Objects.equals(req.getMember().getK(), "U")) {
            addOwner = false;
            removeOwner = false;
        }
        if (addOwner) {
            appProvider.addProjectMember(appId, 1, req.getMember().getV());
        } else if (removeOwner) {
            appProvider.removeProjectMember(appId, 1, req.getMember().getV());
            appProvider.addProjectMember(appId, 2, req.getMember().getV());
        }

        if (updatedPermissionMemberList.size() > 0) {
            // MQTT 推送
            ThreadPools.POOLS.execute(() -> {
                AppResp appResp = appService.getApp(orgId, appId);
                if (Objects.isNull(appResp)) {
                    throw new BusinessException(PermissionResultCode.APP_NOT_EXIST_OR_DELETED);
                }
                for (PerAppPermissionMember member : updatedPermissionMemberList) {
                    PerGroupEvent groupEvent = new PerGroupEvent();
                    groupEvent.setOrgId(orgId);
                    groupEvent.setAppId(appId);
                    groupEvent.setProjectId(appResp.getProjectId());
                    groupEvent.setUserId(operatorId);
                    groupEvent.setGroupId(member.getPermissionGroupId());
                    groupEvent.setNewData(member);
                    Event event = new Event();
                    event.setCategory(PermissionConstant.EventCategoryPerGroup);
                    event.setType(PermissionConstant.EventTypePerGroupMemberChanged);
                    event.setTimestamp(System.currentTimeMillis() * 1000000L);
                    event.setPayload(groupEvent);
                    try {
                        goPushService.pushMqtt(orgId, appResp.getProjectId(), event);
                    } catch (JsonProcessingException e) {
                    }
                }
            });
        }

        updatePermissionTime(appId);

        return true;
    }

    @Override
    public MemberGroupMappingsResp memberGroupMappings(Long orgId, Long operatorId, Long appId) {
        List<PerAppPermissionGroup> groups = getAppPermissionGroupList(orgId, appId);
        Map<Long, AppPerGroupListItem> groupItems = new HashMap<>();
        if (CollectionUtils.isNotEmpty(groups)){
            for (PerAppPermissionGroup group : groups) {
                AppPerGroupListItem item = new AppPerGroupListItem();
                item.setId(group.getId());
                item.setLangCode(group.getLangCode());
                item.setName(group.getName());
                item.setRemake(group.getRemake());
                item.setReadOnly(group.getReadOnly());
                groupItems.put(group.getId(), item);
            }
        }
        Map<Long, List<AppPerGroupListItem>> userGroupMappings = new HashMap<>();
        Map<Long, List<AppPerGroupListItem>> deptGroupMappings = new HashMap<>();
        Map<Long, List<AppPerGroupListItem>> roleGroupMappings = new HashMap<>();

        List<PerAppPermissionMember> permissionMemberList = getPermissionGroupMemberList(orgId, appId);
        if (CollectionUtils.isNotEmpty(permissionMemberList)){
            for (PerAppPermissionMember member: permissionMemberList){
                assemblyMappings(member.getUserIds(), member.getPermissionGroupId(), groupItems, userGroupMappings);
                assemblyMappings(member.getDeptIds(), member.getPermissionGroupId(), groupItems, deptGroupMappings);
                assemblyMappings(member.getRoleIds(), member.getPermissionGroupId(), groupItems, roleGroupMappings);
            }
        }
        MemberGroupMappingsResp resp = new MemberGroupMappingsResp();
        resp.setUserGroupMappings(userGroupMappings);
        resp.setDeptGroupMappings(deptGroupMappings);
        resp.setRoleGroupMappings(roleGroupMappings);
        return resp;
    }

    private void assemblyMappings(String idsJson, Long groupId, Map<Long, AppPerGroupListItem> groupItems, Map<Long, List<AppPerGroupListItem>> mappings){
        AppPerGroupListItem groupItem = groupItems.get(groupId);
        if (groupItem == null){
            return;
        }
        if (StringUtils.isNotBlank(idsJson)){
            List<Long> list = JSON.parseArray(idsJson, Long.class);
            if (CollectionUtils.isNotEmpty(list)){
                for (Long id: list){
                    List<AppPerGroupListItem> items = mappings.computeIfAbsent(id, k -> new ArrayList<>());
                    items.add(groupItem);
                }
            }
        }
    }

    @Override
    public PerAppPermissionMember getPermissionGroupMemberByGroupId(Long orgId, Long appId,
                                                                    Long groupId) {
        return permissionMemberMapper.selectOne(
                new LambdaQueryWrapper<PerAppPermissionMember>().eq(PerAppPermissionMember::getOrgId, orgId)
                        .eq(PerAppPermissionMember::getAppId, appId)
                        .eq(PerAppPermissionMember::getPermissionGroupId,
                                groupId));
    }

    @Override
    public List<PerAppPermissionMember> getPermissionGroupMemberList(Long orgId, Long appId) {
        return permissionMemberMapper.selectList(
                new LambdaQueryWrapper<PerAppPermissionMember>().eq(PerAppPermissionMember::getOrgId, orgId)
                        .eq(PerAppPermissionMember::getAppId, appId));
    }

    @Override
    public List<PerAppPermissionGroup> getAppPermissionGroupList(Long orgId, Long appId) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<PerAppPermissionGroup>().eq(PerAppPermissionGroup::getOrgId, orgId)
                        .eq(PerAppPermissionGroup::getAppId, appId).eq(PerAppPermissionGroup::getDelFlag, CommonConsts.FALSE));
    }

    public List<PerAppPermissionGroup> getAppPermissionGroupListByAppIds(Long orgId, List<Long> appIds) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<PerAppPermissionGroup>().eq(PerAppPermissionGroup::getOrgId, orgId)
                        .in(PerAppPermissionGroup::getAppId, appIds).eq(PerAppPermissionGroup::getDelFlag, CommonConsts.FALSE));
    }

//    @Override
//    public List<PerAppPermissionGroup> getAppPermissionGroupList(Long orgId, Long appId, String name) {
//        return baseMapper.selectList(
//                new LambdaQueryWrapper<PerAppPermissionGroup>()
//                        .eq(PerAppPermissionGroup::getOrgId, orgId)
//                        .eq(PerAppPermissionGroup::getAppId, appId)
//                        .like(StringUtils.isNotBlank(name), PerAppPermissionGroup::getName, name));
//    }

    public List<PerAppPermissionGroup> getAppPermissionGroupList(Long orgId, Long appId, List<Long> groupIds) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<PerAppPermissionGroup>()
                        .eq(PerAppPermissionGroup::getOrgId, orgId)
                        .eq(PerAppPermissionGroup::getAppId, appId)
                        .in(PerAppPermissionGroup::getId, groupIds)
                        .eq(PerAppPermissionGroup::getDelFlag, CommonConsts.FALSE));
    }

    @Override
    public AppPerGroupListResp getPermissionGroupList(Long orgId, Long operatorId, Long appId) {
        OrgUserPermissionContext context = userService.getPermissionContext(orgId, operatorId);

        AppPerGroupListResp resp = new AppPerGroupListResp();
        List<PerAppPermissionGroup> groups = getAppPermissionGroupList(orgId, appId);

        List<AppPerGroupListItem> listItems = getPermissionGroupList(orgId, appId, groups, context);
        resp.setList(listItems);
        resp.setHasCreate(context.hasCreateAppPackagePermission());
        return resp;
    }

    private List<AppPerGroupListItem> getPermissionGroupList(Long orgId, Long appId,
                                                             List<PerAppPermissionGroup> groups, OrgUserPermissionContext context) {
        List<PerAppPermissionMember> permissionMemberList = getPermissionGroupMemberList(orgId,
                appId);

        Map<Long, PerAppPermissionMember> permissionMemberMap = permissionMemberList.stream()
                .collect(Collectors.toMap(
                        PerAppPermissionMember::getPermissionGroupId,
                        v -> v));

        List<AppPerGroupListItem> respList = new ArrayList<>(groups.size());
        for (PerAppPermissionGroup group : groups) {
            AppPerGroupListItem item = new AppPerGroupListItem();
            item.setId(group.getId());
            item.setLangCode(group.getLangCode());
            item.setName(group.getName());
            item.setRemake(group.getRemake());

            item.setReadOnly(group.getReadOnly());
            item.setHasEdit(context.hasManageAppPermission(appId));
            item.setHasDelete(context.hasDeleteAppPermission(appId));
            respList.add(item);
        }
        // 设置成员信息
        wrapMembers(orgId, respList, permissionMemberMap);
        return respList;
    }

    private void wrapMembers(Long orgId, List<AppPerGroupListItem> respList, Map<Long, PerAppPermissionMember> permissionMemberMap) {
        Map<Long, Map<String, List<Long>>> memberIdsMap = new HashMap<>();
        for (AppPerGroupListItem item : respList) {
            PerAppPermissionMember permissionMember = permissionMemberMap.get(item.getId());
            if (Objects.isNull(permissionMember)) {
                continue;
            }
            Map<String, List<Long>> map = new HashMap<>();
            if (Objects.nonNull(permissionMember.getUserIds())) {
                map.put(PermissionMemberCode.USER.getCode(), JsonUtils.fromJsonArray(permissionMember.getUserIds(), Long.class));
            }
            if (Objects.nonNull(permissionMember.getDeptIds())) {
                map.put(PermissionMemberCode.DEPT.getCode(), JsonUtils.fromJsonArray(permissionMember.getDeptIds(), Long.class));
            }
            if (Objects.nonNull(permissionMember.getRoleIds())) {
                map.put(PermissionMemberCode.ROLE.getCode(), JsonUtils.fromJsonArray(permissionMember.getRoleIds(), Long.class));
            }
            memberIdsMap.put(item.getId(), map);
        }

        Map<Long, PermissionMembersBo> permissionMembersMap = PerMemberUtils.convertToMembersBo(userService, orgId, memberIdsMap);

        respList.forEach(group -> group.setMembers(permissionMembersMap.getOrDefault(group.getId(), new PermissionMembersBo())));
    }

    public AppPerGroupInfoResp convertPerGroupToResp(PerAppPermissionGroup group) {
        AppPerGroupInfoResp resp = new AppPerGroupInfoResp();
        resp.setId(group.getId());
        resp.setLangCode(group.getLangCode());
        resp.setName(group.getName());
        resp.setRemake(group.getRemake());
        resp.setOptAuth(JsonUtils.fromJsonArray(StringUtils.isBlank(group.getOptAuth()) ? "[]": group.getOptAuth(), String.class));
        resp.setTableAuth(JsonUtils.fromJsonArray(StringUtils.isBlank(group.getTableAuth()) ? "[]": group.getTableAuth(), String.class));
        resp.setDataAuth(JsonUtils.fromJson(StringUtils.isBlank(group.getDataAuth()) ? "{}" : group.getDataAuth(), Condition.class));
        resp.setViewAuth(JsonUtils.fromJson(StringUtils.isBlank(group.getViewAuth()) ? "{}" : group.getViewAuth(), new TypeReference<LinkedHashMap<String, ViewAuth>>() {}));
        resp.setFieldAuth(appPermissionGroupMergeService.parseFieldAuth(group.getColumnAuth()));
        return resp;
    }

    @Override
    public AppPerGroupInfoResp getPermissionGroupInfo(Long orgId, Long appId, Long groupId)
            throws BusinessException {
        return convertPerGroupToResp(getAssertNotDeleteGroup(orgId, appId, groupId));
    }

    @Override
    public List<AppPerGroupInfoResp> getPermissionGroupInfoList(Long orgId, Long appId, AppPermissionGroupInfoListReq req)
            throws BusinessException {
        List<PerAppPermissionGroup> groups;
        if (req.getGroupIds() == null || req.getGroupIds().size() == 0) {
            groups = getAssertNotDeleteGroups(orgId, appId);
        } else {
            groups = getAssertNotDeleteGroups(orgId, appId, req.getGroupIds());
        }
        List<AppPerGroupInfoResp> resp = new ArrayList<>();
        groups.forEach(group -> {
            resp.add(convertPerGroupToResp(group));
        });
        return resp;
    }


    @Override
    public AppPerBaseConfigResp getPermissionBaseConfig(Long orgId, Long appId)
            throws BusinessException {
        // 获取应用
        AppResp appResp = appService.getApp(orgId, appId);
        if (Objects.isNull(appResp)) {
            throw new BusinessException(PermissionResultCode.APP_NOT_EXIST_OR_DELETED);
        }
        PerAppPermissionConfig permissionConfig = getAppPermissionConfig(orgId,
                appId, appResp.getType());
        if (Objects.isNull(permissionConfig)) {
            throw new BusinessException(PermissionResultCode.PER_APP_BASE_CONFIG_NOT_MATCHED);
        }
        AppPerBaseConfigResp resp = new AppPerBaseConfigResp();
        resp.setOrgId(orgId);
        resp.setAppId(appId);
        resp.setCreatable(permissionConfig.getCreatable());
        resp.setOptAuthOptions(PerBaseConfigUtils.jsonToOptAuthOptionInfoBoList(permissionConfig.getOptAuthOptions()));
        resp.setFieldAuthOptions(PerBaseConfigUtils.jsonToFieldAuthOptionInfoBoList(permissionConfig.getFieldAuthOptions()));
        return resp;
    }

    @Override
    public PerAppPermissionConfig getAppPermissionConfig(Long orgId, Long appId, Integer appType) {
        // 先根据定制化取
        PerAppPermissionConfig config = appPermissionConfigMapper.selectOne(
                new LambdaQueryWrapper<PerAppPermissionConfig>().eq(PerAppPermissionConfig::getOrgId, orgId)
                        .eq(PerAppPermissionConfig::getAppId, appId));
        // 根据类型取
        if (Objects.isNull(config)) {
            config = appPermissionConfigMapper.selectOne(
                    new LambdaQueryWrapper<PerAppPermissionConfig>().eq(PerAppPermissionConfig::getOrgId, 0).eq(PerAppPermissionConfig::getAppType, appType));
        }
        return config;
    }

    @Override
    public List<PerAppPermissionConfig> getAppPermissionConfigList() {
        return appPermissionConfigMapper.selectList(
                new LambdaQueryWrapper<>());
    }

    @Override
    public List<PerAppPermissionGroup> getAppPermissionGroupsByUser(Long orgId, Long appId,
                                                                    Long userId, Collection<Long> deptIds,
                                                                    Collection<Long> roleIds) {
        if (deptIds == null){
            deptIds = new ArrayList<>();
        }
        deptIds.add(0L);
        return baseMapper.selectUserAppPerGroupsByUserAndApp(orgId, appId, userId,
                JsonUtils.toJson(deptIds),
                JsonUtils.toJson(roleIds));
    }

    public List<PerAppPermissionGroup> getAppPermissionGroupsByUserAndApps(Long orgId, List<Long> appIds,
                                                                    Long userId, Collection<Long> deptIds,
                                                                    Collection<Long> roleIds) {
        if (deptIds == null){
            deptIds = new ArrayList<>();
        }
        deptIds.add(0L);
        return baseMapper.selectUserAppPerGroupsByUserAndApps(orgId, appIds, userId,
                JsonUtils.toJson(deptIds),
                JsonUtils.toJson(roleIds));
    }

    @Override
    public List<PerAppPermissionGroup> getAppPermissionGroupsByUser(Long orgId, Long userId, Collection<Long> deptIds
            , Collection<Long> roleIds) {
        if (deptIds == null){
            deptIds = new ArrayList<>();
        }
        deptIds.add(0L);
        return baseMapper.selectUserAppPerGroupsByUser(orgId, userId, JsonUtils.toJson(deptIds),
                JsonUtils.toJson(roleIds));
    }

    @Override
    public List<PerAppPermissionGroup> getAppPerGroupsByRole(Long orgId, Collection<Long> roleIds) {
        return baseMapper.selectAppPerGroupsByRole(orgId, JsonUtils.toJson(roleIds));
    }

    // 记录权限变更时间
    private void updatePermissionTime(Long appId) {
        redisUtil.set(CacheConsts.APP_PERMISSION_UPDATE_TIME + appId, String.valueOf(System.currentTimeMillis() / 1000));
    }
}
