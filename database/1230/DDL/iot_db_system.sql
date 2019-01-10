SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_system' AND table_name = 'country' and COLUMN_NAME='area_code';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_system.country ADD area_code varchar(10) NOT NULL COMMENT ''手机区号''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;