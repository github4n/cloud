-- ----------------------------
-- Table structure for user_feedback
-- ----------------------------

USE iot_db_user;

CREATE TABLE IF NOT EXISTS online_debug(
    id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键id',
	uuid varchar(50) NOT NULL COMMENT '用户uuid',
	state tinyint(2) Not Null COMMENT '用户debug权限状态 0关闭状态 1开启状态',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	INDEX idx_online_debug_uuid (uuid) COMMENT '创建普通索引', 
	PRIMARY KEY (id)	
)