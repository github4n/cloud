-- --------------------------------------------------------
-- 主机:                           192.168.5.35
-- 服务器版本:                        5.7.24-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  7.0.0.4363
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 iot_db_device 的数据库结构
CREATE DATABASE IF NOT EXISTS `iot_db_device` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iot_db_device`;


-- 导出  表 iot_db_device.configuration 结构
CREATE TABLE IF NOT EXISTS `configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `param` varchar(255) DEFAULT NULL COMMENT '参数名称',
  `value` varchar(255) DEFAULT NULL COMMENT '参数内容',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.countdown 结构
CREATE TABLE IF NOT EXISTS `countdown` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `device_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '设备id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id(bigint)',
  `countdown` bigint(20) NOT NULL COMMENT '倒计时时间（单位s）',
  `is_enable` int(11) DEFAULT NULL COMMENT '是否使能（0关闭，1开启）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间时间',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.cust_p2pid 结构
CREATE TABLE IF NOT EXISTS `cust_p2pid` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `p2p_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'P2PID',
  `use_mark` tinyint(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `overdue_time` datetime DEFAULT NULL COMMENT '过期时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='P2PID表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.cust_uuid_manage 结构
CREATE TABLE IF NOT EXISTS `cust_uuid_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `batch_num` varchar(36) COLLATE utf8mb4_bin NOT NULL COMMENT '批次号',
  `create_time` datetime DEFAULT NULL COMMENT '生成时间',
  `cust_code` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客户代码',
  `cust_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客户名称',
  `uuid_type` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'uuid 类型',
  `down_loaded_num` decimal(10,0) DEFAULT NULL COMMENT '下载次数',
  `uuid_amount` decimal(10,0) DEFAULT NULL COMMENT 'uuid生成数量',
  `status` tinyint(1) DEFAULT NULL COMMENT '生成状态(0：进行中;1：已完成;2：生成失败;3：P2PID不足)',
  `file_id` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'UUID列表CVS文件ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='UUID管理表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.daily_electricity_statistics 结构
CREATE TABLE IF NOT EXISTS `daily_electricity_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id(bigint)',
  `device_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '设备id',
  `electric_value` double(20,3) DEFAULT '0.000' COMMENT '电量值',
  `day` date DEFAULT NULL COMMENT '日期',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.daily_runtime 结构
CREATE TABLE IF NOT EXISTS `daily_runtime` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id(bigint)',
  `device_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '设备id',
  `runtime` bigint(20) DEFAULT '0' COMMENT '运行时间',
  `day` date DEFAULT NULL COMMENT '日期',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.data_point 结构
CREATE TABLE IF NOT EXISTS `data_point` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `label_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '显示名称',
  `property_code` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '属性标识',
  `mode` tinyint(4) DEFAULT NULL COMMENT '读写类型(0 r, 1 w, 2 rw)',
  `data_type` tinyint(4) DEFAULT NULL COMMENT '数值类型(0 boolean，1 int，2 unsignedInt，3.枚举型，4故障型，5字符型，6 RAW型）',
  `icon_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图标名称',
  `create_time` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `property` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数值属性',
  `description` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `is_custom` tinyint(1) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='功能表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.data_point_style 结构
CREATE TABLE IF NOT EXISTS `data_point_style` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `data_point_id` bigint(20) DEFAULT NULL COMMENT '功能点id',
  `style_template_id` bigint(20) DEFAULT NULL COMMENT '样式模板id',
  `name` varchar(255) DEFAULT NULL COMMENT '功能点名称',
  `mode` tinyint(4) DEFAULT NULL COMMENT '模式',
  `img` varchar(255) DEFAULT NULL COMMENT '图标',
  `backgroud` varchar(255) DEFAULT NULL COMMENT '背景',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.develop_info 结构
CREATE TABLE IF NOT EXISTS `develop_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `type` tinyint(2) DEFAULT NULL COMMENT '开发者类型：0,开发者，1开发组',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `code` varchar(32) NOT NULL COMMENT '标识',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='开发信息表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device 结构
CREATE TABLE IF NOT EXISTS `device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent_id` varchar(32) DEFAULT NULL COMMENT 'parent 设备id',
  `uuid` varchar(32) NOT NULL COMMENT '设备id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `name` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `icon` varchar(128) DEFAULT NULL COMMENT '设备图片',
  `ip` varchar(20) DEFAULT NULL,
  `mac` varchar(100) DEFAULT NULL,
  `sn` varchar(80) DEFAULT NULL COMMENT '设备序列号',
  `is_direct_device` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否直连设备0否、1是',
  `business_type_id` bigint(50) DEFAULT NULL COMMENT '业务类型',
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
  `dev_model` varchar(50) DEFAULT NULL,
  `hw_version` varchar(50) DEFAULT NULL,
  `supplier` varchar(50) DEFAULT NULL,
  `remote_group_id` bigint(20) DEFAULT NULL COMMENT '遥控器id',
  `is_app_dev` tinyint(2) DEFAULT '0' COMMENT '是否app子设备',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.devicetype_technical_relate 结构
CREATE TABLE IF NOT EXISTS `devicetype_technical_relate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_type_id` bigint(20) NOT NULL COMMENT '设备类型id（对应device_type表id）',
  `technical_scheme_id` bigint(20) NOT NULL COMMENT '技术方案id（对应goods_info表type_id=2）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备类型与技术方案关联关系表（boss维护）';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_business_type 结构
CREATE TABLE IF NOT EXISTS `device_business_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `business_type` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务功能类型',
  `description` varchar(255) DEFAULT NULL COMMENT '业务类型描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品ID',
  `is_home_show` tinyint(2) DEFAULT NULL COMMENT '是否在首页显示  0，否  1，是',
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备-业务类型表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_catalog 结构
CREATE TABLE IF NOT EXISTS `device_catalog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_extend 结构
CREATE TABLE IF NOT EXISTS `device_extend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `device_id` varchar(32) NOT NULL COMMENT '设备id',
  `p2p_id` varchar(50) DEFAULT NULL COMMENT 'p2pid(主键)',
  `uuid_validity_days` decimal(5,0) DEFAULT NULL COMMENT '有效时长',
  `device_cipher` varchar(10) DEFAULT NULL COMMENT '设备密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `first_upload_sub_dev` tinyint(1) DEFAULT '1' COMMENT '是否首次上传子设备,1是,0否(带套包的网关需要)',
  `unbind_flag` tinyint(4) NOT NULL DEFAULT '0',
  `reset_flag` tinyint(4) NOT NULL DEFAULT '0',
  `batch_num` bigint(20) DEFAULT NULL COMMENT '批次号',
  `batch_num_id` varchar(36) DEFAULT NULL COMMENT '批次号',
  `area` varchar(50) DEFAULT NULL COMMENT '地区',
  `comm_type` varchar(12) DEFAULT NULL COMMENT '产品类型 : 88(485),89(网口),8A(网口+wifi)，8B(网口+NB)，8C(网口+2G)，8D(网口+4G）',
  `timezone` varchar(12) DEFAULT NULL COMMENT '时区',
  `server_ip` varchar(64) DEFAULT NULL COMMENT '服务器IP',
  `server_port` bigint(10) DEFAULT NULL COMMENT '服务器端口',
  `report_interval` bigint(10) DEFAULT NULL COMMENT '上传间隔，单位秒',
  `address` bigint(20) DEFAULT NULL COMMENT '设备操作地址',
  PRIMARY KEY (`id`),
  KEY `idx_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_remote_control 结构
CREATE TABLE IF NOT EXISTS `device_remote_control` (
  `id` bigint(32) NOT NULL,
  `device_type_id` bigint(32) NOT NULL,
  `key_code` varchar(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL COMMENT 'GROUP 组控；SCENE 情景；SINGLE 单控',
  `default_value` varchar(200) DEFAULT NULL COMMENT '默认值',
  `event_status` varchar(20) DEFAULT NULL COMMENT 'pressed/released/held down  短按/长按释放/长按 ',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_sn 结构
CREATE TABLE IF NOT EXISTS `device_sn` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `device_sn` varchar(50) NOT NULL COMMENT '设备SN号',
  `using_date` datetime DEFAULT NULL COMMENT '使用时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`,`device_sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备SN表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_state 结构
CREATE TABLE IF NOT EXISTS `device_state` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `device_id` varchar(32) NOT NULL COMMENT '设备id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `property_name` varchar(100) DEFAULT NULL COMMENT '属性名称',
  `property_value` varchar(50) DEFAULT NULL COMMENT '属性值',
  `log_date` datetime DEFAULT NULL COMMENT '上报日期',
  `group_id` varchar(100) DEFAULT NULL COMMENT '组id',
  `property_desc` varchar(100) DEFAULT NULL COMMENT '设备属性描述',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  KEY `key_device_id` (`device_id`),
  KEY `key_device_id_property_name` (`device_id`,`property_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_status 结构
CREATE TABLE IF NOT EXISTS `device_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `device_id` varchar(32) NOT NULL COMMENT '设备id',
  `on_off` tinyint(1) DEFAULT NULL COMMENT '开关，1：开启；0：关闭',
  `active_status` tinyint(1) DEFAULT NULL COMMENT '激活状态（0-未激活，1-已激活）',
  `active_time` datetime DEFAULT NULL COMMENT '激活时间',
  `online_status` enum('disconnected','connected') DEFAULT 'disconnected' COMMENT '在线状态',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `last_login_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`device_id`),
  KEY `idx_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_type 结构
CREATE TABLE IF NOT EXISTS `device_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) DEFAULT NULL COMMENT '类型名称',
  `device_catalog_name` varchar(255) DEFAULT NULL,
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
  `img` varchar(255) DEFAULT NULL COMMENT '图片Id',
  `whether_direct` tinyint(2) DEFAULT '0' COMMENT '是否为直连（0非直连，1直连）',
  `whether_kit` tinyint(2) DEFAULT '0' COMMENT '是否为套包(0非套装，1套装)',
  `whether_soc` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否为免开发（0:非免开发；1：免开发）',
  `network_type` varchar(100) DEFAULT NULL COMMENT '支持的配网模式（对应network_type表id字段，用,拼接）',
  `ifttt_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT 'ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_type_back 结构
CREATE TABLE IF NOT EXISTS `device_type_back` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) DEFAULT NULL COMMENT '类型名称',
  `device_catalog_name` varchar(255) DEFAULT NULL,
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
  `img` varchar(255) DEFAULT NULL COMMENT '图片Id',
  `whether_direct` tinyint(2) DEFAULT '0' COMMENT '是否为直连（0非直连，1直连）',
  `whether_kit` tinyint(2) DEFAULT '0' COMMENT '是否为套包(0非套装，1套装)',
  `whether_soc` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否为免开发（0:非免开发；1：免开发）',
  `network_type` varchar(100) DEFAULT NULL COMMENT '支持的配网模式（对应network_type表id字段，用,拼接）',
  `ifttt_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT 'ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_type_data_point 结构
CREATE TABLE IF NOT EXISTS `device_type_data_point` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `data_point_id` bigint(20) DEFAULT NULL COMMENT '功能点id',
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '设备类型id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueDeviceType` (`data_point_id`,`device_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_type_style_template 结构
CREATE TABLE IF NOT EXISTS `device_type_style_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '设备类型id',
  `style_template_id` bigint(20) DEFAULT NULL COMMENT '样式模板id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_type_to_goods 结构
CREATE TABLE IF NOT EXISTS `device_type_to_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_type_id` bigint(20) NOT NULL COMMENT '设备类型Id',
  `goods_code` varchar(50) NOT NULL COMMENT '增值服务id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`),
  KEY `idx` (`device_type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备类型增值服务关联表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_type_to_service_module 结构
CREATE TABLE IF NOT EXISTS `device_type_to_service_module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `device_type_id` bigint(20) NOT NULL COMMENT '设备类型id',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '0可选，1必选',
  PRIMARY KEY (`id`),
  KEY `idx_dtype_tenant` (`device_type_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备类型对应模组表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.device_type_to_style 结构
CREATE TABLE IF NOT EXISTS `device_type_to_style` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '类型id',
  `style_template_id` bigint(20) DEFAULT NULL COMMENT '样式模板id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='类型-样式表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.gateway_subdev_relation 结构
CREATE TABLE IF NOT EXISTS `gateway_subdev_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键 与网关产品id租户一致',
  `pardev_id` bigint(20) NOT NULL COMMENT '网关产品id',
  `subdev_id` bigint(20) NOT NULL COMMENT '子设备产品id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('valid','invalid') NOT NULL DEFAULT 'valid' COMMENT '数据有效性',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网关子设备关联表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.generate_module 结构
CREATE TABLE IF NOT EXISTS `generate_module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer` varchar(255) DEFAULT NULL COMMENT '客户',
  `device_type_name` varchar(255) DEFAULT NULL COMMENT '设备类型名字',
  `agreement` varchar(255) DEFAULT NULL COMMENT '协议',
  `g_f` varchar(255) DEFAULT NULL COMMENT '固定标示F',
  `device_type_info` varchar(255) DEFAULT NULL COMMENT '设备类型信息',
  `g_n` varchar(255) DEFAULT NULL COMMENT '固定标示n',
  `code` varchar(255) DEFAULT NULL COMMENT '编码',
  `number` bigint(20) DEFAULT NULL COMMENT '序列',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `code_number` varchar(255) DEFAULT NULL,
  `describes` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.generate_module_agreement 结构
CREATE TABLE IF NOT EXISTS `generate_module_agreement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '名字',
  `abbreviation_name` varchar(255) DEFAULT NULL COMMENT '简称',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.goods_smart 结构
CREATE TABLE IF NOT EXISTS `goods_smart` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `goods_code` varchar(50) NOT NULL COMMENT '商品编码',
  `smart` tinyint(2) NOT NULL COMMENT 'smart',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_goods` (`goods_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品smart对照表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.goods_sub_dict 结构
CREATE TABLE IF NOT EXISTS `goods_sub_dict` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `goods_code` varchar(50) NOT NULL COMMENT '商品编码',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_code_smart` (`code`,`goods_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品子项字典表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.group 结构
CREATE TABLE IF NOT EXISTS `group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'group id',
  `name` varchar(50) DEFAULT NULL COMMENT '组名称',
  `home_id` bigint(20) DEFAULT NULL COMMENT '空间id',
  `dev_group_id` int(11) DEFAULT NULL COMMENT '设备控制组Id',
  `icon` varchar(20) DEFAULT NULL COMMENT '图标',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组控制表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.group_detail 结构
CREATE TABLE IF NOT EXISTS `group_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'group id',
  `device_id` varchar(32) NOT NULL COMMENT '设备id',
  `group_id` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组控制详情表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.license_usage 结构
CREATE TABLE IF NOT EXISTS `license_usage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `batch_num` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '批次号',
  `license_uuid` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '设备id',
  `secret_key` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT 'mqtt秘钥',
  `mac` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '设备的mac',
  `mode_id` varchar(255) DEFAULT NULL COMMENT '产品型号',
  `create_by` bigint(20) DEFAULT NULL COMMENT '上传者',
  `create_time` datetime DEFAULT NULL COMMENT '生成时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.min_electricity_statistics 结构
CREATE TABLE IF NOT EXISTS `min_electricity_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id(bigint)',
  `device_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '设备id',
  `step` int(11) DEFAULT NULL COMMENT '统计间隔时长',
  `time` datetime DEFAULT NULL COMMENT '统计时间点(UTC时间）',
  `electric_value` double(20,3) DEFAULT '0.000' COMMENT '电量值',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `area` varchar(32) NOT NULL COMMENT '设备所在地区',
  `localtime` datetime DEFAULT NULL COMMENT '设备上报时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.min_runtime 结构
CREATE TABLE IF NOT EXISTS `min_runtime` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id(bigint)',
  `device_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '设备id',
  `step` int(11) DEFAULT NULL COMMENT '统计间隔时长',
  `time` datetime DEFAULT NULL COMMENT '统计时间点（UTC时间）',
  `runtime` bigint(20) DEFAULT '0' COMMENT '运行时间',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `area` varchar(32) NOT NULL COMMENT '设备所在地区',
  `localtime` datetime DEFAULT NULL COMMENT '设备上报时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.module_action_to_property 结构
CREATE TABLE IF NOT EXISTS `module_action_to_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `module_action_id` bigint(20) NOT NULL COMMENT '方法id',
  `module_property_id` bigint(20) NOT NULL COMMENT '属性id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `param_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT 'param类型(0:入参 1：出参)',
  PRIMARY KEY (`id`),
  KEY `idx_action_tenant` (`module_action_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-方法-to-参数表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.module_event_to_property 结构
CREATE TABLE IF NOT EXISTS `module_event_to_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `module_event_id` bigint(20) NOT NULL COMMENT '事件id',
  `event_property_id` bigint(20) NOT NULL COMMENT '属性id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  KEY `idx_event_tenant` (`module_event_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-事件-to-属性表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.monthly_electricity_statistics 结构
CREATE TABLE IF NOT EXISTS `monthly_electricity_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id(bigint)',
  `device_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '设备id',
  `electric_value` double(20,3) DEFAULT '0.000' COMMENT '电量值',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `month` int(11) DEFAULT NULL,
  `year` year(4) DEFAULT NULL COMMENT '年',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.monthly_runtime 结构
CREATE TABLE IF NOT EXISTS `monthly_runtime` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id(bigint)',
  `device_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '设备id',
  `runtime` bigint(20) DEFAULT '0' COMMENT '运行时间',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `month` int(11) DEFAULT NULL,
  `year` year(4) DEFAULT NULL COMMENT '年',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.network_type 结构
CREATE TABLE IF NOT EXISTS `network_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `network_name` varchar(30) NOT NULL COMMENT '配网模式名称',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `is_deleted` enum('invalid','valid') NOT NULL DEFAULT 'valid' COMMENT '数据有效性（invalid:失效；valid：有效）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `type_code` varchar(20) DEFAULT NULL COMMENT '字典表',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配网模式信息表（boss维护）';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.operate_step_record 结构
CREATE TABLE IF NOT EXISTS `operate_step_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `operate_id` bigint(20) NOT NULL COMMENT '操作对象id',
  `operate_type` enum('product','application') NOT NULL COMMENT '操作类型（product：产品；application：应用）',
  `step_index` tinyint(2) NOT NULL COMMENT '步骤下标',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作步骤记录表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.ota_device_version 结构
CREATE TABLE IF NOT EXISTS `ota_device_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `device_id` varchar(32) NOT NULL COMMENT '设备id',
  `fw_type` tinyint(4) NOT NULL COMMENT '固件类型',
  `version` varchar(50) NOT NULL COMMENT '对应的固件版本',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.ota_firmware_version 结构
CREATE TABLE IF NOT EXISTS `ota_firmware_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id 主键主动增长',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `ota_version` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '版本号',
  `ota_type` enum('WholePackage','Difference') CHARACTER SET utf8mb4 NOT NULL COMMENT '整包升级WholePackage  差分升级Difference',
  `ota_file_id` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '升级文件id',
  `ota_md5` varchar(50) CHARACTER SET utf8mb4 NOT NULL COMMENT 'MD5值',
  `fw_type` enum('0','1','2','3','4') DEFAULT NULL COMMENT '分位的类型 0:所有的模块在一个分位里面(默认) 1:wifi 模块的分位 2: zigbee模块的分位 3: z-wave模块的分位 4：ble模块的分位 ',
  `version_online_time` datetime DEFAULT NULL COMMENT '版本上线时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `init_version` tinyint(2) DEFAULT '0' COMMENT '是否初始版本 0:不是 1：是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`tenant_id`,`product_id`,`ota_version`) USING BTREE COMMENT '复合主键'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.ota_strategy_config 结构
CREATE TABLE IF NOT EXISTS `ota_strategy_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `plan_id` bigint(20) NOT NULL COMMENT '升级计划id',
  `strategy_group` int(11) DEFAULT '0' COMMENT '策略组',
  `upgrade_total` int(11) DEFAULT '0' COMMENT '升级总量',
  `threshold` int(11) DEFAULT '0' COMMENT '容灾阀值',
  `trigger_action` tinyint(2) DEFAULT '0' COMMENT '默认值：0，0：停止升级计划',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') NOT NULL DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ota容灾策略配置表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.ota_strategy_detail 结构
CREATE TABLE IF NOT EXISTS `ota_strategy_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plan_id` bigint(20) NOT NULL COMMENT '升级计划id',
  `strategy_group` int(11) DEFAULT '0' COMMENT '策略组',
  `batch_num` bigint(20) DEFAULT NULL COMMENT '设备批次号',
  `device_uuid` varchar(36) DEFAULT NULL COMMENT '设备uuid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='容灾策略明细表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.ota_strategy_report 结构
CREATE TABLE IF NOT EXISTS `ota_strategy_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plan_id` bigint(20) NOT NULL COMMENT '升级计划id',
  `strategy_group` int(11) DEFAULT '0' COMMENT '策略组',
  `device_uuid` varchar(36) NOT NULL COMMENT '设备uuid',
  `model` varchar(255) NOT NULL COMMENT '产品型号',
  `upgrade_type` enum('Push','Force') NOT NULL DEFAULT 'Push' COMMENT '升级方式 推送升级Push   强制升级Force',
  `target_version` varchar(32) NOT NULL COMMENT '升级版本号',
  `original_version` varchar(32) DEFAULT NULL COMMENT '原版本号',
  `upgrade_result` enum('Success','Failed') DEFAULT NULL COMMENT '升级结果',
  `complete_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='容灾策略报告表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.ota_upgrade_log 结构
CREATE TABLE IF NOT EXISTS `ota_upgrade_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `plan_id` bigint(20) NOT NULL COMMENT '升级计划id',
  `device_uuid` varchar(36) CHARACTER SET utf8mb4 NOT NULL COMMENT '设备uuid',
  `upgrade_type` enum('Push','Force') CHARACTER SET utf8mb4 NOT NULL COMMENT '升级方式  推送升级Push   强制升级Force',
  `target_version` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '升级版本号',
  `original_version` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '原版本号',
  `upgrade_result` enum('Success','Failed') DEFAULT 'Failed' COMMENT '升级结果',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.ota_upgrade_plan 结构
CREATE TABLE IF NOT EXISTS `ota_upgrade_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '计划id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `plan_status` enum('Start','Pause') NOT NULL DEFAULT 'Pause' COMMENT '启动Start 暂停Pause',
  `upgrade_type` enum('Push','Force') DEFAULT 'Push' COMMENT '升级方式  推送升级Push(默认) 强制升级Force',
  `target_version` varchar(32) DEFAULT NULL COMMENT '升级版本号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `edited_times` int(10) DEFAULT '0' COMMENT '编辑次数',
  `strategy_switch` tinyint(2) DEFAULT '0' COMMENT '策略开关 0:不使用 1：使用',
  `upgrade_scope` tinyint(2) DEFAULT '0' COMMENT '升级范围 0:全量升级 1：批次升级 2：指定uuid升级',
  PRIMARY KEY (`id`),
  UNIQUE KEY `productId_index` (`product_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.ota_upgrade_plan_detail 结构
CREATE TABLE IF NOT EXISTS `ota_upgrade_plan_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细id',
  `plan_id` bigint(20) NOT NULL COMMENT '升级计划id',
  `has_transition` tinyint(1) NOT NULL COMMENT '是否有过渡版本 true/false',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.ota_upgrade_plan_log 结构
CREATE TABLE IF NOT EXISTS `ota_upgrade_plan_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `plan_id` bigint(20) NOT NULL COMMENT '升级计划id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `operation_type` enum('Start','Pause','Edit') CHARACTER SET utf8mb4 NOT NULL COMMENT '操作类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.ota_upgrade_plan_substitute 结构
CREATE TABLE IF NOT EXISTS `ota_upgrade_plan_substitute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `detail_id` bigint(20) NOT NULL COMMENT '明细id',
  `substitute_version` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '替换版本号',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.ota_upgrade_plan_transition 结构
CREATE TABLE IF NOT EXISTS `ota_upgrade_plan_transition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `detail_id` bigint(20) NOT NULL COMMENT '明细id',
  `transition_version` varchar(32) NOT NULL COMMENT '过渡版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.product 结构
CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL,
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '设备类型id',
  `product_name` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `communication_mode` tinyint(4) DEFAULT NULL COMMENT '通信类型（0 WIFI 1 蓝牙 2 网关 3 IPC）',
  `transmission_mode` tinyint(4) DEFAULT NULL COMMENT '数据传输方式（定长<所有数据点一并上报>、变长<只上报有变化的数据点数据>）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `model` varchar(255) DEFAULT NULL COMMENT '产品型号',
  `config_net_mode` varchar(255) DEFAULT NULL COMMENT '配置网络方式',
  `is_kit` tinyint(1) DEFAULT '0' COMMENT '是否套包产品,1是,0否',
  `remark` text COMMENT '备注',
  `is_direct_device` tinyint(1) DEFAULT NULL COMMENT '是否直连设备0否、1是',
  `icon` varchar(255) DEFAULT NULL,
  `develop_status` tinyint(2) DEFAULT NULL COMMENT '开发状态,0:未开发,1:开发中,2:已上线,3:发布中；',
  `enterprise_develop_id` bigint(20) DEFAULT NULL COMMENT '企业开发者id',
  `step` tinyint(2) NOT NULL DEFAULT '0' COMMENT '产品步骤',
  `audit_status` tinyint(2) DEFAULT NULL COMMENT '0:未审核 1:审核未通过 2:审核通过',
  `service_goo_audit_status` tinyint(2) DEFAULT NULL COMMENT 'Google语音服务审核状态，0:未审核 1:审核未通过 2:审核通过',
  `service_alx_audit_status` tinyint(2) DEFAULT NULL COMMENT 'Aleax语音服务审核状态，0:未审核 1:审核未通过 2:审核通过',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `apply_audit_time` datetime DEFAULT NULL COMMENT '申请审核时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.product_back 结构
CREATE TABLE IF NOT EXISTS `product_back` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL,
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '设备类型id',
  `product_name` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `communication_mode` tinyint(4) DEFAULT NULL COMMENT '通信类型（0 WIFI 1 蓝牙 2 网关 3 IPC）',
  `transmission_mode` tinyint(4) DEFAULT NULL COMMENT '数据传输方式（定长<所有数据点一并上报>、变长<只上报有变化的数据点数据>）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `model` varchar(255) DEFAULT NULL COMMENT '产品型号',
  `config_net_mode` varchar(255) DEFAULT NULL COMMENT '配置网络方式',
  `is_kit` tinyint(1) DEFAULT '0' COMMENT '是否套包产品,1是,0否',
  `remark` text COMMENT '备注',
  `is_direct_device` tinyint(1) DEFAULT NULL COMMENT '是否直连设备0否、1是',
  `icon` varchar(255) DEFAULT NULL,
  `develop_status` tinyint(2) DEFAULT NULL COMMENT '开发状态,0:未开发,1:开发中,2:已上线,3:发布中；',
  `enterprise_develop_id` bigint(20) DEFAULT NULL COMMENT '企业开发者id',
  `step` tinyint(2) NOT NULL DEFAULT '0' COMMENT '产品步骤',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.product_config_netmode 结构
CREATE TABLE IF NOT EXISTS `product_config_netmode` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` bigint(20) NOT NULL COMMENT '产品Id',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品协议';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.product_data_point 结构
CREATE TABLE IF NOT EXISTS `product_data_point` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `data_point_id` bigint(20) DEFAULT NULL COMMENT '功能数据点id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_data_point` (`data_point_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.product_publish_history 结构
CREATE TABLE IF NOT EXISTS `product_publish_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品ID',
  `publish_time` datetime DEFAULT NULL COMMENT '产品发布时间',
  `publish_status` enum('Reviewing','Post Failure','Post Success') DEFAULT 'Reviewing' COMMENT '产品发布状态(Reviewing(默认);Post Failure;Post Success)',
  `failure_reason` varchar(500) DEFAULT NULL COMMENT '发布失败原因',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品发布历史表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.product_review_record 结构
CREATE TABLE IF NOT EXISTS `product_review_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `product_id` bigint(20) NOT NULL COMMENT '产品Id',
  `operate_time` datetime NOT NULL COMMENT '操作时间（申请时间，审核时间）',
  `operate_desc` varchar(500) DEFAULT NULL COMMENT '操作描述（提交审核，审核通过，审核不通过原因）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  `process_status` tinyint(2) DEFAULT '0' COMMENT '0:未审核 1:审核未通过 2:审核通过',
  PRIMARY KEY (`id`),
  KEY `index_tenant_id` (`tenant_id`) USING BTREE,
  KEY `index_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品审核记录';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.product_style_template 结构
CREATE TABLE IF NOT EXISTS `product_style_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `style_template_id` bigint(20) DEFAULT NULL COMMENT '样式模板id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.product_to_service_module 结构
CREATE TABLE IF NOT EXISTS `product_to_service_module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_tenant` (`product_id`,`tenant_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品对应模组表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.product_to_style 结构
CREATE TABLE IF NOT EXISTS `product_to_style` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `style_template_id` bigint(20) DEFAULT NULL COMMENT '样式模板id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品-样式表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.product_to_timer 结构
CREATE TABLE IF NOT EXISTS `product_to_timer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品ID',
  `timer_type` varchar(20) DEFAULT NULL COMMENT '定时方式',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品-配置定时方式';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.service_buy_record 结构
CREATE TABLE IF NOT EXISTS `service_buy_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `order_id` varchar(50) NOT NULL COMMENT '订单id，对应order_record表主键',
  `goods_id` bigint(20) NOT NULL COMMENT '商品id，对应goods_info主键',
  `goods_num` int(10) NOT NULL COMMENT '购买数量',
  `pay_status` tinyint(2) DEFAULT NULL COMMENT '支付状态（1：待付款；2：付款成功；3：付款失败； 4：退款中；5：退款成功；6：退款失败）',
  `service_id` bigint(20) NOT NULL COMMENT '虚拟服务id（产品Id，appId）',
  `add_demand_desc` varchar(500) DEFAULT NULL COMMENT '附加需求描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `goods_type_id` int(10) NOT NULL COMMENT '商品类别id，字典表：type_id=4',
  PRIMARY KEY (`id`),
  KEY `index_service` (`tenant_id`,`goods_id`,`service_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='虚拟服务购买记录表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.service_module 结构
CREATE TABLE IF NOT EXISTS `service_module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'parent_ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `version` varchar(32) NOT NULL COMMENT '版本',
  `name` varchar(32) NOT NULL COMMENT '模组名称',
  `code` varchar(32) DEFAULT NULL COMMENT '模组唯一标识code',
  `property_status` tinyint(2) DEFAULT NULL COMMENT '属性状态，0:必选,1:必选',
  `develop_status` tinyint(2) DEFAULT NULL COMMENT '开发状态,0:未开发,1:开发中,2:已上线',
  `test_case` varchar(500) DEFAULT NULL COMMENT '测试用例',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `img` varchar(255) DEFAULT NULL COMMENT 'icon',
  `change_img` varchar(255) DEFAULT NULL,
  `component_type` varchar(255) DEFAULT NULL COMMENT '功能组控件类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.service_module_action 结构
CREATE TABLE IF NOT EXISTS `service_module_action` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'parent_ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `version` varchar(32) DEFAULT NULL COMMENT '版本',
  `name` varchar(32) DEFAULT NULL COMMENT '方法名称',
  `code` varchar(100) DEFAULT NULL COMMENT '唯一标识code',
  `tags` varchar(100) DEFAULT NULL COMMENT '标签',
  `api_level` int(11) DEFAULT NULL COMMENT '等级',
  `develop_status` tinyint(2) DEFAULT NULL COMMENT '开发状态,0:未开发,1:开发中,2:已上线',
  `property_status` tinyint(2) DEFAULT NULL COMMENT '属性状态，0:可选,1:必选',
  `req_param_type` int(2) DEFAULT NULL COMMENT '请求参数格式：0 array ,1 object',
  `return_type` int(2) DEFAULT NULL COMMENT '返回参数格式：0 array ,1 object',
  `params` varchar(500) DEFAULT NULL COMMENT 'json参数集',
  `return_desc` varchar(500) DEFAULT NULL COMMENT '返回内容描述',
  `returns` varchar(500) DEFAULT NULL COMMENT '返回结果集',
  `test_case` varchar(500) DEFAULT NULL COMMENT '测试用例',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `ifttt_type` tinyint(2) DEFAULT '0' COMMENT 'ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)',
  `portal_ifttt_type` tinyint(2) DEFAULT '0' COMMENT 'portal联动设置(0:不支持 1：支持if 2:支持then 3:支持if支持then)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-方法表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.service_module_event 结构
CREATE TABLE IF NOT EXISTS `service_module_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'parent_ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `version` varchar(32) DEFAULT NULL COMMENT '版本',
  `name` varchar(32) DEFAULT NULL COMMENT '事件名称',
  `code` varchar(100) DEFAULT NULL COMMENT '唯一标识code',
  `api_level` int(11) DEFAULT NULL COMMENT 'api等级',
  `develop_status` tinyint(2) DEFAULT NULL COMMENT '开发状态,0:未开发,1:开发中,2:已上线',
  `property_status` tinyint(2) DEFAULT NULL COMMENT '属性状态，0:可选,1:必选',
  `params` varchar(500) DEFAULT NULL COMMENT 'json参数集',
  `tags` varchar(200) DEFAULT NULL COMMENT '标签',
  `test_case` varchar(500) DEFAULT NULL COMMENT '测试用例',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `ifttt_type` tinyint(2) DEFAULT '0' COMMENT 'ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)',
  `portal_ifttt_type` tinyint(2) DEFAULT '0' COMMENT 'portal联动设置(0:不支持 1：支持if 2:支持then 3:支持if支持then)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-事件表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.service_module_property 结构
CREATE TABLE IF NOT EXISTS `service_module_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'parent_ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) DEFAULT NULL COMMENT '模组id',
  `version` varchar(32) DEFAULT NULL COMMENT '版本',
  `name` varchar(32) DEFAULT NULL COMMENT '属性名称',
  `code` varchar(100) DEFAULT NULL COMMENT '唯一标识code',
  `tags` varchar(100) DEFAULT NULL COMMENT '标签',
  `api_level` int(11) DEFAULT NULL COMMENT 'api等级',
  `develop_status` tinyint(2) DEFAULT NULL COMMENT '开发状态,0:未开发,1:开发中,2:已上线',
  `req_param_type` int(2) DEFAULT NULL COMMENT '请求参数格式：0 array ,1 object',
  `return_type` int(2) DEFAULT NULL COMMENT '返回参数格式：0 array ,1 object',
  `property_status` tinyint(2) DEFAULT NULL COMMENT '属性状态，0:可选,1:必选',
  `rw_status` tinyint(2) DEFAULT NULL COMMENT '读写特征 0:可读可写,1:不可读不可写,2:可读不可写',
  `param_type` tinyint(2) DEFAULT NULL COMMENT '参数类型，0:int,1:float,2:bool,3:enum,4:string',
  `min_value` varchar(32) DEFAULT NULL COMMENT '最小值',
  `max_value` varchar(32) DEFAULT NULL COMMENT '最大值',
  `allowed_values` text COMMENT '允许值(多个值的json串)',
  `test_case` varchar(500) DEFAULT NULL COMMENT '测试用例',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `portal_ifttt_type` tinyint(2) DEFAULT '0' COMMENT 'portal联动设置(0:不支持 1：支持if 2:支持then 3:支持if支持then)',
  `ifttt_type` tinyint(2) DEFAULT '0' COMMENT 'ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)',
  `property_type` tinyint(2) DEFAULT '0' COMMENT 'property类型(0:property类型 1：参数类型)',
  `in_home_page` tinyint(2) DEFAULT '0' COMMENT '是否显示在首页',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-属性表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.service_module_style 结构
CREATE TABLE IF NOT EXISTS `service_module_style` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `name` varchar(32) NOT NULL COMMENT '模组样式名称',
  `code` varchar(32) DEFAULT NULL COMMENT '模组样式唯一标识code',
  `thumbnail` varchar(32) NOT NULL COMMENT '缩略图',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-样式表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.service_review_record 结构
CREATE TABLE IF NOT EXISTS `service_review_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `product_id` bigint(20) NOT NULL COMMENT '产品Id',
  `service_id` bigint(20) NOT NULL COMMENT '语音服务Id，goods表中对应的Id',
  `operate_time` datetime NOT NULL COMMENT '操作时间（申请时间，审核时间）',
  `operate_desc` varchar(500) DEFAULT NULL COMMENT '操作描述（提交审核，审核通过，审核不通过原因）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  `process_status` tinyint(2) DEFAULT '0' COMMENT '0:未审核 1:审核未通过 2:审核通过',
  PRIMARY KEY (`id`),
  KEY `index_tenant_id` (`tenant_id`) USING BTREE,
  KEY `index_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='语音接入服务审核记录';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.service_style_to_template 结构
CREATE TABLE IF NOT EXISTS `service_style_to_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
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

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.service_to_action 结构
CREATE TABLE IF NOT EXISTS `service_to_action` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `module_action_id` bigint(20) NOT NULL COMMENT '方法id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `status` tinyint(4) DEFAULT NULL COMMENT '0可选，1必选',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-to-方法表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.service_to_event 结构
CREATE TABLE IF NOT EXISTS `service_to_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `module_event_id` bigint(20) NOT NULL COMMENT '事件id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `status` tinyint(4) DEFAULT NULL COMMENT '0可选，1必选',
  PRIMARY KEY (`id`),
  KEY `idx_module_tenant` (`service_module_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-to-事件表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.service_to_property 结构
CREATE TABLE IF NOT EXISTS `service_to_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `service_module_id` bigint(20) NOT NULL COMMENT '模组id',
  `module_property_id` bigint(20) NOT NULL COMMENT '属性id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `status` tinyint(4) DEFAULT NULL COMMENT '0可选，1必选',
  PRIMARY KEY (`id`),
  KEY `idx_module_tenant` (`service_module_id`,`tenant_id`),
  KEY `idx_service_module_id` (`service_module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组-to-属性表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.smart_data_point 结构
CREATE TABLE IF NOT EXISTS `smart_data_point` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_point_id` bigint(20) DEFAULT NULL,
  `property_code` varchar(100) DEFAULT NULL,
  `smart_code` varchar(100) DEFAULT NULL,
  `smart` tinyint(1) DEFAULT NULL COMMENT '1alexa，2google',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `property_id` bigint(20) DEFAULT NULL COMMENT '模组-属性表 id(service_module_property.id)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.smart_device_type 结构
CREATE TABLE IF NOT EXISTS `smart_device_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_type_id` bigint(20) DEFAULT NULL,
  `smart_type` varchar(50) DEFAULT NULL COMMENT '第三方类型',
  `smart` tinyint(2) DEFAULT NULL COMMENT '1alexa，2google',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.style_template 结构
CREATE TABLE IF NOT EXISTS `style_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(32) NOT NULL COMMENT '样式名称',
  `code` varchar(255) DEFAULT NULL COMMENT '样式唯一标识code',
  `img` varchar(500) DEFAULT NULL COMMENT '图片',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `resource_link` varchar(255) DEFAULT NULL COMMENT '资源链接',
  `resource_link_validation` varchar(255) DEFAULT NULL COMMENT 'MD5效验',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='样式模板表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.technical_network_relate 结构
CREATE TABLE IF NOT EXISTS `technical_network_relate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `network_type_id` bigint(20) NOT NULL COMMENT '配网模式id（对应network_type表id）',
  `technical_scheme_id` bigint(20) NOT NULL COMMENT '技术方案id（对应goods_info表type_id=2）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='技术方案支持配网模式关联关系表（boss维护）';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.user_device_bak 结构
CREATE TABLE IF NOT EXISTS `user_device_bak` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id(bigint)',
  `device_id` varchar(32) NOT NULL COMMENT '设备id',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `user_type` varchar(10) DEFAULT NULL COMMENT '用户类型（root：主账号sub: 子账号）',
  `password` varchar(50) DEFAULT NULL COMMENT '用户访问设备秘钥',
  `event_notify_enabled` tinyint(1) DEFAULT '0' COMMENT '事件通知使能（0：开启，1：关闭）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`device_id`,`user_id`),
  UNIQUE KEY `unique_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-设备关系表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.uuid_apply_record 结构
CREATE TABLE IF NOT EXISTS `uuid_apply_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `order_id` varchar(50) NOT NULL COMMENT '订单id，对应order_record表主键',
  `down_num` int(10) NOT NULL DEFAULT '0' COMMENT '下载次数',
  `create_num` int(10) NOT NULL COMMENT '生成数量',
  `goods_id` bigint(20) NOT NULL COMMENT '商品id，对应goods_info主键（主要用于做方案条件搜索）',
  `uuid_apply_status` tinyint(2) NOT NULL COMMENT 'uuid申请状态（0:待处理；1:处理中;2:已完成;3:生成失败;4: P2PID不足）',
  `pay_status` tinyint(2) DEFAULT NULL COMMENT '支付状态（1：待付款；2：付款成功；3：付款失败； 4：退款中；5：退款成功；6：退款失败）',
  `file_id` varchar(32) DEFAULT NULL COMMENT 'UUID列表zip文件ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `uuid_validity_year` tinyint(4) NOT NULL DEFAULT '8' COMMENT '有效时长，单位：年(默认8年)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='UUID申请记录表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.weekly_electricity_statistics 结构
CREATE TABLE IF NOT EXISTS `weekly_electricity_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id(bigint)',
  `device_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '设备id',
  `electric_value` double(20,3) DEFAULT '0.000' COMMENT '电量值',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `week` int(11) DEFAULT NULL COMMENT '周',
  `year` year(4) DEFAULT NULL COMMENT '年',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_device.weekly_runtime 结构
CREATE TABLE IF NOT EXISTS `weekly_runtime` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id(bigint)',
  `device_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '设备id',
  `runtime` bigint(20) DEFAULT '0' COMMENT '运行时间',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `week` int(11) DEFAULT NULL COMMENT '周',
  `year` year(4) DEFAULT NULL COMMENT '年',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
