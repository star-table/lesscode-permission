package com.polaris.lesscode.permission.open.model.req.crm;

//import com.polaris.lesscode.permission.internal.model.bo.ViewAuth;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 应用权限组 保存请求参数（CRM）
// *
// * @author roamer
// * @version v1.0
// * @date 2020-09-03 11:35
// */
//@ApiModel("应用权限组 保存请求参数（CRM）")
//@Data
//public class CrmSaveAppPermissionGroupReq implements Serializable {
//
//    private static final long serialVersionUID = -5043501038278840116L;
//
//    @ApiModelProperty("名称")
//    private String name;
//
//    @ApiModelProperty("描述")
//    private String remake;
//
//    /**
//     * 操作权限
//     *
//     * @see com.polaris.lesscode.permission.internal.enums.OperateAuthCode
//     */
//    @ApiModelProperty("操作权限")
//    private List<String> optAuth;
//
//    /**
//     * 字段权限
//     *
//     * @see com.polaris.lesscode.permission.internal.enums.FormFieldAuthCode
//     */
//    @ApiModelProperty("字段权限")
//    private Map<String, Integer> fieldAuth;
//
//    @ApiModelProperty("数据权限")
//    private List<String> dataAuth;
//
//    @ApiModelProperty("视图权限")
//    private Map<String, ViewAuth> viewAuth;
//
//    public CrmSaveAppPermissionGroupReq() {
//        optAuth = new ArrayList<>();
//        fieldAuth = new HashMap<>();
//        dataAuth = new ArrayList<>();
//    }
//}
