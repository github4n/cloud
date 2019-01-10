SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'allocation' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`allocation` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'build_calendar' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`build_calendar` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'deployment' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`deployment` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'device_business_type' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`device_business_type` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'device_business_type' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`device_business_type` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'index_content' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`index_content` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'index_detail' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`index_detail` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'location' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`location` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'location_scene' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`location_scene` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'reservation' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`reservation` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'space_background_img' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`space_background_img` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'space_template' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`space_template` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'template' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`template` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'warning' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`warning` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'device_business_type' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`device_business_type` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'space_device' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_control.`space_device` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'template' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`template` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'scene_detail' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_control.`scene_detail` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'space_device' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_control.`space_device` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'ota_file_info' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.`ota_file_info` ADD COLUMN `org_id` bigint(20) NULL COMMENT ''组织ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
