USE iot_db_control;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'automation' and COLUMN_NAME='visiable';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.automation ADD visiable int(2) DEFAULT 1 COMMENT ''是否可视 0否 1是''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'automation' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.automation ADD org_id bigint(20) DEFAULT NULL COMMENT ''组织主键''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;









