

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_extend' and COLUMN_NAME='batch_num';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_extend add column `batch_num` bigint(11) DEFAULT NULL COMMENT ''批次号''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_extend' and COLUMN_NAME='unbind_flag';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_extend ADD unbind_flag TINYINT NOT NULL DEFAULT 0',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_extend' and COLUMN_NAME='reset_flag';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_extend ADD reset_flag TINYINT NOT NULL DEFAULT 0',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;