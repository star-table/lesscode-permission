package com.polaris.lesscode.permission.internal.model.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用权限组 列表查询响应模型
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 11:35
 */
@ApiModel("应用权限组 列表查询响应模型")
@Data
public class AppPerGroupListResp implements Serializable {

    private static final long serialVersionUID = 1736698070293677042L;

    @ApiModelProperty("权限组列表")
    private List<AppPerGroupListItem> list;

    @ApiModelProperty(value = "新建权限组权限", notes = "新建权限组权限，有此权限则显示新建权限组按钮")
    private Boolean hasCreate;

    public AppPerGroupListResp() {
        list = new ArrayList<>();
        hasCreate = Boolean.FALSE;
    }

    public void addPerGroup(AppPerGroupListItem appPerGroupListItem) {
        list.add(appPerGroupListItem);
    }
}
