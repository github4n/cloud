CREATE TABLE IF NOT EXISTS `iot_db_device`.`ota_strategy_detail` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`plan_id`  bigint(20) NOT NULL COMMENT '升级计划id' ,
`strategy_group` int NULL DEFAULT 0 COMMENT '策略组',
`batch_num`  bigint(20) NULL COMMENT '设备批次号' ,
`device_uuid`  varchar(36) NULL COMMENT '设备uuid' ,
PRIMARY KEY (`id`),
INDEX `planId_index` (`plan_id`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='容灾策略明细表';
