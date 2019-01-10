package com.iot.control.packagemanager.enums;

/**
  * @despriction：套包設備類型必須枚舉類
  * @author  yeshiyuan
  * @created 2018/11/20 21:57
  */
public enum  PackageDeviceTypeChooseEnum {

    Y("Y","必選"),
    N("N","不用");

    private String value;

    private String desc;

    PackageDeviceTypeChooseEnum(String value, String desc) {
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
        for (PackageDeviceTypeChooseEnum item: PackageDeviceTypeChooseEnum.values()) {
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
