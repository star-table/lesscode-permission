package com.polaris.lesscode.permission.internal.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.polaris.lesscode.app.internal.api.AppViewApi;
import com.polaris.lesscode.app.internal.feign.AppProvider;
import com.polaris.lesscode.app.internal.resp.AppPackageResp;
import com.polaris.lesscode.app.internal.resp.AppResp;
import com.polaris.lesscode.app.internal.resp.AppViewResp;
import com.polaris.lesscode.consts.CommonConsts;
import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.exception.BusinessException;
import com.polaris.lesscode.gotable.internal.resp.TableSchemas;
import com.polaris.lesscode.permission.bo.Event;
import com.polaris.lesscode.permission.bo.MemberParser;
import com.polaris.lesscode.permission.bo.PerGroupEvent;
import com.polaris.lesscode.permission.constant.PermissionConstant;
import com.polaris.lesscode.permission.constant.ThreadPools;
import com.polaris.lesscode.permission.config.RedisConfig;
import com.polaris.lesscode.permission.constant.CacheConsts;
import com.polaris.lesscode.permission.entity.PerAppPermissionConfig;
import com.polaris.lesscode.permission.entity.PerAppPermissionGroup;
import com.polaris.lesscode.permission.entity.PerAppPermissionMember;
import com.polaris.lesscode.permission.internal.enums.AppPerDefaultGroupLangCode;
import com.polaris.lesscode.permission.internal.enums.DefaultAppPermissionGroupType;
import com.polaris.lesscode.permission.internal.model.OrgUserPermissionContext;
import com.polaris.lesscode.permission.internal.model.bo.OptAuthOptionInfoBo;
import com.polaris.lesscode.permission.internal.model.bo.ViewAuth;
import com.polaris.lesscode.permission.internal.model.req.*;
import com.polaris.lesscode.permission.internal.model.resp.*;
import com.polaris.lesscode.permission.internal.service.AppPermissionInternalService;
import com.polaris.lesscode.permission.internal.service.AppPermissionMemberInternalService;
import com.polaris.lesscode.permission.mapper.AppPermissionConfigMapper;
import com.polaris.lesscode.permission.mapper.AppPermissionGroupMapper;
import com.polaris.lesscode.permission.mapper.AppPermissionMemberMapper;
import com.polaris.lesscode.permission.rest.PermissionResultCode;
import com.polaris.lesscode.permission.service.*;
import com.polaris.lesscode.permission.utils.PerBaseConfigUtils;
import com.polaris.lesscode.util.ConvertUtil;
import com.polaris.lesscode.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
public class AppPermissionInternalServiceImpl extends ServiceImpl<AppPermissionGroupMapper, PerAppPermissionGroup>
        implements AppPermissionInternalService {

    private final UserService userService;
    private final AppPermissionService appPermissionService;
    private final AppPermissionMemberInternalService appPermissionMemberInternalService;
    private final AppPermissionMemberMapper appPermissionMemberMapper;
    private final AppPermissionGroupMapper appPermissionGroupMapper;
    private final AppPermissionConfigMapper appPermissionConfigMapper;
    private final AppService appService;
    private final AppViewApi appViewApi;
    private final AppProvider appProvider;
    private final GoTableService goTableService;
    private final GoPushService goPushService;
    private final AppPermissionGroupMergeServiceImpl appPermissionGroupMergeService;
    private final RedisConfig redisUtil;

    public AppPermissionInternalServiceImpl(AppPermissionMemberMapper appPermissionMemberMapper,
                                            UserService userService,
                                            AppPermissionService appPermissionService,
                                            AppPermissionMemberInternalService appPermissionMemberInternalService,
                                            AppPermissionGroupMapper appPermissionGroupMapper,
                                            AppPermissionConfigMapper appPermissionConfigMapper,
                                            AppService appService,
                                            AppViewApi appViewApi,
                                            AppProvider appProvider,
                                            GoTableService goTableService,
                                            GoPushService goPushService,
                                            AppPermissionGroupMergeServiceImpl appPermissionGroupMergeService, RedisConfig redisUtil) {
        this.appPermissionMemberMapper = appPermissionMemberMapper;
        this.userService = userService;
        this.appPermissionService = appPermissionService;
        this.appPermissionMemberInternalService = appPermissionMemberInternalService;
        this.appPermissionGroupMapper = appPermissionGroupMapper;
        this.appPermissionConfigMapper = appPermissionConfigMapper;
        this.appService = appService;
        this.appViewApi = appViewApi;
        this.appProvider = appProvider;
        this.goTableService = goTableService;
        this.goPushService = goPushService;
        this.appPermissionGroupMergeService = appPermissionGroupMergeService;
        this.redisUtil = redisUtil;
    }

    @Override
    public List<AppPermissionGroupResp> getAppPermissionGroupBatch(List<Long> appIds) {
        List<PerAppPermissionGroup> groups = appPermissionGroupMapper.selectList(new LambdaQueryWrapper<PerAppPermissionGroup>().eq(PerAppPermissionGroup::getDelFlag, CommonConsts.FALSE).in(PerAppPermissionGroup::getAppId, appIds));
        return ConvertUtil.convertList(groups, AppPermissionGroupResp.class);
    }

    @Override
    public List<AppPermissionGroupResp> createAppPermissionGroupBatch(List<CreateAppPermissionGroupReq> req) {
        List<PerAppPermissionGroup> groups = ConvertUtil.convertList(req, PerAppPermissionGroup.class);
        groups.forEach(g -> g.setId(null));
        saveBatch(groups);
        List<PerAppPermissionMember> members = new ArrayList<>();
        for (PerAppPermissionGroup group: groups){
            PerAppPermissionMember member = new PerAppPermissionMember();
            member.setOrgId(group.getOrgId());
            member.setAppId(group.getAppId());
            member.setPermissionGroupId(group.getId());
            member.setCreator(group.getCreator());
            member.setUpdator(group.getUpdator());
            member.setDeptIds("[]");
            member.setUserIds("[]");
            member.setRoleIds("[]");
            members.add(member);
        }
        appPermissionMemberInternalService.saveBatch(members);
        return ConvertUtil.convertList(groups, AppPermissionGroupResp.class);
    }

    @Override
    public List<AppPerGroupListItem> getAppPermissionGroupList(Long orgId, Long appId) {
        List<PerAppPermissionGroup> groups = appPermissionGroupMapper.selectList(
                new LambdaQueryWrapper<PerAppPermissionGroup>().eq(PerAppPermissionGroup::getOrgId, orgId)
                        .eq(PerAppPermissionGroup::getAppId, appId));
        List<AppPerGroupListItem> items = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(groups)){
            for (PerAppPermissionGroup group : groups) {
                AppPerGroupListItem item = new AppPerGroupListItem();
                item.setId(group.getId());
                item.setLangCode(group.getLangCode());
                item.setName(group.getName());
                item.setRemake(group.getRemake());
                item.setReadOnly(group.getReadOnly());
                items.add(item);
            }
        }
        return items;
    }

    /**
     * 使用双重锁检查
     *
     * @param req
     * @return
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public boolean initAppPermission(InitAppPermissionReq req) throws BusinessException {
        log.info("[初始化应用权限]-> 参数：{}", JsonUtils.toJson(req));
        initAppPermissionConfig(req);
        return true;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public boolean initAppPermissionFieldAuthCreateTable(InitAppPermissionFieldAuthCreateTableReq req) throws BusinessException {
        log.info("[初始化字段权限(表格创建)]-> 参数：{}", JsonUtils.toJson(req));
        initFieldAuthCreateTable(req);
        return true;
    }

    @Override
    public boolean initAppPermissionFieldAuthDeleteTable(InitAppPermissionFieldAuthDeleteTableReq req) throws BusinessException {
        log.info("[初始化字段权限(表格删除)]-> 参数：{}", JsonUtils.toJson(req));
        initFieldAuthDeleteTable(req);
        return true;
    }

    @Override
    public boolean updateAppPermission(UpdateAppPermissionReq req) throws BusinessException {
        PerAppPermissionGroup appPermissionGroup = new PerAppPermissionGroup();
        appPermissionGroup.setId(req.getId());
        appPermissionGroup.setName(req.getName());
        appPermissionGroup.setRemake(req.getRemake());
        appPermissionGroup.setOptAuth(req.getOptAuth());
        appPermissionGroup.setTableAuth(req.getTableAuth());
        appPermissionGroup.setColumnAuth(req.getFieldAuth());
        appPermissionGroup.setDataAuth(req.getDataAuth());
        appPermissionGroup.setViewAuth(req.getViewAuth());
        appPermissionService.updateById(appPermissionGroup);
        return true;
    }

    @Override
    public boolean updateAppPermissionByLangCode(UpdateAppPermissionByLangCodeReq req) throws BusinessException {
        LambdaUpdateWrapper<PerAppPermissionGroup> updated = new LambdaUpdateWrapper<PerAppPermissionGroup>().eq(PerAppPermissionGroup::getAppId, req.getAppId()).eq(PerAppPermissionGroup::getLangCode, req.getLangCode());
        if (StringUtils.isNotBlank(req.getName())){
            updated.set(PerAppPermissionGroup::getName, req.getName());
        }
        if (StringUtils.isNotBlank(req.getRemake())){
            updated.set(PerAppPermissionGroup::getRemake, req.getRemake());
        }
        if (StringUtils.isNotBlank(req.getOptAuth())){
            updated.set(PerAppPermissionGroup::getOptAuth, req.getOptAuth());
        }
        if (StringUtils.isNotBlank(req.getTableAuth())){
            updated.set(PerAppPermissionGroup::getTableAuth, req.getTableAuth());
        }
        if (StringUtils.isNotBlank(req.getFieldAuth())){
            updated.set(PerAppPermissionGroup::getColumnAuth, req.getFieldAuth());
        }
        if (StringUtils.isNotBlank(req.getDataAuth())){
            updated.set(PerAppPermissionGroup::getDataAuth, req.getDataAuth());
        }
        if (StringUtils.isNotBlank(req.getViewAuth())){
            updated.set(PerAppPermissionGroup::getViewAuth, req.getViewAuth());
        }
        appPermissionService.update(updated);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteAppPermission(Long orgId, Long appId, Long userId) {
        log.info("[删除应用的全部权限组]-> orgId:{} appId:{} userId:{}", orgId, appId, userId);
        deleteAppListPermission(orgId, Collections.singleton(appId), userId);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteAppListPermission(Long orgId, Collection<Long> appIds, Long userId) {
        log.info("[删除应用权限]-> orgId:{} appIds:{} userId:{}", orgId, appIds, userId);
        if (Objects.isNull(appIds) || appIds.isEmpty()) {
            log.warn("[删除应用权限] -> 应用列表为空");
            return true;
        }
        int count = appPermissionConfigMapper.update(null,
                new LambdaUpdateWrapper<PerAppPermissionConfig>().set(PerAppPermissionConfig::getUpdator, userId)
                        .set(PerAppPermissionConfig::getDelFlag,
                                CommonConsts.DELETED)
                        .set(Objects.nonNull(userId), PerAppPermissionConfig::getUpdator, userId)
                        .eq(PerAppPermissionConfig::getOrgId, orgId)
                        .in(PerAppPermissionConfig::getAppId,
                                appIds));
        log.info("[删除应用权限] -> PerAppPermissionConfig 删除[{}]条", count);
        count = appPermissionMemberMapper.update(null,
                new LambdaUpdateWrapper<PerAppPermissionMember>().set(PerAppPermissionMember::getUpdator,
                        userId)
                        .set(PerAppPermissionMember::getDelFlag,
                                CommonConsts.DELETED)
                        .set(Objects.nonNull(userId), PerAppPermissionMember::getUpdator, userId)
                        .eq(PerAppPermissionMember::getOrgId, orgId)
                        .in(PerAppPermissionMember::getAppId,
                                appIds));
        log.info("[删除应用权限] -> PerAppPermissionMember 删除[{}]条", count);
        count = baseMapper.update(null, new LambdaUpdateWrapper<PerAppPermissionGroup>().set(PerAppPermissionGroup::getUpdator, userId)
                .set(PerAppPermissionGroup::getDelFlag,
                        CommonConsts.DELETED)
                .set(Objects.nonNull(userId), PerAppPermissionGroup::getUpdator, userId)
                .eq(PerAppPermissionGroup::getOrgId, orgId)
                .in(PerAppPermissionGroup::getAppId, appIds));
        log.info("[删除应用权限] -> PerAppPermissionGroup 删除[{}]条", count);
        return true;
    }

    public void initAppPermissionConfig(InitAppPermissionReq req) {
        // 开始初始化默认权限组
        initDefaultGroup(req);

        // 添加到管理组
        userService.addAppToManageGroup(req.getOrgId(), req.getUserId(), req.getAppId());
    }

    /**
     * 初始化默认权限组
     *
     * @param req 初始化请求信息 {@link InitAppPermissionReq}
     * @Author Nico
     * @Date 2020/12/29 20:39
     **/
    public void initDefaultGroup(InitAppPermissionReq req) {
        DefaultAppPermissionGroupType type = DefaultAppPermissionGroupType.forValue(req.getDefaultPermissionGroupType());
        if (type == null) {
            log.warn("initDefaultGroup, DefaultAppPermissionGroupType.getByCode result null, type code: {}", req.getDefaultPermissionGroupType());
            return;
        }
        switch (type) {
            case FORM:
                initFormDefaultPermissionGroup(req, type.getDefaultLangCodes().get());
                break;
            case DASHBOARD:
                initDashboardDefaultPermissionGroup(req, type.getDefaultLangCodes().get());
                break;
            case POLARIS_PROJECT:
                initPolarisProjectDefaultPermissionGroup(req, type.getDefaultLangCodes().get());
                break;
            default:
                break;
        }
    }

    /**
     * 初始化默认字段权限(表格创建)
     *
     * @param req 初始化请求信息 {@link InitAppPermissionFieldAuthCreateTableReq}
     * @Author
     * @Date
     **/
    public void initFieldAuthCreateTable(InitAppPermissionFieldAuthCreateTableReq req) {
        DefaultAppPermissionGroupType type = DefaultAppPermissionGroupType.forValue(req.getDefaultPermissionGroupType());
        if (type == null) {
            log.warn("initFieldAuthCreateTable, DefaultAppPermissionGroupType.getByCode result null, type code: {}", req.getDefaultPermissionGroupType());
            return;
        }
        switch (type) {
            case FORM: case DASHBOARD:
                // 不处理
                break;
            case POLARIS_PROJECT:
                log.info("[初始化PolarisProject CreateTable默认字段权限] -> req:{}", JsonUtils.toJson(req));
                PerAppPermissionGroup group;
                for (AppPerDefaultGroupLangCode langCode : type.getDefaultLangCodes().get()) {
                    LambdaUpdateWrapper<PerAppPermissionGroup> updated = new LambdaUpdateWrapper<PerAppPermissionGroup>().
                            eq(PerAppPermissionGroup::getAppId, req.getAppId()).
                            eq(PerAppPermissionGroup::getLangCode, langCode.getCode());
                    String fa;
                    if (langCode == AppPerDefaultGroupLangCode.PROJECT_VIEWER) {
                        fa = PermissionConstant.EMPTY_FIELD_AUTH_ONLY_READ_JSON;
                    } else {
                        fa = PermissionConstant.EMPTY_MAP_JSON;
                    }
                    updated.setSql("column_auth=json_insert(column_auth,'$.\"" + req.getTableId().toString() + "\"','" + fa + "')");
                    updated.set(PerAppPermissionGroup::getUpdator, req.getUserId());
                    updated.set(PerAppPermissionGroup::getUpdateTime, new Date());
                    appPermissionService.update(updated);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化默认字段权限(表格删除)
     *
     * @param req 初始化请求信息 {@link InitAppPermissionFieldAuthDeleteTableReq}
     * @Author
     * @Date
     **/
    public void initFieldAuthDeleteTable(InitAppPermissionFieldAuthDeleteTableReq req) {
        DefaultAppPermissionGroupType type = DefaultAppPermissionGroupType.forValue(req.getDefaultPermissionGroupType());
        if (type == null) {
            log.warn("initFieldAuthDeleteTable, DefaultAppPermissionGroupType.getByCode result null, type code: {}", req.getDefaultPermissionGroupType());
            return;
        }
        switch (type) {
            case FORM: case DASHBOARD:
                // 不处理
                break;
            case POLARIS_PROJECT:
                log.info("[初始化PolarisProject DeleteTable默认字段权限] -> req:{}", JsonUtils.toJson(req));
                PerAppPermissionGroup group;
                for (AppPerDefaultGroupLangCode langCode : type.getDefaultLangCodes().get()) {
                    LambdaUpdateWrapper<PerAppPermissionGroup> updated = new LambdaUpdateWrapper<PerAppPermissionGroup>().
                            eq(PerAppPermissionGroup::getAppId, req.getAppId()).
                            eq(PerAppPermissionGroup::getLangCode, langCode.getCode());
                    updated.setSql("column_auth=json_remove(column_auth,'$.\"" + req.getTableId().toString() + "\"')");
                    appPermissionService.update(updated);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化表单默认权限组
     *
     * @param req 初始化请求信息 {@link InitAppPermissionReq}
     * @Author Nico
     * @Date 2020/12/29 20:13
     **/
    private void initFormDefaultPermissionGroup(InitAppPermissionReq req, Collection<AppPerDefaultGroupLangCode> langCodes) {
        log.info("[初始化应用默认权限组] -> req:{}", JsonUtils.toJson(req));
        // 插入权限组和对应成员
        List<PerAppPermissionGroup> permissionGroupList = new ArrayList<>();
        for (AppPerDefaultGroupLangCode langCode : langCodes) {
            // 默认权限组具有当前时间点所有字段的权限
            PerAppPermissionGroup permissionGroup = new PerAppPermissionGroup();
            permissionGroup.setLangCode(langCode.getCode());
            permissionGroup.setName(langCode.getName());
            permissionGroup.setRemake(langCode.getDesc());
            permissionGroup.setReadOnly(langCode.getReadOnly());
            permissionGroup.setOptAuth(JsonUtils.toJson(langCode.getDefaultOptAuth().get()));
            permissionGroup.setTableAuth(JsonUtils.toJson(langCode.getDefaultTableAuth().get()));
            permissionGroup.setColumnAuth(PermissionConstant.EMPTY_MAP_JSON);
            permissionGroup.setDataAuth(JsonUtils.toJson(langCode.getDefaultDataAuth().get()));
            permissionGroup.setOrgId(req.getOrgId());
            permissionGroup.setAppId(req.getAppId());
            permissionGroup.setCreator(req.getUserId());
            permissionGroup.setUpdator(req.getUserId());
            permissionGroupList.add(permissionGroup);
        }
        saveBatch(permissionGroupList);

        List<PerAppPermissionMember> permissionMemberList = new ArrayList<>();
        for (PerAppPermissionGroup group : permissionGroupList) {
            PerAppPermissionMember newPermissionMember = new PerAppPermissionMember();
            newPermissionMember.setOrgId(req.getOrgId());
            newPermissionMember.setAppId(req.getAppId());
            newPermissionMember.setPermissionGroupId(group.getId());
            newPermissionMember.setUserIds(PermissionConstant.EMPTY_ARRAY_JSON);
            newPermissionMember.setDeptIds(PermissionConstant.EMPTY_ARRAY_JSON);
            newPermissionMember.setRoleIds(PermissionConstant.EMPTY_ARRAY_JSON);
            newPermissionMember.setCreator(req.getUserId());
            newPermissionMember.setUpdator(req.getUserId());
            permissionMemberList.add(newPermissionMember);
        }

        appPermissionMemberInternalService.saveBatch(permissionMemberList);
    }

    /**
     * 初始化仪表盘默认权限组
     *
     * @param req 初始化请求信息 {@link InitAppPermissionReq}
     * @Author Nico
     * @Date 2020/12/29 20:13
     **/
    private void initDashboardDefaultPermissionGroup(InitAppPermissionReq req, Collection<AppPerDefaultGroupLangCode> langCodes) {
        log.info("[初始化仪表盘默认权限组] -> req:{}", JsonUtils.toJson(req));

        // 插入权限组和对应成员
        List<PerAppPermissionGroup> permissionGroupList = new ArrayList<>();
        for (AppPerDefaultGroupLangCode langCode : langCodes) {
            PerAppPermissionGroup permissionGroup = new PerAppPermissionGroup();
            permissionGroup.setLangCode(langCode.getCode());
            permissionGroup.setName(langCode.getName());
            permissionGroup.setRemake(langCode.getDesc());
            permissionGroup.setReadOnly(langCode.getReadOnly());
            permissionGroup.setOptAuth(JsonUtils.toJson(langCode.getDefaultOptAuth().get()));
            permissionGroup.setTableAuth(JsonUtils.toJson(langCode.getDefaultTableAuth().get()));
            permissionGroup.setColumnAuth(PermissionConstant.EMPTY_MAP_JSON);
            permissionGroup.setDataAuth(JsonUtils.toJson(langCode.getDefaultDataAuth().get()));
            permissionGroup.setOrgId(req.getOrgId());
            permissionGroup.setAppId(req.getAppId());
            permissionGroup.setCreator(req.getUserId());
            permissionGroup.setUpdator(req.getUserId());
            permissionGroupList.add(permissionGroup);
        }
        saveBatch(permissionGroupList);

        List<PerAppPermissionMember> permissionMemberList = new ArrayList<>();
        for (PerAppPermissionGroup group : permissionGroupList) {
            PerAppPermissionMember newPermissionMember = new PerAppPermissionMember();
            newPermissionMember.setOrgId(req.getOrgId());
            newPermissionMember.setAppId(req.getAppId());
            newPermissionMember.setPermissionGroupId(group.getId());
            newPermissionMember.setUserIds(PermissionConstant.EMPTY_ARRAY_JSON);
            newPermissionMember.setDeptIds(PermissionConstant.EMPTY_ARRAY_JSON);
            newPermissionMember.setRoleIds(PermissionConstant.EMPTY_ARRAY_JSON);
            newPermissionMember.setCreator(req.getUserId());
            newPermissionMember.setUpdator(req.getUserId());
            permissionMemberList.add(newPermissionMember);
        }

        appPermissionMemberInternalService.saveBatch(permissionMemberList);
    }

    /**
     * 初始化北极星项目默认权限组
     *
     * @param req 初始化请求信息 {@link InitAppPermissionReq}
     * @Author Nico
     * @Date 2020/12/29 20:13
     **/
    private void initPolarisProjectDefaultPermissionGroup(InitAppPermissionReq req, Collection<AppPerDefaultGroupLangCode>  langCodes) {
        log.info("[初始化PolarisProject默认权限组] -> req:{}", JsonUtils.toJson(req));
        List<PerAppPermissionGroup> permissionGroupList = new ArrayList<>();
        for (AppPerDefaultGroupLangCode langCode : langCodes) {
            PerAppPermissionGroup permissionGroup = new PerAppPermissionGroup();
            permissionGroup.setOrgId(req.getOrgId());
            permissionGroup.setLangCode(langCode.getCode());
            permissionGroup.setName(langCode.getName());
            permissionGroup.setRemake(langCode.getDesc());
            permissionGroup.setReadOnly(langCode.getReadOnly());
            permissionGroup.setOptAuth(JsonUtils.toJson(langCode.getDefaultOptAuth().get()));
            permissionGroup.setTableAuth(JsonUtils.toJson(langCode.getDefaultTableAuth().get()));
            permissionGroup.setColumnAuth(PermissionConstant.EMPTY_MAP_JSON);
            permissionGroup.setDataAuth(JsonUtils.toJson(langCode.getDefaultDataAuth().get()));
            permissionGroup.setAppId(req.getAppId());
            permissionGroup.setCreator(req.getUserId());
            permissionGroup.setUpdator(req.getUserId());
            permissionGroupList.add(permissionGroup);
        }
        saveBatch(permissionGroupList);
        List<PerAppPermissionMember> permissionMemberList = new ArrayList<>();
        for (PerAppPermissionGroup group : permissionGroupList) {
            PerAppPermissionMember newPermissionMember = new PerAppPermissionMember();
            newPermissionMember.setOrgId(req.getOrgId());
            newPermissionMember.setAppId(req.getAppId());
            newPermissionMember.setPermissionGroupId(group.getId());
            newPermissionMember.setUserIds(PermissionConstant.EMPTY_ARRAY_JSON);
            newPermissionMember.setDeptIds(PermissionConstant.EMPTY_ARRAY_JSON);
            newPermissionMember.setRoleIds(PermissionConstant.EMPTY_ARRAY_JSON);
            newPermissionMember.setCreator(req.getUserId());
            newPermissionMember.setUpdator(req.getUserId());
            permissionMemberList.add(newPermissionMember);
        }
        appPermissionMemberInternalService.saveBatch(permissionMemberList);
    }

//    /**
//     * 获取默认的字段权限
//     *
//     * @param config
//     */
//    private Map<String, Integer> getDefaultGroupFieldAuth(String config, FormFieldAuthCode fieldAuthCode) throws BusinessException {
//        List<String> fieldNameList = JsonToFieldUtils.fromFieldConfigJsonToFieldNameList(config);
//        return generateFieldAuthMap(fieldNameList, fieldAuthCode);
//    }

//    private Map<String, Map<String, Integer>> generateFieldAuthMap(Map<String, List<String>> fieldNameList, FormFieldAuthCode fieldAuthCode) {
//        int code = Optional.ofNullable(fieldAuthCode).orElse(FormFieldAuthCode.READ_AND_WRITE).getCode();
//
//        Map<String, Map<String, Integer>> map = new HashMap<>();
//        fieldNameList.entrySet().forEach(t -> {
//            Map<String, Integer> submap = new HashMap<>();
//            t.getValue().forEach(fn -> {
//                submap.put(fn, code);
//            });
//            map.put(t.getKey(), submap);
//        });
//
//        // 重复的key
////        Map<String, Integer> map = fieldNameList.stream().collect(Collectors.toMap(name -> name, v -> code));
////        // 三个默认字段
////        map.put(DefaultFieldConstant.CREATOR, FormFieldAuthCode.READ.getCode());
////        map.put(DefaultFieldConstant.CREATE_TIME, FormFieldAuthCode.READ.getCode());
////        map.put(DefaultFieldConstant.UPDATE_TIME, FormFieldAuthCode.READ.getCode());
//        return map;
//    }

    private Map<String, ViewAuth> generateViewAuthMap(List<AppViewResp> appViews) {
        Map<String, ViewAuth> viewAuthMap = new LinkedHashMap<>();
        if (CollectionUtils.isNotEmpty(appViews)) {
            for (AppViewResp view : appViews) {
                ViewAuth viewAuth = new ViewAuth();
                viewAuth.setHasDelete(true);
                viewAuth.setHasUpdate(true);
                viewAuth.setHasSelect(true);
                viewAuthMap.put(String.valueOf(view.getId()), viewAuth);
            }
        }
        return viewAuthMap;
    }

    @Override
    public List<String> getPermissionGroupTableAuth(Long orgId, Long appId, Long userId) {
        List<String> tableIdList = new ArrayList<>();
        Set<String> tableIds = new HashSet<>();
        OrgUserPermissionContext context = userService.getPermissionContext(orgId, userId);
        List<TableSchemas> tableSchemas = goTableService.readTableSchemasByAppId(orgId, appId, userId);

        // 根据当前人员关系信息获取应用权限组
        List<PerAppPermissionGroup> permissionGroupList = appPermissionService.getAppPermissionGroupsByUser(orgId, appId,
                userId, context.getDeptIds(),
                context.getRoleIds());

        tableSchemas.forEach(t -> {
            tableIds.add(t.getTableId().toString());
        });
        permissionGroupList.forEach(group -> {
            List<String> tableAuth = JsonUtils.fromJson(group.getTableAuth(), new TypeReference<List<String>>() {});
            tableAuth.forEach(t -> {
                tableIds.remove(t);
            });
        });
        tableIds.forEach(t -> {
            tableIdList.add(t);
        });

        return tableIdList;
    }

    @Override
    public Map<String, Map<String, Integer>> getPermissionGroupFieldAuth(Long orgId, Long appId, Long userId) {
        // 获取原应用表单字段配置
        AppResp appResp = appService.getApp(orgId, appId);
        if (Objects.isNull(appResp)) {
            log.error("[getPermissionGroupFieldAuth] -> 应用[{}:{}]不存在", orgId, appId);
            return null;
        }

        // 管理所在应用包||应用,拥有所有字段权限
        OrgUserPermissionContext context = userService.getPermissionContext(orgId, userId);
        if (context.hasManageAppPermission(appId)) {
            return Collections.emptyMap();
        }

        // 根据当前人员关系信息获取应用权限组
        List<PerAppPermissionGroup> permissionGroupList = appPermissionService.getAppPermissionGroupsByUser(orgId, appId,
                userId, context.getDeptIds(),
                context.getRoleIds());
        return appPermissionGroupMergeService.mergeFieldAuth(permissionGroupList);
    }

    @Override
    public Map<String, ViewAuth> getPermissionGroupViewAuth(Long orgId, Long appId, Long userId) {
        Map<String, ViewAuth> viewAuth = new LinkedHashMap<>();
        OrgUserPermissionContext context = userService.getPermissionContext(orgId, userId);

        List<AppViewResp> appViewIdList = appViewApi.getAppViewIdList(orgId, appId, true, 0L).getData();
        if (context.hasManageAppPermission(appId)) {
            return generateViewAuthMap(appViewIdList);
        }

        // 根据当前人员关系信息获取应用权限组
        List<PerAppPermissionGroup> permissionGroupList = appPermissionService.getAppPermissionGroupsByUser(orgId, appId,
                userId, context.getDeptIds(),
                context.getRoleIds());
        // 获取视图权限的并集
        permissionGroupList.forEach(group -> {
            Map<String, ViewAuth> viewAuthMap = JsonUtils.fromJson(group.getViewAuth(), new TypeReference<LinkedHashMap<String, ViewAuth>>() {
            });
            if (MapUtils.isNotEmpty(viewAuthMap)) {
                for (Map.Entry<String, ViewAuth> entry : viewAuthMap.entrySet()) {
                    ViewAuth auth = viewAuth.get(entry.getKey());
                    if (auth == null) {
                        auth = new ViewAuth();
                        viewAuth.put(entry.getKey(), auth);
                    }
                    auth.setHasUpdate(auth.isHasUpdate() || entry.getValue().isHasUpdate());
                    auth.setHasSelect(auth.isHasSelect() || entry.getValue().isHasSelect());
                    auth.setHasDelete(auth.isHasDelete() || entry.getValue().isHasDelete());
                }
            }
        });
        return viewAuth;
    }

    @Override
    public FromPerOptAuthVO getPermissionGroupOptAuth(Long orgId, Long appId, Long userId) {
        return new FromPerOptAuthVO(getPermissionGroupOptAuthList(orgId, appId, userId));
    }

    /**
     * 获取应用所有操作权限
     */
    private List<String> getAppAllOptAuth(Long orgId, Long appId, Integer appType) {
        PerAppPermissionConfig config = appPermissionService.getAppPermissionConfig(orgId, appId, appType);
        if (Objects.isNull(config)) {
            return Collections.emptyList();
        }
        return PerBaseConfigUtils.jsonToOptAuthOptionInfoBoList(config.getOptAuthOptions()).stream().map(OptAuthOptionInfoBo::getCode).collect(Collectors.toList());
    }

    @Override
    public List<Condition> getPermissionGroupDataAuth(Long orgId, Long appId, Long userId) {
        OrgUserPermissionContext context = userService.getPermissionContext(orgId, userId);
        // 获取应用
        AppResp appResp = appService.getApp(orgId, appId);
        if (Objects.isNull(appResp)) {
            log.warn("[获取应用数据权限] -> 应用[{}:{}]不存在", orgId, appId);
            return Collections.emptyList();
        }
        // 管理所在应用包, 拥有所有数据权限
        if (context.hasManageAppPermission(appId)) {
            return Collections.emptyList();
        }
        // 根据当前人员关系信息获取应用权限组
        List<PerAppPermissionGroup> permissionGroupList = appPermissionService.getAppPermissionGroupsByUser(orgId, appId,
                userId, context.getDeptIds(),
                context.getRoleIds());
        return permissionGroupList.stream()
                .map(e -> JsonUtils.fromJson(e.getDataAuth(), Condition.class))
                .collect(Collectors.toList());
    }

//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public boolean updateFormFieldConfig(UpdateFieldConfigReq req) {
//        log.info("[修改表单字段配置] -> req:{}", JsonUtils.toJson(req));
//
//        // 获取应用
//        AppResp appResp = appService.getApp(req.getOrgId(), req.getAppId());
//        if (Objects.isNull(appResp)) {
//            log.warn("[修改表单字段配置] -> 应用[{}:{}]不存在", req.getOrgId(), req.getAppId());
//            return true;
//        }
//        // 获取原应用表单字段配置
//        String oldFieldConfig = appService.getFormFieldConfigByApp(req.getOrgId(), req.getAppId());
//        if (Objects.isNull(oldFieldConfig)) {
//            log.warn("[修改表单字段配置] -> 应用字段配置[{}:{}]不存在", req.getOrgId(), req.getAppId());
//            return true;
//        }
//
//        // 获取应用下的权限组
//        List<PerAppPermissionGroup> groupList = appPermissionService.getAppPermissionGroupList(req.getOrgId(), req.getAppId());
//        if (!groupList.isEmpty()) {
//            // 原配置
//            List<FieldParam> oleList = JsonToFieldUtils.fromFieldConfigJsonToFieldList(oldFieldConfig);
//            Map<String, FieldParam> oleMap = com.polaris.lesscode.util.MapUtils.toMap(FieldParam::getName, oleList);
//
//            // 新配置
//            List<FieldParam> newList = JsonToFieldUtils.fromFieldConfigJsonToFieldList(req.getConfig());
//            Map<String, FieldParam> newMap = newList.stream().collect(Collectors.toMap(FieldParam::getName, v -> v));
//            // 新增的字段
//            List<FieldParam> addFieldList = newList.stream().filter(s -> !oleMap.containsKey(s.getName())).collect(Collectors.toList());
//            // 删除的字段
//            List<String> deleteFieldList = oleList.stream().map(FieldParam::getName).filter(name -> !newMap.containsKey(name)).collect(Collectors.toList());
//            if (!addFieldList.isEmpty() || !deleteFieldList.isEmpty()) {
//                log.info("[修改权限组字段权限配置] -> addFieldList:{}    deleteFieldList:{}", addFieldList, deleteFieldList);
//                // 处理
//                for (PerAppPermissionGroup group : groupList) {
//                    AppPerDefaultGroupLangCode groupLangCode = AppPerDefaultGroupLangCode.forValue(group.getLangCode());
//                    Map<String, Integer> oldFieldAuthMap = JsonUtils.fromJson(group.getFieldAuth(), new TypeReference<Map<String, Integer>>() {
//                    });
//                    // 先删除
//                    deleteFieldList.forEach(oldFieldAuthMap::remove);
//                    // 构建新增字段的权限
//                    Map<String, Integer> addFieldMap = addFieldList.stream().collect(
//                            Collectors.toMap(FieldParam::getName, f -> {
//                                        // 默认权限组
//                                        if (Objects.nonNull(groupLangCode)) {
//                                            return groupLangCode.getFieldAuthCode().getCode();
//                                        } else if (Objects.equals(f.getSensitiveFlag(), 1)) {
//                                            // 敏感字段 默认为可见，脱敏
//                                            return FormFieldAuthCode.READ_AND_MASKING.getCode();
//                                        } else {
//                                            return FormFieldAuthCode.READ.getCode();
//                                        }
//
//                                    }
//                            ));
//                    oldFieldAuthMap.putAll(addFieldMap);
//                    group.setFieldAuth(JsonUtils.toJson(oldFieldAuthMap));
//
//                    appPermissionGroupMapper.updateById(group);
//                }
//            }
//        }
//        return true;
//    }

    @Override
    public List<String> getPermissionGroupOptAuthList(Long orgId, Long appId, Long userId) {
        OrgUserPermissionContext context = userService.getPermissionContext(orgId, userId);

        AppResp appResp = appService.getApp(orgId, appId);
        if (Objects.isNull(appResp)) {
            log.warn("应用[{}:{}]不存在", orgId, appId);
            return Collections.emptyList();
        }
        // 管理所在应用包,拥有所有操作权限
        if (context.hasManageAppPermission(appId)) {
            return getAppAllOptAuth(orgId, appId, appResp.getType());
        }
        // 根据当前人员关系信息获取应用权限组
        List<PerAppPermissionGroup> permissionGroupList = appPermissionService.getAppPermissionGroupsByUser(orgId, appId,
                userId, context.getDeptIds(),
                context.getRoleIds());
        return permissionGroupList.stream()
                .map(e -> JsonUtils.fromJsonArray(e.getOptAuth(), String.class))
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public UserAppPermissionListResp getUserAppPermissionListResp(Long orgId, Long userId) {
        OrgUserPermissionContext context = userService.getPermissionContext(orgId, userId);

        UserAppPermissionListResp resp = new UserAppPermissionListResp();
        // 是否可创建应用包
        resp.setCreatable(context.hasCreateAppPackagePermission());
        // 获取所有的应用包和应用
        List<AppPackageResp> pkgList = appService.getPkgList(orgId);
        List<AppResp> appList = appService.getAppList(orgId);

        // 必须是可见的应用包
        Set<AppPkgPerItem> pkgPerSet = pkgList.stream().filter(pkgPer ->
                // 必须可见
                context.hasQueryAppPackagePermission(pkgPer.getId())
        ).map(pkg -> {
            AppPkgPerItem item = new AppPkgPerItem();
            item.setOrgId(orgId);
            item.setParentPkgId(pkg.getParentId());
            item.setAppPackageId(pkg.getId());
            // 可编辑
            if (context.hasManageAppPackagePermission(pkg.getId())) {
                item.setManagePkg(true);
                item.setEditable(true);
            }
            // 可删除
            if (context.hasDeleteAppPackagePermission(pkg.getId())) {
                item.setDeletable(true);
            }
            return item;
        }).collect(Collectors.toSet());
        resp.addPkg(pkgPerSet);

        // 获取 应用权限模版配置
        List<PerAppPermissionConfig> appPerConfigList = appPermissionService.getAppPermissionConfigList();
        Map<Integer, PerAppPermissionConfig> baseConfigByTypeMap = new HashMap<>(appPerConfigList.size());
        Map<String, PerAppPermissionConfig> baseConfigByAppMap = new HashMap<>(appPerConfigList.size());
        appPerConfigList.forEach(baseConfig -> {
            if (Objects.equals(0L, baseConfig.getOrgId()) && Objects.nonNull(baseConfig.getAppType())) {
                baseConfigByTypeMap.put(baseConfig.getAppType(), baseConfig);
            } else {
                String strBl = String.valueOf(baseConfig.getOrgId()) +
                        baseConfig.getAppId();
                baseConfigByAppMap.put(strBl, baseConfig);
            }
        });
        Map<Long, Set<String>> appOptAuthSetMap = new HashMap<>();

        // 非管理员，进行过滤
        if (!context.hasAllPermission()) {
            // 获取所在的权限组
            List<PerAppPermissionGroup> appPerGroupList = appPermissionService.getAppPermissionGroupsByUser(orgId, userId, context.getDeptIds(), context.getRoleIds());
            // 根据App进行分组，diff改App的操作权限
            appPerGroupList.forEach(e -> {
                Set<String> optAuths = appOptAuthSetMap.computeIfAbsent(e.getAppId(), k -> new HashSet<>());
                optAuths.addAll(JsonUtils.fromJsonArray(e.getOptAuth(), String.class));
            });

            // 过滤应用(权限组中存在||管理该应用所在应用包||管理该应用)
            appList = appList.stream().filter(app -> appOptAuthSetMap.containsKey(app.getId()) || context.hasManageAppPermission(app.getId())).collect(Collectors.toList());
        }

        List<AppPerItem> appPerItemList = new ArrayList<>();
        for (AppResp appResp : appList) {
            // 获取自定义基础配置,获取不到从默认基础配置取
            PerAppPermissionConfig config = baseConfigByAppMap.get(String.valueOf(appResp.getOrgId()) + appResp.getId());
            if (Objects.isNull(config)) {
                config = baseConfigByTypeMap.get(appResp.getType());
            }
            AppPerItem item = new AppPerItem();
            item.setOrgId(appResp.getOrgId());
            item.setAppPackageId(appResp.getPkgId());
            item.setAppId(appResp.getId());
            // 管理所在的应用包||应用
            if (context.hasManageAppPermission(appResp.getId())) {
                item.setEditable(true);
                // todo 需要根据该应用是否是隐藏，决定 是否显示
                item.setShow(true);
            } else {
                // 是否具有可以显示的权限
                Set<String> isMenuOptAuth = PerBaseConfigUtils.jsonToOptAuthOptionInfoBoList(config.getOptAuthOptions()).
                        stream().filter(OptAuthOptionInfoBo::getIsMenu).map(OptAuthOptionInfoBo::getCode).collect(Collectors.toSet());
                Set<String> selectedOptAuth;
                if (!isMenuOptAuth.isEmpty() && (selectedOptAuth = appOptAuthSetMap.get(appResp.getId())) != null) {
                    // todo 需要根据该应用是否是隐藏，决定 是否显示
                    item.setShow(selectedOptAuth.stream().anyMatch(isMenuOptAuth::contains));
                }
            }
            if (context.hasDeleteAppPermission(appResp.getId())) {
                item.setDeletable(true);
            }
            appPerItemList.add(item);
        }

        resp.getAppList().addAll(appPerItemList);
        return resp;
    }

    @Override
    public AppAuthorityResp getAppAuthority(Long orgId, Long appId, Long tableId, Long userId) {
        return appPermissionService.getAppAuthority(orgId, appId, tableId, userId, true, null);
    }

    @Override
    public AppAuthorityResp getAppAuthorityByCollaboratorRoles(Long orgId, Long appId, Long userId, List<Long> collaboratorRoleIds) {
        return appPermissionService.getAppAuthority(orgId, appId, null, userId, true, collaboratorRoleIds);
    }

    @Override
    public AppAuthorityResp getAppAuthorityWithoutCollaborator(Long orgId, Long appId, Long userId) {
        return appPermissionService.getAppAuthority(orgId, appId, null,userId, false, null);
    }

    @Override
    public Map<Long, AppAuthorityResp> getAppAuthorityBatch(GetAppAuthorityBatchReq req) {
        return appPermissionService.getAppAuthorityBatch(req);
    }

    @Override
    public AppAuthorityResp getDataAuthority(Long orgId, Long appId, Long userId, Long dataId) {
        return appPermissionService.getDataAuthority(orgId, appId, userId, dataId);
    }

    @Override
    public Map<Long, AppAuthorityResp> getDataAuthorityBatch(GetDataAuthorityBatchReq getDataAuthorityBatchReq) {
        return appPermissionService.getDataAuthorityBatch(getDataAuthorityBatchReq);
    }

    @Override
    public Map<Long, AppAuthorityResp> getDataAuthorities(GetDataAuthorityBatchReq getDataAuthorityBatchReq) {
        return appPermissionService.getDataAuthorities(getDataAuthorityBatchReq);
    }

    @Override
    public MemberGroupMappingsResp memberGroupMappings(Long orgId, Long operatorId, Long appId) {
        return appPermissionService.memberGroupMappings(orgId, operatorId, appId);
    }

    @Override
    public boolean removeAppMembers(RemoveAppMembersReq req) {
        if (CollectionUtils.isEmpty(req.getMemberIds())){
            return true;
        }
        List<PerAppPermissionMember> perAppPermissionMembers = appPermissionService.getPermissionGroupMemberList(req.getOrgId(), req.getAppId());
        List<PerAppPermissionMember> updatedPermissionMemberList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(perAppPermissionMembers)){
            MemberParser memberParser = MemberParser.parse(req.getMemberIds());
            if (memberParser == null){
                return false;
            }
            for (PerAppPermissionMember perAppPermissionMember: perAppPermissionMembers){
                List<Long> userIds = parseMemberIds(perAppPermissionMember.getUserIds());
                List<Long> deptIds = parseMemberIds(perAppPermissionMember.getDeptIds());
                List<Long> roleIds = parseMemberIds(perAppPermissionMember.getRoleIds());
                int usize = userIds.size();
                int dsize = deptIds.size();
                int rsize = roleIds.size();
                userIds.removeIf(id -> memberParser.getUserIds().contains(id));
                deptIds.removeIf(id -> memberParser.getDeptIds().contains(id));
                roleIds.removeIf(id -> memberParser.getRoleIds().contains(id));
                if (userIds.size() != usize || deptIds.size() != dsize || roleIds.size() != rsize){
                    PerAppPermissionMember updated = new PerAppPermissionMember();
                    updated.setId(perAppPermissionMember.getId());
                    updated.setUserIds(JSON.toJSONString(userIds));
                    updated.setDeptIds(JSON.toJSONString(deptIds));
                    updated.setRoleIds(JSON.toJSONString(roleIds));
                    appPermissionMemberMapper.updateById(updated);

                    perAppPermissionMember.setUserIds(updated.getUserIds());
                    perAppPermissionMember.setDeptIds(updated.getDeptIds());
                    perAppPermissionMember.setRoleIds(updated.getRoleIds());
                    updatedPermissionMemberList.add(perAppPermissionMember);
                }
            }
        }

        if (updatedPermissionMemberList.size() > 0) {
            updatePermissionTime(req.getAppId());

            // MQTT 推送
            ThreadPools.POOLS.execute(() -> {
                AppResp appResp = appService.getApp(req.getOrgId(), req.getAppId());
                if (Objects.isNull(appResp)) {
                    throw new BusinessException(PermissionResultCode.APP_NOT_EXIST_OR_DELETED);
                }
                for (PerAppPermissionMember member : updatedPermissionMemberList) {
                    PerGroupEvent groupEvent = new PerGroupEvent();
                    groupEvent.setOrgId(req.getOrgId());
                    groupEvent.setAppId(req.getAppId());
                    groupEvent.setProjectId(appResp.getProjectId());
                    groupEvent.setUserId(req.getUserId());
                    groupEvent.setGroupId(member.getPermissionGroupId());
                    groupEvent.setNewData(member);
                    Event event = new Event();
                    event.setCategory(PermissionConstant.EventCategoryPerGroup);
                    event.setType(PermissionConstant.EventTypePerGroupMemberChanged);
                    event.setTimestamp(System.currentTimeMillis() * 1000000L);
                    event.setPayload(groupEvent);
                    try {
                        goPushService.pushMqtt(req.getOrgId(), appResp.getProjectId(), event);
                    } catch (JsonProcessingException e) {
                    }
                }
            });
        }
        return true;
    }

    @Override
    public boolean addAppMembers(AddAppMembersReq req) {
        log.info("[addAppMembers] req: {}", req);
        if (CollectionUtils.isEmpty(req.getMemberIds())){
            return true;
        }
        PerAppPermissionMember perAppPermissionMember = appPermissionService.getPermissionGroupMemberByGroupId(req.getOrgId(), req.getAppId(), req.getPerGroupId());
        if (perAppPermissionMember != null){
            MemberParser memberParser = MemberParser.parse(req.getMemberIds());
            if (memberParser == null){
                return false;
            }
            List<Long> userIds = parseMemberIds(perAppPermissionMember.getUserIds());
            List<Long> deptIds = parseMemberIds(perAppPermissionMember.getDeptIds());
            List<Long> roleIds = parseMemberIds(perAppPermissionMember.getRoleIds());
            userIds.addAll(memberParser.getUserIds());
            deptIds.addAll(memberParser.getDeptIds());
            roleIds.addAll(memberParser.getRoleIds());
            userIds = userIds.stream().distinct().collect(Collectors.toList());
            deptIds = deptIds.stream().distinct().collect(Collectors.toList());
            roleIds = roleIds.stream().distinct().collect(Collectors.toList());
            PerAppPermissionMember updated = new PerAppPermissionMember();
            updated.setId(perAppPermissionMember.getId());
            updated.setUserIds(JSON.toJSONString(userIds));
            updated.setDeptIds(JSON.toJSONString(deptIds));
            updated.setRoleIds(JSON.toJSONString(roleIds));
            appPermissionMemberMapper.updateById(updated);

            updatePermissionTime(req.getAppId());

            perAppPermissionMember.setUserIds(updated.getUserIds());
            perAppPermissionMember.setDeptIds(updated.getDeptIds());
            perAppPermissionMember.setRoleIds(updated.getRoleIds());
            // MQTT 推送
            ThreadPools.POOLS.execute(() -> {
                AppResp appResp = appService.getApp(req.getOrgId(), req.getAppId());
                if (Objects.isNull(appResp)) {
                    throw new BusinessException(PermissionResultCode.APP_NOT_EXIST_OR_DELETED);
                }
                PerGroupEvent groupEvent = new PerGroupEvent();
                groupEvent.setOrgId(req.getOrgId());
                groupEvent.setAppId(req.getAppId());
                groupEvent.setProjectId(appResp.getProjectId());
                groupEvent.setUserId(req.getUserId());
                groupEvent.setGroupId(perAppPermissionMember.getPermissionGroupId());
                groupEvent.setNewData(perAppPermissionMember);
                Event event = new Event();
                event.setCategory(PermissionConstant.EventCategoryPerGroup);
                event.setType(PermissionConstant.EventTypePerGroupMemberChanged);
                event.setTimestamp(System.currentTimeMillis() * 1000000L);
                event.setPayload(groupEvent);
                try {
                    goPushService.pushMqtt(req.getOrgId(), appResp.getProjectId(), event);
                } catch (JsonProcessingException e) {
                }
            });
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyPermissionGroup(CopyAppPermissionGroupReq req) {
        List<PerAppPermissionGroup> groups = appPermissionGroupMapper.selectList(new LambdaQueryWrapper<PerAppPermissionGroup>()
                .eq(PerAppPermissionGroup::getAppId, req.getSourceAppId())
                .eq(PerAppPermissionGroup::getDelFlag, CommonConsts.FALSE));
        if (CollectionUtils.isEmpty(groups)){
            return;
        }
        appPermissionGroupMapper.delete(new LambdaQueryWrapper<PerAppPermissionGroup>().eq(PerAppPermissionGroup::getAppId, req.getTargetAppId()));
        appPermissionMemberMapper.delete(new LambdaQueryWrapper<PerAppPermissionMember>().eq(PerAppPermissionMember::getAppId, req.getTargetAppId()));

        List<PerAppPermissionMember> members = appPermissionMemberMapper.selectList(new LambdaQueryWrapper<PerAppPermissionMember>()
                .eq(PerAppPermissionMember::getAppId, req.getSourceAppId())
                .eq(PerAppPermissionMember::getDelFlag, CommonConsts.FALSE));
        Map<Long, PerAppPermissionGroup> groupMap = com.polaris.lesscode.util.MapUtils.toMap(PerAppPermissionGroup::getId, groups);
        Map<Long, List<PerAppPermissionMember>> memberMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(members)){
            for (PerAppPermissionMember member: members){
                List<PerAppPermissionMember> ms = memberMap.computeIfAbsent(member.getPermissionGroupId(), k -> new ArrayList<>());
                ms.add(member);
            }
        }

        // 这里数据量不会太大，暂时for里insert
        for (PerAppPermissionGroup group: groups){
            Long groupId = group.getId();
            group.setId(null);
            group.setAppId(req.getTargetAppId());
            appPermissionGroupMapper.insert(group);
            List<PerAppPermissionMember> ms = memberMap.get(groupId);
            if (CollectionUtils.isNotEmpty(ms)){
                for (PerAppPermissionMember m: ms){
                    m.setId(null);
                    m.setAppId(req.getTargetAppId());
                    m.setPermissionGroupId(group.getId());
                    appPermissionMemberMapper.insert(m);
                }
            }
        }
    }

    public boolean updatePermissionTime(UpdatePermissionTimeReq req) {
        redisUtil.set(CacheConsts.APP_PERMISSION_UPDATE_TIME + req.getAppId(), req.getUpdateTime().toString());
        return true;
    }

    public void updatePermissionTime(Long appId) {
        redisUtil.set(CacheConsts.APP_PERMISSION_UPDATE_TIME + appId, String.valueOf(System.currentTimeMillis() / 1000));
    }

    public GetPermissionUpdateTimeResp getPermissionUpdateTime(GetPermissionUpdateTimeReq req) {
        GetPermissionUpdateTimeResp result = new GetPermissionUpdateTimeResp();
        String updateTime = redisUtil.get(CacheConsts.APP_PERMISSION_UPDATE_TIME + req.getAppId());
        long updateTimeLong = 0L;
        if (updateTime != null) {
            updateTimeLong = Long.parseLong(updateTime);
        }
        result.setUpdateTime(updateTimeLong);
        return result;
    }

    private List<Long> parseMemberIds(String idJsons){
        List<Long> ids = JSON.parseArray(idJsons, Long.class);
        if (ids == null){
            ids = new ArrayList<>();
        }
        return ids;
    }
}
