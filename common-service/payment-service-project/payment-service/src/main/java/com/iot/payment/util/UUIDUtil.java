package com.iot.payment.util;

import java.util.UUID;

public class UUIDUtil {
	
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
