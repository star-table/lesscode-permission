package com.polaris.lesscode.permission.internal.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.permission.entity.PerAppPermissionGroup;
import com.polaris.lesscode.permission.internal.enums.AppPerDefaultGroupLangCode;
import com.polaris.lesscode.permission.internal.enums.FormFieldAuthCode;
import com.polaris.lesscode.permission.internal.model.bo.ViewAuth;
import com.polaris.lesscode.util.JsonUtils;
import com.polaris.lesscode.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AppPermissionGroupMergeServiceImpl {

    public boolean mergeIsCollaborator(List<PerAppPermissionGroup> permissionGroups) {
        boolean isCollaborator = true;
        if (CollectionUtils.isNotEmpty(permissionGroups)) {
            for (PerAppPermissionGroup group: permissionGroups) {
                if (group == null) { continue; }
                if (!group.isCollaborator()) {
                    isCollaborator = false;
                    break;
                }
            }
        }
        return isCollaborator;
    }

    public AppPerDefaultGroupLangCode mergeLangCode(List<PerAppPermissionGroup> permissionGroups) {
        AppPerDefaultGroupLangCode appPerDefaultGroupLangCode = null;
        if (CollectionUtils.isNotEmpty(permissionGroups)) {
            for (PerAppPermissionGroup group: permissionGroups) {
                if (group == null) { continue; }
                String groupLangCode = group.getLangCode();
                if (Objects.equals(groupLangCode, AppPerDefaultGroupLangCode.DASHBOARD_ADMINISTRATOR.getCode())) {
                    groupLangCode = AppPerDefaultGroupLangCode.OWNER.getCode();
                }
                AppPerDefaultGroupLangCode langCode = AppPerDefaultGroupLangCode.forValue(groupLangCode);
                if (langCode != null) {
                    if (appPerDefaultGroupLangCode == null) {
                        appPerDefaultGroupLangCode = langCode;
                    } else if (langCode.getAdmin()) {
                        appPerDefaultGroupLangCode = langCode;
                    }
                }
            }
        }
        return appPerDefaultGroupLangCode;
    }

    public List<Condition> mergeDataAuth(List<PerAppPermissionGroup> permissionGroups) {
        List<Condition> dataAuth = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(permissionGroups)) {
            for (PerAppPermissionGroup group : permissionGroups) {
                if (group == null) { continue; }
                if (StringUtils.isNotBlank(group.getDataAuth())) {
                    dataAuth.add(JsonUtils.fromJson(group.getDataAuth(), Condition.class));
                }
            }
        }
        return dataAuth;
    }

    public Map<String, ViewAuth> mergeViewAuth(List<PerAppPermissionGroup> permissionGroups) {
        Map<String, ViewAuth> viewAuth = new LinkedHashMap<>();

        if (CollectionUtils.isNotEmpty(permissionGroups)) {
            for (PerAppPermissionGroup group : permissionGroups) {
                if (group == null) { continue; }
                Map<String, ViewAuth> viewAuthMap = JsonUtils.fromJson(group.getViewAuth(), new TypeReference<LinkedHashMap<String, ViewAuth>>() {});
                if (!MapUtils.isEmpty(viewAuthMap)) {
                    if (org.apache.commons.collections4.MapUtils.isNotEmpty(viewAuthMap)) {
                        for (Map.Entry<String, ViewAuth> entry : viewAuthMap.entrySet()) {
                            ViewAuth auth = viewAuth.get(entry.getKey());
                            if (auth == null) {
                                auth = new ViewAuth();
                                viewAuth.put(entry.getKey(), auth);
                            }
                            auth.setHasUpdate(auth.isHasUpdate() || entry.getValue().isHasUpdate());
                            auth.setHasSelect(auth.isHasSelect() || entry.getValue().isHasSelect());
                            auth.setHasDelete(auth.isHasDelete() || entry.getValue().isHasDelete());
                        }
                    }
                }
            }
        }
        return viewAuth;
    }

    public Set<String> mergeOptAuth(List<PerAppPermissionGroup> permissionGroups) {
        Set<String> optAuth = new HashSet<>();

        if (CollectionUtils.isNotEmpty(permissionGroups)) {
            for (PerAppPermissionGroup group : permissionGroups) {
                if (group == null) { continue; }
                if (StringUtils.isNotBlank(group.getOptAuth())) {
                    List<String> opts = JsonUtils.fromJsonArray(group.getOptAuth(), String.class);
                    if (CollectionUtils.isNotEmpty(opts)) {
                        optAuth.addAll(opts);
                    }
                }
            }
        }
        return optAuth;
    }

    public Set<String> mergeTableAuth(List<PerAppPermissionGroup> permissionGroups) {
        Set<String> tableAuth = null;
        Boolean isTableAuthEmpty = false;

        if (CollectionUtils.isNotEmpty(permissionGroups)) {
            for (PerAppPermissionGroup group : permissionGroups) {
                if (group == null) { continue; }
                if (StringUtils.isNotBlank(group.getTableAuth())) {
                    List<String> tableIds = JsonUtils.fromJsonArray(group.getTableAuth(), String.class);

                    if (CollectionUtils.isEmpty(tableIds)) {
                        isTableAuthEmpty = true;
                        break;
                    }

                    if (tableAuth == null) {
                        tableAuth = new HashSet<>();
                        tableAuth.addAll(tableIds);
                    } else {
                        Set newTableAuth = new HashSet<>();
                        for (String tableId : tableIds) {
                            if (tableAuth.contains(tableId)) {
                                newTableAuth.add(tableId);
                            }
                        }
                        tableAuth = newTableAuth;
                    }
                }
            }
        }

        return (isTableAuthEmpty || tableAuth == null) ? new HashSet<>() : tableAuth;
    }

    public Map<String, Map<String, Integer>> parseFieldAuth(String columnAuth) {
        String ca = StringUtils.isBlank(columnAuth) ? "{}" : columnAuth;

        Map<String, Object> fieldAuthObjectMap = JsonUtils.fromJson(ca, new TypeReference<HashMap<String, Object>>() {});
        Map<String, Map<String, Integer>> fieldAuthMap = JsonUtils.fromJson(ca, new TypeReference<HashMap<String, Map<String, Integer>>>() {});;

        if (fieldAuthMap == null) {
            fieldAuthMap = new HashMap<>();
            if (fieldAuthObjectMap != null) {
                for (Map.Entry<String, Object> et: fieldAuthObjectMap.entrySet()) {
                    String tableId = et.getKey();
                    Object faObject = et.getValue();
                    if (faObject instanceof Map) {
                        fieldAuthMap.put(tableId, (Map<String, Integer>) faObject);
                    } else {
                        fieldAuthMap.put(tableId, new HashMap<>());
                    }
                }
            }
        }
        return fieldAuthMap;
    }

    public Map<String, Map<String, Integer>> mergeFieldAuth(List<PerAppPermissionGroup> permissionGroups) {
        Map<String, Map<String, Integer>> fieldAuth = new HashMap<>();
        Boolean isFieldAuthEmpty = false;

        if (CollectionUtils.isNotEmpty(permissionGroups)) {
            for (int i = 0; i < permissionGroups.size(); i++) {
                PerAppPermissionGroup group = permissionGroups.get(i);
                if (group == null) { continue; }
                Map<String, Map<String, Integer>> fieldAuthMap = parseFieldAuth(group.getColumnAuth());

                // 只要有一个角色的fieldAuth为空，那么就可以直接返回空
                if (MapUtils.isEmpty(fieldAuthMap)) {
                    isFieldAuthEmpty = true;
                    break;
                }

                int finalI = i;
                fieldAuthMap.entrySet().forEach(et -> {
                    String tableId = et.getKey();
                    Map<String, Integer> fa;
                    if (fieldAuth.containsKey(tableId)) {
                        fa = fieldAuth.get(tableId);
                    } else if (finalI == 0){
                        fa = new HashMap<>();
                    } else {
                        return;
                    }

                    if (MapUtils.isEmpty(et.getValue())) {
                        fa.put("hasRead", 1);
                        fa.put("hasWrite", 1);
                    } else {
                        et.getValue().entrySet().forEach(entry -> {
                            String key = entry.getKey();
                            int code = entry.getValue();
                            if (Objects.equals(key, "hasRead") || Objects.equals(key, "hasWrite")) {
                                if (fa.containsKey(key)) {
                                    if (code == 1) {
                                        fa.put(key, code);
                                    }
                                } else {
                                    fa.put(key, code);
                                }
                            } else {
                                if (fa.containsKey(key)) {
                                    FormFieldAuthCode oldAuthCode = FormFieldAuthCode.forValue(fa.get(key));
                                    FormFieldAuthCode thisAuthCode = FormFieldAuthCode.forValue(code);
                                    if (Objects.isNull(oldAuthCode) || Objects.isNull(thisAuthCode)) {
                                        if (!Objects.isNull(oldAuthCode)) {
                                            if (Objects.isNull(thisAuthCode)) {
                                                code = fa.get(key);
                                            }
                                        }
                                    } else {
                                        code = oldAuthCode.isRead() || thisAuthCode.isRead() ? FormFieldAuthCode.READ.getCode() : 0;
                                        code += oldAuthCode.isWrite() || thisAuthCode.isWrite() ? FormFieldAuthCode.WRITE.getCode() : 0;
                                        // todo 对于有些字段不是脱敏的，要额外处理
                                        // 脱敏比较特殊，反向获取
                                        code += !oldAuthCode.isMasking() || !thisAuthCode.isMasking() ? 0 : FormFieldAuthCode.MASKING.getCode();
                                    }
                                }
                                fa.put(key, code);
                            }
                        });
                    }
                    fieldAuth.put(tableId, fa);
                });
            }
        }

        return isFieldAuthEmpty ? new HashMap<>() : fieldAuth;
    }
}
