SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_upgrade_log' and COLUMN_NAME='upgrade_msg';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.ota_upgrade_log ADD upgrade_msg varchar(60) NULL COMMENT ''升级结果信息''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

