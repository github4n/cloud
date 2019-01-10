-- --------------------------------------------------------
-- 主机:                           192.168.5.35
-- 服务器版本:                        5.7.24-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  7.0.0.4363
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 iot_db_tenant 的数据库结构
CREATE DATABASE IF NOT EXISTS `iot_db_tenant` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iot_db_tenant`;


-- 导出  表 iot_db_tenant.app_info 结构
CREATE TABLE IF NOT EXISTS `app_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT 'app名称',
  `en_name` varchar(100) DEFAULT NULL COMMENT '英文名称',
  `pack_name` varchar(100) DEFAULT NULL COMMENT '包名称',
  `app_code` varchar(100) DEFAULT NULL COMMENT '应用标识名',
  `theme` int(2) DEFAULT NULL COMMENT 'app皮肤 0：小清新 1：科技范 2：中性',
  `ver_flag` int(2) DEFAULT NULL COMMENT '版本 0：在线 1：本地',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `android_ver` varchar(50) DEFAULT NULL COMMENT '安卓版本号',
  `ios_ver` varchar(50) DEFAULT NULL COMMENT '苹果版本号',
  `file_id` varchar(100) DEFAULT NULL COMMENT '文件id',
  `logo` varchar(100) DEFAULT NULL COMMENT 'logo图片文件ID',
  `loading_img` varchar(100) DEFAULT NULL COMMENT '加载页图片文件ID',
  `has_security` int(2) DEFAULT NULL COMMENT '是否支持安防 0否 1是',
  `has_privacy_policy` int(2) DEFAULT NULL COMMENT '是否支持隐私政策 0否 1是',
  `en_copyright` varchar(100) DEFAULT NULL COMMENT '英文版权声明',
  `zn_copyright` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '中文版权声明',
  `android_install_url` varchar(255) DEFAULT NULL COMMENT '安卓安装路径',
  `ios_install_url` varchar(255) DEFAULT NULL COMMENT 'ios安装路径',
  `choose_lang` varchar(100) DEFAULT NULL COMMENT '多语言中 已选语言 1,2,3',
  `status` int(2) DEFAULT '0' COMMENT '应用状态 0未打包 1打包中 2打包成功 3打包失败',
  `mfile` varchar(100) DEFAULT NULL COMMENT '授权文件',
  `pfile` varchar(100) DEFAULT NULL COMMENT '证书文件',
  `file_password` varchar(100) DEFAULT NULL COMMENT '证书密码',
  `android_pack` int(2) DEFAULT '1' COMMENT '是否打包 0 不打包 1 打包',
  `ios_pack` int(2) DEFAULT '1' COMMENT '是否打包 0 不打包 1 打包',
  `copy_id` int(20) DEFAULT NULL COMMENT '复制应用主键',
  `android_pack_url` varchar(255) DEFAULT NULL COMMENT '安卓蒲公英下载路径',
  `ios_pack_url` varchar(255) DEFAULT NULL COMMENT 'ios蒲公英下载路径',
  `desc` varchar(200) DEFAULT NULL COMMENT '打包结果描述',
  `pack_time` datetime DEFAULT NULL COMMENT '打包开始时间',
  `fcm_file_id` varchar(100) DEFAULT NULL COMMENT 'google FCM文件id',
  `google_url` varchar(100) DEFAULT NULL COMMENT '谷歌推送url',
  `google_key` varchar(255) DEFAULT NULL COMMENT '谷歌推送秘钥',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `keystore_id` varchar(100) DEFAULT NULL COMMENT '安卓证书文件fileId',
  `audit_status` tinyint(2) DEFAULT NULL COMMENT '0:未审核 1:审核未通过 2:审核通过',
  `background_color` varchar(255) DEFAULT NULL COMMENT '背景色',
  `image_id` varchar(255) DEFAULT NULL COMMENT '图片',
  `apply_audit_time` datetime DEFAULT NULL COMMENT 'app申请审核时间',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `confirm_status` int(11) DEFAULT NULL COMMENT '确认状态',
  `display_identification` int(11) DEFAULT NULL COMMENT '显示标识',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `privacy_cn_link_id` varchar(255) DEFAULT NULL COMMENT '隐私链接中文',
  `privacy_en_link_id` varchar(255) DEFAULT NULL COMMENT '隐私链接英文',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='App应用';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.app_product 结构
CREATE TABLE IF NOT EXISTS `app_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` bigint(20) DEFAULT NULL COMMENT '关联产品主键',
  `app_id` bigint(20) DEFAULT NULL COMMENT '应用ID',
  `smart_img` varchar(50) DEFAULT NULL COMMENT '配网引导图id smartConfig',
  `ap_img` varchar(50) DEFAULT NULL COMMENT 'ap 配网图fileId',
  `choose_lang` varchar(100) DEFAULT NULL COMMENT '多语言中 已选语言 1,2,3',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='app关联产品';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.app_product_info 结构
