package com.polaris.lesscode.permission.internal.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-09-21 18:27
 */
@ApiModel("管理项信息 响应模型(由用户服务返回)")
@Data
public class ObjOptAuthItem {
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("是否具有管理权限")
    private Boolean hasManage;

    public ObjOptAuthItem() {
        hasManage = false;
    }
}
