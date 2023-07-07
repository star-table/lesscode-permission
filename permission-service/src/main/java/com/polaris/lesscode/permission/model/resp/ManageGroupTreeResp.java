package com.polaris.lesscode.permission.model.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 管理组列表信息
 *
 * @author Nico
 * @date 2020/12/25 15:39
 */
@ApiModel("管理组列表信息")
@Data
public class ManageGroupTreeResp {

   @ApiModelProperty("系统管理组")
   private SimpleManageGroupInfo sysGroup;

    @ApiModelProperty("普通管理组列表")
   private List<SimpleManageGroupInfo> generalGroups;
}
