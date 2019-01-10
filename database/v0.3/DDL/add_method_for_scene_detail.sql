SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'scene_detail' and COLUMN_NAME='method';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.scene_detail ADD method varchar(100) DEFAULT NULL',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;