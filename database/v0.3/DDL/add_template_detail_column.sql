SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'template_detail' and COLUMN_NAME='business_type_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.template_detail ADD business_type_id bigint(20) DEFAULT NULL COMMENT ''业务类型ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



