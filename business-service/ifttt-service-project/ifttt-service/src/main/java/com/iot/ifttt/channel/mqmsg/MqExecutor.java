package com.iot.ifttt.channel.mqmsg;

import com.alibaba.fastjson.JSON;
import com.iot.ifttt.channel.base.IExecutor;
import com.iot.ifttt.entity.AppletItem;
import com.iot.ifttt.vo.ActionMessage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 描述：MQ执行器
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/27 20:11
 */
@Service
public class MqExecutor implements IExecutor {

    private final Logger logger = LoggerFactory.getLogger(MqExecutor.class);

    @Autowired
    private MQSender mqSender;

    @Override
    public void execute(List<AppletItem> items) {
        if (CollectionUtils.isNotEmpty(items)) {
            //循环执行
            for (AppletItem vo : items) {
                sendMq(vo.getJson());
            }
        }
    }

    /**
     * 发送MQ消息
     *
     * @param json
     */
    private void sendMq(String json) {
        try {
            logger.info("MQ发送消息体：" + json);
            Map<Object, Object> maps = JSON.parseObject(json, Map.class);
            Long tenantId = Long.valueOf(maps.get("tenantId").toString());
            String msg = maps.get("msg").toString();
            String route = String.valueOf(maps.get("route"));

            //发送消息总线
            logger.info("执行MQ消息：tenantId=" + tenantId + ",msg=" + msg);

            ActionMessage action = new ActionMessage();
            action.setTenantId(tenantId);
            if (!"null".equals(route) && !StringUtils.isEmpty(route)) {
                action.setRoute(route);
            } else {
                action.setRoute("2C");
            }
            action.setMessage(msg);
            mqSender.send(action);
        } catch (Exception e) {
            logger.error("发送MQ消息失败", e);
        }
    }
}
