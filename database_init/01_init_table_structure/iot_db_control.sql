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

-- 导出 iot_db_control 的数据库结构
CREATE DATABASE IF NOT EXISTS `iot_db_control` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iot_db_control`;


-- 导出  表 iot_db_control.activity_record 结构
CREATE TABLE IF NOT EXISTS `activity_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(16) DEFAULT NULL COMMENT '类型,DEVICE=设备,SCENE=情景,AUTO=使能,SECURITY=安防',
  `icon` varchar(128) DEFAULT NULL COMMENT '图标',
  `activity` varchar(512) DEFAULT NULL COMMENT '活动日志描述',
  `time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `foreign_id` varchar(32) DEFAULT NULL COMMENT '外部关联主键ID',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '假删除标识 0：正常 1：删除',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `org_id` bigint(20) DEFAULT NULL COMMENT '组织机构id',
  `device_name` varchar(50) DEFAULT NULL COMMENT '设备名称',
  `result` int(2) DEFAULT '0' COMMENT '结果 0=正常 1=异常',
  `set_time` varchar(32) DEFAULT NULL COMMENT '模板设置的执行时间',
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动日志记录';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.automation 结构
CREATE TABLE IF NOT EXISTS `automation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
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
  `dev_scene_id` int(11) DEFAULT NULL COMMENT 'ble设备对应的Id与dev_timer_id共同确认autoId',
  `dev_timer_id` int(11) DEFAULT NULL COMMENT 'ble设备对应的Id与dev_scene_id共同确认autoId',
  `visiable` int(2) DEFAULT '1' COMMENT '是否可视 0否 1是',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_space_id` (`space_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联动表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.automation_item 结构
CREATE TABLE IF NOT EXISTS `automation_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(30) DEFAULT NULL COMMENT '类型 dev/scene',
  `object_id` varchar(50) DEFAULT NULL COMMENT '关联id',
  `item_id` bigint(20) DEFAULT NULL COMMENT 'ifttt item主键',
  `applet_id` bigint(20) DEFAULT NULL COMMENT 'ifttt 程序主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联动子项';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.camera_record 结构
CREATE TABLE IF NOT EXISTS `camera_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `title` varchar(20) DEFAULT NULL,
  `time` varchar(32) DEFAULT NULL,
  `register` varchar(2) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.deployment 结构
