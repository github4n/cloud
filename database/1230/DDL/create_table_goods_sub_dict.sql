CREATE TABLE IF NOT EXISTS iot_db_device.`goods_sub_dict` (
`id`  bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`code`  varchar(50) NOT NULL COMMENT '编码' ,
`name`  varchar(50) NOT NULL COMMENT '名称' ,
`goods_code`  varchar(50) NOT NULL COMMENT '商品编码' ,
`create_by`  bigint(20) NULL COMMENT '创建人' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
PRIMARY KEY (`id`),
INDEX `idx_code_smart` (`code`, `goods_code`) USING BTREE 
)
COMMENT='商品子项字典表'
;