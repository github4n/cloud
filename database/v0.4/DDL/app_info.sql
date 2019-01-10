



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='fcm_file_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.`app_info` ADD COLUMN `fcm_file_id`  varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT ''google FCM文件id'' AFTER `pack_time`',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
