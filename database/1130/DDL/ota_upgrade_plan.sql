SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_upgrade_plan' and COLUMN_NAME='strategy_switch';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.ota_upgrade_plan ADD strategy_switch tinyint(2) NULL DEFAULT 0 COMMENT ''策略开关 0:不使用 1：使用''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_upgrade_plan' and COLUMN_NAME='upgrade_scope';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.ota_upgrade_plan ADD upgrade_scope tinyint(2) NULL DEFAULT 0 COMMENT ''升级范围 0:全量升级 1：批次升级 2：指定uuid升级''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

