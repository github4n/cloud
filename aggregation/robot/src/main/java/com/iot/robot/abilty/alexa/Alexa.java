package com.iot.robot.abilty.alexa;


public class Alexa {
	private String type = "AlexaInterface";
	private String interfaces = Alexa.class.getSimpleName();
	private String version = "3";
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getInterface() {
		return interfaces;
	}
	public void setInterface(String interfaces) {
		this.interfaces = Alexa.class.getSimpleName()+"."+interfaces;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
