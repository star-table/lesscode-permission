## 2021-01-06 11:06 Changed

- 管理组，应用组操作项字段命名统一为optAuth
- lc_per_manage_group表结构变动：``ALTER TABLE lc_per_manage_group CHANGE usage_ids opt_auth json DEFAULT NULL COMMENT '操作权限';``
- 有些业务点需要二次确认，已标记``TODO``

## 2021-01-06 11:26 Changed

- 应用权限langCode改成字符串:``ALTER TABLE lc_per_app_permission_group MODIFY `lang_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '';``
- 改动涉及到vo的字段类型变动，日后需前端配合调整

## 2021-01-06 11:42 Changed

- lc_per_app_permission_config增加app_type字段用作应用类型区分:``ALTER TABLE lc_per_app_permission_config ADD `app_type` tinyint NOT NULL DEFAULT '1' COMMENT '应用类型，1：form，2：dashboard，3：文件夹，4：project' AFTER `app_id`;``
- lc_per_app_permission_config之后会作为全局操作项配置，根据app_type区分不用应用的全局配置，业务需调整