CREATE TABLE IF NOT EXISTS iot_db_tenant.`device_network_step_base` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `device_type_id` bigint(20) NOT NULL COMMENT '设备类型id',
  `network_type_id` bigint(20) NOT NULL COMMENT '配网模式id(对应network_type表的id)',
  `is_help` enum('Y','N') NOT NULL default 'N' COMMENT '是否是帮助文档(Y:是，N：否)',
  `step` tinyint(2) NOT NULL COMMENT '对应步骤',
  `icon` varchar(50) NOT NULL COMMENT '配网引导图',
  `data_status` enum('invalid','valid') NOT NULL COMMENT '数据有效性（invalid:失效；valid：有效）',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  PRIMARY KEY (`id`),
  KEY `device_type_id_index` (`device_type_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='设备配网步骤模板表（BOSS维护）';


CREATE TABLE IF NOT EXISTS iot_db_tenant.`device_network_step_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `app_id` bigint(20) NOT NULL COMMENT 'App应用',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `network_type_id` bigint(20) NOT NULL COMMENT '配网模式id(对应network_type表的id)',
  `is_help` enum('Y','N') NOT NULL default 'N' COMMENT '是否是帮助文档(Y:是，N：否)',
  `step` tinyint(2) NOT NULL COMMENT '对应步骤',
  `icon` varchar(50) NOT NULL COMMENT '配网引导图',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  PRIMARY KEY (`id`),
  KEY `tenant_id_index` (`tenant_id`) USING BTREE,
  KEY `app_id_index` (`app_id`) USING BTREE,
  KEY `product_id_index` (`product_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户设备配网步骤详情表（portal维护）';


CREATE TABLE IF NOT EXISTS iot_db_tenant.`lang_info_base` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT '-1' COMMENT '租户主键 默认值-1',
  `object_type` varchar(20) NOT NULL COMMENT '对象类型 prodcutOTA、appConfig、deviceNetwork、deviceType',
  `object_id` bigint(20) NOT NULL COMMENT '对象ID 产品id或APPid、deviceTypeId，如果object_type是appConfig，object_id为-1',
  `belong_module` varchar(20) default NULL COMMENT '所属模块',
  `lang_type` varchar(30) NOT NULL COMMENT '语言类型',
  `lang_key` varchar(100) NOT NULL COMMENT '国际化键',
  `lang_value` varchar(1000) NOT NULL COMMENT '国际化内容',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` enum('invalid','valid') NOT NULL DEFAULT 'valid' COMMENT '数据有效性（invalid:失效；valid：有效）',
  PRIMARY KEY (`id`),
  KEY `tenant_id_index` (`tenant_id`) USING BTREE,
  KEY `object_type_index` (`object_type`) USING BTREE,
  KEY `object_id_index` (`object_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8 COMMENT='文案基础信息表（Boss维护）';

CREATE TABLE IF NOT EXISTS iot_db_tenant.`lang_info_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `object_type` varchar(20) NOT NULL COMMENT '对象类型 prodcutOTA、appConfig、deviceNetwork、deviceType',
  `object_id` varchar(50) NOT NULL COMMENT '对象ID 产品id或APPid、deviceTypeId',
  `belong_module` varchar(20) default NULL COMMENT '所属模块',
  `lang_type` varchar(30) NOT NULL COMMENT '语言类型',
  `lang_key` varchar(100) NOT NULL COMMENT '国际化键',
  `lang_value` varchar(1000) NOT NULL COMMENT '国际化内容',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` enum('invalid','valid') NOT NULL DEFAULT 'valid' COMMENT '数据有效性（invalid:失效；valid：有效）',
  PRIMARY KEY (`id`),
  KEY `tenant_id_index` (`tenant_id`) USING BTREE,
  KEY `object_type_index` (`object_type`) USING BTREE,
  KEY `object_id_index` (`object_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8 COMMENT='租户文案信息表';


CREATE TABLE IF NOT EXISTS iot_db_device.`network_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
   network_name	varchar(30)	NOT NULL COMMENT '配网模式名称',
   description	varchar(100) DEFAULT NULL	COMMENT '描述',
  `is_deleted` enum('invalid','valid') NOT NULL DEFAULT 'valid' COMMENT '数据有效性（invalid:失效；valid：有效）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配网模式信息表（boss维护）';

CREATE TABLE IF NOT EXISTS iot_db_device.`technical_network_relate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
   network_type_id	bigint(20)	NOT NULL COMMENT '配网模式id（对应network_type表id）',
   technical_scheme_id	bigint(20) NOT NULL	COMMENT '技术方案id（对应goods_info表type_id=2）',
	`create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='技术方案支持配网模式关联关系表（boss维护）';

CREATE TABLE IF NOT EXISTS iot_db_device.`devicetype_technical_relate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
   device_type_id	bigint(20)	NOT NULL COMMENT '设备类型id（对应device_type表id）',
   technical_scheme_id	bigint(20) NOT NULL	COMMENT '技术方案id（对应goods_info表type_id=2）',
	`create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备类型与技术方案关联关系表（boss维护）';


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_type' and COLUMN_NAME='network_type';
SET @query = If(@exist=0,
'alter TABLE iot_db_device.device_type add COLUMN network_type VARCHAR(100) DEFAULT NULL COMMENT \'支持的配网模式（对应network_type表id字段，用,拼接）\'',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;





