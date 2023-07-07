package com.polaris.lesscode.permission.model.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * CRM 根据角色权限列表查询参数 REQ
 *
 * @author roamer
 * @version v1.0
 * @date 2020-12-09 15:28
 */
@ApiModel("CRM 根据角色权限列表查询参数 REQ")
@Data
public class CrmRolePermissionListReq implements Serializable {

    @NotNull(message = "角色ID不可为空")
    private Long roleId;

    private Long page = 1L;

    private Long size = 10L;
}
