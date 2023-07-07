package com.polaris.lesscode.permission.open.model.resp.cmr;

//import com.polaris.lesscode.permission.internal.constant.CrmConstant;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.util.Collection;
//
///**
// * 大数据数据域权限 数据模型
// *
// * @author roamer
// * @version v1.0
// * @date 2020-11-10 11:42
// */
//@ApiModel("大数据数据域权限 数据模型")
//@Data
//public class CrmPermissionDataAuth implements Serializable {
//    private static final long serialVersionUID = -2710599086254081929L;
//
//    @ApiModelProperty("本人")
//    private Boolean currentUser;
//
//    @ApiModelProperty("本部门")
//    private Boolean currentDept;
//
//    @ApiModelProperty("本部门及其子部门")
//    private Boolean currentDeptAndChildren;
//
//    @ApiModelProperty("所有部门")
//    private Boolean allDept;
//
//
//    public CrmPermissionDataAuth() {
//        currentUser = false;
//        currentDept = false;
//        currentDeptAndChildren = false;
//        allDept = false;
//    }
//
//
//    public CrmPermissionDataAuth(Collection<String> dataAuthString) {
//        this();
//        currentUser = dataAuthString.contains(CrmConstant.CURRENT_USER);
//        currentDept = dataAuthString.contains(CrmConstant.CURRENT_DEPT);
//        currentDeptAndChildren = dataAuthString.contains(CrmConstant.CURRENT_AND_CHILDS_DEPARTMENT);
//        allDept = dataAuthString.contains(CrmConstant.ALL_DEPARTMENT);
//    }
//}
