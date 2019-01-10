CREATE TABLE IF NOT EXISTS `iot_db_device`.`gateway_subdev_relation`  (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户主键 与网关产品id租户一致' ,
`pardev_id`  bigint(20) NOT NULL COMMENT '网关产品id' ,
`subdev_id`  bigint(20) NOT NULL COMMENT '子设备产品id' ,
`create_by`  bigint(20) NULL COMMENT '创建人' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`update_by`  bigint(20) NULL COMMENT '更新人' ,
`update_time`  datetime NULL COMMENT '修改时间' ,
`is_deleted`  enum('valid','invalid') NOT NULL DEFAULT 'valid' COMMENT '数据有效性' ,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='网关子设备关联表';