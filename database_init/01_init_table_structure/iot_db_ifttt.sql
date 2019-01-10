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

-- 导出 iot_db_ifttt 的数据库结构
CREATE DATABASE IF NOT EXISTS `iot_db_ifttt` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iot_db_ifttt`;


-- 导出  表 iot_db_ifttt.applet 结构
CREATE TABLE IF NOT EXISTS `applet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `status` enum('on','off') NOT NULL COMMENT '状态 0on  1off',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用程序表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_ifttt.applet_item 结构
CREATE TABLE IF NOT EXISTS `applet_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `applet_id` bigint(20) NOT NULL COMMENT '程序主键',
  `type` varchar(10) NOT NULL COMMENT '规则类型 this/that',
  `event_id` bigint(20) NOT NULL COMMENT 'this/that主键',
  `json` varchar(16000) NOT NULL COMMENT '规则体',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='子规则表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_ifttt.applet_relation 结构
CREATE TABLE IF NOT EXISTS `applet_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `applet_id` bigint(20) NOT NULL COMMENT '程序主键',
  `relation_key` varchar(100) NOT NULL COMMENT '关联key devId/sunny/spaceId等',
  `type` enum('devStatus','sunny','space') NOT NULL COMMENT '类型 1 dev_status 2 sunny 3 space',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='程序关联表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_ifttt.applet_that 结构
CREATE TABLE IF NOT EXISTS `applet_that` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `applet_id` bigint(20) NOT NULL COMMENT '程序主键',
  `service_code` varchar(50) NOT NULL COMMENT '服务标识',
  `code` varchar(50) NOT NULL COMMENT '触发码',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='that组表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_ifttt.applet_this 结构
CREATE TABLE IF NOT EXISTS `applet_this` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `applet_id` bigint(20) NOT NULL COMMENT '程序主键',
  `service_code` varchar(50) NOT NULL COMMENT '服务标识',
  `logic` enum('or','and') NOT NULL COMMENT '逻辑 or/and',
  `param` varchar(200) DEFAULT NULL COMMENT '传参',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='this组表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_ifttt.service 结构
CREATE TABLE IF NOT EXISTS `service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `desc` varchar(300) DEFAULT NULL COMMENT '描述',
  `code` varchar(30) NOT NULL COMMENT '服务标识 timer等',
  `type` enum('this','that') NOT NULL COMMENT '类型 0this 1that',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务表';

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
