package com.iot.cloud.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Constants {
    public static final String SESSION_PIX = "session_";  //用户保存厂商代码的前缀
    //空间分类
    public static final String SPACE_BUILD = "BUILD";  //楼
    public static final String SPACE_FLOOR = "FLOOR";  //层
    public static final String SPACE_ROOM = "ROOM";  //房间
    public static final String SPACE_GROUP = "GROUP";  //分组
    //设备分类
    public static final String DEVICE_TYPE_LIGHT = "light";  //灯分类
    public static final String DEVICE_TYPE_PLUG = "plug"; //插座分类
    public static final String DEVICE_TYPE_DOOR = "door"; //门窗分类
    public static final String DEVICE_TYPE_AIR_CONDITIONER = "air_conditioner"; //空调
    public static final String DEVICE_TYPE_SMOKE_SENSOR = "somke_sensor"; //烟雾传感器
    public static final String DEVICE_TYPE_WATER_SENSOR = "water_sensor"; //水浸
    public static final String DEVICE_TYPE_TEMPERATURE_SENSOR = "temperature_sensor";
    public static final String DEVICE_TYPE_PRESENCE_SENSOR = "presence_sensor";
    public static final String DEVICE_TYPE_SENSOR = "sensor"; //传感器
    //websocketType
    public static final String SOCKET_DEVICE = "DEVICE"; //设备
    public static final String SOCKET_SPACE = "SPACE"; //空间
    public static final String SOCKET_SCENE = "SENCE"; //场景
    public static final String SOCKET_IFTTT = "IFTTT"; //联动
    public static final String SOCKET_WARNING = "WARNING"; //告警
    public static final String SOCKET_MOUNT = "MOUNT"; //告警
    //redis 前缀分类
    public static final String REDIS_SPACE_STR = "SS_";
    public static final String REDIS_DEVICE_STR = "SD_";
    public static final String REDIS_SCENE_STR = "SC_"; //情景前缀
    //房间亮或者灭
    public static final int SPACE_STATUS_BRIGHT = 1;
    public static final int SPACE_STATUS_DARK = 0;
    public static final String DEVICE_SWITCH_PROPERYT = "switch";
    //告警条件
    public static final String WARNING_SPACE_SCOPE = "space";
    public static final String WARNING_DEVICE_SCOPE = "device";
    //直连设备标识
    public static final int IS_DIRECT_DEVICE = 1; //直连设备
    public static final int IS_NOT_DIRECT_DEVICE = 0; //非直连设备
    public static final String CALLBACK_KEY_PREFIX = "iftttListenerKey_";
    public static final String DEVICE_REPORT_CALLBACK_KEY_PREFIX = "deviceReportListenerKey_";
    public static final String TOPIC_CLIENT_PREFIX = "iot/v1/c/";
    public static final String REDIS_DEVICE_PROPERTY_PREFIX = "dev_prop:";
    public static final int SUCCESS_CODE = 200;
    public static final int NOT_EXIST = -10992;
    //协议规定里面的变量
    public static final String SCENE_ID = "sceneId"; //情景ID
    public static final String SCENE_NAME = "sceneName"; //情景名称
    public static final String SCENE_SORT = "idx"; //情景详情ID
    public static final String SCENE_RELATION_ID = "id"; //情景关联ID
    public static final String ATTR = "attr"; //属性集合标识
    public static final String SCENE_TYPE = "thenType"; //情景里面类型
    public static final String SCENE_TYPE_DEV = "DEV"; //情景类型为设备
    public static final String TOTAL_COUNT = "totalCount"; //总条数
    public static final String SUCCESS = "then"; //成功数据返回标志
    public static final String USER_ID = "userId"; //用户ID
    public static final String SCENE_RULE_RESP = "getSceneRuleResp"; //情景返回详情方法名称
    public static final String SCENE_SERVICE = "scene"; //情景服务名称
    public static final String EXC_SCENEREQ = "excSceneReq"; //情景方法名称
    public static final String DEL_SCENE_RESP = "delSceneResp"; //情景返回详情方法名称
    public static final String SPACE_ID = "spaceId"; //情景返回详情方法名称
    public static final String SCENE_DETAIL_COUNT = "ruleCount"; //情景统计设备数量key
    public static final String SORT_KEY = "sort"; //情景sort key
    // MQTT KEY
    public static final String MQTT_HOST = "mqtt.host";   // host
    public static final String MQTT_USERNAME = "mqtt.username";   // username
    public static final String MQTT_PASSWORD = "mqtt.password";   // password
    public static final String MQTT_CLIENTID = "mqtt.clientId";   // clientId
    public static final String MQTT_QOS = "mqtt.qos";   // qos
    public static final String MQTT_CLIENTTOPIC = "mqtt.clientTopic";   // clientTopic
    public static final String MQTT_CLIENTBROADCAST = "mqtt.clientBroadcast";   // clientBroadcast
    //联动参数key
    public static final String IFTTT_ID = "autoId"; //ifttt ID
    public static final String IFTTT_IS_MULTI = "enable"; //使能
    public static final int IFTTT_IS_MULTI_TRUE = 1; //夸多直连
    public static final int IFTTT_IS_MULTI_FALSE = 0; //单直连
    public static final String IFTTT_IS_MULTI_ENABLE = "runing"; //单直连
    public static final String IFTTT_IS_MULTI_DISABLE = "stop"; //单直连
    public static final String IFTTT_SORT = "idx"; //联动详情ID
    public static final String IFTTT_DEVICE = "then"; //联动详情ID
    public static final String IFTTT_SENSOR = "if"; //联动详情ID
    public static final String IFTTT_RULEID = "ruleId"; //联动ID
    public static final String IFTTT_SORT_TYPE = "idxType"; //联动排序类型
    public static final String IFTTT_DEL_RESP = "delAutoRuleResp"; //联动详情ID
    // 是否直连设备标识
    public static final String DEVICE_DIRECT_YES = "1";
    public static final String DEVICE_DIRECT_NO = "0";
    // 是否默认家庭
    public static final Integer SPACE_DEFAULT_YES = 1;
    public static final Integer SPACE_DEFAULT_NO = 0;
    // IFTTT 请求标识(add 还是 edit)
    public static final String IFTTT_RESQUEST_ADD = "add";
    public static final String IFTTT_RESQUEST_EDIT = "edit";
    // IFTTT 设备联动关系 redis前缀
    public static final String DEVICE_IFTTT_PREFIX = "device_ifttt_";
    public static final String DEVICE_IFTTT_RULE_PREFIX = "device_ifttt_rule_";
    // IFTTT 表达式关系
    public static final String IFTTT_RELATION_AND = "AND";
    public static final String IFTTT_RELATION_OR = "OR";
    // IFTTT 触发器类型
    public static final String IFTTT_TRIGGER_TIMER = "timer";
    public static final String IFTTT_TRIGGER_DEVICE = "dev";
    // IFTTT 执行器类型
    public static final String IFTTT_THEN_DEVICE = "dev";
    public static final String IFTTT_THEN_SCENE = "scene";
    public static final String IFTTT_THEN_AUTO = "auto";
    // IFTTT 运算符类型
    public static final String IFTTT_COMP_OP_EQUALS = "==";
    public static final String IFTTT_COMP_OP_GREATER_EQUALS = ">=";
    public static final String IFTTT_COMP_OP_LESS_EQUALS = "<=";
    public static final String IFTTT_COMP_OP_GREATER = ">";
    public static final String IFTTT_COMP_OP_LESS = "<";

    // 活动日志标识
    public static final String ACTIVITY_RECORD_DEVICE = "DEVICE";
    public static final String ACTIVITY_RECORD_SCENE = "SCENE";
    public static final String ACTIVITY_RECORD_AUTO = "AUTO";
    public static final String ACTIVITY_RECORD_SECURITY = "SECURITY";
    public static Map<String, Object> catchMap = new HashMap<String, Object>();

    public static String mqttAddress(String host) {
        return "tcp: //" + host + ":1883";
    }

    public static String replace(String jsonStr) {
        return jsonStr.replace("cODE", "CODE").replace("mETHOD", "METHOD").replace("mSG_BODY", "MSG_BODY")
                .replace("rEQUEST_ID", "REQUEST_ID").replace("sERVICE", "SERVICE").replace("rEQUEST_CLIENT_ID", "REQUEST_CLIENT_ID")
                .replace("rEQUEST_TOPIC", "REQUEST_TOPIC");

    }

    //灯的类型
    public static List<String> getLightTypeList() {
        List<String> typeList = new ArrayList<String>();
        typeList.add("Light_Dimmable");
        typeList.add("Light_RGBW");
        typeList.add("Light_ColourTemperature");
        typeList.add("Light_RGBW-HUB"); //hue网关的灯
        return typeList;
    }

    //插座的类型
    public static List<String> getPlugTypeList() {
        List<String> typeList = new ArrayList<String>();
        typeList.add("Smartplug_OnOff");
        typeList.add("Smartplug_Meter");
        return typeList;
    }

    //Sensor的类型
    public static Map<String, String> getSensorTypeMap() {
        Map<String, String> typeMap = new HashMap<String, String>();
        typeMap.put("-1004", "Sensor_PIR");
        typeMap.put("-1007", "Sensor_Doorlock");
        typeMap.put("-1009", "Sensor_Waterleak");
        typeMap.put("-1010", "Sensor_Humiture");
        typeMap.put("-1011", "Sensor_Mulitlevel");
        typeMap.put("-1012", "Sensor_Smoke");
        return typeMap;
    }

    public static Map<String, String> getSensorAndPlugTypeMap() {
        Map<String, String> typeMap = new HashMap<String, String>();
        typeMap.put("-1004", "Sensor_PIR");
        typeMap.put("-1007", "Sensor_Doorlock");
        typeMap.put("-1009", "Sensor_Waterleak");
        typeMap.put("-1010", "Sensor_Humiture");
        typeMap.put("-1011", "Sensor_Mulitlevel");
        typeMap.put("-1012", "Sensor_Smoke");
        typeMap.put("-1013", "Smartplug_OnOff");
        typeMap.put("-1008", "Smartplug_Meter");
        return typeMap;
    }

    /**
     * 插座类型初始化
     *
     * @return
     */
    public static Map<String, String> getPlugTypeMap() {
        Map<String, String> typeMap = new HashMap<String, String>();
        typeMap.put("-1013", "Smartplug_OnOff");
        typeMap.put("-1008", "Smartplug_Meter");
        return typeMap;
    }

}