CREATE TABLE IF NOT EXISTS `app_product_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_product_id` bigint(20) DEFAULT NULL COMMENT '配网分类主键',
  `type` int(2) DEFAULT NULL COMMENT '文案类型 0 配网文案 1 按钮文案 2 帮助文案',
  `lang` int(2) DEFAULT NULL,
  `content` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `mode` int(2) DEFAULT NULL COMMENT '模式 0 SmartConfig模式 1 AP模式',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='app关联产品配网信息';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.app_review_record 结构
CREATE TABLE IF NOT EXISTS `app_review_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `app_id` bigint(20) NOT NULL COMMENT 'appId',
  `operate_time` datetime NOT NULL COMMENT '操作时间（申请时间，审核时间）',
  `operate_desc` varchar(500) DEFAULT NULL COMMENT '操作描述（提交审核，审核通过，审核不通过原因）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  `process_status` tinyint(2) DEFAULT '0' COMMENT '0:未审核 1:审核未通过 2:审核通过',
  PRIMARY KEY (`id`),
  KEY `index_tenant_id` (`tenant_id`) USING BTREE,
  KEY `index_app_id` (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='APP审核记录';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.app_version 结构
CREATE TABLE IF NOT EXISTS `app_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `version` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '版本号',
  `app_id` bigint(20) NOT NULL COMMENT '应用主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `install_mode` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '安装参数',
  `remark` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `full_file_id` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '全量包文件ID',
  `incr_file_id` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '增量包文件ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `full_file_md5` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '全量包文件Md5',
  `incr_file_md5` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '全量包文件Md5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='应用版本记录';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.app_version_log 结构
CREATE TABLE IF NOT EXISTS `app_version_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `system_info` varchar(255) DEFAULT NULL,
  `app_package` varchar(255) DEFAULT NULL,
  `key` varchar(255) DEFAULT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `discribes` varchar(255) DEFAULT NULL,
  `down_location` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.device_network_step_base 结构
CREATE TABLE IF NOT EXISTS `device_network_step_base` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `device_type_id` bigint(20) NOT NULL COMMENT '设备类型id',
  `network_type_id` bigint(20) NOT NULL COMMENT '配网模式id(对应network_type表的id)',
  `is_help` enum('Y','N') NOT NULL DEFAULT 'N' COMMENT '是否是帮助文档(Y:是，N：否)',
  `step` tinyint(2) NOT NULL COMMENT '对应步骤',
  `icon` varchar(50) NOT NULL COMMENT '配网引导图',
  `data_status` enum('invalid','valid') NOT NULL COMMENT '数据有效性（invalid:失效；valid：有效）',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  PRIMARY KEY (`id`),
  KEY `device_type_id_index` (`device_type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备配网步骤模板表（BOSS维护）';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.device_network_step_tenant 结构
CREATE TABLE IF NOT EXISTS `device_network_step_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `app_id` bigint(20) NOT NULL COMMENT 'App应用',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `network_type_id` bigint(20) NOT NULL COMMENT '配网模式id(对应network_type表的id)',
  `is_help` enum('Y','N') NOT NULL DEFAULT 'N' COMMENT '是否是帮助文档(Y:是，N：否)',
  `step` tinyint(2) NOT NULL COMMENT '对应步骤',
  `icon` varchar(50) NOT NULL COMMENT '配网引导图',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  PRIMARY KEY (`id`),
  KEY `tenant_id_index` (`tenant_id`) USING BTREE,
  KEY `app_id_index` (`app_id`) USING BTREE,
  KEY `product_id_index` (`product_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户设备配网步骤详情表（portal维护）';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.lang_info 结构
