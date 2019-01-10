
USE `iot_db_tenant`;

CREATE TABLE IF NOT EXISTS iot_db_tenant.`copywrite_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `object_id` bigint(20) DEFAULT NULL COMMENT '对象ID',
  `object_type` enum('ProdcutOTA','AppConfig') NOT NULL DEFAULT 'ProdcutOTA' COMMENT '对象类型',
  `lang_type` enum('zh_CN','en_US') NOT NULL DEFAULT 'en_US' COMMENT '语言类型',
  `lang_key` varchar(50) NOT NULL COMMENT '国际化键',
  `lang_value` varchar(1000) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tenant_id` (`tenant_id`,`object_id`,`lang_type`,`lang_key`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='文案信息表';
