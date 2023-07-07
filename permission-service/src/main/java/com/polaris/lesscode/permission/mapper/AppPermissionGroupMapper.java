package com.polaris.lesscode.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polaris.lesscode.permission.entity.PerAppPermissionGroup;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 应用权限组 Mapper
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 10:40
 */
public interface AppPermissionGroupMapper extends BaseMapper<PerAppPermissionGroup> {


    List<PerAppPermissionGroup> selectUserAppPerGroupsByUserAndApp(@Param("orgId") Long orgId,
                                                                   @Param("appId") Long appId, @Param("userId") Long userId,
                                                                   @Param("deptIds") String deptIds,
                                                                   @Param("roleIds") String roleIds);

    List<PerAppPermissionGroup> selectUserAppPerGroupsByUserAndApps(@Param("orgId") Long orgId,
                                                                   @Param("appIds") List<Long> appIds, @Param("userId") Long userId,
                                                                   @Param("deptIds") String deptIds,
                                                                   @Param("roleIds") String roleIds);


    List<PerAppPermissionGroup> selectUserAppPerGroupsByUser(@Param("orgId") Long orgId, @Param("userId") Long userId,
                                                             @Param("deptIds") String deptIds,
                                                             @Param("roleIds") String roleIds);


    List<PerAppPermissionGroup> selectAppPerGroupsByRole(@Param("orgId") Long orgId, @Param("roleIds") String roleIds);


//    /**
//     * 修改字段 同步到app对应的权限组中
//     *
//     * @param orgId
//     * @param appId
//     * @param deleteFields
//     * @param saveFieldMap
//     * @return
//     */
//    @Deprecated
//    int updateFieldAuthToPerGroup(@Param("orgId") Long orgId, @Param("appId") Long appId,
//                                  @Param("deleteFields") Collection<String> deleteFields, @Param("saveFieldMap") Map<String, Integer> saveFieldMap);
}
