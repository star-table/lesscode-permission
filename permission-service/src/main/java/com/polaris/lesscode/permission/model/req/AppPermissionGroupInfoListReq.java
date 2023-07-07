package com.polaris.lesscode.permission.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * req
 *
 * @author zhanghao
 * @date 2022/05/26
 * @since 1.0.0
 */
@ApiModel("批量获取应用权限组详情")
@Data
public class AppPermissionGroupInfoListReq {
    @ApiModelProperty("权限组ID")
    private List<Long> groupIds;
}
