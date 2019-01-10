-- ----------------------------
-- Table structure for uuid_apply_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_device`.`uuid_apply_record` (
  `id` bigint(20) NOT NULL COMMENT '批次号,主键自动增长',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `order_id` varchar(50) NOT NULL COMMENT '订单id，对应order_record表主键',
  `down_num` int(10) NOT NULL DEFAULT '0' COMMENT '下载次数',
  `create_num` int(10) NOT NULL COMMENT '生成数量',
  `goods_id` bigint(20) NOT NULL COMMENT '商品id，对应goods_info主键（主要用于做方案条件搜索）',
  `uuid_apply_status` tinyint(2) NOT NULL COMMENT 'uuid申请状态（0:待处理；1:处理中;2:已完成;3:生成失败;4: P2PID不足）',
  `pay_status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '支付状态（1:待付款；2：已付款）',
  `file_id` varchar(32) DEFAULT NULL COMMENT 'UUID列表zip文件ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `uuid_validity_year` tinyint(4) NOT NULL DEFAULT '8' COMMENT '有效时长，单位：年(默认8年)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='UUID申请记录表';

-- ----------------------------
-- Table structure for permission
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_permission`.`permission` (
  `id` bigint(20) NOT NULL COMMENT '权限id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父资源id',
  `permission_code` varchar(36) NOT NULL COMMENT '权限code',
  `permission_url` varchar(100) NOT NULL COMMENT '资源对应url',
  `permission_name` varchar(200) NOT NULL COMMENT '资源名称',
  `permission_type` enum('menu','button','other') NOT NULL DEFAULT 'other' COMMENT '资源类型（menu; button;other(默认)）',
  `sort` smallint(6) DEFAULT NULL COMMENT '资源排序  低的排前面',
  `icon` varchar(50) DEFAULT NULL COMMENT '资源图标  权限类型为菜单或按钮时才有值',
  `service_id` bigint(20) DEFAULT NULL COMMENT '服务ID',
  `system_type` enum('Boss','2B','2C') DEFAULT '2B' COMMENT '系统类别',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限信息表';

-- ----------------------------
-- Table structure for role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_permission`.`role` (
  `id` bigint(20) NOT NULL COMMENT '角色id',
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `role_code` enum('Owner','Manager','ProductManager','APPDeveloper','EmbeddedDeveloper','Tester','Operator','DataQueryer') NOT NULL DEFAULT 'Manager' COMMENT '角色编码',
  `role_type` enum('Boss','2B','2C') NOT NULL DEFAULT '2B' COMMENT '角色类型:（Boss;2B(默认)；2C）',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `role_desc` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息表';

-- ----------------------------
-- Table structure for user_role_relate
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_permission`.`user_role_relate` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `user_id, role_id` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';

-- ----------------------------
-- Table structure for role_permission_relate
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_permission`.`role_permission_relate` (
  `id` bigint(20) NOT NULL COMMENT '角色权限关联id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `permission_id` bigint(20) NOT NULL COMMENT '权限id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性（invalid;valid(默认)）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `permission_id, role_id` (`permission_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关联表';




-- ----------------------------
-- Table structure for goods_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_payment`.`goods_info` (
  `id` bigint(20) NOT NULL COMMENT '商品id',
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

-- ----------------------------
-- Table structure for order_goods
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_payment`.`order_goods` (
  `id` bigint(20) NOT NULL COMMENT '订单商品id',
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

-- ----------------------------
-- Table structure for goods_extend_service
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_payment`.`goods_extend_service` (
  `id` bigint(20) NOT NULL COMMENT '商品附加服务id',
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

-- ----------------------------
-- Table structure for order_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_payment`.`order_record` (
  `id` varchar(50) NOT NULL COMMENT '订单id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `total_price` decimal(10,2) NOT NULL COMMENT '订单总价',
  `currency` varchar(10) NOT NULL COMMENT '货币单位',
  `order_type` int(10) NOT NULL COMMENT '订单类型，对应字典表sys_dict_item的type_id=2的记录',
  `order_status` tinyint(2) NOT NULL COMMENT '订单状态,对应字典表sys_dict_item的type_id=3的记录（0:已关闭；1:待付款；2:付款失败；3:付款成功；4：退款中；5：退款成功；6：退款失败）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单记录表';

-- ----------------------------
-- Table structure for order_goods_extend_service
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_payment`.`order_goods_extend_service` (
  `id` bigint(20) NOT NULL COMMENT '订单商品附加服务id',
  `order_id` varchar(50) NOT NULL COMMENT '订单id，order_record主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `order_goods_id` bigint(20) NOT NULL COMMENT '对应订单商品id',
  `goods_ex_id` bigint(20) NOT NULL COMMENT '原商品附加服务id，goods_extend_service主键',
  `goods_ex_name` varchar(50) NOT NULL COMMENT '商品附加服务名称',
  `goods_ex_price` decimal(10,2) NOT NULL COMMENT '价格',
  `goods_ex_currency` varchar(10) NOT NULL COMMENT '货币单位',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单商品-附加服务关系表';

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_user`.`sys_dict_item` (
  `type_id` int(10) NOT NULL COMMENT '大类型id',
  `item_id` int(10) NOT NULL COMMENT '小类型id',
  `item_name` varchar(50) NOT NULL COMMENT '小类型名称',
  `item_desc` varchar(500) DEFAULT NULL COMMENT '小类型描述',
  `item_sort` tinyint(4) DEFAULT '0' COMMENT '排序字段',
  UNIQUE KEY `type_id,item_id` (`type_id`,`item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表小类';

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_user`.`sys_dict_type` (
  `type_id` int(10) NOT NULL COMMENT '类型id',
  `type_name` varchar(50) NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `id` (`type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表大类';

-- ----------------------------
-- Table structure for lang_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_user`.`lang_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `lang_type` enum('zh_CN','en_US') NOT NULL DEFAULT 'en_US' COMMENT '语言类型',
  `lang_key` varchar(50) NOT NULL COMMENT '国际化键',
  `lang_value` varchar(200) DEFAULT NULL COMMENT '国际化值',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` enum('invalid','valid') DEFAULT NULL COMMENT '数据有效性',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=369 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for lang_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_tenant`.`lang_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) DEFAULT NULL COMMENT '应用主键',
  `key` varchar(50) DEFAULT NULL COMMENT 'key值',
  `lang` int(2) DEFAULT NULL COMMENT '语言标识',
  `content` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '内容',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=latin1 COMMENT='多语言管理';