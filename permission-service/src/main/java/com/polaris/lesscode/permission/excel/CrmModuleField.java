package com.polaris.lesscode.permission.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
@ColumnWidth(20)
public class CrmModuleField {
    @ExcelProperty(value = "功能模块")
    private String moduleName;

    @ExcelProperty(value = "操作权限")
    private String op;

    @ExcelProperty(value = "字段权限")
    private String field;

    @ExcelProperty(value = "敏感字段")
    private String mask;
}
