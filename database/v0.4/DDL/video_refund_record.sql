
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_boss' AND table_name = 'video_refund_record' and COLUMN_NAME='user_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_boss.`video_refund_record` ADD COLUMN `user_id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''申请退款用户uuid'' AFTER `tenant_id`''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

ALTER TABLE `iot_db_boss`.`video_refund_record` MODIFY COLUMN `plan_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '计划id（对应video_plan的plan_id）' AFTER `order_id`;
ALTER TABLE `iot_db_boss`.`video_refund_record` MODIFY COLUMN `audit_status`  tinyint(2) NULL DEFAULT NULL COMMENT '审批状态(0：待审核；1、通过；2审核不通过)' AFTER `audit_time`;
ALTER TABLE `iot_db_boss`.`video_refund_record` MODIFY COLUMN `refund_status`  tinyint(2) NULL DEFAULT NULL COMMENT '退款状态(0-取消,1-申请退款中，2-退款成功,3-退款失败;4-paypal退款中)' AFTER `audit_status`;