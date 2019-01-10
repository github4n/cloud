package com.iot.payment.contants;

import com.iot.common.constant.SystemConstants;

public class ModuleConstants extends SystemConstants {


    /**--------------------- redis key前缀 --------------------*/
    /**
     * paypal支付信息缓存（规则： key为 pay:payId:paypal生成的id ,value为支付信息json字符串   ）
     */
    public static final String REDIS_PRE_PAY_PAYID = "pay:payId:";

//    public static final String DB_TABLE_IOT_DB_PAYMENT = "max-row-id:pay_transation";
//
//    public static final String DB_TABLE_ORDER_GOODS_EX_SER = "max-row-id:order_goods_extend_service";
//
//    public static final String DB_TABLE_ORDER_GOODS = "max-row-id:order_goods";

}
