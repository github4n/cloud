USE iot_db_tenant;
ALTER TABLE iot_db_tenant.`tenant` MODIFY COLUMN `lock_status`  tinyint(2) DEFAULT 0 COMMENT  '锁定状态(0:未锁定 1:锁定)';