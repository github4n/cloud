CREATE TABLE IF NOT EXISTS iot_db_tenant.`app_version_log` (
	`id` BIGINT(20) NOT NULL,
	`system_info` VARCHAR(255) NOT NULL,
	`app_package` VARCHAR(255) NOT NULL,
	`key` VARCHAR(255) NOT NULL,
	`app_name` VARCHAR(255) NOT NULL,
	`version` VARCHAR(255) NOT NULL,
	`discribes` VARCHAR(255) NOT NULL,
	`down_location` VARCHAR(255) NOT NULL,
	`create_time` DATETIME NOT NULL,
	`update_time` DATETIME NOT NULL,
	`create_by` BIGINT NOT NULL,
	`update_by` BIGINT NOT NULL,
	`tenant_id` BIGINT NOT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;




ALTER TABLE iot_db_tenant.`app_version_log` MODIFY COLUMN `id`  bigint(20) NOT NULL AUTO_INCREMENT FIRST ;


ALTER TABLE iot_db_tenant.`app_version_log`
	ALTER `system_info` DROP DEFAULT,
	ALTER `app_package` DROP DEFAULT,
	ALTER `key` DROP DEFAULT,
	ALTER `app_name` DROP DEFAULT,
	ALTER `version` DROP DEFAULT,
	ALTER `discribes` DROP DEFAULT,
	ALTER `down_location` DROP DEFAULT,
	ALTER `create_time` DROP DEFAULT,
	ALTER `update_time` DROP DEFAULT,
	ALTER `create_by` DROP DEFAULT,
	ALTER `update_by` DROP DEFAULT,
	ALTER `tenant_id` DROP DEFAULT;
ALTER TABLE iot_db_tenant.`app_version_log`
	CHANGE COLUMN `system_info` `system_info` VARCHAR(255) NULL AFTER `id`,
	CHANGE COLUMN `app_package` `app_package` VARCHAR(255) NULL AFTER `system_info`,
	CHANGE COLUMN `key` `key` VARCHAR(255) NULL AFTER `app_package`,
	CHANGE COLUMN `app_name` `app_name` VARCHAR(255) NULL AFTER `key`,
	CHANGE COLUMN `version` `version` VARCHAR(255) NULL AFTER `app_name`,
	CHANGE COLUMN `discribes` `discribes` VARCHAR(255) NULL AFTER `version`,
	CHANGE COLUMN `down_location` `down_location` VARCHAR(255) NULL AFTER `discribes`,
	CHANGE COLUMN `create_time` `create_time` DATETIME NULL AFTER `down_location`,
	CHANGE COLUMN `update_time` `update_time` DATETIME NULL AFTER `create_time`,
	CHANGE COLUMN `create_by` `create_by` BIGINT(20) NULL AFTER `update_time`,
	CHANGE COLUMN `update_by` `update_by` BIGINT(20) NULL AFTER `create_by`,
	CHANGE COLUMN `tenant_id` `tenant_id` BIGINT(20) NULL AFTER `update_by`;
