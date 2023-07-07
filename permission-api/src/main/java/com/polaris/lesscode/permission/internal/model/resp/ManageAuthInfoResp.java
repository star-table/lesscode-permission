package com.polaris.lesscode.permission.internal.model.resp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * 管理认证信息
 *
 * @Author Nico
 * @Date 2020/12/30 20:21
 **/
@Data
public class ManageAuthInfoResp {
    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 是否是管理员
     **/
    private boolean isAdmin;

    /**
     * 管理组内有权限的应用包列表
     */
    private Set<Long> appPackageIds;

    /**
     * 管理组内有权限的应用列表
     */
    private Set<Long> appIds;

    /**
     * 功能权限
     */
    private Set<String> optAuth;

    /**
     * 组织架构管理范围
     */
    private Set<Long> deptIds;

    /**
     * 角色管理范围
     */
    private Set<Long> roleIds;

}
