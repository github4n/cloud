SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'deployment' and COLUMN_NAME='location_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.deployment ADD COLUMN location_id bigint(20) NULL COMMENT ''区域ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'deployment' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.deployment ADD COLUMN tenant_id bigint(20) NULL COMMENT ''租户ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'deployment' and COLUMN_NAME='create_by';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.deployment ADD COLUMN create_by bigint(20) NULL COMMENT ''创建者''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'deployment' and COLUMN_NAME='update_by';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.deployment ADD COLUMN update_by bigint(20) NULL COMMENT ''更新人''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'deployment' and COLUMN_NAME='create_time';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.deployment ADD COLUMN create_time datetime NULL COMMENT ''创建者''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_build' AND table_name = 'deployment' and COLUMN_NAME='update_time';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_build.deployment ADD COLUMN update_time datetime NULL COMMENT ''更新人''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;