package com.iot.robot.norm;

// robot的控制属性
public abstract class KeyValue<T> {

    //闪烁指令
	public static final String BLINK = "Blink";
	//整数型
	public static final String CCT = "CCT";
	public static final String CONTROL = "Control";
	//百分比
	public static final String DIMMING = "Dimming";
	public static final String LIFT = "Lift";

	//0关闭 1开启
	public static final String ONOFF = "OnOff";
	// IPC设备的 开关属性( 0关闭 1开启)
	public static final String TURN_ON_OFF = "TurnOnOff";

	//false离线 true在线
	public static final String ONLINE = "online";
    //整数型rgb
	public static final String RGBW = "RGBW";
	//代表情景
	public static final String SCENE = "scene";
	//查询指令
	public static final String QUERY = "query";
	//报警控制
	public static final String WARNING = "warning";
	//灯管闪烁控制
	public static final String STROBE = "strobe";
	//色温增量默认梯度
	public static final String GRAD_COLOR_TEMPERATURE = "500";
	//是否增量  例如：调亮，并没有具体的亮度值，要根据当前设备的亮度增加一点亮度
	public boolean isDelat = false;
	
	public abstract T getFixedValue();

	public abstract void setFixedValue(T fixedValue);

	public abstract T getDeltaValue();

	public abstract void setDeltaValue(T deltaValue);

	public abstract String getKey();

	public boolean isDelat() {
		return isDelat;
	}

	public void setDelat(boolean delat) {
		isDelat = delat;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println(obj.toString());
		return getKey().equals(obj.toString());
	}
}
