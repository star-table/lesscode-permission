package com.polaris.lesscode.permission.internal.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新应用权限组
 *
 * @Author Nico
 * @Date 2021/4/22 18:29
 **/
@ApiModel("应用权限组 更新应用权限组")
@Data
public class UpdateAppPermissionByLangCodeReq {

    @ApiModelProperty(value = "应用id", required = true)
    @NotNull(message = "应用id不能为空")
    private Long appId;

    @ApiModelProperty(value = "应用id", required = true)
    @NotNull(message = "langCode不能为空")
    private Integer langCode;

    /**
     * 权限组名称
     **/
    @ApiModelProperty(value = "权限组名称")
    private String name;

    /**
     * 权限组说明
     **/
    @ApiModelProperty(value = "权限组备注")
    private String remake;

    @ApiModelProperty(value = "权限组操作权限, json")
    private String optAuth;

    @ApiModelProperty(value = "表格权限, json")
    private String tableAuth;

    @ApiModelProperty(value = "字段权限, json")
    private String fieldAuth;

    @ApiModelProperty(value = "数据权限, json")
    private String dataAuth;

    @ApiModelProperty(value = "视图权限, json")
    private String viewAuth;
}
