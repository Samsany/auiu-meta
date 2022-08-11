/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : auiu_meta

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 11/08/2022 10:33:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`
(
    `id`          bigint     NOT NULL AUTO_INCREMENT,
    `sort`        int                                                           DEFAULT '0' COMMENT '字典排序',
    `dict_label`  varchar(255) COLLATE utf8mb4_general_ci                       DEFAULT '' COMMENT '字典标签',
    `dict_value`  varchar(100) COLLATE utf8mb4_general_ci                       DEFAULT '' COMMENT '字典键值',
    `dict_type`   varchar(100) COLLATE utf8mb4_general_ci                       DEFAULT '' COMMENT '字典类型',
    `css_class`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
    `list_class`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表格回显样式',
    `is_default`  tinyint(1)                                                    DEFAULT '0' COMMENT '是否默认（1-是 0-否）',
    `status`      tinyint(1)                                                    DEFAULT '0' COMMENT '状态(0-正常 1-停用)',
    `create_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `is_deleted`  tinyint(1) NOT NULL                                           DEFAULT '0' COMMENT '逻辑删除(0-存在 1-已删除)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_data` (`id`, `sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`,
                             `is_default`, `status`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                             `is_deleted`)
VALUES (1, 0, '启用', '1', 'sys_normal_disable', NULL, 'primary', 0, 1, '2022-07-14 00:04:58', '2022-07-14 00:16:25',
        NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` (`id`, `sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`,
                             `is_default`, `status`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                             `is_deleted`)
VALUES (2, 0, '禁用', '0', 'sys_normal_disable', NULL, 'danger', 0, 1, '2022-07-14 00:10:23', '2022-07-14 00:16:25', NULL,
        NULL, NULL, 0);
INSERT INTO `sys_dict_data` (`id`, `sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`,
                             `is_default`, `status`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                             `is_deleted`)
VALUES (3, 0, '显示', '0', 'sys_show_hide', NULL, 'primary', 0, 1, '2022-08-07 17:22:55', '2022-08-07 17:22:55', NULL,
        NULL, NULL, 0);
INSERT INTO `sys_dict_data` (`id`, `sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`,
                             `is_default`, `status`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                             `is_deleted`)
VALUES (4, 0, '隐藏', '1', 'sys_show_hide', NULL, 'danger', 0, 1, '2022-08-07 17:23:11', '2022-08-07 17:23:11', NULL,
        NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`
(
    `id`          bigint     NOT NULL AUTO_INCREMENT,
    `dict_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典名称',
    `dict_type`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典类型',
    `status`      tinyint(1)                                                    DEFAULT '1' COMMENT '状态(0-禁用 1-正常)',
    `create_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `is_deleted`  tinyint(1) NOT NULL                                           DEFAULT '0' COMMENT '逻辑删除(0-存在 1-已删除)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `dict_type` (`dict_type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_type` (`id`, `dict_name`, `dict_type`, `status`, `create_time`, `update_time`, `create_by`,
                             `update_by`, `remark`, `is_deleted`)
VALUES (1, '系统开关', 'sys_normal_disable', 1, '2022-07-13 23:48:01', '2022-08-07 16:28:35', 'admin', NULL, NULL, 0);
INSERT INTO `sys_dict_type` (`id`, `dict_name`, `dict_type`, `status`, `create_time`, `update_time`, `create_by`,
                             `update_by`, `remark`, `is_deleted`)
VALUES (2, '显示状态', 'sys_show_hide', 1, '2022-08-07 16:28:49', '2022-08-07 16:29:51', NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`
(
    `id`           bigint   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `type`         tinyint(1)                                                    DEFAULT '1' COMMENT '日志类型',
    `trace_id`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '跟踪ID',
    `title`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '日志标题',
    `operation`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '操作内容',
    `method`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '执行方法',
    `params`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '参数',
    `url`          varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求路径',
    `ip`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT 'ip地址',
    `exception`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '异常信息',
    `execute_time` decimal(11, 0)                                                DEFAULT NULL COMMENT '耗时',
    `location`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '地区',
    `create_by`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '创建人',
    `update_by`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '更新人',
    `create_time`  datetime NOT NULL                                             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime                                                      DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`    int                                                           DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='系统日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT,
    `parent_id`       bigint                                                                 DEFAULT '0' COMMENT '父级ID',
    `name`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路由名称',
    `title`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '菜单名称',
    `component`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '组件',
    `icon`            varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '图标',
    `path`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '路由路径',
    `query`           varchar(255) COLLATE utf8mb4_general_ci                                DEFAULT NULL COMMENT '路由参数',
    `redirect`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '重定向地址，设置为noRedirect时，面包屑不可点击',
    `type`            tinyint                                                                DEFAULT '0' COMMENT '菜单类型(0-目录 1-菜单 2-外链)',
    `status`          tinyint(1)                                                             DEFAULT '1' COMMENT '菜单状态(0-禁用 1-启用)',
    `is_always_show`  tinyint(1)                                                             DEFAULT '0' COMMENT '永久显示根菜单(0-否 1-是)',
    `is_affix`        tinyint(1)                                                             DEFAULT '0' COMMENT '固定在tags-view中(0-否 1-是)',
    `is_hidden`       tinyint(1)                                                             DEFAULT '0' COMMENT '前端隐藏(0-否 1-是)',
    `is_hide_header`  tinyint(1)                                                             DEFAULT '0' COMMENT '隐藏面包屑(0-否 1-是)',
    `is_keep_alive`   tinyint(1)                                                             DEFAULT '0' COMMENT '开启缓存(0-否 1-是)',
    `is_require_auth` tinyint(1)                                                             DEFAULT '1' COMMENT '开启验证(0-否 1-是)',
    `sort`            int                                                                    DEFAULT '0' COMMENT '排序',
    `create_time`     datetime                                                               DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime                                                               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '创建人',
    `update_by`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '更新人',
    `remark`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '备注',
    `is_deleted`      tinyint(1)                                                    NOT NULL DEFAULT '0' COMMENT '逻辑删除(0-存在 1-已删除)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `component`, `icon`, `path`, `query`, `redirect`, `type`,
                        `status`, `is_always_show`, `is_affix`, `is_hidden`, `is_hide_header`, `is_keep_alive`,
                        `is_require_auth`, `sort`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                        `is_deleted`)
VALUES (1, 0, 'systemManagement', '系统管理', NULL, 'el-icon-setting', 'system', NULL, NULL, 0, 1, 0, 0, 0, 0, 0, 1, 0,
        '2022-06-19 13:27:34', '2022-08-09 23:39:22', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `component`, `icon`, `path`, `query`, `redirect`, `type`,
                        `status`, `is_always_show`, `is_affix`, `is_hidden`, `is_hide_header`, `is_keep_alive`,
                        `is_require_auth`, `sort`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                        `is_deleted`)
VALUES (2, 1, 'userManagement', '用户管理', 'system/user/index', 'user', 'user', NULL, NULL, 1, 1, 0, 0, 0, 0, 0, 1, 0,
        '2022-06-19 13:29:34', '2022-06-29 18:14:54', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `component`, `icon`, `path`, `query`, `redirect`, `type`,
                        `status`, `is_always_show`, `is_affix`, `is_hidden`, `is_hide_header`, `is_keep_alive`,
                        `is_require_auth`, `sort`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                        `is_deleted`)
VALUES (3, 1, 'menuManagement', '菜单管理', 'system/menu/index', 'tree-table', 'menu', NULL, NULL, 1, 1, 0, 0, 0, 0, 0, 1,
        2, '2022-06-29 18:11:09', '2022-08-09 23:12:01', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `component`, `icon`, `path`, `query`, `redirect`, `type`,
                        `status`, `is_always_show`, `is_affix`, `is_hidden`, `is_hide_header`, `is_keep_alive`,
                        `is_require_auth`, `sort`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                        `is_deleted`)
VALUES (4, 1, 'dictManagement', '字典管理', 'system/dict/index', 'dict', 'dict', NULL, NULL, 1, 1, 0, 0, 0, 0, 0, 1, 9,
        '2022-07-13 23:18:05', '2022-08-09 23:27:05', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `component`, `icon`, `path`, `query`, `redirect`, `type`,
                        `status`, `is_always_show`, `is_affix`, `is_hidden`, `is_hide_header`, `is_keep_alive`,
                        `is_require_auth`, `sort`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                        `is_deleted`)
VALUES (6, 1, 'dictData', '字典数据', 'system/dict/data', NULL, 'dict-data/:id', NULL, NULL, 1, 1, 0, 0, 1, 0, 0, 1, 9,
        '2022-07-13 23:18:05', '2022-08-09 23:38:34', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `component`, `icon`, `path`, `query`, `redirect`, `type`,
                        `status`, `is_always_show`, `is_affix`, `is_hidden`, `is_hide_header`, `is_keep_alive`,
                        `is_require_auth`, `sort`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                        `is_deleted`)
VALUES (7, 1, 'department', '部门管理', 'system/department', 'tree', 'department', NULL, NULL, 1, 1, 0, 0, 0, 0, 0, 1, 4,
        '2022-08-09 22:40:50', '2022-08-09 23:12:09', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `component`, `icon`, `path`, `query`, `redirect`, `type`,
                        `status`, `is_always_show`, `is_affix`, `is_hidden`, `is_hide_header`, `is_keep_alive`,
                        `is_require_auth`, `sort`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                        `is_deleted`)
VALUES (8, 1, 'role', '角色管理', 'system/role/index', 'peoples', 'role', NULL, NULL, 1, 1, 0, 0, 0, 0, 0, 1, 1,
        '2022-08-09 23:11:20', '2022-08-09 23:11:41', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `component`, `icon`, `path`, `query`, `redirect`, `type`,
                        `status`, `is_always_show`, `is_affix`, `is_hidden`, `is_hide_header`, `is_keep_alive`,
                        `is_require_auth`, `sort`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                        `is_deleted`)
VALUES (9, 1, 'post', '岗位管理', 'system/post/index', 'post', 'post', NULL, NULL, 1, 1, 0, 0, 0, 0, 0, 1, 5,
        '2022-08-09 23:15:47', '2022-08-09 23:20:51', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `component`, `icon`, `path`, `query`, `redirect`, `type`,
                        `status`, `is_always_show`, `is_affix`, `is_hidden`, `is_hide_header`, `is_keep_alive`,
                        `is_require_auth`, `sort`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                        `is_deleted`)
VALUES (10, 0, 'monitor', '系统监控', NULL, 'monitor', 'monitor', NULL, NULL, 0, 1, 0, 0, 0, 0, 0, 1, 1,
        '2022-08-09 23:35:32', '2022-08-09 23:39:22', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `component`, `icon`, `path`, `query`, `redirect`, `type`,
                        `status`, `is_always_show`, `is_affix`, `is_hidden`, `is_hide_header`, `is_keep_alive`,
                        `is_require_auth`, `sort`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                        `is_deleted`)
VALUES (11, 0, 'systemTools', '系统工具', NULL, 'tool', 'tools', NULL, NULL, 0, 1, 0, 0, 0, 0, 0, 1, 2,
        '2022-08-09 23:36:35', '2022-08-09 23:39:22', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `component`, `icon`, `path`, `query`, `redirect`, `type`,
                        `status`, `is_always_show`, `is_affix`, `is_hidden`, `is_hide_header`, `is_keep_alive`,
                        `is_require_auth`, `sort`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                        `is_deleted`)
VALUES (12, 11, 'generatorCode', '代码生成', 'tools/gen/index', 'code', 'gen', NULL, NULL, 1, 1, 0, 0, 0, 0, 0, 1, 0,
        '2022-08-10 00:32:23', '2022-08-10 00:33:47', NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_oauth_client
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client`;
CREATE TABLE `sys_oauth_client`
(
    `id`                      bigint                                                        NOT NULL AUTO_INCREMENT,
    `client_id`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端ID',
    `client_name`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL COMMENT '客户端名称',
    `resource_ids`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '资源ID',
    `client_secret`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '客户端密匙',
    `scope`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '权限范围',
    `authorized_grant_types`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '授权类型',
    `web_server_redirect_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '重定向路径',
    `authorities`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '用户权限',
    `access_token_validity`   int                                                                    DEFAULT NULL COMMENT '令牌过期秒数',
    `refresh_token_validity`  int                                                                    DEFAULT NULL COMMENT '刷新令牌过期秒数',
    `additional_information`  varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '附加的信息',
    `auto_approve`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '自动授权',
    `create_time`             datetime                                                               DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`             datetime                                                               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '创建人',
    `update_by`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '更新人',
    `remark`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '备注',
    `is_deleted`              tinyint(1)                                                    NOT NULL DEFAULT '0' COMMENT '逻辑删除 【 0 - 未删除  1 - 已删除】',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_oauth_client_id` (`client_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统客户端表';

-- ----------------------------
-- Records of sys_oauth_client
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client` (`id`, `client_id`, `client_name`, `resource_ids`, `client_secret`, `scope`,
                                `authorized_grant_types`, `web_server_redirect_uri`, `authorities`,
                                `access_token_validity`, `refresh_token_validity`, `additional_information`,
                                `auto_approve`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`,
                                `is_deleted`)
VALUES (1, 'meta-admin-client', '后台管理客户端', NULL, '{noop}secret', 'all',
        'authorization_code,password,refresh_token,client_credentials,sms,captcha,social',
        'http://localhost:9999/oauth2/code/web-client-auth-code', NULL, 7200, 31536000, NULL, 'true',
        '2022-04-12 15:45:26', '2022-06-01 14:05:52', 'admin', NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`
(
    `id`          bigint     NOT NULL AUTO_INCREMENT COMMENT '主键',
    `menu_id`     bigint                                                        DEFAULT NULL COMMENT '菜单ID',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '权限名称',
    `method`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '方法类型',
    `url_perm`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'URL权限标识',
    `btn_perm`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '按钮权限标识',
    `create_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `is_deleted`  tinyint(1) NOT NULL                                           DEFAULT '0' COMMENT '逻辑删除(0-存在 1-已删除)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          bigint     NOT NULL AUTO_INCREMENT,
    `role_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名称',
    `role_code`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色编码',
    `admin_count` int                                                           DEFAULT '0' COMMENT '后台用户数量',
    `status`      tinyint(1)                                                    DEFAULT '1' COMMENT '启用状态(0-禁用 1-启用)',
    `built_in`    tinyint(1) NOT NULL                                           DEFAULT '0' COMMENT '内置角色(0-否 1-是)',
    `sort`        int                                                           DEFAULT '0' COMMENT '排序',
    `create_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `is_deleted`  tinyint(1) NOT NULL                                           DEFAULT '0' COMMENT '逻辑删除(0-存在 1-已删除)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_role_name` (`role_name`) USING BTREE,
    UNIQUE KEY `uk_role_code` (`role_code`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `admin_count`, `status`, `built_in`, `sort`, `create_time`,
                        `update_time`, `create_by`, `update_by`, `remark`, `is_deleted`)
VALUES (1, '超级管理员', 'SUPER_ADMIN', 0, 1, 1, 0, '2022-05-30 19:05:42', '2022-05-30 19:06:19', NULL, NULL, '初始化角色', 0);
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `admin_count`, `status`, `built_in`, `sort`, `create_time`,
                        `update_time`, `create_by`, `update_by`, `remark`, `is_deleted`)
VALUES (2, '管理员', 'ADMIN', 0, 1, 1, 0, '2022-06-14 17:28:37', '2022-06-14 17:28:41', NULL, NULL, NULL, 0);
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `admin_count`, `status`, `built_in`, `sort`, `create_time`,
                        `update_time`, `create_by`, `update_by`, `remark`, `is_deleted`)
VALUES (3, '访客', 'GUEST', 0, 1, 1, 0, '2022-06-14 17:29:06', '2022-06-14 17:29:06', NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `id`      bigint NOT NULL AUTO_INCREMENT,
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `menu_id` bigint NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_role_id` (`role_id`) USING BTREE,
    KEY `idx_menu_id` (`role_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='角色菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`
(
    `id`      bigint NOT NULL AUTO_INCREMENT,
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `perm_id` bigint NOT NULL COMMENT '权限ID',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_role_id` (`role_id`) USING BTREE,
    KEY `idx_perm_id` (`perm_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='角色权限关联表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`                      bigint                                                        NOT NULL AUTO_INCREMENT,
    `account`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '账号',
    `dept_id`                 bigint                                                                 DEFAULT NULL COMMENT '部门ID',
    `real_name`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL COMMENT '真实姓名',
    `nickname`                varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL COMMENT '用户昵称',
    `avatar`                  varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '头像',
    `mobile`                  varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL COMMENT '手机号',
    `email`                   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '邮箱',
    `password`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
    `gender`                  tinyint                                                                DEFAULT '0' COMMENT '性别(0-女性 1-男性 2-未知)',
    `birthday`                datetime                                                               DEFAULT NULL COMMENT '出生日期',
    `status`                  tinyint(1)                                                             DEFAULT '1' COMMENT '是否启用(0-禁用 1-启用)',
    `account_non_expired`     tinyint(1)                                                    NOT NULL DEFAULT '1' COMMENT '账户是否过期(0-过期 1-未过期)',
    `account_non_locked`      tinyint(1)                                                    NOT NULL DEFAULT '1' COMMENT '账户是否锁定(0-锁定 1-未锁定)',
    `credentials_non_expired` tinyint(1)                                                    NOT NULL DEFAULT '1' COMMENT '证书(密码)是否过期(0-过期 1-未过期)',
    `built_in`                tinyint(1)                                                             DEFAULT '0' COMMENT '内置用户(0-否 1-是)',
    `login_date`              datetime                                                               DEFAULT NULL COMMENT '最后登录时间',
    `login_ip`                varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '登录ip',
    `register_ip`             varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '注册ip',
    `register_address`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '注册地址',
    `register_source`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '注册方式',
    `create_time`             datetime                                                               DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`             datetime                                                               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '创建人',
    `update_by`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '更新人',
    `remark`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '备注',
    `is_deleted`              tinyint(1)                                                    NOT NULL DEFAULT '0' COMMENT '逻辑删除(0-未删除 1-已删除)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_account` (`account`) USING BTREE,
    UNIQUE KEY `uk_email` (`email`) USING BTREE,
    UNIQUE KEY `uk_mobile` (`mobile`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `account`, `dept_id`, `real_name`, `nickname`, `avatar`, `mobile`, `email`, `password`,
                        `gender`, `birthday`, `status`, `account_non_expired`, `account_non_locked`,
                        `credentials_non_expired`, `built_in`, `login_date`, `login_ip`, `register_ip`,
                        `register_address`, `register_source`, `create_time`, `update_time`, `create_by`, `update_by`,
                        `remark`, `is_deleted`)
VALUES (1, 'admin', 1, 'admin', 'admin',
        'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg2.woyaogexing.com%2F2019%2F08%2F04%2Fb1e0603aa74e488dbbdcf69b9d37a2a7%21400x400.jpeg&refer=http%3A%2F%2Fimg2.woyaogexing.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1658910570&t=7141a4d46f165a785b374fc591c5cc84',
        '17388888888', NULL, '{bcrypt}$2a$10$vsPRonUHWSZLsszZkZZPN.QDorbzN39D0Bp9Ikp0CHYPMx6jRuXFe', 0,
        '2022-05-31 17:33:32', 1, 1, 1, 1, 1, NULL, NULL, NULL, NULL, NULL, '2022-04-12 15:23:11',
        '2022-06-27 16:30:24', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` (`id`, `account`, `dept_id`, `real_name`, `nickname`, `avatar`, `mobile`, `email`, `password`,
                        `gender`, `birthday`, `status`, `account_non_expired`, `account_non_locked`,
                        `credentials_non_expired`, `built_in`, `login_date`, `login_ip`, `register_ip`,
                        `register_address`, `register_source`, `create_time`, `update_time`, `create_by`, `update_by`,
                        `remark`, `is_deleted`)
VALUES (2, 'dries', 1, 'DREIES', 'DRIES', NULL, NULL, NULL, '{noop}123456', 0, NULL, 1, 1, 1, 1, 0, NULL, NULL, NULL,
        NULL, NULL, '2022-06-10 18:17:04', '2022-06-14 17:29:38', NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`      bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_user_id` (`user_id`) USING BTREE,
    KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`)
VALUES (1, 1, 1);
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`)
VALUES (2, 2, 2);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
