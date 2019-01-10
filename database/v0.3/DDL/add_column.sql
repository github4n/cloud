SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'tenant' and COLUMN_NAME='business';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.tenant ADD business varchar(80) COLLATE utf8mb4_bin DEFAULT NULL COMMENT ''主营业务''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'tenant' and COLUMN_NAME='job';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.tenant ADD job varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT ''职务''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'tenant' and COLUMN_NAME='type';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.tenant ADD type tinyint(4) DEFAULT ''0'' COMMENT ''0:2c,1:2B''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_user' AND table_name = 'user' and COLUMN_NAME='user_level';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_user.user ADD user_level tinyint(2) DEFAULT ''3'' COMMENT ''区分用户级别(1:BOSS;2:企业级客户;3:终端用户)''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'ifttt_rule' and COLUMN_NAME='ifttt_type';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.ifttt_rule ADD ifttt_type varchar(8) DEFAULT NULL COMMENT ''ifttt类型 "_2B"  "_2C"''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;