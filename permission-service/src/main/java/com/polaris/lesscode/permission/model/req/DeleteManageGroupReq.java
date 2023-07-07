package com.polaris.lesscode.permission.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 删除管理组请求结构体
 *
 * @author nico
 * @date 2020/12/25 15:30
 */
@ApiModel("删除管理组请求结构体")
@Data
public class DeleteManageGroupReq implements Serializable {

    @ApiModelProperty("管理组ID")
    private Long id;
}
