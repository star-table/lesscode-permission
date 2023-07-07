package com.polaris.lesscode.permission.internal.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.enums.DescEnum;

import java.util.*;
import java.util.function.Supplier;

/**
 * 默认应用权限组 LangCode
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 14:56
 */
public enum AppPerDefaultGroupLangCode implements DescEnum<String> {

    /**
     * 查看权限
     **/
    READ("-2", "只可查看当前文件夹或者是数据表的信息，不可邀请成员和新成员信息", "查看者",
            // 查看
            1,
            1,
            () -> Collections.singletonList(OperateAuthCode.HAS_READ.getCode()),
            () -> Arrays.asList(),
            // 无条件
            Condition::new,
            FormFieldAuthCode.READ, false),

    /**
     * 编辑权限
     **/
    EDIT("-3", "只可编辑当前文件夹或者是数据表的信息，不可邀请成员和新成员信息", "编辑者",
            // 查看、添加、复制、编辑、删除、导入、导出、分享
            1,
            1,
            () -> Arrays.asList(
                    OperateAuthCode.HAS_READ.getCode(),
                    OperateAuthCode.HAS_CREATE.getCode(),
                    OperateAuthCode.HAS_COPY.getCode(),
                    OperateAuthCode.HAS_UPDATE.getCode(),
                    OperateAuthCode.HAS_DELETE.getCode(),
                    OperateAuthCode.HAS_IMPORT.getCode(),
                    OperateAuthCode.HAS_EXPORT.getCode(),
                    OperateAuthCode.HAS_SHARE.getCode()
            ),
            () -> Arrays.asList(),
            // 无条件
            Condition::new,
            FormFieldAuthCode.READ_AND_WRITE, false),

    /**
     * 表单管理员
     **/
    FORM_ADMINISTRATOR("-4", "可直接管理当前文件夹或者是数据表所有权限信息，并可邀请成员和新成员", "管理员",
            // 查看、添加、复制、编辑、删除、导入、导出、邀请、分享、编辑成员
            1,
            1,
            () -> Arrays.asList(
                    OperateAuthCode.HAS_READ.getCode(),
                    OperateAuthCode.HAS_CREATE.getCode(),
                    OperateAuthCode.HAS_COPY.getCode(),
                    OperateAuthCode.HAS_UPDATE.getCode(),
                    OperateAuthCode.HAS_DELETE.getCode(),
                    OperateAuthCode.HAS_IMPORT.getCode(),
                    OperateAuthCode.HAS_EXPORT.getCode(),
                    OperateAuthCode.HAS_INVITE.getCode(),
                    OperateAuthCode.HAS_SHARE.getCode(),
                    OperateAuthCode.HAS_EDIT_MEMBER.getCode()
            ),
            () -> Arrays.asList(),
            // 无条件
            Condition::new,
            FormFieldAuthCode.READ_AND_WRITE, true),

    /**
     * 仪表盘管理员
     **/
    DASHBOARD_ADMINISTRATOR("-5", "可直接管理当前文件夹或者是数据表所有权限信息，并可邀请成员和新成员", "管理员",
            // 查看、添加、复制、编辑、删除、导入、导出、邀请、分享、编辑成员
            1,
            2,
            () -> Arrays.asList(
                    OperateAuthCode.HAS_READ.getCode(),
                    OperateAuthCode.HAS_CREATE.getCode(),
                    OperateAuthCode.HAS_COPY.getCode(),
                    OperateAuthCode.HAS_UPDATE.getCode(),
                    OperateAuthCode.HAS_DELETE.getCode(),
                    OperateAuthCode.HAS_INVITE.getCode(),
                    OperateAuthCode.HAS_SHARE.getCode(),
                    OperateAuthCode.HAS_EDIT_MEMBER.getCode()
            ),
            () -> Arrays.asList(),
            // 无条件
            Condition::new,
            FormFieldAuthCode.READ, true),


