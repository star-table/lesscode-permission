package com.polaris.lesscode.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.polaris.lesscode.exception.BusinessException;
import com.polaris.lesscode.permission.entity.PerAppPermissionConfig;
import com.polaris.lesscode.permission.entity.PerAppPermissionGroup;
import com.polaris.lesscode.permission.entity.PerAppPermissionMember;
import com.polaris.lesscode.permission.internal.model.req.GetAppAuthorityBatchReq;
import com.polaris.lesscode.permission.internal.model.req.GetDataAuthorityBatchReq;
import com.polaris.lesscode.permission.internal.model.resp.AppAuthorityResp;
import com.polaris.lesscode.permission.internal.model.resp.AppPerGroupListResp;
import com.polaris.lesscode.permission.internal.model.resp.MemberGroupMappingsResp;
import com.polaris.lesscode.permission.model.req.AppPermissionGroupInfoListReq;
import com.polaris.lesscode.permission.model.req.MemberSwitchGroupReq;
import com.polaris.lesscode.permission.model.req.SaveAppPermissionGroupReq;
import com.polaris.lesscode.permission.model.req.UpdateAppPerMembersReq;
import com.polaris.lesscode.permission.model.resp.AppPerBaseConfigResp;
import com.polaris.lesscode.permission.model.resp.AppPerGroupInfoResp;
//import com.polaris.lesscode.permission.open.model.req.crm.CrmSaveAppPermissionGroupReq;
import com.polaris.lesscode.permission.rest.PermissionResultCode;
import com.polaris.lesscode.vo.ResultCode;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 应用权限组 业务逻辑接口
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 15:55
 */
public interface AppPermissionService extends IService<PerAppPermissionGroup> {

    AppAuthorityResp getAppAuthority(Long orgId, Long appId, Long tableId, Long userId, Boolean isContainCollaborator, List<Long> collaboratorRoleIds);

    AppAuthorityResp getDataAuthority(Long orgId, Long appId, Long userId, Long dataId);

    Map<Long, AppAuthorityResp> getDataAuthorityBatch(GetDataAuthorityBatchReq getDataAuthorityBatchReq);

    Map<Long, AppAuthorityResp> getDataAuthorities(GetDataAuthorityBatchReq getDataAuthorityBatchReq);

    /**
     * 批量获取应用权限
     *
     * @param req {@link GetAppAuthorityBatchReq}
     * @return Map, K->appId, V->{@link AppAuthorityResp}
     */
    Map<Long, AppAuthorityResp> getAppAuthorityBatch(GetAppAuthorityBatchReq req);

    /**
     * 创建应用权限组
     *
     * @param orgId        组织ID
     * @param operatorId   操作用户ID
     * @param appId        应用ID
     * @param req          参数
     * @return {@code long} 主键id
     * @throws BusinessException {@link ResultCode#PARAM_ERROR} 参数错误
     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_REF_INVALID} 条件关联方式无效
     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_METHOD_INVALID} 过滤方法无效
     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_DATA_TYPE_INVALID} 字段类型无效
     *                           <p>
     *                           {@link PermissionResultCode#PER_APP_OPT_AUTH_EMPTY} 至少选择一个操作权限
     *                           <p>
     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_EMPTY} 至少选择一个字段权限
     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_CODE_INVALID}  字段权限无效
     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_REQUIRED} 字段权限必须设置为可见
     */
    Long createPermissionGroup(Long orgId, Long operatorId, Long appId,
                               SaveAppPermissionGroupReq req) throws BusinessException;

//    /**
//     * 创建应用权限组
//     *
//     * @param orgId        组织ID
//     * @param operatorId   操作用户ID
//     * @param appId        应用ID
//     * @param req          参数
//     * @return {@code long} 主键id
//     * @throws BusinessException {@link ResultCode#PARAM_ERROR} 参数错误
//     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_REF_INVALID} 条件关联方式无效
//     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_METHOD_INVALID} 过滤方法无效
//     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_DATA_TYPE_INVALID} 字段类型无效
//     *                           <p>
//     *                           {@link PermissionResultCode#PER_APP_OPT_AUTH_EMPTY} 至少选择一个操作权限
//     *                           <p>
//     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_EMPTY} 至少选择一个字段权限
//     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_CODE_INVALID}  字段权限无效
//     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_REQUIRED} 字段权限必须设置为可见
//     */
//    Long createPermissionGroup(Long orgId, Long operatorId, Long appId,
//                               CrmSaveAppPermissionGroupReq req) throws BusinessException;

