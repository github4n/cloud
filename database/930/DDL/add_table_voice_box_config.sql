-- 2018.09.21 by yucx add voice_box_config
CREATE TABLE IF NOT EXISTS iot_db_tenant.`voice_box_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `type` varchar(20) NOT NULL COMMENT '音箱类型 alexa、googlehome',
  `skill_type` varchar(50) NOT NULL COMMENT '技能类型:smartHome、customSkill',
  `oauth_client_id` varchar(100) NOT NULL COMMENT '第三方 来 云端授权(获取token)使用',
  `oauth_client_secret` varchar(255) NOT NULL COMMENT '第三方 来 云端授权(获取token)使用',
  `company_name` varchar(100) DEFAULT NULL COMMENT 'oauth界面展示的 公司名称',
  `oauth_tip_content` varchar(255) DEFAULT NULL COMMENT 'oauth授权界面展示的提示内容',
  `report_client_id` varchar(255) DEFAULT NULL COMMENT '上报数据(请求第三方服务)用到',
  `report_client_secret` varchar(255) DEFAULT NULL COMMENT '上报数据(请求第三方服务)用到',
  `project_id` varchar(255) DEFAULT NULL COMMENT 'googlehome smartHome技能使用的配置',
  `private_key` text COMMENT 'googlehome smartHome技能使用的配置',
  `private_key_id` varchar(255) DEFAULT NULL COMMENT 'googlehome smartHome技能使用的配置',
  `client_email` varchar(255) DEFAULT NULL COMMENT 'googlehome smartHome技能使用的配置',
  `logo` varchar(255) DEFAULT NULL COMMENT '租户logo',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='音箱租户配置信息';