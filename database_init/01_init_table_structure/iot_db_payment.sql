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

-- 导出 iot_db_payment 的数据库结构
CREATE DATABASE IF NOT EXISTS `iot_db_payment` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iot_db_payment`;


-- 导出  表 iot_db_payment.goods_extend_service 结构
CREATE TABLE IF NOT EXISTS `goods_extend_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) NOT NULL COMMENT '所属商品id',
  `goods_ex_name` varchar(50) NOT NULL COMMENT '商品附加服务名称',
  `goods_ex_desc` varchar(200) DEFAULT NULL COMMENT '商品附加服务描述',
  `price` decimal(10,2) NOT NULL COMMENT '附加服务价格',
  `currency` varchar(10) NOT NULL COMMENT '货币单位',
  `sort` int(4) DEFAULT NULL COMMENT '排序字段（值高的排前面）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `online_status` tinyint(1) DEFAULT '0' COMMENT '上线状态（0：下线；1、上线；）',
  `data_status` tinyint(1) DEFAULT '1' COMMENT '数据有效性（0：无效；1：有效；）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品附加服务信息表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_payment.goods_info 结构
CREATE TABLE IF NOT EXISTS `goods_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type_id` int(10) NOT NULL COMMENT '商品类型,对应字典表sys_dict_item的type_id=4的记录',
  `goods_code` varchar(50) NOT NULL COMMENT '商品编码（不可重复）',
  `goods_name` varchar(50) NOT NULL COMMENT '商品名称',
  `icon` varchar(100) DEFAULT NULL COMMENT '商品图片',
  `description` varchar(200) DEFAULT NULL COMMENT '商品描述',
  `standard` varchar(50) DEFAULT NULL COMMENT '商品规格（描述商品某种属性）',
  `standard_unit` int(10) DEFAULT NULL COMMENT '商品规格单位（比如时间期限：年/月等等，对应字典表sys_dict_item的type_id=1的记录）',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `currency` varchar(10) NOT NULL COMMENT '货币单位',
  `select_extend_service` tinyint(2) NOT NULL DEFAULT '0' COMMENT '选择附加服务(0：可不选；1：必选附加规格)',
  `sort` int(4) DEFAULT NULL COMMENT '排序字段（值高的排前面）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `online_status` tinyint(1) DEFAULT '0' COMMENT '上线状态（0：下线；1、上线；）',
  `data_status` tinyint(1) DEFAULT '1' COMMENT '数据有效性（0：无效；1：有效；）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品信息表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_payment.order_goods 结构
CREATE TABLE IF NOT EXISTS `order_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) NOT NULL COMMENT '订单id，order_record主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `num` int(10) NOT NULL COMMENT '购买数量',
  `goods_id` bigint(10) NOT NULL COMMENT '原商品id,关联goods_info表',
  `goods_name` varchar(50) NOT NULL COMMENT '商品名称',
  `goods_standard` varchar(50) DEFAULT NULL COMMENT '商品规格（描述商品某种属性）',
  `goods_standard_unit` int(10) DEFAULT NULL COMMENT '商品规格单位（比如时间期限：年/月等等，对应字典表sys_dict_item的type_id=1的记录）',
  `goods_price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `goods_currency` varchar(10) NOT NULL COMMENT '货币单位',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单商品关系表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_payment.order_goods_extend_service 结构
CREATE TABLE IF NOT EXISTS `order_goods_extend_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) NOT NULL COMMENT '订单id，order_record主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `order_goods_id` bigint(20) NOT NULL COMMENT '对应订单商品id',
  `goods_ex_id` bigint(20) NOT NULL COMMENT '原商品附加服务id，goods_extend_service主键',
  `goods_ex_name` varchar(50) NOT NULL COMMENT '商品附加服务名称',
  `goods_ex_price` decimal(10,2) NOT NULL COMMENT '价格',
  `goods_ex_currency` varchar(10) NOT NULL COMMENT '货币单位',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单商品-附加服务关系表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_payment.order_record 结构
CREATE TABLE IF NOT EXISTS `order_record` (
  `id` varchar(50) NOT NULL COMMENT '订单id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `total_price` decimal(10,2) NOT NULL COMMENT '订单总价',
  `currency` varchar(10) NOT NULL COMMENT '货币单位',
  `order_type` int(10) NOT NULL COMMENT '订单类型，对应字典表sys_dict_item的type_id=2的记录',
  `order_status` tinyint(2) NOT NULL COMMENT '订单状态,对应字典表sys_dict_item的type_id=3的记录（0:已关闭；1:待付款；2:付款成功；3:付款失败；4：退款中；5：退款成功；6：退款失败）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单记录表';

-- 数据导出被取消选择。


-- 导出  表 iot_db_payment.pay_transation 结构
CREATE TABLE IF NOT EXISTS `pay_transation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(36) COLLATE utf8_bin DEFAULT NULL COMMENT '用户id',
  `pay_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '支付id',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `pay_price` decimal(10,2) DEFAULT NULL COMMENT '支付价格',
  `order_id` varchar(36) COLLATE utf8_bin DEFAULT NULL COMMENT '订单id',
  `trade_id` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '交易id',
  `pay_type` int(2) DEFAULT NULL COMMENT '支付类别，0-支付，1-退款',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `pay_status` int(2) DEFAULT NULL COMMENT '支付状态,0-待支付，1-支付中，2-已支付，3-支付失败',
  `refund_status` int(2) DEFAULT NULL COMMENT '退款状态,0-未退款,1-退款中，2-退款成功,3-退款失败',
  `pay_fail_reason` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '支付失败原因',
  `refund_fail_reason` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '退款失败原因',
  `refund_sum` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
  `currency` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '货币代码',
  `pay_source` int(2) DEFAULT NULL COMMENT '支付来源,0-web,1-app',
  `payment_id` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '预支付id',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `refund_reason` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '退款原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
