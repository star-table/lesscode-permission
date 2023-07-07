package com.polaris.lesscode.permission.internal.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限成员-部门信息 响应模型
 * @author roamer
 * @version v1.0
 * @date 2020-09-10 18:35
 */
@ApiModel("权限成员-部门信息 响应模型")
@Data
public class DeptItemBo implements Serializable {
    private static final long serialVersionUID = 8205685181898168488L;
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("部门名称")
    private String name;
}
