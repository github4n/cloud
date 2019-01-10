USE iot_db_control;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'space_device' and COLUMN_NAME='space_id';

SET @query = If(@exist=1,
		'ALTER TABLE iot_db_control.space_device MODIFY space_id bigint(20)  NOT NULL  COMMENT ''空间ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'space_device' and COLUMN_NAME='device_id';

SET @query = If(@exist=1,
		'ALTER TABLE iot_db_control.space_device MODIFY device_id varchar(255) NOT NULL  COMMENT ''设备uuid''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;