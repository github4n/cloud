USE iot_db_tenant;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='is_favorite';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `is_favorite` DATETIME COMMENT ''喜好'' ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;