package com.polaris.lesscode.permission.internal.model.resp;

import com.polaris.lesscode.permission.internal.model.bo.PermissionMembersBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 应用权限组 列表查询详情模型
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 11:35
 */
@ApiModel("应用权限组 列表查询详情模型")
@Data
public class AppPerGroupListItem implements Serializable {

    private static final long serialVersionUID = 6360447467409064190L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "langCode")
    private String langCode;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String remake;

    @ApiModelProperty(value = "只读", notes = "1 为只读 2非只读")
    private Integer readOnly;

    @ApiModelProperty(value = "编辑权限组权限", notes = "编辑权限组权限,只读时，则忽视该权限")
    private Boolean hasEdit;

    @ApiModelProperty(value = "删除权限组权限", notes = "删除权限组权限,只读时，则忽视该权限")
    private Boolean hasDelete;

    @ApiModelProperty("成员列表")
    private PermissionMembersBo members;

    public AppPerGroupListItem() {
        readOnly = 2;
        hasEdit = false;
        hasDelete = false;
    }

}
