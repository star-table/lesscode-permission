package com.polaris.lesscode.permission.constant;

import com.polaris.lesscode.consts.CommonConsts;

/**
 * 权限缓存常量
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 11:06
 */
public interface PermissionCacheConstant {

    /** 权限模块 缓存前缀 **/
    String PERMISSION_CACHE_KEY_PREFIX = CommonConsts.LOCK_ROOT_KEY + "permission";

    /** 序列号 生成器 **/
    String FORM_PER_SEQ_CACHE_KEY = PermissionCacheConstant.PERMISSION_CACHE_KEY_PREFIX + ":formPerGroup:uniqueId";

    /** 应用权限组 初始化 锁 **/
    String APP_PER_GROUP_INIT_LOCK_KEY = PermissionCacheConstant.PERMISSION_CACHE_KEY_PREFIX + ":appPerGroup:initLock:${orgId}:${appPackageId}:${appId}";

    /** 修改应用包权限 成员 锁 **/
    String APP_PACKAGE_PER_UPDATE_MEMBER_LOCK_KEY = PermissionCacheConstant.PERMISSION_CACHE_KEY_PREFIX + ":appPackagePermission:updateMemberLock:${orgId}:${appPackageId}";
}
