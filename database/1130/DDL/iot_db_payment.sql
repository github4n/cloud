-- 订单状态枚举值统一
ALTER TABLE iot_db_payment.order_record MODIFY COLUMN `order_status` tinyint(2) NOT NULL COMMENT '订单状态,对应字典表sys_dict_item的type_id=3的记录（0:已关闭；1:待付款；2:付款成功；3:付款失败；4：退款中；5：退款成功；6：退款失败）';

UPDATE `iot_db_system`.`sys_dict_item` SET `item_name`='dict_item:fukuanchenggong' WHERE (`type_id`='3') AND (`item_id`='2');
UPDATE `iot_db_system`.`sys_dict_item` SET `item_name`='dict_item:fukuanshibai' WHERE (`type_id`='3') AND (`item_id`='3');

UPDATE iot_db_payment.order_record set order_status =2 where order_status=3;

SELECT Count(1)
INTO @exist
FROM information_schema.`COLUMNS`
WHERE table_schema = 'iot_db_payment' AND table_name = 'pay_transation' and COLUMN_NAME='refund_reason';
SET @query = If(@exist=0,
'alter TABLE iot_db_payment.pay_transation ADD COLUMN `refund_reason` varchar(200) DEFAULT NULL COMMENT \'退款原因\'',
'SELECT \'nothing\' status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



UPDATE `iot_db_payment`.`goods_info` SET `goods_name`='goods:wififangan', `icon`='plan_wifi' WHERE (`id`='1');

UPDATE `iot_db_system`.`lang_info` SET `lang_key`='goods:wififangan', `lang_value`='wifi方案' WHERE (`id`='261');
UPDATE `iot_db_system`.`lang_info` SET `lang_key`='goods:wififangan', `lang_value`='WIFI Solution' WHERE (`id`='262');

