SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'style_template' and COLUMN_NAME='resource_link';
SET @query = If(@exist=0,
'ALTER TABLE `iot_db_device`.`style_template` ADD COLUMN `resource_link` VARCHAR(255) NULL COMMENT ''资源链接'' AFTER `tenant_id`',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;