CREATE TABLE IF NOT EXISTS `lang_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) DEFAULT NULL COMMENT '应用主键',
  `key` varchar(50) DEFAULT NULL COMMENT 'key值',
  `lang` int(2) DEFAULT NULL COMMENT '语言标识',
  `content` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '内容',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='多语言管理';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.lang_info_base 结构
CREATE TABLE IF NOT EXISTS `lang_info_base` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT '-1' COMMENT '租户主键 默认值-1',
  `object_type` varchar(20) NOT NULL COMMENT '对象类型 prodcutOTA、appConfig、deviceNetwork、deviceType',
  `object_id` bigint(20) NOT NULL COMMENT '对象ID 产品id或APPid、deviceTypeId，如果object_type是appConfig，object_id为-1',
  `belong_module` varchar(20) DEFAULT NULL COMMENT '所属模块',
  `lang_type` varchar(30) NOT NULL COMMENT '语言类型',
  `lang_key` varchar(100) NOT NULL COMMENT '国际化键',
  `lang_value` varchar(1000) NOT NULL COMMENT '国际化内容',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` enum('invalid','valid') NOT NULL DEFAULT 'valid' COMMENT '数据有效性（invalid:失效；valid：有效）',
  PRIMARY KEY (`id`),
  KEY `tenant_id_index` (`tenant_id`) USING BTREE,
  KEY `object_type_index` (`object_type`) USING BTREE,
  KEY `object_id_index` (`object_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文案基础信息表（Boss维护）';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.lang_info_tenant 结构
CREATE TABLE IF NOT EXISTS `lang_info_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `object_type` varchar(20) NOT NULL COMMENT '对象类型 prodcutOTA、appConfig、deviceNetwork、deviceType',
  `object_id` varchar(50) NOT NULL COMMENT '对象ID 产品id或APPid、deviceTypeId',
  `belong_module` varchar(20) DEFAULT NULL COMMENT '所属模块',
  `lang_type` varchar(30) NOT NULL COMMENT '语言类型',
  `lang_key` varchar(100) NOT NULL COMMENT '国际化键',
  `lang_value` varchar(1000) NOT NULL COMMENT '国际化内容',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` enum('invalid','valid') NOT NULL DEFAULT 'valid' COMMENT '数据有效性（invalid:失效；valid：有效）',
  PRIMARY KEY (`id`),
  KEY `tenant_id_index` (`tenant_id`) USING BTREE,
  KEY `object_type_index` (`object_type`) USING BTREE,
  KEY `object_id_index` (`object_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户文案信息表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.reply_message_record 结构
CREATE TABLE IF NOT EXISTS `reply_message_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上一条消息id',
  `object_type` varchar(20) NOT NULL COMMENT '对象类型（app、Google_voice、alx_voice、message等等）',
  `object_id` bigint(20) NOT NULL COMMENT '对象ID, 产品id或APPid、审核记录id',
  `message` text NOT NULL COMMENT '内容',
  `message_type` enum('feedback','reply') NOT NULL DEFAULT 'feedback' COMMENT '消息类型（feedback：反馈，reply：回复）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='回复消息记录表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.tenant 结构
CREATE TABLE IF NOT EXISTS `tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `business` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '主营业务',
  `code` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '唯一标示uuid',
  `cellphone` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '电话',
  `email` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `contacts` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '联系人',
  `job` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '职务',
  `country` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '国家',
  `province` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '省、州',
  `city` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '城市',
  `address` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '详细地址',
  `type` tinyint(4) DEFAULT '0' COMMENT '0:2c,1:2B',
  `lock_status` tinyint(2) DEFAULT '0' COMMENT '锁定状态(0:未锁定 1:锁定)',
  `tenant_status` tinyint(2) DEFAULT '0' COMMENT '0:未审核 1:审核未通过 2:审核通过',
  `audit_status` tinyint(2) DEFAULT '0' COMMENT '0:未审核 1:审核未通过 2:审核通过',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='租户表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.tenant_addres_manage 结构
CREATE TABLE IF NOT EXISTS `tenant_addres_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `country` varchar(100) DEFAULT NULL COMMENT '国家',
  `state` varchar(100) DEFAULT NULL COMMENT '省',
  `city` varchar(100) DEFAULT NULL COMMENT '市',
  `addres` varchar(500) DEFAULT NULL COMMENT '详细地址',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `contacter_name` varchar(100) DEFAULT NULL COMMENT '联系人姓名',
  `contacter_tel` varchar(20) DEFAULT NULL COMMENT '联系人电话',
  `zip_code` varchar(10) DEFAULT NULL COMMENT '邮编',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户地址管理表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.tenant_review_record 结构
CREATE TABLE IF NOT EXISTS `tenant_review_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户主键',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间（申请时间，审核时间）',
  `operate_desc` varchar(500) DEFAULT NULL COMMENT '操作描述（提交审核，审核通过，审核不通过原因）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  `process_status` tinyint(2) DEFAULT '0' COMMENT '0:未审核 1:审核未通过 2:审核通过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户审核记录';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.user_to_virtual_org 结构
CREATE TABLE IF NOT EXISTS `user_to_virtual_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `org_id` bigint(20) NOT NULL COMMENT '租户id',
  `default_used` tinyint(1) DEFAULT '0' COMMENT '默认组织0,受邀请的1 默认为0',
  `description` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户-组织表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.virtual_org 结构
CREATE TABLE IF NOT EXISTS `virtual_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `description` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='租户-组织表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_tenant.voice_box_config 结构
CREATE TABLE IF NOT EXISTS `voice_box_config` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
