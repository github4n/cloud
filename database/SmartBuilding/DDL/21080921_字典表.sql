use iot_db_center_cloud;
DROP TABLE IF EXISTS `system_dict`;
CREATE TABLE `iot_db_center_cloud`.`system_dict` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户id' ,
`dict`  varchar(255) NULL COMMENT '字典' ,
`dict_name`  varchar(50) NULL COMMENT '字典名称' ,
`dict_code`  varchar(50) NULL COMMENT '字典code' ,
`create_by`  bigint(20) NULL DEFAULT NULL COMMENT '创建人ID' ,
`create_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间' ,
`update_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`update_by`  bigint(20) NULL DEFAULT NULL COMMENT '修改人' ,
`data_status`  tinyint(1) NULL DEFAULT NULL COMMENT '0-无效；1-有效' ,
PRIMARY KEY (`id`)
)
;

