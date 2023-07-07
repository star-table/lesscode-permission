/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.polaris.lesscode.permission.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * req
 *
 * @author ethanliao
 * @date 2020/12/10
 * @since 1.0.0
 */
@ApiModel("通过名称获取应用权限组列表")
@Data
public class AppPerGroupListItemByNameReq {
    @ApiModelProperty("名称")
    private String name;
}
