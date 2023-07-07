package com.polaris.lesscode.permission.internal.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 应用权限 删除参数（内部调用）
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-02 18:24
 */
@ApiModel("应用权限 删除参数（内部调用）")
@NoArgsConstructor
@Data
public class DeleteAppPermissionReq {
    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID", required = true)
    @NotNull(message = "组织ID为空")
    private Long orgId;

    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID", required = true)
    @NotNull(message = "应用ID为空")
    private Long appId;

    /**
     * 操作用户ID
     */
    @ApiModelProperty(value = "操作用户ID", required = true)
    @NotNull(message = "操作用户为空")
    private Long userId;
}
