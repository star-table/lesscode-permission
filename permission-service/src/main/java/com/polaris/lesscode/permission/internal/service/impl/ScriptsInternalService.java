package com.polaris.lesscode.permission.internal.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.polaris.lesscode.consts.CommonConsts;
import com.polaris.lesscode.permission.entity.PerAppPermissionGroup;
import com.polaris.lesscode.permission.entity.PerAppPermissionMember;
import com.polaris.lesscode.permission.internal.enums.AppPerDefaultGroupLangCode;
import com.polaris.lesscode.permission.mapper.AppPermissionGroupMapper;
import com.polaris.lesscode.permission.mapper.AppPermissionMemberMapper;
import com.polaris.lesscode.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ScriptsInternalService {

    @Autowired
    private AppPermissionGroupMapper permissionGroupMapper;

    @Autowired
    private AppPermissionMemberMapper permissionMemberMapper;

    private static final Long updateVersion = 11L;

    public void defaultPermissionGroupReset(Long orgId){
        long lastId = 0;
        int batch = 500;
        long count = 0;
        List<String> projectMemberOptAuth = AppPerDefaultGroupLangCode.PROJECT_MEMBER.getDefaultOptAuth().get();
        List<String> projectViewerOptAuth = AppPerDefaultGroupLangCode.PROJECT_VIEWER.getDefaultOptAuth().get();
        Collections.sort(projectMemberOptAuth);
        Collections.sort(projectViewerOptAuth);
        String projectMemberOptAuthJson = GsonUtils.toJson(projectMemberOptAuth);
        String projectViewerOptAuthJson = GsonUtils.toJson(projectViewerOptAuth);
        while (true){
            LambdaQueryWrapper<PerAppPermissionGroup> lambdaQueryWrapper = new LambdaQueryWrapper<PerAppPermissionGroup>()
                    .in(PerAppPermissionGroup::getLangCode, Arrays.asList("42", "43"))
                    .eq(PerAppPermissionGroup::getDelFlag, CommonConsts.FALSE)
                    .eq(PerAppPermissionGroup::getReadOnly, CommonConsts.FALSE)
                    .gt(PerAppPermissionGroup::getId, lastId)
                    .ne(PerAppPermissionGroup::getVersion, updateVersion)
                    .orderByAsc(PerAppPermissionGroup::getId)
                    .last("limit " + batch);
            if (Objects.nonNull(orgId) && orgId > 0){
                lambdaQueryWrapper.eq(PerAppPermissionGroup::getOrgId, orgId);
            }
            List<PerAppPermissionGroup> perAppPermissionGroups = permissionGroupMapper.selectList(lambdaQueryWrapper);
            if (CollectionUtils.isEmpty(perAppPermissionGroups)){
                break;
            }
            lastId = perAppPermissionGroups.get(perAppPermissionGroups.size() - 1).getId();
            for (PerAppPermissionGroup permissionGroup: perAppPermissionGroups){
                defaultPermissionGroupReset(permissionGroup, projectMemberOptAuthJson, projectViewerOptAuthJson);
            }
            if (perAppPermissionGroups.size() < batch){
                break;
            }
            count += batch;
            log.info("defaultPermissionGroupReset: process {}", count);
        }
    }

    private void defaultPermissionGroupReset(PerAppPermissionGroup permissionGroup, String projectMemberOptAuthJson, String projectViewerOptAuthJson){
        boolean access = true;
        List<String> optAuth = JSON.parseArray(permissionGroup.getOptAuth(), String.class);
        Collections.sort(optAuth);
        String optAuthJson = GsonUtils.toJson(optAuth);
        if (Objects.equals(permissionGroup.getLangCode(), AppPerDefaultGroupLangCode.PROJECT_MEMBER.getCode())){
            access = Objects.equals(optAuthJson, projectMemberOptAuthJson);
            if (access){
                access = Objects.equals(permissionGroup.getColumnAuth(), "{\"hasRead\": 1, \"hasWrite\": 1}") || Objects.equals(permissionGroup.getColumnAuth(), "{}");
            }
        }else if (Objects.equals(permissionGroup.getLangCode(), AppPerDefaultGroupLangCode.PROJECT_VIEWER.getCode())){
            access = Objects.equals(optAuthJson, projectViewerOptAuthJson);
            if (access){
                access = Objects.equals(permissionGroup.getColumnAuth(), "{\"hasRead\": 1, \"hasWrite\": 0}") || Objects.equals(permissionGroup.getColumnAuth(), "{}");
            }
        }else{
            log.info("defaultPermissionGroupReset: invalid lang code {}", permissionGroup.getLangCode());
            return;
        }
        if (!access){
            // 原权限组重置
            PerAppPermissionGroup newPermissionGroup = new PerAppPermissionGroup();
            BeanUtils.copyProperties(permissionGroup, newPermissionGroup);
            if (Objects.equals(newPermissionGroup.getLangCode(), AppPerDefaultGroupLangCode.PROJECT_MEMBER.getCode())){
                newPermissionGroup.setOptAuth(projectMemberOptAuthJson);
                newPermissionGroup.setColumnAuth("{\"hasRead\": 1, \"hasWrite\": 1}");
            }else if (Objects.equals(permissionGroup.getLangCode(), AppPerDefaultGroupLangCode.PROJECT_VIEWER.getCode())){
                newPermissionGroup.setOptAuth(projectViewerOptAuthJson);
                newPermissionGroup.setColumnAuth("{\"hasRead\": 1, \"hasWrite\": 0}");
            }
            newPermissionGroup.setReadOnly(CommonConsts.TRUE);
            newPermissionGroup.setVersion(updateVersion);
            permissionGroupMapper.updateById(newPermissionGroup);

            // 原权限组转为新权限组
            permissionGroup.setId(null);
            permissionGroup.setLangCode("");
            permissionGroup.setName(permissionGroup.getName() + "1");
            permissionGroup.setReadOnly(CommonConsts.FALSE);
            permissionGroup.setVersion(updateVersion);
            permissionGroupMapper.insert(permissionGroup);

            // 更新原权限组成员中的groupId, 指向新权限组
            permissionMemberMapper.update(null, new LambdaUpdateWrapper<PerAppPermissionMember>()
                .eq(PerAppPermissionMember::getPermissionGroupId, newPermissionGroup.getId())
                .set(PerAppPermissionMember::getPermissionGroupId, permissionGroup.getId())
            );

            // 插入原权限组成员
            PerAppPermissionMember newPermissionMember = new PerAppPermissionMember();
            newPermissionMember.setOrgId(newPermissionGroup.getOrgId());
            newPermissionMember.setAppId(newPermissionGroup.getAppId());
            newPermissionMember.setPermissionGroupId(newPermissionGroup.getId());
            newPermissionMember.setUserIds("[]");
            newPermissionMember.setDeptIds("[]");
            newPermissionMember.setRoleIds("[]");
            newPermissionMember.setCreator(newPermissionGroup.getCreator());
            newPermissionMember.setUpdator(newPermissionGroup.getUpdator());
            permissionMemberMapper.insert(newPermissionMember);

        }else{
            PerAppPermissionGroup perGroup = new PerAppPermissionGroup();
            perGroup.setId(permissionGroup.getId());
            perGroup.setReadOnly(CommonConsts.TRUE);
            perGroup.setVersion(updateVersion);
            permissionGroupMapper.updateById(perGroup);
        }
    }

}
