package robot;

public interface KeyValue<T> {

	public static final String BLINK = "Blink";
	//整数型
	public static final String CCT = "CCT";
	public static final String CONTROL = "Control";
	//百分比
	public static final String DIMMING = "Dimming";
	public static final String LIFT = "Lift";
	//0关闭 1开启
	public static final String ONOFF = "OnOff";
    //整数型rgb
	public static final String RGBW = "RGBW";
	//梯度
	public static final String GRAD_COLOR_TEMPERATURE = "500";
	public T getFixedValue();

	public void setFixedValue(T fixedValue);

	public T getDeltaValue();

	public void setDeltaValue(T deltaValue);

	String getKey();
}
