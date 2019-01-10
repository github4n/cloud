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

-- 导出 iot_db_boss 的数据库结构
CREATE DATABASE IF NOT EXISTS `iot_db_boss` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iot_db_boss`;


-- 导出  表 iot_db_boss.malf_attendance_timer 结构
CREATE TABLE IF NOT EXISTS `malf_attendance_timer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '定时器id',
  `timer_name` varchar(100) DEFAULT NULL COMMENT '定时器名称',
  `execution_cycle` tinyint(2) DEFAULT NULL COMMENT '执行周期 0~6(代表周日至周六)',
  `start_time` varchar(10) DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(10) DEFAULT NULL COMMENT '结束时间',
  `admin_id` bigint(20) DEFAULT NULL COMMENT '管理员ID',
  `timer_status` tinyint(2) DEFAULT NULL COMMENT '定时器状态 0：有效 1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员值班表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_boss.malf_proces_log 结构
CREATE TABLE IF NOT EXISTS `malf_proces_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流程记录id',
  `malf_id` bigint(20) NOT NULL COMMENT '报障单号',
  `handle_type` tinyint(2) DEFAULT NULL COMMENT '处理类型  0：分派完毕 1：确认故障  2：处理完毕 3：确认已修复 4：确认未修复 5：确认是问题  ',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_msg` varchar(1000) DEFAULT NULL COMMENT '处理人留言',
  `handle_admin_id` bigint(20) DEFAULT NULL COMMENT '处理人ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报障处理流程记录表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_boss.malf_record 结构
CREATE TABLE IF NOT EXISTS `malf_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) NOT NULL COMMENT '报障单关联的用户ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '报障单关联的用户所属租户id',
  `malf_desc` varchar(1000) DEFAULT NULL COMMENT '报障描述',
  `malf_status` tinyint(2) DEFAULT NULL COMMENT '报障处理进度 0：及时 1：延迟  2：严重延迟',
  `malf_rank` tinyint(2) DEFAULT NULL COMMENT '报障等级 0：非问题 1：轻度 2：普通 3：严重',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `confirm_time` datetime DEFAULT NULL COMMENT '管理员确认时间',
  `is_allocated` tinyint(2) DEFAULT NULL COMMENT '是否已经被分配给普通管理员 0：否 1：是',
  `handle_status` tinyint(2) DEFAULT NULL COMMENT '处理状态 0：创建 1：处理中 2：处理完成 3：修复完毕',
  `pre_handle_admin_id` bigint(20) DEFAULT NULL COMMENT '上一个处理人ID',
  `cur_handle_admin_id` bigint(20) DEFAULT NULL COMMENT '当前处理人ID',
  `handle_start_time` datetime DEFAULT NULL COMMENT '运维处理开始时间',
  `handle_end_time` datetime DEFAULT NULL COMMENT '运维处理结束时间',
  `renovate_time` datetime DEFAULT NULL COMMENT '修复时间',
  `is_rollback` tinyint(2) DEFAULT NULL COMMENT '是否超级管理员退回  0：否 1：是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报障计划表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_boss.system_user 结构
CREATE TABLE IF NOT EXISTS `system_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `admin_name` varchar(50) DEFAULT NULL COMMENT '管理员名称',
  `email` varchar(40) DEFAULT NULL COMMENT '邮箱',
  `nick_name` varchar(25) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `admin_no` varchar(20) DEFAULT NULL COMMENT '管理员编号',
  `admin_desc` varchar(200) DEFAULT NULL COMMENT '管理员描述',
  `password` varchar(50) DEFAULT NULL COMMENT '登录密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '注销时间',
  `admin_state` tinyint(4) DEFAULT NULL COMMENT '管理员状态 0-未激活，1-审核中，2-已认证，3-在线，4-离线，5-已冻结，6-已注销',
  `admin_type` tinyint(255) DEFAULT NULL COMMENT '管理员类型 0-超级管理员，1-普通管理员,2-运维人员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_boss.video_refund_log 结构
CREATE TABLE IF NOT EXISTS `video_refund_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `refund_id` bigint(20) NOT NULL COMMENT '订单退款表ID',
  `operator_id` bigint(20) NOT NULL COMMENT '操作者id（-1：表示paypal操作）',
  `refund_price` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `refund_remark` varchar(1000) DEFAULT NULL COMMENT '退款备注',
  `refund_status` tinyint(2) DEFAULT NULL COMMENT '退款状态(0-取消,1-申请退款中，2-退款成功,3-退款失败,4：审核通过，5：审核不通过)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退款操作日志';

-- 数据导出被取消选择。


-- 导出  表 iot_db_boss.video_refund_record 结构
CREATE TABLE IF NOT EXISTS `video_refund_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(36) NOT NULL COMMENT '订单id',
  `plan_id` varchar(32) NOT NULL COMMENT '计划id（对应video_plan的plan_id）',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `user_id` varchar(36) NOT NULL COMMENT '申请退款用户uuid',
  `refund_apply_id` bigint(20) DEFAULT NULL COMMENT '退款操作员ID',
  `refund_apply_name` varchar(100) DEFAULT NULL COMMENT '退款操作员名称',
  `refund_reason` varchar(1000) DEFAULT NULL COMMENT '退款原因',
  `refund_price` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
  `refund_apply_time` datetime DEFAULT NULL COMMENT '退款申请时间',
  `audit_id` bigint(20) DEFAULT NULL COMMENT '审批人ID',
  `audit_name` varchar(100) DEFAULT NULL COMMENT '审批人名称',
  `audit_message` varchar(1000) DEFAULT NULL COMMENT '审批留言',
  `audit_time` datetime DEFAULT NULL COMMENT '审批时间',
  `audit_status` tinyint(2) DEFAULT NULL COMMENT '审批状态(0：待审核；1、通过；2审核不通过)',
  `refund_status` tinyint(2) DEFAULT NULL COMMENT '退款状态(0-取消,1-申请退款中，2-退款成功,3-退款失败;4-paypal退款中)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退款申请记录';

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
