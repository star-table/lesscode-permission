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
@TableName("lc_per_manage_permission_config")
@NoArgsConstructor
@Data
public class PerManagePermissionConfig implements Serializable {

    /**
     * 主键ID
     **/
    @TableId
    private Long id;

    /**
     * 组织ID， 0代表模板
     **/
    @TableField("org_id")
    private Long orgId;

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
     * 系统类型：1无码，2北极星
     *
     * @Author Nico
     * @Date 2021/3/11 14:21
     **/
    @TableField("type")
    private Integer type;

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
