USE iot_db_tenant;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='ios_file_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `ios_file_id` VARCHAR(100) NULL DEFAULT NULL COMMENT ''ios 文件id'' AFTER `fcm_file_id` ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='ios_send_key';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `ios_send_key` VARCHAR(100) NULL DEFAULT NULL COMMENT ''ios推送密码''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='keypass';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `keypass` VARCHAR(255) NULL DEFAULT NULL COMMENT ''安卓证书秘钥'' AFTER `privacy_en_link_id` ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='storepass';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `storepass` VARCHAR(255) NULL DEFAULT NULL COMMENT ''安卓证书秘钥'' AFTER `keypass` ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;