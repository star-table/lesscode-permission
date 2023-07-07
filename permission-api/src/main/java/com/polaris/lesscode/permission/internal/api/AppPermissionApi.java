package com.polaris.lesscode.permission.internal.api;

import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.permission.internal.model.bo.ViewAuth;
import com.polaris.lesscode.permission.internal.model.req.*;
import com.polaris.lesscode.permission.internal.model.resp.*;
import com.polaris.lesscode.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 应用权限组（内部调用）
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 15:37
 */
@Api(tags = "应用权限组（内部调用）")
@RequestMapping("/permission/inner/api/v1/app-permission")
public interface AppPermissionApi {

    @ApiOperation(value = "初始化应用权限", notes = "初始化应用权限")
    @PostMapping("init")
    Result<Boolean> initAppPermission(@Validated @RequestBody InitAppPermissionReq req);

    @ApiOperation(value = "通过id更新应用权限", notes = "更新应用权限")
    @PutMapping
    Result<Boolean> updateAppPermission(@Validated @RequestBody UpdateAppPermissionReq req);

    @ApiOperation(value = "通过langCode更新应用权限", notes = "通过langCode更新应用权限")
    @PutMapping("/updateByLangCode")
    Result<Boolean> updateAppPermissionByLangCode(@Validated @RequestBody UpdateAppPermissionByLangCodeReq req);

    @ApiOperation(value = "删除应用权限", notes = "删除应用时，同步的删除应用权限")
    @DeleteMapping()
    Result<Boolean> deleteAppPermission(@Validated @RequestBody DeleteAppPermissionReq req);

    @ApiOperation("创建应用权限组")
    @PostMapping("/createPermissionGroup")
    Result<List<AppPermissionGroupResp>> createAppPermissionGroup(@RequestBody List<CreateAppPermissionGroupReq> req);

    @ApiOperation("复制应用权限组")
    @PostMapping("copy-app-permission-group")
    Result<Boolean> copyPermissionGroup(@RequestBody CopyAppPermissionGroupReq req);

    @ApiOperation(value = "初始化应用字段权限(创建表格时)", notes = "初始化应用字段权限(创建表格时)")
    @PostMapping("/initAppPermissionFieldAuthCreateTable")
    Result<Boolean> initAppPermissionFieldAuthCreateTable(@Validated @RequestBody InitAppPermissionFieldAuthCreateTableReq req);

    @ApiOperation(value = "初始化应用字段权限(删除表格时)", notes = "初始化应用字段权限(删除表格时)")
    @PostMapping("/initAppPermissionFieldAuthDeleteTable")
    Result<Boolean> initAppPermissionFieldAuthDeleteTable(@Validated @RequestBody InitAppPermissionFieldAuthDeleteTableReq req);

    @ApiOperation("获取应用权限组列表")
    @GetMapping("/getAppPermissionGroupList")
    Result<List<AppPerGroupListItem>> getAppPermissionGroupList(@RequestParam("orgId") Long orgId, @RequestParam("appId") Long appId);

    @ApiOperation("批量获取应用权限组")
    @PostMapping("/getPermissionGroupBatch")
    Result<List<AppPermissionGroupResp>> getAppPermissionGroupBatch(@RequestBody List<Long> appIds);

    @ApiOperation(value = "获取应用权限组-字段权限", notes = "获取应用权限组-字段权限")
    @GetMapping("fieldAuth")
    Result<Map<String, Map<String, Integer>>> getFieldAuth(@RequestParam("orgId") Long orgId,
                                              @RequestParam("appId") Long appId, @RequestParam("userId") Long userId);

    @ApiOperation(value = "获取应用权限组-表格权限", notes = "获取应用权限组-表格权限")
    @GetMapping("tableAuth")
    Result<List<String>> getTableAuth(@RequestParam("orgId") Long orgId,
                                              @RequestParam("appId") Long appId, @RequestParam("userId") Long userId);

    @ApiOperation(value = "获取应用权限组-视图权限", notes = "获取应用权限组-视图权限")
    @GetMapping("viewAuth")
    Result<Map<String, ViewAuth>> getViewAuth(@RequestParam("orgId") Long orgId,
                                              @RequestParam("appId") Long appId, @RequestParam("userId") Long userId);

