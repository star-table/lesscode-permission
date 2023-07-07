package com.polaris.lesscode.permission.controller;

import com.polaris.lesscode.context.RequestContext;
import com.polaris.lesscode.permission.internal.model.resp.AppAuthorityResp;
import com.polaris.lesscode.permission.internal.model.resp.AppPerGroupListItem;
import com.polaris.lesscode.permission.internal.model.resp.AppPerGroupListResp;
import com.polaris.lesscode.permission.internal.model.resp.MemberGroupMappingsResp;
import com.polaris.lesscode.permission.internal.service.AppPermissionInternalService;
import com.polaris.lesscode.permission.model.req.*;
import com.polaris.lesscode.permission.model.resp.AppPerBaseConfigResp;
import com.polaris.lesscode.permission.model.resp.AppPerGroupInfoResp;
//import com.polaris.lesscode.permission.model.resp.CrmRolePermissionListResp;
//import com.polaris.lesscode.permission.open.model.req.crm.CrmSaveAppPermissionGroupReq;
//import com.polaris.lesscode.permission.open.model.resp.cmr.CrmAppPerBaseConfigResp;
//import com.polaris.lesscode.permission.open.model.resp.cmr.CrmAppPerGroupInfoResp;
//import com.polaris.lesscode.permission.open.model.resp.cmr.CrmModuleItem;
//import com.polaris.lesscode.permission.open.service.PermissionOpenService;
import com.polaris.lesscode.permission.service.AppPermissionService;
import com.polaris.lesscode.permission.service.AppService;
import com.polaris.lesscode.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 应用权限组 接口
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 15:36
 */
@Api(tags = "应用权限组")
@RestController
@RequestMapping("/permission/api/v1/app-permission")
public class AppPermissionController {

    private final AppPermissionService permissionGroupService;
    private final AppService appService;
    private final AppPermissionInternalService appPermissionInternalService;

    public AppPermissionController(AppPermissionService permissionGroupService, AppService appService, AppPermissionInternalService appPermissionInternalService) {
        this.permissionGroupService = permissionGroupService;
        this.appService = appService;
        this.appPermissionInternalService = appPermissionInternalService;
    }

    @ApiOperation(value = "创建应用权限组", notes = "创建应用权限组")
    @PostMapping("/{appPackageId}/{appId}/auth-group/create")
    public Result<Long> createPermissionGroup(@PathVariable("appId") Long appId,
                                              @Validated @RequestBody SaveAppPermissionGroupReq req) {

        return Result.ok(permissionGroupService.createPermissionGroup(RequestContext.currentOrgId(),
                RequestContext.currentUserId(),
                appId, req));
    }

    @ApiOperation(value = "修改应用权限组", notes = "修改应用权限组")
    @PostMapping("/{appPackageId}/{appId}/auth-group/update-info/{groupId}")
    public Result<Boolean> updatePermissionGroup(@PathVariable("appId") Long appId,
                                                 @PathVariable("groupId") Long groupId,
                                                 @Validated @RequestBody SaveAppPermissionGroupReq req) {
        return Result.ok(permissionGroupService.updatePermissionGroup(RequestContext.currentOrgId(),
                RequestContext.currentUserId(),
                appId, groupId, req));
    }

    @ApiOperation(value = "删除应用权限组", notes = "删除应用权限组")
    @DeleteMapping("/{appPackageId}/{appId}/auth-group/{groupId}")
    public Result<Boolean> deletePermissionGroup(@PathVariable("appPackageId") Long appPackageId,
                                                 @PathVariable("appId") Long appId,
                                                 @PathVariable("groupId") Long groupId) {
        return Result.ok(permissionGroupService.deletePermissionGroup(RequestContext.currentOrgId(),
                RequestContext.currentUserId(),
                appId, groupId));
    }

