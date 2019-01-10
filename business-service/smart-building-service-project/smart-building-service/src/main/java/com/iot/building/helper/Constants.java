package com.iot.building.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iot.building.device.vo.DeviceRemoteControlResp;

public class Constants {

	public final static String SESSION_PIX = "session_"; // 用户保存厂商代码的前缀

	public static String mqttAddress(String host) {
		return "tcp://" + host + ":1883";
	}

	// 空间分类
	public final static String SPACE_BUILD = "BUILD";// 楼

	public final static String SPACE_FLOOR = "FLOOR";// 层

	public final static String SPACE_ROOM = "ROOM";// 房间

	public final static String SPACE_GROUP = "GROUP";// 分组

	public final static Integer MEETING_TRUE = 1;// 会议室标记

	public final static String SPACE_MEETING = "MEETING";// 会议

	public final static String SPACE_HOME = "HOME";// 2C

	// 设备分类
	public final static String DEVICE_TYPE_LIGHT = "light";// 灯分类

	public final static String DEVICE_TYPE_PLUG = "plug";// 插座分类

	public final static String DEVICE_TYPE_DOOR = "door";// 门窗分类

	public final static String DEVICE_TYPE_AIR_CONDITIONER = "air_conditioner";// 空调

	public final static String DEVICE_TYPE_SMOKE_SENSOR = "somke_sensor";// 烟雾传感器

	public final static String DEVICE_TYPE_WATER_SENSOR = "water_sensor";// 水浸

	public final static String DEVICE_TYPE_TEMPERATURE_SENSOR = "temperature_sensor";

	public final static String DEVICE_TYPE_presence_sensor = "presence_sensor";

	public final static String DEVICE_TYPE_SENSOR = "sensor";// 传感器

	public final static String DEVICE_BUSINESS_TYPE_LIGHT = "light";// 默认灯的业务类型

	public final static String DEVICE_BUSINESS_TYPE_PLUG = "plug";// 默认插座的业务类型

	public final static String DEVICE_BUSINESS_TYPE_SENSOR = "sensor";// 默认传感器的业务类型

	// websocketType
	public final static String SOCKET_DEVICE = "DEVICE";// 设备
	public final static String SOCKET_SPACE = "SPACE";// 空间
	public final static String SOCKET_SCENE = "SENCE";// 场景
	public final static String SOCKET_IFTTT = "IFTTT";// 联动
	public final static String SOCKET_WARNING = "WARNING";// 告警
	public final static String SOCKET_MOUNT = "MOUNT";// 告警

	// redis 前缀分类
	public final static String REDIS_SPACE_STR = "SS_";
	public final static String REDIS_DEVICE_STR = "SD_";
	public final static String REDIS_SCENE_STR = "SC_";// 情景前缀

	// 房间亮或者灭
	public final static int SPACE_STATUS_BRIGHT = 1;
	public final static int SPACE_STATUS_DARK = 0;

	// 分组未创建
	public final static int UN_CREATE_GROUP = 0;

	public final static String DEVICE_SWITC_PROPERYT = "switch";

	public static Map<String, Object> catchMap = new HashMap<String, Object>();

	// 设备属性
	public final static String Device_Property_Swtich = "switch";
	public final static String Device_Property_Brightness = "Dimming";
	public final static String Device_Property_Color = "RGBW";
	public final static String Device_Property_CCT = "CCTStatus";
	// 传感器属性
	public final static String Sensor_Property_LastUpdated = "lastUpdated";
	public final static String Sensor_Property_Daylight = "daylight";
	public final static String Sensor_Property_Temperature = "temperature";
	public final static String Sensor_Property_Presence = "presence";

	// 现有状态
	public final static String hub_Device_Status = "hubDeviceStatus";
	public final static String hub_Sensor_Status = "sensorDeviceStatus";
	public final static Map<String, Long> addMountMap = new HashMap<String, Long>();
	public final static Map<String, Long> removeMountMap = new HashMap<>();

	// 告警条件
	public final static String WARNING_SPACE_SCOPE = "space";
	public final static String WARNING_DEVICE_SCOPE = "device";

