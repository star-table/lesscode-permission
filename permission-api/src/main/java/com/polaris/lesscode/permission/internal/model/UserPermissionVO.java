package com.polaris.lesscode.permission.internal.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-09-18 11:29
 */
@Data
public class UserPermissionVO implements Serializable {

    private static final long serialVersionUID = -983454280351406147L;

    @ApiModelProperty("组织Id")
    private Long orgId;

    @ApiModelProperty("用户Id")
    private Long userId;

    @ApiModelProperty("部门Id列表")
    private List<Long> refDeptIds;

    @ApiModelProperty("角色Id列表")
    private List<Long> refRoleIds;

    @ApiModelProperty("是否是组织拥有者")
    private Boolean isOrgOwner;

    @ApiModelProperty("是否是系统管理员")
    private Boolean isSysAdmin;

    @ApiModelProperty("是否是子管理员")
    private Boolean isSubAdmin;

    @ApiModelProperty("是否是外部协作人")
    private Boolean isOutCollaborator;

    @ApiModelProperty("管理部门的权限")
    private Boolean hasDeptOptAuth;

    @ApiModelProperty("管理角色的权限")
    private Boolean hasRoleOptAuth;

    @ApiModelProperty("管理应用包的权限")
    private Boolean hasAppPackageOptAuth;

    @ApiModelProperty("管理的角色列表")
    private List<Long> manageRoles;

    @ApiModelProperty("管理的部门列表")
    private List<Long> manageDepts;

    @ApiModelProperty("管理的应用包列表")
    private List<Long> manageAppPackages;

    @ApiModelProperty("管理的应用列表")
    private List<Long> manageApps;

    @ApiModelProperty("可见的应用包列表")
    private List<Long> visibleAppPackageIds;

    public UserPermissionVO() {
        refDeptIds = new ArrayList<>();
        refRoleIds = new ArrayList<>();
        isOrgOwner = false;
        isSysAdmin = false;
        hasDeptOptAuth = false;
        hasRoleOptAuth = false;
        hasAppPackageOptAuth = false;
        manageRoles = new ArrayList<>();
        manageDepts = new ArrayList<>();
        manageAppPackages = new ArrayList<>();
        manageApps = new ArrayList<>();
        visibleAppPackageIds = new ArrayList<>();
    }

}
