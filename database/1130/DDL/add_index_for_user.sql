SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_user'
AND table_name = 'user'
AND index_name = 'idx_user_name';


SET @query = If(@exist=0,
		'alter table iot_db_user.user add index idx_user_name(user_name)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;