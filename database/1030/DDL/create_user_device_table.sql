CREATE TABLE IF NOT EXISTS iot_db_control.`user_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `device_id` varchar(32) NOT NULL COMMENT '设备id',
  `org_id` bigint(20) DEFAULT NULL COMMENT '用户所属组织id',
  `user_type` varchar(10) DEFAULT NULL COMMENT '用户类型（root：主账号sub: 子账号）',
  `password` varchar(50) DEFAULT NULL COMMENT '用户访问设备秘钥',
  `event_notify_enabled` tinyint(1) DEFAULT '0' COMMENT '事件通知使能（0：开启，1：关闭）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) COMMENT='用户-设备关系表';