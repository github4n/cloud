USE iot_db_control;

CREATE TABLE IF NOT EXISTS `iot_db_openapi`.`ifttt_api` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `type` enum('that','this') DEFAULT 'this' COMMENT '类型',
  `code` varchar(50) DEFAULT NULL COMMENT '处理标识',
  `field` varchar(50) DEFAULT NULL COMMENT '判断字段',
  `device_flag` int(2) DEFAULT NULL COMMENT '关联设备 0否 1是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) COMMENT='IFTTT API 配置';

CREATE TABLE IF NOT EXISTS `iot_db_openapi`.`ifttt_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `api_id` bigint(20) DEFAULT NULL COMMENT 'api主键',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) COMMENT='IFTTT 关联设备 配置';
