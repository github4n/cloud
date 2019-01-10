USE iot_db_device;
insert into `iot_db_device`.`product_service_info`  (
  tenant_id,
  product_id,
  service_id,
  create_by,
  create_time,
  update_time,
  audit_status) SELECT tenant_id, id, 10 as service_id, create_by, create_time, update_time,service_goo_audit_status from `iot_db_device`.`product`  where service_goo_audit_status is not null;

insert into `iot_db_device`.`product_service_info` (
  tenant_id,
  product_id,
  service_id,
  create_by,
  create_time,
  update_time,
  audit_status) SELECT tenant_id, id, 11 as service_id, create_by, create_time, update_time,service_goo_audit_status from `iot_db_device`.`product` where service_alx_audit_status is not null;
