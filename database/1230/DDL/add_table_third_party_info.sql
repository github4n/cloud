CREATE DATABASE IF NOT EXISTS `iot_db_openapi` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS `iot_db_openapi`.`third_party_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL COMMENT '第三方类型(要唯一索引)',
  `company_name` varchar(100) NOT NULL COMMENT '第三方公司名称',
  `description` varchar(255) DEFAULT NULL COMMENT '第三方描述',
  `company_website` varchar(255) DEFAULT NULL COMMENT '第三方公司网址',
  `app_website` varchar(255) DEFAULT NULL COMMENT '第三方app网址',
  `logo` varchar(255) DEFAULT NULL COMMENT '第三方图标',
  `redirect_uri` varchar(255) NOT NULL COMMENT 'oauth的重定向地址',
  `webhook_url` varchar(255) DEFAULT NULL COMMENT '第三方事件通知地址',
  `client_id` varchar(255) NOT NULL COMMENT '应用的client_id',
  `client_secret` varchar(255) NOT NULL COMMENT '应用的client_secret',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方接入信息';

CREATE TABLE IF NOT EXISTS `iot_db_openapi`.`device_classify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) NOT NULL COMMENT '分类名称',
  `type_code` varchar(50) NOT NULL COMMENT '分类编码',
  `product_id` bigint(20) NOT NULL COMMENT '最小属性集的产品id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  KEY `idx_type_code` (`type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备分类';

CREATE TABLE IF NOT EXISTS `iot_db_openapi`.`device_classify_product_xref` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_classify_id` bigint(20) NOT NULL COMMENT '设备分类表id',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备分类与产品关联表';
