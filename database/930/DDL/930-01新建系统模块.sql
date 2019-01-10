CREATE TABLE IF NOT EXISTS `iot_db_system`.`lang_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `lang_type` enum('zh_CN','en_US') NOT NULL DEFAULT 'en_US' COMMENT '语言类型',
  `lang_key` varchar(50) NOT NULL COMMENT '国际化键',
  `lang_value` varchar(1000) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `iot_db_system`.`sys_db_version` (
  `version` varchar(13) NOT NULL COMMENT '版本号',
  `version_desc` varchar(1000) NOT NULL COMMENT '变更描述',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `pk_version` (`version`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库升级版本信息';

CREATE TABLE IF NOT EXISTS `iot_db_system`.`sys_dict_item` (
  `type_id` int(10) NOT NULL COMMENT '大类型id',
  `item_id` varchar(40) NOT NULL COMMENT '小类型id',
  `item_name` varchar(200) NOT NULL COMMENT '小类型名称',
  `item_desc` varchar(500) DEFAULT NULL COMMENT '小类型描述',
  `item_sort` tinyint(4) DEFAULT '0' COMMENT '排序字段',
  UNIQUE KEY `type_id,item_id` (`type_id`,`item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表小类';

CREATE TABLE IF NOT EXISTS `iot_db_system`.`sys_dict_type` (
  `type_id` int(10) NOT NULL COMMENT '类型id',
  `type_name` varchar(50) NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `id` (`type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表大类';