package com.polaris.lesscode.permission.internal.controller;

import com.polaris.lesscode.permission.internal.service.impl.ScriptsInternalService;
import com.polaris.lesscode.vo.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "跑批脚本（内部调用）")
@RestController
@RequestMapping("/permission/inner/api/v1/scripts")
public class ScriptsInternalController {

    @Autowired
    private ScriptsInternalService scriptsInternalService;

    /**
     * 默认权限组重置，规则：
     *  - 只针对于默认权限组：编辑人，查看人
     *  - 编辑人，查看人不允许编辑，readonly设为1
     *  - 如果编辑人，查看人权限被用户编辑过，则将之转为自定义权限组并更名
     *  再为之新建默认权限组
     *
     * @return ?
     */
    @GetMapping("/defaultPermissionGroupReset")
    public Result<?> defaultPermissionGroupReset(
            @RequestParam(required = false) Long orgId
    ){
        scriptsInternalService.defaultPermissionGroupReset(orgId);
        return Result.ok();
    }
}
