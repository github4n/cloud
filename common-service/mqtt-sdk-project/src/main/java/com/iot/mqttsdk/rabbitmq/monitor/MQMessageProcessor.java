package com.iot.mqttsdk.rabbitmq.monitor;


import com.alibaba.fastjson.JSON;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.DeadBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.rabbitmq.RabbitMqClient;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：服务接口调用
 * 创建人： 490485964@qq.com
 * 创建时间：2018年03月20日 10:52
 * 修改人： 490485964@qq.com
 * 修改时间：2018年03月20日 10:52
 */

public class MQMessageProcessor implements Processor {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(MQMessageProcessor.class);

    /**正常消息回调*/
    private CallBackProcessor callBackProcessor;

    /**死信消息回调*/
    private DeadBackProcessor deadBackProcessor;

    public MQMessageProcessor(CallBackProcessor callBackProcessor, DeadBackProcessor deadBackProcessor) {
        this.callBackProcessor = callBackProcessor;
        this.deadBackProcessor = deadBackProcessor;
    }

    @SuppressWarnings("unchecked")
    public void process(Exchange exchange) throws Exception {
        String content = exchange.getIn().getBody(String.class);
        logger.info("content->" + content);
        try {
            if (StringUtils.isEmpty(content)) {
                return;
            }
            if(this.callBackProcessor == null && this.deadBackProcessor == null){
                logger.info("processor is null");
                return;
            }
            long startTime = System.currentTimeMillis();
            if(!RabbitMqClient.DEAD_QUEUES.contains(exchange.getFromRouteId())){
                MqttMsg mqttMsg = this.parseObject(content);
                this.callBackProcessor.onMessage(mqttMsg);
                logger.info("topic:{}, method: {} cost: {} ms", mqttMsg.getTopic(), mqttMsg.getMethod(), (System.currentTimeMillis() - startTime));
            }else{
                this.deadBackProcessor.onMessage(content);
                logger.info("deadMessage:{}, cost: {} ms", content, (System.currentTimeMillis() - startTime));
            }
        } catch (Exception e) {
            logger.error("error->", e);
        }
    }

    /** 
     * 描述：解析json(解析失败时将content放入payload)
     * @author maochengyuan
     * @created 2018/11/19 17:14
     * @param content 消息内容
     * @return com.iot.mqttsdk.common.MqttMsg
     */
    private MqttMsg parseObject(String content){
        try {
            return JSON.parseObject(content, MqttMsg.class);
        } catch (Exception e) {
            logger.error("JSON parse error->", e);
            return new MqttMsg(content);
        }
    }

}
