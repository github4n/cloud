USE iot_db_control;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'scene' and COLUMN_NAME='dev_scene_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.scene ADD COLUMN `dev_scene_id` bigint(20) DEFAULT NULL COMMENT ''设备控制sceneId''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
