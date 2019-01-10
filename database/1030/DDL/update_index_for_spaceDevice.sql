-- drop space_device unique key
SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_control'
AND table_name = 'space_device'
AND index_name = 'unique_device_id';


SET @query = If(@exist>0,
		'alter table iot_db_control.space_device drop index unique_device_id',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


-- add space_device index
SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_control'
AND table_name = 'space_device'
AND index_name = 'idx_device_id';


SET @query = If(@exist=0,
		'alter table iot_db_control.space_device add index idx_device_id(device_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;