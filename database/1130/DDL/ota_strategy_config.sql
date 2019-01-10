CREATE TABLE IF NOT EXISTS `iot_db_device`.`ota_strategy_config` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户主键' ,
`plan_id`  bigint(20) NOT NULL COMMENT '升级计划id' ,
`strategy_group`  int NULL DEFAULT 0 COMMENT '策略组',
`upgrade_total`  int NULL DEFAULT 0 COMMENT '升级总量' ,
`threshold`  int NULL DEFAULT 0 COMMENT '容灾阀值' ,
`trigger_action`  tinyint(2) NULL DEFAULT 0 COMMENT '默认值：0，0：停止升级计划' ,
`create_by`  bigint(20) NULL COMMENT '创建人' ,
`create_time`  datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间' ,
`update_by`  bigint(20) NULL COMMENT '更新人' ,
`update_time`  datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`is_deleted`  enum('invalid','valid') NOT NULL DEFAULT 'valid'  COMMENT '数据有效性（invalid;valid(默认)）',
PRIMARY KEY (`id`),
INDEX `planId_index` (`plan_id`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='ota容灾策略配置表';