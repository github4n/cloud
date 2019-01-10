CREATE TABLE IF NOT EXISTS iot_db_device.`device_type_to_goods` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`device_type_id`  bigint(20) NOT NULL COMMENT '设备类型Id' ,
`goods_code`  varchar(50) NOT NULL COMMENT '增值服务id' ,
`create_by`  bigint(20) NULL COMMENT '创建人' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`update_by`  bigint(20) NULL COMMENT '更新人' ,
`update_time`  datetime NULL COMMENT '更新时间' ,
`tenant_id`  bigint(20) NULL COMMENT '租户id' ,
PRIMARY KEY (`id`),
INDEX `idx` (`device_type_id`) USING BTREE
)
COMMENT='设备类型增值服务关联表'
;