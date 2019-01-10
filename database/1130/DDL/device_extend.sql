USE iot_db_device;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_extend' and COLUMN_NAME='address';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_extend ADD COLUMN `address` BIGINT NULL COMMENT ''设备操作地址'' ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;