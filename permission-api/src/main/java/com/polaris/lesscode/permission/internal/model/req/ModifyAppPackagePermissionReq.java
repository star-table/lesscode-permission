package com.polaris.lesscode.permission.internal.model.req;

import com.polaris.lesscode.permission.internal.enums.AppVisibilityScope;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用包权限 修改参数（内部调用）
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-02 18:24
 */
@ApiModel("应用包权限 修改参数（内部调用）")
@Data
public class ModifyAppPackagePermissionReq {
    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID", required = true)
    @NotNull(message = "组织ID为空")
    private Long orgId;

    /**
     * 父应用包ID
     */
    @ApiModelProperty(value = "父应用包ID")
    private Long parentPkgId;

    /**
     * 应用包ID
     */
    @ApiModelProperty(value = "应用包ID", required = true)
    @NotNull(message = "应用包ID为空")
    private Long appPackageId;

    /**
     * 操作用户ID
     */
    @ApiModelProperty(value = "操作用户ID", required = true)
    @NotNull(message = "操作用户为空")
    private Long userId;

    /**
     * 可见性
     *
     * @see AppVisibilityScope
     */
    @ApiModelProperty(value = "可见性", notes = "可见性 [0:全部, 1:指定范围成员]", allowableValues = "0, 1", required = true)
    @NotNull(message = "可见性为空")
    private Integer scope;

    /**
     * 成员列表
     */
    @ApiModelProperty(value = "成员列表", notes = "成员列表")
    private List<PermissionMembersItemReq> members;

    public ModifyAppPackagePermissionReq() {
        this.members = new ArrayList<>();
    }
}
