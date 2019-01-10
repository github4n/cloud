SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_device'
AND table_name = 'device_type_to_service_module'
AND index_name = 'idx_dtype_tenant';


SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_type_to_service_module ADD INDEX idx_dtype_tenant (device_type_id, tenant_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;