package com.polaris.lesscode.permission.internal.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限成员-角色信息 数据模型
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-10 18:35
 */
@ApiModel("权限成员-角色信息 数据模型")
@Data
public class RoleItemBo implements Serializable {


    private static final long serialVersionUID = 5220074020337895239L;
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;
}
