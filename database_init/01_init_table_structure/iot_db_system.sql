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

-- 导出 iot_db_system 的数据库结构
CREATE DATABASE IF NOT EXISTS `iot_db_system` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iot_db_system`;


-- 导出  表 iot_db_system.city 结构
CREATE TABLE IF NOT EXISTS `city` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `country_code` varchar(10) DEFAULT NULL COMMENT '国家代码',
  `province_code` varchar(10) DEFAULT NULL COMMENT '州省代码',
  `city_code` varchar(10) DEFAULT NULL COMMENT '城市代码',
  `city_key` varchar(50) DEFAULT NULL COMMENT '城市KEY',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='城市信息';

-- 数据导出被取消选择。


-- 导出  表 iot_db_system.country 结构
CREATE TABLE IF NOT EXISTS `country` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `country_code` varchar(10) DEFAULT NULL COMMENT '国家代码',
  `country_key` varchar(50) DEFAULT NULL COMMENT '国家KEY',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  `area_code` varchar(10) NOT NULL COMMENT '手机区号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='国家信息';

-- 数据导出被取消选择。


-- 导出  表 iot_db_system.lang_info 结构
CREATE TABLE IF NOT EXISTS `lang_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `lang_type` enum('zh_CN','en_US') NOT NULL DEFAULT 'en_US' COMMENT '语言类型',
  `lang_key` varchar(50) NOT NULL COMMENT '国际化键',
  `lang_value` varchar(5000) DEFAULT NULL COMMENT '国际化值',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 iot_db_system.province 结构
CREATE TABLE IF NOT EXISTS `province` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `country_code` varchar(10) DEFAULT NULL COMMENT '国家代码',
  `province_code` varchar(10) DEFAULT NULL COMMENT '州省代码',
  `province_key` varchar(50) DEFAULT NULL COMMENT '州省KEY',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='州省信息';

-- 数据导出被取消选择。


-- 导出  表 iot_db_system.sys_db_version 结构
CREATE TABLE IF NOT EXISTS `sys_db_version` (
  `version` varchar(13) NOT NULL COMMENT '版本号',
  `version_desc` varchar(1000) NOT NULL COMMENT '变更描述',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `pk_version` (`version`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库升级版本信息';

-- 数据导出被取消选择。


-- 导出  表 iot_db_system.sys_dict_item 结构
CREATE TABLE IF NOT EXISTS `sys_dict_item` (
  `type_id` int(10) NOT NULL COMMENT '大类型id',
  `item_id` varchar(40) NOT NULL COMMENT '小类型id',
  `item_name` varchar(200) NOT NULL COMMENT '小类型名称',
  `item_desc` varchar(500) DEFAULT NULL COMMENT '小类型描述',
  `item_sort` tinyint(4) DEFAULT '0' COMMENT '排序字段',
  UNIQUE KEY `type_id,item_id` (`type_id`,`item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表小类';

-- 数据导出被取消选择。


-- 导出  表 iot_db_system.sys_dict_type 结构
CREATE TABLE IF NOT EXISTS `sys_dict_type` (
  `type_id` int(10) NOT NULL COMMENT '类型id',
  `type_name` varchar(50) NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `id` (`type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表大类';

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
