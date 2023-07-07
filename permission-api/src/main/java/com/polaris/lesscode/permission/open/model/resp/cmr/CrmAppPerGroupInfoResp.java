package com.polaris.lesscode.permission.open.model.resp.cmr;

//import com.polaris.lesscode.dc.internal.dsl.Condition;
//import com.polaris.lesscode.permission.internal.model.bo.ViewAuth;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.util.List;
//import java.util.Map;
//
///**
// * 应用权限组 详情查询响应模型
// *
// * @author roamer
// * @version v1.0
// * @date 2020-09-03 11:35
// */
//@ApiModel("应用权限组 详情查询响应模型(crm)")
//@Data
//public class CrmAppPerGroupInfoResp implements Serializable {
//
//    private static final long serialVersionUID = 4920942826303857050L;
//
//    @ApiModelProperty(value = "主键ID")
//    private Long id;
//
//    @ApiModelProperty(value = "langCode")
//    private String langCode;
//
//    @ApiModelProperty("名称")
//    private String name;
//
//    @ApiModelProperty("描述")
//    private String remake;
//
//    @ApiModelProperty("操作权限")
//    private List<String> optAuth;
//
//    @ApiModelProperty("字段权限")
//    private Map<String, Integer> fieldAuth;
//
//    @ApiModelProperty("数据权限")
//    private List<String> dataAuth;
//
//    @ApiModelProperty("无码数据权限")
//    private Condition lcDataAuth;
//
//    @ApiModelProperty("视图权限，返回有权限的视图id列表")
//    private Map<String, ViewAuth> viewAuth;
//
//}
