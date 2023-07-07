package com.polaris.lesscode.permission.utils;

import com.polaris.lesscode.form.internal.sula.FieldParam;
import com.polaris.lesscode.form.internal.sula.FormJson;
import com.polaris.lesscode.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * json to field utils
 *
 * @author ethanliao
 * @date 2020/12/9
 * @since 1.0.0
 */
@Slf4j
public final class JsonToFieldUtils {
//    public static List<FieldParam> fromFieldConfigJsonToFieldList(String config) {
//        if (StringUtils.isBlank(config)) {
//            return Collections.emptyList();
//        }
//        FormJson formJson = JsonUtils.fromJson(config, FormJson.class);
//        if (Objects.isNull(formJson) || Objects.isNull(formJson.getFields())) {
//            return Collections.emptyList();
//        }
//        return formJson.getFields();
//    }
//
//    public static List<String> fromFieldConfigJsonToFieldNameList(String config) {
//        return fromFieldConfigJsonToFieldList(config).stream().map(FieldParam::getName).collect(Collectors.toList());
//    }
//
//    public static Map<String, FieldParam> fromFieldConfigJsonToFieldByNameMap(String config) {
//        return fromFieldConfigJsonToFieldList(config).stream().collect(
//                Collectors.toMap(
//                        FieldParam::getName, fieldParam -> fieldParam,
//                        (o1, o2) -> {
//                            log.warn("存在重复的字段:{} \n全部字段:{}", o1, config);
//                            return o2;
//                        }
//                ));
//    }
}
