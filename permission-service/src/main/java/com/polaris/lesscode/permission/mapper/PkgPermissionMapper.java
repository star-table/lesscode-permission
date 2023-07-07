package com.polaris.lesscode.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polaris.lesscode.permission.entity.PerPkgPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 应用包权限 Mapper
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 10:40
 */
public interface PkgPermissionMapper extends BaseMapper<PerPkgPermission> {

    int appendMembers(@Param("orgId") Long orgId, @Param("appPkgId") Long appPkgId,
                      @Param("deptIds") List<Long> deptIds, @Param("roleIds") List<Long> roleIds,
                      @Param("userIds") List<Long> userIds);

    List<Long> selectUserOfAppPkgIds(@Param("orgId") Long orgId, @Param("userId") Long userId,
                                     @Param("deptIds") String deptIds, @Param("roleIds") String roleIds);
}
