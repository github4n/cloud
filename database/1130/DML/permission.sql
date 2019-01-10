REPLACE INTO `iot_db_permission`.`permission` VALUES ('1000', null, 'HOME_PAGE', '/','首页', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1001', null, 'SOLUTION_PAGE', '/','解决方案',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1002', '1001', 'MEETING_SYSTEM', '/','会议系统', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1003', '1002', 'MEETING_MANAGER', '/','会议管理',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1004', '1001', 'WISDOM_PARK', '/','智慧停车',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1005', '1004', 'CAR_MANAGER', '/','车辆管理',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1006', '1004', 'CAR_QUERY', '/','车辆查找', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1007', '1001', 'WISDOM_PROPERTY', '/','智慧物业',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1008', '1007', 'PROPERTY_SPACE_MANAGER', '/','空间管理',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1009', '1007', 'PROPERTY_RELATION_MANAGER','/', '关系管理',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1010', '1001', 'WISDOM_SCHOOL', '/','智慧校园',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1011', '1010', 'FREE_PERIOD_SIGN', '/','自习课签到',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
-- REPLACE INTO `iot_db_permission`.`permission` VALUES ('1012', null, 'MONITOR_PAGE', '/','监测管理',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1013', '1001', 'ELECTRIC_MANAGER', '/','电量管理',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1014', '1013', 'ELECTRIC_STATISTICS', '/','用电统计',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1015', '1013', 'CREEPAGE_INF', '/','漏电情况',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1016', '1013', 'EXCEPTION_ALARM', '/','异常报警', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1017', '1013', 'CREEPAGE_SELF_CHECK','/', '漏电自检',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1018', '1001', 'VIDEO_LIVE','/', '视频直播', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1019', '1018', 'IPC_MANAGER', '/','IPC管理', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1020', '1001', 'CALLER', '/','访客', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1021', '1020', 'CALLER_MANAGER','/', '访客管理', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1022', '1020', 'CALLER_HISTORY','/', '历史访客', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1023', '1001', 'VISITORS_FLOWRATE', '/','人流量', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1024', '1023', 'VISITORS_FLOWRATE_STATISTICS', '/','人流量统计',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1025', null, 'SETTING_PAGE', '/','设置',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1026', '1025', 'SCENE', '/','情景',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1027', '1026', 'SCENE_TEMPLATE', '/','情景模板',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1028', '1026', 'LOCATION_SCENE', '/','整校模板',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1029', '1026', 'SCENE_INIT', '/','初始化', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1030', '1025', 'IFTTT', '/','IFTTT',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1031', '1030', 'MY_IFTTT', '/','我的IFTTT',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1032', '1030', 'MY_IFTTT_TEMPLATE', '/','我的模板', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1033', '1025', 'SPACE', '/','空间',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1034', '1033', 'SPACE_MANAGER', '/','空间管理', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1035', '1033', 'RELATION_MANAGER', '/','关系管理',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1036', '1025', 'DEVICE', '/','设备',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1037', '1036', 'DEVICE_LIST', '/','设备列表',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1038', '1036', 'REMOTE_CONTROL', '/','遥制器',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1039', '1036', 'DEVICE_PURPOSE', '/','设备用途', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1040', '1036', 'OTA_MANAGER', '/','OTA管理',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1041', '1036', 'PROJECT_OTA_PACKAGE_MANAGER', '产品OTA包管理', '/', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1042', '1036', 'BATCH_ALLOCATION', '/','批量下发',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1043', '1025', 'SCHEDULE', '/','Schedule', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1044', '1043', 'MY_SCHEDULE', '/','我的Schedule',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1045', '1025', 'DASHBOARD', '/','Dashboard', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1046', '1045', 'TEMPLATE_LIST', '/','模板列表','menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1047', '1025', 'USER_MANAGER', '/','用户管理',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1048', '1047', 'USER_LIST', '/','用户列表',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1049', '1047', 'USER_LOG', '/','用户日志',  'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1050', '1047', 'USER_PERMISSION', '/','用户权限', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');
REPLACE INTO `iot_db_permission`.`permission` VALUES ('1051', '1047', 'ROLE_PERMISSION', '/','角色权限', 'menu', '3', null, null, '2B', null, '2018-01-01 00:00:00', null, null, 'valid');