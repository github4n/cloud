
USE `iot_db_permission`;

CREATE TABLE IF NOT EXISTS iot_db_permission.`user_data_permission_relate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `data_id` bigint(20) DEFAULT NULL,
  `data_type` int(1) DEFAULT NULL COMMENT '1: 项目  2：栋  3：层  4：房间',
  `data_name` varchar(128) DEFAULT NULL COMMENT '数据名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户数据权限关联表';