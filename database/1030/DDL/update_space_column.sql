ALTER TABLE iot_db_control.`space`
MODIFY COLUMN `parent_id`  bigint(20) NULL DEFAULT -1 COMMENT '上级ID';

