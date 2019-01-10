CREATE TABLE IF NOT EXISTS `iot_db_device`.`generate_module_agreement` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255) NULL DEFAULT NULL COMMENT '名字',
	`abbreviation_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '简称',
	`create_by` BIGINT(20) NULL DEFAULT NULL,
	`create_time` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
	`update_by` BIGINT(20) NULL DEFAULT NULL,
	`update_time` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
	`description` VARCHAR(255) NULL DEFAULT NULL COMMENT '描述',
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=8;


REPLACE INTO `iot_db_device`.`generate_module_agreement` (`id`, `name`, `abbreviation_name`, `create_by`, `create_time`, `update_by`, `update_time`, `description`) VALUES (1, 'zigbee', 'ZB', NULL, '2018-09-17 06:15:55', NULL, '2018-09-17 06:15:55', NULL);
REPLACE INTO `iot_db_device`.`generate_module_agreement` (`id`, `name`, `abbreviation_name`, `create_by`, `create_time`, `update_by`, `update_time`, `description`) VALUES (2, 'BLE SIG mesh', 'BLE', NULL, '2018-09-17 06:17:27', NULL, '2018-09-17 14:18:50', NULL);
REPLACE INTO `iot_db_device`.`generate_module_agreement` (`id`, `name`, `abbreviation_name`, `create_by`, `create_time`, `update_by`, `update_time`, `description`) VALUES (3, 'Z-wave', 'ZW', NULL, '2018-09-17 14:18:57', NULL, '2018-09-17 06:17:31', NULL);
REPLACE INTO `iot_db_device`.`generate_module_agreement` (`id`, `name`, `abbreviation_name`, `create_by`, `create_time`, `update_by`, `update_time`, `description`) VALUES (4, 'Wi-Fi', 'WF', NULL, '2018-09-17 14:19:01', NULL, '2018-09-17 06:17:34', NULL);
REPLACE INTO `iot_db_device`.`generate_module_agreement` (`id`, `name`, `abbreviation_name`, `create_by`, `create_time`, `update_by`, `update_time`, `description`) VALUES (5, 'sub-G', 'SG', NULL, '2018-09-17 14:19:03', NULL, '2018-09-17 06:17:37', NULL);
REPLACE INTO `iot_db_device`.`generate_module_agreement` (`id`, `name`, `abbreviation_name`, `create_by`, `create_time`, `update_by`, `update_time`, `description`) VALUES (6, 'DALI\r\n', 'DA\r\n', NULL, '2018-09-17 14:19:06', NULL, '2018-09-17 06:17:40', NULL);
REPLACE INTO `iot_db_device`.`generate_module_agreement` (`id`, `name`, `abbreviation_name`, `create_by`, `create_time`, `update_by`, `update_time`, `description`) VALUES (7, 'KNX', 'KNX', NULL, '2018-09-17 14:19:10', NULL, '2018-09-17 06:17:44', NULL);
