use iot_db_device;

ALTER TABLE `iot_db_device`.`service_module_property`
	CHANGE COLUMN `portal_ifttt_type` `portal_ifttt_type` TINYINT(2) NULL DEFAULT '0' COMMENT 'portal联动设置(0:不支持 1：支持if 2:支持then 3:支持if支持then)' AFTER `description`,
	CHANGE COLUMN `ifttt_type` `ifttt_type` TINYINT(2) NULL DEFAULT '0' COMMENT 'ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)' AFTER `portal_ifttt_type`,
	CHANGE COLUMN `property_type` `property_type` TINYINT(2) NULL DEFAULT '0' COMMENT 'property类型(0:property类型 1：参数类型)' AFTER `ifttt_type`,
	CHANGE COLUMN `in_home_page` `in_home_page` TINYINT(2) NULL DEFAULT '0' COMMENT '是否显示在首页' AFTER `property_type`;
