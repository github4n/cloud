package com.iot.control.packagemanager.enums;

import com.iot.common.util.StringUtil;

/**
  * @despriction：安防套包必选设备类型（至少包含其中一种）枚举类
  * @author  yeshiyuan
  * @created 2018/12/7 10:03
  */
public enum  SecurityDeviceTypeEnum {

    keyfob("keyfob"),

    keypad("keypad");

    private String value;

    SecurityDeviceTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
      * @despriction：是否满足
      * @author  yeshiyuan
      * @created 2018/12/7 10:17
      */
    public static boolean isSatisfy(String deviceType) {
        if (StringUtil.isBlank(deviceType)) {
            return false;
        }
        boolean result = false;
        for (SecurityDeviceTypeEnum securityDeviceTypeEnum : SecurityDeviceTypeEnum.values()) {
            if (securityDeviceTypeEnum.getValue().equals(deviceType)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
