-- ----------------------------
-- Table structure for ota_device_version
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_device`.`ota_device_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `device_id` varchar(32) NOT NULL COMMENT '设备id',
  `fw_type` tinyint(4) NOT NULL COMMENT '固件类型',
  `version` varchar(50) NOT NULL COMMENT '对应的固件版本',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='设备-固件类型-版本表';

-- ----------------------------
-- Table structure for ota_firmware_version
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_device`.`ota_firmware_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id 主键主动增长',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `ota_version` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '版本号',
  `ota_type` enum('WholePackage','Difference') CHARACTER SET utf8mb4 NOT NULL COMMENT '整包升级WholePackage  差分升级Difference',
  `ota_file_id` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '升级文件id',
  `ota_md5` varchar(50) CHARACTER SET utf8mb4 NOT NULL COMMENT 'MD5值',
  `fw_type` enum('0','1','2','3','4') DEFAULT NULL COMMENT '分位的类型 0:所有的模块在一个分位里面(默认) 1:wifi 模块的分位 2: zigbee模块的分位 3: z-wave模块的分位 4：ble模块的分位 ',
  `version_online_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '版本上线时间',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`tenant_id`,`product_id`,`ota_version`) USING BTREE COMMENT '复合主键',
  KEY `normal` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='OTA固件版本表';

-- ----------------------------
-- Table structure for ota_upgrade_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_device`.`ota_upgrade_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `plan_id` bigint(20) NOT NULL COMMENT '升级计划id',
  `device_uuid` varchar(36) CHARACTER SET utf8mb4 NOT NULL COMMENT '设备uuid',
  `upgrade_type` enum('Push','Force') CHARACTER SET utf8mb4 NOT NULL COMMENT '升级方式  推送升级Push   强制升级Force',
  `original_version` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '原版本号',
  `target_version` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '升级版本号',
  `upgrade_result` enum('Success','Failed') DEFAULT 'Failed' COMMENT '升级结果',
  `complete_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
  PRIMARY KEY (`id`),
  KEY `plan_id_index` (`plan_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='OTA升级日志表';

-- ----------------------------
-- Table structure for ota_upgrade_plan
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_device`.`ota_upgrade_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '计划id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `plan_status` enum('Start','Pause') NOT NULL DEFAULT 'Pause' COMMENT '启动Start 暂停Pause',
  `upgrade_type` enum('Push','Force') DEFAULT 'Push' COMMENT '升级方式  推送升级Push(默认) 强制升级Force',
  `target_version` varchar(32) DEFAULT NULL COMMENT '升级版本号',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `edited_times` int(10) DEFAULT '0' COMMENT '编辑次数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`product_id`) USING BTREE,
  KEY `tenant_id_index` (`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级计划表';

-- ----------------------------
-- Table structure for ota_upgrade_plan_detail
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_device`.`ota_upgrade_plan_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细id',
  `plan_id` bigint(20) NOT NULL COMMENT '升级计划id',
  `has_transition` tinyint(1) NOT NULL COMMENT '是否有过渡版本 true/false',
  PRIMARY KEY (`id`),
  KEY `plan_id_index` (`plan_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级计划明细表';

-- ----------------------------
-- Table structure for ota_upgrade_plan_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_device`.`ota_upgrade_plan_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `plan_id` bigint(20) NOT NULL COMMENT '升级计划id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `operation_type` enum('Start','Pause','Edit') CHARACTER SET utf8mb4 NOT NULL COMMENT '操作类型',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  KEY `plan_id_index` (`plan_id`) USING BTREE,
  KEY `tenant_id_index` (`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='OTA升级计划操作日志表';

-- ----------------------------
-- Table structure for ota_upgrade_plan_substitute
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_device`.`ota_upgrade_plan_substitute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `detail_id` bigint(20) NOT NULL COMMENT '明细id',
  `substitute_version` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '替换版本号',
  PRIMARY KEY (`id`),
  KEY `detail_id_index` (`detail_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='OTA升级替代版本表';

-- ----------------------------
-- Table structure for ota_upgrade_plan_transition
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_device`.`ota_upgrade_plan_transition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `detail_id` bigint(20) NOT NULL COMMENT '明细id',
  `transition_version` varchar(32) NOT NULL COMMENT '过渡版本号',
  PRIMARY KEY (`id`),
  KEY `detail_id_index` (`detail_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级过渡版本表';

DROP TABLE IF EXISTS `iot_db_device`.`device_to_version`;

DROP TABLE IF EXISTS `iot_db_device`.`device_version`;

DROP TABLE IF EXISTS `iot_db_device`.`upgrade_version_log`;