SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_user' AND table_name = 'user' and COLUMN_NAME='user_status';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_user.user add column `user_status` enum(''unreviewed'',''auditFailed'',''normal'',''deleted'') DEFAULT ''normal'' COMMENT ''用户状态（未审核:unreviewed，审核未通过:auditFailed，正常:normal，已删除:deleted）''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'cust_p2pid' and COLUMN_NAME='user_mark';

SET @query = If(@exist=1,
		'alter table iot_db_device.cust_p2pid change column user_mark use_mark tinyint(1)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_extend' and COLUMN_NAME='batch_num';

SET @query = If(@exist=0,
		'alter table iot_db_device.device_extend add batch_num bigint(20) comment ''批次号''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;