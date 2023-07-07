package com.polaris.lesscode.permission.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-12-11 22:12
 */
@Data
public class PerGroupFieldAuthInfoData implements Serializable {

    private static final long serialVersionUID = 2545470096641583685L;

    @ApiModelProperty("字段code")
    private String name;

    @ApiModelProperty("字段label")
    private String label;

    @ApiModelProperty("字段组件类型")
    private String formFieldType;

    @ApiModelProperty("敏感标示 1敏感字段 2非敏感字段")
    private Integer sensitiveFlag;

    @ApiModelProperty("脱敏策略")
    private String sensitiveStrategy;


    @ApiModelProperty("是否脱敏")
    public Boolean isMasking;

    @ApiModelProperty("是否可见")
    public Boolean isRead;

    @ApiModelProperty("是否可编辑")
    public Boolean isWrite;

    public PerGroupFieldAuthInfoData() {
        name = "";
        label = "";
        formFieldType = "";
        sensitiveFlag = 2;
        sensitiveStrategy = "";
        isMasking = false;
        isRead = false;
        isWrite = false;
    }
}