    /**
     * 修改权限组信息
     *
     * @param orgId        组织ID
     * @param operatorId   操作用户ID
     * @param appId        应用ID
     * @param groupId      应用权限组ID
     * @param req          参数
     * @return {@code long} 主键id
     * @throws BusinessException {@link PermissionResultCode#PER_APP_NOT_EXIST} 权限组不存在或已删除
     *                           {@link PermissionResultCode#PER_APP_NOT_ALLOWED_MODIFY_DEFAULT_GROUP} 不允许修改默认应用权限组
     *                           <p>
     *                           {@link ResultCode#PARAM_ERROR} 参数错误
     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_REF_INVALID} 条件关联方式无效
     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_METHOD_INVALID} 过滤方法无效
     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_DATA_TYPE_INVALID} 字段类型无效
     *                           <p>
     *                           {@link PermissionResultCode#PER_APP_OPT_AUTH_EMPTY} 至少选择一个操作权限
     *                           <p>
     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_EMPTY} 至少选择一个字段权限
     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_CODE_INVALID}  字段权限无效
     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_REQUIRED} 字段权限必须设置为可见
     */
    boolean updatePermissionGroup(Long orgId, Long operatorId, Long appId, Long groupId,
                                  SaveAppPermissionGroupReq req) throws BusinessException;

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
//     *                           {@link ResultCode#PARAM_ERROR} 参数错误
//     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_REF_INVALID} 条件关联方式无效
//     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_METHOD_INVALID} 过滤方法无效
//     *                           {@link PermissionResultCode#PER_APP_DATA_AUTH_DATA_TYPE_INVALID} 字段类型无效
//     *                           <p>
//     *                           {@link PermissionResultCode#PER_APP_OPT_AUTH_EMPTY} 至少选择一个操作权限
//     *                           <p>
//     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_EMPTY} 至少选择一个字段权限
//     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_CODE_INVALID}  字段权限无效
//     *                           {@link PermissionResultCode#PER_APP_FIELD_AUTH_REQUIRED} 字段权限必须设置为可见
//     */
//    boolean updatePermissionGroupInfo(Long orgId, Long operatorId, Long appId, Long groupId,
//                                      CrmSaveAppPermissionGroupReq req) throws BusinessException;

//    /**
//     * 修改权限组成员
//     *
//     * @param orgId        组织ID
//     * @param operatorId   操作用户ID
//     * @param appId        应用ID
//     * @param groupId      应用权限组ID
//     * @param req          参数
//     * @return {@code true} 修改成功
//     * @throws BusinessException {@link PermissionResultCode#PER_APP_NOT_EXIST} 权限组不存在或已删除
//     *                           {@link PermissionResultCode#PER_APP_MEMBER_CODE_INVALID}  成员类型无效
//     */
//    boolean updatePermissionGroupMembers(Long orgId, Long operatorId, Long appId, Long groupId, UpdateAppPerMembersReq req) throws BusinessException;

    /**
     * 切换权限组
     * @param orgId        组织ID
     * @param operatorId   操作用户ID
     * @param appId        应用ID
     * @param groupId      应用权限组ID
     * @param req          参数
     * @Author Nico
     * @Date 2021/5/26 14:35
     **/
    boolean memberSwitchGroup(Long orgId, Long operatorId, Long appId, Long groupId, MemberSwitchGroupReq req) throws BusinessException;

    /**
     * 查询和权限组的映射关系
     *
     * @param orgId        组织ID
     * @param operatorId   操作用户ID
     * @param appId        应用ID
     * @Author Nico
     * @Date 2021/5/26 16:53
     **/
    MemberGroupMappingsResp memberGroupMappings(Long orgId, Long operatorId, Long appId);

