package com.polaris.lesscode.permission.internal.model.bo;

import com.polaris.lesscode.permission.internal.enums.PermissionMemberCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 权限成员 数据模型
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-02 19:33
 */
@ApiModel("权限 成员数据 模型")
@NoArgsConstructor
@Data
public class PermissionMembersItemBo implements Serializable {

    private static final long serialVersionUID = -6106646327125606963L;

    /**
     * 成员类型
     *
     * @see PermissionMemberCode
     */
    @ApiModelProperty(value = "成员类型", allowableValues = "U, D, R", required = true)
    @NotNull(message = "成员类型为空")
    private String k;

    /**
     * 成员ID
     */
    @ApiModelProperty(value = "成员ID", required = true)
    @NotNull(message = "成员ID为空")
    private Long v;
}
