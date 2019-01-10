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

-- 导出 iot_db_message 的数据库结构
CREATE DATABASE IF NOT EXISTS `iot_db_message` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iot_db_message`;


-- 导出  表 iot_db_message.app_cert_info 结构
CREATE TABLE IF NOT EXISTS `app_cert_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `app_id` bigint(20) NOT NULL COMMENT 'APP应用id',
  `cert_info` blob NOT NULL COMMENT '证书信息',
  `cert_pass_word` varchar(100) NOT NULL COMMENT '证书密码',
  `service_host` varchar(50) NOT NULL COMMENT '服务主机',
  `service_port` int(5) NOT NULL COMMENT '服务端口',
  `android_push_key` varchar(200) NOT NULL COMMENT '安卓推送秘钥',
  `android_push_url` varchar(100) NOT NULL COMMENT '安卓推送链接',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `data_status` varchar(10) DEFAULT NULL COMMENT '数据状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_message.push_notice_log 结构
CREATE TABLE IF NOT EXISTS `push_notice_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '推送id',
  `push_from` varchar(50) DEFAULT NULL COMMENT '发信方',
  `push_to` varchar(200) DEFAULT NULL COMMENT '收信方',
  `push_type` varchar(5) DEFAULT NULL COMMENT '推送类型',
  `push_time` datetime DEFAULT NULL COMMENT '推送时间',
  `template_id` varchar(20) DEFAULT NULL COMMENT '模板id',
  `notice_subject` varchar(100) DEFAULT NULL COMMENT '主题',
  `param_value` varchar(1000) DEFAULT NULL COMMENT '推送内容',
  `result_code` varchar(10) DEFAULT NULL COMMENT '推送结果代码',
  `result_type` varchar(5) DEFAULT NULL COMMENT '推送结果类型',
  `result_message` varchar(1000) DEFAULT NULL COMMENT '推送结果信息',
  `result_answer_time` datetime DEFAULT NULL COMMENT '结果应答时间',
  `data_status` varchar(10) DEFAULT NULL COMMENT '数据状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_message.push_notice_template 结构
CREATE TABLE IF NOT EXISTS `push_notice_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `template_id` varchar(20) DEFAULT NULL COMMENT '模板id',
  `template_name` varchar(100) DEFAULT NULL COMMENT '模板名称',
  `template_content` varchar(1000) DEFAULT NULL COMMENT '模板内容',
  `template_type` varchar(10) DEFAULT NULL COMMENT '模板类型',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_delete` varchar(10) DEFAULT NULL COMMENT '是否已删除',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_message.system_push_control 结构
CREATE TABLE IF NOT EXISTS `system_push_control` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` bigint(20) NOT NULL COMMENT 'APP应用id',
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户id',
  `switch` enum('on','off') DEFAULT 'on' COMMENT '推送开关（on or off）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统推送控制表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_message.tenant_mail_info 结构
CREATE TABLE IF NOT EXISTS `tenant_mail_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `mail_host` varchar(100) NOT NULL COMMENT '邮件服务主机Host',
  `mail_name` varchar(100) NOT NULL COMMENT '邮箱用户名',
  `pass_word` varchar(100) NOT NULL COMMENT '邮箱密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `data_status` varchar(10) DEFAULT NULL COMMENT '数据状态',
  `mail_port` int(5) NOT NULL COMMENT '邮箱端口',
  `app_id` bigint(20) NOT NULL COMMENT 'APPid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_message.user_phone_relate 结构
CREATE TABLE IF NOT EXISTS `user_phone_relate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `phone_id` varchar(200) DEFAULT NULL COMMENT '手机id',
  `phone_type` varchar(5) DEFAULT NULL COMMENT '手机类型(1-IOS；2-android)',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  `data_status` varchar(10) DEFAULT NULL COMMENT '数据状态',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `app_id` bigint(20) DEFAULT NULL COMMENT 'APP应用id',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