    /**
     * 负责人
     **/
    OWNER("41", "当前项目的管理员，具有项目的所有管理权限", "管理员", 1, 3, () -> Arrays.asList(
            OperateAuthCode.PERMISSION_ORG_PROJECTOBJECTTYPE_MODIFY.getCode(),
            OperateAuthCode.PERMISSION_ORG_PROJECTOBJECTTYPE_CREATE.getCode(),
            OperateAuthCode.PERMISSION_ORG_PROJECTOBJECTTYPE_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_CONFIG_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_CONFIG_MODIFY_BIND_UNBIND.getCode(),
            OperateAuthCode.PERMISSION_PRO_CONFIG_FILING_UNFILING.getCode(),
            OperateAuthCode.PERMISSION_PRO_CONFIG_MODIFYSTATUS.getCode(),
            OperateAuthCode.PERMISSION_PRO_CONFIG_MODIFYFIELD.getCode(),
            OperateAuthCode.PERMISSION_PRO_ITERATION_MODIFY.getCode(),
            OperateAuthCode.PERMISSION_PRO_ITERATION_CREATE.getCode(),
            OperateAuthCode.PERMISSION_PRO_ITERATION_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_ITERATION_MODIFYSTATUS.getCode(),
            OperateAuthCode.PERMISSION_PRO_ITERATION_BIND_UNBIND.getCode(),
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_MODIFY_BIND_UNBIND.getCode(),
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_CREATE.getCode(),
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_MODIFYSTATUS.getCode(),
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_COMMENT.getCode(),
            OperateAuthCode.PERMISSION_PRO_ROLE_MODIFY.getCode(),
            OperateAuthCode.PERMISSION_PRO_ROLE_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_ROLE_CREATE.getCode(),
            OperateAuthCode.PERMISSION_PRO_ROLE_MODIFYPERMISSION.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_MODIFY.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_UPLOAD.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_DOWNLOAD.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_CREATEFOLDER.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_MODIFYFOLDER.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_DELETEFOLDER.getCode(),
            OperateAuthCode.PERMISSION_PRO_TAG_CREATE.getCode(),
            OperateAuthCode.PERMISSION_PRO_TAG_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_TAG_REMOVE.getCode(),
            OperateAuthCode.PERMISSION_PRO_TAG_MODIFY.getCode(),
            OperateAuthCode.PERMISSION_PRO_ATTACHMENT_UPLOAD.getCode(),
            OperateAuthCode.PERMISSION_PRO_ATTACHMENT_DOWNLOAD.getCode(),
            OperateAuthCode.PERMISSION_PRO_ATTACHMENT_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_MEMBER_MODIFY.getCode(),
            OperateAuthCode.PERMISSION_PRO_MEMBER_BIND.getCode(),
            OperateAuthCode.PERMISSION_PRO_MEMBER_UNBIND.getCode(),
            OperateAuthCode.PERMISSION_PRO_VIEW_MANAGEPRIVATE.getCode(),
            OperateAuthCode.PERMISSION_PRO_VIEW_MANAGEPUBLIC.getCode(),
            OperateAuthCode.PERMISSION_PRO_AUTOMATION_MANAGE.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_ITERATION.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_DEMAND.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_ISSUE.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_BUG.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_ITERATIONOVERVIEW.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_PLAN.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_FILE.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_PROOVERVIEW.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_WORKHOUR.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_STATISTICS.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_GANTT.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_SETTING.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_TRASH.getCode(),
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_IMPORT.getCode(),
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_EXPORT.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_PRONAME.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_COLLECTION.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_USERROLENAME.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_PROMEMBER.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_GROUPCHAT.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_MOREOPERATION.getCode(),
            OperateAuthCode.PERMISSION_PRO_TABLEMANAGER_CREATE.getCode(),
            OperateAuthCode.PERMISSION_PRO_TABLEMANAGER_MODIFY.getCode(),
            OperateAuthCode.PERMISSION_PRO_TABLEMANAGER_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_DASHBOARD_MODIFY.getCode()
    ), () -> Arrays.asList(), Condition::new, FormFieldAuthCode.READ_AND_WRITE, true),

