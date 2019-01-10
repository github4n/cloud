package com.iot.device.enums.uuid;

/**
 * 项目名称：cloud
 * 功能描述：uuid订单支付状态
 * 创建人： yeshiyuan
 * 创建时间：2018/6/29 14:57
 * 修改人： yeshiyuan
 * 修改时间：2018/6/29 14:57
 * 修改描述：
 */
public enum UuidPayStatusEnum {

    WAIT_PAY(1,"待付款"),
    PAYED(2,"已付款");

    private Integer value;

    private String desc;

    UuidPayStatusEnum(Integer value, String desc) {
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
        for (UuidPayStatusEnum item: UuidPayStatusEnum.values()) {
            if (value == item.getValue()){
                return true;
            }
        }
        return false;
    }
}
