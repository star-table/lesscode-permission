package com.polaris.lesscode.permission.model.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 管理组列表附属信息
 *
 * @author Nico
 * @date 2020/12/25 15:39
 */
@ApiModel("管理组列表附属信息")
@Data
public class ManageGroupHeadData {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("管理组名字")
    private String name;
}
