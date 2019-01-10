DROP PROCEDURE IF EXISTS iot_db_device.migration_tenant_id;
delimiter $$
CREATE PROCEDURE iot_db_device.migration_tenant_id()
BEGIN

	DECLARE tableSchema varchar(64);
	DECLARE tableName varchar(64);
	DECLARE done int DEFAULT 0;

	DECLARE tenant_cursor CURSOR FOR 
	select table_schema, table_name 
	from information_schema.`COLUMNS` 
	where column_name='tenant_id' and data_type in ('bigint', 'int') and table_schema like 'iot_%';

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

	OPEN tenant_cursor;
	cursor_loop : LOOP
		FETCH tenant_cursor INTO tableSchema, tableName;

		IF done = 1 THEN
				LEAVE cursor_loop;
		END IF;
		
		SET @query = CONCAT('update ', tableSchema, '.', tableName, ' set tenant_id=1 where tenant_id=0;');

-- 		select @query;

		PREPARE stmt FROM @query;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;

	END LOOP cursor_loop;
	CLOSE tenant_cursor;


END$$
delimiter ;

CALL iot_db_device.migration_tenant_id();

DROP PROCEDURE IF EXISTS iot_db_device.migration_tenant_id;

-- update iot_db_device.device
update iot_db_device.device d 
inner join iot_db_device.product p on p.id=d.product_id
set d.tenant_id=p.tenant_id, d.update_by=-20181030
where d.tenant_id is null and p.tenant_id is not null and d.parent_id is null;

update iot_db_device.device d 
inner join iot_db_device.device pd on pd.uuid=d.uuid
set d.tenant_id=pd.tenant_id, d.update_by=-20181030
where d.tenant_id is null and pd.tenant_id is not null;


-- update iot_db_device.device_state
update iot_db_device.device_state ds
inner join iot_db_device.device d on d.uuid=ds.device_id
set ds.tenant_id=d.tenant_id
where ds.tenant_id is null and d.tenant_id is not null;

-- update iot_db_device.device_status
update iot_db_device.device_status ds 
inner join iot_db_device.device d on d.uuid=ds.device_id
set ds.tenant_id=d.tenant_id
where ds.tenant_id is null and d.tenant_id is not null;

-- update iot_db_control.space_device
update iot_db_control.space_device sd
inner join iot_db_device.device d on d.uuid=sd.device_id
set sd.tenant_id=d.tenant_id
where sd.tenant_id is null;