    @ApiOperation(value = "获取应用权限组-操作权限", notes = "获取应用权限组-操作权限")
    @GetMapping("optAuth")
    Result<FromPerOptAuthVO> getOptAuth(@RequestParam("orgId") Long orgId,
                                        @RequestParam("appId") Long appId, @RequestParam("userId") Long userId);

    @ApiOperation(value = "获取应用权限组-操作权限", notes = "获取应用权限组-操作权限")
    @GetMapping("optAuthMap")
    Result<List<String>> getOptAuthList(@RequestParam("orgId") Long orgId,
                                        @RequestParam("appId") Long appId, @RequestParam("userId") Long userId);


    @ApiOperation(value = "获取应用权限组-数据权限", notes = "获取应用权限组-数据权限 返回的集合使用OR进行链接")
    @GetMapping("dataAuth")
    Result<List<Condition>> getDataAuth(@RequestParam("orgId") Long orgId,
                                        @RequestParam("appId") Long appId, @RequestParam("userId") Long userId);


    @ApiOperation(value = "获取应用权限组", notes = "获取应用权限组-数据权限")
    @GetMapping("appAuth")
    Result<AppAuthorityResp> getAppAuthority(@RequestParam("orgId") Long orgId,
                                             @RequestParam("appId") Long appId,
                                             @RequestParam(value = "tableId", required = false) Long tableId,
                                             @RequestParam("userId") Long userId);

    @ApiOperation(value = "获取应用权限组（使用传入的协作人角色）", notes = "获取应用权限组-数据权限")
    @PostMapping("appAuthByCollaboratorRoles")
    Result<AppAuthorityResp> getAppAuthorityByCollaboratorRoles(@RequestParam("orgId") Long orgId,
                                             @RequestParam("appId") Long appId,
                                             @RequestParam("userId") Long userId, @RequestBody List<Long> collaboratorRoleIds);

    @ApiOperation(value = "获取应用权限组（不包含协作人权限）", notes = "获取应用权限组-数据权限")
    @GetMapping("appAuthWithoutCollaborator")
    Result<AppAuthorityResp> getAppAuthorityWithoutCollaborator(@RequestParam("orgId") Long orgId,
                                             @RequestParam("appId") Long appId,
                                             @RequestParam("userId") Long userId);

    @ApiOperation(value = "获取应用权限组", notes = "获取应用权限组-数据权限")
    @PostMapping("appAuths")
    Result<Map<Long, AppAuthorityResp>> getAppAuthorityBatch(@RequestBody GetAppAuthorityBatchReq req);

    @ApiOperation(value = "获取应用数据权限组", notes = "获取应用数据权限组")
    @GetMapping("collaborators/dataAuth")
    Result<AppAuthorityResp> getDataAuthority(@RequestParam("orgId") Long orgId,
                                             @RequestParam("appId") Long appId,
                                             @RequestParam("userId") Long userId,
                                              @RequestParam("dataId") Long dataId);

    @ApiOperation(value = "批量获取应用数据权限组", notes = "批量获取应用数据权限组")
    @PostMapping("collaborators/dataAuths")
    Result<Map<Long, AppAuthorityResp>> getDataAuthorityBatch(@RequestBody GetDataAuthorityBatchReq getDataAuthorityBatchReq);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation("获取成员与权限组的映射关系")
    @GetMapping("member-group-mappings")
    Result<MemberGroupMappingsResp> memberGroupMappings(@RequestParam("orgId") Long orgId,
                                                               @RequestParam("appId") Long appId,
                                                               @RequestParam("userId") Long userId);

    @ApiOperation("新增应用成员")
    @PostMapping("add-app-members")
    Result<Boolean> addAppMembers(@RequestBody AddAppMembersReq req);

    @ApiOperation("移除应用成员")
    @PostMapping("remove-app-members")
    Result<Boolean> removeAppMembers(@RequestBody RemoveAppMembersReq req);

    @ApiOperation("更新权限更新时间")
    @PostMapping("update-permission-time")
    Result<Boolean> updatePermissionTime(@RequestBody UpdatePermissionTimeReq req);

    @ApiOperation("获取权限更新时间")
    @PostMapping("get-permission-update-time")
    Result<GetPermissionUpdateTimeResp> getPermissionUpdateTime(@RequestBody GetPermissionUpdateTimeReq req);

}
