


-- 2018.07.24
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'ifttt_actuator' and COLUMN_NAME='object_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.ifttt_actuator ADD object_id varchar(64) DEFAULT NULL COMMENT ''外部对象id,视type内容而定''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2018.07.26
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_extend' and COLUMN_NAME='area';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_extend ADD area varchar(50) DEFAULT NULL COMMENT ''地区''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


-- 2018.07.30
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_type' and COLUMN_NAME='whether_soc';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_type ADD whether_soc  tinyint(2) NOT NULL DEFAULT 0 COMMENT  ''是否为免开发（0:非免开发；1：免开发）''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'activity_record' and COLUMN_NAME='user_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.`activity_record` ADD COLUMN `user_id`  bigint(20) NULL DEFAULT NULL AFTER `set_time`',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

ALTER TABLE iot_db_control.`ifttt_rule` MODIFY COLUMN `ifttt_type`  varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ifttt类型 \"_2B\"  \"_2C\"' AFTER `template_id`;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'location_scene_detail' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.`location_scene_detail` ADD COLUMN `tenant_id`  bigint(20) NULL DEFAULT NULL AFTER `scene_id`',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'location_scene_detail' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.`location_scene_detail` ADD COLUMN `tenant_id`  bigint(20) NULL DEFAULT NULL AFTER `scene_id`',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'template' and COLUMN_NAME='deploy_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.`template` ADD COLUMN `deploy_id`  bigint(20) NULL DEFAULT NULL COMMENT ''部署方式ID'' AFTER `template_type`',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

ALTER TABLE iot_db_control.`reservation` MODIFY COLUMN `tenant_id`  int(11) NULL DEFAULT NULL AFTER `flag`;
ALTER TABLE iot_db_control.`reservation` MODIFY COLUMN `type`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会议类型' AFTER `tenant_id`;

ALTER TABLE iot_db_control.`space` MODIFY COLUMN `model`  int(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '0 普通房间 1会议室' AFTER `org_id`;
ALTER TABLE iot_db_control.`space` MODIFY COLUMN `time_zone_offset`  int(8) NULL DEFAULT '-480' COMMENT '时区偏移量 单位分钟' AFTER `model`;

ALTER TABLE iot_db_control.`space_device` MODIFY COLUMN `business_type_id`  bigint(20) NULL DEFAULT NULL COMMENT '业务类型ID' AFTER `order`;



-- 2018.08.23 create by xfz
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'ifttt_rule' and COLUMN_NAME='update_time';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.ifttt_rule ADD update_time datetime DEFAULT NULL COMMENT ''更新时间''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2018.08.24 create by xby
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'location_scene' and COLUMN_NAME='build_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.location_scene ADD COLUMN build_id bigint(20) DEFAULT NULL COMMENT ''栋ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'location_scene' and COLUMN_NAME='floor_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.location_scene ADD COLUMN floor_id bigint(20) DEFAULT NULL COMMENT ''层ID''',
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

-- 2018.12.03 create by ljh
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'scene' and COLUMN_NAME='silence_status';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_control.`scene` ADD COLUMN silence_status tinyint(1) DEFAULT NULL COMMENT ''静默状态 0否1是''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


