package com.polaris.lesscode.permission.model.req;

import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.permission.internal.model.bo.ViewAuth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用权限组 保存请求参数
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 11:35
 */
@ApiModel("应用权限组 保存请求参数")
@Data
public class SaveAppPermissionGroupReq implements Serializable {

    private static final long serialVersionUID = -5043501038278840116L;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String remake;

    /**
     * 操作权限
     *
     * @see com.polaris.lesscode.permission.internal.enums.OperateAuthCode
     */
    @ApiModelProperty("操作权限")
    private List<String> optAuth;

    /**
     * 操作权限
     *
     * @see com.polaris.lesscode.permission.internal.enums.OperateAuthCode
     */
    @ApiModelProperty("表格权限")
    private List<String> tableAuth;

    /**
     * 字段权限
     *
     * @see com.polaris.lesscode.permission.internal.enums.FormFieldAuthCode
     */
    @ApiModelProperty("字段权限")
    private Map<String, Map<String, Integer>> fieldAuth;

    @ApiModelProperty("数据权限")
    private Condition dataAuth;

    @ApiModelProperty("视图权限")
    private Map<String, ViewAuth> viewAuth;

    public SaveAppPermissionGroupReq() {
        optAuth = new ArrayList<>();
        tableAuth = new ArrayList<>();
        fieldAuth = new HashMap<>();
        dataAuth = new Condition();
    }
}
