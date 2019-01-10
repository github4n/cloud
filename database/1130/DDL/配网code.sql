SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_device' AND table_name = 'network_type' and COLUMN_NAME='type_code';
SET @query = If(@exist=0,
'alter TABLE iot_db_device.network_type add COLUMN type_code VARCHAR(20) DEFAULT NULL COMMENT \'字典表\'',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

replace INTO `iot_db_system`.`sys_dict_type` (`type_id`, `type_name`) VALUES ('15', '配网模式code');
replace INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('15', 'wired', 'wired', 'wired配网模式', '1');
replace INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('15', 'ap', 'ap', 'ap配网模式', '2');
replace INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('15', 'smartlink', 'smartlink', 'smartlink配网模式', '3');
replace INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('15', 'qrcode', 'qrcode', 'qrcode配网模式', '4');
replace INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('15', 'zigbee', 'zigbee', 'zigbee配网模式', '5');
replace INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('15', 'zwave', 'zwave', 'zwave配网模式', '6');
replace INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('15', 'ble', 'ble', 'ble配网模式', '7');

