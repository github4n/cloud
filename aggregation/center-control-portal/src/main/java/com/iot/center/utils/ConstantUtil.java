package com.iot.center.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iot.building.allocation.api.EnvironmentPropertyApi;
import com.iot.center.vo.IndexVo;
import com.iot.saas.SaaSContextHolder;

@Component
public class ConstantUtil {

	public static List<IndexVo> indexPage=new ArrayList<>();
	
	public final static String CENTER_CONTROL_TENANTID = "center-control.tenantId";
	public final static String CENTER_CONTROL_UUID = "agent-mqtt.username";
	public final static String CENTER_CONTROL_PWD = "agent-mqtt.password";
	public final static String MQTT_HOST = "mqtt.host";
	public final static String AGENT_MQTT_HOST = "agent-mqtt.host";
	
	public final static String WX_APPID = "wx.appId";
	public final static String WX_APPSECRET = "wx.appSecret";
	
	@Autowired
	private EnvironmentPropertyApi environment;
	
	public Long getTenantId(){
		Long tenantId = getPropertyValue(CENTER_CONTROL_TENANTID)!=null?Long.parseLong(getPropertyValue(CENTER_CONTROL_TENANTID).toString()):0l;
		return tenantId;
	}
	
	public String getAdminUuid(){
		String adminUuid = getPropertyValue(CENTER_CONTROL_UUID)!=null?getPropertyValue(CENTER_CONTROL_UUID).toString():"";
		return adminUuid;
	}

	public String getMqttHost(){
		String adminUuid = getPropertyValue(AGENT_MQTT_HOST)!=null?getPropertyValue(AGENT_MQTT_HOST).toString():"";
		return adminUuid;
	}
	
	public String getAdminPwd(){
		String adminPwd = getPropertyValue(CENTER_CONTROL_PWD)!=null?getPropertyValue(CENTER_CONTROL_PWD).toString():"";
		return adminPwd;
	}
	
	public Object getPropertyValue(String key){
		Object value = environment.getPropertyValue(key);
		return value;
	}
	
	public void setTenantId2Holder(Long tenantId){
		SaaSContextHolder.setCurrentTenantId(tenantId);
	}
}
