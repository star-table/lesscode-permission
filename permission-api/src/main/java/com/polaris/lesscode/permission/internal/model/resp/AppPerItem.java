package com.polaris.lesscode.permission.internal.model.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 应用权限 列表项响应(内部调用)
 *
 * @author roamer
 * @version v1.0
 * @date 2020-10-10 16:26
 */
@ApiModel("应用权限 列表项响应(内部调用)")
@Data
public class AppPerItem implements Serializable {

    private static final long serialVersionUID = -8198991246949605303L;

    @ApiModelProperty("组织ID")
    private Long orgId;

    @ApiModelProperty("应用包ID")
    private Long appPackageId;

    @ApiModelProperty("应用ID")
    private Long appId;

    @ApiModelProperty("可编辑")
    private Boolean editable;

    @ApiModelProperty("可删除")
    private Boolean deletable;

    @ApiModelProperty("可见")
    private Boolean show;

    public AppPerItem() {
        editable = false;
        deletable = false;
        show = false;
    }
}
