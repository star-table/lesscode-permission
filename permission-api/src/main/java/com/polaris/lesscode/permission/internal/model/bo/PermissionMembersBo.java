package com.polaris.lesscode.permission.internal.model.bo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限 成员信息 数据模型
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-10 18:24
 */
@ApiModel("权限 成员信息 数据模型")
@Data
public class PermissionMembersBo implements Serializable {

    private static final long serialVersionUID = 7543354365036948801L;

    private List<UserItemBo> users;

    private List<DeptItemBo> depts;

    private List<RoleItemBo> roles;

    public PermissionMembersBo() {
        users = new ArrayList<>();
        depts = new ArrayList<>();
        roles = new ArrayList<>();
    }
}

