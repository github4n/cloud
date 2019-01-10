package com.iot.mqttploy.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AclsInfo implements Serializable {

    /**
     * 序列
     */
    private static final long serialVersionUID = 403630406019480987L;

    /**
     * MQTT登录密码
     */
    private String password;

    /**
     * 发布信息
     */
    private Set<String> pubAcls;

    /**
     * 订阅信息
     */
    private Set<String> subAcls;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getPubAcls() {
        return pubAcls;
    }

    public void setPubAcls(Set<String> pubAcls) {
        this.pubAcls = pubAcls;
    }

    public Set<String> getSubAcls() {
        return subAcls;
    }

    public void setSubAcls(Set<String> subAcls) {
        this.subAcls = subAcls;
    }

}
