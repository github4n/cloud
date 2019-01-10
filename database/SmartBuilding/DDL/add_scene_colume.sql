SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'scene' and COLUMN_NAME='silence_status';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_control.`scene` ADD COLUMN silence_status tinyint(1) DEFAULT NULL COMMENT ''静默状态 0否1是''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
