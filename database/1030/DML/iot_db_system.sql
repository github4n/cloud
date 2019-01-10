REPLACE INTO `iot_db_system`.`sys_dict_type` (`type_id`, `type_name`) VALUES ('11', 'UUID支付跳转URL');
REPLACE INTO `iot_db_system`.`sys_dict_type` (`type_id`, `type_name`) VALUES ('12', 'Google接入支付跳转URL');
REPLACE INTO `iot_db_system`.`sys_dict_type` (`type_id`, `type_name`) VALUES ('13', 'APP打包跳转URL');


REPLACE INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('11', 'cancelUrl', '/payment/payfail?params=basicOperate-orderList', '取消url', '2');
REPLACE INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('11', 'failedUrl', '/payment/payfail?params=basicOperate-orderList', '失败url', '3');
REPLACE INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('11', 'successUrl', '/payment/paysuccess?params=basicOperate-orderList', '成功url', '1');
REPLACE INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('12', 'cancelUrl', '/payment/payfail?params=productList-alexaAccess-payment', '取消url', '2');
REPLACE INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('12', 'failedUrl', '/payment/payfail?params=productList-alexaAccess-payment', '失败url', '3');
REPLACE INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('12', 'successUrl', '/payment/paysuccess?params=productList-expandFunction', '成功url', '1');
REPLACE INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('13', 'cancelUrl', '/payment/payfail?params=productList-bundledetail', '取消url', '2');
REPLACE INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('13', 'failedUrl', '/payment/payfail?params=productList-bundledetail', '失败url', '3');
REPLACE INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('13', 'successUrl', '/payment/paysuccess?params=productList-release', '成功url', '1');
