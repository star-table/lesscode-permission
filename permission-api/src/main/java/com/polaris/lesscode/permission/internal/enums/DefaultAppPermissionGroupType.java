package com.polaris.lesscode.permission.internal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.polaris.lesscode.enums.DescEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 默认应用权限组类型
 *
 * @author Nico
 * @date 2020/12/29 20:01
 */
public enum DefaultAppPermissionGroupType implements DescEnum<Integer> {


    /**
     * 表单默认权限组
     **/
    FORM(1, "表单默认权限组",
            () -> {
                return Arrays.asList(AppPerDefaultGroupLangCode.READ, AppPerDefaultGroupLangCode.EDIT, AppPerDefaultGroupLangCode.FORM_ADMINISTRATOR);
            }
    ),


    /**
     * 仪表盘默认权限组
     **/
    DASHBOARD(2, "仪表盘默认权限组",
            () -> {
                return Arrays.asList(AppPerDefaultGroupLangCode.DASHBOARD_ADMINISTRATOR );
            }
    ),


    /**
     * 北极星协作默认权限组
     **/
    POLARIS_PROJECT(3, "北极星协作默认权限组",
            () -> {
                return Arrays.asList(AppPerDefaultGroupLangCode.OWNER,AppPerDefaultGroupLangCode.PROJECT_MEMBER,AppPerDefaultGroupLangCode.PROJECT_VIEWER);
            }
    ),
    ;

    private final Integer code;
    private final String desc;
    private final Supplier<List<AppPerDefaultGroupLangCode>> langCodes;

    DefaultAppPermissionGroupType(Integer code, String desc, Supplier<List<AppPerDefaultGroupLangCode>> langCodes) {
        this.code = code;
        this.desc = desc;
        this.langCodes = langCodes;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public Supplier<List<AppPerDefaultGroupLangCode>> getDefaultLangCodes() {
        return langCodes;
    }

    private static final Map<Integer, DefaultAppPermissionGroupType> MAP = new HashMap<>();

    static {
        Arrays.stream(DefaultAppPermissionGroupType.values()).forEach(e -> {
            MAP.put(e.code, e);
        });
    }

    @JsonCreator
    public static DefaultAppPermissionGroupType forValue(Integer code) {
        return MAP.get(code);
    }
}
