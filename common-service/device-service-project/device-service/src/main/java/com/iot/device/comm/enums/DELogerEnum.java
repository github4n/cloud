package com.iot.device.comm.enums;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：设备模块
 * 功能描述：日志枚举
 * 创建人： mao2080@sina.com
 * 创建时间：2017年4月12日 下午5:17:19
 * 修改人： mao2080@sina.com
 * 修改时间：2017年4月12日 下午5:17:19
 */
public enum DELogerEnum {

	DeviceState("DeviceState", "DeviceStateLogger", "devicestate_test"),

	;

	/**日志主键 */
	private String logKey;

	/**日志名称 */
	private String logName;

	/**日志流或主题 */
	private String logStream;

	/**
	 * 
	 * 描述：构建类型
	 * @author mao2080@sina.com
	 * @created 2017年4月10日 下午3:42:57
	 * @since 
	 * @param logKey 日志主键
	 * @param logName 日志名称
	 * @param logStream 日志流或主题
	 * @return
	 */
	private DELogerEnum(String logKey, String logName, String logStream) {
		this.logKey = logKey;
		this.logName = logName;
		this.logStream = logStream;
	}

	public String getLogKey() {
		return logKey;
	}

	public String getLogName() {
		return logName;
	}

	public String getLogStream() {
		return logStream;
	}

}
