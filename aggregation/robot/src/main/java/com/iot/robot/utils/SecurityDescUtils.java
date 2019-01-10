package com.iot.robot.utils;

import java.util.HashMap;
import java.util.Map;

public class SecurityDescUtils {
	
	private static final long DAY = 24 * 60 * 60 * 1000;
	private static final long HOUR = 60 * 60 * 1000;
	private static final long MINUTE = 60 * 1000;
	private static final Map<String, String> MODEDESC = new HashMap<>();
	static {
		MODEDESC.put("away", "enable the away mode ");
		MODEDESC.put("stay", "enable the stay mode ");
		MODEDESC.put("off", "enable the off mode ");
		MODEDESC.put("panic", "enable the sos mode ");
	}

	public static String toDateString(long millions, String d, String h, String m) {
		StringBuilder sb = new StringBuilder();
		int day = (int) (millions/DAY);
		int hour = (int) ((millions%DAY)/HOUR);
	    int minute = (int) (((millions%DAY)%HOUR)/MINUTE);
		if (day != 0) {
			sb.append(day);
			sb.append(" ");
			sb.append(d);
			if (hour != 0 || minute != 0)
			  sb.append(",");
		}
	    if (hour != 0) {
			sb.append(hour);
			sb.append(" ");
			sb.append(h);
			if (minute != 0)
			  sb.append(" and ");
		}
	    if (minute != 0) {
			sb.append(minute);
			sb.append(" ");
			sb.append(m);
		}
	    if (sb.length() == 0) {
	    	return "just";
	    }
	    if (day > 365) {
	    	return "A year ago";
	    }
	    sb.append(" ago");
		return sb.toString();
	}
	public static String getModeDesc(String mode) {
		return MODEDESC.get(mode);
	}
}
