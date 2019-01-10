

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'product' and COLUMN_NAME='icon';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.product ADD icon varchar(255) DEFAULT NULL',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'product' and COLUMN_NAME='develop_status';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.product ADD develop_status TINYINT DEFAULT NULL',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'product' and COLUMN_NAME='enterprise_develop_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.product ADD enterprise_develop_id bigint(20) DEFAULT NULL',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;