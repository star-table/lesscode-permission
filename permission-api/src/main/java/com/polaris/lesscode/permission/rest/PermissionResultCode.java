package com.polaris.lesscode.permission.rest;

import com.polaris.lesscode.exception.BusinessException;
import com.polaris.lesscode.vo.AbstractResultCode;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-09-14 11:03
 */
public enum PermissionResultCode implements AbstractResultCode {

    /**
     * 应用权限组
     **/
    PER_APP_FIELD_AUTH_EMPTY(100000, "至少选择一个字段权限"),
    PER_APP_OPT_AUTH_EMPTY(100001, "至少选择一个操作权限"),
    PER_APP_FIELD_AUTH_CODE_INVALID(100002, "字段权限无效"),
    PER_APP_FIELD_AUTH_REQUIRED(100003, "缺少必选的字段权限"),
    PER_APP_DATA_AUTH_REF_INVALID(100004, "数据权限条件关联方式无效"),
    PER_APP_DATA_AUTH_METHOD_INVALID(100005, "数据权限条件过滤方法无效"),
    PER_APP_DATA_AUTH_DATA_TYPE_INVALID(100006, "数据权限条件字段类型无效"),
    PER_APP_NOT_EXIST(100007, "权限组不存在或已删除"),
    PER_APP_NOT_ALLOWED_MODIFY_DEFAULT_GROUP(100008, "不允许修改默认权限组"),
    PER_APP_NOT_ALLOWED_DELETE_DEFAULT_GROUP(100009, "不允许删除默认权限组"),
    PER_APP_OPT_AUTH_INVALID(100010, "操作权限项无效"),
    PER_APP_MEMBER_CODE_INVALID(100011, "成员类型无效"),
    PER_APP_CONFIG_INITIALIZED(100012, "请勿重复初始化应用权限配置"),
    PER_APP_OPT_AUTH_REQUIRED(100013, "缺少必选的操作权限"),
    PER_APP_TYPE_INVALID(100014, "应用类型无效"),
    PER_APP_BASE_CONFIG_NOT_MATCHED(100015, "未找到匹配的基础权限"),
    PER_APP_DATA_AUTH_EMPTY(100016, "至少选择一个数据权限"),
    PER_APP_DATA_AUTH_CODE_INVALID(100017, "数据权限无效"),
    PER_APP_DATA_AUTH_REQUIRED(100018, "缺少必选的数据权限"),
    PER_APP_READ_ONLY(100019, "只读权限组，不可修改"),

    APP_NOT_EXIST_OR_DELETED(100020, "应用不存在或已删除"),


    /**
     * 应用包权限
     **/
    PER_APP_PACKAGE_MEMBER_CODE_INVALID(100100, "成员类型无效"),
    PER_APP_PACKAGE_SCOPE_INVALID(100101, "可见范围无效"),
    PER_APP_PACKAGE_NOT_EXIST(100102, "应用包权限不存在或已删除"),
    PER_NO_PERMISSION(100103, "无权限操作"),

    /**
     * 管理组
     **/
    PER_APP_MANAGE_GROUP_NOT_EXIST(103100, "管理组不存在或已删除"),
    PER_APP_CANNOT_REMOVE_DEFAULT_MANAGE_GROUP_ERR(103101, "系统管理组不可删除"),
    PER_APP_DEFAULT_MANAGE_GROUP_CANT_MODIFY(103102, "系统管理组不可编辑"),
    PER_APP_DEFAULT_MANAGE_GROUP_ERR(103103, "与默认管理组名称冲突"),
    PER_APP_MANAGE_GROUP_REF_MODIFY_BUSY(103104, "管理组更新繁忙"),
    PER_APP_MANAGE_GROUP_NAME_REPEAT_ERR(103105, "管理组名称重复"),
    PER_APP_MANAGE_GROUP_NAME_LEN_ERR(103106, "角色名包含非法字符或长度超出10个字符"),
    PER_APP_SYS_MANAGE_GROUP_REPEAT_ERR(103107, "系统管理组已存在"),
    PER_APP_MANAGE_GROUP_OPTIONS_NOT_EXIST(103108, "修改项非法"),
    PER_APP_MANAGE_GROUP_MEMBER_CONFLICT(103109, "成员已存在于其他管理组"),
    PER_APP_CANNOT_DELETE_SELF(103110, "不可移除自身"),
    PER_APP_MANAGE_GROUP_MEMBER_COUNT_LIMIT_ERR(103111, "只可设置一个子管理员"),
    PER_APP_ORG_NOT_EXIST(103112, "组织不存在"),
    PER_APP_MANAGE_GROUP_USER_INVALID(103113, "管理组存在无效成员"),
    PER_APP_MANAGE_GROUP_DEPT_INVALID(103114, "管理组存在无效部门"),
    PER_APP_MANAGE_GROUP_ROLE_INVALID(103115, "管理组存在无效角色"),
    PER_APP_MANAGE_GROUP_INSERT_FAILURE(103116, "管理组保存失败"),
    PER_APP_MANAGE_GROUP_PERMISSION_CONFIG_INSERT_FAILURE(103117, "管理组配置保存失败"),
    PER_APP_SYS_MANAGE_GROUP_NOT_EXIST(103118, "系统管理组不存在"),
    PER_APP_MANAGE_GROUP_PERMISSION_CONFIG_NOT_EXIST(103119, "管理组配置不存在"),
    ;
    private final int code;

    private final String message;

    PermissionResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException error() {
        return new BusinessException(this);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
