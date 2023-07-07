package com.polaris.lesscode.permission.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.polaris.lesscode.permission.entity.PerPkgPermission;
import com.polaris.lesscode.permission.mapper.PkgPermissionMapper;
import com.polaris.lesscode.permission.service.PkgPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 应用包权限 业务逻辑实现
 * @author roamer
 * @version v1.0
 * @date 2020-09-10 11:20
 */
@Slf4j
@Service
public class PkgPermissionServiceImpl extends ServiceImpl<PkgPermissionMapper, PerPkgPermission>
        implements PkgPermissionService {
}
