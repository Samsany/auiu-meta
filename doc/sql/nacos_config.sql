/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : nacos_config

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 11/08/2022 10:35:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`
(
    `id`           bigint                                           NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`     varchar(255) CHARACTER SET utf8 COLLATE utf8_bin          DEFAULT NULL,
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL COMMENT 'content',
    `md5`          varchar(32) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`     text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
    `src_ip`       varchar(20) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL COMMENT 'source ip',
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin          DEFAULT NULL,
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin          DEFAULT '' COMMENT '租户字段',
    `c_desc`       varchar(256) CHARACTER SET utf8 COLLATE utf8_bin          DEFAULT NULL,
    `c_use`        varchar(64) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL,
    `effect`       varchar(64) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL,
    `type`         varchar(64) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL,
    `c_schema`     text CHARACTER SET utf8 COLLATE utf8_bin,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 275
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin COMMENT ='config_info';

-- ----------------------------
-- Records of config_info
-- ----------------------------
BEGIN;
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (11, 'datasource.yml', 'DEV_GROUP',
        'spring:\r\n  datasource:\r\n    url: jdbc:mysql://182.254.171.162:3306/jxmall_sms?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai\r\n    username: root\r\n    password: Zxx131013@\r\n    driver-class-name: com.mysql.cj.jdbc.Driver',
        '6d3bdf673ab2b93f5fa162b21f63465c', '2020-07-04 03:07:01', '2020-07-22 12:53:15', NULL, '223.166.141.109', '',
        'd1811724-f605-4522-9430-3ac08b858546', '数据库配置', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (18, 'datasource.yml', 'DEV_GROUP',
        'spring:\r\n  datasource:\r\n    url: jdbc:mysql://182.254.171.162:3306/jxmall_ums?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai\r\n    username: root\r\n    password: Zxx131013@\r\n    driver-class-name: com.mysql.cj.jdbc.Driver',
        'e87ad38684566f9b5b6e8499ca944505', '2020-07-04 03:16:16', '2020-07-22 12:54:02', NULL, '223.166.141.109', '',
        'ae2a1555-9bcc-4a03-b618-0ab966f99291', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (23, 'datasource.yml', 'DEV_GROUP',
        'spring:\r\n  datasource:\r\n    url: jdbc:mysql://182.254.171.162:3306/jxmall_pms?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai\r\n    username: root\r\n    password: Zxx131013@\r\n    driver-class-name: com.mysql.cj.jdbc.Driver',
        '08bccdc2707e6026c9c22d239cfcbae4', '2020-07-04 03:19:24', '2020-07-22 12:54:38', NULL, '223.166.141.109', '',
        '02d64543-2d8e-4510-b06f-03fd310cb368', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (26, 'datasource.yml', 'DEV_GROUP',
        'spring:\r\n  datasource:\r\n    url: jdbc:mysql://182.254.171.162:3306/jxmall_oms?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai\r\n    username: root\r\n    password: Zxx131013@\r\n    driver-class-name: com.mysql.cj.jdbc.Driver',
        '451bb7db8092fbd6f0f8ffc34a9951fb', '2020-07-04 03:19:29', '2020-07-22 12:54:24', NULL, '223.166.141.109', '',
        '1c1dc47b-9461-48dd-8275-56d18a66d9ff', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (29, 'datasource.yml', 'DEV_GROUP',
        'spring:\r\n  datasource:\r\n    url: jdbc:mysql://182.254.171.162:3306/jxmall_wms?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai\r\n    username: root\r\n    password: Zxx131013@\r\n    driver-class-name: com.mysql.cj.jdbc.Driver',
        '25e25f02ed4ac87da4b7d1acd969e0ff', '2020-07-04 03:19:34', '2020-07-22 12:54:57', NULL, '223.166.141.109', '',
        '2879f8a4-c7e3-4595-9660-52bb2afd7c93', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (40, 'mybatis-plus.yml', 'DEFAULT_GROUP',
        'spring:\r\n  jackson:\r\n    default-property-inclusion: non_null\r\n    date-format: yyyy-MM-dd HH:mm:ss\r\n    time-zone: GMT+8\r\n\r\nmybatis-plus:\r\n  mapper-locations: classpath:/mapper/**/*.xml\r\n  configuration:\r\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n  global-config:\r\n    db-config:\r\n      logic-delete-field: deleted  # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)\r\n      logic-delete-value: 1 # 逻辑已删除值(默认为 1)\r\n      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)\r\n      id-type: auto # 主键默认自增\r\n\r\nlogging:\r\n  level:\r\n    com.samphanie.jxmall: debug     ',
        '8f8939e3f14213a782f09dea18f1656f', '2020-07-04 03:46:17', '2020-10-19 10:20:47', NULL, '61.170.198.219', '',
        '02d64543-2d8e-4510-b06f-03fd310cb368', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (41, 'mybatis-plus.yml', 'DEFAULT_GROUP',
        'mybatis-plus:\r\n  mapper-locations: classpath:/mapper/**/*.xml\r\n\r\n  global-config:\r\n    db-config:\r\n      id-type: auto     ',
        '8ddca52b8d88d22f13d50ddfef4cc0fa', '2020-07-04 03:46:28', '2020-07-04 03:46:28', NULL, '192.168.200.1', '',
        '1c1dc47b-9461-48dd-8275-56d18a66d9ff', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (42, 'mybatis-plus.yml', 'DEFAULT_GROUP',
        'spring:\r\n  jackson:\r\n    default-property-inclusion: non_null\r\n    date-format: yyyy-MM-dd HH:mm:ss\r\n    time-zone: GMT+8\r\n\r\nmybatis-plus:\r\n  mapper-locations: classpath:/mapper/**/*.xml\r\n  configuration:\r\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n  global-config:\r\n    db-config:\r\n      logic-delete-field: deleted  # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)\r\n      logic-delete-value: 1 # 逻辑已删除值(默认为 1)\r\n      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)\r\n      id-type: auto # 主键默认自增\r\n\r\nlogging:\r\n  level:\r\n    com.samphanie.jxmall: debug',
        'f06b9718e72f89173e7a5e489f92f798', '2020-07-04 03:46:44', '2020-10-19 10:21:06', NULL, '61.170.198.219', '',
        '2879f8a4-c7e3-4595-9660-52bb2afd7c93', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (43, 'mybatis-plus.yml', 'DEFAULT_GROUP',
        'spring:\r\n  jackson:\r\n    default-property-inclusion: non_null\r\n    date-format: yyyy-MM-dd HH:mm:ss\r\n    time-zone: GMT+8\r\n\r\nmybatis-plus:\r\n  mapper-locations: classpath:/mapper/**/*.xml\r\n  configuration:\r\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n  global-config:\r\n    db-config:\r\n      logic-delete-field: deleted  # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)\r\n      logic-delete-value: 1 # 逻辑已删除值(默认为 1)\r\n      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)\r\n      id-type: auto # 主键默认自增\r\n\r\nlogging:\r\n  level:\r\n    com.samphanie.jxmall: debug',
        'f06b9718e72f89173e7a5e489f92f798', '2020-07-04 03:46:50', '2020-10-19 10:21:25', NULL, '61.170.198.219', '',
        'ae2a1555-9bcc-4a03-b618-0ab966f99291', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (44, 'mybatis-plus.yml', 'DEFAULT_GROUP',
        'spring:\r\n  jackson:\r\n    default-property-inclusion: non_null\r\n    date-format: yyyy-MM-dd HH:mm:ss\r\n    time-zone: GMT+8\r\n\r\nmybatis-plus:\r\n  mapper-locations: classpath:/mapper/**/*.xml\r\n  configuration:\r\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n  global-config:\r\n    db-config:\r\n      logic-delete-field: deleted  # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)\r\n      logic-delete-value: 1 # 逻辑已删除值(默认为 1)\r\n      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)\r\n      id-type: auto # 主键默认自增\r\n\r\nlogging:\r\n  level:\r\n    com.samphanie.jxmall: debug   ',
        '03075d96c0c17071abcfd50905b231d9', '2020-07-04 03:46:56', '2020-10-19 10:21:40', NULL, '61.170.198.219', '',
        'd1811724-f605-4522-9430-3ac08b858546', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (45, 'server.yml', 'DEFAULT_GROUP',
        'spring:\r\n  application:\r\n    name: jxmall-product\r\n\r\nserver:\r\n  port: 10000',
        '9e29792fcbc3810e9bb6516fce7ced98', '2020-07-04 03:49:01', '2020-07-08 12:36:36', NULL, '192.168.200.1', '',
        '02d64543-2d8e-4510-b06f-03fd310cb368', '端口配置', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (46, 'server.yml', 'DEFAULT_GROUP',
        'spring:\r\n  application:\r\n    name: jxmall-order\r\n\r\nserver:\r\n  port: 9000',
        'cca33d4bc2cca4e73ed09cc63ad05d96', '2020-07-04 03:49:38', '2020-07-08 12:36:56', NULL, '192.168.200.1', '',
        '1c1dc47b-9461-48dd-8275-56d18a66d9ff', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (47, 'server.yml', 'DEFAULT_GROUP',
        'spring:\r\n  application:\r\n    name: jxmall-ware\r\n\r\nserver:\r\n  port: 11000',
        'ec49c5c79aa8a700bf0066d51e4bb60e', '2020-07-04 03:49:42', '2020-07-08 12:36:05', NULL, '192.168.200.1', '',
        '2879f8a4-c7e3-4595-9660-52bb2afd7c93', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (49, 'server.yml', 'DEFAULT_GROUP',
        'spring:\r\n  application:\r\n    name: jxmall-member\r\n\r\nserver:\r\n  port: 8000',
        '487214148b46df94f7eeb0484a256408', '2020-07-04 03:50:00', '2020-07-08 12:37:13', NULL, '192.168.200.1', '',
        'ae2a1555-9bcc-4a03-b618-0ab966f99291', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (50, 'server.yml', 'DEFAULT_GROUP',
        'spring:\r\n  application:\r\n    name: jxmall-coupon\r\n    \r\nserver:\r\n  port: 7000',
        'fd18b2f60ac009d7aa45e978cbfd1c09', '2020-07-04 03:50:04', '2020-07-08 12:37:33', NULL, '192.168.200.1', '',
        'd1811724-f605-4522-9430-3ac08b858546', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (51, 'server.yml', 'DEFAULT_GROUP',
        'spring:\r\n  application:\r\n    name: jxmall-gateway\r\n\r\nserver:\r\n  port: 88',
        'd2e63591d54aea276d47719d690e27ca', '2020-07-04 08:32:06', '2020-07-08 12:40:16', NULL, '192.168.200.1', '',
        'd9c1eebe-b054-4ce6-9d43-a7b09395c5ce', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (56, 'mybatis-plus.yml', 'DEFAULT_GROUP',
        'mybatis-plus:\r\n  mapper-locations: classpath:/mapper/**/*.xml\r\n\r\n  global-config:\r\n    db-config:\r\n      id-type: auto     ',
        '8ddca52b8d88d22f13d50ddfef4cc0fa', '2020-07-04 09:43:20', '2020-07-04 09:43:20', NULL, '192.168.200.1', '',
        'd9c1eebe-b054-4ce6-9d43-a7b09395c5ce', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (57, 'oss.yml', 'DEFAULT_GROUP',
        '## 阿里云OSS对象存储\r\nspring:\r\n  cloud:\r\n    alicloud:\r\n      access-key: LTAI4GDMV8rH5GjMH4A6eUDZ\r\n      secret-key: 1naALFNAooKRyY9uw2Z4dGCFPhL1Dl\r\n      oss:\r\n        endpoint: oss-cn-shanghai.aliyuncs.com\r\n        bucket: dries-jxmall',
        'dd5f7489d577fc0c523ad1e6effae04b', '2020-07-24 16:37:33', '2020-07-24 17:03:46', NULL, '223.166.141.109', '',
        'e18b5c72-d68b-41de-b6e6-95b1635c5764', '对象存储配置', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (58, 'server.yml', 'DEFAULT_GROUP',
        'spring:\r\n  application:\r\n    name: jxmall-third-party\r\n\r\nserver:\r\n  port: 30000',
        'ba7bd23a2b3e1c5ad52798c113d92017', '2020-07-24 16:39:15', '2020-07-24 16:43:06', NULL, '223.166.141.109', '',
        'e18b5c72-d68b-41de-b6e6-95b1635c5764', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (63, 'server.yml', 'DEFAULT_GROUP',
        'spring:\r\n  application:\r\n    name: jxmall-search\r\n\r\nserver:\r\n  port: 12000',
        'dce61e6e7983f8733e26b041e1bd5c12', '2020-10-26 07:11:50', '2020-10-26 07:13:42', NULL, '61.170.198.219', '',
        '2802adea-a7df-45a1-89ff-2d7516d13c9b', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (66, 'mybatis-plus.yml', 'DEFAULT_GROUP',
        'spring:\r\n  jackson:\r\n    default-property-inclusion: non_null\r\n    date-format: yyyy-MM-dd HH:mm:ss\r\n    time-zone: GMT+8\r\n\r\nmybatis-plus:\r\n  mapper-locations: classpath:/mapper/**/*.xml\r\n  configuration:\r\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n  global-config:\r\n    db-config:\r\n      logic-delete-field: deleted  # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)\r\n      logic-delete-value: 1 # 逻辑已删除值(默认为 1)\r\n      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)\r\n      id-type: auto # 主键默认自增\r\n\r\nlogging:\r\n  level:\r\n    com.samphanie.jxmall: debug     ',
        '8f8939e3f14213a782f09dea18f1656f', '2020-10-26 07:14:03', '2020-10-26 07:14:03', NULL, '61.170.198.219', '',
        '2802adea-a7df-45a1-89ff-2d7516d13c9b', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (158, 'transport.type', 'SEATA_GROUP', 'TCP', 'b136ef5f6a01d816991fe3cf7a6ac763', '2021-06-29 05:00:23',
        '2021-06-29 05:00:23', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (159, 'transport.server', 'SEATA_GROUP', 'NIO', 'b6d9dfc0fb54277321cebc0fff55df2f', '2021-06-29 05:00:23',
        '2021-06-29 05:00:23', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (160, 'transport.heartbeat', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2021-06-29 05:00:24',
        '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (161, 'transport.enableClientBatchSendRequest', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (162, 'transport.threadFactory.bossThreadPrefix', 'SEATA_GROUP', 'NettyBoss', '0f8db59a3b7f2823f38a70c308361836',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (163, 'transport.threadFactory.workerThreadPrefix', 'SEATA_GROUP', 'NettyServerNIOWorker',
        'a78ec7ef5d1631754c4e72ae8a3e9205', '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '',
        '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (164, 'transport.threadFactory.serverExecutorThreadPrefix', 'SEATA_GROUP', 'NettyServerBizHandler',
        '11a36309f3d9df84fa8b59cf071fa2da', '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '',
        '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (165, 'transport.threadFactory.shareBossWorker', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (166, 'transport.threadFactory.clientSelectorThreadPrefix', 'SEATA_GROUP', 'NettyClientSelector',
        'cd7ec5a06541e75f5a7913752322c3af', '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '',
        '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (167, 'transport.threadFactory.clientSelectorThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (168, 'transport.threadFactory.clientWorkerThreadPrefix', 'SEATA_GROUP', 'NettyClientWorkerThread',
        '61cf4e69a56354cf72f46dc86414a57e', '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '',
        '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (169, 'transport.threadFactory.bossThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (170, 'transport.threadFactory.workerThreadSize', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (171, 'transport.shutdown.wait', 'SEATA_GROUP', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2021-06-29 05:00:24',
        '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (172, 'service.vgroupMapping.my_test_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (173, 'service.default.grouplist', 'SEATA_GROUP', '127.0.0.1:8091', 'c32ce0d3e264525dcdada751f98143a3',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (174, 'service.enableDegrade', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-06-29 05:00:24',
        '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (175, 'service.disableGlobalTransaction', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (176, 'client.rm.asyncCommitBufferLimit', 'SEATA_GROUP', '10000', 'b7a782741f667201b54880c925faec4b',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (177, 'client.rm.lock.retryInterval', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (178, 'client.rm.lock.retryTimes', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (179, 'client.rm.lock.retryPolicyBranchRollbackOnConflict', 'SEATA_GROUP', 'true',
        'b326b5062b2f0e69046810717534cb09', '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '',
        '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (180, 'client.rm.reportRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (181, 'client.rm.tableMetaCheckEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327',
        '2021-06-29 05:00:24', '2021-06-29 05:00:24', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (182, 'client.rm.tableMetaCheckerInterval', 'SEATA_GROUP', '60000', '2b4226dd7ed6eb2d419b881f3ae9c97c',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (183, 'client.rm.sqlParserType', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (184, 'client.rm.reportSuccessEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (185, 'client.rm.sagaBranchRegisterEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (186, 'client.rm.tccActionInterceptorOrder', 'SEATA_GROUP', '-2147482648', 'f056d9efa5dae3872f9da035c83bcde8',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (187, 'client.tm.commitRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (188, 'client.tm.rollbackRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (189, 'client.tm.defaultGlobalTransactionTimeout', 'SEATA_GROUP', '60000', '2b4226dd7ed6eb2d419b881f3ae9c97c',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (190, 'client.tm.degradeCheck', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (191, 'client.tm.degradeCheckAllowTimes', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (192, 'client.tm.degradeCheckPeriod', 'SEATA_GROUP', '2000', '08f90c1a417155361a5c4b8d297e0d78',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (193, 'client.tm.interceptorOrder', 'SEATA_GROUP', '-2147482648', 'f056d9efa5dae3872f9da035c83bcde8',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (194, 'store.mode', 'SEATA_GROUP', 'db', 'd77d5e503ad1439f585ac494268b351b', '2021-06-29 05:00:25',
        '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (195, 'store.lock.mode', 'SEATA_GROUP', 'file', '8c7dd922ad47494fc02c388e12c00eac', '2021-06-29 05:00:25',
        '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (196, 'store.session.mode', 'SEATA_GROUP', 'file', '8c7dd922ad47494fc02c388e12c00eac', '2021-06-29 05:00:25',
        '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (197, 'store.file.dir', 'SEATA_GROUP', 'file_store/data', '6a8dec07c44c33a8a9247cba5710bbb2',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (198, 'store.file.maxBranchSessionSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (199, 'store.file.maxGlobalSessionSize', 'SEATA_GROUP', '512', '10a7cdd970fe135cf4f7bb55c0e3b59f',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (200, 'store.file.fileWriteBufferCacheSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (201, 'store.file.flushDiskMode', 'SEATA_GROUP', 'async', '0df93e34273b367bb63bad28c94c78d5',
        '2021-06-29 05:00:25', '2021-06-29 05:00:25', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (202, 'store.file.sessionReloadReadSize', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd',
        '2021-06-29 05:00:26', '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (203, 'store.db.datasource', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2021-06-29 05:00:26',
        '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (204, 'store.db.dbType', 'SEATA_GROUP', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2021-06-29 05:00:26',
        '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (205, 'store.db.driverClassName', 'SEATA_GROUP', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760',
        '2021-06-29 05:00:26', '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (206, 'store.db.url', 'SEATA_GROUP',
        'jdbc:mysql://47.100.74.170:33061/seata?useUnicode=true&rewriteBatchedStatements=true',
        '0ac808ea16698b3bc3505aa3973455b3', '2021-06-29 05:00:26', '2021-06-29 05:00:26', NULL, '47.100.74.170', '',
        '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (207, 'store.db.user', 'SEATA_GROUP', 'root', '63a9f0ea7bb98050796b649e85481845', '2021-06-29 05:00:26',
        '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (208, 'store.db.password', 'SEATA_GROUP', 'Zxx131013@', '77e1f583da9bebf8337dddf9c3394e50',
        '2021-06-29 05:00:26', '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (209, 'store.db.minConn', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2021-06-29 05:00:26',
        '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (210, 'store.db.maxConn', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2021-06-29 05:00:26',
        '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (211, 'store.db.globalTable', 'SEATA_GROUP', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b',
        '2021-06-29 05:00:26', '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (212, 'store.db.branchTable', 'SEATA_GROUP', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c',
        '2021-06-29 05:00:26', '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (213, 'store.db.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2021-06-29 05:00:26',
        '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (214, 'store.db.lockTable', 'SEATA_GROUP', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f',
        '2021-06-29 05:00:26', '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (215, 'store.db.maxWait', 'SEATA_GROUP', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2021-06-29 05:00:26',
        '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (216, 'store.redis.mode', 'SEATA_GROUP', 'single', 'dd5c07036f2975ff4bce568b6511d3bc', '2021-06-29 05:00:26',
        '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (217, 'store.redis.single.host', 'SEATA_GROUP', '127.0.0.1', 'f528764d624db129b32c21fbca0cb8d6',
        '2021-06-29 05:00:26', '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (218, 'store.redis.single.port', 'SEATA_GROUP', '6379', '92c3b916311a5517d9290576e3ea37ad',
        '2021-06-29 05:00:26', '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (219, 'store.redis.maxConn', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2021-06-29 05:00:26',
        '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (220, 'store.redis.minConn', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2021-06-29 05:00:26',
        '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (221, 'store.redis.maxTotal', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2021-06-29 05:00:26',
        '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (222, 'store.redis.database', 'SEATA_GROUP', '0', 'cfcd208495d565ef66e7dff9f98764da', '2021-06-29 05:00:26',
        '2021-06-29 05:00:26', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (223, 'store.redis.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2021-06-29 05:00:27',
        '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (224, 'service.vgroupMapping.auiu_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (225, 'server.recovery.committingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (226, 'server.recovery.asynCommittingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (227, 'server.recovery.rollbackingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (228, 'server.recovery.timeoutRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (229, 'server.maxCommitRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (230, 'server.maxRollbackRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (231, 'server.rollbackRetryTimeoutUnlockEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (232, 'server.distributedLockExpireTime', 'SEATA_GROUP', '10000', 'b7a782741f667201b54880c925faec4b',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (233, 'client.undo.dataValidation', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (234, 'client.undo.logSerialization', 'SEATA_GROUP', 'jackson', 'b41779690b83f182acc67d6388c7bac9',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (235, 'client.undo.onlyCareUpdateColumns', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (236, 'server.undo.logSaveDays', 'SEATA_GROUP', '7', '8f14e45fceea167a5a36dedd4bea2543', '2021-06-29 05:00:27',
        '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (237, 'server.undo.logDeletePeriod', 'SEATA_GROUP', '86400000', 'f4c122804fe9076cb2710f55c3c6e346',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (238, 'client.undo.logTable', 'SEATA_GROUP', 'undo_log', '2842d229c24afe9e61437135e8306614',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (239, 'client.undo.compress.enable', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (240, 'client.undo.compress.type', 'SEATA_GROUP', 'zip', 'adcdbd79a8d84175c229b192aadc02f2',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (241, 'client.undo.compress.threshold', 'SEATA_GROUP', '64k', 'bd44a6458bdbff0b5cac721ba361f035',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (242, 'log.exceptionRate', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2021-06-29 05:00:27',
        '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (243, 'transport.serialization', 'SEATA_GROUP', 'seata', 'b943081c423b9a5416a706524ee05d40',
        '2021-06-29 05:00:27', '2021-06-29 05:00:27', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (244, 'transport.compressor', 'SEATA_GROUP', 'none', '334c4a4c42fdb79d7ebc3e73b517e6f8', '2021-06-29 05:00:28',
        '2021-06-29 05:00:28', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (245, 'metrics.enabled', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-06-29 05:00:28',
        '2021-06-29 05:00:28', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0', NULL, NULL, NULL,
        'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (246, 'metrics.registryType', 'SEATA_GROUP', 'compact', '7cf74ca49c304df8150205fc915cd465',
        '2021-06-29 05:00:28', '2021-06-29 05:00:28', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (247, 'metrics.exporterList', 'SEATA_GROUP', 'prometheus', 'e4f00638b8a10e6994e67af2f832d51c',
        '2021-06-29 05:00:28', '2021-06-29 05:00:28', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (248, 'metrics.exporterPrometheusPort', 'SEATA_GROUP', '9898', '7b9dc501afe4ee11c56a4831e20cee71',
        '2021-06-29 05:00:28', '2021-06-29 05:00:28', NULL, '47.100.74.170', '', '20219151-84fc-4c8d-a94a-633dbd709dc0',
        NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (256, 'auiu-meta-dynamic-routes.yaml', 'DEFAULT_GROUP',
        'routes:\n  - id: meta-uaa\n    uri: lb://meta-uaa\n    predicates:\n      - Path=/meta-uaa/**\n    #filters:\n    #  - StripPrefix=1\n\n  - id: meta-admin\n    uri: lb://meta-admin\n    predicates:\n      - Path=/meta-admin/**\n    filters:\n      - name: RequestRateLimiter\n        args:\n          # 限流策略\n          key-resolver: \'#{@remoteAddressKeyResolver}\'\n          # 令牌桶每秒填充率\n          redis-rate-limiter.burstCapacity: 20\n          # 令牌桶容量\n          redis-rate-limiter.replenishRate: 20\n    #  - StripPrefix=1',
        'b3403ec9ac83990f26fc2a509970948a', '2021-12-29 20:05:46', '2022-06-19 23:47:42', 'nacos', '101.82.58.134', '',
        '4af1f54f-5abf-4ec8-a8b0-20d50885d42b', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (259, 'auiu-meta-cloud-dev.yaml', 'DEFAULT_GROUP',
        '#spring配置\nspring:\n  redis:\n    #redis 单机环境配置\n    host: 127.0.0.1\n    port: 6379\n    password:\n    database: 15\n    ssl: false\n    #redis 集群环境配置\n    #cluster:\n    #  nodes: 127.0.0.1:7001,127.0.0.1:7002,127.0.0.1:7003\n    #  commandTimeout: 5000\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    druid:\n      # MySql校验\n      validation-query: select 1\n\nmeta:\n  datasource:\n    # url: jdbc:mysql://47.100.74.170:33061/auiu_meta?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull\n    url: jdbc:mysql://localhost:3306/auiu_meta?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull\n    username: root\n    password: Zxx131013@\n  # public-key: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJLLURUH8XNPkX9MME3mQrog3mpkOVYwnYrUqLbSN+Wi6IcmWg1YywHi/kKGUU1MTLjv3C406f1IYu+XWJ3XmX8CAwEAAQ==\n  # 预览模式开关\n  preview:\n    enabled: false\n  # 租户开关\n#  tenant:\n#    enable: false\n  # 网关认证开关\n  uaa:\n    enabled: false\n    # 开关：同应用账号互踢\n    isSingleLogin: false\n    ignore-url:\n      - /auth/login/**\n      - /auth/callback/**\n      - /auth/sms-code\n  # Swagger文档开关\n  swagger:\n    enabled: true\n',
        '34a2ecdf5f885b3619c4d86e24b89461', '2021-12-29 21:41:26', '2022-08-04 02:57:16', 'nacos', '61.173.29.123',
        'auiu-meta', '4af1f54f-5abf-4ec8-a8b0-20d50885d42b', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`,
                           `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES (260, 'auiu-meta-cloud.yaml', 'DEFAULT_GROUP',
        '#服务器配置\nserver:\n  undertow:\n    # 以下的配置会影响buffer,这些buffer会用于服务器连接的IO操作,有点类似netty的池化内存管理\n    buffer-size: 1024\n    # 是否分配的直接内存\n    direct-buffers: true\n    # 线程配置\n    threads:\n      # 设置IO线程数, 它主要执行非阻塞的任务,它们会负责多个连接, 默认设置每个CPU核心一个线程\n      io: 16\n      # 阻塞任务线程池, 当执行类似servlet请求阻塞操作, undertow会从这个线程池中取得线程,它的值设置取决于系统的负载\n      worker: 400\n\n#spring配置\nspring:\n  devtools:\n    restart:\n      log-condition-evaluation-delta: false\n    livereload:\n      port: 23333\n  main:\n    allow-bean-definition-overriding: true\n    allow-circular-references: true\n  #修改文件上传限制\n  servlet:\n    multipart:\n      # 文件最大限制\n      max-file-size: 1024MB\n      # 请求最大限制\n      max-request-size: 1024MB\n      enabled: true\n      # 设置文件缓存的临界点,超过则先保存到临时目录,默认为0,所有文件都会进行缓存\n      file-size-threshold: 0\n\n#feign配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n#对外暴露端口\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always',
        '5b01a1228b565c07e99f42af9ba41f04', '2021-12-29 21:41:43', '2022-07-01 22:22:59', 'nacos', '223.166.141.200',
        '', '4af1f54f-5abf-4ec8-a8b0-20d50885d42b', '', '', '', 'yaml', '');
COMMIT;

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`
(
    `id`           bigint                                           NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`     varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `datum_id`     varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL COMMENT '内容',
    `gmt_modified` datetime                                         NOT NULL COMMENT '修改时间',
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin COMMENT ='增加租户字段';

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`
(
    `id`           bigint                                           NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin          DEFAULT NULL COMMENT 'app_name',
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL COMMENT 'content',
    `beta_ips`     varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin         DEFAULT NULL COMMENT 'betaIps',
    `md5`          varchar(32) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`     text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
    `src_ip`       varchar(20) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL COMMENT 'source ip',
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin          DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin COMMENT ='config_info_beta';

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`
(
    `id`           bigint                                           NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin          DEFAULT '' COMMENT 'tenant_id',
    `tag_id`       varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin          DEFAULT NULL COMMENT 'app_name',
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL COMMENT 'content',
    `md5`          varchar(32) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`     text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
    `src_ip`       varchar(20) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL COMMENT 'source ip',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin COMMENT ='config_info_tag';

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`
(
    `id`        bigint                                           NOT NULL COMMENT 'id',
    `tag_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
    `tag_type`  varchar(64) CHARACTER SET utf8 COLLATE utf8_bin  DEFAULT NULL COMMENT 'tag_type',
    `data_id`   varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`  varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
    `nid`       bigint                                           NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`nid`) USING BTREE,
    UNIQUE KEY `uk_configtagrelation_configidtag` (`id`, `tag_name`, `tag_type`) USING BTREE,
    KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin COMMENT ='config_tag_relation';

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`
(
    `id`                bigint unsigned                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `group_id`          varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
    `quota`             int unsigned                                     NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
    `usage`             int unsigned                                     NOT NULL DEFAULT '0' COMMENT '使用量',
    `max_size`          int unsigned                                     NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count`    int unsigned                                     NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
    `max_aggr_size`     int unsigned                                     NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int unsigned                                     NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
    `gmt_create`        datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`      datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_group_id` (`group_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin COMMENT ='集群、各Group容量信息表';

-- ----------------------------
-- Records of group_capacity
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`
(
    `id`           bigint unsigned                                  NOT NULL,
    `nid`          bigint unsigned                                  NOT NULL AUTO_INCREMENT,
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `group_id`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin          DEFAULT NULL COMMENT 'app_name',
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL,
    `md5`          varchar(32) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL,
    `gmt_create`   datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified` datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `src_user`     text CHARACTER SET utf8 COLLATE utf8_bin,
    `src_ip`       varchar(20) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL,
    `op_type`      char(10) CHARACTER SET utf8 COLLATE utf8_bin              DEFAULT NULL,
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin          DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`nid`) USING BTREE,
    KEY `idx_gmt_create` (`gmt_create`) USING BTREE,
    KEY `idx_gmt_modified` (`gmt_modified`) USING BTREE,
    KEY `idx_did` (`data_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 305
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin COMMENT ='多租户改造';

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
BEGIN;
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`,
                               `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`)
VALUES (259, 304, 'auiu-meta-cloud-dev.yaml', 'DEFAULT_GROUP', 'auiu-meta',
        '#spring配置\nspring:\n  redis:\n    #redis 单机环境配置\n    host: 127.0.0.1\n    port: 6379\n    password:\n    database: 15\n    ssl: false\n    #redis 集群环境配置\n    #cluster:\n    #  nodes: 127.0.0.1:7001,127.0.0.1:7002,127.0.0.1:7003\n    #  commandTimeout: 5000\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    druid:\n      # MySql校验\n      validation-query: select 1\n\nmeta:\n  datasource:\n    url: jdbc:mysql://47.100.74.170:33061/auiu_meta?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull\n    username: root\n    password: Zxx131013@\n  # public-key: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJLLURUH8XNPkX9MME3mQrog3mpkOVYwnYrUqLbSN+Wi6IcmWg1YywHi/kKGUU1MTLjv3C406f1IYu+XWJ3XmX8CAwEAAQ==\n  # 预览模式开关\n  preview:\n    enabled: false\n  # 租户开关\n#  tenant:\n#    enable: false\n  # 网关认证开关\n  uaa:\n    enabled: false\n    # 开关：同应用账号互踢\n    isSingleLogin: false\n    ignore-url:\n      - /auth/login/**\n      - /auth/callback/**\n      - /auth/sms-code\n  # Swagger文档开关\n  swagger:\n    enabled: true\n',
        '5258beb51d20869477b12e869f96dee2', '2022-08-04 15:57:15', '2022-08-04 02:57:16', 'nacos', '61.173.29.123', 'U',
        '4af1f54f-5abf-4ec8-a8b0-20d50885d42b');
COMMIT;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`
(
    `role`     varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL,
    `resource` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `action`   varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL,
    UNIQUE KEY `uk_role_permission` (`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;

-- ----------------------------
-- Records of permissions
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`
(
    `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `role`     varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    UNIQUE KEY `idx_user_role` (`username`, `role`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;

-- ----------------------------
-- Records of roles
-- ----------------------------
BEGIN;
INSERT INTO `roles` (`username`, `role`)
VALUES ('nacos', 'ROLE_ADMIN');
COMMIT;

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`
(
    `id`                bigint unsigned                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`         varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
    `quota`             int unsigned                                     NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
    `usage`             int unsigned                                     NOT NULL DEFAULT '0' COMMENT '使用量',
    `max_size`          int unsigned                                     NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count`    int unsigned                                     NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
    `max_aggr_size`     int unsigned                                     NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int unsigned                                     NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
    `gmt_create`        datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`      datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin COMMENT ='租户容量信息表';

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`
(
    `id`            bigint                                           NOT NULL AUTO_INCREMENT COMMENT 'id',
    `kp`            varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
    `tenant_id`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
    `tenant_name`   varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
    `tenant_desc`   varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
    `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin  DEFAULT NULL COMMENT 'create_source',
    `gmt_create`    bigint                                           NOT NULL COMMENT '创建时间',
    `gmt_modified`  bigint                                           NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`, `tenant_id`) USING BTREE,
    KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 16
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin COMMENT ='tenant_info';

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
BEGIN;
INSERT INTO `tenant_info` (`id`, `kp`, `tenant_id`, `tenant_name`, `tenant_desc`, `create_source`, `gmt_create`,
                           `gmt_modified`)
VALUES (9, '1', '2294c22a-2d50-4267-9d97-0656425645af', 'dev', '测试环境', 'nacos', 1623130291392, 1623130291392);
INSERT INTO `tenant_info` (`id`, `kp`, `tenant_id`, `tenant_name`, `tenant_desc`, `create_source`, `gmt_create`,
                           `gmt_modified`)
VALUES (14, '1', '20219151-84fc-4c8d-a94a-633dbd709dc0', 'seta', 'seataio分布式事务', 'nacos', 1624955120383, 1624955120383);
INSERT INTO `tenant_info` (`id`, `kp`, `tenant_id`, `tenant_name`, `tenant_desc`, `create_source`, `gmt_create`,
                           `gmt_modified`)
VALUES (15, '1', '4af1f54f-5abf-4ec8-a8b0-20d50885d42b', 'auiu-cloud', '微服务基本配置', 'nacos', 1640744969964,
        1640744969964);
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL,
    `password` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `enabled`  tinyint(1)                                              NOT NULL,
    PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` (`username`, `password`, `enabled`)
VALUES ('nacos', '$2a$10$6lJYKGKRjbxkaFuPSM06ZO71H8LM0.fsaG8.2M5wAwh4ccYAYojH6', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
