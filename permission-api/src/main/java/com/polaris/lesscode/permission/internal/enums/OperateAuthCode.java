package com.polaris.lesscode.permission.internal.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.polaris.lesscode.enums.DescEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用权限组 操作权限-操作类型
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 14:56
 */
public enum OperateAuthCode implements DescEnum<String> {
    /**
     * 查看
     **/
    HAS_READ("hasRead", "查看"),
    /**
     * 新增
     **/
    HAS_CREATE("hasCreate", "新增"),
    /**
     * 复制
     **/
    HAS_COPY("hasCopy", "复制"),
    /**
     * 编辑
     **/
    HAS_UPDATE("hasUpdate", "编辑"),
    /**
     * 删除
     **/
    HAS_DELETE("hasDelete", "删除"),
    /**
     * 导入
     **/
    HAS_IMPORT("hasImport", "导入"),
    /**
     * 导出
     **/
    HAS_EXPORT("hasExport", "导出"),
    /**
     * 批量编辑
     **/
    @Deprecated
    HAS_BATCH_UPDATE("hasBatchPrint", "批量编辑"),
    /**
     * 批量导出
     **/
    @Deprecated
    HAS_BATCH_PRINT("hasBatchPrint", "批量导出"),
    /**
     * 邀请
     **/
    HAS_INVITE("hasInvite", "邀请"),
    /**
     * 分享
     **/
    HAS_SHARE("hasShare", "分享"),
    /**
     * 编辑成员
     **/
    HAS_EDIT_MEMBER("hasEditMember", "编辑成员"),
    /**
     * 创建视图
     */
    HAS_CREATE_VIEW("hasCreateView", "创建视图"),

