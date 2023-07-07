package com.polaris.lesscode.permission.internal.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 应用字段权限 初始化（创建表格时调用）
 *
 * @author
 * @version v1.0
 * @date 2022-04-18 18:24
 */
@ApiModel("应用权限组 初始化参数（内部调用）")
@Data
public class InitAppPermissionFieldAuthCreateTableReq {
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
     * 操作用户ID
     */
    @ApiModelProperty(value = "操作用户ID", required = true)
    @NotNull(message = "操作用户为空")
    private Long userId;

    /**
     * 默认权限组类型（为空则不初始化），1 初始化表单的权限组，2 初始化仪表盘的权限组，3 初始化北极星项目的权限组
     * （初始化管理组的逻辑暂时放在permission服务中来做，因为北极星这种属于公司内部服务）
     * @author nico
     **/
    @ApiModelProperty(value = "默认权限组类型")
    private Integer defaultPermissionGroupType;
}
