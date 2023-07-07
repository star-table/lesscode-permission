package com.polaris.lesscode.permission.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用权限组与成员绑定关系 entity
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 15:49
 */
@TableName("lc_per_app_permission_group_member")
@NoArgsConstructor
@Data
public class PerAppPermissionMember implements Serializable {


    private static final long serialVersionUID = -1719783531066254848L;
    /** 主键ID **/
    @TableId
    private Long id;

    /** 组织ID **/
    @TableField("org_id")
    private Long orgId;

    /** 应用ID **/
    @TableField("app_id")
    private Long appId;


    /** 应用权限组ID **/
    @TableField("permission_group_id")
    private Long permissionGroupId;

    /** 用户ID  array json **/
    @TableField("user_ids")
    private String userIds;

    /** 部门ID array json **/
    @TableField("dept_ids")
    private String deptIds;

    /** 角色ID array json **/
    @TableField("role_ids")
    private String roleIds;

    /** 创建人 **/
    @TableField("creator")
    private Long creator;

    /** 创建时间 **/
    @TableField("create_time")
    private Date createTime;

    /** 修改人 **/
    @TableField("updator")
    private Long updator;

    /** 修改时间 **/
    @TableField("update_time")
    private Date updateTime;

    /** 版本号 **/
    @Version
    @TableField("version")
    private Long version;

    /** 删除标记 [1: 删除, 2: 未删除] **/
    @TableLogic(value = "2", delval = "1")
    @TableField("del_flag")
    private Integer delFlag;
}
