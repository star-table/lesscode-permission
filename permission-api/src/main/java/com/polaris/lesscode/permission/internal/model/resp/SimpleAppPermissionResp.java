package com.polaris.lesscode.permission.internal.model.resp;

import com.polaris.lesscode.permission.internal.enums.AppVisibilityScope;
import com.polaris.lesscode.permission.internal.model.bo.PermissionMembersBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 应用包权限 响应数据模型
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-02 18:24
 */
@ApiModel("应用包权限 响应数据模型（内部调用）")
@Data
public class SimpleAppPermissionResp {

    /**
     * 可见性
     *
     * @see AppVisibilityScope
     */
    @ApiModelProperty(value = "可见性", notes = "可见性 [0:全部, 1:指定范围成员]", allowableValues = "0, 1")
    private Integer scope;

    /**
     * 成员列表
     */
    @ApiModelProperty(value = "成员列表", notes = "成员列表")
    private PermissionMembersBo members;
}
