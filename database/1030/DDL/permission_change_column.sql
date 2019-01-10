USE iot_db_permission;

ALTER TABLE iot_db_permission.`permission` MODIFY COLUMN `system_type`  enum('Boss','2B','2C','Portal') DEFAULT '2B' COMMENT '系统类别';
ALTER TABLE iot_db_permission.`role` MODIFY COLUMN `role_type` enum('Boss','2B','2C','Portal') DEFAULT '2B' COMMENT '角色类型:（Boss;2B(默认)；2C；Portal）';
