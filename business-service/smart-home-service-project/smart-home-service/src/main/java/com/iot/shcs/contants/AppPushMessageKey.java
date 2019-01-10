package com.iot.shcs.contants;

/**
 * 项目名称: IOT云平台
 * 模块名称：  app消息推送的 消息key
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/6/26 18:06
 * 修改人:
 * 修改时间：
 */
public class AppPushMessageKey {
    // 设备低电量报警
    public static final String LACK_OF_ELECTRICITY_ALARM = "lack.of.electricity.alarm";
    // 设备离线
    public static final String DEVICE_IS_OFFLINE = "device.is.offline";
    // 防拆被触发
    public static final String ANTI_TAMPER_TRIGGER = "anti-tamper.trigger";
    // 安防被触发
    public static final String SECURITY_TRIGGER = "security.trigger";
    //水浸被触发
    public  static final String WATER_SENSOR_DETECTED_LEAKING="water.sensor.detected.leaking";

    //烟感报警触发
    public static final String SMOKE_SENSOR_IS_ALARMING = "smoke.sensor.is.alarming";

    public static final String SECURITY_TRIGGER_DELAY="security.trigger.delay";

    public static final String SOS_ALERT="sos.alert";

    public static final String IPC_DETECTED_ACTIVITY="ipc.detected.activity";


}