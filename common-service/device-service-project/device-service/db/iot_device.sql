SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cust_p2pid
-- ----------------------------
DROP TABLE IF EXISTS `cust_p2pid`;
CREATE TABLE `cust_p2pid` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `p2p_id` varchar(50) NOT NULL COMMENT 'P2PID',
  `use_mark` tinyint(1) DEFAULT NULL COMMENT '使用标识(0-未使用，1-已使用)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `overdue_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin  COMMENT='P2PID表';



-- ----------------------------
-- Table structure for cust_uuid_manage
-- ----------------------------
DROP TABLE IF EXISTS `cust_uuid_manage`;
CREATE TABLE `cust_uuid_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `batch_num` varchar(20) NOT NULL COMMENT '批次号',
  `create_time` datetime DEFAULT NULL COMMENT '生成时间',
  `cust_code` varchar(10) DEFAULT NULL COMMENT '客户代码',
  `cust_name` varchar(100) DEFAULT NULL COMMENT '客户名称',
  `uuid_type` varchar(30) DEFAULT NULL COMMENT 'uuid 类型',
  `down_loaded_num` decimal(10,0) DEFAULT NULL COMMENT '下载次数',
  `uuid_amount` decimal(10,0) DEFAULT NULL COMMENT 'uuid生成数量',
  `status` tinyint(1) DEFAULT NULL COMMENT '生成状态(0：进行中;1：已完成;2：生成失败;3：P2PID不足)',
  `file_id` varchar(1000) DEFAULT NULL COMMENT 'UUID列表CVS文件ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin  COMMENT='UUID管理表';

-- ----------------------------
-- Table structure for data_point
-- ----------------------------
DROP TABLE IF EXISTS `data_point`;
CREATE TABLE `data_point` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `label_name` varchar(255) DEFAULT NULL COMMENT '显示名称',
  `property_code` varchar(255) DEFAULT NULL COMMENT '属性标识',
  `mode` tinyint(4) DEFAULT NULL COMMENT '读写类型(0 r, 1 w, 2 rw)',
  `data_type` tinyint(4) DEFAULT NULL COMMENT '数值类型(0 boolean，1 int，2 unsignedInt，3.枚举型，4故障型，5字符型，6 RAW型）',
  `icon_name` varchar(255) DEFAULT NULL COMMENT '图标名称',
  `property` varchar(255) DEFAULT NULL COMMENT '数值属性',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin  COMMENT='功能表';

-- ----------------------------
-- Table structure for data_point_style
-- ----------------------------
DROP TABLE IF EXISTS `data_point_style`;
CREATE TABLE `data_point_style` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `data_point_id` bigint(20) DEFAULT NULL COMMENT '功能点id',
  `style_template_id` bigint(20) DEFAULT NULL COMMENT '样式模板id',
  `name` varchar(255) DEFAULT NULL COMMENT '功能点名称',
  `mode` tinyint(4) DEFAULT NULL COMMENT '模式',
  `img` varchar(255) DEFAULT NULL COMMENT '图标',
  `backgroud` varchar(255) DEFAULT NULL COMMENT '背景',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `parent_id` varchar(32) DEFAULT NULL COMMENT 'parent 设备id',
  `uuid` varchar(32) NOT NULL COMMENT '设备id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `name` varchar(200) DEFAULT NULL,
  `icon` varchar(128) DEFAULT NULL COMMENT '设备图片',
  `ip` varchar(20) DEFAULT NULL,
  `mac` varchar(100) DEFAULT NULL,
  `sn` varchar(80) DEFAULT NULL COMMENT '设备序列号',
  `is_direct_device` tinyint(1) DEFAULT NULL COMMENT '是否直连设备0否、1是',
  `business_type_id` varchar(50) DEFAULT NULL COMMENT '业务类型',
  `reality_id` varchar(100) DEFAULT NULL COMMENT 'MAC 地址 hue网关用',
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '设置类型id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `extra_name` varchar(100) DEFAULT NULL COMMENT '扩展名 hue用',
  `ssid` varchar(128) DEFAULT NULL COMMENT 'wifi名称',
  `reset_random` varchar(20) DEFAULT NULL COMMENT '重置标识',
  `version` varchar(20) DEFAULT NULL COMMENT '设备版本号',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update_date` datetime DEFAULT NULL COMMENT '修改时间',

  `conditional` varchar(100) DEFAULT NULL COMMENT '条件',
  `location_id` bigint(20) DEFAULT NULL COMMENT '位置id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




