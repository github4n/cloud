USE iot_db_tenant;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'virtual_org' and COLUMN_NAME='parent_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.virtual_org ADD parent_id BIGINT(20) DEFAULT NULL COMMENT ''父组织主键''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'virtual_org' and COLUMN_NAME='order_num';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.virtual_org ADD order_num int(5) DEFAULT NULL COMMENT ''排序值''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'virtual_org' and COLUMN_NAME='path';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.virtual_org ADD path varchar(50) DEFAULT NULL COMMENT ''组织路径''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'virtual_org' and COLUMN_NAME='type';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.virtual_org ADD type int(2) DEFAULT NULL COMMENT ''组织类型：0 经销商 1学校''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'virtual_org' and COLUMN_NAME='update_time';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.virtual_org ADD update_time datetime DEFAULT NULL COMMENT ''更新时间''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
