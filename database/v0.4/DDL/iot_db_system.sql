-- ----------------------------
-- Table structure for sys_db_version
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_system`.`sys_db_version` (
  `version` varchar(13) NOT NULL COMMENT '版本号',
  `version_desc` varchar(1000) NOT NULL COMMENT '变更描述',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `pk_version` (`version`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库升级版本信息';

-- ----------------------------
-- Records of sys_db_version
-- ----------------------------
replace INTO `iot_db_system`.`sys_db_version` VALUES ('2018.06.27.01', 'sss', '2018-06-28 11:10:27');
-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_system`.`sys_dict_item` (
  `type_id` int(10) NOT NULL COMMENT '大类型id',
  `item_id` int(10) NOT NULL COMMENT '小类型id',
  `item_name` varchar(50) NOT NULL COMMENT '小类型名称',
  `item_desc` varchar(500) DEFAULT NULL COMMENT '小类型描述',
  `item_sort` tinyint(4) DEFAULT '0' COMMENT '排序字段',
  UNIQUE KEY `type_id,item_id` (`type_id`,`item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表小类';

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('1', '1', 'dict_item:ci', '按次收费', '1');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('1', '2', 'dict_item:nian', '按年收费', '2');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('1', '3', 'dict_item:ge', '按个收费', '3');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('1', '4', 'dict_item:Event', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('1', '5', 'dict_item:tian', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('2', '1', 'dict_item:luyingjihua', '', '1');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('2', '2', 'dict_item:UUIDfangan', '', '2');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('2', '3', 'dict_item:APPdabao', '', '3');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('3', '0', 'dict_item:yiguanbi', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('3', '1', 'dict_item:daifukuan', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('3', '2', 'dict_item:fukuanshibai', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('3', '3', 'dict_item:fukuanchenggong', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('3', '4', 'dict_item:tuikuanzhong', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('3', '5', 'dict_item:tuikuanchenggong', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('3', '6', 'dict_item:tuikuanshibai', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('4', '0', 'dict_item:quanshiluying', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('4', '1', 'dict_item:shijianluying', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('4', '2', 'dict_item:uuidfangan', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('4', '3', 'dict_item:APPdabao', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('5', '0', 'dict_item:daichuli', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('5', '1', 'dict_item:chulizhong', null, '1');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('5', '2', 'dict_item:yiwancheng', null, '2');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('5', '3', 'dict_item:shengchengshibai', null, '3');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('5', '4', 'dict_item:P2PIDbuzu', null, '4');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('6', '0', 'dict_item:weijihuo', null, '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('6', '1', 'dict_item:yijihuo', null, '1');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('7', '1', 'dict_item:daifukuan', null, '1');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('7', '2', 'dict_item:yifukuan', null, '2');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('8', '1', 'dict_item:zhengbaoshengji', '', '0');
replace INTO `iot_db_system`.`sys_dict_item` VALUES ('8', '2', 'dict_item:chafenshengji', '', '2');
-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_system`.`sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `type_id` int(10) NOT NULL COMMENT '类型id',
  `type_name` varchar(50) NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `id` (`type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表大类';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
replace INTO `iot_db_system`.`sys_dict_type` VALUES ('1', '商品规格单位');
replace INTO `iot_db_system`.`sys_dict_type` VALUES ('2', '订单类型');
replace INTO `iot_db_system`.`sys_dict_type` VALUES ('3', '订单状态');
replace INTO `iot_db_system`.`sys_dict_type` VALUES ('4', '商品类型');
replace INTO `iot_db_system`.`sys_dict_type` VALUES ('5', 'UUID订单状态');
replace INTO `iot_db_system`.`sys_dict_type` VALUES ('6', 'UUID激活状态');
replace INTO `iot_db_system`.`sys_dict_type` VALUES ('7', 'UUID支付状态');
replace INTO `iot_db_system`.`sys_dict_type` VALUES ('8', '升级包类型');
-- ----------------------------
-- Table structure for lang_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_db_system`.`lang_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `lang_type` enum('zh_CN','en_US') NOT NULL DEFAULT 'en_US' COMMENT '语言类型',
  `lang_key` varchar(50) NOT NULL COMMENT '国际化键',
  `lang_value` varchar(200) DEFAULT NULL COMMENT '国际化值',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` enum('invalid','valid') DEFAULT 'valid' COMMENT '数据有效性',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=415 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lang_info
-- ----------------------------
replace INTO `iot_db_system`.`lang_info` VALUES ('175', 'zh_CN', 'auth:jichuyunying', '基础运营', null, '2018-07-12 03:06:21', null, '2018-07-12 03:06:21', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('176', 'en_US', 'auth:jichuyunying', '基础运营-en', null, '2018-07-12 03:06:21', null, '2018-07-12 03:06:21', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('177', 'zh_CN', 'auth:shujubaobiao', '数据报表', null, '2018-07-12 03:06:21', null, '2018-07-12 03:06:21', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('178', 'en_US', 'auth:shujubaobiao', '数据报表-en', null, '2018-07-12 03:06:21', null, '2018-07-12 03:06:21', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('179', 'zh_CN', 'auth:chanpinshixian', '产品实现', null, '2018-07-12 03:06:21', null, '2018-07-12 03:06:21', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('180', 'en_US', 'auth:chanpinshixian', '产品实现-en', null, '2018-07-12 03:06:21', null, '2018-07-12 03:06:21', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('181', 'zh_CN', 'auth:jihuoshuju', '激活数据', null, '2018-07-12 03:06:21', null, '2018-07-12 03:06:21', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('182', 'en_US', 'auth:jihuoshuju', '激活数据-en', null, '2018-07-12 03:06:21', null, '2018-07-12 03:06:21', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('183', 'zh_CN', 'auth:huoyueshuju', '活跃数据', null, '2018-07-12 03:06:21', null, '2018-07-12 03:06:21', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('184', 'en_US', 'auth:huoyueshuju', '活跃数据-en', null, '2018-07-12 03:06:21', null, '2018-07-12 03:06:21', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('185', 'zh_CN', 'auth:xingweifenxi', '行为分析', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('186', 'en_US', 'auth:xingweifenxi', '行为分析-en', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('187', 'zh_CN', 'auth:yonghuhuaxiang', '用户画像', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('188', 'en_US', 'auth:yonghuhuaxiang', '用户画像-en', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('189', 'zh_CN', 'auth:diqufenbu', '地区分布', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('190', 'en_US', 'auth:diqufenbu', '地区分布-en', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('191', 'zh_CN', 'auth:UUID', 'UUID', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('192', 'en_US', 'auth:UUID', 'UUID-en', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('193', 'zh_CN', 'auth:UUIDxiangqing', 'UUID详情', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('194', 'en_US', 'auth:UUIDxiangqing', 'UUID详情-en', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('195', 'zh_CN', 'auth:UUIDshenqing', 'UUID申请', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('196', 'en_US', 'auth:UUIDshenqing', 'UUID申请-en', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('197', 'zh_CN', 'auth:UUIDchaxun', 'UUID查询', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('198', 'en_US', 'auth:UUIDchaxun', 'UUID查询-en', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('199', 'zh_CN', 'auth:OTAguanli', 'OTA管理', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('200', 'en_US', 'auth:OTAguanli', 'OTA管理-en', null, '2018-07-12 03:06:22', null, '2018-07-12 03:06:22', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('201', 'zh_CN', 'auth:banbenguanli', '版本管理', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('202', 'en_US', 'auth:banbenguanli', '版本管理-en', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('203', 'zh_CN', 'auth:lishishengjibaogao', '历史升级报告', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('204', 'en_US', 'auth:lishishengjibaogao', '历史升级报告-en', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('205', 'zh_CN', 'auth:chuangjianchanpin', '创建产品', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('206', 'en_US', 'auth:chuangjianchanpin', '创建产品-en', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('207', 'zh_CN', 'auth:chuangjianyingyong', '创建应用', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('208', 'en_US', 'auth:chuangjianyingyong', '创建应用-en', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('209', 'zh_CN', 'auth:chuangjianchanpin-chanpinxinxi', '创建产品-产品信息', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('210', 'en_US', 'auth:chuangjianchanpin-chanpinxinxi', '创建产品-产品信息-en', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('211', 'zh_CN', 'auth:chanpingongnengdingyi', '产品功能定义', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('212', 'en_US', 'auth:chanpingongnengdingyi', '产品功能定义-en', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('213', 'zh_CN', 'auth:chanpingongnengdingyi-biaozhungongneng', '产品功能定义-标准功能', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('214', 'en_US', 'auth:chanpingongnengdingyi-biaozhungongneng', '产品功能定义-标准功能-en', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('215', 'zh_CN', 'auth:zizhugongneng', '自主功能', null, '2018-07-12 03:06:23', null, '2018-07-12 03:06:23', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('216', 'en_US', 'auth:zizhugongneng', '自主功能-en', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('217', 'zh_CN', 'auth:gongnengshuxi，fangfa，shijiantianjia', '功能熟悉，方法，事件添加', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('218', 'en_US', 'auth:gongnengshuxi，fangfa，shijiantianjia', '功能熟悉，方法，事件添加-en', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('219', 'zh_CN', 'auth:gongnengshuxingbianji', '功能属性编辑', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('220', 'en_US', 'auth:gongnengshuxingbianji', '功能属性编辑-en', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('221', 'zh_CN', 'auth:gongnengfanganbianji', '功能方案编辑', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('222', 'en_US', 'auth:gongnengfanganbianji', '功能方案编辑-en', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('223', 'zh_CN', 'auth:gongnengshijianbianji', '功能事件编辑', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('224', 'en_US', 'auth:gongnengshijianbianji', '功能事件编辑-en', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('225', 'zh_CN', 'auth:chanpingongnengdingyi-APPjiemian', '产品功能定义-APP界面', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('226', 'en_US', 'auth:chanpingongnengdingyi-APPjiemian', '产品功能定义-APP界面-en', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('227', 'zh_CN', 'auth:xuanzhongmoban-saomatiyan', '选中模板-扫码体验', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('228', 'en_US', 'auth:xuanzhongmoban-saomatiyan', '选中模板-扫码体验-en', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('229', 'zh_CN', 'auth:chanpindingyi-yingjiandiaoshi', '产品定义-硬件调试', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('230', 'en_US', 'auth:chanpindingyi-yingjiandiaoshi', '产品定义-硬件调试-en', null, '2018-07-12 03:06:24', null, '2018-07-12 03:06:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('231', 'zh_CN', 'auth:yingjiandiaoshi-genghuanmokuai', '硬件调试-更换模块', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('232', 'en_US', 'auth:yingjiandiaoshi-genghuanmokuai', '硬件调试-更换模块-en', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('233', 'zh_CN', 'auth:yingjiandiaoshi-mokuaixiangqing', '硬件调试-模块详情', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('234', 'en_US', 'auth:yingjiandiaoshi-mokuaixiangqing', '硬件调试-模块详情-en', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('235', 'zh_CN', 'auth:chanpindingyi-tuozhangongneng', '产品定义-拓展功能', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('236', 'en_US', 'auth:chanpindingyi-tuozhangongneng', '产品定义-拓展功能-en', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('237', 'zh_CN', 'auth:liandongpeizhi', '联动配置', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('238', 'en_US', 'auth:liandongpeizhi', '联动配置-en', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('239', 'zh_CN', 'auth:tuozhangongneng-duoyuyanguanli', '拓展功能-多语言管理', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('240', 'en_US', 'auth:tuozhangongneng-duoyuyanguanli', '拓展功能-多语言管理-en', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('241', 'zh_CN', 'auth:Alexajieru', 'Alexa接入', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('242', 'en_US', 'auth:Alexajieru', 'Alexa接入-en', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('243', 'zh_CN', 'auth:chanpindingyi-piliangtouchan', '产品定义-批量投产', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('244', 'en_US', 'auth:chanpindingyi-piliangtouchan', '产品定义-批量投产-en', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('245', 'zh_CN', 'auth:bianjichanpinxinxi', '编辑产品信息', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('246', 'en_US', 'auth:bianjichanpinxinxi', '编辑产品信息-en', null, '2018-07-12 03:06:25', null, '2018-07-12 03:06:25', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('247', 'zh_CN', 'auth:gexinghuayugongneng', '个性化与功能', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('248', 'en_US', 'auth:gexinghuayugongneng', '个性化与功能-en', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('249', 'zh_CN', 'auth:fenleichakan', '分类查看', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('250', 'en_US', 'auth:fenleichakan', '分类查看-en', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('251', 'zh_CN', 'auth:peiwangyindao', '配网引导', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('252', 'en_US', 'auth:peiwangyindao', '配网引导-en', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('253', 'zh_CN', 'auth:duoyuyanguanli', '多语言管理', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('254', 'en_US', 'auth:duoyuyanguanli', '多语言管理-en', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('255', 'zh_CN', 'auth:yingyongzhengshu', '应用证书', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('256', 'en_US', 'auth:yingyongzhengshu', '应用证书-en', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('257', 'zh_CN', 'auth:tuozhanpeizhi', '拓展配置', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('258', 'en_US', 'auth:tuozhanpeizhi', '拓展配置-en', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('259', 'zh_CN', 'auth:yingyonggoujianyuxiazai', '应用构建与下载', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('260', 'en_US', 'auth:yingyonggoujianyuxiazai', '应用构建与下载-en', null, '2018-07-12 03:06:26', null, '2018-07-12 03:06:26', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('261', 'zh_CN', 'goods:zhilianfangan', '直连方案', null, '2018-07-12 03:08:05', null, '2018-07-12 03:08:05', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('262', 'en_US', 'goods:zhilianfangan', '直连方案-en', null, '2018-07-12 03:08:05', null, '2018-07-12 03:08:05', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('263', 'zh_CN', 'goods:lanyafangan', '蓝牙方案', null, '2018-07-12 03:08:05', null, '2018-07-12 03:08:05', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('264', 'en_US', 'goods:lanyafangan', '蓝牙方案-en', null, '2018-07-12 03:08:05', null, '2018-07-12 03:08:05', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('265', 'zh_CN', 'goods:wangguanfangan', '网关方案', null, '2018-07-12 03:08:05', null, '2018-07-12 03:08:05', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('266', 'en_US', 'goods:wangguanfangan', '网关方案-en', null, '2018-07-12 03:08:05', null, '2018-07-12 03:08:05', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('267', 'zh_CN', 'goods:IPCfangan', 'IPC方案', null, '2018-07-12 03:08:05', null, '2018-07-12 03:08:05', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('268', 'en_US', 'goods:IPCfangan', 'IPC方案-en', null, '2018-07-12 03:08:05', null, '2018-07-12 03:08:05', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('269', 'zh_CN', 'goods:APPdabao', 'APP打包', null, '2018-07-12 03:08:05', null, '2018-07-12 03:08:05', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('270', 'en_US', 'goods:APPdabao', 'APP打包-en', null, '2018-07-12 03:08:05', null, '2018-07-12 03:08:05', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('271', 'zh_CN', 'goods:quanshiluying', '全时录影', null, '2018-07-12 03:08:06', null, '2018-07-12 03:08:06', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('272', 'en_US', 'goods:quanshiluying', '全时录影-en', null, '2018-07-12 03:08:06', null, '2018-07-12 03:08:06', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('273', 'zh_CN', 'goods:300shijianluying', '300事件录影', null, '2018-07-12 03:08:06', null, '2018-07-12 03:08:06', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('274', 'en_US', 'goods:300shijianluying', '300事件录影-en', null, '2018-07-12 03:08:06', null, '2018-07-12 03:08:06', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('275', 'zh_CN', 'goods:600shijianluying', '600事件录影', null, '2018-07-12 03:08:06', null, '2018-07-12 03:08:06', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('276', 'en_US', 'goods:600shijianluying', '600事件录影-en', null, '2018-07-12 03:08:06', null, '2018-07-12 03:08:06', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('293', 'zh_CN', 'goods_extend:anfang', '安防', null, '2018-07-12 03:14:27', null, '2018-07-12 03:14:27', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('294', 'en_US', 'goods_extend:anfang', '安防-en', null, '2018-07-12 03:14:27', null, '2018-07-12 03:14:27', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('295', 'zh_CN', 'goods_extend:zhaoming', '照明', null, '2018-07-12 03:14:27', null, '2018-07-12 03:14:27', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('296', 'en_US', 'goods_extend:zhaoming', '照明-en', null, '2018-07-12 03:14:27', null, '2018-07-12 03:14:27', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('297', 'zh_CN', 'goods_extend:kongzhi', '控制', null, '2018-07-12 03:14:27', null, '2018-07-12 03:14:27', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('298', 'en_US', 'goods_extend:kongzhi', '控制-en', null, '2018-07-12 03:14:27', null, '2018-07-12 03:14:27', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('299', 'zh_CN', 'dict_item:ci', '次', null, '2018-07-12 03:17:43', null, '2018-07-12 03:17:43', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('300', 'en_US', 'dict_item:ci', '次-en', null, '2018-07-12 03:17:43', null, '2018-07-12 03:17:43', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('301', 'zh_CN', 'dict_item:nian', '年', null, '2018-07-12 03:17:43', null, '2018-07-12 03:17:43', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('302', 'en_US', 'dict_item:nian', '年-en', null, '2018-07-12 03:17:43', null, '2018-07-12 03:17:43', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('303', 'zh_CN', 'dict_item:ge', '个', null, '2018-07-12 03:17:43', null, '2018-07-12 03:17:43', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('304', 'en_US', 'dict_item:ge', '个-en', null, '2018-07-12 03:17:43', null, '2018-07-12 03:17:43', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('305', 'zh_CN', 'dict_item:Event', 'Event', null, '2018-07-12 03:17:43', null, '2018-07-12 03:17:43', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('306', 'en_US', 'dict_item:Event', 'Event-en', null, '2018-07-12 03:17:43', null, '2018-07-12 03:17:43', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('307', 'zh_CN', 'dict_item:tian', '天', null, '2018-07-12 03:17:43', null, '2018-07-12 03:17:43', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('308', 'en_US', 'dict_item:tian', '天-en', null, '2018-07-12 03:17:43', null, '2018-07-12 03:17:43', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('309', 'zh_CN', 'dict_item:luyingjihua', '录影计划', null, '2018-07-12 03:17:43', null, '2018-07-12 03:17:43', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('310', 'en_US', 'dict_item:luyingjihua', '录影计划-en', null, '2018-07-12 03:17:43', null, '2018-07-12 03:17:43', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('311', 'zh_CN', 'dict_item:UUIDfangan', 'UUID方案', null, '2018-07-12 03:17:44', null, '2018-07-12 03:17:44', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('312', 'en_US', 'dict_item:UUIDfangan', 'UUID方案-en', null, '2018-07-12 03:17:44', null, '2018-07-12 03:17:44', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('313', 'zh_CN', 'dict_item:APPdabao', 'APP打包', null, '2018-07-12 03:17:44', null, '2018-07-12 03:17:44', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('314', 'en_US', 'dict_item:APPdabao', 'APP打包-en', null, '2018-07-12 03:17:44', null, '2018-07-12 03:17:44', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('315', 'zh_CN', 'dict_item:yiguanbi', '已关闭', null, '2018-07-12 03:17:44', null, '2018-07-12 03:17:44', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('316', 'en_US', 'dict_item:yiguanbi', '已关闭-en', null, '2018-07-12 03:17:44', null, '2018-07-12 03:17:44', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('317', 'zh_CN', 'dict_item:daifukuan', '待付款', null, '2018-07-12 03:17:44', null, '2018-07-12 03:17:44', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('318', 'en_US', 'dict_item:daifukuan', '待付款-en', null, '2018-07-12 03:17:44', null, '2018-07-12 03:17:44', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('319', 'zh_CN', 'dict_item:fukuanshibai', '付款失败', null, '2018-07-12 03:17:44', null, '2018-07-12 03:17:44', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('320', 'en_US', 'dict_item:fukuanshibai', '付款失败-en', null, '2018-07-12 03:17:44', null, '2018-07-12 03:17:44', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('321', 'zh_CN', 'dict_item:fukuanchenggong', '付款成功', null, '2018-07-12 03:17:44', null, '2018-07-12 03:17:44', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('322', 'en_US', 'dict_item:fukuanchenggong', '付款成功-en', null, '2018-07-12 03:17:45', null, '2018-07-12 03:17:45', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('323', 'zh_CN', 'dict_item:tuikuanzhong', '退款中', null, '2018-07-12 03:17:45', null, '2018-07-12 03:17:45', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('324', 'en_US', 'dict_item:tuikuanzhong', '退款中-en', null, '2018-07-12 03:17:45', null, '2018-07-12 03:17:45', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('325', 'zh_CN', 'dict_item:tuikuanchenggong', '退款成功', null, '2018-07-12 03:17:45', null, '2018-07-12 03:17:45', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('326', 'en_US', 'dict_item:tuikuanchenggong', '退款成功-en', null, '2018-07-12 03:17:45', null, '2018-07-12 03:17:45', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('327', 'zh_CN', 'dict_item:tuikuanshibai', '退款失败', null, '2018-07-12 03:17:45', null, '2018-07-12 03:17:45', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('328', 'en_US', 'dict_item:tuikuanshibai', '退款失败-en', null, '2018-07-12 03:17:45', null, '2018-07-12 03:17:45', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('329', 'zh_CN', 'dict_item:quanshiluying', '全时录影', null, '2018-07-12 03:17:46', null, '2018-07-12 03:17:46', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('330', 'en_US', 'dict_item:quanshiluying', '全时录影-en', null, '2018-07-12 03:17:46', null, '2018-07-12 03:17:46', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('331', 'zh_CN', 'dict_item:shijianluying', '事件录影', null, '2018-07-12 03:17:46', null, '2018-07-12 03:17:46', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('332', 'en_US', 'dict_item:shijianluying', '事件录影-en', null, '2018-07-12 03:17:46', null, '2018-07-12 03:17:46', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('333', 'zh_CN', 'dict_item:uuidfangan', 'uuid方案', null, '2018-07-12 03:17:46', null, '2018-07-12 03:17:46', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('334', 'en_US', 'dict_item:uuidfangan', 'uuid方案-en', null, '2018-07-12 03:17:46', null, '2018-07-12 03:17:46', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('335', 'zh_CN', 'dict_item:APPdabao', 'APP打包', null, '2018-07-12 03:17:46', null, '2018-07-12 03:17:46', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('336', 'en_US', 'dict_item:APPdabao', 'APP打包-en', null, '2018-07-12 03:17:47', null, '2018-07-12 03:17:47', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('337', 'zh_CN', 'dict_item:daichuli', '待处理', null, '2018-07-12 03:17:47', null, '2018-07-12 03:17:47', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('338', 'en_US', 'dict_item:daichuli', '待处理-en', null, '2018-07-12 03:17:47', null, '2018-07-12 03:17:47', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('339', 'zh_CN', 'dict_item:chulizhong', '处理中', null, '2018-07-12 03:17:47', null, '2018-07-12 03:17:47', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('340', 'en_US', 'dict_item:chulizhong', '处理中-en', null, '2018-07-12 03:17:47', null, '2018-07-12 03:17:47', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('341', 'zh_CN', 'dict_item:yiwancheng', '已完成', null, '2018-07-12 03:17:47', null, '2018-07-12 03:17:47', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('342', 'en_US', 'dict_item:yiwancheng', '已完成-en', null, '2018-07-12 03:17:47', null, '2018-07-12 03:17:47', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('343', 'zh_CN', 'dict_item:shengchengshibai', '生成失败', null, '2018-07-12 03:17:47', null, '2018-07-12 03:17:47', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('344', 'en_US', 'dict_item:shengchengshibai', '生成失败-en', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('345', 'zh_CN', 'dict_item:P2PIDbuzu', 'P2PID不足', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('346', 'en_US', 'dict_item:P2PIDbuzu', 'P2PID不足-en', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('347', 'zh_CN', 'dict_item:weijihuo', '未激活', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('348', 'en_US', 'dict_item:weijihuo', '未激活-en', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('349', 'zh_CN', 'dict_item:yijihuo', '已激活', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('350', 'en_US', 'dict_item:yijihuo', '已激活-en', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('351', 'zh_CN', 'dict_item:daifukuan', '待付款', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('352', 'en_US', 'dict_item:daifukuan', '待付款-en', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('353', 'zh_CN', 'dict_item:yifukuan', '已付款', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('354', 'en_US', 'dict_item:yifukuan', '已付款-en', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('355', 'en_US', 'goods:1000shijianluying', '1000事件录影-en', null, '2018-07-17 12:38:10', null, '2018-07-17 12:38:10', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('356', 'zh_CN', 'goods:1000shijianluying', '1000事件录影', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('357', 'en_US', 'role:name:2B-Owner', 'Owner', null, '2018-07-17 12:38:10', null, '2018-07-17 12:38:10', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('358', 'zh_CN', 'role:name:2B-Owner', '拥有者', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('359', 'en_US', 'role:desc:2B-Owner', 'Owner', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('360', 'zh_CN', 'role:desc:2B-Owner', '拥有者', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('361', 'en_US', 'role:name:2B-Manager', 'Manager', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('362', 'zh_CN', 'role:name:2B-Manager', '管理者', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('363', 'en_US', 'role:desc:2B-Manager', 'Manager', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('364', 'zh_CN', 'role:desc:2B-Manager', '管理者', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('365', 'en_US', 'role:name:2B-ProductManager', 'ProductManager', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('366', 'zh_CN', 'role:name:2B-ProductManager', '产品经理', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('367', 'en_US', 'role:desc:2B-ProductManager', 'ProductManager', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('368', 'zh_CN', 'role:desc:2B-ProductManager', '产品经理', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('369', 'en_US', 'role:name:2B-APPDeveloper', 'APPDeveloper', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('370', 'zh_CN', 'role:name:2B-APPDeveloper', 'APP开发人员', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('371', 'en_US', 'role:desc:2B-APPDeveloper', 'APPDeveloper', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('372', 'zh_CN', 'role:desc:2B-APPDeveloper', 'APP开发人员', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('373', 'en_US', 'role:name:2B-EmbeddedDeveloper', 'EmbeddedDeveloper', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('374', 'zh_CN', 'role:name:2B-EmbeddedDeveloper', '嵌入式开发人员', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('375', 'en_US', 'role:desc:2B-EmbeddedDeveloper', 'EmbeddedDeveloper', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('376', 'zh_CN', 'role:desc:2B-EmbeddedDeveloper', '嵌入式开发人员', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('377', 'en_US', 'role:name:2B-Tester', 'Tester', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('378', 'zh_CN', 'role:name:2B-Tester', '测试人员', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('379', 'en_US', 'role:desc:2B-Tester', 'Tester', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('380', 'zh_CN', 'role:desc:2B-Tester', '测试人员', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('381', 'en_US', 'role:name:2B-Operator', 'Operator', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('382', 'zh_CN', 'role:name:2B-Operator', '运营人员', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('383', 'en_US', 'role:desc:2B-Operator', 'Operator', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('384', 'zh_CN', 'role:desc:2B-Operator', '运营人员', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('385', 'en_US', 'role:name:2B-DataQueryer', 'DataQueryer', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('386', 'zh_CN', 'role:name:2B-DataQueryer', '数据查询人员', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('387', 'en_US', 'role:desc:2B-DataQueryer', 'DataQueryer', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('388', 'zh_CN', 'role:desc:2B-DataQueryer', '数据查询人员', null, '2018-07-12 03:17:48', null, '2018-07-12 03:17:48', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('399', 'en_US', 'ota:otaguanli', 'OTA管理en', null, '2018-07-24 08:28:04', null, '2018-07-24 08:28:04', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('400', 'zh_CN', 'ota:otaguanli', 'OTA管理', null, '2018-07-24 08:28:00', null, '2018-07-24 08:28:00', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('401', 'en_US', 'ota:shengjijihua', '升级计划en', null, '2018-07-24 08:28:10', null, '2018-07-24 08:28:10', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('402', 'zh_CN', 'ota:shengjijihua', '升级计划', null, '2018-07-24 08:28:06', null, '2018-07-24 08:28:06', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('403', 'en_US', 'ota:banbenguanli', '版本管理en', null, '2018-07-24 08:28:16', null, '2018-07-24 08:28:16', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('404', 'zh_CN', 'ota:banbenguanli', '版本管理', null, '2018-07-24 08:28:12', null, '2018-07-24 08:28:12', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('405', 'en_US', 'ota:lishishengjibaogao', '历史升级报告en', null, '2018-07-24 16:29:15', null, '2018-07-24 16:29:18', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('406', 'zh_CN', 'ota:lishishengjibaogao', '历史升级报告', null, '2018-07-24 08:27:54', null, '2018-07-24 08:27:54', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('407', 'zh_CN', 'ota:chenggongrizhi', '成功日志', null, '2018-07-24 08:32:51', null, '2018-07-24 08:32:51', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('408', 'en_US', 'ota:chenggongrizhi', '成功日志en', null, '2018-07-24 08:33:05', null, '2018-07-24 08:33:05', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('409', 'zh_CN', 'ota:shibairizhi', '失败日志', null, '2018-07-24 16:34:38', null, '2018-07-24 08:33:50', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('410', 'en_US', 'ota:shibairizhi', '失败日志en', null, '2018-07-24 16:34:40', null, '2018-07-24 08:33:54', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('411', 'en_US', 'dict_item:zhengbaoshengji', '整包升级en', null, '2018-07-24 16:34:40', null, '2018-07-25 08:17:58', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('412', 'zh_CN', 'dict_item:zhengbaoshengji', '整包升级', null, '2018-07-25 08:18:24', null, '2018-07-25 08:18:24', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('413', 'en_US', 'dict_item:dict_item:chafenshengji', '差分升级en', null, '2018-07-24 16:34:40', null, '2018-07-25 08:17:58', 'valid');
replace INTO `iot_db_system`.`lang_info` VALUES ('414', 'zh_CN', 'dict_item:zhengbaoshengji', '差分升级', null, '2018-07-25 08:19:11', null, '2018-07-25 08:19:11', 'valid');
