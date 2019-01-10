
USE iot_db_device;

-- 删除多余数据 保留最新数据
delete from iot_db_device.device_state where id not in (

SELECT id from (
SELECT
			s.id,
			s.device_id,
			s.property_name,
			s.property_value
		FROM
			iot_db_device.device_state AS s
		GROUP BY
			s.device_id,
			s.property_name
)as  da_id
)