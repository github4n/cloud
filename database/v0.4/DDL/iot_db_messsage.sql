USE `iot_db_message`;

CREATE TABLE IF NOT EXISTS `iot_db_message`.`app_cert_info` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户id' ,
`app_id`  bigint(20) NOT NULL COMMENT 'APP应用id' ,
`cert_info`  blob NOT NULL COMMENT '证书信息' ,
`cert_pass_word`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '证书密码' ,
`service_host`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务主机' ,
`service_port`  int(5) NOT NULL COMMENT '服务端口' ,
`android_push_key`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '安卓推送秘钥' ,
`android_push_url`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '安卓推送链接' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
`update_time`  datetime NULL DEFAULT NULL COMMENT '更新时间' ,
`data_status`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据状态' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=Dynamic;

CREATE TABLE IF NOT EXISTS `iot_db_message`.`tenant_mail_info` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户ID' ,
`mail_host`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮件服务主机Host' ,
`mail_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱用户名' ,
`pass_word`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱密码' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
`update_time`  datetime NULL DEFAULT NULL COMMENT '更新时间' ,
`data_status`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据状态' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=Dynamic
;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_message' AND table_name = 'user_phone_relate' and COLUMN_NAME='app_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_message.user_phone_relate ADD app_id bigint(20) NULL DEFAULT NULL COMMENT ''APP应用id''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_message' AND table_name = 'user_phone_relate' and COLUMN_NAME='tenant_id';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_message.user_phone_relate ADD tenant_id bigint(20) NULL DEFAULT NULL COMMENT ''租户id''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;