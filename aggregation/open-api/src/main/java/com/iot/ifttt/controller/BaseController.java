package com.iot.ifttt.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.ifttt.vo.CommonReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 描述：基础接口
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/19 10:20
 */
@Slf4j
public class BaseController {

    /**
     * 验证服务秘钥
     */
    protected boolean checkKey(String iftttKey) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String key = request.getHeader("IFTTT-Service-Key");
        boolean flag = false;

        if (!StringUtils.isEmpty(iftttKey) && key != null && key.equals(iftttKey)) {
            flag = true;
        } else {
            log.debug("秘钥为空，验证失败！key=" + iftttKey);
        }

        if (!flag) {
            setResponseStatus(401);
        }

        return flag;
    }

    /**
     * 应答错误信息
     *
     * @param msg
     * @return
     */
    protected String errorMsg(String msg) {
        String res = "{\n" +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"message\": \"" + msg + "\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return res;
    }

    /**
     * 设置应答状态
     *
     * @param code
     */
    protected void setResponseStatus(int code) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setStatus(code);
    }

    /**
     * 返回设备列表
     *
     * @param deviceMap
     * @return
     */
    protected String resList(Map<String, String> deviceMap) {
        if (deviceMap == null || deviceMap.size() == 0) {
            String json = "{\n" +
                    "  \"data\": [\n" +
                    "  ]\n" +
                    "}";
            return json;
        }

        List<Map<String, String>> dataList = Lists.newArrayList();
        for (Map.Entry<String, String> entry : deviceMap.entrySet()) {
            Map<String, String> item = Maps.newHashMap();
            item.put("label", entry.getValue());
            item.put("value", entry.getKey());
            dataList.add(item);
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("data", dataList);

        return JSON.toJSONString(dataMap);
    }

    /**
     * 校验参数方法
     *
     * @param req
     * @param field
     * @return
     */
    protected boolean checkField(CommonReq req, String... field) {
        Map<String, Object> triggerFields = req.getTriggerFields();
        boolean flag = true;
        if (triggerFields == null || triggerFields.size() == 0) {
            flag = false;
        } else {
            for (int i = 0; i < field.length; i++) {
                String key = field[i];
                String value = (String) triggerFields.get(key);
                if (value == null) {
                    flag = false;
                    break;
                }
            }
        }

        if (!flag) {
            setResponseStatus(400);
        }
        return flag;
    }

    /**
     * 返回事件项
     *
     * @param rc    0 校验成功 1 参数错误 2 条件未触发
     * @param limit
     * @return
     */
    protected String resItem(int rc, Integer limit) {
        String items = "";
        long time = System.currentTimeMillis() / 1000;
        if (rc == 1) {
            log.debug("请求参数错误！");
            setResponseStatus(400);
            String res = "{\n" +
                    "  \"errors\": [\n" +
                    "    {\n" +
                    "      \"message\": \"fields error\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            return res;
        } else if (rc == 0) {
            log.debug("达到触发条件！");
            items = "    {\n" +
                    "      \"created_at\": \"2018-12-19T09:23:00-07:00\"," +
                    "      \"meta\": {\n" +
                    "        \"id\": \"" + time + "\",\n" +
                    "        \"timestamp\": " + time + "\n" +
                    "      }\n" +
                    "    }";
        } else {
            log.debug("未达到触发条件！");
        }

        if (limit == null) {
            limit = 100;
        }

        if (limit == 0) {
            items = "";
        } else if (limit == 1) {

        } else {
            if (StringUtils.isEmpty(items)) {
                items = "    {\n" +
                        "      \"created_at\": \"2013-11-04T09:23:00-07:00\"," +
                        "      \"meta\": {\n" +
                        "        \"id\": \"14b9-1fd2-acaa-5df6\",\n" +
                        "        \"timestamp\": 1383597267\n" +
                        "      }\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"created_at\": \"2013-11-04T09:23:00-07:00\"," +
                        "      \"meta\": {\n" +
                        "        \"id\": \"14b9-1fd2-acaa-5df8\",\n" +
                        "        \"timestamp\": 1383597263\n" +
                        "      }\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"created_at\": \"2013-11-04T09:23:00-07:00\"," +
                        "      \"meta\": {\n" +
                        "        \"id\": \"ffb27-a63e-18e0-18ad\",\n" +
                        "        \"timestamp\": 1383596355\n" +
                        "      }\n" +
                        "    }\n";
            }
        }

        //响应
        String res = "{\n" +
                "  \"data\": [\n" +
                items +
                "  ]\n" +
                "}";
        log.debug("触发响应内容：" + res);
        return res;
    }

    /**
     * action应答消息
     *
     * @return
     */
    protected String resAction() {
        String res = "{\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"id\": \"" + System.currentTimeMillis() + "\",\n" +
                "      \"url\": \"http://www.baidu.com\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return res;
    }

}
