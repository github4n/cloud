SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_control'
AND table_name = 'scene_detail'
AND index_name = 'idx_scene_id';


SET @query = If(@exist=0,
		'alter table iot_db_control.scene_detail add index idx_scene_id(scene_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;