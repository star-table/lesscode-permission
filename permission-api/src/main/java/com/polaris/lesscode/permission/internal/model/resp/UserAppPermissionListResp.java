package com.polaris.lesscode.permission.internal.model.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户应用权限 列表响应(内部调用)
 *
 * @author roamer
 * @version v1.0
 * @date 2020-10-10 16:26
 */
@ApiModel("用户应用权限 列表响应(内部调用)")
@Data
public class UserAppPermissionListResp implements Serializable {
    private static final long serialVersionUID = 1079105261925245239L;

    @ApiModelProperty("可创建应用包")
    private Boolean creatable;

    @ApiModelProperty("应用包权限列表")
    private List<AppPkgPerItem> appPkgList;

    @ApiModelProperty("应用权限列表")
    private List<AppPerItem> appList;

    public UserAppPermissionListResp() {
        creatable = false;
        appPkgList = new ArrayList<>();
        appList = new ArrayList<>();
    }

    public void addPkg(AppPkgPerItem item) {
        appPkgList.add(item);
    }

    public void addPkg(Collection<AppPkgPerItem> items) {
        appPkgList.addAll(items);
    }

    public void addApp(AppPerItem item) {
        appList.add(item);
    }

    public void addApp(Collection<AppPerItem> items) {
        appList.addAll(items);
    }
}
