CREATE TABLE  IF NOT EXISTS `iot_db_device`.`operate_step_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `operate_id` bigint(20) NOT NULL COMMENT '操作对象id',
  `operate_type` enum('product','application') NOT NULL COMMENT '操作类型（product：产品；application：应用）',
  `step_index` tinyint(2) NOT NULL COMMENT '步骤下标',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='操作步骤记录表';


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'product' and COLUMN_NAME='step';

SET @query = If(@exist=1,
'ALTER TABLE iot_db_device.product DROP step',
'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;