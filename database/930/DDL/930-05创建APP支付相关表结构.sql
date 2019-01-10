CREATE TABLE IF NOT EXISTS `iot_db_device`.`service_buy_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `order_id` varchar(50) NOT NULL COMMENT '订单id，对应order_record表主键',
  `goods_id` bigint(20) NOT NULL COMMENT '商品id，对应goods_info主键',
  `goods_num` int(10) NOT NULL COMMENT '购买数量',
  `pay_status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '支付状态（1:待付款；2：已付款）',
  `service_id` bigint(20) NOT NULL COMMENT '虚拟服务id（产品Id，appId）',
  `add_demand_desc` varchar(500) DEFAULT NULL COMMENT '附加需求描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `goods_type_id` int(10) NOT NULL COMMENT '商品类别id，字典表：type_id=4',
  PRIMARY KEY (`id`),
  KEY `index_service` (`tenant_id`,`goods_id`,`service_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='虚拟服务购买记录表';