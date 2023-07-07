package com.polaris.lesscode.permission.utils;


import com.polaris.lesscode.form.internal.enums.FieldTypeEnums;
import com.polaris.lesscode.permission.internal.enums.FieldFilterMethod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表单过滤 条件工具类
 * FormFilterCondUtils
 *
 * @author roamer FormFieldTypeMethods.java
 * @version v1.0
 * @date 2020-09-01 14:56
 */
public final class FormFilterCondUtils {
    private static final Map<FieldTypeEnums, FieldFilterMethod[]> MAP = new HashMap<>();

    static {
        // 单行文本：等于、不等于、等于任意一个、不等于任意一个、包含、不包含、为空、不为空
        MAP.put(FieldTypeEnums.SINGLE_TEXT, new FieldFilterMethod[]{
                FieldFilterMethod.EQ, FieldFilterMethod.NE, FieldFilterMethod.IN, FieldFilterMethod.NIN,
                FieldFilterMethod.LIKE, FieldFilterMethod.UNLIKE, FieldFilterMethod.EMPTY, FieldFilterMethod.NOT_EMPTY,
        });
        // 多行文本：包含、不包含、为空、不为空
        MAP.put(FieldTypeEnums.MUL_TEXT, new FieldFilterMethod[]{
                FieldFilterMethod.LIKE, FieldFilterMethod.UNLIKE, FieldFilterMethod.EMPTY, FieldFilterMethod.NOT_EMPTY,
        });
        // 日期：等于、不等于、大于等于、小于等于、选择范围、动态筛选、为空、不为空
        MAP.put(FieldTypeEnums.DATE, new FieldFilterMethod[]{
                FieldFilterMethod.EQ, FieldFilterMethod.NE, FieldFilterMethod.GE, FieldFilterMethod.LE,
                FieldFilterMethod.RANGE, FieldFilterMethod.FORMULA, FieldFilterMethod.EMPTY,
                FieldFilterMethod.NOT_EMPTY,
        });
        // 邮箱：包含、为空、不为空
        MAP.put(FieldTypeEnums.EMAIL,
                new FieldFilterMethod[]{FieldFilterMethod.LIKE, FieldFilterMethod.EMPTY, FieldFilterMethod.NOT_EMPTY,});
        // 手机：包含、为空、不为空
        MAP.put(FieldTypeEnums.PHONE,
                new FieldFilterMethod[]{FieldFilterMethod.LIKE, FieldFilterMethod.EMPTY, FieldFilterMethod.NOT_EMPTY,});
        // 数字：等于、不等于、大于、大于等于、小于、小于等于、选择范围、为空、不为空
        MAP.put(FieldTypeEnums.NUMBER, new FieldFilterMethod[]{
                FieldFilterMethod.EQ, FieldFilterMethod.NE, FieldFilterMethod.GT, FieldFilterMethod.GE,
                FieldFilterMethod.LT, FieldFilterMethod.LE, FieldFilterMethod.RANGE, FieldFilterMethod.EMPTY,
                FieldFilterMethod.NOT_EMPTY,
        });
//        // 单选按钮组：等于、不等于、等于任意一个、不等于任意一个、包含、不包含、为空、不为空
//        MAP.put(FieldTypeEnums.SINGLE_CHOICE, new FieldFilterMethod[]{
//                FieldFilterMethod.EQ, FieldFilterMethod.NE, FieldFilterMethod.IN, FieldFilterMethod.NIN,
//                FieldFilterMethod.LIKE, FieldFilterMethod.UNLIKE, FieldFilterMethod.EMPTY, FieldFilterMethod.NOT_EMPTY,
//        });
//        // 复选框组：等于任意一个、包含全部、等于、为空、不为空
//        MAP.put(FieldTypeEnums.MUL_CHOICE, new FieldFilterMethod[]{
//                FieldFilterMethod.IN, FieldFilterMethod.ALL, FieldFilterMethod.EQ, FieldFilterMethod.EMPTY,
//                FieldFilterMethod.NOT_EMPTY,
//        });
//        // 地址：属于、不属于、为空、不为空
//        MAP.put(FieldTypeEnums.ADDRESS, new FieldFilterMethod[]{
//                FieldFilterMethod.ALL, FieldFilterMethod.NIN, FieldFilterMethod.EMPTY, FieldFilterMethod.NOT_EMPTY,
//        });
        // 成员单选：等于、不等于、等于任意一个、不等于任意一个、为空、不为空
        MAP.put(FieldTypeEnums.USER, new FieldFilterMethod[]{
                FieldFilterMethod.EQ, FieldFilterMethod.NE, FieldFilterMethod.IN, FieldFilterMethod.NIN,
                FieldFilterMethod.EMPTY, FieldFilterMethod.NOT_EMPTY,
        });
        // 部门单选：等于、不等于、等于任意一个、不等于任意一个、为空、不为空
        MAP.put(FieldTypeEnums.DEPT, new FieldFilterMethod[]{
                FieldFilterMethod.EQ, FieldFilterMethod.NE, FieldFilterMethod.IN, FieldFilterMethod.NIN,
                FieldFilterMethod.EMPTY, FieldFilterMethod.NOT_EMPTY,
        });
//        // 成员多选：等于、不等于、等于任意一个、不等于任意一个、为空、不为空
//        MAP.put(FieldTypeEnums.USER_GROUP, new FieldFilterMethod[]{
//                FieldFilterMethod.IN, FieldFilterMethod.ALL, FieldFilterMethod.EQ, FieldFilterMethod.EMPTY,
//                FieldFilterMethod.NOT_EMPTY,
//        });
//        // 部门多选：等于、不等于、等于任意一个、不等于任意一个、为空、不为空
//        MAP.put(FieldTypeEnums.DEPT_GROUP, new FieldFilterMethod[]{
//                FieldFilterMethod.IN, FieldFilterMethod.ALL, FieldFilterMethod.EQ, FieldFilterMethod.EMPTY,
//                FieldFilterMethod.NOT_EMPTY,
//        });

    }

    public static List<FieldFilterMethod> getFieldFilterMethodsByFieldType(FieldTypeEnums fieldType) {
        return Arrays.asList(MAP.getOrDefault(fieldType, new FieldFilterMethod[]{}).clone());
    }

}
