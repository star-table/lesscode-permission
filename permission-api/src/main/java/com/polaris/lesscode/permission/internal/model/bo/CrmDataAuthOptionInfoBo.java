package com.polaris.lesscode.permission.internal.model.bo;

import lombok.Data;

import java.util.Objects;

/**
 * 数据域权限选项-外部
 *
 * @author roamer
 * @version v1.0
 * @date 2020-11-04 16:15
 */
@Data
public class CrmDataAuthOptionInfoBo {
    private String code;
    private String name;
    private Boolean required;

    public CrmDataAuthOptionInfoBo() {
        required = Boolean.FALSE;
    }

    public CrmDataAuthOptionInfoBo(String code, String name, Boolean required) {
        this.code = code;
        this.name = name;
        this.required = Objects.isNull(required) ? Boolean.FALSE : required;
    }

    public CrmDataAuthOptionInfoBo(String code, String name) {
        this(code, name, Boolean.FALSE);
    }
}
