USE iot_db_tenant;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='apply_audit_time';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `apply_audit_time` DATETIME COMMENT ''app申请审核时间'' ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='confirm_time';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `confirm_time` DATETIME NULL DEFAULT NULL COMMENT ''确认时间'' ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='confirm_status';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `confirm_status` INT NULL DEFAULT NULL COMMENT ''确认状态'' ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='display_identification';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `display_identification` INT NULL DEFAULT NULL COMMENT ''显示标识'' ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='background_color';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `background_color` VARCHAR(255) NULL COMMENT ''背景色'' ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='image_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `image_id` VARCHAR(255) NULL COMMENT ''图片'' ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='privacy_cn_link_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `privacy_cn_link_id` VARCHAR(255) NULL DEFAULT NULL COMMENT ''隐私链接中文'' ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='privacy_en_link_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_info ADD COLUMN `privacy_en_link_id` VARCHAR(255) NULL DEFAULT NULL COMMENT ''隐私链接英文'' ',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

