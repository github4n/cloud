USE iot_db_message;

-- 2018.10.16 by lishuai add push_notice_template tenant_id
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_message' AND table_name = 'push_notice_template' and COLUMN_NAME='tenant_id';
SET @query = If(@exist=0,
'ALTER TABLE iot_db_message.push_notice_template ADD tenant_id bigint(20) COMMENT ''租户主键''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
