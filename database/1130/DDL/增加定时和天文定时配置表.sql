CREATE TABLE IF NOT EXISTS iot_db_device.`product_to_timer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品ID',
  `timer_type` varchar(20) DEFAULT NULL COMMENT '定时方式',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='产品-配置定时方式';

