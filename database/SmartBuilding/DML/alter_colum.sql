use mysql;
-- 2018.09.21 create by ljh
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_file_info' and COLUMN_NAME='latest_version';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.ota_file_info CHANGE latest_version version varchar(20) DEFAULT NULL COMMENT ''OTA 版本号''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2018.09.21 create by ljh
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_file_info' and COLUMN_NAME='version';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.ota_file_info ADD COLUMN version varchar(20) DEFAULT NULL COMMENT ''OTA 版本号''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



-- 2018.09.21 create by ljh
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_file_info' and COLUMN_NAME='download_url';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.ota_file_info ADD COLUMN download_url varchar(500) DEFAULT NULL COMMENT ''下载路径''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2018.09.21 create by ljh
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_file_info' and COLUMN_NAME='md5';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.ota_file_info ADD COLUMN md5 varchar(32) DEFAULT NULL COMMENT ''OTA md5值''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;