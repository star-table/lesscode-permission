package com.polaris.lesscode.permission.service.impl;

import com.polaris.lesscode.app.internal.resp.AppPackageResp;
import com.polaris.lesscode.app.internal.resp.AppResp;
import com.polaris.lesscode.exception.BusinessException;
import com.polaris.lesscode.permission.annotation.AutoCache;
import com.polaris.lesscode.permission.constant.CacheConsts;
import com.polaris.lesscode.permission.entity.PerAppPermissionGroup;
import com.polaris.lesscode.permission.internal.enums.AppPerDefaultGroupLangCode;
import com.polaris.lesscode.permission.internal.model.OrgUserPermissionContext;
import com.polaris.lesscode.permission.internal.model.UserPermissionVO;
import com.polaris.lesscode.permission.mapper.AppPermissionGroupMapper;
import com.polaris.lesscode.permission.service.AppService;
import com.polaris.lesscode.permission.service.UserService;
import com.polaris.lesscode.uc.internal.feign.UserCenterProvider;
import com.polaris.lesscode.uc.internal.req.*;
import com.polaris.lesscode.uc.internal.resp.DeptInfoResp;
import com.polaris.lesscode.uc.internal.resp.RoleInfoResp;
import com.polaris.lesscode.uc.internal.resp.UserAuthorityResp;
import com.polaris.lesscode.uc.internal.resp.UserInfoResp;
import com.polaris.lesscode.util.JsonUtils;
import com.polaris.lesscode.vo.BaseResultCode;
import com.polaris.lesscode.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-09-22 11:09
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserCenterProvider userCenterProvider;
    private final AppPermissionGroupMapper appPermissionGroupMapper;
    private final AppService appService;

    public UserServiceImpl(UserCenterProvider userCenterProvider, AppPermissionGroupMapper appPermissionGroupMapper, AppService appService) {
        this.userCenterProvider = userCenterProvider;
        this.appPermissionGroupMapper = appPermissionGroupMapper;
        this.appService = appService;
    }

    @Override
    public OrgUserPermissionContext getPermissionContext(Long orgId, Long userId) {
        return new OrgUserPermissionContext(getUserPermission(orgId, userId));
    }

    @Override
    //@AutoCache( value = CacheConsts.ORG_AUTH_CACHE_KEY, type = AutoCache.CacheType.STRING, expire = 20 )
    public UserPermissionVO getUserPermission(Long orgId, Long userId) {
        UserAuthorityResp authorityResp = getUserAuthority(orgId, userId);
        UserPermissionVO vo = new UserPermissionVO();
        vo.setOrgId(authorityResp.getOrgId());
        vo.setUserId(authorityResp.getUserId());
        vo.setRefDeptIds(authorityResp.getRefDeptIds());
        vo.setRefRoleIds(authorityResp.getRefRoleIds());

        vo.setIsOrgOwner(authorityResp.getIsOrgOwner());
        vo.setIsSysAdmin(authorityResp.getIsSysAdmin());
        vo.setIsSubAdmin(authorityResp.getIsSubAdmin());
        vo.setIsOutCollaborator(authorityResp.getIsOutCollaborator());

        vo.setHasDeptOptAuth(authorityResp.getHasDeptOptAuth());
        vo.setHasRoleOptAuth(authorityResp.getHasRoleOptAuth());
        vo.setHasAppPackageOptAuth(authorityResp.getHasAppPackageOptAuth());

        vo.setManageDepts(authorityResp.getManageDepts());
        vo.setManageRoles(authorityResp.getManageRoles());

        log.info("[getUserPermission] userId:{}, managerAppIds:{}", userId, authorityResp.getManageApps());

        // 获取所有应用包和应用
        List<AppResp> appList = appService.getAppList(orgId);
        // 管理员查看所有
        if (vo.getIsOrgOwner() || vo.getIsSysAdmin()) {
            List<Long> appIds = appList.stream().map(AppResp::getId).collect(Collectors.toList());
            vo.setManageApps(appIds);
            return vo;
        }

        // 管理组管理的应用
        Set<Long> manageAppIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(authorityResp.getManageApps())){
            manageAppIds.addAll(authorityResp.getManageApps());
        }

        // 获取所在的应用权限组应用ID和管理组直接指定的app
        List<PerAppPermissionGroup> appPerGroupList = appPermissionGroupMapper.selectUserAppPerGroupsByUser(orgId, userId, JsonUtils.toJson(authorityResp.getRefDeptIds()), JsonUtils.toJson(authorityResp.getRefRoleIds()));
        if (CollectionUtils.isNotEmpty(appPerGroupList)){
            for (PerAppPermissionGroup permissionGroup: appPerGroupList){
                if (Objects.equals(permissionGroup.getLangCode(), AppPerDefaultGroupLangCode.FORM_ADMINISTRATOR.getCode())){
                    manageAppIds.add(permissionGroup.getAppId());
                }
            }
        }

        log.info("[getUserPermission2] userId:{}, managerAppIds:{}", userId, manageAppIds);

        // 子应用也加进去
