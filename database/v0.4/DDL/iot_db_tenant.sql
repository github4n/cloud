CREATE TABLE IF NOT EXISTS iot_db_tenant.`app_product_info` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`app_product_id`  bigint(20) NULL DEFAULT NULL COMMENT '配网分类主键' ,
`type`  int(2) NULL DEFAULT NULL COMMENT '文案类型 0 配网文案 1 按钮文案 2 帮助文案' ,
`lang`  int(2) NULL DEFAULT NULL ,
`content`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`mode`  int(2) NULL DEFAULT NULL COMMENT '模式 0 SmartConfig模式 1 AP模式' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户ID' ,
PRIMARY KEY (`id`)
)

CREATE TABLE IF NOT EXISTS iot_db_tenant.`app_product` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`product_id`  bigint(20) NULL DEFAULT NULL COMMENT '关联产品主键' ,
`app_id`  bigint(20) NULL DEFAULT NULL COMMENT '应用ID' ,
`smart_img`  varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '配网引导图id smartConfig' ,
`ap_img`  varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT 'ap 配网图fileId' ,
`choose_lang`  varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '多语言中 已选语言 1,2,3' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户ID' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
PRIMARY KEY (`id`)
)

-- 2018.08.09
ALTER TABLE iot_db_tenant.`app_info` MODIFY COLUMN `status`  int(2) NULL DEFAULT 0 COMMENT '应用状态 0未打包 1打包中 2打包成功 3打包失败' AFTER `choose_lang`;
ALTER TABLE iot_db_tenant.`tenant` MODIFY COLUMN `business`  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '主营业务' AFTER `name`;
ALTER TABLE iot_db_tenant.`tenant` MODIFY COLUMN `job`  varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '职务' AFTER `contacts`;
DROP TABLE iot_db_tenant.`app_pack`;

--2018.08.15
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='google_url';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.`app_info` ADD COLUMN `google_url`  varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT "谷歌推送url" AFTER `fcm_file_id`;',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_info' and COLUMN_NAME='google_key';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.`app_info` ADD COLUMN `google_key`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT "谷歌推送秘钥" AFTER `google_url`',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

