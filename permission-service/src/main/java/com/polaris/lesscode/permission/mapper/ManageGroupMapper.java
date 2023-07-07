package com.polaris.lesscode.permission.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polaris.lesscode.consts.CommonConsts;
import com.polaris.lesscode.permission.entity.PerManageGroup;
import com.polaris.lesscode.permission.entity.PerManagePermissionConfig;
import com.polaris.lesscode.util.GsonUtils;

import java.util.List;

/**
 * 管理组 Mapper
 *
 * @author Nico
 * @version v1.0
 * @date 2020-12-25
 */
public interface ManageGroupMapper extends BaseMapper<PerManageGroup> {

    /**
     * 通过用户ids获取管理组列表
     *
     * @param orgId 组织id
     * @param userIds 用户列表
     * @return 管理组列表
     **/
    default List<PerManageGroup> getManageGroupListByUsers(Long orgId, List<Long> userIds){
        List<PerManageGroup> groups = selectList(new LambdaQueryWrapper<PerManageGroup>()
        .eq(PerManageGroup::getIsDelete, CommonConsts.FALSE)
        .eq(PerManageGroup::getOrgId, orgId)
        .apply("JSON_OVERLAPS(`user_ids` -> '$', CAST('" + GsonUtils.toJson(userIds) + "' AS JSON))"));
        return groups;
    }

    /**
     * 通过组织id获取管理组列表
     *
     * @param orgId 组织id
     * @return 管理组列表
     **/
    default List<PerManageGroup> getManageGroupListByOrg(Long orgId){
        List<PerManageGroup> groups = selectList(new LambdaQueryWrapper<PerManageGroup>()
                .eq(PerManageGroup::getIsDelete, CommonConsts.FALSE)
                .eq(PerManageGroup::getOrgId, orgId)
                .orderByAsc(PerManageGroup::getCreateTime));
        return groups;
    }

    /**
     * 获取管理组
     *
     * @param orgId 组织id
     * @param groupId 管理组id
     * @return 管理组
     **/
    default PerManageGroup getManageGroup(Long orgId, Long groupId){
        return selectOne(new LambdaQueryWrapper<PerManageGroup>()
        .eq(PerManageGroup::getId, groupId)
        .eq(PerManageGroup::getOrgId, orgId)
        .eq(PerManageGroup::getIsDelete, CommonConsts.FALSE));
    }
}
