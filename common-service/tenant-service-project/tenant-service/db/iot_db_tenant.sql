SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tenant
-- ----------------------------
DROP TABLE IF EXISTS `tenant`;
CREATE TABLE `tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(50) NOT NULL COMMENT '唯一标示uuid',
  `cellphone` varchar(50) DEFAULT NULL COMMENT '电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin  COMMENT='租户表';


-- ----------------------------
-- Table structure for virtual_org
-- ----------------------------
DROP TABLE IF EXISTS `virtual_org`;
CREATE TABLE `virtual_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `description` varchar(50) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin  COMMENT='租户-组织表';



-- ----------------------------
-- Table structure for user_to_virtual_org
-- ----------------------------
DROP TABLE IF EXISTS `user_to_virtual_org`;
CREATE TABLE `user_to_virtual_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `org_id` bigint(20) NOT NULL COMMENT '租户id',
  `default_used` tinyint(1) DEFAULT '0' COMMENT '默认组织0,受邀请的1 默认为0',
  `description` varchar(50) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin  COMMENT='用户-组织表';


