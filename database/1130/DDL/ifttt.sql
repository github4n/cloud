USE iot_db_ifttt;

ALTER TABLE iot_db_ifttt.applet_item MODIFY COLUMN json varchar(16000) NOT NULL COMMENT '规则体';