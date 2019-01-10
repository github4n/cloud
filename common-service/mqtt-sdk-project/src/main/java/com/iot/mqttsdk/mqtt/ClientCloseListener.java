package com.iot.mqttsdk.mqtt;

public interface ClientCloseListener {

	void closeNotify(String clientId);
}
