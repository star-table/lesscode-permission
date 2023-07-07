package com.polaris.lesscode.permission.internal.model.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 应用包权限 列表响应列表项(内部调用)
 *
 * @author roamer
 * @version v1.0
 * @date 2020-10-10 16:26
 */
@ApiModel("应用包权限 列表响应列表项(内部调用)")
@Data
public class AppPkgPerItem implements Serializable {
    private static final long serialVersionUID = 7620837032030418350L;

    @ApiModelProperty("组织ID")
    private Long orgId;

    @ApiModelProperty("父应用包ID")
    private Long parentPkgId;

    @ApiModelProperty("应用包ID")
    private Long appPackageId;

    @ApiModelProperty("管理应用包")
    private Boolean managePkg;

    @ApiModelProperty("可编辑")
    private Boolean editable;

    @ApiModelProperty("可删除")
    private Boolean deletable;

    public AppPkgPerItem() {
        managePkg = false;
        editable = false;
        deletable = false;
    }
}
