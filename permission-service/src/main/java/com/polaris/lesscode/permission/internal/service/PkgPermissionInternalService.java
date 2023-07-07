package com.polaris.lesscode.permission.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.polaris.lesscode.exception.BusinessException;
import com.polaris.lesscode.permission.entity.PerPkgPermission;
import com.polaris.lesscode.permission.internal.model.req.ModifyAppPackagePermissionReq;
import com.polaris.lesscode.permission.internal.model.resp.SimpleAppPermissionResp;
import com.polaris.lesscode.permission.rest.PermissionResultCode;

import java.util.Collection;

/**
 * 应用包权限组 业务逻辑接口(内部调用)
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 15:55
 */
public interface PkgPermissionInternalService extends IService<PerPkgPermission> {

    /**
     * 修改应用包权限
     *
     * @param req
     * @return
     * @throws BusinessException {@link PermissionResultCode#PER_APP_PACKAGE_SCOPE_INVALID} 可见范围无效
     *                           {@link PermissionResultCode#PER_APP_PACKAGE_MEMBER_CODE_INVALID}  成员类型无效
     */
    boolean modifyPackagePermission(ModifyAppPackagePermissionReq req) throws BusinessException;

    /**
     * 删除应用包
     *
     * @param orgId
     * @param appPkgIds
     * @param appIds
     * @param userId
     * @return {@code true}
     */
    boolean deletePackagePermission(Long orgId, Collection<Long> appPkgIds, Collection<Long> appIds, Long userId);

    /**
     * 获取应用包权限信息
     *
     * @param orgId 组织ID
     * @param appId 应用ID
     * @return {@code AppPermissionResp}
     */
    SimpleAppPermissionResp getPackagePermission(Long orgId, Long appId);

}
