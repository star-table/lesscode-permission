package com.polaris.lesscode.permission.model.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 管理详情信息返回值
 *
 * @author Nico
 * @date 2020/12/25 15:39
 */
@ApiModel("管理详情信息返回值")
@Data
public class ManageGroupDetailResp {

    @ApiModelProperty("管理组信息")
    private ManageGroupInfo adminGroup;

    @ApiModelProperty("附属信息")
    private ManageGroupHead headData;
}
