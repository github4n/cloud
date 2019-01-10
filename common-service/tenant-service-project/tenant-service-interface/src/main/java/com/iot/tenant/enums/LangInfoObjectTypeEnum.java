package com.iot.tenant.enums;


import com.iot.common.util.StringUtil;

/**
 * 项目名称：cloud
 * 功能描述：文案对应的类型
 * 创建人： yeshiyuan
 * 创建时间：2018/9/29 16:18
 * 修改人： yeshiyuan
 * 修改时间：2018/9/29 16:18
 * 修改描述：
 */
public enum LangInfoObjectTypeEnum {

    //OTA升级文案
    prodcutOTA,
    //app配置文案
    appConfig,
    //设备配网文案
    deviceNetwork,
    //设备类型文案
    deviceType,
    //功能组属性参数文案
    property;

    public static boolean checkObjectType(String objectType) {
        if (StringUtil.isBlank(objectType)) {
            return false;
        }
        for (LangInfoObjectTypeEnum o : LangInfoObjectTypeEnum.values()) {
            if (objectType.equals(o.name())) {
                return true;
            }
        }
        return false;
    }

    /**
      * @despriction：校验是否是设备类型
      * @author  yeshiyuan
      * @created 2018/9/30 18:21
      * @return
      */
    public static boolean checkIsDeviceType(String objectType) {
        if (StringUtil.isBlank(objectType)) {
            return false;
        }
        return prodcutOTA.name().equals(objectType) || deviceNetwork.toString().equals(objectType) || deviceType.name().equals(objectType);
    }
}
