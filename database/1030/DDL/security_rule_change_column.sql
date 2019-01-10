USE iot_db_control;

ALTER TABLE iot_db_control.`security_rule` MODIFY COLUMN `if` text DEFAULT NULL COMMENT 'if条件配置';
ALTER TABLE iot_db_control.`security_rule` MODIFY COLUMN `then` text DEFAULT NULL COMMENT 'then条件配置';