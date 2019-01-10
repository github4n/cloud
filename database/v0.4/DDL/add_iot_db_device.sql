

USE `iot_db_device`;

SET FOREIGN_KEY_CHECKS=0;


CREATE TABLE IF NOT EXISTS iot_db_device.`device_remote_control` (
`id`  bigint(32) NOT NULL ,
`device_type_id`  bigint(32) NOT NULL ,
`key_code`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`type`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'GROUP 组控；SCENE 情景；SINGLE 单控' ,
`default_value`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '默认值' ,
`event_status`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'pressed/released/held down  短按/长按释放/长按 ' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=Dynamic;



CREATE TABLE IF NOT EXISTS  iot_db_device.`ota_device_version` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id' ,
`device_id`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id' ,
`fw_type`  tinyint(4) NOT NULL COMMENT '固件类型' ,
`version`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对应的固件版本' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
`update_time`  datetime NULL DEFAULT NULL COMMENT '修改时间' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic;


CREATE TABLE IF NOT EXISTS  iot_db_device.`ota_firmware_version` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id 主键主动增长' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户id' ,
`product_id`  bigint(20) NOT NULL COMMENT '产品id' ,
`ota_version`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号' ,
`ota_type`  enum('WholePackage','Difference') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '整包升级WholePackage  差分升级Difference' ,
`ota_file_id`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '升级文件id' ,
`ota_md5`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'MD5值' ,
`fw_type`  enum('0','1','2','3','4') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分位的类型 0:所有的模块在一个分位里面(默认) 1:wifi 模块的分位 2: zigbee模块的分位 3: z-wave模块的分位 4：ble模块的分位 ' ,
`version_online_time`  datetime NULL DEFAULT NULL COMMENT '版本上线时间' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
`create_by`  bigint(20) NULL DEFAULT NULL COMMENT '创建人' ,
`update_time`  datetime NULL DEFAULT NULL COMMENT '修改时间' ,
`update_by`  bigint(20) NULL DEFAULT NULL COMMENT '修改人' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`tenant_id`, `product_id`, `ota_version`) USING BTREE COMMENT '复合主键'
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=Dynamic;


CREATE TABLE IF NOT EXISTS  iot_db_device.`ota_upgrade_log` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志id' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户id' ,
`plan_id`  bigint(20) NOT NULL COMMENT '升级计划id' ,
`device_uuid`  varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备uuid' ,
`upgrade_type`  enum('Push','Force') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '升级方式  推送升级Push   强制升级Force' ,
`target_version`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '升级版本号' ,
`original_version`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原版本号' ,
`upgrade_result`  enum('Success','Failed') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'Failed' COMMENT '升级结果' ,
`complete_time`  datetime NULL DEFAULT NULL COMMENT '完成时间' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=Dynamic;


CREATE TABLE IF NOT EXISTS  iot_db_device.`ota_upgrade_plan` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '计划id' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户id' ,
`product_id`  bigint(20) NOT NULL COMMENT '产品id' ,
`plan_status`  enum('Start','Pause') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Pause' COMMENT '启动Start 暂停Pause' ,
`upgrade_type`  enum('Push','Force') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Push' COMMENT '升级方式  推送升级Push(默认) 强制升级Force' ,
`target_version`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '升级版本号' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
`create_by`  bigint(20) NULL DEFAULT NULL COMMENT '创建人' ,
`update_time`  datetime NULL DEFAULT NULL COMMENT '修改时间' ,
`update_by`  bigint(20) NULL DEFAULT NULL COMMENT '修改人' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `productId_index` (`product_id`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic;



CREATE TABLE IF NOT EXISTS  iot_db_device.`ota_upgrade_plan_detail` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细id' ,
`plan_id`  bigint(20) NOT NULL COMMENT '升级计划id' ,
`has_transition`  tinyint(1) NOT NULL COMMENT '是否有过渡版本 true/false' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic;


CREATE TABLE IF NOT EXISTS  iot_db_device.`ota_upgrade_plan_log` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志id' ,
`plan_id`  bigint(20) NOT NULL COMMENT '升级计划id' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户id' ,
`operation_type`  enum('Start','Pause','Edit') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
`create_by`  bigint(20) NULL DEFAULT NULL COMMENT '创建人' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=Dynamic;


CREATE TABLE IF NOT EXISTS  iot_db_device.`ota_upgrade_plan_substitute` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`detail_id`  bigint(20) NOT NULL COMMENT '明细id' ,
`substitute_version`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '替换版本号' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=Dynamic;


CREATE TABLE IF NOT EXISTS  iot_db_device.`ota_upgrade_plan_transition` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`detail_id`  bigint(20) NOT NULL COMMENT '明细id' ,
`transition_version`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '过渡版本号' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic;

SET FOREIGN_KEY_CHECKS=1;