    // 项目权限项
    PERMISSION_ORG_PROJECTOBJECTTYPE_MODIFY("Permission.Org.ProjectObjectType-Modify", "Permission.Org.ProjectObjectType-Modify"),
    PERMISSION_ORG_PROJECTOBJECTTYPE_CREATE("Permission.Org.ProjectObjectType-Create", "Permission.Org.ProjectObjectType-Create"),
    PERMISSION_ORG_PROJECTOBJECTTYPE_DELETE("Permission.Org.ProjectObjectType-Delete", "Permission.Org.ProjectObjectType-Delete"),
    PERMISSION_ORG_CONFIG_MODIFYFIELD("Permission.Org.Config-ModifyField", "Permission.Org.Config-ModifyField"),
    PERMISSION_PRO_CONFIG_DELETE("Permission.Pro.Config-Delete", "Permission.Pro.Config-Delete"),
    PERMISSION_PRO_CONFIG_MODIFY_BIND_UNBIND("Permission.Pro.Config-Modify,Bind,Unbind", "Permission.Pro.Config-Modify,Bind,Unbind"),
    PERMISSION_PRO_CONFIG_FILING_UNFILING("Permission.Pro.Config-Filing,UnFiling", "Permission.Pro.Config-Filing,UnFiling"),
    PERMISSION_PRO_CONFIG_MODIFYSTATUS("Permission.Pro.Config-ModifyStatus", "Permission.Pro.Config-ModifyStatus"),
    PERMISSION_PRO_CONFIG_MODIFYFIELD("Permission.Pro.Config-ModifyField", "Permission.Pro.Config-ModifyField"),
    PERMISSION_PRO_ITERATION_MODIFY("Permission.Pro.Iteration-Modify", "Permission.Pro.Iteration-Modify"),
    PERMISSION_PRO_ITERATION_CREATE("Permission.Pro.Iteration-Create", "Permission.Pro.Iteration-Create"),
    PERMISSION_PRO_ITERATION_DELETE("Permission.Pro.Iteration-Delete", "Permission.Pro.Iteration-Delete"),
    PERMISSION_PRO_ITERATION_MODIFYSTATUS("Permission.Pro.Iteration-ModifyStatus", "Permission.Pro.Iteration-ModifyStatus"),
    PERMISSION_PRO_ITERATION_BIND_UNBIND("Permission.Pro.Iteration-Bind,Unbind", "Permission.Pro.Iteration-Bind,Unbind"),
    PERMISSION_PRO_ISSUE_4_MODIFY_BIND_UNBIND("Permission.Pro.Issue.4-Modify,Bind,Unbind", "Permission.Pro.Issue.4-Modify,Bind,Unbind"),
    PERMISSION_PRO_ISSUE_4_CREATE("Permission.Pro.Issue.4-Create", "Permission.Pro.Issue.4-Create"),
    PERMISSION_PRO_ISSUE_4_DELETE("Permission.Pro.Issue.4-Delete", "Permission.Pro.Issue.4-Delete"),
    PERMISSION_PRO_ISSUE_4_MODIFYSTATUS("Permission.Pro.Issue.4-ModifyStatus", "Permission.Pro.Issue.4-ModifyStatus"),
    PERMISSION_PRO_ISSUE_4_COMMENT("Permission.Pro.Issue.4-Comment", "Permission.Pro.Issue.4-Comment"),
    PERMISSION_PRO_ROLE_MODIFY("Permission.Pro.Role-Modify", "Permission.Pro.Role-Modify"),
    PERMISSION_PRO_ROLE_DELETE("Permission.Pro.Role-Delete", "Permission.Pro.Role-Delete"),
    PERMISSION_PRO_ROLE_CREATE("Permission.Pro.Role-Create", "Permission.Pro.Role-Create"),
    PERMISSION_PRO_ROLE_MODIFYPERMISSION("Permission.Pro.Role-ModifyPermission", "Permission.Pro.Role-ModifyPermission"),
    PERMISSION_PRO_FILE_MODIFY("Permission.Pro.File-Modify", "Permission.Pro.File-Modify"),
    PERMISSION_PRO_FILE_UPLOAD("Permission.Pro.File-Upload", "Permission.Pro.File-Upload"),
    PERMISSION_PRO_FILE_DELETE("Permission.Pro.File-Delete", "Permission.Pro.File-Delete"),
    PERMISSION_PRO_FILE_DOWNLOAD("Permission.Pro.File-Download", "Permission.Pro.File-Download"),
    PERMISSION_PRO_FILE_CREATEFOLDER("Permission.Pro.File-CreateFolder", "Permission.Pro.File-CreateFolder"),
    PERMISSION_PRO_FILE_MODIFYFOLDER("Permission.Pro.File-ModifyFolder", "Permission.Pro.File-ModifyFolder"),
    PERMISSION_PRO_FILE_DELETEFOLDER("Permission.Pro.File-DeleteFolder", "Permission.Pro.File-DeleteFolder"),
    PERMISSION_PRO_TAG_CREATE("Permission.Pro.Tag-Create", "Permission.Pro.Tag-Create"),
    PERMISSION_PRO_TAG_DELETE("Permission.Pro.Tag-Delete", "Permission.Pro.Tag-Delete"),
    PERMISSION_PRO_TAG_REMOVE("Permission.Pro.Tag-Remove", "Permission.Pro.Tag-Remove"),
    PERMISSION_PRO_TAG_MODIFY("Permission.Pro.Tag-Modify", "Permission.Pro.Tag-Modify"),
    PERMISSION_PRO_ATTACHMENT_UPLOAD("Permission.Pro.Attachment-Upload", "Permission.Pro.Attachment-Upload"),
    PERMISSION_PRO_ATTACHMENT_DOWNLOAD("Permission.Pro.Attachment-Download", "Permission.Pro.Attachment-Download"),
    PERMISSION_PRO_ATTACHMENT_DELETE("Permission.Pro.Attachment-Delete", "Permission.Pro.Attachment-Delete"),
    PERMISSION_PRO_MEMBER_MODIFY("Permission.Pro.Member-Modify", "Permission.Pro.Member-Modify"),
    PERMISSION_PRO_MEMBER_BIND("Permission.Pro.Member-Bind", "Permission.Pro.Member-Bind"),
    PERMISSION_PRO_MEMBER_UNBIND("Permission.Pro.Member-Unbind", "Permission.Pro.Member-Unbind"),
    PERMISSION_PRO_VIEW_MANAGEPRIVATE("Permission.Pro.View-ManagePrivate", "Permission.Pro.View-ManagePrivate"),
    PERMISSION_PRO_VIEW_MANAGEPUBLIC("Permission.Pro.View-ManagePublic", "Permission.Pro.View-ManagePublic"),
    PERMISSION_PRO_AUTOMATION_MANAGE("Permission.Pro.Automation-Manage", "Permission.Pro.Automation-Manage"),

