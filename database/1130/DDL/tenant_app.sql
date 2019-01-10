USE iot_db_tenant;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='keystore_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD keystore_id varchar(100) DEFAULT NULL COMMENT ''安卓证书文件fileId''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='create_by';
SET @query = If(@exist=0,
		'ALTER  TABLE `iot_db_tenant`.`app_info` ADD COLUMN create_by bigint(20) DEFAULT NULL COMMENT ''创建人''',
		'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;