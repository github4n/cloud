USE iot_db_control;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'share_space' and COLUMN_NAME='alias';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.share_space ADD alias varchar(50) DEFAULT NULL COMMENT ''受邀人别名''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'share_space_log' and COLUMN_NAME='alias';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.share_space_log ADD alias varchar(50) DEFAULT NULL COMMENT ''受邀人别名''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'share_space' and COLUMN_NAME='from_user_uuid';
SET @query = If(@exist=0,
'alter TABLE iot_db_control.share_space ADD COLUMN `from_user_uuid` varchar(32) DEFAULT NULL COMMENT ''分享用户uuid''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'share_space' and COLUMN_NAME='to_user_uuid';
SET @query = If(@exist=0,
'alter TABLE iot_db_control.share_space ADD COLUMN `to_user_uuid` varchar(32) DEFAULT NULL COMMENT ''受邀分享用户uuid''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'share_space' and COLUMN_NAME='expire_time';
SET @query = If(@exist=0,
'alter TABLE iot_db_control.share_space ADD COLUMN `expire_time` bigint(20) DEFAULT NULL COMMENT ''失效时间,单位秒''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;





SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'share_space_log' and COLUMN_NAME='from_user_uuid';
SET @query = If(@exist=0,
'alter TABLE iot_db_control.share_space_log ADD COLUMN `from_user_uuid` varchar(32) DEFAULT NULL COMMENT ''分享用户uuid''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'share_space_log' and COLUMN_NAME='to_user_uuid';
SET @query = If(@exist=0,
'alter TABLE iot_db_control.share_space_log ADD COLUMN `to_user_uuid` varchar(32) DEFAULT NULL COMMENT ''受邀分享用户uuid''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'share_space_log' and COLUMN_NAME='expire_time';
SET @query = If(@exist=0,
'alter TABLE iot_db_control.share_space_log ADD COLUMN `expire_time` bigint(20) DEFAULT NULL COMMENT ''失效时间,单位秒''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;





SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'share_space' and COLUMN_NAME='to_user_email';
SET @query = If(@exist=0,
'alter TABLE iot_db_control.share_space ADD COLUMN `to_user_email` varchar(32) DEFAULT NULL COMMENT ''受邀人邮箱''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;