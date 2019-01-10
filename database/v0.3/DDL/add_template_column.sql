SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'template' and COLUMN_NAME='deploy_id';

SET @query = If(@exist=0,
		'ALTER TABLE template ADD COLUMN deploy_id  bigint(20) DEFAULT NULL COMMENT ''部署方式ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

