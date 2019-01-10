update `iot_db_permission`.role set role_type = 'Portal' where id in(9,10,11,12,13,14,15,16);

update `iot_db_permission`.permission set system_type = 'Portal' where id < 50;