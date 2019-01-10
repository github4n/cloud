CREATE TABLE IF NOT EXISTS iot_db_build.`business_type_statistic` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`business_type_id`  bigint(20) NULL COMMENT '设备用途ID' ,
`online`  bigint(20) NULL COMMENT '在线数量',
`total`  bigint(20) NULL COMMENT '设置总数',
`create_time`  datetime NULL ,
`update_time`  datetime NULL ,
`tenant_id`  bigint NULL ,
`location_id`  bigint NULL ,
PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用途统计';
