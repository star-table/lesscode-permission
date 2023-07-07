package com.polaris.lesscode.permission.internal.model.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.util.Date;

/**
 * @author Nico
 * @date 2020/12/29 15:38
 */
@Data
public class ManageGroupReq {

    /**
     * 主键ID
     **/
    private Long id;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 名称
     */
    private String name;

    /**
     * langCode, 权限组唯一标识
     */
    private String langCode;

    /**
     * 子管理员
     */
    private String userIds;

    /**
     * 管理组内有权限的应用包列表
     */
    private String appPackageIds;

    /**
     * 管理组内有权限的应用列表
     */
    private String appIds;

    /**
     * 功能权限
     */
    private String optAuth;

    /**
     * 组织架构管理范围
     */
    private String deptIds;

    /**
     * 角色管理范围
     */
    private String roleIds;

    /**
     * 创建人
     **/
    private Long creator;

    /**
     * 创建时间
     **/
    private Date createTime;

    /**
     * 修改人
     **/
    private Long updator;

    /**
     * 修改时间
     **/
    private Date updateTime;

    /**
     * 版本号
     **/
    private Long version;

    /**
     * 删除标记 [1: 删除, 2: 未删除]
     **/
    private Integer delFlag;
}
