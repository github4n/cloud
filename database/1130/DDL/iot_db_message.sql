CREATE TABLE IF NOT EXISTS `iot_db_message`.`system_push_control` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` bigint(20) NOT NULL COMMENT 'APP应用id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `switch` enum('on','off') DEFAULT 'on' COMMENT '推送开关（on or off）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='系统推送控制表';

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_message' AND table_name = 'system_push_control' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_message.system_push_control ADD tenant_id bigint(20) NOT NULL COMMENT ''租户id''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

 ALTER TABLE `iot_db_message`.`system_push_control` MODIFY COLUMN `user_id`  varchar(50) DEFAULT NULL COMMENT '用户id';
 

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_message' AND table_name = 'tenant_mail_info' and COLUMN_NAME='app_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_message.tenant_mail_info ADD app_id bigint(20) NOT NULL COMMENT ''APPid''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;