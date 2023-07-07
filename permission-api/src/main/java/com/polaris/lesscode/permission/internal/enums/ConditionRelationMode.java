package com.polaris.lesscode.permission.internal.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.polaris.lesscode.enums.DescEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用权限组 数据权限-条件关联方式
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 14:56
 */
public enum ConditionRelationMode implements DescEnum<String> {

    /** AND **/
    AND("and", "and"),

    /** OR **/
    OR("or", "or"),

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

    ConditionRelationMode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<String, ConditionRelationMode> MAP = new HashMap<>();

    static {
        Arrays.stream(ConditionRelationMode.values()).forEach(e -> {
            MAP.put(e.code, e);
        });
    }

    @JsonCreator
    public static ConditionRelationMode forValue(String code) {
        return MAP.get(code);
    }

}
