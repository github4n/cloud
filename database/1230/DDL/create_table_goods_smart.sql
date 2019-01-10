CREATE TABLE IF NOT EXISTS iot_db_device.`goods_smart` (
`id`  bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`goods_code`  varchar(50) NOT NULL COMMENT '商品编码' ,
`smart`  tinyint(2) NOT NULL COMMENT 'smart' ,
`create_by`  bigint(20) NULL COMMENT '创建人' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
PRIMARY KEY (`id`),
INDEX `idx_goods` (`goods_code`) USING BTREE 
)
COMMENT='商品smart对照表'
;