USE iot_db_system;

ALTER TABLE iot_db_system.`lang_info` MODIFY COLUMN `lang_value`  varchar(5000)  COMMENT '国际化值';
