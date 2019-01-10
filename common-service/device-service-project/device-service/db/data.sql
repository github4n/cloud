INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (2, 'OnOff', 'OnOff', 2, 0, NULL, '{\"boolean\":\"0\"}', 'OnOff');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (3, 'Dimming', 'Dimming', 2, 1, NULL, '{\"min\":\"0\",\"max\":\"100\"}', 'Dimming');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (4, 'CCT', 'CCT', 2, 1, NULL, '{\"min\":\"2700\",\"max\":\"6500\"}', 'cct');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (5, 'RGBW', 'RGBW', 2, 1, '', '{\"min\":\"\",\"max\":\"\"}', 'rgbw');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (6, 'Blink', 'Blink', 2, 2, NULL, NULL, 'Blink');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (7, 'AlarmStatus', 'AlarmStatus', 0, 0, '', '{\"boolean\":\"0\"}', 'Alarm');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (8, 'PowerLowStatus', 'PowerLowStatus', 0, 0, '', '{\"boolean\":\"0\"}', 'PowerLowStatus');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (9, 'TamperStatus', 'TamperStatus', 0, 0, '', '{\"boolean\":\"0\"}', 'TamperStatus');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (10, 'TempStatus', 'TempStatus', 0, 5, '', '{\"boolean\":\"0\"}', 'TempStatus');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (11, 'EnergyStatus', 'EnergyStatus', 0, 5, '', '{\"boolean\":\"0\"}', 'EnergyStatus');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (12, 'Duration', 'Duration', 2, 2, NULL, NULL, 'Duration');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (13, 'OccupancyStatus', 'OccupancyStatus', 0, 0, NULL, '{\"boolean\":\"0\"}', 'OccupancyStatus');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (14, 'HumStatus', 'HumStatus', 0, 5, NULL, NULL, 'HumStatus');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (15, 'DoorStatus', 'DoorStatus', 0, 0, NULL, '{\"boolean\":\"0\"}', 'DoorStatus');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (16, 'BrightnessStatus', 'BrightnessStatus', 0, 1, NULL, '{\"min\":\"\",\"max\":\"\"}', 'BrightnessStatus');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (17, 'Control', 'Control', 2, 5, NULL, NULL, 'Control');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (18, 'LiftStatus', 'LiftStatus', 2, 1, NULL, '{\"min\":\"0\",\"max\":\"100\"}', 'LiftStatus');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (19, 'KeyCodeStatus', 'KeyCodeStatus', 2, 1, NULL, NULL, 'KeyCodeStatus');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (20, 'EventStatus', 'EventStatus', 2, 5, NULL, NULL, 'EventStatus');
INSERT INTO `data_point` (`id`, `label_name`, `property_code`, `mode`, `data_type`, `icon_name`, `property`, `description`) VALUES (21, 'SmokeStatus', 'SmokeStatus', 0, 0, NULL, '{\"boolean\":\"0\"}', 'SmokeStatus');



























INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (1, -1006, 'RGB灯', 11, 11, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (2, -1003, 'Dimmable灯', 44, 66, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (3, -1005, 'ColourTemperature灯', 44, 66, '2018-4-19 09:57:39', '2018-4-19 09:57:39', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (4, -1007, 'Doorlock', 44, 66, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (5, -1004, 'Sensor_PIR', 44, 66, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (6, -1008, 'Meter', 44, 66, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (7, -1009, 'Waterleak', 44, 66, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (8, -1010, 'Humiture', 44, 66, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (9, -1011, 'Mulitlevel', 44, 66, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (10, -1012, 'Smoke', 44, 66, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (11, -1013, 'Smartplug_OnOff', 44, 66, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (12, -2001, 'HubGateway', 44, 66, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (13, -1006, 'RGB灯', 11, 11, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (14, -1016, 'Alarm_Siren', 11, 11, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (15, -1017, 'WindowCovering_Nomal', 11, 11, '2018-4-12 19:02:22', '2018-4-12 19:02:22', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (16, -1018, 'Control_Remote_Keycode', 11, 11, '2018-4-13 15:54:00', '2018-4-13 15:54:00', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (17, -1019, 'Light_OnOff_Connector', 11, 11, '2018-4-13 13:39:09', '2018-4-13 13:39:11', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (18, -1020, 'ColorTemperature灯', 44, 44, '2018-4-19 09:57:57', '2018-4-19 09:57:57', '1', NULL);
INSERT INTO `product` (`id`, `device_type_id`, `product_name`, `communication_mode`, `transmission_mode`, `create_time`, `update_time`, `model`, `config_net_mode`) VALUES (1090210037, 1, '多协议网关', 11, 11, '2018-4-2 17:18:51', '2018-4-2 17:18:51', '1', NULL);





























INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-2001, 'HubGateway', '', NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'HubGateway', 'HubGateway');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1021, 'Light_Dimmable_Connector', NULL, NULL, NULL, '2018-4-19 17:40:21', '2018-4-19 17:40:40', NULL, NULL, NULL, 'MultiProtocolGateway', 'Light_Dimmable_Connector');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1020, 'Light_ColorTemperature', NULL, NULL, NULL, '2018-4-19 09:54:56', '2018-4-19 09:56:08', NULL, NULL, NULL, 'MultiProtocolGateway', 'Light_ColorTemperature');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1019, 'Light_OnOff_Connector', NULL, NULL, NULL, '2018-4-13 13:37:31', '2018-4-13 13:37:47', NULL, NULL, NULL, 'MultiProtocolGateway', 'Light_OnOff_Connector');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1018, 'Control_Remote_Keycode', NULL, NULL, NULL, '2018-4-13 13:36:49', '2018-4-13 15:53:22', NULL, NULL, NULL, 'MultiProtocolGateway', 'Control_Remote_Keycode');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1017, 'WindowCovering_Nomal', NULL, NULL, NULL, '2018-4-12 15:49:31', '2018-4-12 15:50:01', NULL, NULL, NULL, 'MultiProtocolGateway', 'WindowCovering_Nomal');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1016, 'Alarm_Siren', NULL, NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'MultiProtocolGateway', 'Alarm_Siren');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1015, 'Light_Dimmable_Illuminance', '', NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'MultiProtocolGateway', 'Light_Dimmable_Illuminance');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1014, 'Sensor_volume', NULL, NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_volume');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1013, 'Smartplug_OnOff', '', NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'MultiProtocolGateway', 'Smartplug_OnOff');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1012, 'Sensor_Smoke', '', NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_Smoke');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1011, 'Sensor_Mulitlevel', '', NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_Mulitlevel');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1010, 'Sensor_Humiture', '', NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_Humiture');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1009, 'Sensor_Waterleak', '', NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_Waterleak');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1008, 'Smartplug_Meter', '', NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'MultiProtocolGateway', 'Smartplug_Meter');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1007, 'Sensor_Doorlock', '', NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_Doorlock');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1006, 'Light_RGBW', '', NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'MultiProtocolGateway', 'Light_RGBW');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1005, 'Light_ColourTemperature', '', NULL, NULL, '2018-4-2 19:09:44', '2018-4-16 10:09:01', NULL, NULL, NULL, 'MultiProtocolGateway', 'Light_ColourTemperature');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1004, 'Sensor_PIR', NULL, NULL, NULL, '2018-4-2 19:09:44', '2018-4-10 15:40:07', NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_PIR');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (-1003, 'Light_Dimmable', NULL, NULL, NULL, '2018-4-2 19:09:44', '2018-4-13 15:53:36', NULL, NULL, NULL, 'MultiProtocolGateway', 'Light_Dimmable');
INSERT INTO `device_type` (`id`, `name`, `description`, `device_catalog_id`, `tenant_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`, `vender_flag`, `type`) VALUES (1, 'Multi_Gateway', NULL, NULL, NULL, '2018-4-2 19:09:44', '2018-4-2 19:09:44', NULL, NULL, NULL, 'MultiProtocolGateway', 'Multi_Gateway');



INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (2, 8, -1009, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (3, 7, -1009, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (4, 9, -1009, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (5, 10, -1009, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (6, 11, -1009, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (7, 2, -1003, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (8, 3, -1003, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (9, 3, -1005, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (10, 2, -1005, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (11, 4, -1005, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (12, 12, -1005, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (13, 12, -1003, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (14, 6, -1005, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (15, 6, -1003, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (16, 2, -1006, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (17, 3, -1006, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (18, 4, -1006, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (19, 5, -1006, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (20, 6, -1006, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (21, 12, -1006, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (22, 13, -1004, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (23, 9, -1004, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (24, 8, -1004, '2018-4-2 17:18:52', '2018-4-2 17:18:52', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (25, 10, -1010, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (26, 14, -1010, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (27, 9, -1010, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (28, 8, -1010, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (29, 15, -1007, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (30, 9, -1007, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (31, 8, -1007, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (32, 2, -1013, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (34, 13, -1011, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (35, 14, -1011, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (36, 10, -1011, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (37, 9, -1011, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (38, 16, -1011, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (39, 8, -1011, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (40, 2, -1008, '2018-4-2 17:18:53', '2018-4-13 16:08:15', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (41, 17, -1017, '2018-4-12 16:21:49', '2018-4-12 16:21:46', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (42, 18, -1017, '2018-4-12 16:22:01', '2018-4-12 16:22:04', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (43, 19, -1018, '2018-4-13 13:39:58', '2018-4-13 16:08:17', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (44, 2, -1019, '2018-4-13 13:40:20', '2018-4-13 13:40:22', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (45, 6, -1019, '2018-4-13 13:41:03', '2018-4-13 13:41:04', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (46, 12, -1019, '2018-4-13 13:41:30', '2018-4-13 13:41:32', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (47, 20, -1018, '2018-4-13 16:07:18', '2018-4-13 16:07:19', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (48, 21, -1012, '2018-4-16 09:29:28', '2018-4-16 09:29:29', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (49, 8, -1012, '2018-4-16 09:29:38', '2018-4-16 09:29:40', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (50, 9, -1012, '2018-4-16 09:29:54', '2018-4-16 09:29:56', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (51, 2, -1020, '2018-4-19 10:00:33', '2018-4-19 10:00:37', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (52, 3, -1020, '2018-4-19 10:00:47', '2018-4-19 10:00:48', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (53, 4, -1020, '2018-4-19 10:00:59', '2018-4-19 10:01:01', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (54, 6, -1020, '2018-4-19 10:01:14', '2018-4-19 10:01:15', NULL, NULL, NULL);
INSERT INTO `device_type_data_point` (`id`, `data_point_id`, `device_type_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (55, 12, -1020, '2018-4-19 10:01:25', '2018-4-19 10:01:27', NULL, NULL, NULL);





INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (2, 2, 2, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (3, 3, 2, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (4, 2, 3, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (5, 3, 3, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (6, 4, 3, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (7, 7, 7, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (8, 8, 7, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (9, 9, 7, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (10, 10, 7, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (11, 11, 7, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (12, 12, 2, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (13, 12, 3, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (14, 6, 3, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (15, 6, 2, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (16, 2, 11, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (17, 2, 1, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (18, 3, 1, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (19, 4, 1, '2018-4-2 17:18:53', '2018-4-2 17:18:53', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (20, 5, 1, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (21, 6, 1, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (22, 12, 1, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (23, 8, 4, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (24, 9, 4, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (25, 15, 4, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (26, 8, 5, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (27, 9, 5, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (28, 13, 5, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (29, 8, 8, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (30, 9, 8, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (31, 10, 8, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (32, 14, 8, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (34, 8, 9, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (35, 9, 9, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (36, 10, 9, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (37, 13, 9, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (38, 14, 9, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (39, 16, 9, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (40, 2, 6, '2018-4-2 17:18:54', '2018-4-2 17:18:54', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (41, 17, 15, '2018-4-12 16:24:31', '2018-4-12 16:24:34', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (42, 18, 15, '2018-4-12 16:24:44', '2018-4-12 16:24:46', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (43, 19, 16, '2018-4-13 13:42:26', '2018-4-13 16:08:45', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (44, 2, 17, '2018-4-13 13:42:36', '2018-4-13 13:42:37', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (45, 6, 17, '2018-4-13 13:43:04', '2018-4-13 13:43:06', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (46, 12, 17, '2018-4-13 13:43:13', '2018-4-13 13:43:14', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (47, 20, 16, '2018-4-13 16:08:42', '2018-4-13 16:08:43', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (48, 21, 10, '2018-4-16 09:30:28', '2018-4-16 09:30:30', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (49, 8, 10, '2018-4-16 09:30:37', '2018-4-16 09:30:39', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (50, 9, 10, '2018-4-16 09:30:45', '2018-4-16 09:30:48', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (51, 2, 18, '2018-4-19 10:02:03', '2018-4-19 10:02:04', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (52, 3, 18, '2018-4-19 10:02:13', '2018-4-19 10:02:15', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (53, 4, 18, '2018-4-19 10:02:32', '2018-4-19 10:02:34', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (54, 6, 18, '2018-4-19 10:03:00', '2018-4-19 10:03:01', NULL, NULL, NULL);
INSERT INTO `product_data_point` (`id`, `device_data_point_id`, `product_id`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`) VALUES (55, 12, 18, '2018-4-19 10:03:08', '2018-4-19 10:03:10', NULL, NULL, NULL);
