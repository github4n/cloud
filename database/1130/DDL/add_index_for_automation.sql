SELECT Count(1)
INTO @exist
FROM information_schema.statistics
WHERE table_schema='iot_db_control' AND table_name = 'automation' AND index_name = 'idx_user_id';

SET @query = If(@exist=0,
'ALTER TABLE iot_db_control.automation ADD INDEX idx_user_id (user_id), ADD INDEX idx_tenant_id (tenant_id), ADD INDEX idx_space_id (space_id)',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;