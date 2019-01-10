package com.iot.smarthome.vo;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Descrpiton:
 *      设备分类支持的 属性 返回的数据
 *
 * @Author: yuChangXing
 * @Date: 2018/12/14 16:11
 * @Modify by:
 */

@Deprecated
public class DeviceTypeAttributeResp extends Resp implements Serializable {
    private static final long serialVersionUID = 2025580783894328456L;

    private List<Map<String, Object>> attrList;

    public DeviceTypeAttributeResp() {
        attrList = Lists.newArrayList();
        payload.put("attr", getAttrList());
    }

    public List<Map<String, Object>> getAttrList() {
        return attrList;
    }

    public void addAttr(Map<String, Object> attr) {
        this.attrList.add(attr);
    }

    @Override
    public Map<String, Object> getPayload() {
        return payload;
    }

    @Override
    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    @Override
    public Map<String, Object> buildMsg() {
       /* JSONObject jsonObject = new JSONObject();
        jsonObject.put("payload", getPayload());
        return jsonObject;*/

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payload", getPayload());
        return resultMap;
    }

    public static void main(String[] args) {
        DeviceTypeAttributeResp deviceTypeAttributeResp = new DeviceTypeAttributeResp();

        Map<String, Object> attr1 = Maps.newHashMap();
        attr1.put("name", "OnOff");
        attr1.put("unit", "true|false");
        attr1.put("type", "Boolean");
        attr1.put("rw", 1);
        attr1.put("min", 2700);
        attr1.put("max", 6500);

        Map<String, Object> attr2 = Maps.newHashMap();
        attr2.put("name", "OnOff");
        attr2.put("unit", "true|false");
        attr2.put("type", "Boolean");
        attr2.put("rw", 1);
        attr2.put("min", 2700);
        attr2.put("max", 6500);

        deviceTypeAttributeResp.addAttr(attr1);
        deviceTypeAttributeResp.addAttr(attr2);

        Map<String, Object> resultMap = deviceTypeAttributeResp.buildMsg();
        System.out.println(resultMap);
    }
}
