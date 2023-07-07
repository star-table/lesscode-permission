package com.polaris.lesscode.permission.model.resp;

import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.permission.entity.PerAppPermissionGroup;
import com.polaris.lesscode.permission.internal.model.bo.ViewAuth;
import com.polaris.lesscode.util.JsonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用权限组 详情查询响应模型
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 11:35
 */
@ApiModel("应用权限组 详情查询响应模型")
@Data
public class AppPerGroupInfoResp implements Serializable {

    private static final long serialVersionUID = 4920942826303857050L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "langCode")
    private String langCode;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String remake;

    @ApiModelProperty("操作权限")
    private List<String> optAuth;

    @ApiModelProperty("表格权限")
    private List<String> tableAuth;

    @ApiModelProperty("字段权限")
    private Map<String, Map<String, Integer>> fieldAuth;

    @ApiModelProperty("视图权限，返回有权限的视图id列表")
    private Map<String, ViewAuth> viewAuth;

    @ApiModelProperty("数据权限")
    private Condition dataAuth;


}
