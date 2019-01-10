USE iot_db_user;

-- 增加注释
alter table `iot_db_user`.`smart_token` modify column `smart` tinyint(1) DEFAULT NULL comment '第三方类型(1:alexa,2:googleHome)';
alter table `iot_db_user`.`smart_token` modify column `access_token` text DEFAULT NULL comment '第三方的access_token';
alter table `iot_db_user`.`smart_token` modify column `refresh_token` text DEFAULT NULL comment '第三方的refresh_token';


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_user' AND table_name = 'smart_token' and COLUMN_NAME='local_access_token';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_user.smart_token ADD local_access_token varchar(1000) DEFAULT NULL COMMENT ''本地生成的access_token''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_user' AND table_name = 'smart_token' and COLUMN_NAME='local_refresh_token';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_user.smart_token ADD local_refresh_token varchar(1000) DEFAULT NULL COMMENT ''本地生成的refresh_token''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_user' AND table_name = 'smart_token' and COLUMN_NAME='third_party_info_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_user.smart_token ADD third_party_info_id bigint(20) DEFAULT NULL COMMENT ''第三方的third_party_info.id''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_user' AND table_name = 'smart_token' and COLUMN_NAME='client_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_user.smart_token ADD client_id varchar(255) DEFAULT NULL COMMENT ''第三方的client_id''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;