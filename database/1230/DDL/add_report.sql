CREATE DATABASE IF NOT EXISTS `iot_db_report` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS `iot_db_report`.`report_user_active`(
   id bigint(20) PRIMARY KEY AUTO_INCREMENT COMMENT '主键id',
   tenant_id bigint(20) NOT NULL COMMENT '租户id',
   data_date datetime NOT NULL COMMENT '报表日期',
   active_amount bigint(20) NOT NULL COMMENT '活跃用户数',
   activated_amount bigint(20) NOT NULL COMMENT '激活用户数',
   total_amount bigint(20) NOT NULL COMMENT '用户总数',
   create_by bigint(20) NOT NULL COMMENT '创建人',
   create_time datetime NOT NULL COMMENT '创建时间',
   update_by bigint(20) COMMENT '修改人',
   update_time datetime COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户数据报表';

CREATE TABLE IF NOT EXISTS `iot_db_report`.`report_device_active`(
   id bigint(20) PRIMARY KEY AUTO_INCREMENT COMMENT '主键id',
   tenant_id bigint(20) NOT NULL COMMENT '租户id',
   product_type varchar(20) NOT NULL COMMENT '产品类型',
   data_date datetime NOT NULL COMMENT '报表日期',
   active_amount bigint(20) NOT NULL COMMENT '活跃设备数',
   activated_amount bigint(20) NOT NULL COMMENT '激活设备数',
   total_amount bigint(20) NOT NULL COMMENT '设备总数',
   create_by bigint(20) NOT NULL COMMENT '创建人',
   create_time datetime NOT NULL COMMENT '创建时间',
   update_by bigint(20) COMMENT '修改人',
   update_time datetime COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备数据报表';

CREATE TABLE IF NOT EXISTS `iot_db_report`.`report_device_distributed`(
   id bigint(20) PRIMARY KEY AUTO_INCREMENT COMMENT '主键id',
   tenant_id bigint(20) NOT NULL COMMENT '租户id',
   country_code varchar(20) NOT NULL COMMENT '国家编码',
   data_date datetime NOT NULL COMMENT '报表日期',
   active_amount bigint(20) NOT NULL COMMENT '活跃设备数',
   activated_amount bigint(20) NOT NULL COMMENT '激活设备数',
   total_amount bigint(20) NOT NULL COMMENT '设备总数',
   create_by bigint(20) NOT NULL COMMENT '创建人',
   create_time datetime NOT NULL COMMENT '创建时间',
   update_by bigint(20) COMMENT '修改人',
   update_time datetime COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='区域分布报表';