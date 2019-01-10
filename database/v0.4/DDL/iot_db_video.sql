

ALTER TABLE `iot_db_video`.`video_package` MODIFY COLUMN `package_status`  int(2) NULL DEFAULT NULL COMMENT '套餐状态({0:无效,1:有效}，默认1)' AFTER `vip_discount`;



CREATE TABLE IF NOT EXISTS `iot_db_video`.`order_goods_mid` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单商品id' ,
`order_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单id，order_record主键' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户id' ,
`num`  int(10) NOT NULL COMMENT '购买数量' ,
`goods_id`  bigint(10) NOT NULL COMMENT '原商品id,关联goods_info表' ,
`goods_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称' ,
`goods_standard`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品规格（描述商品某种属性）' ,
`goods_standard_unit`  int(10) NULL DEFAULT NULL COMMENT '商品规格单位（比如时间期限：年/月等等，对应字典表sys_dict_item的type_id=1的记录）' ,
`goods_price`  decimal(10,2) NOT NULL COMMENT '商品价格' ,
`goods_currency`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '货币单位' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Dynamic
;


DROP TABLE IF  EXISTS `iot_db_video`.`video_event_back`;

DROP TABLE IF  EXISTS `iot_db_video`.`video_file_back`;