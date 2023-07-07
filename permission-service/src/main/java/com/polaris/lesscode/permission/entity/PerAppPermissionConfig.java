package com.polaris.lesscode.permission.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用权限配置 entity
 *
 * @author roamer
 * @version v1.0
 * @date 2020-11-01 15:49
 */
@TableName("lc_per_app_permission_config")
@NoArgsConstructor
@Data
public class PerAppPermissionConfig implements Serializable {

    private static final long serialVersionUID = 910959386751737075L;
    /**
     * 主键ID
     **/
    @TableId
    private Long id;

    /**
     * 组织ID
     **/
    @TableField("org_id")
    private Long orgId;

    /**
     * 应用ID
     **/
    @TableField("app_id")
    private Long appId;

    /**
     * 应用类型，1：form，2：dashboard，3：文件夹，4：project
     *
     * @Author Nico
     * @Date 2021/1/6 11:41
     **/
    @TableField("app_type")
    private Integer appType;

    /**
     * 是否可新建
     **/
    @TableField("creatable")
    private Boolean creatable;

    /**
     * 组件类型
     **/
    @TableField("component_type")
    private String componentType;

    /**
     * 操作权限选项 array json
     * <p>
     * 格式：
     * [
     * {
     * "code" : "hasRead"
     * "name" : "查看"
     * },
     * {
     * "code" : "hasUpdate"
     * "name" : "编辑"
     * }
     * ]
     * </p>
     * 基础选项参照 {@link com.polaris.lesscode.permission.internal.enums.OperateAuthCode}
     **/
    @TableField("opt_auth_options")
    private String optAuthOptions;

    /**
     * 字段权限选项 array json
     * <p>
     * 格式：
     * [
     * {
     * "code" : 1
     * "name" : "可见"
     * },
     * {
     * "code" : 2
     * "name" : "可编辑"
     * }
     * {
     * "code" : 4
     * "name" : "是否脱敏"
     * }
     * ]
     * </p>
     * 字段权限参照 {@link com.polaris.lesscode.permission.internal.enums.FormFieldAuthCode}
     **/
    @TableField("field_auth_options")
    private String fieldAuthOptions;

    /**
     * 数据域权限选项
     */
    @TableField("data_auth_options")
    private String dataAuthOptions;

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
    @TableField("del_flag")
    private Integer delFlag;
}
