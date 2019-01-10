


USE `iot_db_device`;

SET FOREIGN_KEY_CHECKS=0;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'daily_electricity_statistics' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.daily_electricity_statistics ADD COLUMN tenant_id  bigint(20) NULL DEFAULT NULL COMMENT ''租户id''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_business_type' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.device_business_type ADD COLUMN tenant_id  bigint(20) int(11) NULL DEFAULT NULL',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'min_electricity_statistics' and COLUMN_NAME='area';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.min_electricity_statistics ADD COLUMN area  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ''设备所在地区''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;




SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'min_electricity_statistics' and COLUMN_NAME='localtime';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.min_electricity_statistics ADD COLUMN `localtime`  datetime NULL DEFAULT NULL COMMENT ''设备上报时间'',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'min_runtime' and COLUMN_NAME='area';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.min_runtime ADD COLUMN area  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL  COMMENT ''设备所在地区'',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'min_runtime' and COLUMN_NAME='localtime';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.min_runtime ADD COLUMN `localtime`   datetime NULL DEFAULT NULL COMMENT ''设备上报时间''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



ALTER TABLE `cust_p2pid` MODIFY COLUMN `use_mark`  tinyint(1) NULL DEFAULT NULL AFTER `p2p_id`;
ALTER TABLE `cust_uuid_manage` MODIFY COLUMN `cust_code`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '客户代码' AFTER `create_time`;
ALTER TABLE `daily_electricity_statistics` MODIFY COLUMN `electric_value`  double(20,3) NULL DEFAULT 0.000 COMMENT '电量值' AFTER `device_id`;
ALTER TABLE `daily_runtime` MODIFY COLUMN `runtime`  bigint(20) NULL DEFAULT 0 COMMENT '运行时间' AFTER `device_id`;
ALTER TABLE `data_point` DROP PRIMARY KEY;
ALTER TABLE `data_point` MODIFY COLUMN `property_code`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '属性标识' AFTER `label_name`;
ALTER TABLE `data_point` MODIFY COLUMN `property`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数值属性' AFTER `create_by`;
ALTER TABLE `data_point` MODIFY COLUMN `description`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述' AFTER `property`;
ALTER TABLE `data_point` MODIFY COLUMN `is_custom`  tinyint(1) NULL DEFAULT NULL AFTER `description`;
ALTER TABLE `data_point` ADD PRIMARY KEY (`id`, `property_code`);


ALTER TABLE `device_extend` MODIFY COLUMN `p2p_id`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'p2pid(主键)' AFTER `device_id`;
ALTER TABLE `device_extend` MODIFY COLUMN `uuid_validity_days`  decimal(5,0) NULL DEFAULT NULL COMMENT '有效时长' AFTER `p2p_id`;
ALTER TABLE `device_extend` MODIFY COLUMN `device_cipher`  varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备密码' AFTER `uuid_validity_days`;
ALTER TABLE `device_extend` MODIFY COLUMN `create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间' AFTER `device_cipher`;
ALTER TABLE `device_extend` MODIFY COLUMN `first_upload_sub_dev`  tinyint(1) NULL DEFAULT 1 COMMENT '是否首次上传子设备,1是,0否(带套包的网关需要)' AFTER `create_time`;
ALTER TABLE `device_extend` MODIFY COLUMN `unbind_flag`  tinyint(4) NOT NULL DEFAULT 0 AFTER `first_upload_sub_dev`;
ALTER TABLE `device_extend` MODIFY COLUMN `reset_flag`  tinyint(4) NOT NULL DEFAULT 0 AFTER `unbind_flag`;
ALTER TABLE `device_extend` MODIFY COLUMN `batch_num`  bigint(20) NULL DEFAULT NULL COMMENT '批次号' AFTER `reset_flag`;
ALTER TABLE `device_extend` MODIFY COLUMN `batch_num_id`  varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次号' AFTER `batch_num`;
CREATE UNIQUE INDEX `device_id` ON `device_extend`(`device_id`) USING BTREE ;



ALTER TABLE `device_status` MODIFY COLUMN `online_status`  enum('disconnected','connected') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'disconnected' COMMENT '在线状态' AFTER `active_time`;
CREATE UNIQUE INDEX `device_id` ON `device_status`(`device_id`) USING BTREE ;
ALTER TABLE `min_electricity_statistics` MODIFY COLUMN `time`  datetime NULL DEFAULT NULL COMMENT '统计时间点(UTC时间）' AFTER `step`;
ALTER TABLE `min_electricity_statistics` MODIFY COLUMN `electric_value`  double(20,3) NULL DEFAULT 0.000 COMMENT '电量值' AFTER `time`;
ALTER TABLE `min_runtime` MODIFY COLUMN `time`  datetime NULL DEFAULT NULL COMMENT '统计时间点（UTC时间）' AFTER `step`;
ALTER TABLE `min_runtime` MODIFY COLUMN `runtime`  bigint(20) NULL DEFAULT 0 COMMENT '运行时间' AFTER `time`;

ALTER TABLE `monthly_electricity_statistics` MODIFY COLUMN `electric_value`  double(20,3) NULL DEFAULT 0.000 COMMENT '电量值' AFTER `device_id`;

ALTER TABLE `monthly_runtime` MODIFY COLUMN `runtime`  bigint(20) NULL DEFAULT 0 COMMENT '运行时间' AFTER `device_id`;



ALTER TABLE `product` MODIFY COLUMN `icon`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `is_direct_device`;

ALTER TABLE `weekly_electricity_statistics` MODIFY COLUMN `electric_value`  double(20,3) NULL DEFAULT 0.000 COMMENT '电量值' AFTER `device_id`;

ALTER TABLE `weekly_runtime` MODIFY COLUMN `runtime`  bigint(20) NULL DEFAULT 0 COMMENT '运行时间' AFTER `device_id`;

SET FOREIGN_KEY_CHECKS=1;