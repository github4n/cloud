
-- 表 product
REPLACE INTO `iot_db_device`.`product` VALUES (1, -1006, 'RGB灯', 11, 11, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (2, -1003, 'Dimmable灯', 44, 66, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (3, -1005, 'ColorTemperature灯', 44, 66, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (4, -1007, 'Doorlock', 44, 66, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (5, -1004, 'Sensor_PIR', 44, 66, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (6, -1008, 'Meter', 44, 66, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (7, -1009, 'Waterleak', 44, 66, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (8, -1010, 'Humiture', 44, 66, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (9, -1011, 'Mulitlevel', 44, 66, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (10, -1012, 'Smoke', 44, 66, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (11, -1013, 'Smartplug_OnOff', 44, 66, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (12, -2001, 'HubGateway', 44, 66, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (13, -1006, 'RGB灯', 11, 11, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (14, -1016, 'Alarm_Siren', 11, 11, NOW(), NOW(), '1', NULL);
REPLACE INTO `iot_db_device`.`product` VALUES (1090210037, 1, '多协议网关', 11, 11, NOW(), NOW(), '1', NULL);

-- 表 data_point
REPLACE INTO `iot_db_device`.`data_point` VALUES (2, 'OnOff', 'OnOff', 2, 0, NULL, '{\"boolean\":\"0\"}', 'OnOff');
REPLACE INTO `iot_db_device`.`data_point` VALUES (3, 'Dimming', 'Dimming', 2, 1, NULL, '{\"min\":\"0\",\"max\":\"100\"}', 'Dimming');
REPLACE INTO `iot_db_device`.`data_point` VALUES (4, 'CCT', 'CCT', 2, 1, NULL, '{\"min\":\"2700\",\"max\":\"6500\"}', 'cct');
REPLACE INTO `iot_db_device`.`data_point` VALUES (5, 'RGBW', 'RGBW', 2, 1, '', '{\"min\":\"\",\"max\":\"\"}', 'rgbw');
REPLACE INTO `iot_db_device`.`data_point` VALUES (6, 'Blink', 'Blink', 2, 2, NULL, NULL, 'Blink');
REPLACE INTO `iot_db_device`.`data_point` VALUES (7, 'AlarmStatus', 'AlarmStatus', 0, 0, '', '{\"boolean\":\"0\"}', 'Alarm');
REPLACE INTO `iot_db_device`.`data_point` VALUES (8, 'PowerLowStatus', 'PowerLowStatus', 0, 0, '', '{\"boolean\":\"0\"}', 'PowerLowStatus');
REPLACE INTO `iot_db_device`.`data_point` VALUES (9, 'TamperStatus', 'TamperStatus', 0, 0, '', '{\"boolean\":\"0\"}', 'TamperStatus');
REPLACE INTO `iot_db_device`.`data_point` VALUES (10, 'TempStatus', 'TempStatus', 0, 5, '', '{\"boolean\":\"0\"}', 'TempStatus');
REPLACE INTO `iot_db_device`.`data_point` VALUES (11, 'EnergyStatus', 'EnergyStatus', 0, 5, '', '{\"boolean\":\"0\"}', 'EnergyStatus');
REPLACE INTO `iot_db_device`.`data_point` VALUES (12, 'Duration', 'Duration', 2, 2, NULL, NULL, 'Duration');
REPLACE INTO `iot_db_device`.`data_point` VALUES (13, 'OccupancyStatus', 'OccupancyStatus', 0, 0, NULL, '{\"boolean\":\"0\"}', 'OccupancyStatus');
REPLACE INTO `iot_db_device`.`data_point` VALUES (14, 'HumStatus', 'HumStatus', 0, 5, NULL, NULL, 'HumStatus');
REPLACE INTO `iot_db_device`.`data_point` VALUES (15, 'DoorStatus', 'DoorStatus', 0, 0, NULL, '{\"boolean\":\"0\"}', 'DoorStatus');
REPLACE INTO `iot_db_device`.`data_point` VALUES (16, 'BrightnessStatus', 'BrightnessStatus', 0, 1, NULL, '{\"min\":\"\",\"max\":\"\"}', 'BrightnessStatus');

-- 表 device_type
REPLACE INTO `iot_db_device`.`device_type` VALUES (-2001, 'HubGateway', '', NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'HubGateway', 'HubGateway');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1016, 'Alarm_Siren', NULL, NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Alarm_Siren');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1015, 'Light_Dimmable_Illuminance', '', NULL, '', NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Light_Dimmable_Illuminance');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1014, 'Sensor_volume', NULL, NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_volume');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1013, 'Smartplug_OnOff', '', NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Smartplug_OnOff');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1012, 'Sensor_Smoke', '', NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_Smoke');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1011, 'Sensor_Mulitlevel', '', NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_Mulitlevel');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1010, 'Sensor_Humiture', '', NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_Humiture');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1009, 'Sensor_Waterleak', '', NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_Waterleak');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1008, 'Smartplug_Meter', '', NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Smartplug_Meter');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1007, 'Sensor_Doorlock', '', NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_Doorlock');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1006, 'Light_RGBW', '', NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Light_RGBW');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1005, 'Light_ColorTemperature', '', NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Light_ColorTemperature');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1004, 'Sensor_Motion', NULL, NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Sensor_Motion');
REPLACE INTO `iot_db_device`.`device_type` VALUES (-1003, 'Light_Dimmable', NULL, NULL, NULL, NOW(), NULL, NULL, NULL, NULL, 'MultiProtocolGateway', 'Light_Dimmable');
REPLACE INTO `iot_db_device`.`device_type` VALUES (1, 'Multi_Gateway', NULL, NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 'MultiProtocolGateway', 'Multi_Gateway');

-- 表 device_type_data_point
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (2, 8, -1009, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (3, 7, -1009, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (4, 9, -1009, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (5, 10, -1009, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (6, 11, -1009, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (7, 2, -1003, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (8, 3, -1003, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (9, 3, -1005, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (10, 2, -1005, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (11, 4, -1005, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (12, 12, -1005, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (13, 12, -1003, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (14, 6, -1005, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (15, 6, -1003, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (16, 2, -1006, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (17, 3, -1006, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (18, 4, -1006, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (19, 5, -1006, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (20, 6, -1006, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (21, 12, -1006, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (22, 13, -1004, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (23, 9, -1004, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (24, 8, -1004, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (25, 10, -1010, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (26, 14, -1010, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (27, 9, -1010, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (28, 8, -1010, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (29, 15, -1007, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (30, 9, -1007, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (31, 8, -1007, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (32, 2, -1013, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (34, 13, -1011, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (35, 14, -1011, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (36, 10, -1011, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (37, 9, -1011, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (38, 16, -1011, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (39, 8, -1011, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`device_type_data_point` VALUES (40, 2, -1008, NOW(), NOW(), NULL, NULL, NULL);

-- 表 product_data_point
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (2, 2, 2, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (3, 3, 2, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (4, 2, 3, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (5, 3, 3, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (6, 4, 3, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (7, 7, 7, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (8, 8, 7, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (9, 9, 7, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (10, 10, 7, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (11, 11, 7, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (12, 12, 2, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (13, 12, 3, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (14, 6, 3, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (15, 6, 2, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (16, 2, 11, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (17, 2, 1, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (18, 3, 1, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (19, 4, 1, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (20, 5, 1, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (21, 6, 1, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (22, 12, 1, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (23, 8, 4, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (24, 9, 4, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (25, 15, 4, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (26, 8, 5, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (27, 9, 5, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (28, 13, 5, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (29, 8, 8, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (30, 9, 8, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (31, 10, 8, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (32, 14, 8, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (34, 8, 9, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (35, 9, 9, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (36, 10, 9, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (37, 13, 9, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (38, 14, 9, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (39, 16, 9, NOW(), NOW(), NULL, NULL, NULL);
REPLACE INTO `iot_db_device`.`product_data_point` VALUES (40, 2, 6, NOW(), NOW(), NULL, NULL, NULL);




