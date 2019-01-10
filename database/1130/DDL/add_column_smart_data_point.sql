USE iot_db_device;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'smart_data_point' and COLUMN_NAME='property_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.smart_data_point ADD property_id bigint(20) DEFAULT NULL COMMENT ''模组-属性表 id(service_module_property.id)''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;