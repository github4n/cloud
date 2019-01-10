ALTER TABLE `iot_db_device`.`service_module_property`
	ALTER `service_module_id` DROP DEFAULT,
	ALTER `version` DROP DEFAULT,
	ALTER `name` DROP DEFAULT,
	ALTER `min_value` DROP DEFAULT,
	ALTER `max_value` DROP DEFAULT,
	ALTER `allowed_values` DROP DEFAULT;
ALTER TABLE `iot_db_device`.`service_module_property`
	CHANGE COLUMN `service_module_id` `service_module_id` BIGINT(20) NULL COMMENT '模组id' AFTER `tenant_id`,
	CHANGE COLUMN `version` `version` VARCHAR(32) NULL COMMENT '版本' AFTER `service_module_id`,
	CHANGE COLUMN `name` `name` VARCHAR(32) NULL COMMENT '属性名称' AFTER `version`,
	CHANGE COLUMN `min_value` `min_value` VARCHAR(32) NULL COMMENT '最小值' AFTER `param_type`,
	CHANGE COLUMN `max_value` `max_value` VARCHAR(32) NULL COMMENT '最大值' AFTER `min_value`,
	CHANGE COLUMN `allowed_values` `allowed_values` VARCHAR(32) NULL COMMENT '允许值(enum 可以多个逗号隔开)' AFTER `max_value`;
