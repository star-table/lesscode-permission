package com.polaris.lesscode.permission.internal.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.polaris.lesscode.enums.DescEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 字段过滤-方法类型
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 14:56
 */
public enum FieldFilterMethod implements DescEnum<String> {

    /** 等于 **/
    EQ("eq", "equal to"),

    /** 不等于 **/
    NE("ne", "not equal to"),

    /** 大于 **/
    GT("gt", "greater than"),

    /** 大于等于 **/
    GE("ge", "greater than or equal to"),

    /** 小于 **/
    LT("lt", "less than"),

    /** 小于等于 **/
    LE("le", "less than or equal to"),

    /** 等于任意一个 **/
    IN("in", "in"),

    /** 不等于任意一个 **/
    NIN("nin", "not in"),

    /** 包含 **/
    LIKE("like", "like"),

    /** 不包含 **/
    UNLIKE("unlike", "unlike"),

    /** 为空 **/
    EMPTY("empty", "empty"),

    /** 不为空 **/
    NOT_EMPTY("not_empty", "not empty"),

    /** 选择范围 **/
    RANGE("range", "range"),

    /** 动态筛选 **/
    FORMULA("formula", "formula"),

    /** 包含全部 **/
    ALL("all", "all"),

    ;

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }


    private final String code;

    private final String desc;

    FieldFilterMethod(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<String, FieldFilterMethod> MAP = new HashMap<>();

    static {
        Arrays.stream(FieldFilterMethod.values()).forEach(e -> {
            MAP.put(e.code, e);
        });
    }

    @JsonCreator
    public static FieldFilterMethod forValue(String code) {
        return MAP.get(code);
    }

}
