package com.polaris.lesscode.permission.internal.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.polaris.lesscode.enums.DescEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用权限组  字段权限-系数
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 14:56
 */
public enum FormFieldAuthCode implements DescEnum<Integer> {

    /** 可见 **/
    READ(1, "可见", true, false, false),
    /** 可编辑 **/
    WRITE(2, "可编辑", false, true, false),
    /** 脱敏 **/
    MASKING(4, "脱敏", false, false, true),


    /** 可见,可编辑 **/
    READ_AND_WRITE(3, "可见,可编辑", true, true, false),
    /** 可见,脱敏 **/
    READ_AND_MASKING(5, "可见,脱敏", true, false, true),
    /** 可编辑,脱敏 **/
    WRITE_AND_MASKING(6, "可编辑,脱敏", false, true, true),
    /** 可见,可编辑,脱敏 **/
    READ_AND_WRITE_MASKING(7, "可见,可编辑,脱敏", true, true, true),
    ;

    private final Integer code;
    private final String desc;
    private final boolean isRead;
    private final boolean isWrite;
    private final boolean isMasking;

    FormFieldAuthCode(Integer code, String desc, boolean isRead, boolean isWrite, boolean isMasking) {
        this.code = code;
        this.desc = desc;
        this.isRead = isRead;
        this.isWrite = isWrite;
        this.isMasking = isMasking;
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

    public boolean isRead() {
        return isRead;
    }

    public boolean isWrite() {
        return isWrite;
    }

    public boolean isMasking() {
        return isMasking;
    }


    private static final Map<Integer, FormFieldAuthCode> MAP = new HashMap<>();

    static {
        Arrays.stream(FormFieldAuthCode.values()).forEach(e -> {
            MAP.put(e.code, e);
        });
    }

    @JsonCreator
    public static FormFieldAuthCode forValue(Integer code) {
        return MAP.get(code);
    }

}
