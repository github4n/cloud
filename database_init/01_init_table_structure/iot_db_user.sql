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

-- 导出 iot_db_user 的数据库结构
CREATE DATABASE IF NOT EXISTS `iot_db_user` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iot_db_user`;


-- 导出  表 iot_db_user.online_debug 结构
CREATE TABLE IF NOT EXISTS `online_debug` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键id',
  `uuid` varchar(50) NOT NULL COMMENT '用户uuid',
  `state` tinyint(2) NOT NULL COMMENT '用户debug权限状态 0关闭状态 1开启状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `uuid` (`uuid`) USING BTREE COMMENT '创建普通索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_user.smart_token 结构
CREATE TABLE IF NOT EXISTS `smart_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `smart` tinyint(1) DEFAULT NULL COMMENT '第三方类型(1:alexa,2:googleHome)',
  `access_token` text COMMENT '第三方的access_token',
  `refresh_token` text COMMENT '第三方的refresh_token',
  `update_time` datetime DEFAULT NULL,
  `local_access_token` varchar(1000) DEFAULT NULL COMMENT '本地生成的access_token',
  `local_refresh_token` varchar(1000) DEFAULT NULL COMMENT '本地生成的refresh_token',
  `third_party_info_id` bigint(20) DEFAULT NULL COMMENT '第三方的third_party_info.id',
  `client_id` varchar(255) DEFAULT NULL COMMENT '第三方的client_id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_user.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(50) DEFAULT '' COMMENT '昵称',
  `state` tinyint(2) DEFAULT NULL COMMENT '用户状态（0-未激活，1-已激活，2-在线，3-离线，4-已冻结，5-已注销）',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `uuid` varchar(50) DEFAULT NULL COMMENT '用户mqtt标识',
  `mqtt_password` varchar(30) DEFAULT NULL COMMENT 'mqtt登录密码',
  `tel` varchar(20) DEFAULT NULL COMMENT '联系方式',
  `head_img` varchar(255) DEFAULT NULL COMMENT '头像',
  `background` varchar(255) DEFAULT NULL COMMENT '背景图片',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `location_id` bigint(20) DEFAULT NULL COMMENT '区域ID',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `user_level` tinyint(2) DEFAULT '3' COMMENT '区分用户级别(1:BOSS;2:企业级客户;3:终端用户)',
  `admin_status` tinyint(2) DEFAULT NULL COMMENT '管理员标识(当user_level为1或2时，1：超级管理员;2:普通用户)',
  `company` varchar(50) DEFAULT NULL COMMENT '公司',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `user_status` enum('unreviewed','auditFailed','normal','deleted') DEFAULT 'normal' COMMENT '用户状态（未审核:unreviewed，审核未通过:auditFailed，正常:normal，已删除:deleted）',
  PRIMARY KEY (`id`),
  KEY `idx_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_user.user_feedback 结构
CREATE TABLE IF NOT EXISTS `user_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户主键',
  `feedback_content` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。


-- 导出  表 iot_db_user.user_feedback_fileid 结构
CREATE TABLE IF NOT EXISTS `user_feedback_fileid` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `tenant_id` bigint(20) NOT NULL COMMENT '用户的tenantId',
  `file_id` varchar(500) NOT NULL COMMENT '用户反馈上传的文件id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。


-- 导出  表 iot_db_user.user_log 结构
CREATE TABLE IF NOT EXISTS `user_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_name` varchar(255) NOT NULL COMMENT '用户注册名',
  `uuid` varchar(50) NOT NULL COMMENT '用户uuid',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `accept` tinyint(4) DEFAULT NULL COMMENT '接受隐私条款(1:同意，0:不同意）',
  `accept_time` datetime DEFAULT NULL COMMENT '接受隐私条款的时间',
  `cancel` tinyint(4) DEFAULT NULL COMMENT '注销账户(1:注销，0:未注销）',
  `cancel_time` datetime DEFAULT NULL COMMENT '注销账户时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户接受隐私条款和注销历史表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_user.user_login 结构
CREATE TABLE IF NOT EXISTS `user_login` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户主键',
  `last_ip` varchar(20) DEFAULT NULL COMMENT '最后登录IP',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `phone_id` varchar(200) DEFAULT NULL COMMENT '手机唯一标识',
  `os` varchar(20) DEFAULT NULL COMMENT '手机操作系统(1，iOS 2，Android)',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