    /**
     * 项目成员
     **/
    PROJECT_MEMBER("42", "当前项目的编辑者，可操作任务、文件、附件", "编辑者", 1, 3, () -> Arrays.asList(
            OperateAuthCode.PERMISSION_ORG_PROJECTOBJECTTYPE_MODIFY.getCode(),
            OperateAuthCode.PERMISSION_ORG_PROJECTOBJECTTYPE_CREATE.getCode(),
            OperateAuthCode.PERMISSION_ORG_PROJECTOBJECTTYPE_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_ITERATION_MODIFY.getCode(),
            OperateAuthCode.PERMISSION_PRO_ITERATION_CREATE.getCode(),
            OperateAuthCode.PERMISSION_PRO_ITERATION_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_ITERATION_MODIFYSTATUS.getCode(),
            OperateAuthCode.PERMISSION_PRO_ITERATION_BIND_UNBIND.getCode(),
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_MODIFY_BIND_UNBIND.getCode(),
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_CREATE.getCode(),
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_MODIFYSTATUS.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_UPLOAD.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_DOWNLOAD.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_MODIFY.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_CREATEFOLDER.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_MODIFYFOLDER.getCode(),
            OperateAuthCode.PERMISSION_PRO_FILE_DELETEFOLDER.getCode(),
            OperateAuthCode.PERMISSION_PRO_TAG_CREATE.getCode(),
            OperateAuthCode.PERMISSION_PRO_TAG_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_TAG_REMOVE.getCode(),
            OperateAuthCode.PERMISSION_PRO_TAG_MODIFY.getCode(),
            OperateAuthCode.PERMISSION_PRO_ATTACHMENT_UPLOAD.getCode(),
            OperateAuthCode.PERMISSION_PRO_ATTACHMENT_DOWNLOAD.getCode(),
            OperateAuthCode.PERMISSION_PRO_ATTACHMENT_DELETE.getCode(),
            OperateAuthCode.PERMISSION_PRO_MEMBER_BIND.getCode(),
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_COMMENT.getCode(),
            OperateAuthCode.PERMISSION_PRO_VIEW_MANAGEPRIVATE.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_ITERATION.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_DEMAND.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_ISSUE.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_BUG.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_ITERATIONOVERVIEW.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_PLAN.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_FILE.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_PROOVERVIEW.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_WORKHOUR.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_STATISTICS.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_GANTT.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_SETTING.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_TRASH.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_PRONAME.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_COLLECTION.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_USERROLENAME.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_PROMEMBER.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_GROUPCHAT.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_MOREOPERATION.getCode(),
            OperateAuthCode.PERMISSION_PRO_DASHBOARD_MODIFY.getCode()
    ), () -> Arrays.asList(), Condition::new, FormFieldAuthCode.READ, false),

    PROJECT_VIEWER("43", "当前项目的查看者，只能查看和评论任务", "查看者", 1, 3, () -> Arrays.asList(
            OperateAuthCode.PERMISSION_PRO_ISSUE_4_COMMENT.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_ITERATION.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_DEMAND.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_ISSUE.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_BUG.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_ITERATIONOVERVIEW.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_PLAN.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_FILE.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_PROOVERVIEW.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_WORKHOUR.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_STATISTICS.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_GANTT.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_SETTING.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_TRASH.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_PRONAME.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_COLLECTION.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_USERROLENAME.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_PROMEMBER.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_GROUPCHAT.getCode(),
            OperateAuthCode.MENUPERMISSION_PRO_MOREOPERATION.getCode()
    ), () -> Arrays.asList(), Condition::new, FormFieldAuthCode.READ, false),

    ;


    private final String code;
    private final String desc;
    private final String name;
    private final Integer readOnly;
    private final Integer groupType;
    private final Supplier<List<String>> defaultOptAuth;
    private final Supplier<List<String>> defaultTableAuth;
    private final Supplier<Condition> defaultDataAuth;
    private final FormFieldAuthCode fieldAuthCode;
    private final Boolean isAdmin;

    AppPerDefaultGroupLangCode(String code, String desc, String name, Integer readOnly, Integer groupType, Supplier<List<String>> defaultOptAuth, Supplier<List<String>> defaultTableAuth, Supplier<Condition> defaultDataAuth, FormFieldAuthCode fieldAuthCode, Boolean isAdmin) {
        this.code = code;
        this.desc = desc;
        this.name = name;
        this.readOnly = readOnly;
        this.groupType = groupType;
        this.defaultOptAuth = defaultOptAuth;
        this.defaultTableAuth = defaultTableAuth;
        this.defaultDataAuth = defaultDataAuth;
        this.fieldAuthCode = fieldAuthCode;
        this.isAdmin = isAdmin;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public Integer getReadOnly() {
        return readOnly;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public Supplier<List<String>> getDefaultOptAuth() { return defaultOptAuth; }

    public Supplier<List<String>> getDefaultTableAuth() { return defaultTableAuth; }

    public Supplier<Condition> getDefaultDataAuth() {
        return defaultDataAuth;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public FormFieldAuthCode getFieldAuthCode() {
        return fieldAuthCode;
    }

    private static final Map<String, AppPerDefaultGroupLangCode> MAP = new HashMap<>();

    static {
        Arrays.stream(AppPerDefaultGroupLangCode.values()).forEach(e -> {
            MAP.put(e.code, e);
        });
    }

    @JsonCreator
    public static AppPerDefaultGroupLangCode forValue(String code) {
        return MAP.get(code);
    }
}
