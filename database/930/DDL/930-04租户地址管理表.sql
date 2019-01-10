CREATE TABLE IF NOT EXISTS `iot_db_tenant`.`tenant_addres_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `country` varchar(100) DEFAULT NULL COMMENT '国家',
  `state` varchar(100) DEFAULT NULL COMMENT '省',
  `city` varchar(100) DEFAULT NULL COMMENT '市',
  `addres` varchar(500) DEFAULT NULL COMMENT '详细地址',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `contacter_name` varchar(100) DEFAULT NULL COMMENT '联系人姓名',
  `contacter_tel` varchar(20) DEFAULT NULL COMMENT '联系人电话',
  `zip_code` varchar(10) DEFAULT NULL COMMENT '邮编',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='租户地址管理表';

CREATE TABLE IF NOT EXISTS `iot_db_device`.`product_publish_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品ID',
  `publish_time` datetime DEFAULT NULL COMMENT '产品发布时间',
  `publish_status` enum('Reviewing','Post Failure','Post Success') DEFAULT 'Reviewing' COMMENT '产品发布状态(Reviewing(默认);Post Failure;Post Success)',
  `failure_reason` varchar(500) DEFAULT NULL COMMENT '发布失败原因',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='产品发布历史表';

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_message' AND table_name = 'tenant_mail_info' and COLUMN_NAME='mail_port';

SET @query = If(@exist=0,
'ALTER TABLE iot_db_message.tenant_mail_info ADD mail_port int(5) not Null COMMENT ''邮箱端口''',
'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;