USE iot_db_device;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'service_to_event' and COLUMN_NAME='status';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.service_to_event ADD COLUMN `status` TINYINT NULL COMMENT ''0可选，1必选'' AFTER `description` ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;