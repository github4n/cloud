SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'group_info' and COLUMN_NAME='device_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`group_info` ADD COLUMN device_id varchar(32) DEFAULT NULL COMMENT ''…Ë±∏Id''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
