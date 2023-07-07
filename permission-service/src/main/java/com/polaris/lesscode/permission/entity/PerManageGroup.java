package com.polaris.lesscode.permission.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理组权限配置
 *
 * @author Nico
 */
@TableName("lc_per_manage_group")
@NoArgsConstructor
@Data
public class PerManageGroup implements Serializable {

    /**
     * 主键ID
     **/
    @TableId
    @TableField("id")
    private Long id;

    /**
     * 组织ID
     */
    @TableField("org_id")
    private Long orgId;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * langCode, 权限组唯一标识
     */
    @TableField("lang_code")
    private String langCode;

    /**
     * 子管理员
     */
    @TableField("user_ids")
    private String userIds;

    /**
     * 管理组内有权限的应用包列表
     */
    @TableField("app_package_ids")
    private String appPackageIds;

    /**
     * 管理组内有权限的应用列表
     */
    @TableField("app_ids")
    private String appIds;

    /**
     * 功能权限
     */
    @TableField("opt_auth")
    private String optAuth;

    /**
     * 组织架构管理范围
     */
    @TableField("dept_ids")
    private String deptIds;

    /**
     * 角色管理范围
     */
    @TableField("role_ids")
    private String roleIds;

    /**
     * 创建人
     **/
    @TableField("creator")
    private Long creator;

    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改人
     **/
    @TableField("updator")
    private Long updator;

    /**
     * 修改时间
     **/
    @TableField("update_time")
    private Date updateTime;

    /**
     * 版本号
     **/
    @Version
    @TableField("version")
    private Long version;

    /**
     * 删除标记 [1: 删除, 2: 未删除]
     **/
    @TableLogic(value = "2", delval = "1")
    @TableField("is_delete")
    private Integer isDelete;
}
