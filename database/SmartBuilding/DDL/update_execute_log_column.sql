ALTER TABLE iot_db_build.`execute_log`
MODIFY COLUMN `exe_content`  varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行结果内容';