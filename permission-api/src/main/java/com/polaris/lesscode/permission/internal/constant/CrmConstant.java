package com.polaris.lesscode.permission.internal.constant;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-11-13 14:50
 */
public interface CrmConstant {

    String CURRENT_USER = "/current/user";
    String CURRENT_DEPT = "/current/department";
    String CURRENT_AND_CHILDS_DEPARTMENT = "/current/and/childs/department";
    String ALL_DEPARTMENT = "/all/department";

    String CRM_DATA_AUTH_OPTIONS_JSON = "[{\"code\":\"/current/department\",\"name\":\"本部门\"},{\"code\":\"/current/and/childs/department\",\"name\":\"本部门以及子部门\"},{\"code\":\"/all/department\",\"name\":\"所有部门\"},{\"code\":\"/current/user\",\"name\":\"本人\"}]";
}
