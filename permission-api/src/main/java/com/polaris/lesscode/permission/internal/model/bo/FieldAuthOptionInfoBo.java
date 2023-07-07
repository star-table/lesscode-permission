package com.polaris.lesscode.permission.internal.model.bo;

import lombok.Data;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-11-04 16:15
 */
@Data
public class FieldAuthOptionInfoBo {
    private Integer code;
    private String name;
    private Boolean required;

    public FieldAuthOptionInfoBo() {
        required = Boolean.FALSE;
    }
}
