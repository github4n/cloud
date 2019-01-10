SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'device_extend' and COLUMN_NAME='server_ip';
SET @query = If(@exist=0,
'alter TABLE iot_db_device.device_extend add COLUMN comm_type varchar(12) DEFAULT NULL COMMENT ''产品类型 : 88(485),89(网口),8A(网口+wifi)，8B(网口+NB)，8C(网口+2G)，8D(网口+4G）'',
 add COLUMN timezone varchar(12) DEFAULT NULL COMMENT ''时区'',
 add COLUMN server_ip varchar(64) DEFAULT NULL COMMENT ''服务器IP'',
 add COLUMN server_port bigint(10) DEFAULT NULL COMMENT ''服务器端口'',
 add COLUMN report_interval bigint(10) DEFAULT NULL COMMENT ''上传间隔，单位秒''
 ',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

