CREATE TABLE IF NOT EXISTS `iot_db_system`.`country` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `country_code` varchar(10) DEFAULT NULL COMMENT '国家代码',
  `country_key` varchar(50) DEFAULT NULL COMMENT '国家KEY',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='国家信息';

CREATE TABLE IF NOT EXISTS `iot_db_system`.`province` (
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='州省信息';

CREATE TABLE IF NOT EXISTS `iot_db_system`.`city` (
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='城市信息';