CREATE TABLE IF NOT EXISTS `deployment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deploy_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.device_remote_control 结构
CREATE TABLE IF NOT EXISTS `device_remote_control` (
  `id` bigint(32) NOT NULL,
  `device_type_id` varchar(32) NOT NULL,
  `key_code` varchar(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL COMMENT 'GROUP 组控；SCENE 情景；SINGLE 单控',
  `default_value` varchar(200) DEFAULT NULL COMMENT '默认值',
  `event_status` varchar(20) DEFAULT NULL COMMENT 'pressed/released/held down  短按/长按释放/长按 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.group_info 结构
CREATE TABLE IF NOT EXISTS `group_info` (
  `id` bigint(20) NOT NULL,
  `group_id` varchar(32) DEFAULT NULL COMMENT '分组ID',
  `name` varchar(50) DEFAULT NULL COMMENT '分组名称',
  `gateway_id` varchar(32) DEFAULT NULL COMMENT '直连设备ID',
  `model` varchar(50) DEFAULT NULL,
  `remote_id` varchar(32) DEFAULT NULL COMMENT '遥控器ID',
  PRIMARY KEY (`id`),
  KEY `goupIdIndex` (`group_id`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.ifttt_actuator 结构
CREATE TABLE IF NOT EXISTS `ifttt_actuator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `label` varchar(100) DEFAULT NULL COMMENT '标签',
  `properties` varchar(1000) DEFAULT NULL COMMENT '属性值',
  `device_id` varchar(32) DEFAULT NULL COMMENT '设备uuid',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则主键',
  `position` varchar(100) DEFAULT NULL COMMENT '坐标位置',
  `device_type` varchar(100) DEFAULT NULL COMMENT '设备类型，如：temperatureSensor',
  `idx` int(10) DEFAULT NULL COMMENT '顺序号',
  `type` varchar(30) DEFAULT NULL COMMENT '类型： dev timer scene auto',
  `delay` int(10) DEFAULT '0' COMMENT '延时执行，单位秒',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品主键',
  `object_id` varchar(64) DEFAULT NULL COMMENT '外部对象id,视type内容而定',
  PRIMARY KEY (`id`),
  KEY `idx_actuator_rule_id_label` (`rule_id`,`label`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联动-执行';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.ifttt_relation 结构
CREATE TABLE IF NOT EXISTS `ifttt_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `label` varchar(255) DEFAULT NULL COMMENT '标签',
  `type` varchar(20) DEFAULT NULL COMMENT '类型: OR AND',
  `parent_labels` varchar(1000) DEFAULT NULL COMMENT '关联标签名数组',
  `combinations` varchar(1000) DEFAULT NULL COMMENT '联合结果数组',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则主键',
  `position` varchar(255) DEFAULT NULL COMMENT '坐标位置',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`),
  KEY `idx_relation_rule_id` (`rule_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联动-关联';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.ifttt_rule 结构
CREATE TABLE IF NOT EXISTS `ifttt_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `type` varchar(30) DEFAULT NULL COMMENT '场景类型： dev timer sunrise sunset  template',
  `status` tinyint(2) DEFAULT '0' COMMENT '启用状态 0=停止 1=启动',
  `is_multi` tinyint(2) DEFAULT '0' COMMENT '是否多网关 0：否 1：是',
  `location_id` bigint(20) DEFAULT NULL COMMENT '区域主键',
  `space_id` bigint(20) DEFAULT NULL COMMENT '空间主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `direct_id` varchar(32) DEFAULT NULL COMMENT '单直连设备时，直连设备主键',
  `org_id` bigint(20) DEFAULT NULL COMMENT '组织主键',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `template_flag` tinyint(2) DEFAULT '0' COMMENT '模板标识 0=否 1=是',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品主键',
  `rule_type` tinyint(2) DEFAULT '0' COMMENT '规则类型 0=普通 1=安防',
  `security_type` varchar(20) DEFAULT NULL COMMENT '安防类型， stay,away',
  `delay` int(11) DEFAULT '0' COMMENT '安防联动延时生效时间,单位秒',
  `template_id` bigint(20) DEFAULT NULL COMMENT '模板ID',
  `ifttt_type` varchar(8) DEFAULT NULL COMMENT 'ifttt类型 "_2B"  "_2C"',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_location_id_status` (`location_id`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联动-规则';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.ifttt_sensor 结构
CREATE TABLE IF NOT EXISTS `ifttt_sensor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `label` varchar(100) DEFAULT NULL COMMENT '标签',
  `properties` varchar(512) DEFAULT NULL COMMENT '属性',
  `device_id` varchar(32) DEFAULT NULL COMMENT '设备uuid',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则主键',
  `position` varchar(255) DEFAULT NULL COMMENT '坐标位置',
  `device_type` varchar(64) DEFAULT NULL COMMENT '设备类型，如：hubLight',
  `timing` varchar(512) DEFAULT NULL COMMENT '时间设置',
  `idx` int(10) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL COMMENT '触发类型： dev/timer/weather/people',
  `delay` int(10) DEFAULT NULL COMMENT '触发延时，单位秒',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品主键',
  PRIMARY KEY (`id`),
  KEY `idx_sensor_rule_id_label` (`rule_id`,`label`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联动-传感器';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.ifttt_trigger 结构
CREATE TABLE IF NOT EXISTS `ifttt_trigger` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `line_id` varchar(255) DEFAULT NULL COMMENT '线条标识',
  `source_label` varchar(64) DEFAULT NULL COMMENT '源标签',
  `start` bigint(20) DEFAULT NULL COMMENT '页面元素起点',
  `destination_label` varchar(64) DEFAULT NULL COMMENT '目标标签',
  `end` bigint(20) DEFAULT NULL COMMENT '页面元素终点',
  `invocation_policy` int(11) DEFAULT NULL COMMENT '调用频率',
  `status_trigger` varchar(255) DEFAULT NULL COMMENT '触发流程的判断状态，多个用逗号间隔',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`),
  KEY `idx_trigger_rule_id` (`rule_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联动-触发器';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.index_content 结构
CREATE TABLE IF NOT EXISTS `index_content` (
  `id` bigint(20) NOT NULL,
  `type` int(1) DEFAULT NULL COMMENT '类型 0背景图 1小地图 2 build',
  `image` varchar(30) DEFAULT NULL,
  `top` int(10) DEFAULT NULL,
  `left` int(10) DEFAULT NULL,
  `width` int(10) DEFAULT NULL,
  `build_id` int(20) DEFAULT NULL,
  `location_id` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.location 结构
CREATE TABLE IF NOT EXISTS `location` (
  `id` bigint(32) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `icon` varchar(64) DEFAULT NULL,
  `tenant_id` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_update_date` datetime DEFAULT NULL,
  `create_by` varchar(32) DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.location_scene 结构
CREATE TABLE IF NOT EXISTS `location_scene` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `del_flag` tinyint(2) DEFAULT NULL COMMENT '删除标志',
  `tenant_id` bigint(20) DEFAULT NULL,
  `location_id` bigint(20) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.location_scene_detail 结构
CREATE TABLE IF NOT EXISTS `location_scene_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `location_scene_id` bigint(20) DEFAULT NULL,
  `scene_id` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `del_flag` tinyint(2) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.location_scene_relation 结构
CREATE TABLE IF NOT EXISTS `location_scene_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location_scene_id` int(11) DEFAULT NULL,
  `location_id` int(11) DEFAULT NULL,
  `start_cron` varchar(32) DEFAULT NULL,
  `end_cron` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.online_status_record 结构
CREATE TABLE IF NOT EXISTS `online_status_record` (
  `id` varchar(36) DEFAULT NULL COMMENT '用户ID或设备ID',
  `status` varchar(20) DEFAULT 'disconnected' COMMENT '上线状态（''disconnected'',''connected''）',
  `type` varchar(20) DEFAULT NULL COMMENT '类型（user:用户,device:设备）',
  `record_time` datetime DEFAULT NULL COMMENT '记录时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  KEY `idx_id_status` (`id`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='记录设备上下线状态';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.package 结构
CREATE TABLE IF NOT EXISTS `package` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `package_type` int(10) NOT NULL DEFAULT '2' COMMENT '套包類型(对应sys_dict_item的type_id=14)',
  `package_id` bigint(20) DEFAULT NULL COMMENT '选择套包id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='套包表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.package_device_type 结构
CREATE TABLE IF NOT EXISTS `package_device_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `package_id` bigint(20) NOT NULL COMMENT '套包id(对应package表主键)',
  `device_type_id` bigint(20) NOT NULL COMMENT '设备类型id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='套包支持设备类型表（boss维护）';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.package_product 结构
CREATE TABLE IF NOT EXISTS `package_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `package_id` bigint(20) NOT NULL COMMENT '套包id(对应package表主键)',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='套包支持产品表（portal维护）';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.region_hour_record 结构
CREATE TABLE IF NOT EXISTS `region_hour_record` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `ch1` bigint(10) DEFAULT NULL,
  `ch2` bigint(10) DEFAULT NULL,
  `ch3` bigint(10) DEFAULT NULL,
  `ch4` bigint(10) DEFAULT NULL,
  `time_flag` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.region_record 结构
CREATE TABLE IF NOT EXISTS `region_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ch1` bigint(10) DEFAULT NULL,
  `ch2` bigint(10) DEFAULT NULL,
  `ch3` bigint(10) DEFAULT NULL,
  `ch4` bigint(10) DEFAULT NULL,
  `time` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.reservation 结构
CREATE TABLE IF NOT EXISTS `reservation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `start_time` bigint(20) DEFAULT NULL COMMENT '预约开始时间',
  `end_time` bigint(20) DEFAULT NULL COMMENT '预约结束时间',
  `create_time` datetime DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `open_id` varchar(50) DEFAULT NULL,
  `space_id` bigint(20) DEFAULT NULL,
  `flag` int(10) unsigned DEFAULT '0' COMMENT '1已经确认预约',
  `tenant_id` int(11) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL COMMENT '会议类型',
  `model` bigint(20) DEFAULT NULL COMMENT '情景ID',
  `position` varchar(50) DEFAULT NULL COMMENT '前段界面的位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.scene 结构
CREATE TABLE IF NOT EXISTS `scene` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scene_name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `space_id` bigint(20) DEFAULT NULL COMMENT '空间id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `org_id` bigint(20) DEFAULT NULL COMMENT '组织机构id',
  `icon` varchar(20) DEFAULT NULL COMMENT '图标',
  `set_type` tinyint(1) DEFAULT NULL COMMENT '1.设备类型  2.全量设置 3.业务类型',
  `sort` tinyint(1) DEFAULT NULL COMMENT '排序',
  `upload_status` tinyint(1) DEFAULT NULL COMMENT '上传状态：是否同步网关',
  `location_id` bigint(20) DEFAULT NULL,
  `template_id` bigint(20) DEFAULT NULL COMMENT '模板ID',
  `dev_scene_id` bigint(20) DEFAULT NULL COMMENT '设备控制sceneId',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='情景';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.scene_detail 结构
CREATE TABLE IF NOT EXISTS `scene_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scene_id` bigint(20) DEFAULT NULL COMMENT '情景id',
  `device_id` varchar(255) DEFAULT NULL COMMENT '设备uuid',
  `space_id` bigint(20) DEFAULT NULL COMMENT '空间id',
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '设备类型id',
  `target_value` varchar(500) DEFAULT NULL COMMENT '目标值json格式',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `sort` int(10) DEFAULT NULL COMMENT '排序',
  `location_id` bigint(20) DEFAULT NULL,
  `method` varchar(100) DEFAULT NULL COMMENT '调用设备事件的方法',
  PRIMARY KEY (`id`),
  KEY `idx_scene_id` (`scene_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='情景详情表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.scene_info 结构
CREATE TABLE IF NOT EXISTS `scene_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `package_id` bigint(20) NOT NULL COMMENT '套包id(对应package表主键)',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id(-1则为boss创建的场景)',
  `scene_name` varchar(32) DEFAULT NULL COMMENT '场景名称',
  `json` varchar(1000) DEFAULT NULL COMMENT '目标值json格式',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='场景信息表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.security 结构
CREATE TABLE IF NOT EXISTS `security` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `space_id` bigint(20) DEFAULT NULL COMMENT '空间表id',
  `arm_mode` varchar(20) DEFAULT 'off' COMMENT '布置安防模式,off:撤防,stay:在家布防,away:离家布防,紧急呼叫:panic',
  `password` varchar(100) DEFAULT NULL COMMENT '安防密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `org_id` bigint(20) DEFAULT NULL COMMENT '组织机构id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_space_id` (`space_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='安防信息主表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.security_rule 结构
CREATE TABLE IF NOT EXISTS `security_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键id',
  `security_id` bigint(20) DEFAULT NULL COMMENT '安防主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `type` varchar(20) DEFAULT NULL COMMENT '安防类型：stay、away、panic',
  `enabled` int(2) DEFAULT NULL COMMENT '安防使能开关，0禁用1启用',
  `defer` int(20) DEFAULT NULL COMMENT '安防联动延时生效',
  `delay` int(20) DEFAULT NULL COMMENT '延时报警',
  `if` text COMMENT 'if条件配置',
  `then` text COMMENT 'then条件配置',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='安防规则表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.sence_info 结构
CREATE TABLE IF NOT EXISTS `sence_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `package_id` bigint(20) NOT NULL COMMENT '套包id(对应package表主键)',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id(-1则为boss创建的场景)',
  `sence_name` varchar(32) DEFAULT NULL COMMENT '场景名称',
  `json` varchar(1000) DEFAULT NULL COMMENT '目标值json格式',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='场景信息表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.space 结构
CREATE TABLE IF NOT EXISTS `space` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `position` varchar(5000) DEFAULT NULL COMMENT '坐标位置',
  `name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT '-1' COMMENT '上级ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `location_id` bigint(20) DEFAULT NULL COMMENT '顶级区域ID',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `type` enum('GROUP','ROOM','FLOOR','HOME','BUILD') DEFAULT NULL COMMENT '空间类型',
  `sort` int(2) DEFAULT NULL COMMENT '楼层排序',
  `style` text COMMENT '空间样式属性',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `default_space` int(2) DEFAULT '0' COMMENT '是否默认空间标识 0：否  1：是',
  `people_num` varchar(10) DEFAULT NULL,
  `org_id` bigint(10) DEFAULT NULL COMMENT '组织ID',
  `model` int(1) unsigned zerofill DEFAULT '0' COMMENT '0 普通房间 1会议室',
  `time_zone_offset` int(8) DEFAULT '-480' COMMENT '时区偏移量 单位分钟',
  `seq` int(8) DEFAULT '0' COMMENT '空间下设备序列号',
  `deploy_id` bigint(20) DEFAULT NULL COMMENT '部署方式',
  `mesh_name` varchar(255) DEFAULT NULL,
  `mesh_password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `dix_s_id_pid` (`id`,`parent_id`) USING BTREE,
  KEY `index_parent_id` (`parent_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='空间表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.space_backgroud_device_pos 结构
CREATE TABLE IF NOT EXISTS `space_backgroud_device_pos` (
  `id` bigint(20) DEFAULT NULL,
  `background_img_id` bigint(20) DEFAULT NULL,
  `space_id` bigint(20) DEFAULT NULL,
  `device_id` bigint(20) DEFAULT NULL,
  `device_pos` varchar(32) DEFAULT NULL COMMENT '设备坐标',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.space_background_img 结构
CREATE TABLE IF NOT EXISTS `space_background_img` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `space_id` bigint(20) DEFAULT NULL,
  `bg_img` varchar(64) DEFAULT NULL COMMENT '背景图',
  `thumbnail_img` varchar(64) DEFAULT NULL COMMENT '缩略图',
  `view_img` varchar(64) DEFAULT NULL COMMENT '视角小图片',
  `thumbnail_pos` varchar(32) DEFAULT NULL COMMENT '缩略图坐标',
  `view_pos` varchar(32) DEFAULT NULL COMMENT '视角小图片坐标',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.space_device 结构
CREATE TABLE IF NOT EXISTS `space_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `device_id` varchar(255) DEFAULT NULL COMMENT '设备uuid',
  `space_id` bigint(20) DEFAULT NULL COMMENT '空间ID',
  `location_id` bigint(20) DEFAULT NULL COMMENT '顶级区域ID',
  `device_category_id` bigint(20) DEFAULT NULL COMMENT '产品类型',
  `status` varchar(10) DEFAULT NULL COMMENT '房间挂载状态',
  `position` varchar(100) DEFAULT NULL COMMENT '设备位置坐标',
  `tenant_id` bigint(20) DEFAULT NULL,
  `device_type_id` bigint(20) DEFAULT NULL,
  `order` int(10) DEFAULT '1' COMMENT '排序',
  `business_type_id` bigint(20) DEFAULT NULL COMMENT '业务类型ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  PRIMARY KEY (`id`),
  KEY `index_space_id` (`space_id`),
  KEY `idx_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房间设备关联表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.space_template 结构
CREATE TABLE IF NOT EXISTS `space_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `space_id` bigint(20) DEFAULT NULL,
  `template_id` bigint(20) DEFAULT NULL,
  `location_id` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `template_type` varchar(32) DEFAULT NULL COMMENT '模板类型  SCENE   IFTTT SCENE_TEMPLATE',
  `start_cron` varchar(32) DEFAULT NULL COMMENT 'quartz cron 表达式',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(32) DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `loop_type` varchar(4) DEFAULT NULL COMMENT '0: 没有设置具体执行星期 1：有设置具体执行星期',
  `properties` varchar(512) DEFAULT NULL COMMENT '定时时间设置',
  `end_cron` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.template 结构
CREATE TABLE IF NOT EXISTS `template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品表id',
  `location_id` bigint(20) DEFAULT NULL COMMENT '区域主键',
  `space_id` bigint(20) DEFAULT NULL COMMENT 'space主键',
  `name` varchar(32) DEFAULT NULL COMMENT '模板名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `template_type` varchar(30) DEFAULT NULL COMMENT '模板类型,套包:kit,情景:scene,ifttt:ifttt',
  `deploy_id` bigint(20) DEFAULT NULL COMMENT '部署方式ID',
  PRIMARY KEY (`id`),
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板主表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.template_detail 结构
CREATE TABLE IF NOT EXISTS `template_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `template_id` bigint(20) DEFAULT NULL COMMENT '模板id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品表id',
  `device_category_id` bigint(20) DEFAULT NULL COMMENT '设备分类id',
  `device_type_id` bigint(20) DEFAULT NULL COMMENT '设备类型id',
  `target_value` varchar(500) DEFAULT NULL COMMENT '目标值json格式',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `business_type_id` bigint(20) DEFAULT NULL COMMENT '业务类型ID',
  PRIMARY KEY (`id`),
  KEY `id` (`id`) USING BTREE,
  KEY `template_id` (`template_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='情景详情模板表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.template_ifttt 结构
CREATE TABLE IF NOT EXISTS `template_ifttt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `template_id` bigint(20) DEFAULT NULL COMMENT '商品套包模板id',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '联动-规则id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者id(管理员)',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者id(管理员)',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联动-规则的模板表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.template_rule 结构
CREATE TABLE IF NOT EXISTS `template_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `package_id` bigint(20) DEFAULT NULL COMMENT '套包主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `type` int(2) DEFAULT NULL COMMENT '模板类型 0:安防 1:scene 2:ifttt;3:策略',
  `json` varchar(1000) DEFAULT NULL COMMENT '规则体',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `rule_name` varchar(32) DEFAULT NULL COMMENT '规则名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板规则表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.user_device 结构
CREATE TABLE IF NOT EXISTS `user_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `device_id` varchar(32) NOT NULL COMMENT '设备id',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `user_type` varchar(10) DEFAULT NULL COMMENT '用户类型（root：主账号sub: 子账号）',
  `password` varchar(50) DEFAULT NULL COMMENT '用户访问设备秘钥',
  `event_notify_enabled` tinyint(1) DEFAULT '0' COMMENT '事件通知使能（0：开启，1：关闭）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-设备关系表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_control.warning 结构
CREATE TABLE IF NOT EXISTS `warning` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(32) NOT NULL COMMENT '设备ID',
  `type` varchar(50) DEFAULT NULL COMMENT '告警类型',
  `content` varchar(500) DEFAULT NULL COMMENT '告警内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` varchar(10) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `location_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='告警内容';

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
