USE iot_db_device;

SELECT Count(1)
  INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_status' AND COLUMN_NAME = 'token';

SET @query = If(@exist = 0,
                'ALTER TABLE iot_db_device.device_status ADD token varchar(255) DEFAULT NULL COMMENT ''2B用''',
                'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;