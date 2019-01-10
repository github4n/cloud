
-- --------------------------------------------产品定义相关表-----------------------------------------------------------------------------


USE `iot_db_device`;

-- ----------------------------
-- Table structure for style_template
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `style_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `name` VARCHAR(32) NOT NULL COMMENT '样式名称',
  `code` VARCHAR(32) DEFAULT NULL COMMENT '样式唯一标识code',
  `img` VARCHAR(500) DEFAULT NULL COMMENT '图片',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='样式模板表';



-- ----------------------------
-- Table structure for device_type_to_style
-- ----------------------------
CREATE TABLE IF NOT EXISTS `device_type_to_style` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '类型id',
  `style_template_id` bigint(20) DEFAULT NULL COMMENT '样式模板id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='类型-样式表';


-- ----------------------------
-- Table structure for product_to_style
-- ----------------------------
CREATE TABLE IF NOT EXISTS `product_to_style` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `style_template_id` bigint(20) DEFAULT NULL COMMENT '样式模板id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品-样式表';


-- -------------------------------------------------------service 模组相关表--------------------------------------------------------------------------------------------

-- ----------------------------
-- Table structure for device_type_to_service_module
-- ----------------------------
CREATE TABLE IF NOT EXISTS `device_type_to_service_module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `device_type_id` bigint(20) NOT NULL COMMENT '设备类型id',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备类型对应模组表';


-- ----------------------------
-- Table structure for product_to_service_module
-- ----------------------------
CREATE TABLE IF NOT EXISTS `product_to_service_module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品对应模组表';



-- ----------------------------
-- Table structure for service_module
-- ----------------------------
CREATE TABLE IF NOT EXISTS `service_module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'parent_ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `version` VARCHAR(32) NOT NULL COMMENT '版本',
  `name` VARCHAR(32) NOT NULL COMMENT '模组名称',
  `code` VARCHAR(32) DEFAULT NULL COMMENT '模组唯一标识code',
  `property_status` tinyint(2) DEFAULT NULL COMMENT '属性状态，0:必选,1:必选',
  `develop_status` tinyint(2) DEFAULT NULL COMMENT '开发状态,0:未开发,1:开发中,2:已上线',
  `test_case` VARCHAR(500) DEFAULT NULL COMMENT '测试用例',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组表';



-- ----------------------------
-- Table structure for service_module_style
-- ----------------------------
CREATE TABLE IF NOT EXISTS `service_module_style` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `name` VARCHAR(32) NOT NULL COMMENT '模组样式名称',
  `code` VARCHAR(32) DEFAULT NULL COMMENT '模组样式唯一标识code',
  `thumbnail` VARCHAR(32) NOT NULL COMMENT '缩略图',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-样式表';



-- ----------------------------
-- Table structure for service_style_to_template
-- ----------------------------
CREATE TABLE IF NOT EXISTS `service_style_to_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `module_style_id` bigint(20) NOT NULL COMMENT '样式id',
  `style_template_id` bigint(20) NOT NULL COMMENT '样式模板id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-样式-to-模板样式表';




-- ----------------------------
-- Table structure for service_to_action
-- ----------------------------
CREATE TABLE IF NOT EXISTS `service_to_action` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `module_action_id` bigint(20) NOT NULL COMMENT '方法id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-to-方法表';



-- ----------------------------
-- Table structure for service_to_event
-- ----------------------------
CREATE TABLE IF NOT EXISTS `service_to_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `module_event_id` bigint(20) NOT NULL COMMENT '事件id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-to-事件表';



