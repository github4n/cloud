REPLACE INTO iot_db_ifttt.`service` VALUES (1, '定时服务', '固定时间点触发', 'timer', 'this', 1, 1, '2018-9-27 09:50:59', '2018-9-27 09:50:59');
REPLACE INTO iot_db_ifttt.`service` VALUES (2, '天文定时服务', '天文定时，每天日出前后时间触发', 'astronomical ', 'this', 1, 1, '2018-9-27 09:50:59', '2018-9-27 09:50:59');
REPLACE INTO iot_db_ifttt.`service` VALUES (3, '时间范围服务', '判断当前时间是否在范围内', 'timeRange', 'this', 1, 1, '2018-9-27 09:50:59', '2018-9-27 09:50:59');
REPLACE INTO iot_db_ifttt.`service` VALUES (4, '设备状态服务', '设备状态变化时，判断是否触发', 'devStatus', 'this', 1, 1, '2018-9-27 01:50:07', '2018-9-27 09:50:59');
REPLACE INTO iot_db_ifttt.`service` VALUES (5, 'MQ消息服务', '发送MQ消息动作', 'MQ', 'that', 1, 1, '2018-9-27 01:50:44', '2018-9-27 09:50:59');
REPLACE INTO iot_db_ifttt.`service` VALUES (6, '邮件服务', '发送邮件动作', 'email', 'that', 1, 1, '2018-9-27 01:51:09', '2018-9-27 09:50:59');
REPLACE INTO iot_db_ifttt.`service` VALUES (7, '短信服务', '发送短信动作', 'sms', 'that', 1, 1, '2018-9-27 01:51:39', '2018-9-27 09:50:59');