    PERMISSION_PRO_TABLEMANAGER_CREATE("Permission.Pro.TableManage-Create","Permission.Pro.TableManage-Create"),
    PERMISSION_PRO_TABLEMANAGER_MODIFY("Permission.Pro.TableManage-Modify","Permission.Pro.TableManage-Modify"),
    PERMISSION_PRO_TABLEMANAGER_DELETE("Permission.Pro.TableManage-Delete","Permission.Pro.TableManage-Delete"),

    PERMISSION_PRO_DASHBOARD_MODIFY("Permission.Pro.Dash-Modify","Permission.Pro.Dash-Modify"),

    MENUPERMISSION_PRO_ITERATION("MenuPermission.Pro-Iteration","MenuPermission.Pro-Iteration"),
    MENUPERMISSION_PRO_DEMAND("MenuPermission.Pro-Demand","MenuPermission.Pro-Demand"),
    MENUPERMISSION_PRO_ISSUE("MenuPermission.Pro-Issue","MenuPermission.Pro-Issue"),
    MENUPERMISSION_PRO_BUG("MenuPermission.Pro-Bug","MenuPermission.Pro-Bug"),
    MENUPERMISSION_PRO_ITERATIONOVERVIEW("MenuPermission.Pro-IterationOverview","MenuPermission.Pro-IterationOverview"),
    MENUPERMISSION_PRO_PLAN("MenuPermission.Pro-Plan","MenuPermission.Pro-Plan"),
    MENUPERMISSION_PRO_FILE("MenuPermission.Pro-File","MenuPermission.Pro-File"),
    MENUPERMISSION_PRO_PROOVERVIEW("MenuPermission.Pro-ProOverview","MenuPermission.Pro-ProOverview"),
    MENUPERMISSION_PRO_WORKHOUR("MenuPermission.Pro-WorkHour","MenuPermission.Pro-WorkHour"),
    MENUPERMISSION_PRO_STATISTICS("MenuPermission.Pro-Statistics","MenuPermission.Pro-Statistics"),
    MENUPERMISSION_PRO_GANTT("MenuPermission.Pro-Gantt","MenuPermission.Pro-Gantt"),
    MENUPERMISSION_PRO_SETTING("MenuPermission.Pro-Setting","MenuPermission.Pro-Setting"),
    MENUPERMISSION_PRO_TRASH("MenuPermission.Pro-Trash","MenuPermission.Pro-Trash"),

    PERMISSION_PRO_ISSUE_4_IMPORT("Permission.Pro.Issue.4-Import", "Permission.Pro.Issue.4-Import"),
    PERMISSION_PRO_ISSUE_4_EXPORT("Permission.Pro.Issue.4-Export", "Permission.Pro.Issue.4-Export"),

    MENUPERMISSION_PRO_PRONAME("MenuPermission.Pro-ProName", "MenuPermission.Pro-ProName"),
    MENUPERMISSION_PRO_COLLECTION("MenuPermission.Pro-Collection", "MenuPermission.Pro-Collection"),
    MENUPERMISSION_PRO_USERROLENAME("MenuPermission.Pro-UserRoleName", "MenuPermission.Pro-UserRoleName"),
    MENUPERMISSION_PRO_PROMEMBER("MenuPermission.Pro-ProMember", "MenuPermission.Pro-ProMember"),
    MENUPERMISSION_PRO_GROUPCHAT("MenuPermission.Pro-GroupChat", "MenuPermission.Pro-GroupChat"),
    MENUPERMISSION_PRO_MOREOPERATION("MenuPermission.Pro-MoreOperation", "MenuPermission.Pro-MoreOperation"),

    ;

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }


    private final String code;

    private final String desc;

    OperateAuthCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<String, OperateAuthCode> MAP = new HashMap<>();

    static {
        Arrays.stream(OperateAuthCode.values()).forEach(e -> {
            MAP.put(e.code, e);
        });
    }

    @JsonCreator
    public static OperateAuthCode forValue(String code) {
        return MAP.get(code);
    }


}
