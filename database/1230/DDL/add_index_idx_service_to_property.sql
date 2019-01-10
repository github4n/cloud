SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_device'
AND table_name = 'service_to_property'
AND index_name = 'idx_service_module_id';


SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.service_to_property ADD INDEX idx_service_module_id (service_module_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;