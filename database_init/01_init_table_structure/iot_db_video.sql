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

-- 导出 iot_db_video 的数据库结构
CREATE DATABASE IF NOT EXISTS `iot_db_video` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iot_db_video`;


-- 导出  表 iot_db_video.order_goods_mid 结构
CREATE TABLE IF NOT EXISTS `order_goods_mid` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单商品id',
  `order_id` varchar(50) NOT NULL COMMENT '订单id，order_record主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `num` int(10) NOT NULL COMMENT '购买数量',
  `goods_id` bigint(10) NOT NULL COMMENT '原商品id,关联goods_info表',
  `goods_name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `goods_standard` varchar(50) DEFAULT NULL COMMENT '商品规格（描述商品某种属性）',
  `goods_standard_unit` int(10) DEFAULT NULL COMMENT '商品规格单位（比如时间期限：年/月等等，对应字典表sys_dict_item的type_id=1的记录）',
  `goods_price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `goods_currency` varchar(10) NOT NULL COMMENT '货币单位',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_video.report_cash_flow 结构
CREATE TABLE IF NOT EXISTS `report_cash_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '金流id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `income` decimal(12,2) DEFAULT NULL COMMENT '金流收入',
  `expense` decimal(12,2) DEFAULT NULL COMMENT '金流支出',
  `profit` decimal(12,2) DEFAULT NULL COMMENT '金流盈利',
  `active_count` decimal(10,0) DEFAULT NULL COMMENT '激活量',
  `inactive_count` decimal(10,0) DEFAULT NULL COMMENT '未激活量',
  `storage_fee` decimal(12,2) DEFAULT NULL COMMENT '存储费用',
  `flow_fee` decimal(12,2) DEFAULT NULL COMMENT '流量费用',
  `directive_fee` decimal(12,2) DEFAULT NULL COMMENT '指令调用费用',
  `storage` decimal(12,2) DEFAULT NULL COMMENT '存储空间',
  `flow` decimal(12,2) DEFAULT NULL COMMENT '流量',
  `put_directive_call` decimal(12,0) DEFAULT NULL COMMENT 'put指令调用次数',
  `get_directive_call` decimal(12,0) DEFAULT NULL COMMENT 'get指令调用次数',
  `post_directive_call` decimal(12,0) DEFAULT NULL COMMENT 'post指令调用次数',
  `calculate_fee` decimal(12,2) DEFAULT NULL COMMENT '计算费用',
  `calculate_count` decimal(12,0) DEFAULT NULL COMMENT '计算次数',
  `statistic_time` datetime DEFAULT NULL COMMENT '统计时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 iot_db_video.report_device_log_day 结构
CREATE TABLE IF NOT EXISTS `report_device_log_day` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_id` bigint(20) DEFAULT NULL COMMENT '设备表id',
  `log_date` datetime DEFAULT NULL COMMENT '日志日期',
  `day_online_hour` decimal(6,2) DEFAULT NULL COMMENT '在线时长',
  `device_type` varchar(5) COLLATE utf8_bin DEFAULT NULL COMMENT '设备类型',
  `day_bright_hour` decimal(6,2) DEFAULT NULL COMMENT '使用时长',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 iot_db_video.report_device_log_month 结构
CREATE TABLE IF NOT EXISTS `report_device_log_month` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_id` bigint(20) DEFAULT NULL COMMENT '设备表id',
  `log_date` datetime DEFAULT NULL COMMENT '日志日期',
  `month_online_hour` decimal(6,2) DEFAULT NULL COMMENT '在线时长',
  `device_type` varchar(5) COLLATE utf8_bin DEFAULT NULL COMMENT '设备类型',
  `month_bright_hour` decimal(6,2) DEFAULT NULL COMMENT '使用时长',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 iot_db_video.report_overspend 结构
