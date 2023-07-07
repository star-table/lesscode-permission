package com.polaris.lesscode.permission.constant;

import com.polaris.lesscode.permission.internal.model.resp.AppAuthorityResp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 常量
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-03 11:06
 */
public interface PermissionConstant {
    /**
     * 启用
     */
    Integer STATUS_ENABLED = 1;
    /**
     * 禁用
     */
    Integer STATUS_DISABLED = 2;

    /**
     * 是
     */
    Integer YES = 1;
    /**
     * 否
     */
    Integer NO = 2;

    String MEMBER_USER_TYPE = "U_";
    String MEMBER_ROLE_TYPE = "R_";
    String MEMBER_DEPT_TYPE = "D_";


    /**
     * 公共模板view权限用户
     */
    Long PUBLIC_TEMPLATE_USER_ID = 2L;
    Long PUBLIC_TEMPLATE_ORG_ID = 999L;

    String EMPTY_ARRAY_JSON = "[]";
    String EMPTY_MAP_JSON = "{}";
    String EMPTY_FIELD_AUTH_ALL_JSON = "{\"hasRead\":1,\"hasWrite\":1}";
    String EMPTY_FIELD_AUTH_ONLY_READ_JSON = "{\"hasRead\":1,\"hasWrite\":0}";

    String EventCategoryPerGroup            = "PerGroup";
    String EventTypePerGroupRefresh         = "PerGroupRefresh";
    String EventTypePerGroupDeleted         = "PerGroupDeleted";
    String EventTypePerGroupMemberChanged   = "PerGroupMemberChanged";
}
