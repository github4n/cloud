USE iot_db_device;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'service_module_action' and COLUMN_NAME='ifttt_type';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.service_module_action ADD ifttt_type tinyint(2) NOT NULL DEFAULT 0 COMMENT ''ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'service_module_event' and COLUMN_NAME='ifttt_type';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.service_module_event ADD ifttt_type tinyint(2) NOT NULL DEFAULT 0 COMMENT ''ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'service_module_property' and COLUMN_NAME='ifttt_type';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.service_module_property ADD ifttt_type tinyint(2) NOT NULL DEFAULT 0 COMMENT ''ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_type' and COLUMN_NAME='ifttt_type';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_type ADD ifttt_type tinyint(2) NOT NULL DEFAULT 0 COMMENT ''ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'service_module_property' and COLUMN_NAME='property_type';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.service_module_property ADD property_type tinyint(2) NOT NULL DEFAULT 0 COMMENT ''property类型(0:property类型 1：参数类型)''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'module_action_to_property' and COLUMN_NAME='param_type';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.module_action_to_property ADD param_type tinyint(2) NOT NULL DEFAULT 0 COMMENT ''param类型(0:入参 1：出参)''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'service_module_action' and COLUMN_NAME='portal_ifttt_type';
SET @query = If(@exist=0,
'ALTER TABLE iot_db_device.service_module_action ADD COLUMN `portal_ifttt_type` tinyint(2) NOT NULL DEFAULT 0 COMMENT \'portal联动设置(0:不支持 1：支持if 2:支持then 3:支持if支持then)\'',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'service_module_event' and COLUMN_NAME='portal_ifttt_type';
SET @query = If(@exist=0,
'ALTER TABLE iot_db_device.service_module_event ADD COLUMN `portal_ifttt_type` tinyint(2) NOT NULL DEFAULT 0 COMMENT \'portal联动设置(0:不支持 1：支持if 2:支持then 3:支持if支持then)\'',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'service_module_property' and COLUMN_NAME='portal_ifttt_type';
SET @query = If(@exist=0,
'ALTER TABLE iot_db_device.service_module_property ADD COLUMN `portal_ifttt_type` tinyint(2) NOT NULL DEFAULT 0 COMMENT \'portal联动设置(0:不支持 1：支持if 2:支持then 3:支持if支持then)\'',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

ALTER TABLE iot_db_device.product MODIFY COLUMN `develop_status` tinyint(2) DEFAULT NULL COMMENT '开发状态,0:未开发,1:开发中,2:已上线,3:发布中；';

ALTER TABLE iot_db_device.service_module_property MODIFY COLUMN `allowed_values` text DEFAULT NULL COMMENT '允许值(多个值的json串)';


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'service_module' and COLUMN_NAME='component_type';
SET @query = If(@exist=0,
'ALTER TABLE iot_db_device.service_module ADD COLUMN `component_type` VARCHAR(255) DEFAULT NULL COMMENT \'功能组控件类型\'',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;








