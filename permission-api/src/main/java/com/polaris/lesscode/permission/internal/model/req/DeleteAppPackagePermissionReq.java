package com.polaris.lesscode.permission.internal.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 应用包权限 删除参数（内部调用）
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-02 18:24
 */
@ApiModel("应用包权限 删除参数（内部调用）")
@Data
public class DeleteAppPackagePermissionReq {
    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID", required = true)
    @NotNull(message = "组织ID为空")
    private Long orgId;

    /**
     * 应用包ID列表
     */
    @ApiModelProperty(value = "应用包ID列表", required = true)
    @NotNull(message = "应用包ID列表为空")
    private Collection<Long> appPackageIds;

    /**
     * 应用ID列表
     */
    @ApiModelProperty(value = "应用ID列表", required = true)
    @NotNull(message = "应用ID列表为空")
    private Collection<Long> appIds;

    /**
     * 操作用户ID
     */
    @ApiModelProperty(value = "操作用户ID", required = true)
    @NotNull(message = "操作用户为空")
    private Long userId;

    public DeleteAppPackagePermissionReq() {
        appPackageIds = new ArrayList<>();
        appIds = new ArrayList<>();
    }

    public void appendPkgId(Long pkgId) {
        if (appPackageIds == null) {
            appPackageIds = new ArrayList<>();
        }
        appPackageIds.add(pkgId);
    }

    public void appendAppId(Long appId) {
        if (appIds == null) {
            appIds = new ArrayList<>();
        }
        appIds.add(appId);
    }
}
