-- ----------------------------
-- Table structure for user_feedback
-- ----------------------------
USE iot_db_user;

CREATE TABLE IF NOT EXISTS user_feedback(
    id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键id',
	user_id bigint(20) NOT NULL COMMENT '用户主键',
	feedback_content varchar(500) NOT NULL COMMENT '用户反馈内容',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)	
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户反馈表';