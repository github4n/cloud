SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_file' AND table_name = 'file_info' and COLUMN_NAME='file_name';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_file.file_info ADD file_name varchar(100) DEFAULT NULL COMMENT ''文件名称''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;