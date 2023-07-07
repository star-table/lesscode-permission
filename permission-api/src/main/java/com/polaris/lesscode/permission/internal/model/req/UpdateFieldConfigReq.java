package com.polaris.lesscode.permission.internal.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 表单字段配置 修改参数（内部调用）
 *
 * @author roamer
 * @version v1.0
 * @date 2020-11-10 18:24
 */
@ApiModel("表单字段配置 修改参数（内部调用）")
@Data
public class UpdateFieldConfigReq {
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
     * 表格ID
     */
    @ApiModelProperty(value = "表格ID", required = true)
    @NotNull(message = "表格ID为空")
    private Long tableId;

    /**
     * 字段配置
     */
    @ApiModelProperty(value = "字段配置")
    @NotNull(message = "字段配置为空")
    private String config;

    public UpdateFieldConfigReq() {
    }
}
