-- 2018.09.12 by laiguiming add app_version
CREATE TABLE IF NOT EXISTS iot_db_tenant.`app_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `version` bigint(20) NOT NULL COMMENT '版本号',
  `app_id` bigint(20) NOT NULL COMMENT '应用主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户主键',
  `install_mode` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '安装参数',
  `remark` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `full_file_id` bigint(20) DEFAULT NULL COMMENT '全量包文件ID',
  `full_file_md5` varchar(64) DEFAULT NULL COMMENT '全量包文件Md5',
  `incr_file_id` bigint(20) DEFAULT NULL COMMENT '增量包文件ID',
  `incr_file_md5` varchar(64) DEFAULT NULL COMMENT '增量包文件Md5',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='应用版本记录';

-- 2018.09.27 by huanglingcong add app_version column (fulle_file_md5, incr_file_md5)
SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_version' and COLUMN_NAME='full_file_md5';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_version ADD COLUMN full_file_md5 varchar(64) DEFAULT NULL COMMENT ''全量包文件Md5''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_tenant' AND table_name = 'app_version' and COLUMN_NAME='incr_file_md5';

SET @query = If(@exist=0,
		'ALTER TABLE iot_db_tenant.app_version ADD COLUMN incr_file_md5 varchar(64) DEFAULT NULL COMMENT ''全量包文件Md5''',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;