    @ApiOperation(value = "获取应用权限组列表", notes = "获取应用权限组列表")
    @GetMapping("/{appPackageId}/{appId}/auth-group/list")
    public Result<List<AppPerGroupListItem>> getPermissionGroupList(@PathVariable("appId") Long appId) {
        return Result.ok(
                permissionGroupService.getPermissionGroupList(RequestContext.currentOrgId(), RequestContext.currentUserId(), appId).getList());
    }

//    @ApiOperation(value = "通过名称获取应用权限组列表", notes = "通过名称获取应用权限组列表")
//    @PostMapping("/{appPackageId}/{appId}/auth-group/list")
//    public Result<List<AppPerGroupListItem>> getPermissionGroupList(@PathVariable("appId") Long appId,
//                                                                    @RequestBody AppPerGroupListItemByNameReq req) {
//        return Result.ok(permissionGroupService.getPermissionGroupList(RequestContext.currentOrgId(), RequestContext.currentUserId(),
//                appId, req.getName()).getList());
//    }

//    @ApiOperation(value = "获取应用权限组列表", notes = "获取应用权限组列表")
//    @GetMapping("/{appPackageId}/{appId}/auth-group/list-info")
//    public Result<AppPerGroupListResp> getPermissionGroupListResp(@PathVariable("appId") Long appId,
//                                                                  @RequestParam(value = "name", required = false) String name) {
//        return Result.ok(
//                permissionGroupService.getPermissionGroupList(RequestContext.currentOrgId(), RequestContext.currentUserId(), appId, name));
//    }

    @ApiOperation(value = "获取应用权限组详情", notes = "获取应用权限组详情")
    @GetMapping("/{appPackageId}/{appId}/auth-group/info/{groupId}")
    public Result<AppPerGroupInfoResp> getPermissionGroupInfo(@PathVariable("appId") Long appId,
                                                              @PathVariable("groupId") Long groupId) {
        return Result.ok(permissionGroupService.getPermissionGroupInfo(RequestContext.currentOrgId(), appId, groupId));
    }

    @ApiOperation(value = "批量获取应用权限组详情", notes = "批量获取应用权限组详情")
    @PostMapping("/{appPackageId}/{appId}/auth-group/info/list")
    public Result<List<AppPerGroupInfoResp>> getPermissionGroupInfoList(@PathVariable("appId") Long appId,
                                                                        @RequestBody AppPermissionGroupInfoListReq req) {
        return Result.ok(permissionGroupService.getPermissionGroupInfoList(RequestContext.currentOrgId(), appId, req));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    @ApiOperation(value = "修改应用权限组成员列表", notes = "修改应用权限组成员列表")
//    @PostMapping("/{appPackageId}/{appId}/auth-group/update-members/{groupId}")
//    public Result<Boolean> updatePermissionGroupMembers(@PathVariable("appId") Long appId,
//                                                        @PathVariable("groupId") Long groupId,
//                                                        @Validated @RequestBody UpdateAppPerMembersReq req) {
//        return Result.ok(permissionGroupService.updatePermissionGroupMembers(RequestContext.currentOrgId(),
//                RequestContext.currentUserId(), appId, groupId, req));
//    }

    @ApiOperation("成员切换权限组")
    @PostMapping("/{appPackageId}/{appId}/auth-group/member-switch-group/{groupId}")
    public Result<Boolean> memberSwitchGroup(@PathVariable("appId") Long appId,
                                                        @PathVariable("groupId") Long groupId,
                                                        @Validated @RequestBody MemberSwitchGroupReq req) {
        return Result.ok(permissionGroupService.memberSwitchGroup(RequestContext.currentOrgId(),
                RequestContext.currentUserId(), appId, groupId, req));
    }

    @ApiOperation("获取成员与权限组的映射关系")
    @GetMapping("/{appPackageId}/{appId}/auth-group/member-group-mappings")
    public Result<MemberGroupMappingsResp> memberGroupMappings(@PathVariable("appId") Long appId) {
        return Result.ok(permissionGroupService.memberGroupMappings(RequestContext.currentOrgId(),
                RequestContext.currentUserId(), appId));
    }

    @ApiOperation(value = "获取应用权限基础配置", notes = "获取应用权限基础配置")
    @GetMapping("/{appPackageId}/{appId}/auth-group/base-config")
    public Result<AppPerBaseConfigResp> getPermissionBaseConfig(@PathVariable("appId") Long appId) {
        return Result.ok(permissionGroupService.getPermissionBaseConfig(RequestContext.currentOrgId(), appId));
    }

    @ApiOperation(value = "获取应用权限信息", notes = "获取应用权限信息")
    @GetMapping("/{appPackageId}/{appId}/getUserAppPermission")
    public Result<AppAuthorityResp> getAppAuthorityResp(@PathVariable("appId") Long appId){
        return Result.ok(appPermissionInternalService.getAppAuthority(RequestContext.currentOrgId(), appId, null, RequestContext.currentUserId()));
    }

}
