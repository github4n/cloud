CREATE TABLE IF NOT EXISTS `iot_db_device`.`ota_strategy_report` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`plan_id`  bigint(20) NOT NULL COMMENT '升级计划id' ,
`strategy_group` int NULL DEFAULT 0 COMMENT '策略组',
`device_uuid`  varchar(36) NOT NULL COMMENT '设备uuid' ,
`model`  varchar(255) NOT NULL COMMENT '产品型号' ,
`upgrade_type`  enum('Push','Force') NOT NULL DEFAULT 'Push' COMMENT '升级方式 推送升级Push   强制升级Force' ,
`target_version`  varchar(32) NOT NULL COMMENT '升级版本号' ,
`original_version`  varchar(32) NULL COMMENT '原版本号' ,
`upgrade_result`  enum('Success','Failed') NULL COMMENT '升级结果' ,
`reason` varchar(60) NULL COMMENT '原因' ,
`complete_time`  datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间' ,
PRIMARY KEY (`id`),
INDEX `planId_index` (`plan_id`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='容灾策略报告表';
