SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'template' and COLUMN_NAME='shortcut';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.template ADD COLUMN shortcut int(1) DEFAULT 0 COMMENT ''快捷方式0无1有''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'template' and COLUMN_NAME='silence_status';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.template ADD COLUMN silence_status tinyint(1) DEFAULT 0 COMMENT ''静默状态 0否1是''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;