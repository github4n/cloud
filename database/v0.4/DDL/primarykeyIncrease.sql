alter table `iot_db_boss`.`malf_record` change id `id` bigint(20) auto_increment;
alter table `iot_db_boss`.`video_refund_log` change id  `id` bigint(20) AUTO_INCREMENT;
alter table `iot_db_boss`.`video_refund_record` change id  `id` bigint(20) AUTO_INCREMENT;

alter table `iot_db_device`.`uuid_apply_record` change id  `id` bigint(20) AUTO_INCREMENT;

alter table `iot_db_file`.`file_info` change id  `id` bigint(20) AUTO_INCREMENT;

alter table `iot_db_payment`.`goods_extend_service` change id  `id` bigint(20) AUTO_INCREMENT;
alter table `iot_db_payment`.`goods_info` change id  `id` bigint(20) AUTO_INCREMENT;
alter table `iot_db_payment`.`order_goods` change id  `id` bigint(20) AUTO_INCREMENT;
alter table `iot_db_payment`.`order_goods_extend_service` change id  `id` bigint(20) AUTO_INCREMENT;
alter table `iot_db_payment`.`pay_transation` add constraint PK_pay_transation_1 primary key(id);
alter table `iot_db_payment`.`pay_transation` change id  `id` bigint(20) AUTO_INCREMENT;

alter table `iot_db_permission`.`permission` change id  `id` bigint(20) AUTO_INCREMENT;
alter table `iot_db_permission`.`role` change id  `id` bigint(20) AUTO_INCREMENT;
alter table `iot_db_permission`.`role_permission_relate` change id  `id` bigint(20) AUTO_INCREMENT;
alter table `iot_db_permission`.`user_role_relate` change id  `id` bigint(20) AUTO_INCREMENT;