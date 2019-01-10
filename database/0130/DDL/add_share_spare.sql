CREATE TABLE IF NOT EXISTS iot_db_control.`share_space` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `from_user_id` bigint(20) NOT NULL COMMENT '分享用户id',
  `from_user_uuid` varchar(32) NOT NULL COMMENT '分享用户uuid',
  `to_user_id` bigint(20) NOT NULL COMMENT '受邀分享用户id',
  `to_user_uuid` varchar(32) NOT NULL COMMENT '受邀分享用户uuid',
  `space_id` bigint(20) NOT NULL COMMENT '分享的房间id',
  `share_uuid` varchar(32)  NOT NULL COMMENT '分享uuid',
  `expire_time` bigint(20)  NOT NULL COMMENT '失效时间,单位秒',
  `status` int(2)  NOT NULL DEFAULT '0' COMMENT '状态 默认0 0邀请中 1邀请成功、2邀请失败',
  `remark` varchar(32)  NOT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `to_user_id` (`to_user_id`),
  KEY `tenant_id` (`tenant_id`),
  KEY `share_uuid` (`share_uuid`),
  KEY `from_user_id` (`from_user_id`),
  KEY `space_id` (`space_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='家分享表';




CREATE TABLE IF NOT EXISTS iot_db_control.`share_space_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `from_user_id` bigint(20) NOT NULL COMMENT '分享用户id',
  `from_user_uuid` varchar(32) NOT NULL COMMENT '分享用户uuid',
  `to_user_id` bigint(20) NOT NULL COMMENT '受邀分享用户id',
  `to_user_uuid` varchar(32) NOT NULL COMMENT '受邀分享用户uuid',
  `space_id` bigint(20) NOT NULL COMMENT '分享的房间id',
  `share_uuid` varchar(32)  NOT NULL COMMENT '分享uuid',
  `expire_time` bigint(20)  NOT NULL COMMENT '失效时间,单位秒',
  `status` int(2)  NOT NULL DEFAULT '0' COMMENT '状态 默认0 0邀请中 1邀请成功、2邀请失败、3分享人取消分享、4受邀人删除分享',
  `remark` varchar(32)  NOT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `to_user_id` (`to_user_id`),
  KEY `tenant_id` (`tenant_id`),
  KEY `share_uuid` (`share_uuid`),
  KEY `from_user_id` (`from_user_id`),
  KEY `space_id` (`space_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='家分享log表';