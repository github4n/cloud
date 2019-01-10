package com.iot.ifttt.util;


import com.google.common.collect.Maps;
import com.iot.ifttt.vo.CommonReq;

import java.util.Map;

/**
 * 描述：API测试数据工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/22 20:48
 */
public class TestDataUtil {

    public static String devStatus(CommonReq req) {
        Integer limit = req.getLimit();
        String items;
        if (limit == null) {
            limit = 100;
        }
        if (limit == 0) {
            items = "";
        } else if (limit == 1) {
            items = "    {\n" +
                    "      \"created_at\": \"2013-11-04T09:23:00-07:00\","+
                    "      \"meta\": {\n" +
                    "        \"id\": \"14b9-1fd2-acaa-5df5\",\n" +
                    "        \"timestamp\": 1383597267\n" +
                    "      }\n" +
                    "    }";
        } else {
            items = "    {\n" +
                    "      \"created_at\": \"2013-11-04T09:23:00-07:00\","+
                    "      \"meta\": {\n" +
                    "        \"id\": \"14b9-1fd2-acaa-5df5\",\n" +
                    "        \"timestamp\": 1383597267\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"created_at\": \"2013-11-04T09:23:00-07:00\","+
                    "      \"meta\": {\n" +
                    "        \"id\": \"14b9-1fd2-acaa-5df6\",\n" +
                    "        \"timestamp\": 1383597267\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"created_at\": \"2013-11-04T09:23:00-07:00\","+
                    "      \"meta\": {\n" +
                    "        \"id\": \"ffb27-a63e-18e0-18ad\",\n" +
                    "        \"timestamp\": 1383596355\n" +
                    "      }\n" +
                    "    }\n";

        }

        //响应
        String res = "{\n" +
                "  \"data\": [\n" +
                items +
                "  ]\n" +
                "}";
        return res;
    }

    public static String setup(){
        String res = "{\n" +
                "    \"data\": {\n" +
                "        \"accessToken\": \"-1\", \n" +
                "        \"samples\": {\n" +
                "            \"triggers\": {\n" +
                "                \"door_sensor\": {\n" +
                "                    \"objectId\": \"-1\", \n" +
                "                    \"status\": \"1\"\n" +
                "                }, \n" +
                "                \"execute_scene\": {\n" +
                "                    \"objectId\": \"-1\"\n" +
                "                }, \n" +
                "                \"security\": {\n" +
                "                    \"type\": \"-1\"\n" +
                "                }, \n" +
                "                \"motion_sensor\": {\n" +
                "                    \"objectId\": \"-1\", \n" +
                "                    \"status\": \"1\"\n" +
                "                }, \n" +
                "                \"temperature_sensor\": {\n" +
                "                    \"objectId\": \"-1\", \n" +
                "                    \"value1\": \"1\", \n" +
                "                    \"value2\": \"2\"\n" +
                "                }, \n" +
                "                \"humidity_sensor\": {\n" +
                "                    \"objectId\": \"-1\", \n" +
                "                    \"value1\": \"1\", \n" +
                "                    \"value2\": \"2\"\n" +
                "                }, \n" +
                "                \"water_sensor\": {\n" +
                "                    \"objectId\": \"-1\", \n" +
                "                    \"status\": \"1\"\n" +
                "                }\n" +
                "            }, \n" +
                "            \"actions\": {\n" +
                "                \"cct_lighting\": {\n" +
                "                    \"objectId\": \"1\", \n" +
                "                    \"Dimming\": \"2\", \n" +
                "                    \"CCT\": \"1\"\n" +
                "                }, \n" +
                "                \"dimmer_lighting\": {\n" +
                "                    \"objectId\": \"1\", \n" +
                "                    \"Dimming\": \"2\"\n" +
                "                }, \n" +
                "                \"rgbw_lighting\": {\n" +
                "                    \"objectId\": \"1\", \n" +
                "                    \"Dimming\": \"2\", \n" +
                "                    \"CCT\": \"2\", \n" +
                "                    \"RGBW\": \"1\"\n" +
                "                }\n" +
                "            }, \n" +
                "            \"actionRecordSkipping\": {\n" +
                "                \"cct_lighting\": {\n" +
                "                    \"objectId\": \"-1\", \n" +
                "                    \"Dimming\": \"2\", \n" +
                "                    \"CCT\": \"1\"\n" +
                "                }, \n" +
                "                \"dimmer_lighting\": {\n" +
                "                    \"objectId\": \"-1\", \n" +
                "                    \"Dimming\": \"2\"\n" +
                "                }, \n" +
                "                \"rgbw_lighting\": {\n" +
                "                    \"objectId\": \"-1\", \n" +
                "                    \"Dimming\": \"2\", \n" +
                "                    \"CCT\": \"2\", \n" +
                "                    \"RGBW\": \"1\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        return res;
    }

    public static String skipMsg(){
        String res = "{\n" +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"status\": \"SKIP\",\n" +
                "      \"message\": \"Audio file size too big\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return res;
    }

    public static Map<Long,String> getConfigMap(){
        Map<Long,String> map = Maps.newHashMap();
        map.put(1090211159l,"rgbw_lighting");
        map.put(1090210065l,"dimmer_lighting");
        map.put(1090211163l,"cct_lighting");
        map.put(1090211103l,"door_sensor");
        return map;
    }
}
