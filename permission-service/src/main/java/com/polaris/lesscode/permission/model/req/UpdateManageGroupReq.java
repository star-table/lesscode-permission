package com.polaris.lesscode.permission.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 更新管理组请求结构体
 *
 * @author nico
 * @date 2020/12/25 15:30
 */
@ApiModel("更新管理组请求结构体")
@Data
public class UpdateManageGroupReq implements Serializable {

    @ApiModelProperty("管理组ID")
    private Long id;

    @ApiModelProperty("管理组名称")
    @Pattern(regexp = "^[\\pP\\pL\\pS\\pN]{1,20}$", message = "管理组名称格式错误")
    private String name;
}
