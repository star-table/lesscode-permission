package com.polaris.lesscode.permission.model.resp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import rx.functions.ActionN;

/**
 * 简单管理组信息
 *
 * @author Nico
 * @date 2020/12/25 15:39
 */
@ApiModel("简单管理组信息")
@Data
public class SimpleManageGroupInfo {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("组织id")
    private Long orgId;

    @ApiModelProperty("管理组信息")
    private String name;

    @ApiModelProperty("管理组langCode")
    private String langCode;
}
