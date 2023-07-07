package com.polaris.lesscode.permission.open.service;

//import com.polaris.lesscode.exception.BusinessException;
//import com.polaris.lesscode.permission.model.req.CrmRolePermissionListReq;
//import com.polaris.lesscode.permission.model.resp.CrmRolePermissionListResp;
//import com.polaris.lesscode.permission.open.model.req.crm.CrmSaveAppPermissionGroupReq;
//import com.polaris.lesscode.permission.open.model.resp.cmr.CrmAppPerBaseConfigResp;
//import com.polaris.lesscode.permission.open.model.resp.cmr.CrmAppPerGroupInfoResp;
//import com.polaris.lesscode.permission.open.model.resp.cmr.CrmModuleItem;
//import com.polaris.lesscode.permission.open.model.resp.cmr.CrmUserPermissionData;
//import com.polaris.lesscode.permission.rest.PermissionResultCode;
//import com.polaris.lesscode.vo.ResultCode;
//
//import java.util.List;
//
///**
// * @author roamer
// * @version v1.0
// * @date 2020-11-10 14:37
// */
//public interface PermissionOpenService {
//
//    List<CrmUserPermissionData> getUserPermissions(Long orgId, Long userId);
//
//    CrmAppPerBaseConfigResp getAppPermissionBaseConfig(Long orgId, Long appId);
//
//
//    /**
//     * 创建应用权限组
//     *
//     * @param orgId        组织ID
//     * @param operatorId   操作用户ID
//     * @param appId        应用ID
//     * @param req          参数
//     * @return {@code long} 主键id
//     * @throws BusinessException {@link ResultCode#PARAM_ERROR} 参数错误
//     *                           <p>
//     *                           {@link PermissionResultCode#PER_APP_OPT_AUTH_EMPTY} 至少选择一个操作权限
//     *                           <p>
//     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_EMPTY} 至少选择一个字段权限
//     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_CODE_INVALID}  字段权限无效
//     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_REQUIRED} 缺少必选的字段权限
//     */
//    Long createPermissionGroup(Long orgId, Long operatorId, Long appId,
//                               CrmSaveAppPermissionGroupReq req) throws BusinessException;
//
//
//    /**
//     * 修改权限组信息
//     *
//     * @param orgId        组织ID
//     * @param operatorId   操作用户ID
//     * @param appId        应用ID
//     * @param groupId      应用权限组ID
//     * @param req          参数
//     * @return {@code long} 主键id
//     * @throws BusinessException {@link PermissionResultCode#PER_APP_NOT_EXIST} 权限组不存在或已删除
//     *                           {@link PermissionResultCode#PER_APP_NOT_ALLOWED_MODIFY_DEFAULT_GROUP} 不允许修改默认应用权限组
//     *                           <p>
//     *                           {@link PermissionResultCode#PER_APP_OPT_AUTH_EMPTY} 至少选择一个操作权限
//     *                           <p>
//     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_EMPTY} 至少选择一个字段权限
//     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_CODE_INVALID}  字段权限无效
//     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_REQUIRED} 缺少必选的字段权限
//     */
//    boolean updatePermissionGroupInfo(Long orgId, Long operatorId, Long appId, Long groupId,
//                                      CrmSaveAppPermissionGroupReq req) throws BusinessException;
//
//    CrmAppPerGroupInfoResp getPermissionGroupInfo(Long currentOrgId, Long appId, Long groupId);
//
//    List<CrmModuleItem> getAppModuleList(Long orgId, Long operatorId);
//
//    CrmRolePermissionListResp getCrmRolePermissionList(Long orgId, Long operatorId, CrmRolePermissionListReq req);
//}
