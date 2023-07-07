package com.polaris.lesscode.permission.open.model.resp.cmr;

//import com.polaris.lesscode.permission.internal.model.bo.CrmDataAuthOptionInfoBo;
//import com.polaris.lesscode.permission.internal.model.bo.FieldAuthOptionInfoBo;
//import com.polaris.lesscode.permission.internal.model.bo.OptAuthOptionInfoBo;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 应用权限基础配置 响应模型(CRM)
// *
// * @author roamer
// * @version v1.0
// * @date 2020-11-03 11:35
// */
//@ApiModel("应用权限基础配置 响应模型(CRM)")
//@Data
//public class CrmAppPerBaseConfigResp implements Serializable {
//
//    private static final long serialVersionUID = -2719003609516540507L;
//    @ApiModelProperty(value = "组织ID")
//    private Long orgId;
//
//    @ApiModelProperty(value = "应用ID")
//    private Long appId;
//
//    @ApiModelProperty(value = "是否可新建权限组")
//    private Boolean creatable;
//
//    @ApiModelProperty(value = "操作权限选项")
//    private List<OptAuthOptionInfoBo> optAuthOptions;
//
//    @ApiModelProperty(value = "字段权限选项")
//    private List<FieldAuthOptionInfoBo> fieldAuthOptions;
//
//    @ApiModelProperty(value = "数据权限选项")
//    private List<CrmDataAuthOptionInfoBo> dataAuthOptions;
//
//    @ApiModelProperty(value = "字段列表")
//    private List<FormFieldInfoResp> formFieldList;
//
//    public CrmAppPerBaseConfigResp() {
//        creatable = false;
//        optAuthOptions = new ArrayList<>();
//        fieldAuthOptions = new ArrayList<>();
//        dataAuthOptions = new ArrayList<>();
//        formFieldList = new ArrayList<>();
//    }
//
//}
