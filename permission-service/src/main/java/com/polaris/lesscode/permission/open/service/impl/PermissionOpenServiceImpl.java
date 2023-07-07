package com.polaris.lesscode.permission.open.service.impl;

//import com.alibaba.fastjson.TypeReference;
//import com.polaris.lesscode.app.internal.enums.AppType;
//import com.polaris.lesscode.app.internal.resp.AppPackageResp;
//import com.polaris.lesscode.app.internal.resp.AppResp;
//import com.polaris.lesscode.dc.internal.dsl.Condition;
//import com.polaris.lesscode.exception.BusinessException;
//import com.polaris.lesscode.form.internal.sula.FieldParam;
//import com.polaris.lesscode.permission.entity.PerAppPermissionConfig;
//import com.polaris.lesscode.permission.entity.PerAppPermissionGroup;
//import com.polaris.lesscode.permission.internal.enums.FormFieldAuthCode;
//import com.polaris.lesscode.permission.internal.model.OrgUserPermissionContext;
//import com.polaris.lesscode.permission.internal.model.bo.ViewAuth;
//import com.polaris.lesscode.permission.model.req.CrmRolePermissionListReq;
//import com.polaris.lesscode.permission.model.resp.CrmPerModuleData;
//import com.polaris.lesscode.permission.model.resp.CrmRolePerGroupInfoData;
//import com.polaris.lesscode.permission.model.resp.CrmRolePermissionListResp;
//import com.polaris.lesscode.permission.model.resp.PerGroupFieldAuthInfoData;
//import com.polaris.lesscode.permission.open.model.req.crm.CrmSaveAppPermissionGroupReq;
//import com.polaris.lesscode.permission.open.model.resp.cmr.*;
//import com.polaris.lesscode.permission.open.service.PermissionOpenService;
//import com.polaris.lesscode.permission.rest.PermissionResultCode;
//import com.polaris.lesscode.permission.service.AppPermissionService;
//import com.polaris.lesscode.permission.service.AppService;
//import com.polaris.lesscode.permission.service.UserService;
//import com.polaris.lesscode.permission.utils.JsonToFieldUtils;
//import com.polaris.lesscode.permission.utils.PerBaseConfigUtils;
//import com.polaris.lesscode.util.JsonUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @author roamer
// * @version v1.0
// * @date 2020-11-10 14:44
// */
//@Slf4j
//@Service
//public class PermissionOpenServiceImpl implements PermissionOpenService {
//
//    private final UserService userService;
//    private final AppPermissionService appPermissionService;
//    private final AppService appService;
//
//    public PermissionOpenServiceImpl(UserService userService, AppPermissionService appPermissionService, AppService appService) {
//        this.userService = userService;
//        this.appPermissionService = appPermissionService;
//        this.appService = appService;
//    }
//
//    @Override
//    public List<CrmUserPermissionData> getUserPermissions(Long orgId, Long userId) {
//        OrgUserPermissionContext context = userService.getPermissionContext(orgId, userId);
//        List<CrmUserPermissionData> list = new ArrayList<>();
//
////        // 获取 应用权限模版配置
////        List<PerAppPermissionConfig> appPerConfigList = appPermissionService.getAppPermissionConfigList();
////        appPerConfigList.removeIf(c -> Objects.equals(c.getAppType(), AppType.PROJECT.getCode()));
////        Map<Integer, PerAppPermissionConfig> baseConfigByTypeMap = new HashMap<>(appPerConfigList.size());
////        Map<String, PerAppPermissionConfig> baseConfigByAppMap = new HashMap<>(appPerConfigList.size());
////        appPerConfigList.forEach(baseConfig -> {
////            if (Objects.equals(0L, baseConfig.getOrgId()) && Objects.nonNull(baseConfig.getAppType())) {
////                baseConfigByTypeMap.put(baseConfig.getAppType(), baseConfig);
////            } else {
////                String strBl = String.valueOf(baseConfig.getOrgId()) +
////                        baseConfig.getAppId();
////                baseConfigByAppMap.put(strBl, baseConfig);
////            }
////        });
////
////        // 获取所有的应用包和应用
////        Map<Long, AppResp> appMap = appService.getAppMap(orgId);
////        Map<Long, String> appFormFieldConfigMap = appService.getFormFieldConfigMapByAppIds(orgId, appMap.keySet());
////
////        // 非管理员 获取所有选中的应用权限组
////        Map<Long, List<PerAppPermissionGroup>> selectedAppPerGroupMap = Collections.emptyMap();
////        if (!context.hasAllPermission()) {
////            List<PerAppPermissionGroup> selectedAppPerGroupList = appPermissionService.getAppPermissionGroupsByUser(orgId, userId, context.getDeptIds(), context.getRoleIds());
////            selectedAppPerGroupMap = selectedAppPerGroupList.stream().filter(p -> appMap.containsKey(p.getAppId())).collect(Collectors.groupingBy(PerAppPermissionGroup::getAppId));
////        }
////
////        for (Map.Entry<Long, AppResp> appRespEntry : appMap.entrySet()) {
////            if (Objects.equals(appRespEntry.getValue().getType(), AppType.PROJECT.getCode())){
////                continue;
////            }
////            AppResp app = appRespEntry.getValue();
////            // 获取自定义基础配置,获取不到从默认基础配置取
////            PerAppPermissionConfig appConf = baseConfigByAppMap.get(String.valueOf(app.getOrgId()) + app.getId());
////            if (Objects.isNull(appConf)) {
////                appConf = baseConfigByTypeMap.get(app.getType());
////            }
////
////            //  app所有的操作权限
////            List<CrmPermissionOptAuth> appAllOptAuthList = PerBaseConfigUtils.jsonToOptAuthOptionInfoBoList(appConf.getOptAuthOptions()).stream().map(o -> {
////                CrmPermissionOptAuth op = new CrmPermissionOptAuth();
////                op.setCode(o.getCode());
////                op.setName(o.getName());
////                return op;
////            }).collect(Collectors.toList());
////
////            Map<String, CrmPermissionOptAuth> appAllOptAuthMap =
////                    appAllOptAuthList.stream().collect(Collectors.toMap(CrmPermissionOptAuth::getCode, v -> v));
////            // App所有的字段
////            log.info("appID:{}", app.getId());
////            Map<String, FieldParam> appAllFieldMap = JsonToFieldUtils.fromFieldConfigJsonToFieldByNameMap(appFormFieldConfigMap.get(app.getId()));
////
////            boolean manageApp = context.hasManageAppPermission(app.getId());
////
////            // 管理应用
////            if (manageApp) {
////                CrmUserPermissionData perData = new CrmUserPermissionData();
////                perData.setAppId(app.getId());
////                perData.setComponentType(appConf.getComponentType());
////                perData.setManageApp(true);
////                perData.setOptAuthList(appAllOptAuthList);
////
////                CrmPermissionDataAuth dataAuth = perData.getDataAuth();
////                dataAuth.setCurrentUser(true);
////                dataAuth.setCurrentDept(true);
////                dataAuth.setCurrentDeptAndChildren(true);
////                dataAuth.setAllDept(true);
////
////                appAllFieldMap.forEach((k, v) -> {
////                    CrmPermissionFieldAuth auth = new CrmPermissionFieldAuth();
////                    auth.setName(k);
////                    auth.setLabel(v.getLabel());
////                    auth.setReadable(true);
////                    auth.setWritable(true);
////                    auth.setMasking(false);
////                    auth.setSelected(true);
////                    auth.setMaskingStrategy(Objects.isNull(v.getSensitiveStrategy()) ? "" : v.getSensitiveStrategy());
////                    perData.getFieldAuthList().add(auth);
////                });
////                list.add(perData);
////                continue;
////            }
////
////            if (!selectedAppPerGroupMap.containsKey(app.getId())) {
////                continue;
////            }
////
////            CrmUserPermissionData perData = new CrmUserPermissionData();
////            perData.setAppId(app.getId());
////            perData.setComponentType(appConf.getComponentType());
////            perData.setManageApp(true);
////
////            Set<String> optAuthSet = new HashSet<>();
////            Map<String, Integer> fieldAuth = new HashMap<>();
////            Set<String> crmDataAuthSet = new HashSet<>();
////            List<PerAppPermissionGroup> groupList = selectedAppPerGroupMap.get(app.getId());
////            if (CollectionUtils.isNotEmpty(groupList)) {
////                for (PerAppPermissionGroup group : groupList) {
////                    // 操作权限
////                    optAuthSet.addAll(JsonUtils.fromJsonArray(group.getOptAuth(), String.class));
////                    // 字段权限
////                    Map<String, Integer> fieldAuthMap = JsonUtils.fromJson(group.getFieldAuth(), HashMap.class);
////                    fieldAuthMap.entrySet().stream().filter(entry -> appAllFieldMap.containsKey(entry.getKey())).forEach(entry -> {
////                        String key = entry.getKey();
////                        int code = entry.getValue();
////                        // 是否是敏感字段
////                        boolean isSensitiveField = Objects.equals(appAllFieldMap.get(key).getSensitiveFlag(), 1);
////                        // 多权限组的合并
////                        if (fieldAuth.containsKey(key)) {
////                            FormFieldAuthCode oldAuthCode = FormFieldAuthCode.forValue(fieldAuth.get(key));
////                            FormFieldAuthCode thisAuthCode = FormFieldAuthCode.forValue(code);
////
////                            code = oldAuthCode.isRead() || thisAuthCode.isRead() ? FormFieldAuthCode.READ.getCode() : 0;
////                            code += oldAuthCode.isWrite() || thisAuthCode.isWrite() ? FormFieldAuthCode.WRITE.getCode() : 0;
////
////                            // 脱敏比较特殊，反向获取
////                            code += !oldAuthCode.isMasking() || !thisAuthCode.isMasking() ? 0 : FormFieldAuthCode.MASKING.getCode();
////
////                        }
////
////                        // 非敏感字段，则脱敏设置为false
////                        if (!isSensitiveField && FormFieldAuthCode.forValue(code).isMasking()) {
////                            code -= FormFieldAuthCode.MASKING.getCode();
////                        }
////
////                        fieldAuth.put(key, code);
////                    });
////                    // 大数据的数据权限
////                    List<String> dataAuths = JsonUtils.fromJsonArray(group.getDataAuth(), String.class);
////                    if (dataAuths != null){
////                        crmDataAuthSet.addAll(dataAuths);
////                    }
////                }
////            }
////            perData.setOptAuthList(optAuthSet.stream().filter(appAllOptAuthMap::containsKey).map(appAllOptAuthMap::get)
////                    .collect(Collectors.toList()));
////
////            perData.setDataAuth(new CrmPermissionDataAuth(crmDataAuthSet));
////
////            appAllFieldMap.forEach((k, v) -> {
////                CrmPermissionFieldAuth auth = new CrmPermissionFieldAuth();
////                auth.setName(k);
////                auth.setLabel(v.getLabel());
////                if (fieldAuth.containsKey(k)) {
////                    FormFieldAuthCode code = FormFieldAuthCode.forValue(fieldAuth.get(k));
////                    auth.setReadable(code.isRead());
////                    auth.setWritable(code.isWrite());
////                    auth.setMasking(code.isMasking());
////                    auth.setSelected(true);
////                }
////                perData.getFieldAuthList().add(auth);
////            });
////            list.add(perData);
////        }
//        return list;
//    }
//
//    @Override
//    public CrmAppPerBaseConfigResp getAppPermissionBaseConfig(Long orgId, Long appId) throws BusinessException {
//        return null;
////        AppResp appResp = appService.getApp(orgId, appId);
////        if (Objects.isNull(appResp)) {
////            throw new BusinessException(PermissionResultCode.APP_NOT_EXIST_OR_DELETED);
////        }
////        PerAppPermissionConfig config = appPermissionService.getAppPermissionConfig(orgId, appId, appResp.getType());
////        if (Objects.isNull(config)) {
////            throw new BusinessException(PermissionResultCode.PER_APP_BASE_CONFIG_NOT_MATCHED);
////        }
////        // 获取表单字段
////        String formFieldConfig = appService.getFormFieldConfigByApp(orgId, appId);
////
////        return convertToCrmBaseConfig(orgId, appId, config, formFieldConfig);
//    }
//
//    @Override
//    public Long createPermissionGroup(Long orgId, Long operatorId, Long appId, CrmSaveAppPermissionGroupReq req) throws BusinessException {
//        return appPermissionService.createPermissionGroup(orgId, operatorId, appId, req);
//    }
//
//    @Override
//    public boolean updatePermissionGroupInfo(Long orgId, Long operatorId, Long appId, Long groupId, CrmSaveAppPermissionGroupReq req) throws BusinessException {
//        return appPermissionService.updatePermissionGroupInfo(orgId, operatorId, appId, groupId, req);
//    }
//
//    @Override
//    public CrmAppPerGroupInfoResp getPermissionGroupInfo(Long orgId, Long appId, Long perGroupId) {
//        PerAppPermissionGroup group = appPermissionService.getAssertNotDeleteGroup(orgId, appId, perGroupId);
//        return convertToCrmAppPerGroup(group);
//    }
//
//    @Override
//    public List<CrmModuleItem> getAppModuleList(Long orgId, Long operatorId) {
//        OrgUserPermissionContext context = userService.getPermissionContext(orgId, operatorId);
//        List<AppPackageResp> pkgList = appService.getPkgList(orgId);
//        if (pkgList.isEmpty()) {
//            return Collections.emptyList();
//        }
//        Map<Long, AppResp> pkgMap = new HashMap<>();
//        List<AppResp> appList = appService.getAppList(orgId);
//
//        appList = appList.stream().filter(app -> {
//            if (Objects.equals(app.getType(), AppType.FOLDER.getCode())){
//                pkgMap.put(app.getId(), app);
//                return false;
//            }
//            return true;
//        }).collect(Collectors.toList());
//
////        // 过滤一次
////        appList = appList.stream().filter(o -> {
////            if (Objects.equals(0L, o.getPkgId()) || pkgMap.containsKey(o.getPkgId())) {
////                return true;
////            }
////            log.warn("该应用所在的包已经不存在 -> appId:{}   pkgId:{}", o.getId(), o.getPkgId());
////            return false;
////        }).collect(Collectors.toList());
////        if (appList.isEmpty()) {
////            return Collections.emptyList();
////        }
//        //Comparison method violates its general contract!
////        appList.sort((o1, o2) -> {
////            if(o1 == null && o2 == null){
////                return 0;
////            }
////            if(o1 == null || o2 == null){
////                return 0;
////            }
////
////            // 其中一个没有包
////            if (Objects.equals(o1.getPkgId(), 0L)) {
////                return -1;
////            }
////            if (Objects.equals(o2.getPkgId(), 0L)) {
////                return 1;
////            }
////
////            // 父包不想等
////            AppPackageResp p1 = pkgMap.get(o1.getPkgId());
////            AppPackageResp p2 = pkgMap.get(o2.getPkgId());
////            Stack<AppPackageResp> ap1 = new Stack<>();
////            Stack<AppPackageResp> ap2 = new Stack<>();
////            do {
////                ap1.push(p1);
////                p1 = pkgMap.get(p1.getParentId());
////            } while (Objects.nonNull(p1));
////            do {
////                ap2.push(p2);
////                p2 = pkgMap.get(p2.getParentId());
////            } while (Objects.nonNull(p2));
////
////            int sort = 0;
////            // 从最上层对比
////            while (sort == 0 && !ap1.empty() && !ap2.empty()) {
////                p1 = ap1.pop();
////                p2 = ap2.pop();
////                sort = p1.getSort() - p2.getSort();
////            }
////
////            // 说明app2已经没有包了
////            if (sort == 0 && !ap1.empty()) {
////                return 1;
////            }
////            // 说明app1已经没有包了
////            if (sort == 0 && !ap2.empty()) {
////                return -1;
////            }
////
////            return sort;
////        });
//        return appList.stream().map(app -> {
//            CrmModuleItem item = new CrmModuleItem();
//            item.setOrgId(app.getOrgId());
//            item.setPackageId(app.getParentId());
//            if (!Objects.equals(app.getParentId(), 0L)) {
//                AppResp pkgInfo = pkgMap.get(app.getParentId());
//                if (pkgInfo != null){
//                    item.setPackageName(pkgInfo.getName());
//                }
//            }
//            item.setAppType(app.getType());
//            item.setHasManageApp(context.hasManageAppPermission(app.getId()));
//            item.setAppId(app.getId());
//            item.setAppName(app.getName());
//            return item;
//
//        }).collect(Collectors.toList());
//
//    }
//
//    @Override
//    public CrmRolePermissionListResp getCrmRolePermissionList(Long orgId, Long operatorId, CrmRolePermissionListReq req) {
//        return null;
////        CrmRolePermissionListResp resp = new CrmRolePermissionListResp();
////        // 1.先获取该角色所在的权限组
////        List<PerAppPermissionGroup> selectedAppPerGroupList = appPermissionService.getAppPerGroupsByRole(orgId, Collections.singletonList(req.getRoleId()));
////        if (selectedAppPerGroupList.isEmpty()) {
////            return resp;
////        }
////        // k->appId
////        Map<Long, List<PerAppPermissionGroup>> appPerGroupMap = selectedAppPerGroupList.stream().collect(Collectors.groupingBy(PerAppPermissionGroup::getAppId));
////        resp.setTotal((long) appPerGroupMap.entrySet().size());
////
////        // 2.获取到全局排序过后的app
////        List<CrmModuleItem> appList = getAppModuleList(orgId, operatorId);
////        // 3.逻辑分页，获取appList
////        appList = appList.stream()
////                .filter(c -> appPerGroupMap.containsKey(c.getAppId())).skip((req.getPage() - 1) * req.getSize())
////                .limit(req.getSize())
////                .collect(Collectors.toList());
////        if (appList.isEmpty()) {
////            return resp;
////        }
////
////        // 4.获取 应用权限模版配置
////        List<PerAppPermissionConfig> appPerConfigList = appPermissionService.getAppPermissionConfigList();
////        Map<Integer, PerAppPermissionConfig> baseConfigByTypeMap = new HashMap<>(appPerConfigList.size());
////        Map<String, PerAppPermissionConfig> baseConfigByAppMap = new HashMap<>(appPerConfigList.size());
////        appPerConfigList.forEach(baseConfig -> {
////            if (Objects.equals(0L, baseConfig.getOrgId()) && Objects.nonNull(baseConfig.getAppType())) {
////                baseConfigByTypeMap.put(baseConfig.getAppType(), baseConfig);
////            } else {
////                String strBl = String.valueOf(baseConfig.getOrgId()) +
////                        baseConfig.getAppId();
////                baseConfigByAppMap.put(strBl, baseConfig);
////            }
////        });
////        // 获取字段配置
////        List<Long> appIds = appList.stream().map(CrmModuleItem::getAppId).collect(Collectors.toList());
////        Map<Long, String> formFieldByAppMap = appService.getFormFieldConfigMapByAppIds(orgId, appIds);
////
////        // 5.构建结果级
////        List<CrmPerModuleData> moduleDataList = new ArrayList<>(appList.size());
////        for (CrmModuleItem item : appList) {
////            CrmPerModuleData data = new CrmPerModuleData();
////            // app基本信息
////            data.setAppInfo(item);
////
////            // 获取自定义基础配置,获取不到从默认基础配置取
////            PerAppPermissionConfig baseConfig = baseConfigByAppMap.get(String.valueOf(item.getOrgId()) + item.getAppId());
////            if (Objects.isNull(baseConfig)) {
////                baseConfig = baseConfigByTypeMap.get(item.getAppType());
////            }
////            data.setBaseConfig(convertToCrmBaseConfig(item.getOrgId(), item.getAppId(), baseConfig, formFieldByAppMap.get(item.getAppId())));
////
////            // app包含的权限组
////            List<CrmRolePerGroupInfoData> perGroupList = new ArrayList<>();
////            for (PerAppPermissionGroup group : appPerGroupMap.getOrDefault(item.getAppId(), Collections.emptyList())) {
////                perGroupList.add(convertToCrmRolePerGroupInfoData(data.getBaseConfig(), group));
////            }
////            data.setPermissions(perGroupList);
////
////            moduleDataList.add(data);
////        }
////        resp.setList(moduleDataList);
////        return resp;
//    }
//
//    private CrmAppPerBaseConfigResp convertToCrmBaseConfig(Long orgId, Long appId, PerAppPermissionConfig baseConfig, String formFieldConfig) {
//        return null;
////        CrmAppPerBaseConfigResp configData = new CrmAppPerBaseConfigResp();
////        configData.setOrgId(orgId);
////        configData.setAppId(appId);
////        configData.setCreatable(baseConfig.getCreatable());
////        configData.setOptAuthOptions(PerBaseConfigUtils.jsonToOptAuthOptionInfoBoList(baseConfig.getOptAuthOptions()));
////        configData.setFieldAuthOptions(PerBaseConfigUtils.jsonToFieldAuthOptionInfoBoList(baseConfig.getFieldAuthOptions()));
////        configData.setDataAuthOptions(PerBaseConfigUtils.jsonToCrmDataAuthOptionInfoBoList(baseConfig.getDataAuthOptions()));
////        List<FieldParam> fieldParamList = JsonToFieldUtils.fromFieldConfigJsonToFieldList(formFieldConfig);
////        List<FormFieldInfoResp> fieldInfoRespList = fieldParamList.stream().map(f -> {
////            FormFieldInfoResp resp = new FormFieldInfoResp();
////            resp.setName(Objects.nonNull(f.getName()) ? f.getName() : "");
////            resp.setLabel(Objects.nonNull(f.getLabel()) ? f.getLabel() : "");
////            resp.setFormFieldType(Objects.nonNull(f.getField()) ? f.getField().getType() : "");
////            resp.setSensitiveFlag(Objects.nonNull(f.getSensitiveFlag()) ? f.getSensitiveFlag() : 2);
////            resp.setSensitiveStrategy(Objects.nonNull(f.getSensitiveStrategy()) ? f.getSensitiveStrategy() : "");
////            return resp;
////        }).collect(Collectors.toList());
////        configData.setFormFieldList(fieldInfoRespList);
////        return configData;
//    }
//
//    private CrmAppPerGroupInfoResp convertToCrmAppPerGroup(PerAppPermissionGroup group) {
//        CrmAppPerGroupInfoResp groupInfo = new CrmAppPerGroupInfoResp();
//        groupInfo.setId(group.getId());
//        groupInfo.setLangCode(group.getLangCode());
//        groupInfo.setName(group.getName());
//        groupInfo.setRemake(group.getRemake());
//        groupInfo.setOptAuth(JsonUtils.fromJsonArray(group.getOptAuth(), String.class));
//        groupInfo.setFieldAuth(JsonUtils.fromJson(group.getFieldAuth(), new TypeReference<HashMap<String, Integer>>() {
//        }));
//        groupInfo.setDataAuth(JsonUtils.fromJsonArray(group.getDataAuth(), String.class));
//        groupInfo.setViewAuth(JsonUtils.fromJson(group.getViewAuth(), new TypeReference<LinkedHashMap<String, ViewAuth>>() {
//        }));
//        return groupInfo;
//    }
//
//    private CrmRolePerGroupInfoData convertToCrmRolePerGroupInfoData(CrmAppPerBaseConfigResp baseConfig, PerAppPermissionGroup group) {
//        CrmRolePerGroupInfoData groupInfo = new CrmRolePerGroupInfoData();
//        groupInfo.setId(group.getId());
//        groupInfo.setLangCode(group.getLangCode());
//        groupInfo.setName(group.getName());
//        groupInfo.setRemake(group.getRemake());
//        groupInfo.setOptAuth(JsonUtils.fromJsonArray(group.getOptAuth(), String.class));
//        groupInfo.setFieldAuth(JsonUtils.fromJson(group.getFieldAuth(), new TypeReference<HashMap<String, Integer>>() {
//        }));
//        groupInfo.setDataAuth(JsonUtils.fromJsonArray(group.getDataAuth(), String.class));
//        groupInfo.setLcDataAuth(JsonUtils.fromJson(group.getDataAuth(), Condition.class));
//        groupInfo.setViewAuth(JsonUtils.fromJson(group.getViewAuth(), new TypeReference<HashMap<String, ViewAuth>>() {
//        }));
//        List<PerGroupFieldAuthInfoData> fieldAuthInfoDataList = new ArrayList<>();
//        for (FormFieldInfoResp field : baseConfig.getFormFieldList()) {
//            Map<String, Integer> auth = groupInfo.getFieldAuth();
//            // 选中的有该字段
//            if (!auth.containsKey(field.getName())) {
//                continue;
//            }
//            PerGroupFieldAuthInfoData fieldAuthInfoData = new PerGroupFieldAuthInfoData();
//            int code = auth.get(field.getName());
//            FormFieldAuthCode authCode = FormFieldAuthCode.forValue(code);
//            // 不是敏感字段，但是勾选了脱敏
//            if (!Objects.equals(field.getSensitiveFlag(), 1) && authCode.isMasking()) {
//                code -= 4;
//            }
//            authCode = FormFieldAuthCode.forValue(code);
//            fieldAuthInfoData.setName(field.getName());
//            fieldAuthInfoData.setLabel(field.getLabel());
//            fieldAuthInfoData.setSensitiveFlag(field.getSensitiveFlag());
//            fieldAuthInfoData.setSensitiveStrategy(field.getSensitiveStrategy());
//            fieldAuthInfoData.setIsRead(authCode.isRead());
//            fieldAuthInfoData.setIsWrite(authCode.isWrite());
//            fieldAuthInfoData.setIsMasking(authCode.isMasking());
//            fieldAuthInfoDataList.add(fieldAuthInfoData);
//        }
//        groupInfo.setFieldAuthInfoDataList(fieldAuthInfoDataList);
//        return groupInfo;
//    }
//}
