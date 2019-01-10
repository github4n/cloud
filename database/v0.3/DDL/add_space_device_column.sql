SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'space_device' and COLUMN_NAME='business_type_id';

SET @query = If(@exist=0,
        'ALTER TABLE iot_db_control.space_device ADD business_type_id int(20) DEFAULT NULL COMMENT ''业务类型ID''',
        'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

