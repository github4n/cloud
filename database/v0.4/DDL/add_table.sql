
-- -------------------------------------------------------------------------------------------------------------------------


USE `iot_db_control`;

-- ----------------------------
-- Table structure for style_template
-- ----------------------------

CREATE TABLE IF NOT EXISTS iot_db_control.`group_info` (
  `id` bigint(20) NOT NULL,
  `group_id` varchar(32) DEFAULT NULL COMMENT '分组ID',
  `name` varchar(50) DEFAULT NULL COMMENT '分组名称',
  `gateway_id` varchar(32) DEFAULT NULL COMMENT '直连设备ID',
  `model` varchar(50) DEFAULT NULL,
  `remote_id` varchar(32) DEFAULT NULL COMMENT '遥控器ID',
  PRIMARY KEY (`id`),
  KEY `goupIdIndex` (`group_id`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分组表';

CREATE TABLE IF NOT EXISTS iot_db_control.`camera_record` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`ip`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`title`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`time`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`register`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`url`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`create_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Dynamic;

CREATE TABLE IF NOT EXISTS iot_db_control.`deployment` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`deploy_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Dynamic;

CREATE TABLE IF NOT EXISTS iot_db_control.`location_scene_relation` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`location_scene_id`  int(11) NULL DEFAULT NULL ,
`location_id`  int(11) NULL DEFAULT NULL ,
`start_cron`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`end_cron`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Dynamic;

CREATE TABLE IF NOT EXISTS iot_db_control.`region_hour_record` (
`id`  bigint(10) NOT NULL AUTO_INCREMENT ,
`ch1`  bigint(10) NULL DEFAULT NULL ,
`ch2`  bigint(10) NULL DEFAULT NULL ,
`ch3`  bigint(10) NULL DEFAULT NULL ,
`ch4`  bigint(10) NULL DEFAULT NULL ,
`time_flag`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`time`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`create_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Dynamic;

CREATE TABLE IF NOT EXISTS iot_db_control.`region_record` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`ch1`  bigint(10) NULL DEFAULT NULL ,
`ch2`  bigint(10) NULL DEFAULT NULL ,
`ch3`  bigint(10) NULL DEFAULT NULL ,
`ch4`  bigint(10) NULL DEFAULT NULL ,
`time`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`create_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Dynamic;

CREATE TABLE IF NOT EXISTS iot_db_control.`allocation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `allocation_id` bigint(20) DEFAULT NULL COMMENT '功能id',
  `exe_status` int(1) DEFAULT NULL COMMENT '执行状态：1：未开始 2；运行中 3；已结束',
  `exe_result` int(1) DEFAULT NULL COMMENT '最后一次执行结果：1：正常 2；异常',
  `is_loop` int(1) DEFAULT NULL COMMENT '是否循环：0：否 1；是',
  `select_week` varchar(64) DEFAULT NULL COMMENT '选择循环星期信息',
  `space_ids` varchar(256) DEFAULT NULL COMMENT '房间信息ID',
  `space_name` varchar(1024) DEFAULT NULL COMMENT '配置地点',
  `device_interval` int(1) DEFAULT NULL COMMENT '设备间隔',
  `param_info` varchar(1024) DEFAULT NULL COMMENT '参数信息',
  `exe_time` varchar(128) DEFAULT NULL COMMENT '执行时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者Id',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新者ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='配置信息表';

CREATE TABLE IF NOT EXISTS iot_db_control.`allocation_name` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT '功能名称',
  `description` varchar(256) DEFAULT NULL COMMENT '功能描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='配置名称表';

CREATE TABLE IF NOT EXISTS iot_db_control.`execute_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `function_id` bigint(20) DEFAULT NULL COMMENT '功能id',
  `exe_result` int(1) DEFAULT NULL COMMENT '执行结果状态：1：正常 2；异常',
  `exe_content` varchar(256) DEFAULT NULL COMMENT '执行结果内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='执行信息表';

