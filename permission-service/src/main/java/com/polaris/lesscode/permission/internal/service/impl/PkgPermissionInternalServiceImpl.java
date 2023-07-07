package com.polaris.lesscode.permission.internal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.polaris.lesscode.exception.BusinessException;
import com.polaris.lesscode.permission.entity.PerPkgPermission;
import com.polaris.lesscode.permission.internal.enums.PermissionMemberCode;
import com.polaris.lesscode.permission.internal.model.bo.PermissionMembersBo;
import com.polaris.lesscode.permission.internal.model.req.ModifyAppPackagePermissionReq;
import com.polaris.lesscode.permission.internal.model.req.PermissionMembersItemReq;
import com.polaris.lesscode.permission.internal.model.resp.SimpleAppPermissionResp;
import com.polaris.lesscode.permission.internal.service.AppPermissionInternalService;
import com.polaris.lesscode.permission.internal.service.PkgPermissionInternalService;
import com.polaris.lesscode.permission.mapper.PkgPermissionMapper;
import com.polaris.lesscode.permission.rest.PermissionResultCode;
import com.polaris.lesscode.permission.service.AppPermissionService;
import com.polaris.lesscode.permission.service.AppService;
import com.polaris.lesscode.permission.service.UserService;
import com.polaris.lesscode.permission.utils.PerMemberUtils;
import com.polaris.lesscode.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-09-10 11:20
 */
