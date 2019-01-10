
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'generate_module' and COLUMN_NAME='describes';
SET @query = If(@exist=0,
'ALTER TABLE `iot_db_device`.`generate_module` ADD COLUMN `describes` TEXT NULL DEFAULT NULL AFTER `code_number`',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;