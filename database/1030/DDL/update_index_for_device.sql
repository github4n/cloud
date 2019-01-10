-- drop device_status unique key 
SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_device'
AND table_name = 'device_status'
AND index_name = 'device_id';


SET @query = If(@exist>0,
		'alter table iot_db_device.device_status drop index device_id',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


-- add device_status index
SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_device'
AND table_name = 'device_status'
AND index_name = 'idx_device_id';


SET @query = If(@exist=0,
		'alter table iot_db_device.device_status add index idx_device_id(device_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


-- drop device_extend unique key
SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_device'
AND table_name = 'device_extend'
AND index_name = 'device_id';


SET @query = If(@exist>0,
		'alter table iot_db_device.device_extend drop index device_id',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


-- add device_extend index
SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_device'
AND table_name = 'device_extend'
AND index_name = 'idx_device_id';


SET @query = If(@exist=0,
		'alter table iot_db_device.device_extend add index idx_device_id(device_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



