SELECT Count(1)
		INTO @exist
FROM
    information_schema.statistics
WHERE
    table_schema = 'iot_db_device'
AND table_name = 'product_config_netmode'
AND index_name = 'idx_product';


SET @query = If(@exist=0,
		'ALTER TABLE iot_db_device.product_config_netmode ADD INDEX idx_product (product_id)',
		'SELECT \'nothing\' status');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;