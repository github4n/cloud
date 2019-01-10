ALTER TABLE iot_db_build.`activity_record`
MODIFY COLUMN `time`  timestamp NULL DEFAULT NULL COMMENT '创建时间';


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'activity_record' and COLUMN_NAME='space_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.activity_record ADD COLUMN `space_id`  bigint(20) NULL COMMENT ''空间ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'activity_record' and COLUMN_NAME='template_name';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.activity_record ADD COLUMN `template_name`  varchar(50) NULL COMMENT ''模板名称''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'activity_record' and COLUMN_NAME='space_template_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.activity_record ADD COLUMN `space_template_id`  bigint(20) NULL COMMENT ''schedule模板ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;