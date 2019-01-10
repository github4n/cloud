SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_user'
AND table_name = 'user_login'
AND index_name = 'idx_user_id';


SET @query = If(@exist=0,
		'alter table iot_db_user.user_login add index idx_user_id(user_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;