    /**
     * 删除权限组
     *
     * @param orgId        组织ID
     * @param operatorId   操作用户ID
     * @param appId        应用ID
     * @param groupId      应用权限组ID
     * @return {@code true} 修改成功
     * @throws BusinessException {@link PermissionResultCode#PER_APP_NOT_EXIST} 权限组不存在或已删除
     *                           {@link PermissionResultCode#PER_APP_NOT_ALLOWED_DELETE_DEFAULT_GROUP} 不允许删除默认权限组
     */
    boolean deletePermissionGroup(Long orgId, Long operatorId, Long appId, Long groupId) throws BusinessException;

    PerAppPermissionMember getPermissionGroupMemberByGroupId(Long orgId, Long appId, Long groupId);

    List<PerAppPermissionMember> getPermissionGroupMemberList(Long orgId, Long appId);

    List<PerAppPermissionGroup> getAppPermissionGroupList(Long orgId, Long appId);

//    List<PerAppPermissionGroup> getAppPermissionGroupList(Long orgId, Long appId, String name);

    AppPerGroupListResp getPermissionGroupList(Long orgId, Long operatorId, Long appId);

    /**
     * 获取权限组信息
     *
     * @param orgId   组织ID
     * @param appId   应用ID
     * @param groupId 应用权限组ID
     * @return {@code AppPerGroupInfoResp} 信息
     * @throws BusinessException {@link PermissionResultCode#PER_APP_NOT_EXIST} 权限组不存在或已删除
     */
    AppPerGroupInfoResp getPermissionGroupInfo(Long orgId, Long appId, Long groupId)
            throws BusinessException;

    /**
     * 获取权限组信息
     *
     * @param orgId   组织ID
     * @param appId   应用ID
     * @param req     应用权限组ID
     * @return {@code List<AppPerGroupInfoResp>} 信息
     * @throws BusinessException {@link PermissionResultCode#PER_APP_NOT_EXIST} 权限组不存在或已删除
     */
    List<AppPerGroupInfoResp> getPermissionGroupInfoList(Long orgId, Long appId, AppPermissionGroupInfoListReq req)
            throws BusinessException;

    /**
     * 获取权限基础配置
     *
     * @param orgId        组织ID
     * @param appId        应用ID
     * @return {@code FormPermissionInfoResp} 信息
     * @throws BusinessException {@link PermissionResultCode#PER_APP_NOT_EXIST} 权限组不存在或已删除
     */
    AppPerBaseConfigResp getPermissionBaseConfig(Long orgId, Long appId) throws BusinessException;

    /**
     * 获取应用权限配置
     * 会先根据AppID取，AppID取不到，则根据AppType获取
     *
     * @param orgId        组织ID
     * @param appId        应用ID
     * @param appType      应用类型
     * @return {@code PerAppPermissionConfig}
     */
    PerAppPermissionConfig getAppPermissionConfig(Long orgId, Long appId, Integer appType);

    /**
     * 获取应用权限配置
     *
     * @return {@code List<PerAppPermissionConfig>}
     */
    List<PerAppPermissionConfig> getAppPermissionConfigList();

    /**
     * 获取应用权限
     *
     * @param orgId 组织ID
     * @return {@code List<PerAppPermissionGroup>}
     */
    List<PerAppPermissionGroup> getAppPermissionGroupsByUser(Long orgId, Long appId,
                                                             Long userId, Collection<Long> deptIds, Collection<Long> roleIds);

    /**
     * 获取应用权限
     *
     * @param orgId 组织ID
     * @return {@code List<PerAppPermissionGroup>}
     */
    List<PerAppPermissionGroup> getAppPermissionGroupsByUser(Long orgId, Long userId, Collection<Long> deptIds,
                                                             Collection<Long> roleIds);

    /**
     * 获取应用权限
     *
     * @param orgId 组织ID
     * @return {@code List<PerAppPermissionGroup>}
     */
    List<PerAppPermissionGroup> getAppPerGroupsByRole(Long orgId, Collection<Long> roleIds);

    PerAppPermissionGroup getAssertNotDeleteGroup(Long orgId, Long appId, Long perGroupId);

    List<PerAppPermissionGroup> getAssertNotDeleteGroups(Long orgId, Long appId, List<Long> perGroupIds);

    List<PerAppPermissionGroup> getAssertNotDeleteGroups(Long orgId, Long appId);
}
