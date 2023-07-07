package com.polaris.lesscode.permission.model.req;

import com.polaris.lesscode.permission.internal.model.bo.PermissionMembersItemBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用权限组-成员修改请求参数
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 14:40
 */
@ApiModel("应用权限组-成员修改请求参数")
@NoArgsConstructor
@Data
public class UpdateAppPerMembersReq implements Serializable {

    private static final long serialVersionUID = 586530694121702710L;

    @ApiModelProperty("成员列表")
    private List<PermissionMembersItemBo> members = new ArrayList<>();

    @ApiModelProperty(value = "操作", notes = "不传递为全量更新，[1:新增, 2:删除]")
    private Integer mode;
}
