package com.polaris.lesscode.permission.internal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.polaris.lesscode.enums.DescEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用包 可见性
 * @author roamer
 * @version v1.0
 * @date 2020-09-02 18:33
 */
public enum AppVisibilityScope implements DescEnum<Integer> {

    /** 全部成员可见 **/
    ALL(0, "全部成员可见"),
    /** 授权范围内成员可见 **/
    ASSIGN(1, "授权范围内成员可见"),
    ;

    private final Integer code;
    private final String desc;

    AppVisibilityScope(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonValue
    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    private static final Map<Integer, AppVisibilityScope> MAP = new HashMap<>();

    static {
        Arrays.stream(AppVisibilityScope.values()).forEach(e -> {
            MAP.put(e.code, e);
        });
    }

    @JsonCreator
    public static AppVisibilityScope forValue(Integer code) {
        return MAP.get(code);
    }

}
