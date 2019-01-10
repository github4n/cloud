UPDATE iot_db_device.product set is_direct_device = 1 where communication_mode in (1,4);
UPDATE iot_db_device.product set is_direct_device = 0 where communication_mode in (2,3);

-- 清洗子设备协议为小写
UPDATE iot_db_device.product_config_netmode SET `name` = 'zwave' where `name` = 'Zwave' and product_id in
(SELECT id from iot_db_device.product where communication_mode = 3);


UPDATE iot_db_device.product_config_netmode SET `name` = 'zigbee' where `name` = 'Zigbee' and product_id in
(SELECT id from iot_db_device.product where communication_mode = 3);

UPDATE iot_db_device.product_config_netmode set `name` = 'ble' where `name` = 'BLE' and product_id in
(SELECT id from iot_db_device.product where communication_mode = 3);

-- 所有协议均变为小写
update iot_db_device.product_config_netmode SET `name` = 'zigbee' where `name` = 'Zigbee';

UPDATE iot_db_device.product_config_netmode SET `name` = 'zwave' where `name` = 'Zwave';

UPDATE iot_db_device.product_config_netmode SET `name` = 'ble' where `name` = 'BLE';

UPDATE iot_db_device.product_config_netmode SET `name` = 'wifi' where `name` = 'WIFI';

-- 删除重复协议
DELETE from iot_db_device.product_config_netmode WHERE id in ( SELECT t.id from (SELECT id FROM iot_db_device.product_config_netmode GROUP BY product_id, `name` HAVING count(1) >1) t);