//        Map<Long, List<Long>> parentIdMap = new HashMap<>();	// parent -> list of child
//        for (AppResp app: appList){
//            if (app.getParentId() > 0){
//                List<Long> ids = parentIdMap.computeIfAbsent(app.getParentId(), k -> new ArrayList<>());
//                ids.add(app.getId());
//            }
//        }
//        Queue<Long> queue = new LinkedBlockingQueue<>(manageAppIds);
//        Long parentAppId = null;
//        while((parentAppId = queue.poll()) != null){
//            List<Long> ids = parentIdMap.get(parentAppId);
//            if (CollectionUtils.isNotEmpty(ids)){
//                ids.removeIf(manageAppIds::contains);
//                queue.addAll(ids);
//                manageAppIds.addAll(ids);
//            }
//        }

        log.info("[getUserPermission3] userId:{}, managerAppIds:{}", userId, manageAppIds);

        vo.setManageApps(new ArrayList<>(manageAppIds));
        return vo;
    }

    public void wrapParentList(Long pkgId, Map<Long, AppPackageResp> pkgMap, Set<AppPackageResp> pkgSet) {
        AppPackageResp appPkg = pkgMap.get(pkgId);
        if (Objects.isNull(appPkg)) {
            return;
        }
        pkgSet.add(appPkg);
        if (Objects.equals(appPkg.getParentId(), 0L)) {
            return;
        }
        wrapParentList(appPkg.getParentId(), pkgMap, pkgSet);
    }

    public void wrapManagerPkgSet(Long pkgId, Map<Long, AppPackageResp> perMap, Map<Long, List<AppPackageResp>> childrenMap, Set<AppPackageResp> managePkgSet) {
        List<AppPackageResp> childrenList = childrenMap.get(pkgId);
        if (Objects.isNull(childrenList) || childrenList.isEmpty()) {
            return;
        }
        managePkgSet.addAll(childrenList);
        for (AppPackageResp child : childrenMap.get(pkgId)) {
            if (!perMap.containsKey(child.getId())) {
                return;
            }
            wrapManagerPkgSet(child.getId(), perMap, childrenMap, managePkgSet);
        }
    }

    public Map<Long, List<AppPackageResp>> toPkgChildrenMap(final List<AppPackageResp> pkgList, final Map<Long, AppPackageResp> perMap) {
        Map<Long, List<AppPackageResp>> childrenMap = new HashMap<>();
        for (AppPackageResp pkg : pkgList) {
            if (!childrenMap.containsKey(pkg.getId())) {
                childrenMap.put(pkg.getId(), new ArrayList<>());
            }
            // 已经是顶节点
            if (Objects.equals(pkg.getParentId(), 0L) || perMap.containsKey(pkg.getParentId())) {
                continue;
            }
            // 加入父节点
            childrenMap.computeIfAbsent(pkg.getParentId(), k -> new ArrayList<>()).add(pkg);
        }
        return childrenMap;
    }

    /**
     * 获取用户所属部门和角色
     *
     * @param orgId  组织ID
     * @param userId 用户ID
     * @return {@code UserAuthorityResp}
     */
    @Override
    public UserAuthorityResp getUserAuthority(Long orgId, Long userId) {
        UserAuthorityReq req = UserAuthorityReq.builder().orgId(orgId).userId(userId).build();
        Result<UserAuthorityResp> resp = userCenterProvider.getUserAuthority(req);
        if (!Objects.equals(resp.getCode(), 0)) {
            throw new BusinessException(BaseResultCode.INTERNAL_SERVICE_ERROR);
        }
        return resp.getData();
    }


    @Override
    public Map<Long, UserInfoResp> getUserMap(Long orgId, Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        UserListByIdsReq req = UserListByIdsReq.builder().orgId(orgId).ids(new ArrayList<>(userIds)).build();
        Result<List<UserInfoResp>> resp = userCenterProvider.getUserListByIds(req);
        if (!Objects.equals(resp.getCode(), 0)) {
            throw new BusinessException(BaseResultCode.INTERNAL_SERVICE_ERROR);
        }
        return resp.getData().stream().collect(Collectors.toMap(UserInfoResp::getId, v -> v));
    }

    @Override
    public Map<Long, DeptInfoResp> getDeptMap(Long orgId, Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        DeptListByIdsReq req = DeptListByIdsReq.builder().orgId(orgId).ids(new ArrayList<>(userIds)).build();
        Result<List<DeptInfoResp>> resp = userCenterProvider.getDeptListByIds(req);
        if (!Objects.equals(resp.getCode(), 0)) {
            throw new BusinessException(BaseResultCode.INTERNAL_SERVICE_ERROR);
        }
        return resp.getData().stream().collect(Collectors.toMap(DeptInfoResp::getId, v -> v));
    }

    @Override
    public Map<Long, RoleInfoResp> getRoleMap(Long orgId, Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        RoleListByIdsReq req = RoleListByIdsReq.builder().orgId(orgId).ids(new ArrayList<>(userIds)).build();
        Result<List<RoleInfoResp>> resp = userCenterProvider.getRoleListByIds(req);
        if (!Objects.equals(resp.getCode(), 0)) {
            throw new BusinessException(BaseResultCode.INTERNAL_SERVICE_ERROR);
        }
        return resp.getData().stream().collect(Collectors.toMap(RoleInfoResp::getId, v -> v));
    }

    @Override
    public Boolean addPkgToManageGroup(Long orgId, Long userId, Long pkgId) {
        log.info("[添加应用包至管理组] -> orgId:{} userId:{} pkgId:{}", orgId, userId, pkgId);

        AddPkgReq req = AddPkgReq.builder().orgId(orgId).userId(userId).pkgId(pkgId).build();
        Result<Boolean> resp = userCenterProvider.addPkgToManageGroup(req);
        if (!Objects.equals(resp.getCode(), 0)) {
            throw new BusinessException(BaseResultCode.INTERNAL_SERVICE_ERROR);
        }
        return resp.getData();
    }

    @Override
    public Boolean deletePkgFromManageGroup(Long orgId, Long userId, Long pkgId) {
        log.info("[从管理组删除应用包] -> orgId:{} userId:{} pkgId:{}", orgId, userId, pkgId);

        DeletePkgReq req = DeletePkgReq.builder().orgId(orgId).userId(userId).pkgId(pkgId).build();
        Result<Boolean> resp = userCenterProvider.deletePkgFromManageGroup(req);
        if (!Objects.equals(resp.getCode(), 0)) {
            throw new BusinessException(BaseResultCode.INTERNAL_SERVICE_ERROR);
        }
        return resp.getData();
    }

    @Override
    public Boolean addAppToManageGroup(Long orgId, Long userId, Long appId) {
        log.info("[添加应用至管理组] -> orgId:{} userId:{} appId:{}", orgId, userId, appId);

        AddAppReq req = AddAppReq.builder().orgId(orgId).userId(userId).appId(appId).build();
        Result<Boolean> resp = userCenterProvider.addAppToManageGroup(req);
        if (!Objects.equals(resp.getCode(), 0)) {
            throw new BusinessException(BaseResultCode.INTERNAL_SERVICE_ERROR);
        }
        return resp.getData();
    }

    @Override
    public Boolean deleteAppFromManageGroup(Long orgId, Long userId, Long appId) {
        log.info("[从管理组删除应用] -> orgId:{} userId:{} appId:{}", orgId, userId, appId);

        DeleteAppReq req = DeleteAppReq.builder().orgId(orgId).userId(userId).appId(appId).build();
        Result<Boolean> resp = userCenterProvider.deleteAppFromManageGroup(req);
        if (!Objects.equals(resp.getCode(), 0)) {
            throw new BusinessException(BaseResultCode.INTERNAL_SERVICE_ERROR);
        }
        return resp.getData();
    }

}
