package com.polaris.lesscode.permission.model.resp;

import com.polaris.lesscode.permission.internal.model.bo.FieldAuthOptionInfoBo;
import com.polaris.lesscode.permission.internal.model.bo.OptAuthOptionInfoBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用权限基础配置 响应模型
 *
 * @author roamer
 * @version v1.0
 * @date 2020-11-03 11:35
 */
@ApiModel("应用权限基础配置 响应模型")
@Data
public class AppPerBaseConfigResp implements Serializable {

    private static final long serialVersionUID = -3350928680901247171L;

    @ApiModelProperty(value = "组织ID")
    private Long orgId;

    @ApiModelProperty(value = "应用ID")
    private Long appId;

    @ApiModelProperty(value = "是否可新建权限组")
    private Boolean creatable;

    @ApiModelProperty(value = "操作权限选项")
    private List<OptAuthOptionInfoBo> optAuthOptions;

    @ApiModelProperty(value = "字段权限选项")
    private List<FieldAuthOptionInfoBo> fieldAuthOptions;

    public AppPerBaseConfigResp() {
        creatable = false;
        optAuthOptions = new ArrayList<>();
        fieldAuthOptions = new ArrayList<>();
    }

}
