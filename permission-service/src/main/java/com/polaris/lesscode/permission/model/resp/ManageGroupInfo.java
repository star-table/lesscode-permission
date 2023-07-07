package com.polaris.lesscode.permission.model.resp;

import com.polaris.lesscode.permission.internal.model.bo.OptAuthOptionInfoBo;
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
public class ManageGroupInfo {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("组织id")
    private Long orgId;

    @ApiModelProperty("管理组名字")
    private String name;

    @ApiModelProperty("1 系统管理组 2普通管理组")
    private Integer type;

    @ApiModelProperty("成员")
    private List<Long> userIds;

    @ApiModelProperty("应用包权限")
    private List<Long> appPackageIds;

    @ApiModelProperty("应用权限")
    private List<Long> appIds;

    /**
     * TODO 是否要转为{@link OptAuthOptionInfoBo}对象返回
     *
     * @Author Nico
     * @Date 2021/1/6 11:02
     **/
    private List<String> optAuth;

    @ApiModelProperty("部门权限")
    private List<Long> deptIds;

    @ApiModelProperty("角色权限")
    private List<Long> roleIds;

    @ApiModelProperty("创建者")
    private Long creator;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
