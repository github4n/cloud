package com.iot.device.comm.constants;


public class ModuleConstants {

	/**
	 * 缓存设备秘钥的key前缀
	 */
	public static final String KEY_DEVICEID_SECURITYKEY = "devId_secukey_";

	/**
	 * 缓存app秘钥的key前缀
	 */
	public static final String KEY_APPID_SECURITYKEY = "appId_secukey_";

	/**设备表名*/
	public static final String DEVICE_TABLE_NAME = "device";

	/**用户设备关系表*/
	public static final String USER_DEVICE_TABLE_NAME = "lds_user_device";

	/**设备数据端点*/
	public static final String END_POINT_TABLE_NAME = "lds_endpoint_";

	/**用户房间关系*/
	public static final String USER_ROOM_TABLE_NAME = "lds_user_room_";

	/**用户场景关系*/
	public static final String USER_SENCE_TABLE_NAME = "lds_user_sence_";

	/**用户联动规则关系*/
	public static final String USER_RULE_TABLE_NAME = "lds_user_rule_";

	/**用户信息表*/
	public static final String USER_INFO_TABLE_NAME = "lds_user_info_";

	/**用户表*/
	public static final String USER_TABLE_NAME = "lds_user_";

	public static final long TOKEN_DURATION = 15 * 60L;

	/**产品表*/
	public static final String PRODUCT_TABLE_NAME = "lds_product_";

	/**设备状态表*/
	public static final String DEVICE_STATUS_TABLE_NAME = "lds_device_state";

	/**Reids-Key*/
//	public static final String REDIS_KEY = ModuleTyp.Device.getCode();

	/**
	 * 缓存分享设备临时密码key前缀
	 */
	public static final String KEY_SHARE_TEM_PASS = "share_tem_pass_";

	/**表名*/
	public static final String TABLENAME = "tableName";

	/** 斜杠 */
	public static final String SPRIT = "/";

	/**缓存某一个计划已添加事件数 key前缀*/
	public static final String KEY_ADDED_EVENT_COUNT = "Added_Event_Count_";

	/** 横杠 */
	public static final String LINE_SPRIT = "_";

	/**ota计划启动相隔分钟数*/
	public static final long DIFFERENCE_MINUTE = 12*60L;

	/**ota计划启动任务执行秒数*/
	public static final long OTA_TASK_TIME = 60*60*24L;

	/**ota计划每次取的条数*/
	public static final int OTA_LIST_SIZE = 100;

	/**ota升级结果缓存时长*/
	public static final long OTA_UPGRADE_INFO_DETAIL_TIME = 24*3600;

}
