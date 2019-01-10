SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'space_template' and COLUMN_NAME='start_time';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.space_template ADD COLUMN `start_time`  bigint(20) NULL COMMENT ''开始时间''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'space_template' and COLUMN_NAME='end_time';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.space_template ADD COLUMN `end_time`  bigint(20) NULL COMMENT ''结束时间''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'space_template' and COLUMN_NAME='run_time';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.space_template ADD COLUMN `run_time`  varchar(10) NULL COMMENT ''UTC 运行时间点''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'space_template' and COLUMN_NAME='business';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.space_template ADD COLUMN `business`  varchar(10) NULL COMMENT ''使能 1开 0关''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'space_template' and COLUMN_NAME='template_name';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.space_template ADD COLUMN `template_name`  varchar(50) NULL COMMENT ''模板名称''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'space_template' and COLUMN_NAME='week';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.space_template ADD COLUMN `week`  varchar(50) NULL COMMENT ''周''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'space_template' and COLUMN_NAME='start_cron';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.space_template MODIFY COLUMN `start_cron` varchar(50) NULL DEFAULT NULL COMMENT ''表达式''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

