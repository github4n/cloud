REPLACE INTO `iot_db_system`.`sys_dict_type` (`type_id`, `type_name`) VALUES ('14', '套包类型');
REPLACE INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('14', '1', 'package:auto', '自动化套包类型', '0');
REPLACE INTO `iot_db_system`.`sys_dict_item` (`type_id`, `item_id`, `item_name`, `item_desc`, `item_sort`) VALUES ('14', '2', 'package:security', '安防套包类型', '1');

REPLACE INTO `iot_db_system`.`lang_info` (`id`, `lang_type`, `lang_key`, `lang_value`, `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`) VALUES ('51028', 'zh_CN', 'package:auto', '自动化', NULL, '2018-11-22 02:43:10', NULL, '2018-11-22 02:43:10', 'valid');
REPLACE INTO `iot_db_system`.`lang_info` (`id`, `lang_type`, `lang_key`, `lang_value`, `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`) VALUES ('51029', 'en_US', 'package:auto', 'automation', NULL, '2018-11-22 10:46:27', NULL, '2018-11-22 10:46:30', 'valid');
REPLACE INTO `iot_db_system`.`lang_info` (`id`, `lang_type`, `lang_key`, `lang_value`, `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`) VALUES ('51030', 'zh_CN', 'package:security', '安防', NULL, '2018-11-22 10:46:50', NULL, NULL, 'valid');
REPLACE INTO `iot_db_system`.`lang_info` (`id`, `lang_type`, `lang_key`, `lang_value`, `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`) VALUES ('51031', 'en_US', 'package:security', 'security', NULL, '2018-11-22 10:47:03', NULL, NULL, 'valid');
