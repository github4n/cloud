SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_permission' AND table_name = 'permission' and COLUMN_NAME='org_id';
SET @query = If(@exist=0,
'alter TABLE iot_db_permission.permission 
 add COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID'',
 add COLUMN org_id bigint(20) DEFAULT NULL COMMENT ''组织ID''
 ',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_permission' AND table_name = 'role' and COLUMN_NAME='org_id';
SET @query = If(@exist=0,
'alter TABLE iot_db_permission.role add COLUMN org_id bigint(20) DEFAULT NULL COMMENT ''组织ID'' ',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;