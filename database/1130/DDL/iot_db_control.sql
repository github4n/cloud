CREATE TABLE IF NOT EXISTS `iot_db_control`.`package_device_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `package_id` bigint(20) NOT NULL COMMENT '套包id(对应package表主键)',
  `device_type_id` bigint(20) NOT NULL COMMENT '设备类型id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='套包支持设备类型表（boss维护）';

CREATE TABLE IF NOT EXISTS `iot_db_control`.`package_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `package_id` bigint(20) NOT NULL COMMENT '套包id(对应package表主键)',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='套包支持产品表（portal维护）';

CREATE TABLE IF NOT EXISTS `iot_db_control`.`scene_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `package_id` bigint(20) NOT NULL COMMENT '套包id(对应package表主键)',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id(-1则为boss创建的场景)',
  `demo_scene_id` bigint(20) DEFAULT NULL COMMENT '模板场景id，对应租户为-1的场景id',
  `scene_name` varchar(32) comment '场景名称',
  `json` varchar(1000) DEFAULT NULL COMMENT '目标值json格式',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='场景信息表';

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'package' and COLUMN_NAME='icon';
SET @query = If(@exist=0,
'alter TABLE iot_db_control.package add COLUMN icon varchar(50) DEFAULT NULL COMMENT ''图标''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'package' and COLUMN_NAME='description';
SET @query = If(@exist=0,
'alter TABLE iot_db_control.package add COLUMN description varchar(255) DEFAULT NULL COMMENT ''描述''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'package' and COLUMN_NAME='package_type';
SET @query = If(@exist=0,
'alter TABLE iot_db_control.package add COLUMN package_type int(10) not null DEFAULT ''2'' COMMENT ''套包類型(对应sys_dict_item的type_id=14)''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'package' and COLUMN_NAME='package_id';
SET @query = If(@exist=0,
'alter TABLE iot_db_control.package add COLUMN package_id bigint(20)  COMMENT ''选择套包id''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_control' AND table_name = 'template_rule' and COLUMN_NAME='rule_name';
SET @query = If(@exist=0,
'alter TABLE iot_db_control.template_rule add COLUMN rule_name varchar(32)  COMMENT ''规则名称''',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

alter TABLE iot_db_control.template_rule modify COLUMN `type` int(2) DEFAULT NULL COMMENT '模板类型 0:安防 1:scene 2:ifttt;3:策略';

alter TABLE iot_db_control.template_rule modify COLUMN `json` varchar(10000) DEFAULT NULL COMMENT '规则体';





