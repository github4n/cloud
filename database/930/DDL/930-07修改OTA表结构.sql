SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_upgrade_plan' and COLUMN_NAME='edited_times';

SET @query = If(@exist=0,
'ALTER TABLE iot_db_device.ota_upgrade_plan ADD edited_times int(10) DEFAULT 0 COMMENT ''编辑次数''',
'SELECT \'nothing\' status');