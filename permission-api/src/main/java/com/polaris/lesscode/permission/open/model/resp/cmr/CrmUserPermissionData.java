package com.polaris.lesscode.permission.open.model.resp.cmr;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 成员权限信息 响应数据(crm使用)
// *
// * @author roamer
// * @version v1.0
// * @date 2020-11-10 11:29
// */
//@ApiModel("成员权限信息 响应数据(crm使用)")
//@Data
//public class CrmUserPermissionData implements Serializable {
//
//    private static final long serialVersionUID = -7681355586210688538L;
//
//    @ApiModelProperty(value = "appId")
//    private Long appId;
//
//    @ApiModelProperty(value = "组建类型")
//    private String componentType;
//
//    @ApiModelProperty(value = "是否管理应用")
//    private Boolean manageApp;
//
//    @ApiModelProperty(value = "操作权限")
//    private List<CrmPermissionOptAuth> optAuthList;
//
//    @ApiModelProperty(value = "字段权限")
//    private List<CrmPermissionFieldAuth> fieldAuthList;
//
//    @ApiModelProperty(value = "数据域权限")
//    private CrmPermissionDataAuth dataAuth;
//
//    public CrmUserPermissionData() {
//        manageApp = false;
//        optAuthList = new ArrayList<>();
//        fieldAuthList = new ArrayList<>();
//        dataAuth = new CrmPermissionDataAuth();
//    }
//
//}
