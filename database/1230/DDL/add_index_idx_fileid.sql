SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_file'
AND table_name = 'file_info'
AND index_name = 'idx_fileid';


SET @query = If(@exist=0,
		'ALTER TABLE iot_db_file.file_info ADD INDEX idx_fileid (file_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;