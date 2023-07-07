package com.polaris.lesscode.permission.internal.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.polaris.lesscode.enums.DescEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限 成员代码
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 14:56
 */
public enum PermissionMemberCode implements DescEnum<String> {

    /** User **/
    USER("U", "User"),

    /** Department **/
    DEPT("D", "Department"),

    /** Role **/
    ROLE("R", "Role"),

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

    PermissionMemberCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<String, PermissionMemberCode> MAP = new HashMap<>();

    static {
        Arrays.stream(PermissionMemberCode.values()).forEach(e -> {
            MAP.put(e.code, e);
        });
    }

    @JsonCreator
    public static PermissionMemberCode forValue(String code) {
        return MAP.get(code);
    }

}
