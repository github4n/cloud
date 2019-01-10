SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_device'
AND table_name = 'module_action_to_property'
AND index_name = 'idx_action_tenant';


SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.module_action_to_property ADD INDEX idx_action_tenant (module_action_id, tenant_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;