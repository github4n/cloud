SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_status' and COLUMN_NAME='last_login_time';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_status ADD last_login_time datetime DEFAULT NULL',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

