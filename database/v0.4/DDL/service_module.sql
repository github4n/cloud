
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'service_module' and COLUMN_NAME='img';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.service_module ADD img varchar(255) DEFAULT NULL COMMENT ''icon''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'service_module' and COLUMN_NAME='change_img';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.service_module ADD change_img varchar(255) DEFAULT NULL',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;