-- ----------------------------
-- Table structure for service_to_property
-- ----------------------------
CREATE TABLE IF NOT EXISTS `service_to_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `module_property_id` bigint(20) NOT NULL COMMENT '属性id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-to-属性表';



-- ----------------------------
-- Table structure for service_module_action
-- ----------------------------
CREATE TABLE IF NOT EXISTS `service_module_action` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'parent_ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `version` VARCHAR(32) NOT NULL COMMENT '版本',
  `name` VARCHAR(32) NOT NULL COMMENT '方法名称',
  `code` VARCHAR(100) DEFAULT NULL COMMENT '唯一标识code',
  `tags` VARCHAR(100) DEFAULT NULL COMMENT '标签',
  `api_level` int(11) DEFAULT NULL COMMENT '等级',
  `develop_status` tinyint(2) DEFAULT NULL COMMENT '开发状态,0:未开发,1:开发中,2:已上线',
  `property_status` tinyint(2) DEFAULT NULL COMMENT '属性状态，0:可选,1:必选',
  `req_param_type` int(2) DEFAULT NULL COMMENT '请求参数格式：0 array ,1 object',
  `return_type` int(2) DEFAULT NULL COMMENT '返回参数格式：0 array ,1 object',
  `params` VARCHAR(500) DEFAULT NULL COMMENT 'json参数集',
  `return_desc` VARCHAR(500) DEFAULT NULL COMMENT '返回内容描述',
  `returns` VARCHAR(500) DEFAULT NULL COMMENT '返回结果集',
  `test_case` VARCHAR(500) DEFAULT NULL COMMENT '测试用例',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-方法表';




-- ----------------------------
-- Table structure for service_module_event
-- ----------------------------
CREATE TABLE IF NOT EXISTS `service_module_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'parent_ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `version` VARCHAR(32) NOT NULL COMMENT '版本',
  `name` VARCHAR(32) NOT NULL COMMENT '事件名称',
  `code` VARCHAR(100) DEFAULT NULL COMMENT '唯一标识code',
  `api_level` int(11) DEFAULT NULL COMMENT 'api等级',
  `develop_status` tinyint(2) DEFAULT NULL COMMENT '开发状态,0:未开发,1:开发中,2:已上线',
  `property_status` tinyint(2) DEFAULT NULL COMMENT '属性状态，0:可选,1:必选',
  `params` VARCHAR(500) DEFAULT NULL COMMENT 'json参数集',
  `tags` VARCHAR(200) NOT NULL COMMENT '标签',
  `test_case` VARCHAR(500) DEFAULT NULL COMMENT '测试用例',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8 COMMENT='模组-事件表';



-- ----------------------------
-- Table structure for service_module_property
-- ----------------------------
CREATE TABLE IF NOT EXISTS `service_module_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'parent_ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `version` VARCHAR(32) NOT NULL COMMENT '版本',
  `name` VARCHAR(32) NOT NULL COMMENT '属性名称',
  `code` VARCHAR(100) DEFAULT NULL COMMENT '唯一标识code',
  `tags` VARCHAR(100) DEFAULT NULL COMMENT '标签',
  `api_level` int(11) DEFAULT NULL COMMENT 'api等级',
  `develop_status` tinyint(2) DEFAULT NULL COMMENT '开发状态,0:未开发,1:开发中,2:已上线',
  `req_param_type` int(2) DEFAULT NULL COMMENT '请求参数格式：0 array ,1 object',
  `return_type` int(2) DEFAULT NULL COMMENT '返回参数格式：0 array ,1 object',
  `property_status` tinyint(2) DEFAULT NULL COMMENT '属性状态，0:可选,1:必选',
  `rw_status` tinyint(2) DEFAULT NULL COMMENT '读写特征 0:可读可写,1:不可读不可写,2:可读不可写',
  `param_type` tinyint(2) DEFAULT NULL COMMENT '参数类型，0:int,1:float,2:bool,3:enum,4:string',
  `min_value` VARCHAR(32) NOT NULL COMMENT '最小值',
  `max_value` VARCHAR(32) NOT NULL COMMENT '最大值',
  `allowed_values` VARCHAR(32) NOT NULL COMMENT '允许值(enum 可以多个逗号隔开)',
  `test_case` VARCHAR(500) DEFAULT NULL COMMENT '测试用例',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-属性表';



-- ----------------------------
-- Table structure for module_action_to_property
-- ----------------------------
CREATE TABLE IF NOT EXISTS `module_action_to_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `module_action_id` bigint(20) NOT NULL COMMENT '方法id',
  `module_property_id` bigint(20) NOT NULL COMMENT '属性id',

  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-方法-to-参数表';



-- ----------------------------
-- Table structure for module_event_to_property
-- ----------------------------
CREATE TABLE IF NOT EXISTS `module_event_to_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `module_event_id` bigint(20) NOT NULL COMMENT '事件id',
  `event_property_id` bigint(20) NOT NULL COMMENT '属性id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-事件-to-属性表';




-- ----------------------------
-- Table structure for develop_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `develop_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `type` tinyint(2) DEFAULT NULL COMMENT '开发者类型：0,开发者，1开发组',
  `name` VARCHAR(32) NOT NULL COMMENT '名称',
  `code` VARCHAR(32) NOT NULL COMMENT '标识',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='开发信息表';

-- ----------------------------
-- Table structure for ota_file_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `ota_file_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `latest_version` VARCHAR(32) DEFAULT NULL COMMENT '最新版本',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `location_id` bigint(20) DEFAULT NULL COMMENT '区域id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='OTA文件信息表';


