CREATE TABLE IF NOT EXISTS `iot_db_device`.`product_config_netmode`  (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`product_id`  bigint(20) NOT NULL COMMENT '产品Id' ,
`name`  VARCHAR(255) NULL COMMENT '名称' ,
`create_by`  bigint(20) NULL COMMENT '创建人' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`update_by`  bigint(20) NULL COMMENT '更新人' ,
`update_time`  datetime NULL COMMENT '修改时间' ,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='产品协议';