package com.polaris.lesscode.permission.model.req;

import com.polaris.lesscode.permission.internal.model.bo.PermissionMembersItemBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ApiModel("应用权限组-成员切换应用组")
@NoArgsConstructor
@Data
public class MemberSwitchGroupReq implements Serializable {

    private static final long serialVersionUID = 586530694121702710L;

    @ApiModelProperty("成员")
    private PermissionMembersItemBo member;

}
