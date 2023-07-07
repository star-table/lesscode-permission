package com.polaris.lesscode.permission.internal.model;

import java.util.*;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-09-21 11:25
 */
public class OrgUserPermissionContext {
    private final Set<Long> refDeptIds;
    private final Set<Long> refRoleIds;

    private final boolean isOrgOwner;
    private final boolean isSysAdmin;
    private final boolean isSubAdmin;
    private final boolean isRoot;

    private final boolean hasDeptOptAuth;
    private final boolean hasRoleOptAuth;
    private final boolean hasAppPackageOptAuth;

    private final Set<Long> manageDeptIds;
    private final Set<Long> manageRoleIds;
    private final Set<Long> manageAppPackageIds;
    private final Set<Long> manageAppIds;

    private final Set<Long> queryPkgIds;

    public OrgUserPermissionContext(UserPermissionVO userPermissionVO) {
        refRoleIds = new LinkedHashSet<>(userPermissionVO.getRefRoleIds());
        refDeptIds = new LinkedHashSet<>(userPermissionVO.getRefDeptIds());

        isOrgOwner = Objects.equals(userPermissionVO.getIsOrgOwner(), Boolean.TRUE);
        isSysAdmin = Objects.equals(userPermissionVO.getIsSysAdmin(), Boolean.TRUE);
        isSubAdmin = Objects.equals(userPermissionVO.getIsSubAdmin(), Boolean.TRUE);
        isRoot = isOrgOwner || isSysAdmin;

        hasDeptOptAuth = Objects.equals(userPermissionVO.getHasDeptOptAuth(), Boolean.TRUE);
        hasRoleOptAuth = Objects.equals(userPermissionVO.getHasRoleOptAuth(), Boolean.TRUE);
        hasAppPackageOptAuth = Objects.equals(userPermissionVO.getHasAppPackageOptAuth(), Boolean.TRUE);

        // 循环创建,防止外部影响
        manageDeptIds = new LinkedHashSet<>(userPermissionVO.getManageDepts());
        manageRoleIds = new LinkedHashSet<>(userPermissionVO.getManageRoles());
        manageAppPackageIds = new LinkedHashSet<>(userPermissionVO.getManageAppPackages());
        manageAppIds = new LinkedHashSet<>(userPermissionVO.getManageApps());

        queryPkgIds = new LinkedHashSet<>(userPermissionVO.getManageAppPackages());
        queryPkgIds.addAll(userPermissionVO.getVisibleAppPackageIds());
    }

    /**
     * 获取所属部门
     *
     * @return
     */
    public List<Long> getDeptIds() {
        return new ArrayList<>(refDeptIds);
    }


    /**
     * 获取所属角色
     *
     * @return
     */
    public List<Long> getRoleIds() {
        return new ArrayList<>(refRoleIds);
    }

    /**
     * 包含所有权限
     *
     * @return {@code true}
     */
    public boolean hasAllPermission() {
        return isRoot;
    }

    /**
     * 是否是组织拥有者
     *
     * @return {@code true}
     */
    public boolean isOrgOwner() {
        return isOrgOwner;
    }

    /**
     * 是否是系统管理员
     *
     * @return {@code true}
     */
    public boolean isSysAdmin() {
        return isSysAdmin;
    }

    /**
     * 创建部门的权限
     *
     * @return
     */
    public boolean hasCreateDeptPermission() {
        return hasAllPermission() || (isSubAdmin && hasDeptOptAuth);
    }

    /**
     * 管理部门的权限
     *
     * @return
     */
    public boolean hasManageDept(Long deptId) {
        return hasAllPermission() || (isSubAdmin && hasDeptOptAuth && (manageDeptIds.isEmpty() || manageDeptIds.contains(
                deptId)));
    }

    /**
     * 管理所有部门的权限
     *
     * @return
     */
    public boolean hasManageAllDept() {
        return hasAllPermission() || (isSubAdmin && hasDeptOptAuth && manageDeptIds.isEmpty());
    }

    /**
     * 管理角色的权限
     *
     * @return
     */
    public boolean hasManageRole(Long roleId) {
        return hasAllPermission() || (isSubAdmin && hasRoleOptAuth && (manageRoleIds.isEmpty() || manageRoleIds.contains(
                roleId)));
    }

    /**
     * 管理所有角色的权限
     *
     * @return
     */
    public boolean hasManageAllRole() {
        return hasAllPermission() || (isSubAdmin && hasRoleOptAuth && manageRoleIds.isEmpty());
    }

    /**
     * 创建应用包的权限
     *
     * @return
     */
    public boolean hasCreateAppPackagePermission() {
        return hasAllPermission() || (isSubAdmin && hasAppPackageOptAuth);
    }

    /**
     * 删除应用包的权限
     *
     * @return
     */
    public boolean hasDeleteAppPackagePermission(Long appPackageId) {
        return hasAllPermission() || (isSubAdmin && hasAppPackageOptAuth && manageAppPackageIds.contains(appPackageId));
    }

    /**
     * 管理应用包的权限
     *
     * @return
     */
    public boolean hasManageAppPackagePermission(Long appPackageId) {
        return hasAllPermission() || (isSubAdmin && manageAppPackageIds.contains(appPackageId));
    }

    /**
     * 删除应用的权限
     *
     * @return
     */
    public boolean hasDeleteAppPermission(Long appId) {
        return hasAllPermission() || (isSubAdmin && hasAppPackageOptAuth && manageAppIds.contains(appId));
    }

    /**
     * 管理应用的权限
     *
     * @return
     */
    public boolean hasManageAppPermission(Long appId) {
        return hasAllPermission() || (isSubAdmin && manageAppIds.contains(appId));
    }

    /**
     * 查看应用包的权限
     *
     * @return
     */
    public boolean hasQueryAppPackagePermission(Long appPackageId) {
        return hasAllPermission() || queryPkgIds.contains(appPackageId);
    }


    /**
     * 获取管理的部门ID
     *
     * @return
     */
    public List<Long> getManageDeptIds() {
        return new ArrayList<>(manageDeptIds);
    }

    /**
     * 获取管理的角色ID
     *
     * @return
     */
    public List<Long> getManageRoleIds() {
        return new ArrayList<>(manageRoleIds);
    }

    /**
     * 获取管理的应用包ID
     *
     * @return
     */
    public List<Long> getManageAppPackageIds() {
        return new ArrayList<>(manageAppPackageIds);
    }

    /**
     * 获取管理的应用ID
     *
     * @return
     */
    public List<Long> getManageAppIds() {
        return new ArrayList<>(manageAppIds);
    }

    /**
     * 获取查看的应用包ID
     * 包括可管理的
     *
     * @return
     */
    public List<Long> getQueryAppPackageIds() {
        return new ArrayList<>(queryPkgIds);
    }


}
