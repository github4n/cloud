SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_control'
AND table_name = 'space'
AND index_name = 'idx_user_id';


SET @query = If(@exist=0,
		'alter table iot_db_control.space add index idx_user_id(user_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;