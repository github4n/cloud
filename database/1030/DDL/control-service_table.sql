
USE `iot_db_control`;

CREATE TABLE IF NOT EXISTS iot_db_control.`automation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `icon` varchar(30) DEFAULT NULL COMMENT '图标',
  `trigger_type` varchar(30) NOT NULL COMMENT '触发类型',
  `status` int(2) NOT NULL COMMENT '0禁用 1启用',
  `user_id` bigint(20) NOT NULL COMMENT '用户主键',
  `space_id` bigint(20) NOT NULL COMMENT '空间主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `applet_id` bigint(20) DEFAULT NULL COMMENT '程序主键',
  `is_multi` int(2) NOT NULL COMMENT '是否直连 0否 1是',
  `direct_id` varchar(50) DEFAULT NULL COMMENT '直连设备主键',
  `delay` int(5) DEFAULT NULL COMMENT '使能开关延时时间',
  `time_json` varchar(300) DEFAULT NULL COMMENT '时间参数json',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `rule_id` bigint(20) DEFAULT NULL COMMENT 'rule主键，用于数据迁移',
  PRIMARY KEY (`id`)
) COMMENT='联动表';

CREATE TABLE IF NOT EXISTS iot_db_control.`package` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='套包表';

CREATE TABLE IF NOT EXISTS iot_db_control.`template_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `package_id` bigint(20) DEFAULT NULL COMMENT '套包主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `type` int(2) DEFAULT NULL COMMENT '模板类型 0安防 1scene 2 ifttt',
  `json` varchar(1000) DEFAULT NULL COMMENT '规则体',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='模板规则表';

CREATE TABLE IF NOT EXISTS iot_db_control.`automation_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(30) DEFAULT NULL COMMENT '类型 dev/scene',
  `object_id` varchar(50) DEFAULT NULL COMMENT '关联id',
  `item_id` bigint(20) DEFAULT NULL COMMENT 'ifttt item主键',
  `applet_id` bigint(20) DEFAULT NULL COMMENT 'ifttt 程序主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`)
) COMMENT='联动子项';

CREATE TABLE IF NOT EXISTS iot_db_control.`security_rule`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键id',
	`security_id` bigint(20) COMMENT '安防主键',
	`tenant_id` bigint(20) COMMENT '租户id',
	`type` varchar(20) COMMENT '安防类型：stay、away、panic',
	`enabled` int(2) COMMENT '安防使能开关，0禁用1启用',
	`defer` int(20) COMMENT '安防联动延时生效',
	`delay` int(20) COMMENT '延时报警',
	`if` varchar(500) COMMENT 'if条件配置',
	`then` varchar(500) COMMENT 'then条件配置',
	`create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
	PRIMARY KEY (`id`)
) COMMENT='安防规则表';