package com.polaris.lesscode.permission.internal.api;

import com.polaris.lesscode.permission.internal.model.UserPermissionVO;
import com.polaris.lesscode.permission.internal.model.resp.UserAppPermissionListResp;
import com.polaris.lesscode.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-09-18 14:12
 */
@Api(tags = "权限（内部调用）")
@RequestMapping("/permission/inner/api/v1/permissions")
public interface PermissionApi {

    @ApiOperation("获取成员权限")
    @GetMapping()
    Result<UserPermissionVO> getUserPermission(@RequestParam("orgId") Long orgId, @RequestParam("userId") Long userId);


    @ApiOperation("获取用户的应用和应用包权限")
    @GetMapping("/user-have-list")
    Result<UserAppPermissionListResp> getUserHavePerList(@RequestParam("orgId") Long orgId, @RequestParam("userId") Long userId);

}