-- ----------------------------
-- Table structure for device_catalog
-- ----------------------------
DROP TABLE IF EXISTS `device_catalog`;
CREATE TABLE `device_catalog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `name` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime  DEFAULT NULL,
  `update_time` datetime  DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for device_extend
-- ----------------------------
DROP TABLE IF EXISTS `device_extend`;
CREATE TABLE `device_extend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `device_id` varchar(32) NOT NULL COMMENT '设备id',
  `batch_num_id` bigint(20) DEFAULT NULL COMMENT '批次id(主键)',
  `p2p_id` bigint(20) DEFAULT NULL COMMENT 'p2pid(主键)',
  `uuid_validity_days` decimal(5,0) DEFAULT NULL COMMENT '有效时长',
  `device_cipher` varchar(10) DEFAULT NULL COMMENT '设备密码',
  `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`,`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for device_sn
-- ----------------------------
DROP TABLE IF EXISTS `device_sn`;
CREATE TABLE `device_sn` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `device_sn` VARCHAR(50) NOT NULL COMMENT '设备SN号',
  `using_date` datetime DEFAULT NULL COMMENT '使用时间',
  PRIMARY KEY (`id`,`device_sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备SN表';

-- ----------------------------
-- Table structure for device_state
-- ----------------------------
DROP TABLE IF EXISTS `device_state`;
CREATE TABLE `device_state` (

  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `device_id` VARCHAR(32) NOT NULL COMMENT '设备id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `property_name` varchar(100) DEFAULT NULL COMMENT '属性名称',
  `property_value` varchar(50) DEFAULT NULL COMMENT '属性值',
  `log_date` datetime  DEFAULT NULL COMMENT '上报日期',
  `group_id` varchar(100) DEFAULT NULL COMMENT '组id',
  `property_desc` varchar(100) DEFAULT NULL COMMENT '设备属性描述',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for device_status
-- ----------------------------
DROP TABLE IF EXISTS `device_status`;
CREATE TABLE `device_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `device_id` VARCHAR(32) NOT NULL COMMENT '设备id',
  `on_off` tinyint(1) DEFAULT NULL COMMENT '开关，1：开启；0：关闭',
  `active_status` tinyint(1) DEFAULT NULL COMMENT '激活状态（0-未激活，1-已激活）',
  `active_time` datetime DEFAULT NULL COMMENT '激活时间',
  `online_status` enum('disconnected','connected') DEFAULT 'disconnected' COMMENT '在线状态',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`,`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for device_type
-- ----------------------------
DROP TABLE IF EXISTS `device_type`;
CREATE TABLE `device_type` (

  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `name` varchar(255) DEFAULT NULL COMMENT '类型名称',
  `device_catalog_id` bigint(20) DEFAULT NULL COMMENT '设备分类id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL,
  `vender_flag` varchar(50) DEFAULT NULL COMMENT '厂商标识',
  `type` varchar(50) DEFAULT NULL COMMENT '设备真实类型',
  `description` varchar(255) DEFAULT NULL COMMENT '类型描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for device_type_data_point
-- ----------------------------
DROP TABLE IF EXISTS `device_type_data_point`;
CREATE TABLE `device_type_data_point` (

  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `data_point_id` bigint(20) DEFAULT NULL COMMENT '功能点id',
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '设备类型id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueDeviceType` (`data_point_id`,`device_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for device_type_style_template
-- ----------------------------
DROP TABLE IF EXISTS `device_type_style_template`;
CREATE TABLE `device_type_style_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '设备类型id',
  `style_template_id` bigint(20) DEFAULT NULL COMMENT '样式模板id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for device_version
-- ----------------------------
DROP TABLE IF EXISTS `device_version`;
CREATE TABLE `device_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `upgrade_status` tinyint(4) NOT NULL  COMMENT '升级模式1 升级无需通知用户,2 升级通知用户不可控(不升级),3 升级通知用户可控设备(不升级)',
  `fw_type` tinyint(4) NOT NULL  COMMENT '固件类型',
  `version_num` varchar(10) NOT NULL COMMENT '设备版本号',
  `md5` varchar(50) DEFAULT NULL COMMENT 'MD5值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '设备类型id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `version_url` varchar(500) DEFAULT NULL COMMENT '版本下载URL',
  `cust_code` varchar(20) DEFAULT NULL COMMENT '厂商代码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '设备类型id',
  `product_name` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `communication_mode` tinyint(4) DEFAULT NULL COMMENT '通信类型（WIFI、蓝牙）',
  `transmission_mode` tinyint(4) DEFAULT NULL COMMENT '数据传输方式（定长<所有数据点一并上报>、变长<只上报有变化的数据点数据>）',
  `develop_status` tinyint(2) DEFAULT NULL COMMENT '开发状态,0:未开发,1:开发中,2:已上线',
  `check_status` tinyint(2) DEFAULT NULL COMMENT '审核状态,0:未审核,1:审核中,2:审核成功,3:审核失败',
  `enterprise_develop_id` bigint(20) DEFAULT NULL COMMENT '企业开发者id',
  `icon` varchar(255) DEFAULT NULL COMMENT 'icon',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `model` varchar(255) DEFAULT NULL COMMENT '产品型号',
  `config_net_mode` varchar(255) DEFAULT NULL COMMENT '配置网络方式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1090210038 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for product_data_point
-- ----------------------------
DROP TABLE IF EXISTS `product_data_point`;
CREATE TABLE `product_data_point` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `device_data_point_id` bigint(20) DEFAULT NULL COMMENT '功能数据点id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_data_point_id` (`device_data_point_id`,`product_id`),
  UNIQUE KEY `device_data_point_id_2` (`device_data_point_id`,`product_id`),
  UNIQUE KEY `device_data_point_id_3` (`device_data_point_id`,`product_id`),
  UNIQUE KEY `device_data_point_id_4` (`device_data_point_id`,`product_id`),
  UNIQUE KEY `device_data_point_id_5` (`device_data_point_id`,`product_id`),
  UNIQUE KEY `device_data_point_id_6` (`device_data_point_id`,`product_id`),
  UNIQUE KEY `device_data_point_id_7` (`device_data_point_id`,`product_id`),
  UNIQUE KEY `device_data_point_id_8` (`device_data_point_id`,`product_id`),
  UNIQUE KEY `device_data_point_id_9` (`device_data_point_id`,`product_id`),
  UNIQUE KEY `device_data_point_id_10` (`device_data_point_id`,`product_id`),
  UNIQUE KEY `device_data_point_id_11` (`device_data_point_id`,`product_id`),
  UNIQUE KEY `product_data_point` (`device_data_point_id`,`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for product_style_template
-- ----------------------------
DROP TABLE IF EXISTS `product_style_template`;
CREATE TABLE `product_style_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `style_template_id` bigint(20) DEFAULT NULL COMMENT '样式模板id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for style_template
-- ----------------------------
DROP TABLE IF EXISTS `style_template`;
CREATE TABLE `style_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `name` varchar(255) DEFAULT NULL COMMENT '模板名称',
  `description` varchar(500) DEFAULT NULL COMMENT '模板描述',
  `img` varchar(255) DEFAULT NULL COMMENT '缩略图',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ----------------------------
-- Table structure for user_device
-- ----------------------------
DROP TABLE IF EXISTS `user_device`;
CREATE TABLE `user_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id(bigint)',
  `device_id` VARCHAR(32) NOT NULL COMMENT '设备id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `user_type` varchar(10) DEFAULT NULL COMMENT '用户类型（root：主账号sub: 子账号）',
  `password` varchar(50) DEFAULT NULL COMMENT '用户访问设备秘钥',
  `event_notify_enabled` tinyint(1) DEFAULT NULL COMMENT '事件通知使能（0：开启，1：关闭）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`,`device_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-设备关系表';



-- ----------------------------
-- Table structure for org_device
-- ----------------------------
DROP TABLE IF EXISTS `org_device`;
CREATE TABLE `org_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `device_id` VARCHAR(32) NOT NULL COMMENT '设备id',
  `org_id` bigint(20) DEFAULT NULL COMMENT '组织id',
  `user_type` varchar(10) DEFAULT NULL COMMENT '用户类型（root：主账号sub: 子账号）',
  `password` varchar(50) DEFAULT NULL COMMENT '用户访问设备秘钥',
  `event_notify_enabled` tinyint(1) DEFAULT NULL COMMENT '事件通知使能（0：开启，1：关闭）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`,`device_id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织-设备关系表';





-- ----------------------------
-- Table structure for upgrade_version_log
-- ----------------------------
DROP TABLE IF EXISTS `upgrade_version_log`;
CREATE TABLE `upgrade_version_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `version_id` bigint(20) DEFAULT NULL COMMENT '版本id',
  `device_id` VARCHAR(32) NOT NULL COMMENT '设备id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `mode` tinyint(1) DEFAULT NULL COMMENT '升级模式 0：普通升级 1：静默升级',
  `stage` tinyint(1) DEFAULT NULL COMMENT '阶段 0: 空闲 1:下载阶段 2:烧录fw阶段 3:成功 4:失败',
  `percent` tinyint(1) DEFAULT NULL COMMENT '0-100百分比',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ota升级日志表';


-- ----------------------------
-- Table structure for data_point to ADD COLUMS 2018-05-03
-- ----------------------------
alter table data_point add COLUMN create_time datetime ;
alter table data_point add COLUMN create_by BIGINT(20) ;
alter table data_point add COLUMN is_custom tinyint(1) ;

-- ----------------------------
-- Table structure for product to ADD COLUMS 2018-05-03
-- ----------------------------
alter table product add COLUMN tenant_id BIGINT(20) ;

-- ----------------------------
-- Table structure for device_status to ADD COLUMS 2018-11-08
-- ----------------------------
alter table device_status add COLUMN token varchar(255) ;

-- ----------------------------
-- Table structure for device_business_type
-- ----------------------------
DROP TABLE IF EXISTS `device_business_type`;
CREATE TABLE `device_business_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `business_type` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务功能类型',
  `description` varchar(255) DEFAULT NULL COMMENT '业务类型描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备-业务类型表';

-- ----------------------------
-- Table structure for `smart_data_point`
-- ----------------------------
DROP TABLE IF EXISTS `smart_data_point`;
CREATE TABLE `smart_data_point` (
  `id` bigint(20) NOT NULL,
  `property_code` varchar(20) DEFAULT NULL,
  `smart_code` varchar(20) DEFAULT NULL,
  `smart` tinyint(2) DEFAULT NULL COMMENT '1alexa，2google',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- ----------------------------
-- Table structure for device_to_version
-- ----------------------------
DROP TABLE IF EXISTS `device_to_version`;
CREATE TABLE `device_to_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `device_id` VARCHAR(32) NOT NULL COMMENT '设备id',
  `fw_type` tinyint(4) NOT NULL  COMMENT '固件类型',
  `version` varchar(50) NOT NULL COMMENT '对应的固件版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备-固件类型-版本表';



DROP TABLE IF EXISTS `smart_device_type`;
CREATE TABLE `smart_device_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_type_id` bigint(20) DEFAULT NULL,
  `smart_type` varchar(20) DEFAULT NULL COMMENT '第三方类型',
  `smart` tinyint(2) DEFAULT NULL COMMENT '1alexa，2google',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- --------------------------------------------产品定义相关表-----------------------------------------------------------------------------



-- ----------------------------
-- Table structure for style_template
-- ----------------------------
DROP TABLE IF EXISTS `style_template`;
CREATE TABLE `style_template` (
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
DROP TABLE IF EXISTS `device_type_to_style`;
CREATE TABLE `device_type_to_style` (
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
DROP TABLE IF EXISTS `product_to_style`;
CREATE TABLE `product_to_style` (
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


---------------------------------------------------------service 模组相关表--------------------------------------------------------------------------------------------

-- ----------------------------
-- Table structure for device_type_to_service_module
-- ----------------------------
DROP TABLE IF EXISTS `device_type_to_service_module`;
CREATE TABLE `device_type_to_service_module` (
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
DROP TABLE IF EXISTS `product_to_service_module`;
CREATE TABLE `product_to_service_module` (
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
DROP TABLE IF EXISTS `service_module`;
CREATE TABLE `service_module` (
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
DROP TABLE IF EXISTS `service_module_style`;
CREATE TABLE `service_module_style` (
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
DROP TABLE IF EXISTS `service_style_to_template`;
CREATE TABLE `service_style_to_template` (
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
DROP TABLE IF EXISTS `service_to_action`;
CREATE TABLE `service_to_action` (
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
DROP TABLE IF EXISTS `service_to_event`;
CREATE TABLE `service_to_event` (
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
DROP TABLE IF EXISTS `service_to_property`;
CREATE TABLE `service_to_property` (
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
DROP TABLE IF EXISTS `service_module_action`;
CREATE TABLE `service_module_action` (
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
DROP TABLE IF EXISTS `service_module_event`;
CREATE TABLE `service_module_event` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-事件表';



-- ----------------------------
-- Table structure for service_module_property
-- ----------------------------
DROP TABLE IF EXISTS `service_module_property`;
CREATE TABLE `service_module_property` (
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
DROP TABLE IF EXISTS `module_action_to_property`;
CREATE TABLE `module_action_to_property` (
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
DROP TABLE IF EXISTS `module_event_to_property`;
CREATE TABLE `module_event_to_property` (
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
DROP TABLE IF EXISTS `develop_info`;
CREATE TABLE `develop_info` (
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

