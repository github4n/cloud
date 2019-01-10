package com.iot.control.packagemanager.enums;

/**
  * @despriction：方法属性
  * @author  nongchongwei
  * @created 2018/11/20 21:57
  */
public enum AttrTypeEnum {

    ACTION("action","方法"),
    PROPERTY("property","属性"),
    EVENT("event","事件");

    private String value;

    private String desc;

    AttrTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * @despriction：判断是否属于此枚举
     * @author  yeshiyuan
     * @created 2018/6/29 14:59
     * @return
     */
    public static boolean checkIsValid(String value){
        for (AttrTypeEnum item: AttrTypeEnum.values()) {
            if (value.equals(item.getValue())){
                return true;
            }
        }
        return false;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