@Slf4j
@Service
public class PkgPermissionInternalServiceImpl extends ServiceImpl<PkgPermissionMapper, PerPkgPermission>
        implements PkgPermissionInternalService {

    private final UserService userService;
    private final AppPermissionInternalService appPermissionInternalService;
    private final AppPermissionService appPermissionService;
    private final AppService appService;

    public PkgPermissionInternalServiceImpl(UserService userService, AppPermissionInternalService appPermissionInternalService, AppPermissionService appPermissionService, AppService appService) {
        this.userService = userService;
        this.appPermissionInternalService = appPermissionInternalService;
        this.appPermissionService = appPermissionService;
        this.appService = appService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyPackagePermission(ModifyAppPackagePermissionReq req) throws BusinessException {
        log.info("[修改应用包权限]-> 参数：{} ", JsonUtils.toJson(req));
//        if (Objects.isNull(AppVisibilityScope.forValue(req.getScope()))) {
//            throw new BusinessException(PermissionResultCode.PER_APP_PACKAGE_SCOPE_INVALID);
//        }
//        List<Long> userIds = Collections.emptyList();
//        List<Long> deptIds = Collections.emptyList();
//        List<Long> roleIds = Collections.emptyList();
//        if (!Objects.equals(AppVisibilityScope.ALL.getCode(), req.getScope())) {
//            Map<String, List<Long>> memberMap = getPermissionMembersMap(req.getMembers());
//            userIds = memberMap.getOrDefault(PermissionMemberCode.USER.getCode(), Collections.emptyList());
//            deptIds = memberMap.getOrDefault(PermissionMemberCode.DEPT.getCode(), Collections.emptyList());
//            roleIds = memberMap.getOrDefault(PermissionMemberCode.ROLE.getCode(), Collections.emptyList());
//        }
//
//
//        PerPkgPermission appPermission = getPerAppPermissionByPackageId(req.getOrgId(), req.getAppPackageId());
//        // 不存在则加锁创建
//        if (Objects.isNull(appPermission)) {
//            Map<String, String> keyMap = new HashMap<>();
//            keyMap.put("orgId", req.getOrgId().toString());
//            keyMap.put("appPackageId", req.getAppPackageId().toString());
//            String key = CacheKeyUtils.buildKey(keyMap, PermissionCacheConstant.APP_PACKAGE_PER_UPDATE_MEMBER_LOCK_KEY);
//            RLock lock = redissonClient.getLock(key);
//            try {
//                if (lock.tryLock(5, TimeUnit.SECONDS)) {
//                    appPermission = getPerAppPermissionByPackageId(req.getOrgId(), req.getAppPackageId());
//                    if (Objects.isNull(appPermission)) {
//                        log.info("[初始化应用包权限]-> 参数：{} ", JsonUtils.toJson(req));
//
//                        createPkgPermission(req.getOrgId(), req.getParentPkgId(), req.getAppPackageId(), req.getUserId(), req.getScope(),
//                                userIds, deptIds, roleIds);
//                    }
//                }
//            } catch (InterruptedException e) {
//                log.error("获取分布式锁失败!", e);
//                throw new SystemException(ResultCode.SYS_ERROR);
//            } finally {
//                if (Objects.nonNull(lock)) {
//                    lock.unlock();
//                }
//            }
//        }
//
//        if (Objects.nonNull(appPermission)) {
//            appPermission.setScope(req.getScope());
//            appPermission.setUserIds(JsonUtils.toJson(userIds));
//            appPermission.setDeptIds(JsonUtils.toJson(deptIds));
//            appPermission.setRoleIds(JsonUtils.toJson(roleIds));
//            appPermission.setUpdator(req.getUserId());
//            baseMapper.updateById(appPermission);
//        }
        return true;
    }

    private void createPkgPermission(Long orgId, Long parentPkgId, Long appPackageId, Long userId, Integer scope, List<Long> userIds,
                                     List<Long> deptIds, List<Long> roleIds) {
        PerPkgPermission newAppPermission = new PerPkgPermission();
        newAppPermission.setOrgId(orgId);
        newAppPermission.setParentPkgId(parentPkgId);
        newAppPermission.setAppPackageId(appPackageId);
        newAppPermission.setScope(scope);
        newAppPermission.setUserIds(JsonUtils.toJson(userIds));
        newAppPermission.setDeptIds(JsonUtils.toJson(deptIds));
        newAppPermission.setRoleIds(JsonUtils.toJson(roleIds));
        newAppPermission.setCreator(userId);
        newAppPermission.setUpdator(userId);
        baseMapper.insert(newAppPermission);

        // 添加到管理组
        userService.addPkgToManageGroup(orgId, userId, appPackageId);
    }

    /**
     * 获取成员
     *
     * @param members 待验证成员
     * @throws BusinessException {@link PermissionResultCode#PER_APP_PACKAGE_MEMBER_CODE_INVALID}  成员类型无效
     */
    private Map<String, List<Long>> getPermissionMembersMap(List<PermissionMembersItemReq> members)
            throws BusinessException {
        Map<String, List<Long>> map = new HashMap<>();
        members.forEach(e -> {
            if (Objects.isNull(PermissionMemberCode.forValue(e.getK()))) {
                throw new BusinessException(PermissionResultCode.PER_APP_PACKAGE_MEMBER_CODE_INVALID);
            }
            map.computeIfAbsent(e.getK(), k -> new ArrayList<>()).add(e.getV());
        });
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePackagePermission(Long orgId, Collection<Long> appPkgIds, Collection<Long> appIds, Long userId) {
        log.info("[删除应用包权限]-> orgId:{}    appPkgIds:{}    appIds:{}    userId:{}] ", orgId, JsonUtils.toJson(appPkgIds), JsonUtils.toJson(appIds), userId);
        if ((Objects.isNull(appPkgIds) || appPkgIds.isEmpty()) && (Objects.isNull(appIds) || appIds.isEmpty())) {
            log.warn("[删除应用包权限] -> 应用包和应用列表为空");
            return true;
        }
//        // 需要级联删除应用包和子应用包
//        List<PerPkgPermission> pkgList = list(new LambdaQueryWrapper<PerPkgPermission>().eq(PerPkgPermission::getOrgId, orgId));
//        Map<Long, PerPkgPermission> pkgMap = pkgList.stream().collect(Collectors.toMap(PerPkgPermission::getAppPackageId, p -> p));
//        Map<Long, List<PerPkgPermission>> childrenMap = new HashMap<>();
//
//        for (PerPkgPermission pkg : pkgList) {
//            childrenMap.computeIfAbsent(pkg.getParentPkgId(), (k) -> new ArrayList<>()).add(pkg);
//        }
//        PerPkgPermission perPkgPermission = pkgMap.get(appPkgId);
//        if (Objects.isNull(perPkgPermission)) {
//            return true;
//        }
//        Queue<PerPkgPermission> stack = new LinkedList<>();
//        stack.add(perPkgPermission);
//        Set<Long> allPkgIds = new HashSet<>();
//        while (!stack.isEmpty()) {
//            PerPkgPermission per = stack.poll();
//            allPkgIds.add(per.getId());
//            List<PerPkgPermission> children = childrenMap.getOrDefault(per.getId(), Collections.emptyList());
//            children.stream().filter(p -> !allPkgIds.contains(p.getAppPackageId())).forEach(stack::add);
//        }
//        if (allPkgIds.isEmpty()) {
//            return true;
//        }
//
//        update(new LambdaUpdateWrapper<PerPkgPermission>().set(PerPkgPermission::getUpdator, userId)
//                .set(PerPkgPermission::getDelFlag,
//                        CommonConsts.DELETED)
//                .eq(PerPkgPermission::getOrgId, orgId)
//                .in(PerPkgPermission::getAppPackageId,
//                        allPkgIds));

        appPermissionInternalService.deleteAppListPermission(orgId, appIds, userId);


//        // 从管理组删除应用包
//        // todo 子包和下面的应用怎么级联的删除?
//        userService.deletePkgFromManageGroup(orgId, userId, appPkgIds);
        return true;
    }

    @Override
    public SimpleAppPermissionResp getPackagePermission(Long orgId, Long appPackageId) {
        SimpleAppPermissionResp resp = new SimpleAppPermissionResp();
        PerPkgPermission permission = getPerAppPermissionByPackageId(orgId, appPackageId);
        if (Objects.isNull(permission)) {
            throw new BusinessException(PermissionResultCode.PER_APP_PACKAGE_NOT_EXIST);
        }
        Map<Long, Map<String, List<Long>>> memberIdsMap = new HashMap<>();
        Map<String, List<Long>> map = new HashMap<>();
        if (Objects.nonNull(permission.getUserIds())) {
            map.put(PermissionMemberCode.USER.getCode(), JsonUtils.fromJsonArray(permission.getUserIds(), Long.class));
        }
        if (Objects.nonNull(permission.getDeptIds())) {
            map.put(PermissionMemberCode.DEPT.getCode(), JsonUtils.fromJsonArray(permission.getDeptIds(), Long.class));
        }
        if (Objects.nonNull(permission.getRoleIds())) {
            map.put(PermissionMemberCode.ROLE.getCode(), JsonUtils.fromJsonArray(permission.getRoleIds(), Long.class));
        }
        memberIdsMap.put(permission.getId(), map);

        PermissionMembersBo membersBo = PerMemberUtils.convertToMembersBo(userService,
                orgId, memberIdsMap).getOrDefault(permission.getId(), new PermissionMembersBo());
        resp.setMembers(membersBo);
        resp.setScope(permission.getScope());
        return resp;
    }


    private PerPkgPermission getPerAppPermissionByPackageId(Long orgId, Long appPackageId) {
        return baseMapper.selectOne(
                new LambdaQueryWrapper<PerPkgPermission>().eq(PerPkgPermission::getOrgId, orgId)
                        .eq(PerPkgPermission::getAppPackageId, appPackageId));
    }
}
