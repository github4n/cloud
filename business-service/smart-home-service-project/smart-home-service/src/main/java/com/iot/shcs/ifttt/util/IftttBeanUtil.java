package com.iot.shcs.ifttt.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.ifttt.common.IftttServiceEnum;
import com.iot.ifttt.vo.AppletItemVo;
import com.iot.ifttt.vo.AppletThatVo;
import com.iot.ifttt.vo.AppletThisVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述：ifttt转换工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/30 8:41
 */
public class IftttBeanUtil {

    /**
     * 获取that
     *
     * @param thenList
     * @param tenantId
     * @return
     */
    public static List<AppletThatVo> getAppletThatByMap(List<Map<String, Object>> thenList, Long tenantId) {
        List<AppletThatVo> appletThatVos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(thenList)) {
            AppletThatVo appletThatVo = new AppletThatVo();
            appletThatVo.setServiceCode(IftttServiceEnum.MQ.getCode());
            appletThatVo.setCode("code");

            List<AppletItemVo> items = Lists.newArrayList();
            for (Map<String, Object> vo : thenList) {
                AppletItemVo item = new AppletItemVo();
                /*{
                    tenantId:
                    msg:{}
                }*/
                String json = "{\"route\":\"2C\",\"tenantId\":" + tenantId + ",\"msg\":" + JSON.toJSONString(vo) + "}";
                item.setJson(json);
                items.add(item);
            }
            appletThatVo.setItems(items);
            appletThatVos.add(appletThatVo);
        }
        return appletThatVos;
    }

    /**
     * 获取设备子项
     *
     * @param properties
     * @return
     */
    public static AppletItemVo getDevItem(String properties) {
        AppletItemVo appletItemVo = new AppletItemVo();
        //{"devId":"97003f06bee103e5fb694e01ee0f6312","trigType":"dev","idx":0,"attr":"occupancy ","value":"1","compOp":"== "}
        Map<String, Object> map = JSON.parseObject(properties, Map.class);
        String attr = String.valueOf(map.get("attr"));
        String event = String.valueOf(map.get("event"));
        String compOp = String.valueOf(map.get("compOp"));
        String value = String.valueOf(map.get("value"));
        String devId = String.valueOf(map.get("devId"));
        String json;
        if(map.get("event")!=null){
            //事件
            json = "{\"devId\":\"" + devId + "\",\"event\": \"" + event + "\",\"value\": " +value + "}";  //json格式
        }else {
            //属性
            json = "{\"devId\":\"" + devId + "\",\"field\": \"" + attr + "\",\"sign\": \"" + compOp + "\",\"value\":\"" + value + "\",\"type\":1,\"recheck\":\"1\"}";  //json格式
        }
        appletItemVo.setJson(json);
        return appletItemVo;
    }

    /**
     * 获取时间范围this
     *
     * @param timing
     * @return
     */
    public static AppletThisVo getTimeRangeThis(String timing, String area) {
        AppletThisVo appletThisVo = new AppletThisVo();
        appletThisVo.setLogic("or");
        appletThisVo.setServiceCode(IftttServiceEnum.TIME_RANGE.getCode());

        //timeRange item
        List<AppletItemVo> appletItemVos = Lists.newArrayList();
        AppletItemVo appletItemVo = new AppletItemVo();
        //{"at":"09:30","repeat":[0,1,2,3,4,5,6],"trigType":"timer","idx":0}
        Map<String, Object> map = JSON.parseObject(timing, Map.class);
        String begin = String.valueOf(map.get("begin"));
        String end = String.valueOf(map.get("end"));
        String repeat = String.valueOf(map.get("week"));
        String json = "{\"begin\":\"" + begin + "\",\"end\":\"" + end + "\",\"repeat\":" + repeat + ",\"area\":\"" + area + "\"}";
        appletItemVo.setJson(json);
        appletItemVos.add(appletItemVo);
        appletThisVo.setItems(appletItemVos);
        return appletThisVo;
    }

    /**
     * 获取定时this
     *
     * @param properties
     * @return
     */
    public static AppletThisVo getTimerThis(String properties, String area) {
        AppletThisVo appletThisVo = new AppletThisVo();
        appletThisVo.setLogic("or");
        appletThisVo.setServiceCode(IftttServiceEnum.TIMER.getCode());

        //1 timer-item
        List<AppletItemVo> appletItemVos = Lists.newArrayList();
        AppletItemVo appletItemVo = new AppletItemVo();
        //{"at":"09:30","repeat":[0,1,2,3,4,5,6],"trigType":"timer","idx":0}
        Map maps = (Map) JSON.parse(properties);
        String at = (String) maps.get("at");
        String repeat = maps.get("repeat").toString();
        String json = "{\"at\":\"" + at + "\",\"repeat\":" + repeat + ",\"area\":\"" + area + "\"}";
        appletItemVo.setJson(json);
        appletItemVos.add(appletItemVo);
        appletThisVo.setItems(appletItemVos);
        return appletThisVo;
    }

    /**
     * 获取Astro this
     *
     * @param properties
     * @return
     */
    public static AppletThisVo getAstroThis(String properties, String area) {
        AppletThisVo appletThisVo = new AppletThisVo();
        appletThisVo.setLogic("or");
        appletThisVo.setServiceCode(IftttServiceEnum.ASTRONOMICAL.getCode());

        //1 timer-item
        List<AppletItemVo> appletItemVos = Lists.newArrayList();
        AppletItemVo appletItemVo = new AppletItemVo();
        //{
        //	"idx": 2,
        //	"trigType": "sunset",
        //	"longitude": "113.211",
        //	"latitude": "40.14924",
        //	"timeZone": "GMT+8",
        //	"intervalType": 0,
        //	"intervalTime": "3600",
        //	"repeat": [0, 1, 2, 3, 4, 5, 6]
        //}
        Map maps = (Map) JSON.parse(properties);
        String trigType = (String) maps.get("trigType");
        String longitude = maps.get("longitude").toString();
        String latitude = maps.get("latitude").toString();
        String timeZone = maps.get("timeZone").toString();
        String intervalType = maps.get("intervalType").toString();
        String intervalTime = maps.get("intervalTime").toString();
        String repeat = maps.get("repeat").toString();

        String json = "{\"trigType\":\"" + trigType
                + "\",\"longitude\":\"" + longitude
                + "\",\"latitude\":\"" + latitude
                + "\",\"timeZone\":\"" + timeZone
                + "\",\"intervalType\":" + intervalType
                + ",\"intervalTime\":\"" + intervalTime
                + "\",\"repeat\":" + repeat
                + ",\"area\":\"" + area + "\"}";

        appletItemVo.setJson(json);
        appletItemVos.add(appletItemVo);
        appletThisVo.setItems(appletItemVos);
        return appletThisVo;
    }


    /**
     * 获取时间范围
     *
     * @param appletThisVo
     * @return
     */
    public static String getTimeRangeJson(AppletThisVo appletThisVo) {
        List<AppletItemVo> itemVos = appletThisVo.getItems();
        AppletItemVo itemVo = itemVos.get(0);
        String json = itemVo.getJson();
        Map<String, Object> maps = JSON.parseObject(json, Map.class);
        String begin = maps.get("begin").toString();
        String end = maps.get("end").toString();
        String repeat = maps.get("repeat").toString();

        //week repeat
        String valid = "{\"end\":\"" + end + "\",\"begin\":\"" + begin + "\",\"week\":" + repeat + "}";

        return valid;
    }

    /**
     * 获取设备trigger map
     *
     * @param appletThisVo
     * @return
     */
    public static List<Map<String, Object>> getDevTriggerList(AppletThisVo appletThisVo) {
        List<Map<String, Object>> triggerList = new ArrayList<>();
        List<AppletItemVo> itemVos = appletThisVo.getItems();
        int index = 0;
        if (CollectionUtils.isNotEmpty(itemVos)) {
            for (AppletItemVo vo : itemVos) {
                //{"devId":"8c0fb8d50a9bb4040db40c8b8be7deb6","trigType":"dev","attr":"Door","idx":1,"value":"1","compOp":"=="}
                String json = vo.getJson();
                Map<String, Object> maps = JSON.parseObject(json, Map.class);
                String devId = maps.get("devId").toString();
                String field = maps.get("field").toString();
                String sign = maps.get("sign").toString();
                String value = maps.get("value").toString();
                String properties = "{\"devId\":\"" + devId + "\",\"trigType\":\"dev\",\"attr\":\"" + field + "\",\"idx\":" + index + ",\"value\":\"" + value + "\",\"compOp\":\"" + sign + "\"}";
                Map<String, Object> triggerMap = JSON.parseObject(properties, Map.class);
                triggerList.add(triggerMap);
                index++;
            }
        }
        return triggerList;
    }

    /**
     * 获取定时map
     *
     * @param thisVos
     * @return
     */
    public static Map<String, Object> getTimerTriggerMap(List<AppletThisVo> thisVos) {
        AppletThisVo thisVo = thisVos.get(0);
        List<AppletItemVo> itemVos = thisVo.getItems();
        AppletItemVo itemVo = itemVos.get(0);
        String json = itemVo.getJson();
        //{"at":"11:10","repeat":[1,2,3,4,5],"trigType":"timer","idx":0}
        Map<String, Object> maps = JSON.parseObject(json, Map.class);
        String at = maps.get("at").toString();
        String repeat = maps.get("repeat").toString();
        String properties = "{\"at\":\"" + at + "\",\"repeat\":" + repeat + ",\"trigType\":\"timer\",\"idx\":0}";
        return JSON.parseObject(properties, Map.class);
    }

    /**
     * 获取设备事件map
     *
     * @param thisVos
     * @return
     */
    public static List<Map<String, Object>> getDevEventTriggerMap(List<AppletThisVo> thisVos) {
        List<Map<String, Object>> triggerList = new ArrayList<>();
        AppletThisVo thisVo = thisVos.get(0);
        List<AppletItemVo> itemVos = thisVo.getItems();
        int index = 0;
        if (CollectionUtils.isNotEmpty(itemVos)) {
            for (AppletItemVo vo : itemVos) {
                //{"devId":"8c0fb8d50a9bb4040db40c8b8be7deb6","trigType":"dev","event":"button_pressed","idx":1,"value":"1","compOp":"=="}
                String json = vo.getJson();
                Map<String, Object> maps = JSON.parseObject(json, Map.class);
                String devId = maps.get("devId").toString();
                String event = maps.get("event").toString();
                String value = maps.get("value").toString();
                String properties = "{\"devId\":\"" + devId + "\",\"trigType\":\"dev\",\"event\":\"" + event + "\",\"idx\":" + index + ",\"value\":" + value + ",\"compOp\":\"==\"}";
                Map<String, Object> triggerMap = JSON.parseObject(properties, Map.class);
                triggerList.add(triggerMap);
                index++;
            }
        }
        return triggerList;
    }

    /**
     * 获取天文定时map
     *
     * @param thisVos
     * @return
     */
    public static Map<String, Object> getAstroTriggerMap(List<AppletThisVo> thisVos) {
        AppletThisVo thisVo = thisVos.get(0);
        List<AppletItemVo> itemVos = thisVo.getItems();
        if (CollectionUtils.isNotEmpty(itemVos)) {
            AppletItemVo itemVo = itemVos.get(0);
            String json = itemVo.getJson();
            Map<String, Object> maps = JSON.parseObject(json, Map.class);
            maps.put("idx", 0);
            return maps;
        } else {
            return Maps.newHashMap();
        }
    }


}
