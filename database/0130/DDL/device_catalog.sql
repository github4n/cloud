USE iot_db_device;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_catalog' and COLUMN_NAME='order';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_catalog ADD COLUMN `order`  int NULL ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;