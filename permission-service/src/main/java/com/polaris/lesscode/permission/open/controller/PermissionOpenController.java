package com.polaris.lesscode.permission.open.controller;

//import com.polaris.lesscode.context.RequestContext;
//import com.polaris.lesscode.permission.open.api.OpenPermissionApi;
//import com.polaris.lesscode.permission.open.model.resp.cmr.CrmUserPermissionData;
//import com.polaris.lesscode.permission.open.service.PermissionOpenService;
//import com.polaris.lesscode.vo.Result;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
///**
// * @author roamer
// * @version v1.0
// * @date 2020-11-10 14:37
// */
//@RestController
//public class PermissionOpenController implements OpenPermissionApi {
//
//    private final PermissionOpenService permissionOpenService;
//
//    public PermissionOpenController(PermissionOpenService permissionOpenService) {
//        this.permissionOpenService = permissionOpenService;
//    }
//
//    @Override
//    public Result<List<CrmUserPermissionData>> getUserPermissions(Long userId) {
//        return Result.ok(permissionOpenService.getUserPermissions(RequestContext.getRequestContextInfo().getOrgId(), userId));
//    }
//
//}
