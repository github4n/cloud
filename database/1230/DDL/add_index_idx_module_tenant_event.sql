SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_device'
AND table_name = 'service_to_event'
AND index_name = 'idx_module_tenant';


SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.service_to_event ADD INDEX idx_module_tenant (service_module_id, tenant_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;