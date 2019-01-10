CREATE TABLE IF NOT EXISTS iot_db_control.`favorite` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户id' ,
`space_id`  bigint(20) NOT NULL COMMENT '主家id' ,
`dev_scene`  varchar(50) NOT NULL COMMENT '喜爱设备或场景' ,
`user_id`  bigint(20) NOT NULL COMMENT '用户id' ,
`type_id` tinyint(1) DEFAULT NULL COMMENT '类型：1表示设备，2表示场景，3表示autho' ,
`order_id` int(10) DEFAULT '1' COMMENT '排序',
`create_by`  bigint(20) NOT NULL COMMENT '创建者id' ,
`update_by`  bigint(20) NOT NULL COMMENT '更新者id' ,
`create_time`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间' ,
`updat_time`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='最喜爱设备表';
