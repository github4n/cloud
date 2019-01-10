

USE `iot_db_control`;

-- ----------------------------
-- 整校主表
-- ----------------------------
DROP TABLE IF EXISTS `location_scene`;
CREATE TABLE `location_scene` (
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


-- ----------------------------
-- 整校子表
-- ----------------------------
DROP TABLE IF EXISTS `location_scene_detail`;
CREATE TABLE `location_scene_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `location_scene_id` bigint(20) DEFAULT NULL,
  `scene_id` bigint(20) DEFAULT NULL,
  `del_flag` tinyint(2) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

