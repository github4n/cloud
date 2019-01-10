use iot_db_device;

ALTER TABLE `iot_db_device`.`service_module_action`
	ALTER `version` DROP DEFAULT,
	ALTER `name` DROP DEFAULT;
ALTER TABLE `iot_db_device`.`service_module_action`
	CHANGE COLUMN `version` `version` VARCHAR(32) NULL COMMENT '版本' AFTER `service_module_id`,
	CHANGE COLUMN `name` `name` VARCHAR(32) NULL COMMENT '方法名称' AFTER `version`,
	CHANGE COLUMN `ifttt_type` `ifttt_type` TINYINT(2) NULL DEFAULT '0' COMMENT 'ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)' AFTER `description`,
	CHANGE COLUMN `portal_ifttt_type` `portal_ifttt_type` TINYINT(2) NULL DEFAULT '0' COMMENT 'portal联动设置(0:不支持 1：支持if 2:支持then 3:支持if支持then)' AFTER `ifttt_type`;
