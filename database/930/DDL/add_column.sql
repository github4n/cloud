USE iot_db_device;

-- 2018.09.07 by xfz add product step
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'product' and COLUMN_NAME='step';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.product ADD step tinyint(2) NOT NULL DEFAULT 0 COMMENT ''产品步骤''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




-- 2018.09.12 by laiguiming modify version

ALTER TABLE iot_db_tenant.`app_version` MODIFY COLUMN `version`  varchar(50) NULL DEFAULT NULL COMMENT '版本号';
ALTER TABLE iot_db_tenant.`app_version` MODIFY COLUMN `full_file_id`  varchar(100) NULL DEFAULT NULL COMMENT '全量包文件ID';
ALTER TABLE iot_db_tenant.`app_version` MODIFY COLUMN `incr_file_id`  varchar(100) NULL DEFAULT NULL COMMENT '增量包文件ID';






-- create by xfz 2018-09-20

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'cust_p2pid' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.cust_p2pid ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'cust_uuid_manage' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.cust_uuid_manage ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'data_point' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.data_point ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;





SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'data_point_style' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.data_point_style ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;





SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_business_type' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_business_type ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;






SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_remote_control' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_remote_control ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;





SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_remote_control_scene' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_remote_control_scene ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_remote_control_scene_template' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_remote_control_scene_template ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_remote_control_template' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_remote_control_template ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_sn' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_sn ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_state' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_state ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_type_data_point' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_type_data_point ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_type_style_template' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_type_style_template ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_type_to_style' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_type_to_style ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;





SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'license_usage' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.license_usage ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;





SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'product_style_template' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.product_style_template ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;





SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'product_to_style' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.product_to_style ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;





SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'smart_data_point' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.smart_data_point ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;





SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'smart_device_type' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.smart_device_type ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;





SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'style_template' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.style_template ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_device_version' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.ota_device_version ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_upgrade_log' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.ota_upgrade_log ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_upgrade_plan_detail' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.ota_upgrade_plan_detail ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'ota_upgrade_plan_substitute' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.ota_upgrade_plan_substitute ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT ''租户ID''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



