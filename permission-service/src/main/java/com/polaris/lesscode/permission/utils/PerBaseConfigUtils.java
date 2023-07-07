package com.polaris.lesscode.permission.utils;

import com.polaris.lesscode.permission.constant.PermissionConstant;
import com.polaris.lesscode.permission.internal.model.bo.CrmDataAuthOptionInfoBo;
import com.polaris.lesscode.permission.internal.model.bo.FieldAuthOptionInfoBo;
import com.polaris.lesscode.permission.internal.model.bo.OptAuthOptionInfoBo;
import com.polaris.lesscode.util.JsonUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-12-23 11:04
 */
public final class PerBaseConfigUtils {

    public static List<OptAuthOptionInfoBo> jsonToOptAuthOptionInfoBoList(String optAuthOptionsJson) {
        return JsonUtils.fromJsonArray(optAuthOptionsJson, OptAuthOptionInfoBo.class)
                // 过滤未启用的
                .stream().filter(o -> Objects.equals(PermissionConstant.STATUS_ENABLED, o.getStatus())).collect(Collectors.toList());
    }

    public static List<FieldAuthOptionInfoBo> jsonToFieldAuthOptionInfoBoList(String fieldAuthOptionsJson) {
        return JsonUtils.fromJsonArray(fieldAuthOptionsJson, FieldAuthOptionInfoBo.class);
    }

    public static List<CrmDataAuthOptionInfoBo> jsonToCrmDataAuthOptionInfoBoList(String crmDataAuthOptionsJson) {
        return JsonUtils.fromJsonArray(crmDataAuthOptionsJson, CrmDataAuthOptionInfoBo.class);
    }
}
