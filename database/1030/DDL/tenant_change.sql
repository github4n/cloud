USE iot_db_tenant;

-- 2018.10.18 by lishuai add tenant lock_status
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'tenant' and COLUMN_NAME='lock_status';
SET @query = If(@exist=0,
'ALTER TABLE iot_db_tenant.tenant ADD lock_status tinyint(2) DEFAULT 0 COMMENT ''锁定状态(0:未锁定 1:锁定)''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'tenant' and COLUMN_NAME='audit_status';
SET @query = If(@exist=0,
'ALTER TABLE `iot_db_tenant`.`tenant` ADD COLUMN `audit_status`  tinyint(2) NULL DEFAULT 0 COMMENT ''0:未审核 1:审核未通过 2:审核通过''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS `iot_db_tenant`.`tenant_review_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间（申请时间，审核时间）',
  `operate_desc` varchar(500) DEFAULT NULL COMMENT '操作描述（提交审核，审核通过，审核不通过原因）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  `process_status` tinyint(2) DEFAULT '0' COMMENT '0:未审核 1:审核未通过 2:审核通过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='租户审核记录';
