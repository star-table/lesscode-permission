package com.polaris.lesscode.permission.internal.controller;

import com.polaris.lesscode.permission.internal.api.PermissionApi;
import com.polaris.lesscode.permission.internal.model.UserPermissionVO;
import com.polaris.lesscode.permission.internal.model.resp.UserAppPermissionListResp;
import com.polaris.lesscode.permission.internal.service.AppPermissionInternalService;
import com.polaris.lesscode.permission.internal.service.PkgPermissionInternalService;
import com.polaris.lesscode.permission.service.UserService;
import com.polaris.lesscode.vo.Result;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限（内部调用）
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 15:37
 */
@RestController()
public class PermissionInternalController implements PermissionApi {

    private final UserService userService;
    private final PkgPermissionInternalService pkgPermissionInternalService;
    private final AppPermissionInternalService appPermissionInternalService;

    public PermissionInternalController(UserService userService,
                                        PkgPermissionInternalService pkgPermissionInternalService,
                                        AppPermissionInternalService appPermissionInternalService) {
        this.userService = userService;
        this.pkgPermissionInternalService = pkgPermissionInternalService;
        this.appPermissionInternalService = appPermissionInternalService;
    }

    @Override
    public Result<UserPermissionVO> getUserPermission(Long orgId, Long userId) {
        return Result.ok(userService.getUserPermission(orgId, userId));
    }

    @Override
    public Result<UserAppPermissionListResp> getUserHavePerList(Long orgId, Long userId) {
        return Result.ok(appPermissionInternalService.getUserAppPermissionListResp(orgId, userId));
    }
}
