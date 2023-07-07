package com.polaris.lesscode.permission.internal.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限成员-用户信息 数据模型
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-10 18:34
 */
@ApiModel("权限成员-用户信息 数据模型")
@Data
public class UserItemBo implements Serializable {

    private static final long serialVersionUID = 795870311167326011L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("昵称")
    private String name;

}
