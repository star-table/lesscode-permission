package com.polaris.lesscode.permission.internal.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 视图权限
 *
 * @author Nico
 * @date 2021/1/26 13:42
 */
@Data
public class ViewAuth {

    @ApiModelProperty("是否有更新权限")
    private boolean hasUpdate;

    @ApiModelProperty("是否有查询权限")
    private boolean hasSelect;

    @ApiModelProperty("是否有删除权限")
    private boolean hasDelete;
}
