package com.iot.device.enums;

/**
 * 项目名称：cloud
 * 功能描述：虚拟服务订单支付状态
 * 创建人： yeshiyuan
 * 创建时间：2018/6/29 14:57
 * 修改人： yeshiyuan
 * 修改时间：2018/6/29 14:57
 * 修改描述：
 */
public enum ServicePayStatusEnum {

    WAIT_PAY(1,"待付款"),
    PAY_SUCCESS(2,"付款成功"),
    PAY_FAIL(3,"付款失败"),
    REFUNDING(4,"退款中"),
    REFUND_SUCCESS(5,"退款成功"),
    REFUND_FAIL(6,"退款失败");

    private Integer value;

    private String desc;

    ServicePayStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    /**
      * @despriction：判断是否属于此枚举
      * @author  yeshiyuan
      * @created 2018/6/29 14:59
      * @param null
      * @return
      */
    public static boolean checkIsValid(Integer value){
        if (value == null){
            return false;
        }
        for (ServicePayStatusEnum item: ServicePayStatusEnum.values()) {
            if (value == item.getValue()){
                return true;
            }
        }
        return false;
    }

    /**
      * @despriction：校验能否退款
      * @author  yeshiyuan
      * @created 2018/11/15 11:02
      * @return
      */
    public static boolean checkCanRefund(Integer payStatus) {
        if (payStatus == null){
            return false;
        }
        if (PAY_SUCCESS.getValue().equals(payStatus) || REFUND_FAIL.getValue().equals(payStatus)) {
            return true;
        }
        return false;
    }

    /**
     * @despriction：校验能否使用
     * @author  yeshiyuan
     * @created 2018/11/15 11:02
     * @return
     */
    public static boolean checkCanUsed(Integer payStatus) {
        if (payStatus == null){
            return false;
        }
        if (PAY_SUCCESS.getValue().equals(payStatus) || REFUNDING.getValue().equals(payStatus) || REFUND_FAIL.getValue().equals(payStatus)) {
            return true;
        }
        return false;
    }

}
