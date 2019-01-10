USE iot_db_device;

CREATE TABLE IF NOT EXISTS `iot_db_device`.`product_service_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `service_id` bigint(20) NOT NULL COMMENT '对应商品信息goods_info表的goods_id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `audit_status` tinyint(2) DEFAULT NULL COMMENT '审核状态（0:未审核 1:审核未通过 2:审核通过）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品关联第三方服务信息表';
