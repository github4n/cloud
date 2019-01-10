USE iot_db_device;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device' and COLUMN_NAME='remote_group_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device ADD COLUMN `remote_group_id` BIGINT NULL COMMENT ''遥控器id'' AFTER `supplier` ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device' and COLUMN_NAME='is_app_dev';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device ADD COLUMN `is_app_dev` tinyint(2) DEFAULT 0 COMMENT ''是否app子设备''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'product' and COLUMN_NAME='create_by';

SET @query = If(@exist=0,
		'ALTER  TABLE `iot_db_device`.`product` ADD COLUMN create_by bigint(20) DEFAULT NULL COMMENT ''创建人''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device' and COLUMN_NAME='is_direct_device';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device modify COLUMN `is_direct_device` tinyint(1) NOT NULL default 0 COMMENT ''是否直连设备0否、1是'' ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;