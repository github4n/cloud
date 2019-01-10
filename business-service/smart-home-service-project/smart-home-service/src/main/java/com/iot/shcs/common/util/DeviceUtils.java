package com.iot.shcs.common.util;

import org.springframework.util.StringUtils;

public class DeviceUtils {

    public static final String ON_OFF_KEY = " is now turned ";

    public static final String DOOR_KEY = " is now ";

    public static String changeValue(String key,String function, String value) {
        String changeValue="";
        switch (function) {
            case "OnOff"://灯，插座
                changeValue = getOnOffValue(key,value);
                break;
            case "Occupancy"://Motion传感器 Sensor_PIR
                changeValue =getOccupancyValue(key,value);
                break;
            case "PowerLow"://传感器低电量警告
//                changeValue = getBatteryValue(key,value);
                break;
            case "Door"://门磁
                changeValue = getDoorValue(key, value);
                break;

//            case "Tamper":
//                changeValue = key + " " + value;
//                break;
//
//            case "SirenLevel":
//                changeValue = key + " " + value;
//                break;
/*******************************auto begin**********************************************************************/

            case "auto"://auto
                changeValue = key + " " + value;
                break;
/*******************************Siren begin**********************************************************************/

/*******************************Siren begin**********************************************************************/

            case "Strobe"://0为不闪，1闪烁--------Siren
                changeValue = key + " " + value;
                break;
            case "WarningMode"://0为静音，1低 2中 3高--------Siren
                changeValue = key + " " + value;
                break;
/*******************************Siren begin**********************************************************************/


/*******************************插座 begin**********************************************************************/
            case "Energy"://插座
                changeValue = key + " " + value;
                break;
            case "Watts"://插座
                changeValue = key + " " + value;
                break;
            case "Voltage"://插座
                changeValue = key + " " + value;
                break;
            case "Current"://插座
                changeValue = key + " " + value;
                break;
/*******************************插座 end**********************************************************************/


/*******************************IPC begin**********************************************************************/
//            case "LedOnOff"://IPC
//                changeValue = getOnOffValue(key, value);
//                break;
            case "VideoAngle"://IPC
                changeValue = key + " " + value;
                break;
            case "SoundLevel"://IPC
                changeValue = key + " " + value;
                break;
            case "MotionDetection"://IPC
                changeValue = key + " " + value;
                break;

            case "Language"://IPC
                changeValue = key + " " + value;
                break;
/*******************************IPC end**********************************************************************/


/*******************************Keypad/keyfob begin***********************************************************/
            case "Arm"://Keypad/keyfob
                changeValue = getKeypandOrFobValue("Arm", value);
                break;
            case "Sos"://Keypad/keyfob
                changeValue = getSecurityValue("sos", value);//求救
                break;
            case "panic"://Keypad/keyfob
                changeValue = getSecurityValue("sos", value);//求救
                break;
/*******************************Keypad/keyfob end*************************************************************/


/*******************************security begin**********************************************************************/
            case "Stay"://security
                changeValue = getSecurityValue("stay", value);
                break;
            case "Away"://security
                changeValue = getSecurityValue("stay", value);
                break;
            case "Off"://security
                changeValue = getSecurityValue("off", value);
                break;
/*******************************security end**********************************************************************/


/*******************************Sensor_Waterleak begin**********************************************************************/
            case "Alarm":
                changeValue=getWaterLeakValue(key,value);
                break;
/*******************************Sensor_Waterleak end**********************************************************************/




/*******************************  4 in 1 FourInOne begin**********************************************************************/

/*******************************  4 in 1 FourInOne end**********************************************************************/



/*******************************WindowCovering_Nomal begin**********************************************************************/
            case "Control":
                changeValue=getCurtainValue(key,value);
                break;
/*******************************WindowCovering_Nomal end**********************************************************************/
//            default:
//                changeValue = key + " " + function + " " + value;
//                break;
        }
//        if (StringUtils.isEmpty(changeValue)) {
//            changeValue = key + " " + function + " " + value;
//        }
        return changeValue;
    }

    private static String getKeypandOrFobValue(String key, String value) {
        if (!StringUtils.isEmpty(value)) {//Stay Away Off
            if (value.equals("0")) {
                return key + " at home";
            }
            if (value.equals("1")) {
                return "" + key + " away home";
            }
            if (value.equals("2")) {
                return "" + key + " no security";
            }
        }
        return "";
    }


    public static String getOnOffValue(String key, String value) {
        if (!StringUtils.isEmpty(value)){
           if(!value.equals("0")) {
               return key + ON_OFF_KEY + "on";
           }else{
               return key + ON_OFF_KEY + "off";
           }
        }
        return "";
    }

    public static String getDoorValue(String key, String value) {
        if (!StringUtils.isEmpty(value)) {
            if (!value.equals("0")) {
                return key + DOOR_KEY + "open";
            } else {
                return key + DOOR_KEY + "closed";
            }
        }
        return "";
    }

    public static String getOccupancyValue(String key,String value) {
        if (!StringUtils.isEmpty(value)) {
            if(value.equals("1")){
                return "" + key + " is motion";
            }else{
                return "" + key + " is idle";
            }
        }
        return "";
    }

    public static String getBatteryValue(String key, String value) {
        if (!StringUtils.isEmpty(value)) {
            if(value.equals("0")){
                return "set "+key+" normal";
            }else{
                return "set "+key+ " low battery";
            }
        }
        return "";
    }

    public static String getSirenLevelValue(String key, String value){
        if (!StringUtils.isEmpty(value)) {
            if(value.equals("0")) {
                return "set "+key+" noLevel";
            }
            if (value.equals("1")) {
                return "set "+key+" lessLevel";
            }
            if (value.equals("2")) {
                return "set "+key+" normal";
            }
            else{
                return "set "+key+" height";
            }
        }
        return "";
    }

    public static String getSecurityValue(String key, String value) {
        if (!StringUtils.isEmpty(value)) {//Stay Away Off
            if (value.equals("0")) {
                return "\""+key + "\" mode is disabled";
            }
            if (value.equals("1")) {
                return "\"" + key + "\" mode is enabled";
            }

        }
        return "";
    }

    public  static String getWaterLeakValue(String key,String value){
        if (!StringUtils.isEmpty(value)) {//getWaterLeakValue
            if (value.equals("1")) {
                return "\"" + key + "\"  detacted wet";
            }
        }
        return "";
    }

    public static String getCurtainValue(String key,String value){
        if (!StringUtils.isEmpty(value)) {//getCurtainValue
            if (value.equals("up")) {
                return "\""+key + "\"  is on";
            }
            if (value.equals("down")) {
                return "\"" + key + "\"  is off";
            }

        }
        return "";
    };

}
