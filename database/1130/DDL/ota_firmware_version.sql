SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_firmware_version' and COLUMN_NAME='init_version';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.ota_firmware_version ADD init_version tinyint(2) NULL DEFAULT 0 COMMENT ''是否初始版本 0:不是 1：是''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;