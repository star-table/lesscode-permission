package com.polaris.lesscode.permission.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.exception.BusinessException;
import com.polaris.lesscode.permission.entity.PerAppPermissionGroup;
import com.polaris.lesscode.permission.internal.model.bo.ViewAuth;
import com.polaris.lesscode.permission.internal.model.req.*;
import com.polaris.lesscode.permission.internal.model.resp.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 应用权限组 业务逻辑接口(内部调用)
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 15:55
 */
public interface AppPermissionInternalService extends IService<PerAppPermissionGroup> {

    /**
     * 批量获取管理组
     * @param appIds
     * @return
     */
    List<AppPermissionGroupResp> getAppPermissionGroupBatch(List<Long> appIds);

    /**
     * 创建管理组
     * @param req
     * @return
     */
    List<AppPermissionGroupResp> createAppPermissionGroupBatch(List<CreateAppPermissionGroupReq> req);

    /**
     * 获取应用权限组列表
     *
     * @Author Nico
     * @Date 2021/6/7 11:30
     **/
    public List<AppPerGroupListItem> getAppPermissionGroupList(Long orgId, Long appId);

    /**
     * 初始化应用权限
     *
     * @param req
     * @return
     * @throws BusinessException
     */
    boolean initAppPermission(InitAppPermissionReq req) throws BusinessException;

    /**
     * 初始化应用字段权限(创建表格时)
     *
     * @param req
     * @return
     * @throws BusinessException
     */
    boolean initAppPermissionFieldAuthCreateTable(InitAppPermissionFieldAuthCreateTableReq req) throws BusinessException;

    /**
     * 初始化应用字段权限(删除表格时)
     *
     * @param req
     * @return
     * @throws BusinessException
     */
    boolean initAppPermissionFieldAuthDeleteTable(InitAppPermissionFieldAuthDeleteTableReq req) throws BusinessException;

    /**
     * 更新权限组
     *
     * @Author Nico
     * @Date 2021/4/22 18:34
     **/
    boolean updateAppPermission(UpdateAppPermissionReq req) throws BusinessException;

    /**
     * 更新权限组，通过langcode
     *
     * @Author Nico
     * @Date 2021/4/22 18:34
     **/
    boolean updateAppPermissionByLangCode(UpdateAppPermissionByLangCodeReq req) throws BusinessException;

    /**
     * 删除应用权限
     *
     * @param orgId
     * @param appId
     * @param userId
     * @return
     */
    boolean deleteAppPermission(Long orgId, Long appId, Long userId);

    /**
     * 删除包下所以应用权限
     *
     * @param orgId
     * @param appIds
     * @param userId
     * @return
     */
    boolean deleteAppListPermission(Long orgId, Collection<Long> appIds, Long userId);

    /**
     * 获取该人员在此应用的字段权限
     *
     * @param orgId
     * @param appId
     * @param userId
     * @return
     */
    Map<String, Map<String, Integer>> getPermissionGroupFieldAuth(Long orgId, Long appId, Long userId);

    /**
     * 获取该人员在此应用的表格权限
     *
     * @param orgId
     * @param appId
     * @param userId
     * @return
     */
    List<String> getPermissionGroupTableAuth(Long orgId, Long appId, Long userId);

    /**
     * 获取该人员在此应用的视图权限
     *
     * @Author Nico
     * @Date 2021/1/26 13:57
     **/
    Map<String, ViewAuth> getPermissionGroupViewAuth(Long orgId, Long appId, Long userId);

    /**
     * 获取该人员在此应用的操作权限
     *
     * @param orgId
     * @param appId
     * @param userId
     * @return
     */
    FromPerOptAuthVO getPermissionGroupOptAuth(Long orgId, Long appId, Long userId);

    /**
     * 获取该人员在此应用的数据权限
     *
     * @param orgId
     * @param appId
     * @param userId
     * @return
     */
    List<Condition> getPermissionGroupDataAuth(Long orgId, Long appId, Long userId);

//    /**
//     * 修改表单字段配置
//     *
//     * @param req req
//     * @return {@code rea}
//     */
//    boolean updateFormFieldConfig(UpdateFieldConfigReq req);

    List<String> getPermissionGroupOptAuthList(Long orgId, Long appId, Long userId);

    /**
     * 获取用户可见的应用包和应用
     *
     * @param orgId
     * @param userId
     * @return
     */
    UserAppPermissionListResp getUserAppPermissionListResp(Long orgId, Long userId);

    /**
     * 获取应用权限
     **/
    AppAuthorityResp getAppAuthority(Long orgId, Long appId, Long tableId, Long userId);

    /**
     * 获取应用权限（使用传入的协作人角色）
     **/
    AppAuthorityResp getAppAuthorityByCollaboratorRoles(Long orgId, Long appId, Long userId, List<Long> collaboratorsRoleIds);

    /**
     * 获取应用权限（不包含协作人角色）
     **/
    AppAuthorityResp getAppAuthorityWithoutCollaborator(Long orgId, Long appId, Long userId);

    /**
     * 批量获取应用权限
     *
     * @param req {@link GetAppAuthorityBatchReq}
     * @return Map, K->appId, V->{@link AppAuthorityResp}
     */
    Map<Long, AppAuthorityResp> getAppAuthorityBatch(GetAppAuthorityBatchReq req);

    /*
     * 获取数据权限
     **/
    AppAuthorityResp getDataAuthority(Long orgId, Long appId, Long userId, Long dataId);

    /**
     * 批量获取数据权限
     **/
    Map<Long, AppAuthorityResp> getDataAuthorityBatch(GetDataAuthorityBatchReq getDataAuthorityBatchReq);

    /**
     * 批量获取数据权限
     */
    Map<Long, AppAuthorityResp> getDataAuthorities(GetDataAuthorityBatchReq getDataAuthorityBatchReq);

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
     * 移除应用成员
     *
     * @Author Nico
     * @Date 2021/6/7 16:06
     **/
    boolean removeAppMembers(RemoveAppMembersReq req);

    /**
     * 新增应用成员
     *
     * @Author Nico
     * @Date 2021/6/7 16:06
     **/
    boolean addAppMembers(AddAppMembersReq req);

    /**
     * 复制权限组
     *
     * @Author Nico
     * @Date 2021/6/15 14:19
     **/
    void copyPermissionGroup(CopyAppPermissionGroupReq req);

    boolean updatePermissionTime(UpdatePermissionTimeReq req);

    GetPermissionUpdateTimeResp getPermissionUpdateTime(GetPermissionUpdateTimeReq req);
}
