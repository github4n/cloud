package com.iot.cloud.util;

import com.iot.saas.SaaSContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConstantUtil {


    public final static String commonPath = "/api/v1";

    public final static Long tenantId = 11L;
    public final static Long locationId = 11L;


    public final static String CENTER_CONTROL_TENANTID = "center-control.tenantId";
    public final static String CENTER_CONTROL_UUID = "agent-mqtt.username";
    public final static String CENTER_CONTROL_PWD = "agent-mqtt.password";
    public final static String MQTT_HOST = "mqtt.host";

    public final static String WX_APPID = "wx.appId";
    public final static String WX_APPSECRET = "wx.appSecret";

    @Autowired
    private EnvironmentPropertyApi environment;

    public Long getTenantId() {
        Long tenantId = getPropertyValue(CENTER_CONTROL_TENANTID) != null ? Long.parseLong(getPropertyValue(CENTER_CONTROL_TENANTID).toString()) : 0l;
        return tenantId;
    }

    public String getAdminUuid() {
        String adminUuid = getPropertyValue(CENTER_CONTROL_UUID) != null ? getPropertyValue(CENTER_CONTROL_UUID).toString() : "";
        return adminUuid;
    }

    public String getMqttHost() {
        String adminUuid = getPropertyValue(MQTT_HOST) != null ? getPropertyValue(MQTT_HOST).toString() : "";
        return adminUuid;
    }

    public String getAdminPwd() {
        String adminPwd = getPropertyValue(CENTER_CONTROL_PWD) != null ? getPropertyValue(CENTER_CONTROL_PWD).toString() : "";
        return adminPwd;
    }

    public Object getPropertyValue(String key) {
        Object value = environment.getPropertyValue(key);
        return value;
    }

    public void setTenantId2Holder(Long tenantId) {
        SaaSContextHolder.setCurrentTenantId(tenantId);
    }
}
