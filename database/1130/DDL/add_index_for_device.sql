SELECT Count(1)
INTO @exist
FROM information_schema.statistics
WHERE table_schema='iot_db_device' AND table_name = 'device' AND index_name = 'parent_id';

SET @query = If(@exist=0,
'ALTER TABLE iot_db_device.device ADD INDEX parent_id (parent_id)',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;