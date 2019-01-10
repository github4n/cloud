USE iot_db_device;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device ADD org_id bigint(20) DEFAULT NULL COMMENT ''组织id''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;