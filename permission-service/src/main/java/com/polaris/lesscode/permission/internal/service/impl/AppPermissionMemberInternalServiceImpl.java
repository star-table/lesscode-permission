package com.polaris.lesscode.permission.internal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.polaris.lesscode.permission.entity.PerAppPermissionMember;
import com.polaris.lesscode.permission.internal.service.AppPermissionMemberInternalService;
import com.polaris.lesscode.permission.mapper.AppPermissionMemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 应用权限组成员内部service impl
 *
 * @Author Nico
 * @Date 2020/12/29 20:44
 **/
@Slf4j
@Service
public class AppPermissionMemberInternalServiceImpl extends ServiceImpl<AppPermissionMemberMapper, PerAppPermissionMember>
        implements AppPermissionMemberInternalService {

}
