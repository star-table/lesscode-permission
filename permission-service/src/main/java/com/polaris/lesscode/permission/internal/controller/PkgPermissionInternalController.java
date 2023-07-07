package com.polaris.lesscode.permission.internal.controller;

import com.polaris.lesscode.permission.internal.api.AppPackagePermissionApi;
import com.polaris.lesscode.permission.internal.model.req.DeleteAppPackagePermissionReq;
import com.polaris.lesscode.permission.internal.model.req.ModifyAppPackagePermissionReq;
import com.polaris.lesscode.permission.internal.model.resp.SimpleAppPermissionResp;
import com.polaris.lesscode.permission.internal.service.PkgPermissionInternalService;
import com.polaris.lesscode.vo.Result;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用包权限（内部调用）
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 15:37
 */
@RestController()
public class PkgPermissionInternalController implements AppPackagePermissionApi {

    private final PkgPermissionInternalService appPackagePermissionService;

    public PkgPermissionInternalController(PkgPermissionInternalService appPackagePermissionService) {
        this.appPackagePermissionService = appPackagePermissionService;
    }

    @Override
    public Result<Boolean> saveOrUpdateAppPackagePermission(ModifyAppPackagePermissionReq req) {
        return Result.ok(appPackagePermissionService.modifyPackagePermission(req));
    }

    @Override
    public Result<Boolean> deleteAppPackagePermission(DeleteAppPackagePermissionReq req) {
        return Result.ok(appPackagePermissionService.deletePackagePermission(req.getOrgId(), req.getAppPackageIds(), req.getAppIds(), req.getUserId()));
    }

    @Override
    public Result<SimpleAppPermissionResp> getSimpleAppPackagePermission(Long orgId,
                                                                         Long appPackageId) {
        return Result.ok(appPackagePermissionService.getPackagePermission(orgId, appPackageId));
    }


}