CREATE TABLE IF NOT EXISTS `report_overspend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '超支id',
  `user_id` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '用户id',
  `trigger_time` datetime DEFAULT NULL COMMENT '触发时间',
  `plan_id` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '计划id',
  `tenant_id` varchar(6) COLLATE utf8_bin DEFAULT NULL COMMENT '租户id',
  `overspend` decimal(10,2) DEFAULT NULL COMMENT '超支金额',
  `statistic_time` datetime DEFAULT NULL COMMENT '统计时间',
  `user_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  `plan_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '计划名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 iot_db_video.report_overspend_detail 结构
CREATE TABLE IF NOT EXISTS `report_overspend_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '超支详情id',
  `overspend_id` bigint(20) DEFAULT NULL COMMENT '超支id',
  `use_storage` decimal(12,2) DEFAULT NULL COMMENT '已用存储空间',
  `available_storage` decimal(12,2) DEFAULT NULL COMMENT '可用存储空间',
  `storage_overspend` decimal(12,2) DEFAULT NULL COMMENT '存储超支金额',
  `use_flow` decimal(12,2) DEFAULT NULL COMMENT '已用流量',
  `available_flow` decimal(12,2) DEFAULT NULL COMMENT '可用流量',
  `flow_overspend` decimal(12,2) DEFAULT NULL COMMENT '流量超支金额',
  `use_directive` decimal(10,0) DEFAULT NULL COMMENT '已用多少条指令',
  `available_directive` decimal(10,0) DEFAULT NULL COMMENT '可用多少条指令',
  `directive_overspend` decimal(12,2) DEFAULT NULL COMMENT '指令超支金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 iot_db_video.video_event_back 结构
CREATE TABLE IF NOT EXISTS `video_event_back` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '事件ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `plan_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '计划ID',
  `event_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '事件代码',
  `event_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '事件名称',
  `event_desc` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '事件描述',
  `event_oddur_time` datetime DEFAULT NULL COMMENT '事件发生时间',
  `event_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '事件状态',
  `event_uuid` varchar(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '事件UUID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `data_status` tinyint(2) DEFAULT '1' COMMENT '0-无效；1-有效'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_video.video_file_back 结构
CREATE TABLE IF NOT EXISTS `video_file_back` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '主键ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `plan_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '计划ID',
  `file_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '文件ID',
  `device_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '设备ID',
  `video_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '视频类型(0：全时录影，1：事件录影)',
  `video_start_time` datetime DEFAULT NULL COMMENT '视频开始时间',
  `video_end_time` datetime DEFAULT NULL COMMENT '视频结束时间',
  `file_exp_date` datetime DEFAULT NULL COMMENT '文件失效日期',
  `file_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '文件类型',
  `event_uuid` varchar(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '事件UUID',
  `video_length` decimal(10,2) DEFAULT NULL COMMENT '视频时长',
  `file_size` int(12) DEFAULT NULL COMMENT '文件大小',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `data_status` tinyint(2) DEFAULT '1' COMMENT '0-无效；1-有效'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_video.video_pay_record 结构
CREATE TABLE IF NOT EXISTS `video_pay_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `user_id` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '用户id',
  `order_id` varchar(36) COLLATE utf8_bin DEFAULT NULL COMMENT '订单id',
  `plan_id` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '计划id',
  `package_id` bigint(20) DEFAULT NULL COMMENT '套餐标识',
  `counts` int(10) DEFAULT NULL COMMENT '购买数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `plan_start_time` datetime DEFAULT NULL COMMENT '计划开始时间',
  `plan_end_time` datetime DEFAULT NULL COMMENT '计划结束时间',
  `plan_status` int(2) NOT NULL COMMENT '计划状态(定时扫描,1-使用中 2-到期 3-过期定时扫描需要分布式锁 4:退款，计划失效)',
  PRIMARY KEY (`id`),
  KEY `test_index` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 iot_db_video.video_plan 结构
CREATE TABLE IF NOT EXISTS `video_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plan_id` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '计划id',
  `plan_name` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `user_id` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '用户id',
  `package_id` bigint(20) DEFAULT NULL COMMENT '套餐id',
  `device_id` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '设备id',
  `plan_start_time` datetime DEFAULT NULL COMMENT '计划开始时间',
  `plan_end_time` datetime DEFAULT NULL COMMENT '计划结束时间',
  `plan_exec_status` int(2) DEFAULT NULL COMMENT '计划执行状态(0-计划关闭，1-计划开启，2-计划过期，3-计划失效)',
  `plan_cycle` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '执行周期({D:天,W:周,M:月})',
  `plan_status` int(2) DEFAULT NULL COMMENT '时程开关(0:停止,1:执行)',
  `plan_order` int(4) DEFAULT NULL COMMENT '排序字段',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `comedue_remind_state` varchar(10) COLLATE utf8_bin DEFAULT '0' COMMENT '到期提醒(0-未到期，1-计划临期，2-计划到期，3-计划过期)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 iot_db_video.video_task 结构
CREATE TABLE IF NOT EXISTS `video_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `user_id` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '用户id',
  `plan_id` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '计划id',
  `task_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '执行日期',
  `execute_start_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '执行开始时间',
  `execute_end_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '执行结束时间',
  `task_status` int(2) DEFAULT NULL COMMENT '计划状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
