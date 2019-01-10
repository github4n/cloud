
USE `iot_db_ifttt`;

CREATE TABLE IF NOT EXISTS iot_db_ifttt.`service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `desc` varchar(300) DEFAULT NULL  COMMENT '描述',
  `code` varchar(30) NOT NULL COMMENT '服务标识 timer等',
  `type` ENUM('this','that') NOT NULL COMMENT '类型 0this 1that',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='服务表';

CREATE TABLE IF NOT EXISTS iot_db_ifttt.`applet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `status` ENUM('on','off') NOT NULL COMMENT '状态 0on  1off',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='应用程序表';

CREATE TABLE IF NOT EXISTS iot_db_ifttt.`applet_this` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `applet_id` bigint(20) NOT NULL COMMENT '程序主键',
  `service_code` varchar(50) NOT NULL COMMENT '服务标识',
  `logic` ENUM('or','and') NOT NULL COMMENT '逻辑 or/and',
  `param` varchar(200) DEFAULT NULL COMMENT '传参',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='this组表';

CREATE TABLE IF NOT EXISTS iot_db_ifttt.`applet_that` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `applet_id` bigint(20) NOT NULL COMMENT '程序主键',
  `service_code` varchar(50) NOT NULL COMMENT '服务标识',
  `code` varchar(50) NOT NULL COMMENT '触发码',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='that组表';

CREATE TABLE IF NOT EXISTS iot_db_ifttt.`applet_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `applet_id` bigint(20) NOT NULL COMMENT '程序主键',
  `type` varchar(10) NOT NULL COMMENT '规则类型 this/that',
  `event_id` bigint(20) NOT NULL COMMENT 'this/that主键',
  `json` varchar(500) NOT NULL COMMENT '规则体',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='子规则表';

CREATE TABLE IF NOT EXISTS iot_db_ifttt.`applet_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `applet_id` bigint(20) NOT NULL COMMENT '程序主键',
  `relation_key` varchar(100) NOT NULL COMMENT '关联key devId/sunny/spaceId等',
  `type` ENUM('devStatus','sunny','space') NOT NULL COMMENT '类型 1 dev_status 2 sunny 3 space',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='程序关联表';



