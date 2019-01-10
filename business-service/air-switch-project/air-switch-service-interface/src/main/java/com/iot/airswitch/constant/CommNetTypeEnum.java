package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/10/16
 * @Description: 通讯模块网络类型
 */
public enum CommNetTypeEnum {

    FOUR_EIGHT_FIVE("88", "485通讯模块"),
    NET_PAPE("89", "网口通讯模块"),
    NET_PAPE_PLUS_WIFI("8A", "网口+wifi通讯模块"),
    NET_PAPE_PLUS_NB("8B", "网口+NB通讯模块"),
    NET_PAPE_PLUS_2G("8C", "网口+2G通讯模块");

    public String code;
    public String desc;

    CommNetTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
