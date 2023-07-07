package com.polaris.lesscode.permission.internal.controller;

import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.permission.internal.api.AppPermissionApi;
import com.polaris.lesscode.permission.internal.model.bo.ViewAuth;
import com.polaris.lesscode.permission.internal.model.req.*;
import com.polaris.lesscode.permission.internal.model.resp.*;
import com.polaris.lesscode.permission.internal.service.AppPermissionInternalService;
import com.polaris.lesscode.vo.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 应用权限组（内部调用）
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 15:37
 */
@RestController()
public class AppPermissionInternalController implements AppPermissionApi {

    private final AppPermissionInternalService appPermissionInternalService;

    public AppPermissionInternalController(AppPermissionInternalService appPermissionInternalService) {
        this.appPermissionInternalService = appPermissionInternalService;
    }

    @Override
    public Result<List<AppPerGroupListItem>> getAppPermissionGroupList(Long orgId, Long appId) {
        return Result.ok(appPermissionInternalService.getAppPermissionGroupList(orgId, appId));
    }

    @Override
    public Result<List<AppPermissionGroupResp>> getAppPermissionGroupBatch(List<Long> appIds) {
        return Result.ok(appPermissionInternalService.getAppPermissionGroupBatch(appIds));
    }

    @Override
    public Result<List<AppPermissionGroupResp>> createAppPermissionGroup(List<CreateAppPermissionGroupReq> req) {
        return Result.ok(appPermissionInternalService.createAppPermissionGroupBatch(req));
    }

    @Override
    public Result<Boolean> initAppPermission(InitAppPermissionReq req) {
        return Result.ok(appPermissionInternalService.initAppPermission(req));
    }

    @Override
    public Result<Boolean> initAppPermissionFieldAuthCreateTable(InitAppPermissionFieldAuthCreateTableReq req) {
        return Result.ok(appPermissionInternalService.initAppPermissionFieldAuthCreateTable(req));
    }

    @Override
    public Result<Boolean> initAppPermissionFieldAuthDeleteTable(InitAppPermissionFieldAuthDeleteTableReq req) {
        return Result.ok(appPermissionInternalService.initAppPermissionFieldAuthDeleteTable(req));
    }

//    @Override
//    public Result<Boolean> updateFormFieldConfig(UpdateFieldConfigReq req) {
//        return Result.ok(appPermissionInternalService.updateFormFieldConfig(req));
//    }

    @Override
    public Result<Boolean> updateAppPermission(UpdateAppPermissionReq req) {
        return Result.ok(appPermissionInternalService.updateAppPermission(req));
    }

    @Override
    public Result<Boolean> updateAppPermissionByLangCode(UpdateAppPermissionByLangCodeReq req) {
        return Result.ok(appPermissionInternalService.updateAppPermissionByLangCode(req));
    }

    @Override
    public Result<Boolean> deleteAppPermission(DeleteAppPermissionReq req) {
        return Result.ok(appPermissionInternalService.deleteAppPermission(req.getOrgId(), req.getAppId(),
                req.getUserId()));
    }

    @Override
    public Result<Map<String, Map<String, Integer>>> getFieldAuth(Long orgId, Long appId, Long userId) {
        return Result.ok(appPermissionInternalService.getPermissionGroupFieldAuth(orgId, appId, userId));
    }

    @Override
    public Result<List<String>> getTableAuth(Long orgId, Long appId, Long userId) {
        return Result.ok(appPermissionInternalService.getPermissionGroupTableAuth(orgId, appId, userId));
    }

    @Override
    public Result<Map<String, ViewAuth>> getViewAuth(Long orgId, Long appId, Long userId) {
        return Result.ok(appPermissionInternalService.getPermissionGroupViewAuth(orgId, appId, userId));
    }

    @Override
    public Result<FromPerOptAuthVO> getOptAuth(Long orgId, Long appId, Long userId) {
        return Result.ok(appPermissionInternalService.getPermissionGroupOptAuth(orgId, appId, userId));
    }

    @Override
    public Result<List<String>> getOptAuthList(Long orgId, Long appId, Long userId) {
        return Result.ok(appPermissionInternalService.getPermissionGroupOptAuthList(orgId, appId, userId));
    }

    @Override
    public Result<List<Condition>> getDataAuth(Long orgId, Long appId, Long userId) {
        return Result.ok(appPermissionInternalService.getPermissionGroupDataAuth(orgId, appId, userId));
    }

    @Override
    public Result<AppAuthorityResp> getAppAuthority(Long orgId, Long appId, Long tableId, Long userId) {
        return Result.ok(appPermissionInternalService.getAppAuthority(orgId, appId, tableId, userId));
    }

    @Override
    public Result<AppAuthorityResp> getAppAuthorityByCollaboratorRoles(Long orgId, Long appId, Long userId, List<Long> collaboratorRoleIds) {
        return Result.ok(appPermissionInternalService.getAppAuthorityByCollaboratorRoles(orgId, appId, userId, collaboratorRoleIds));
    }

    @Override
    public Result<AppAuthorityResp> getAppAuthorityWithoutCollaborator(Long orgId, Long appId, Long userId) {
        return Result.ok(appPermissionInternalService.getAppAuthorityWithoutCollaborator(orgId, appId, userId));
    }

    @Override
    public Result<Map<Long, AppAuthorityResp>> getAppAuthorityBatch(GetAppAuthorityBatchReq req) {
        return Result.ok(appPermissionInternalService.getAppAuthorityBatch(req));
    }

    @Override
    public Result<AppAuthorityResp> getDataAuthority(Long orgId, Long appId, Long userId, Long dataId) {
        return Result.ok(appPermissionInternalService.getDataAuthority(orgId, appId, userId, dataId));
    }

    @Override
    public Result<Map<Long, AppAuthorityResp>> getDataAuthorityBatch(GetDataAuthorityBatchReq getDataAuthorityBatchReq) {
        return Result.ok(appPermissionInternalService.getDataAuthorityBatch(getDataAuthorityBatchReq));
    }

    @Override
    public Result<MemberGroupMappingsResp> memberGroupMappings(Long orgId, Long appId, Long userId) {
        return Result.ok(appPermissionInternalService.memberGroupMappings(orgId, userId, appId));
    }

    @Override
    public Result<Boolean> removeAppMembers(RemoveAppMembersReq req) {
        return Result.ok(appPermissionInternalService.removeAppMembers(req));
    }

    @Override
    public Result<Boolean> addAppMembers(AddAppMembersReq req) {
        return Result.ok(appPermissionInternalService.addAppMembers(req));
    }

    @Override
    public Result<Boolean> copyPermissionGroup(CopyAppPermissionGroupReq req) {
        appPermissionInternalService.copyPermissionGroup(req);
        return Result.ok(true);
    }

    @Override
    public Result<Boolean> updatePermissionTime(@RequestBody UpdatePermissionTimeReq req) {
        return Result.ok(appPermissionInternalService.updatePermissionTime(req));
    }

    @Override
    public Result<GetPermissionUpdateTimeResp> getPermissionUpdateTime(@RequestBody GetPermissionUpdateTimeReq req) {
        return Result.ok(appPermissionInternalService.getPermissionUpdateTime(req));
    }
}
