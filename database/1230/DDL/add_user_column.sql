USE iot_db_user;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_user' AND table_name = 'user' and COLUMN_NAME='org_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_user.user ADD org_id bigint(20) DEFAULT NULL COMMENT ''组织主键''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;