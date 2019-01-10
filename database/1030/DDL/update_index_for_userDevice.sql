
-- add user_device index device_id
SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_control'
AND table_name = 'user_device'
AND index_name = 'idx_device_id';


SET @query = If(@exist=0,
		'alter table iot_db_control.user_device add index idx_device_id(device_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



-- add user_device index user_id
SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_control'
AND table_name = 'user_device'
AND index_name = 'idx_user_id';


SET @query = If(@exist=0,
		'alter table iot_db_control.user_device add index idx_user_id(user_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




-- add user_device index tenant_id
SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_control'
AND table_name = 'user_device'
AND index_name = 'idx_tenant_id';


SET @query = If(@exist=0,
		'alter table iot_db_control.user_device add index idx_tenant_id(tenant_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;