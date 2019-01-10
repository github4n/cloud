SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'tenant' and COLUMN_NAME='update_time';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.tenant ADD update_time datetime DEFAULT NULL COMMENT ''修改时间''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;