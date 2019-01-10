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

-- 导出 iot_db_permission 的数据库结构
CREATE DATABASE IF NOT EXISTS `iot_db_permission` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iot_db_permission`;


-- 导出  表 iot_db_permission.permission 结构
CREATE TABLE IF NOT EXISTS `permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父资源id',
  `permission_code` varchar(36) NOT NULL COMMENT '权限code',
  `permission_url` varchar(100) NOT NULL COMMENT '资源对应url',
  `permission_name` varchar(200) NOT NULL COMMENT '资源名称',
  `permission_type` enum('menu','button','other') NOT NULL DEFAULT 'other' COMMENT '资源类型（menu; button;other(默认)）',
  `sort` smallint(6) DEFAULT NULL COMMENT '资源排序  低的排前面',
  `icon` varchar(50) DEFAULT NULL COMMENT '资源图标  权限类型为菜单或按钮时才有值',
  `service_id` bigint(20) DEFAULT NULL COMMENT '服务ID',
  `system_type` enum('Boss','2B','2C','Portal') DEFAULT '2B' COMMENT '系统类别',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限信息表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_permission.role 结构
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `role_code` enum('Owner','Manager','ProductManager','APPDeveloper','EmbeddedDeveloper','Tester','Operator','DataQueryer') NOT NULL DEFAULT 'Manager' COMMENT '角色编码',
  `role_type` enum('Boss','2B','2C','Portal') DEFAULT '2B' COMMENT '角色类型:（Boss;2B(默认)；2C；Portal）',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `role_desc` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_permission.role_permission_relate 结构
CREATE TABLE IF NOT EXISTS `role_permission_relate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `permission_id` bigint(20) NOT NULL COMMENT '权限id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `permission_id, role_id` (`permission_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关联表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_permission.user_role_relate 结构
CREATE TABLE IF NOT EXISTS `user_role_relate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `user_id, role_id` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
