SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE  table_schema ='iot_db_device' AND  table_name ='service_module_property' AND COLUMN_NAME='in_home_page'; 

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.service_module_property ADD COLUMN `in_home_page` tinyint(2) NOT NULL DEFAULT 0 COMMENT ''是否显示在首页'' AFTER `property_type` ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;