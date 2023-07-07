USE `lesscode-prod`;
/* Table: lc_per_app_permission                                          */
CREATE TABLE IF NOT EXISTS `lc_per_app_pkg_permission`
(
    `id`             BIGINT   NOT NULL COMMENT '主键',
    `org_id`         BIGINT   NOT NULL DEFAULT 0 COMMENT '组织ID',
    `app_package_id` BIGINT   NOT NULL DEFAULT 0 COMMENT '应用包ID',
    `scope`          INT      NOT NULL DEFAULT 0 COMMENT '可见范围',
    `user_ids`       JSON COMMENT '用户ID列表',
    `dept_ids`       JSON COMMENT '部门ID列表',
    `role_ids`       JSON COMMENT '角色ID列表',
    `creator`        BIGINT   NOT NULL DEFAULT 0,
    `create_time`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updator`        BIGINT   NOT NULL DEFAULT 0,
    `update_time`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `version`        INT      NOT NULL DEFAULT 1,
    `del_flag`       TINYINT  NOT NULL DEFAULT 2,
    PRIMARY KEY (`id`)
) comment '应用包权限';
alter table lc_per_app_pkg_permission
    add parent_pkg_id bigint default 0 not null comment '父应用包ID' after app_package_id;

/* Index: index_app_pkg_permission_org_id                            */
CREATE INDEX `index_app_pkg_permission_org_id` ON `lc_per_app_pkg_permission` (`org_id`);
/* Index: index_app_pkg_permission_app_package_id                          */
CREATE INDEX `index_app_pkg_permission_app_package_id` ON `lc_per_app_pkg_permission` (`app_package_id`);
/* Index: index_app_pkg_permission_parent_pkg_id                          */
CREATE INDEX `index_app_pkg_permission_parent_pkg_id` ON `lc_per_app_pkg_permission` (`parent_pkg_id`);
/* Index: index_app_pkg_permission_create_time                          */
CREATE INDEX `index_app_pkg_permission_create_time` ON `lc_per_app_pkg_permission` (`create_time`);
/* Index: index_app_pkg_permission_user_ids                     */
CREATE INDEX `index_app_pkg_permission_user_ids` ON `lc_per_app_pkg_permission` ((CAST(`user_ids` -> '$' AS UNSIGNED ARRAY)));
/* Index: index_app_pkg_permission_dept_ids                        */
CREATE INDEX `index_app_pkg_permission_dept_ids` ON `lc_per_app_pkg_permission` ((CAST(`dept_ids` -> '$' AS UNSIGNED ARRAY)));
/* Index: index_app_pkg_permission_role_ids                          */
CREATE INDEX `index_app_pkg_permission_role_ids` ON `lc_per_app_pkg_permission` ((CAST(`role_ids` -> '$' AS UNSIGNED ARRAY)));


/* Table: lc_per_form_permission_group */
CREATE TABLE IF NOT EXISTS `lc_per_app_permission_group`
(
    `id`             BIGINT       NOT NULL COMMENT '主键',
    `org_id`         BIGINT       NOT NULL DEFAULT 0 COMMENT '组织ID',
    `app_package_id` BIGINT       NOT NULL DEFAULT 0 COMMENT '应用包ID',
    `app_id`         BIGINT       NOT NULL DEFAULT 0 COMMENT '应用ID',
    `lang_code`      TINYINT      NOT NULL COMMENT 'langCode',
    `name`           VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '名称',
    `remake`         VARCHAR(255) NOT NULL DEFAULT '' COMMENT '描述',
    `opt_auth`       JSON COMMENT '操作权限',
    `field_auth`     JSON COMMENT '字段权限',
    `data_auth`      JSON COMMENT '数据权限',
    `creator`        BIGINT       NOT NULL DEFAULT 0,
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updator`        BIGINT       NOT NULL DEFAULT 0,
    `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `version`        INT          NOT NULL DEFAULT 1,
    `del_flag`       TINYINT      NOT NULL DEFAULT 2,
    PRIMARY KEY (`id`)
) comment '应用权限组';
alter table lc_per_app_permission_group
    add crm_data_auth json null comment 'CRM数据权限' after data_auth;
/* Index: index_app_permission_group_org_id                       */
CREATE INDEX `index_app_permission_group_org_id` ON `lc_per_app_permission_group` (`org_id`);
/* Index: index_app_permission_group_app_package_id                  */
CREATE INDEX `index_app_permission_group_app_package_id` ON `lc_per_app_permission_group` (`app_package_id`);
/* Index: index_app_permission_group_app_id                  */
CREATE INDEX `index_app_permission_group_app_id` ON `lc_per_app_permission_group` (`app_id`);
/* Index: index_app_permission_group_create_time                  */
CREATE INDEX `index_app_permission_group_create_time` ON `lc_per_app_permission_group` (`create_time`);


/* Table: lc_per_form_permission_member */
CREATE TABLE IF NOT EXISTS `lc_per_app_permission_group_member`
(
    `id`                  BIGINT   NOT NULL COMMENT '主键',
    `org_id`              BIGINT   NOT NULL DEFAULT 0 COMMENT '组织ID',
    `app_package_id`      BIGINT   NOT NULL DEFAULT 0 COMMENT '应用包ID',
    `app_id`              BIGINT   NOT NULL DEFAULT 0 COMMENT '应用ID',
    `permission_group_id` BIGINT   NOT NULL DEFAULT 0 COMMENT '应用权限组ID',
    `user_ids`            JSON COMMENT '用户ID列表',
    `dept_ids`            JSON COMMENT '部门ID列表',
    `role_ids`            JSON COMMENT '角色ID列表',
    `creator`             BIGINT   NOT NULL DEFAULT 0,
    `create_time`         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updator`             BIGINT   NOT NULL DEFAULT 0,
    `update_time`         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `version`             INT      NOT NULL DEFAULT 1,
    `del_flag`            TINYINT  NOT NULL DEFAULT 2,
    PRIMARY KEY (`id`)
) comment '应用权限组与成员关系';
/* Index: index_app_permission_group_member_org_id                       */
CREATE INDEX `index_app_permission_group_member_org_id` ON `lc_per_app_permission_group_member` (`org_id`);
/* Index: index_app_permission_group_member_app_package_id                  */
CREATE INDEX `index_app_permission_group_member_app_package_id` ON `lc_per_app_permission_group_member` (`app_package_id`);
/* Index: index_app_permission_group_member_app_id                  */
CREATE INDEX `index_app_permission_group_member_app_id` ON `lc_per_app_permission_group_member` (`app_id`);
/* Index: index_app_permission_group_member_permission_id                  */
CREATE INDEX `index_app_permission_group_member_permission_id` ON `lc_per_app_permission_group_member` (`permission_group_id`);
/* Index: index_app_permission_group_member_user_ids                     */
CREATE INDEX `index_app_permission_group_member_user_ids` ON `lc_per_app_permission_group_member` ((CAST(`user_ids` -> '$' AS UNSIGNED ARRAY)));
/* Index: index_app_permission_group_member_dept_ids                        */
CREATE INDEX `index_app_permission_group_member_dept_ids` ON `lc_per_app_permission_group_member` ((CAST(`dept_ids` -> '$' AS UNSIGNED ARRAY)));
/* Index: index_app_permission_group_member_role_ids                          */
CREATE INDEX `index_app_permission_group_member_role_ids` ON `lc_per_app_permission_group_member` ((CAST(`role_ids` -> '$' AS UNSIGNED ARRAY)));


