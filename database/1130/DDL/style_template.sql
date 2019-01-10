ALTER TABLE `iot_db_device`.`style_template` CHANGE COLUMN `code` `code` VARCHAR(255) NULL DEFAULT NULL COMMENT '样式唯一标识code' AFTER `name`;

USE iot_db_device;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'style_template' and COLUMN_NAME='resource_link_validation';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.style_template ADD COLUMN `resource_link_validation` VARCHAR(255) NULL COMMENT ''MD5效验'' AFTER `resource_link` ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;