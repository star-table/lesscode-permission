package com.polaris.lesscode.permission.service;

import com.polaris.lesscode.permission.internal.model.OrgUserPermissionContext;
import com.polaris.lesscode.permission.internal.model.UserPermissionVO;
import com.polaris.lesscode.uc.internal.resp.DeptInfoResp;
import com.polaris.lesscode.uc.internal.resp.RoleInfoResp;
import com.polaris.lesscode.uc.internal.resp.UserAuthorityResp;
import com.polaris.lesscode.uc.internal.resp.UserInfoResp;

import java.util.Collection;
import java.util.Map;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-09-22 11:07
 */
public interface UserService {

    OrgUserPermissionContext getPermissionContext(Long orgId, Long userId);

    UserPermissionVO getUserPermission(Long orgId, Long userId);

    Map<Long, UserInfoResp> getUserMap(Long orgId, Collection<Long> userIds);


    Map<Long, DeptInfoResp> getDeptMap(Long orgId, Collection<Long> userIds);

    Map<Long, RoleInfoResp> getRoleMap(Long orgId, Collection<Long> userIds);

    Boolean addPkgToManageGroup(Long orgId, Long userId, Long pkgId);

    Boolean deletePkgFromManageGroup(Long orgId, Long userId, Long pkgId);

    Boolean addAppToManageGroup(Long orgId, Long userId, Long appId);

    Boolean deleteAppFromManageGroup(Long orgId, Long userId, Long appId);

    UserAuthorityResp getUserAuthority(Long orgId, Long userId);
}
