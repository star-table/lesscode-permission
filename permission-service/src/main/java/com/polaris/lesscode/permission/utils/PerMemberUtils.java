package com.polaris.lesscode.permission.utils;

import com.polaris.lesscode.permission.internal.enums.PermissionMemberCode;
import com.polaris.lesscode.permission.internal.model.bo.DeptItemBo;
import com.polaris.lesscode.permission.internal.model.bo.PermissionMembersBo;
import com.polaris.lesscode.permission.internal.model.bo.RoleItemBo;
import com.polaris.lesscode.permission.internal.model.bo.UserItemBo;
import com.polaris.lesscode.permission.service.UserService;
import com.polaris.lesscode.uc.internal.resp.DeptInfoResp;
import com.polaris.lesscode.uc.internal.resp.RoleInfoResp;
import com.polaris.lesscode.uc.internal.resp.UserInfoResp;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-12-23 12:00
 */
public final class PerMemberUtils {

    public static Map<Long, PermissionMembersBo> convertToMembersBo(UserService userService, Long orgId, Map<Long, Map<String, List<Long>>> memberIdsMap) {
        Set<Long> allUserIds = new HashSet<>();
        Set<Long> allDeptIds = new HashSet<>();
        Set<Long> allRoleIds = new HashSet<>();
        memberIdsMap.forEach((k, v) -> {
            allUserIds.addAll(v.getOrDefault(PermissionMemberCode.USER.getCode(), Collections.emptyList()));
            allDeptIds.addAll(v.getOrDefault(PermissionMemberCode.DEPT.getCode(), Collections.emptyList()));
            allRoleIds.addAll(v.getOrDefault(PermissionMemberCode.ROLE.getCode(), Collections.emptyList()));
        });
        Map<Long, UserInfoResp> userMap = userService.getUserMap(orgId, allUserIds);
        Map<Long, DeptInfoResp> deptMap = userService.getDeptMap(orgId, allDeptIds);
        Map<Long, RoleInfoResp> roleMap = userService.getRoleMap(orgId, allRoleIds);

        Map<Long, PermissionMembersBo> perMemberMap = new HashMap<>();
        memberIdsMap.forEach((k, v) -> {
            PermissionMembersBo membersBo = new PermissionMembersBo();

            List<Long> userIds = v.getOrDefault(PermissionMemberCode.USER.getCode(), Collections.emptyList());
            List<UserItemBo> userItemBos = userIds.stream().filter(userMap::containsKey).map(id -> {
                UserInfoResp userInfoResp = userMap.get(id);
                UserItemBo item = new UserItemBo();
                item.setId(id);
                item.setName(userInfoResp.getName());
                return item;
            }).collect(Collectors.toList());
            membersBo.setUsers(userItemBos);

            List<Long> deptIds = v.getOrDefault(PermissionMemberCode.DEPT.getCode(), Collections.emptyList());
            List<DeptItemBo> deptItemBos = deptIds.stream().filter(deptMap::containsKey).map(id -> {
                DeptInfoResp dept = deptMap.get(id);
                DeptItemBo item = new DeptItemBo();
                item.setId(id);
                item.setName(dept.getName());
                return item;
            }).collect(Collectors.toList());
            membersBo.setDepts(deptItemBos);

            List<Long> roleIds = v.getOrDefault(PermissionMemberCode.ROLE.getCode(), Collections.emptyList());
            List<RoleItemBo> roleItemBos = roleIds.stream().filter(roleMap::containsKey).map(id -> {
                RoleInfoResp role = roleMap.get(id);
                RoleItemBo item = new RoleItemBo();
                item.setId(id);
                item.setName(role.getName());
                return item;
            }).collect(Collectors.toList());
            membersBo.setRoles(roleItemBos);

            perMemberMap.put(k, membersBo);
        });
        return perMemberMap;
    }

}