/* Table: lc_per_app_permission_config                                          */
CREATE TABLE IF NOT EXISTS `lc_per_app_permission_config`
(
    `id`                 BIGINT   NOT NULL COMMENT '主键',
    `org_id`             BIGINT   NOT NULL DEFAULT 0 COMMENT '组织ID',
    `app_package_id`     BIGINT   NOT NULL DEFAULT 0 COMMENT '应用包ID',
    `app_id`             BIGINT   NOT NULL DEFAULT 0 COMMENT '应用ID',
    `creatable`          BIT      NOT NULL DEFAULT 0 COMMENT '是否可新建权限组',
    `opt_auth_options`   JSON COMMENT '操作权限选项列表',
    `field_auth_options` JSON COMMENT '字段权限选项列表',
    `creator`            BIGINT   NOT NULL DEFAULT 0,
    `create_time`        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updator`            BIGINT   NOT NULL DEFAULT 0,
    `update_time`        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `version`            INT      NOT NULL DEFAULT 1,
    `del_flag`           TINYINT  NOT NULL DEFAULT 2,
    PRIMARY KEY (`id`)
) COMMENT '应用权限配置';

alter table lc_per_app_permission_config
    add is_ext bit default 0 not null comment '是否是外部表单' after creatable;
alter table lc_per_app_permission_config
    add crm_data_auth_options json null comment 'CRM数据域权限选项列表' after field_auth_options;
alter table lc_per_app_permission_config
    add component_type varchar(255) not null default '' not null comment '组件类型' after is_ext;
alter table lc_per_app_permission_config
    add field_config json null comment '字段配置（和lc_app_form同步）' after crm_data_auth_options;

/* Index: index_app_permission_config_org_id                       */
CREATE
    INDEX `index_app_permission_config_org_id` ON `lc_per_app_permission_config` (`org_id`);
/* Index: index_app_permission_config_app_package_id                  */
CREATE
    INDEX `index_app_permission_config_app_package_id` ON `lc_per_app_permission_config` (`app_package_id`);
/* Index: index_app_permission_config_app_id                  */
CREATE
    INDEX `index_app_permission_config_app_id` ON `lc_per_app_permission_config` (`app_id`);
/* Index: index_app_permission_config_create_time                  */
CREATE
    INDEX `index_app_permission_config_create_time` ON `lc_per_app_permission_config` (`create_time`);

-- 添加权限组字段只读
alter table lc_per_app_permission_group
    add `read_only` int default 2 not null comment '是否只读 1:是 2:否' after remake;

-- 增加应用权限基础配置 的appType字段
ALTER TABLE lc_per_app_permission_config
    ADD `app_type` tinyint NOT NULL DEFAULT '1' COMMENT '应用类型，1：form，2：dashboard，3：文件夹，4：project' AFTER `app_id`;

-- 应用权限langCode改成字符串
ALTER TABLE lc_per_app_permission_group
    MODIFY `lang_code` varchar(64) NOT NULL DEFAULT '' comment '默认权限组类型';

-- 修改权限基础配置 数据域权限选项 字段key
alter table lc_per_app_permission_config
    change crm_data_auth_options data_auth_options json null comment '数据域权限选项列表(定制化)';

-- 废弃 crm_data_auth 字段
update lc_per_app_permission_group
set data_auth = crm_data_auth
where id = id;