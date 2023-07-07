package com.polaris.lesscode.permission.internal.api;

import com.polaris.lesscode.permission.internal.model.req.DeleteAppPackagePermissionReq;
import com.polaris.lesscode.permission.internal.model.req.ModifyAppPackagePermissionReq;
import com.polaris.lesscode.permission.internal.model.resp.SimpleAppPermissionResp;
import com.polaris.lesscode.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 应用包权限（内部调用）
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 15:37
 */
@Api(tags = "应用包权限（内部调用）")
@RequestMapping("/permission/inner/api/v1/app-package-permission")
public interface AppPackagePermissionApi {


    @ApiOperation(value = "创建/修改应用包权限", notes = "创建/修改应用时，同步调用")
    @PostMapping()
    Result<Boolean> saveOrUpdateAppPackagePermission(@Validated @RequestBody ModifyAppPackagePermissionReq req);

    @ApiOperation(value = "删除应用包权限", notes = "删除应用时，同步调用")
    @DeleteMapping
    Result<Boolean> deleteAppPackagePermission(@Validated @RequestBody DeleteAppPackagePermissionReq req);

    @ApiOperation(value = "获取应用包权限 简要信息",notes = "应用包设置查询时使用")
    @GetMapping()
    Result<SimpleAppPermissionResp> getSimpleAppPackagePermission(@RequestParam("orgId") Long orgId, @RequestParam("appPackageId") Long appPackageId);

}
