-- 2018.08.09
CREATE TABLE IF NOT EXISTS iot_db_user.`online_debug` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键id' ,
`uuid`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户uuid' ,
`state`  tinyint(2) NOT NULL COMMENT '用户debug权限状态 0关闭状态 1开启状态' ,
`create_time`  datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
`update_time`  datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间' ,
PRIMARY KEY (`id`),
INDEX `uuid` (`uuid`) USING BTREE COMMENT '创建普通索引'
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Dynamic
;

CREATE TABLE IF NOT EXISTS iot_db_user.`user_feedback` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键id' ,
`user_id`  bigint(20) NOT NULL COMMENT '用户主键' ,
`feedback_content`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户反馈内容' ,
`create_time`  datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
`update_time`  datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Dynamic
;

ALTER TABLE iot_db_user.`user_login` MODIFY COLUMN `os`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机操作系统(1，iOS 2，Android)' AFTER `phone_id`;
ALTER TABLE iot_db_user.`smart_token` MODIFY COLUMN `smart`  tinyint(1) NULL DEFAULT NULL AFTER `user_id`;

ALTER TABLE iot_db_user.`user` MODIFY COLUMN `user_level`  tinyint(2) NULL DEFAULT 3 COMMENT '区分用户级别(1:BOSS;2:企业级客户;3:终端用户)' AFTER `address`;
ALTER TABLE iot_db_user.`user` MODIFY COLUMN `admin_status`  tinyint(2) NULL DEFAULT NULL COMMENT '管理员标识(当user_level为1或2时，1：超级管理员;2:普通用户)' AFTER `user_level`;
ALTER TABLE iot_db_user.`user` MODIFY COLUMN `user_status`  enum('unreviewed','auditFailed','normal','deleted') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'normal' COMMENT '用户状态（未审核:unreviewed，审核未通过:auditFailed，正常:normal，已删除:deleted）' AFTER `update_time`;
ALTER TABLE iot_db_user.`lang_info` MODIFY COLUMN `is_deleted`  enum('invalid','valid') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'valid' COMMENT '数据有效性' AFTER `update_time`;