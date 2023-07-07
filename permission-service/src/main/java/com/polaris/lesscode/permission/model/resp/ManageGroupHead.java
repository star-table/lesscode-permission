package com.polaris.lesscode.permission.model.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 管理组信息
 *
 * @author Nico
 * @date 2020/12/25 15:39
 */
@ApiModel("管理组信息")
@Data
public class ManageGroupHead {

   private List<ManageGroupHeadData> users;

   private List<ManageGroupHeadData> appPackages;

   private List<ManageGroupHeadData> apps;

   private List<ManageGroupHeadData> depts;

   private List<ManageGroupHeadData> roles;
}
