USE iot_db_control;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'space' and COLUMN_NAME='mesh_name';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.space ADD COLUMN `mesh_name` VARCHAR(255) NULL AFTER `deploy_id` ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'space' and COLUMN_NAME='mesh_password';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.space ADD COLUMN `mesh_password` VARCHAR(255) NULL AFTER `mesh_name` ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;