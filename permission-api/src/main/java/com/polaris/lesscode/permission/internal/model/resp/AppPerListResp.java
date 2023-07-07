package com.polaris.lesscode.permission.internal.model.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用权限 列表响应(内部调用)
 *
 * @author roamer
 * @version v1.0
 * @date 2020-10-10 16:26
 */
@ApiModel("应用权限 列表响应(内部调用)")
@Data
public class AppPerListResp implements Serializable {
    private static final long serialVersionUID = 1079105261925245239L;

    @ApiModelProperty("管理应用包")
    private Boolean hasManageAppPackage;

    @ApiModelProperty("应用列表")
    private List<Long> appList;

    public AppPerListResp() {
        hasManageAppPackage = false;
        appList = new ArrayList<>();
    }
}
