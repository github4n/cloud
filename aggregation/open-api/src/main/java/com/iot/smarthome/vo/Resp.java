package com.iot.smarthome.vo;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/14 15:14
 * @Modify by:
 */
public abstract class Resp {
    protected Map<String, Object> payload = Maps.newHashMap();

    public abstract Map<String, Object> getPayload();

    public abstract void setPayload(Map<String, Object> payload);

    public abstract Map<String, Object> buildMsg();
}
