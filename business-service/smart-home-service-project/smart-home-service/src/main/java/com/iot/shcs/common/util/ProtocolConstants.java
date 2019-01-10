package com.iot.shcs.common.util;

public class ProtocolConstants {
	
	public final static String PREFIX="#{";//占位符前缀
	public final static String SUFFIX="}";//占位符后缀
	public final static String REGULAR="\\#\\{(.*?)\\}";//正则规则
	public static String protocol_type="protocol.vendor.type";//配置协议类型
	public static String protocol_body="protocol.vendor.method.send.body";//配置协议内容
	public static String protocol_dataformat="protocol.vendor.dataformat";//配置协议内容
	public static String protocol_encoded="protocol.vendor.encoding";//配置协议内容
	public static final int QOS=1;
	public static final String JSON_FORMAT="json";//数据格式json
	public static final String STRING_FORMAT="String";//数据格式标识
	public static final String MQTT_PROTOCOL="mqtt";//mqtt协议标志
	public static final String TCP_PROTOCOL="tcp";//tcp协议标志
	
	//占位符关键字
	public static final String DEVICE_ID="deviceId";//设备id
	public static final String DEVICE_NAME="deviceName";//设备名称
	public static final String GATEWAY_ID="gatewayId";//网关Id
	public static final String IP="ip";//设备ip
	public static final String IS_DIRECT_DEVICE="isDirectDevice";//是否直连设备标志
	public static final String DEVICE_TYPE_ID="deviceTypeId";//设备类型标志
	public static final String PRODUCT_ID="productId";//产品ID
	public static final String SPACE_NAME="spaceName";//空间名称
	public static final String UUID="uuid";//用户UUID
	public static final String DEVICE_PASSWORD="devicePassword";//设备密码
	public static final String ATTR="attr";//前端用户控制属性

	public static final String CONTROL_METHOD="control";//控制方法
	public static final String QUERY_DEVICE_STATUS_METHOD="queryDeviceStatus";//查询设备状态方法
}
