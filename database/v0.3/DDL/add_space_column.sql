SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'space' and COLUMN_NAME='deploy_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_control.space ADD deploy_id bigint(20) DEFAULT NULL COMMENT ''部署方式''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'space' and COLUMN_NAME='model';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_control.space ADD model int(1) DEFAULT NULL COMMENT ''0 普通房间 1会议室''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'space' and COLUMN_NAME='time_zone_offset';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_control.space ADD time_zone_offset int(8) DEFAULT NULL COMMENT ''时区偏移量 单位分钟''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
