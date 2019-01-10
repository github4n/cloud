ALTER TABLE iot_db_control.space MODIFY COLUMN `name` varchar(50) CHARACTER SET utf8mb4;
ALTER TABLE iot_db_control.scene MODIFY COLUMN scene_name varchar(50) CHARACTER SET utf8mb4;
ALTER TABLE iot_db_device.device MODIFY COLUMN `name` varchar(200) CHARACTER SET utf8mb4;
ALTER TABLE iot_db_video.video_plan MODIFY COLUMN plan_name varchar(100) CHARACTER SET utf8mb4;
ALTER TABLE iot_db_ifttt.applet CONVERT TO CHARACTER SET utf8;
ALTER TABLE iot_db_ifttt.applet MODIFY COLUMN `name` varchar(100) CHARACTER SET utf8mb4;
ALTER TABLE iot_db_control.automation MODIFY COLUMN `name` VARCHAR(50) CHARACTER SET utf8mb4;