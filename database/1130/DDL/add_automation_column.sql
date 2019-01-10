USE iot_db_control;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'automation' and COLUMN_NAME='dev_scene_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.automation ADD dev_scene_id int(11) DEFAULT NULL COMMENT ''ble设备对应的Id与dev_timer_id共同确认autoId''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'automation' and COLUMN_NAME='dev_timer_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.automation ADD dev_timer_id int(11) DEFAULT NULL COMMENT ''ble设备对应的Id与dev_scene_id共同确认autoId''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;