	// 情景缓存
	public final static Map<String, List<DevicePropertyDTO>> SCENE_DETAIL_MAP = new HashMap<String, List<DevicePropertyDTO>>();
	public final static Map<Long, Set<String>> SCENE_GATEWAY_MAP = new HashMap<Long, Set<String>>();
	// 外接情景设备缓存
	public final static Map<String, Map<String, Object>> SCENE_EXTERNAL_DEVICE_MAP = new HashMap<String, Map<String, Object>>();

	// ifttt属性
	public final static String Trigger_Type_Timing = "timing";
	public final static String Actuator_Type_Space = "space";
	
	// 产品Map
	public final static Map<String, String> PRODUCT_MAP = new HashMap<>();

	public static String replace(String json) {
		json = json.replace("cODE", "CODE").replace("mETHOD", "METHOD").replace("mSG_BODY", "MSG_BODY")
				.replace("rEQUEST_ID", "REQUEST_ID").replace("sERVICE", "SERVICE")
				.replace("rEQUEST_CLIENT_ID", "REQUEST_CLIENT_ID").replace("rEQUEST_TOPIC", "REQUEST_TOPIC");
		return json;
	}

	// 灯的类型
	public static List<String> getLightTypeList() {
		List<String> typeList = new ArrayList<String>();
		typeList.add("Light_Dimmable");
		typeList.add("Light_RGBW");
		typeList.add("Light_ColourTemperature");
		typeList.add("Light_ColorTemperature");
		typeList.add("Light_OnOff_Connector");
		typeList.add("HubGateway");// hue网关的灯
		return typeList;
	}

	// 插座的类型
	public static List<String> getPlugTypeList() {
		List<String> typeList = new ArrayList<String>();
		typeList.add("Smartplug_OnOff");
		typeList.add("Smartplug_Meter");
		return typeList;
	}

	// Sensor的类型
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

	public static Map<String, String> getUnMountGatewayTypeMap() {
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("-1004", "Sensor_PIR");
		typeMap.put("-1007", "Sensor_Doorlock");
		typeMap.put("-1009", "Sensor_Waterleak");
		typeMap.put("-1010", "Sensor_Humiture");
		typeMap.put("-1011", "Sensor_Mulitlevel");
		typeMap.put("-1012", "Sensor_Smoke");
		// typeMap.put("-1013","Smartplug_OnOff");
		// typeMap.put("-1008","Smartplug_Meter");
		// typeMap.put("-1017","WindowCovering_Nomal");
		typeMap.put("-1018", "Control_Remote_Keycode");
		typeMap.put("-2001", "light");
		return typeMap;
	}

