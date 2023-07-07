package com.polaris.lesscode.permission.constant;

import com.polaris.lesscode.consts.CommonConsts;

public interface CacheConsts {

    String APP_AUTH_CACHE_KEY = CommonConsts.LOCK_ROOT_KEY + "appauth:${appId}:${userId}:${isContainCollaborator}";
    String ORG_AUTH_CACHE_KEY = CommonConsts.LOCK_ROOT_KEY + "orgauth:${orgId}:${userId}";
    String APP_PERMISSION_UPDATE_TIME = CommonConsts.LOCK_ROOT_KEY + "permissionUpdateTime:";
}
