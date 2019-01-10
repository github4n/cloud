CREATE TABLE IF NOT EXISTS `iot_db_device`.`product_review_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `product_id` bigint(20) NOT NULL COMMENT '产品Id',
  `operate_time` datetime NOT NULL COMMENT '操作时间（申请时间，审核时间）',
  `operate_desc` varchar(500) DEFAULT NULL COMMENT '操作描述（提交审核，审核通过，审核不通过原因）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  `process_status` tinyint(2) DEFAULT NULL COMMENT '0:未审核 1:审核未通过 2:审核通过 3:留言 4：反馈',
  PRIMARY KEY (`id`),
  KEY `index_tenant_id` (`tenant_id`) USING BTREE,
  KEY `index_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='产品审核记录';

CREATE TABLE IF NOT EXISTS `iot_db_tenant`.`app_review_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `app_id` bigint(20) NOT NULL COMMENT 'appId',
  `operate_time` datetime NOT NULL COMMENT '操作时间（申请时间，审核时间）',
  `operate_desc` varchar(500) DEFAULT NULL COMMENT '操作描述（提交审核，审核通过，审核不通过原因）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  `process_status` tinyint(2) DEFAULT NULL COMMENT '0:未审核 1:审核未通过 2:审核通过 3:留言 4：反馈',
  PRIMARY KEY (`id`),
  KEY `index_tenant_id` (`tenant_id`) USING BTREE,
  KEY `index_app_id` (`app_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='APP审核记录';


CREATE TABLE IF NOT EXISTS `iot_db_device`.`service_review_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `product_id` bigint(20) NOT NULL COMMENT '产品Id',
  `service_id` bigint(20) NOT NULL COMMENT '语音服务Id，goods表中对应的Id',
  `operate_time` datetime NOT NULL COMMENT '操作时间（申请时间，审核时间）',
  `operate_desc` varchar(500) DEFAULT NULL COMMENT '操作描述（提交审核，审核通过，审核不通过原因）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  `process_status` tinyint(2) DEFAULT NULL COMMENT '0:未审核 1:审核未通过 2:审核通过 3:留言 4：反馈',
  PRIMARY KEY (`id`),
  KEY `index_tenant_id` (`tenant_id`) USING BTREE,
  KEY `index_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='语音接入服务审核记录';

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='audit_status';
SET @query = If(@exist=0,
'alter TABLE iot_db_tenant.app_info add COLUMN audit_status tinyint(2) DEFAULT NULL COMMENT ''0:未审核 1:审核未通过 2:审核通过''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'product' and COLUMN_NAME='audit_status';
SET @query = If(@exist=0,
'alter TABLE iot_db_device.product add COLUMN audit_status tinyint(2) DEFAULT NULL COMMENT ''0:未审核 1:审核未通过 2:审核通过''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'product' and COLUMN_NAME='service_goo_audit_status';
SET @query = If(@exist=0,
'alter TABLE iot_db_device.product add COLUMN service_goo_audit_status tinyint(2) DEFAULT NULL COMMENT ''Google语音服务审核状态，0:未审核 1:审核未通过 2:审核通过''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'product' and COLUMN_NAME='service_alx_audit_status';
SET @query = If(@exist=0,
'alter TABLE iot_db_device.product add COLUMN service_alx_audit_status tinyint(2) DEFAULT NULL COMMENT ''Aleax语音服务审核状态，0:未审核 1:审核未通过 2:审核通过''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='apply_audit_time';
SET @query = If(@exist=0,
'alter TABLE iot_db_tenant.app_info ADD COLUMN `apply_audit_time` datetime DEFAULT NULL COMMENT ''申请审核时间''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'product' and COLUMN_NAME='apply_audit_time';
SET @query = If(@exist=0,
'alter TABLE iot_db_device.product ADD COLUMN `apply_audit_time` datetime DEFAULT NULL COMMENT ''申请审核时间''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

alter table iot_db_device.`product_review_record` MODIFY column `process_status` tinyint(2) DEFAULT '0' COMMENT '0:未审核 1:审核未通过 2:审核通过';
alter table iot_db_tenant.`app_review_record` MODIFY column `process_status` tinyint(2) DEFAULT '0' COMMENT '0:未审核 1:审核未通过 2:审核通过';
alter table iot_db_device.`service_review_record` MODIFY column `process_status` tinyint(2) DEFAULT '0' COMMENT '0:未审核 1:审核未通过 2:审核通过';



CREATE TABLE IF NOT EXISTS iot_db_tenant.reply_message_record(
	`id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '自增id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `parent_id` bigint(20)  COMMENT '上一条消息id',
  `object_type` varchar(20) NOT NULL COMMENT '对象类型（app、Google_voice、alx_voice、message等等）',
  `object_id` bigint(20) NOT NULL COMMENT '对象ID, 产品id或APPid、审核记录id',
  `message` text NOT NULL COMMENT '内容',
  `message_type` enum('feedback','reply') DEFAULT 'feedback' NOT NULL COMMENT '消息类型（feedback：反馈，reply：回复）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '回复消息记录表';


UPDATE `iot_db_payment`.`goods_info` SET `icon`='icon-com_icon_permission_GoogleAssistant', `update_time`=now() WHERE (`goods_code`='0010');
UPDATE `iot_db_payment`.`goods_info` SET `icon`='icon-com_icon_permission_Alexa', `update_time`=now()  WHERE (`goods_code`='0011');
ALTER TABLE  iot_db_device.service_buy_record  MODIFY pay_status tinyint(2) COMMENT '支付状态（1：待付款；2：付款成功；3：付款失败； 4：退款中；5：退款成功；6：退款失败）';
ALTER TABLE  iot_db_device.uuid_apply_record  MODIFY pay_status tinyint(2) COMMENT '支付状态（1：待付款；2：付款成功；3：付款失败； 4：退款中；5：退款成功；6：退款失败）';


