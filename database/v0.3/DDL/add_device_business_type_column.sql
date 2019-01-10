SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_business_type' and COLUMN_NAME='product_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_business_type ADD product_id bigint(20) DEFAULT NULL COMMENT ''产品ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_business_type' and COLUMN_NAME='is_home_show';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_business_type ADD is_home_show tinyint(2) DEFAULT NULL COMMENT ''是否在首页显示  0，否  1，是''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

