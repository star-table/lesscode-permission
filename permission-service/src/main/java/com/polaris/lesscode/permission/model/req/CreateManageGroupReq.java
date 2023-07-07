package com.polaris.lesscode.permission.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 创建管理组请求结构体
 *
 * @author nico
 * @date 2020/12/25 15:30
 */
@ApiModel("创建管理组请求结构体")
@Data
public class CreateManageGroupReq implements Serializable {

    @ApiModelProperty("管理组名称")
    @Pattern(regexp = "^[\\pP\\pL\\pS\\pN]{1,20}$", message = "管理组名称格式错误")
    private String name;

    @ApiModelProperty(" 类型 1.系统管理组 2.普通管理组")
    @Range(min = 1, max = 2, message = "类型错误, 1.系统管理组 2.普通管理组")
    private Integer groupType;
}
