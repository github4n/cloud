CREATE TABLE IF NOT EXISTS iot_db_user.`user_log` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
`user_name`  varchar(255) NOT NULL COMMENT '用户注册名' ,
`uuid`  varchar(50) NOT NULL COMMENT '用户uuid' ,
`tenant_id`  bigint(20) NOT NULL COMMENT '租户id' ,
`accept`  tinyint NULL  COMMENT '接受隐私条款(1:同意，0:不同意）' ,
`accept_time`  datetime NULL COMMENT '接受隐私条款的时间' ,
`cancel`  tinyint NULL  COMMENT '注销账户(1:注销，0:未注销）' ,
`cancel_time`  datetime NULL  COMMENT '注销账户时间' ,
PRIMARY KEY (`id`)
)COMMENT='用户接受隐私条款和注销历史表'
;