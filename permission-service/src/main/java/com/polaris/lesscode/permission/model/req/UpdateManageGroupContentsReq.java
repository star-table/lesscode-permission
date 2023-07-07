package com.polaris.lesscode.permission.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新管理组内容请求结构体
 *
 * @author nico
 * @date 2020/12/25 15:30
 */
@ApiModel("更新管理组内容请求结构体")
@Data
public class UpdateManageGroupContentsReq implements Serializable {

    @ApiModelProperty("管理组ID")
    private Long id;

    @ApiModelProperty("values列表")
    private List<Long> values;

    @ApiModelProperty("key")
    private String key;
}
