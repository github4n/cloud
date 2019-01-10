CREATE TABLE IF NOT EXISTS `iot_db_user`.`user_feedback_fileId` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键id' ,
`user_id`  bigint(20) NOT NULL COMMENT '用户id' ,
`tenant_id`  bigint(20) NOT NULL  COMMENT '用户的tenantId' ,
`file_id`  varchar(500)  NOT NULL  COMMENT '用户反馈上传的文件id' ,
`create_time`  datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
`update_time`  datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4
;