	// 外接设备类型
	public static Map<String, String> getExternalDeviceType() {
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("-3000", "AirConditioning");
		typeMap.put("-4000", "AirSwitch");
		typeMap.put("-4001", "AirSwitchNode");
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

	/**
	 * 窗帘类型初始化
	 * 
	 * @return
	 */
	public static Map<String, String> WindowCoveringTypeMap() {
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("-1017", "WindowCovering_Nomal");
		return typeMap;
	}
	
	/**
	 * 墙控类型初始化
	 * 
	 * @return
	 */
	public static Map<String, String> WallSwitchTypeMap() {
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("-1027", "13key_remote");
		return typeMap;
	}
	
	/**
	 * 恒照度类型初始化
	 * 
	 * @return
	 */
	public static Map<String, String> IlluminanceTypeMap() {
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("-1015", "Light_Dimmable_Illuminance");
		return typeMap;
	}

	public static Map<String, String> getLightTypeMap() {
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("-1003", "Light_Dimmable");
		typeMap.put("-1015", "Light_Dimmable_Illuminance");
		typeMap.put("-1019", "Light_OnOff_Connector");
		typeMap.put("-1020", "Light_ColorTemperature");
		typeMap.put("-1006", "Light_RGBW");
//		typeMap.put("-1018", "Control_Remote_Keycode");
//		typeMap.put("-1013", "Smartplug_OnOff");
//		typeMap.put("-1008", "Smartplug_Meter");
		return typeMap;
	}

	/**
	 * 告警类型初始化
	 *
	 * @return
	 */
	public static Map<String, String> warningTypeMap() {
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("-1009", "Sensor_Waterleak");
		typeMap.put("-1012", "Sensor_Smoke");
		return typeMap;
	}

	/**
	 * 中控支持类型初始化
	 * 
	 * @return
	 */
	public static Map<String, String> supportTypeMap() {
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("Multi_Gateway", "1");
		typeMap.put("Light_Dimmable", "-1003");
		typeMap.put("Light_ColourTemperature", "-1005");
		typeMap.put("Light_RGBW", "-1006");
		typeMap.put("WindowCovering_Nomal", "-1017");
		typeMap.put("Light_OnOff_Connector", "-1019");
		typeMap.put("Light_ColorTemperature", "-1020");
		typeMap.put("Light_Dimmable_Connector", "-1021");
		typeMap.put("Light_Dimmable_Illuminance", "-1015");
		typeMap.put("Alarm_Siren", "-1016");
		typeMap.put("Sensor_PIR", "-1004");
		typeMap.put("Sensor_Doorlock", "-1007");
		typeMap.put("Sensor_Waterleak", "-1009");
		typeMap.put("Sensor_Humiture", "-1010");
		typeMap.put("Sensor_Mulitlevel", "-1011");
		typeMap.put("Sensor_Smoke", "-1012");
		typeMap.put("Smartplug_OnOff", "-1013");
		typeMap.put("Smartplug_Meter", "-1008");
		typeMap.put("Control_Remote_Keycode", "-1018");
		typeMap.put("light", "-2001");
		typeMap.put("AirConditioning", "-3000");
		return typeMap;
	}

	/**
	 * 产品类型初始化
	 * 
	 * @return
	 */
	public static Map<String, String> getProductIdMap() {
		Map<String, String> productMap = new HashMap<String, String>();
		productMap.put("1", "100019");
		productMap.put("-1003", "100002");
		productMap.put("-1004", "100005");
		productMap.put("-1005", "100003");
		productMap.put("-1006", "100001");
		productMap.put("-1007", "100004");
		productMap.put("-1008", "100006");
		productMap.put("-1009", "100007");
		productMap.put("-1010", "100008");
		productMap.put("-1011", "100009");
		productMap.put("-1012", "100010");
		productMap.put("-1013", "100011");
		productMap.put("-1016", "100014");
		productMap.put("-1017", "100015");
		productMap.put("-1018", "100016");
		productMap.put("-1019", "100017");
		productMap.put("-1020", "100018");
		productMap.put("-2001", "100012");
		// 空调测试的
		productMap.put("-3000", "1090210138");
		return productMap;
	}

	// 直连设备标识
	public final static int IS_DIRECT_DEVICE = 1;// 直连设备
	public final static int IS_NOT_DIRECT_DEVICE = 0;// 非直连设备

	public final static String TOPIC_CLIENT_PREFIX = "iot/v1/c/";

	public final static String REDIS_DEVICE_PROPERTY_PREFIX = "dev_prop:";

	public final static int SUCCESS_CODE = 200;
	public final static int NOT_EXIST = -10992;

	// 协议规定里面的变量
	public final static String SCENE_ID = "sceneId";// 情景ID
	public final static String SCENE_NAME = "sceneName";// 情景名称
	public final static String SCENE_SORT = "idx";// 情景详情ID
	public final static String SCENE_RELATION_ID = "id";// 情景关联ID
	public final static String SCENE_SILENCE = "silenceStatus";// 情景静默
	public final static String ATTR = "attr";// 属性集合标识
	public final static String SCENE_TYPE = "thenType";// 情景里面类型
	public final static String SCENE_TYPE_DEV = "DEV";// 情景类型为设备
	public final static String TOTAL_COUNT = "totalCount";// 总条数
	public final static String SUCCESS = "then";// 成功数据返回标志
	public final static String USER_ID = "userId";// 用户ID
	public final static String SCENE_RULE_RESP = "getSceneRuleResp";// 情景返回详情方法名称
	public final static String SCENE_SERVICE = "scene";// 情景服务名称
	public final static String EXC_SCENEREQ = "excSceneReq";// 情景方法名称
	public final static String DEL_SCENE_RESP = "delSceneResp";// 情景返回详情方法名称
	public final static String SPACE_ID = "spaceId";// 情景返回详情方法名称
	public final static String SCENE_DETAIL_COUNT = "ruleCount";// 情景统计设备数量key
	public final static String SORT_KEY = "sort";// 情景sort key
	public final static String SCENE_THEN_PARAMS = "params";// 情景then报文里的 params
	public final static String SCENE_THEN_METHOD = "method";// 情景then报文里的 method
	public final static String SCENE_THEN_UUID = "uuid";// 情景then报文里的 uuid
	public final static String SCENE_THEN_TYPE = "type";// 情景then报文里的 type

	// MQTT KEY
	public final static String MQTT_HOST = "mqtt.host"; // host
	public final static String MQTT_PORT = "mqtt.port"; // port
	public final static String MQTT_USERNAME = "mqtt.username"; // username
	public final static String MQTT_PASSWORD = "mqtt.password"; // password
	public final static String MQTT_CLIENTID = "mqtt.clientId"; // clientId
	public final static String MQTT_QOS = "mqtt.qos"; // qos
	public final static String MQTT_CLIENTTOPIC = "mqtt.clientTopic"; // clientTopic
	public final static String MQTT_CLIENTBROADCAST = "mqtt.clientBroadcast"; // clientBroadcast

	// Center control MQTT KEY
	public final static String NETWORK_MQTT_HOST = "network-mqtt.host"; // host
	public final static String NETWORK_MQTT_PORT = "network-mqtt.port"; // port
	public final static String NETWORK_MQTT_USERNAME = "network-mqtt.username"; // username
	public final static String NETWORK_MQTT_PASSWORD = "network-mqtt.password"; // password
	public final static String NETWORK_MQTT_CLIENTID = "network-mqtt.clientId"; // 必须设置为租户ID
	public final static String NETWORK_MQTT_QOS = "network-mqtt.qos"; // qos
	public final static String NETWORK_MQTT_CLIENTTOPIC = "network-mqtt.clientTopic"; // clientTopic
	public final static String NETWORK_MQTT_CLIENTBROADCAST = "network-mqtt.clientBroadcast"; // clientBroadcast

	// Center control MQTT KEY
	public final static String AGENT_MQTT_HOST = "agent-mqtt.host"; // host
	public final static String AGENT_MQTT_PORT = "agent-mqtt.port"; // port
	public final static String AGENT_MQTT_USERNAME = "agent-mqtt.username"; // username
	public final static String AGENT_MQTT_PASSWORD = "agent-mqtt.password"; // password
	public final static String AGENT_MQTT_CLIENTID = "agent-mqtt.clientId"; // 必须设置为租户ID
	public final static String AGENT_MQTT_QOS = "agent-mqtt.qos"; // qos
	public final static String AGENT_MQTT_CLIENTTOPIC = "agent-mqtt.clientTopic"; // clientTopic
	public final static String AGENT_MQTT_CLIENTBROADCAST = "agent-mqtt.clientBroadcast"; // clientBroadcast

	// 联动参数key
	public final static String IFTTT_ID = "autoId";// ifttt ID
	public final static String IFTTT_IS_MULTI = "enable";// 使能
	public final static int IFTTT_IS_MULTI_TRUE = 1;// 跨多直连
	public final static int IFTTT_IS_MULTI_FALSE = 0;// 单直连
	public final static String IFTTT_IS_MULTI_ENABLE = "runing";// 单直连
	public final static String IFTTT_IS_MULTI_DISABLE = "stop";// 单直连
	public final static String IFTTT_SORT = "idx";// 联动详情ID
	public final static String IFTTT_DEVICE = "then";// 联动详情ID
	public final static String IFTTT_SENSOR = "if";// 联动详情ID
	public final static String IFTTT_RULEID = "ruleId";// 联动ID
	public final static String IFTTT_SORT_TYPE = "idxType";// 联动排序类型
	public final static String IFTTT_DEL_RESP = "delAutoRuleResp";// 联动详情ID
	// 是否直连设备标识
	public final static String DEVICE_DIRECT_YES = "1";
	public final static String DEVICE_DIRECT_NO = "0";

	// 是否默认家庭
	public final static Integer SPACE_DEFAULT_YES = 1;
	public final static Integer SPACE_DEFAULT_NO = 0;

	// IFTTT 请求标识(add 还是 edit)
	public final static String IFTTT_RESQUEST_ADD = "add";
	public final static String IFTTT_RESQUEST_EDIT = "edit";

	// IFTTT 设备联动关系 redis前缀
	public final static String DEVICE_IFTTT_PREFIX = "device_ifttt_";
	public final static String DEVICE_IFTTT_RULE_PREFIX = "device_ifttt_rule_";
	// IFTTT 表达式关系
	public final static String IFTTT_RELATION_AND = "AND";
	public final static String IFTTT_RELATION_OR = "OR";
	// IFTTT 触发器类型
	public final static String IFTTT_TRIGGER_TIMER = "timer";
	public final static String IFTTT_TRIGGER_DEVICE = "dev";
	public final static String IFTTT_TRIGGER_SUNRISE = "sunrise";
	public final static String IFTTT_TRIGGER_SUNSET = "sunset";
	// IFTTT 执行器类型
	public final static String IFTTT_THEN_DEVICE = "dev";
	public final static String IFTTT_THEN_SCENE = "scene";
	public final static String IFTTT_THEN_AUTO = "auto";
	// IFTTT 运算符类型
	public final static String IFTTT_COMP_OP_EQUALS = "==";
	public final static String IFTTT_COMP_OP_GREATER_EQUALS = ">=";
	public final static String IFTTT_COMP_OP_LESS_EQUALS = "<=";
	public final static String IFTTT_COMP_OP_GREATER = ">";
	public final static String IFTTT_COMP_OP_LESS = "<";

	// 活动日志标识
	// 情景
	public final static String ACTIVITY_RECORD_SCENE = "SCENE";
	// 设备
	public final static String ACTIVITY_RECORD_DEVICE = "DEVICE";
	// 安防
	public final static String ACTIVITY_RECORD_SECURITY = "SECURITY";
	// 使能
	public final static String ACTIVITY_RECORD_AUTO = "AUTO";

	// 群控-房间设备挂载关系
	public final static Map<String, Set<String>> SPACE_GATEWAY_MOUNT = new HashMap<>();
	public final static Map<String, Set<String>> SPACE_GROUP_UUID = new HashMap<>();
	public final static Map<Long, Set<String>> SPACE_PLUG_MOUNT = new HashMap<>();
	public final static Map<String, Set<String>> SPACE_EXTERNAL_DEVICE_MOUNT = new HashMap<>();

	// 业务类型
	public final static Map<String, Long> DEVICE_BUSINESS_TYPE_MAP = new HashMap<String, Long>();

	// 遥控器集合
	public static Map<Long, List<DeviceRemoteControlResp>> DEVICE_REMOTE_CONTROL = new HashMap<>();// 遥控器类型对应
	public static Map<Long, Integer> SPACE_DIMMING_STATUS = new HashMap<>();// 房间遥控器灯Dimming
	public static Map<String, Integer> REMOTE_ONOFF_STATUS = new HashMap<>();// 房间遥控器灯OnOff
	public static Map<String, Integer> SPACE_CURRENT_ONOFF = new HashMap<>();// 空间当前开关状态 key=spaceId value= 0 or 1
	public static Map<Long, Integer> SCENE_CURRENT_IDX = new HashMap<>();// 请求当前的顺序号 key =sceneID value=idx

	public final static String PROPERTY_MQTT_CLIENT_ID = "mqtt.client.id"; // mqtt.client.id
	public final static String PROPERTY_MQTT_CLIENT_HOST = "mqtt.client.host"; // mqtt.client.host

	public static final String CALLBACK_KEY_PREFIX = "iftttListenerKey";

	public static final String DEVICE_REPORT_CALLBACK_KEY_PREFIX = "deviceReportListenerKey";

	public static final String DEVICE_LISTENER_KEY = "deviceListenerKey";// 设备监听的key

	public static final String SPACE_LISTENER_KEY = "spaceListenerKey";// 空间监听的key

	public static final String SCENE_LISTENER_KEY = "sceneListenerKey";// 情景监听的key

	public static final String WARNING_LISTENER_KEY = "warningListenerKey";// 告警

	public static final String REMOTE_CONTROL_LISTENER_KEY = "remoteControlListenerKey";// 遥控器

	// 定时任务开关
	public final static String TASK_SWITCH_OFF = "false";
	public final static String TASK_SWITCH_ON = "true";

	public final static String TASK_LOAD_COUNT_QUARTZ = "task.cron.load-count-cron"; // load-count-quartz
	public final static String TASK_PUSH_SPACE_QUARTZ = "task.cron.push-space-cron"; // push-space-quartz

	// 中控开关
	public final static String CENTER_CONTROL_IS_OPEN = "center-control.isOpen";
	public final static String CENTER_CONTROL_TENANTID = "center-control.tenantId";
	public final static String CENTER_CONTROL_EXTERNAL_NETWORK = "center-control.externalNetwork";

	// RABBIT_MQ
    public final static String RABBIT_MQ_ON_OFF = "rabbit-mq.switch";
    public final static String RABBIT_MQ_HOST = "rabbit-mq.host";
    public final static String RABBIT_MQ_USERNAME = "rabbit-mq.username";
    public final static String RABBIT_MQ_PWD = "rabbit-mq.pwd";
    public final static String RABBIT_MQ_PORT = "rabbit-mq.port";
	// tenant类型
	public final static Integer SMART_BUILD = 1;// 2b
	public final static Integer SMART_HOME = 0;// 2c

	// 定时任务执行时间
	public final static String SCHEDULE_CRON_POWER = "schedule.cron.power";
	public final static String SCHEDULE_CRON_ETIME = "schedule.cron.e-time";
	public final static String SCHEDULE_CRON_ASTRONOMY = "schedule.cron.astronomy";

	// 厂商类型标识
	public static final String VENDER_FLAG_MULTI_GATEWAY = "MultiProtocolGateway";// 多协议网关
	public static final String VENDER_FLAG_AIRCONDITIONING = "AirConditioning";// 大金空调
	public static final String VENDER_FLAG_MANTUNSCI = "ManTunSci";// 空开

	public final static String SWITCH = "redis-catch.switch";

	//人脸的websocket  ip
	public static final String FACE_URL = "webSocket.faceUrl";
	//人群密度的websocket  ip
	public static final String DENSITY_URL = "webSocket.densityUrl";

	//人脸图片存放路径
	public static final String LOCAL_FILE_CURRENT_FACE_PATH = "local.file.current-face-path";
	//人脸图片网络下载地址
	public static final String LOCAL_FILE_URL_PATH = "local.file.url_path";
	public static final String LOCAL_FILE_SCENEID = "local.file.sceneId";
	//group 存储key
	public static final String GROUP_KEY="group:";
	public static final String GROUP_UUID_KEY="group_uuid:";
	public static final String GROUP_REMOTE_KEY="groupRemote:";
	public static final String GROUP_NAME_KEY="groupName";

	public static final Integer EXECUTE_STATUS_NO_START = 1;
	public static final Integer EXECUTE_STATUS_DOING = 2;
	public static final Integer EXECUTE_STATUS_END = 3;
	// allocation execute result
	public static final Integer EXECUTE_RESULT_SUCCESS = 0;
	public static final Integer EXECUTE_RESULT_FAIL = 1;

	//是否为Sensor类型
	public static final String  isSensorType = "sensor_type.type";
	//功能类型
	public static final String functionType = "sensor_function.type";
	
	//模板类型
	public static final String SHORTCHT_SCENE ="scene";
	public static final String SHORTCHT_IFTTT="ifttt";
	public static final String SHORTCHT_LOCATIONSCENE="locationScene";
	public static final String SHORTCHT_ROOM_SCENE="room_scene";
	public static final String SHORTCHT_ROOM_IFTTT="room_ifttt";
	
	public final static String SCHEDULE_ROOM_SCENE = "room_scene";
	public final static String SCHEDULE_ROOM_IFTTT = "room_ifttt";
	public final static String SCHEDULE_IFTTT_TEMPLATE = "ifttt";
	public final static String SCHEDULE_SCENE_TEMPLATE = "scene";
	public final static String SCHEDULE_LOCATION = "locationScene";

	public static final Long[] OR = new Long[]{460L,2L,1542251540000L};
	public static final Long OR_LEFT = 460L;
	public static final Long OR_RIGHT = 2L;
	public static final Long OR_TOP = 1542251540000L;


	//设备上下线
	public static final String DEVICE_ONLINE ="connected";
	public static final String DEVICE_OFFLINE="disconnected";
	
	public final static String SCHEDULE_STATISTIC = "scheduleJob.statistic.corn"; //设备用途统计时间表达式
	
	//初始化配置
	public final static String INIT_SCENE = "SCENE";
	public final static String INIT_IFTTT = "IFTTT";
	public final static String INIT_GOURP = "GROUP";
	
	//单次和循环设置标识
	public final static String LOOP_TYPE_ONCE="0";//单次
	public final static String LOOP_TYPE_LOOP="1";//循环
}
