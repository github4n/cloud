package com.iot.robot.vo.alexa;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/9/7 17:09
 * @Modify by:
 */
public class Endpoint {
    private Scope scope = new Scope();
    private String endpointId;
    public Scope getScope() {
        return scope;
    }
    public String getEndpointId() {
        return endpointId;
    }
    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }
}
