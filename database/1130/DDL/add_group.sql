USE iot_db_device;

CREATE TABLE IF NOT EXISTS iot_db_device.`group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'group id',
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '组名称',
  `home_id` bigint(20) DEFAULT NULL COMMENT '空间id',
  `dev_group_id` int(11) DEFAULT NULL COMMENT '设备控制组Id',
  `icon` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '图标',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='组控制表';

CREATE TABLE IF NOT EXISTS iot_db_device.`group_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'group id',
  `device_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '设备id',
  `group_id` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='组控制详情表';