SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_device'
AND table_name = 'module_event_to_property'
AND index_name = 'idx_event_tenant';


SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.module_event_to_property ADD INDEX idx_event_tenant (module_event_id, tenant_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;