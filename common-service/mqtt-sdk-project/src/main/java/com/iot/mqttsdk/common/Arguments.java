package com.iot.mqttsdk.common;

/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：队列其他参数
 * 创建人： mao2080@sina.com
 * 创建时间：2018/11/15 19:58
 * 修改人： mao2080@sina.com
 * 修改时间：2018/11/15 19:58
 * 修改描述：
 */
public class Arguments {

    /**死信队列名称*/
    private String deadQueueName;

    /**死信路由*/
    private String deadLetterRoutingKey;

    /**死信TTL*/
    private Long messageTtl;

    /**死信队列开关*/
    private boolean deadSwitchOn = false;

    /**当前队列是否自动消费*/
    private boolean normAutoConsume = true;

    /**死信队列是否自动消费*/
    private boolean deadAutoConsume = true;

    /**正常消息回调*/
    private CallBackProcessor callBackProcessor;

    /**死信消息回调*/
    private DeadBackProcessor deadBackProcessor;

    public Arguments() {

    }

    public Arguments(CallBackProcessor callBackProcessor) {
        this.callBackProcessor = callBackProcessor;
    }

    public Arguments(boolean deadSwitchOn) {
        this.deadSwitchOn = deadSwitchOn;
    }

    public Arguments(String deadQueueName, boolean normAutoConsume) {
        this.deadQueueName = deadQueueName;
        this.normAutoConsume = normAutoConsume;
    }

    public Arguments(String deadQueueName, boolean normAutoConsume, boolean deadAutoConsume) {
        this.deadQueueName = deadQueueName;
        this.normAutoConsume = normAutoConsume;
        this.deadAutoConsume = deadAutoConsume;
    }

    public Arguments(String deadLetterRoutingKey, Long messageTtl) {
        this.deadLetterRoutingKey = deadLetterRoutingKey;
        this.messageTtl = messageTtl;
    }

    public Arguments(String deadLetterRoutingKey, Long messageTtl, boolean deadSwitchOn) {
        this.deadLetterRoutingKey = deadLetterRoutingKey;
        this.messageTtl = messageTtl;
        this.deadSwitchOn = deadSwitchOn;
    }

    public Arguments(String deadQueueName, String deadLetterRoutingKey, Long messageTtl, boolean deadSwitchOn) {
        this.deadQueueName = deadQueueName;
        this.deadLetterRoutingKey = deadLetterRoutingKey;
        this.messageTtl = messageTtl;
        this.deadSwitchOn = deadSwitchOn;
    }

    public Arguments(String deadQueueName, String deadLetterRoutingKey, Long messageTtl, boolean deadSwitchOn, boolean normAutoConsume, boolean deadAutoConsume, DeadBackProcessor deadBackProcessor) {
        this.deadQueueName = deadQueueName;
        this.deadLetterRoutingKey = deadLetterRoutingKey;
        this.messageTtl = messageTtl;
        this.deadSwitchOn = deadSwitchOn;
        this.normAutoConsume = normAutoConsume;
        this.deadAutoConsume = deadAutoConsume;
        this.deadBackProcessor = deadBackProcessor;
    }

    public String getDeadQueueName() {
        return deadQueueName;
    }

    public void setDeadQueueName(String deadQueueName) {
        this.deadQueueName = deadQueueName;
    }

    public String getDeadLetterRoutingKey() {
        return deadLetterRoutingKey;
    }

    public void setDeadLetterRoutingKey(String deadLetterRoutingKey) {
        this.deadLetterRoutingKey = deadLetterRoutingKey;
    }

    public Long getMessageTtl() {
        return messageTtl;
    }

    public void setMessageTtl(Long messageTtl) {
        this.messageTtl = messageTtl;
    }

    public boolean isDeadSwitchOn() {
        return deadSwitchOn;
    }

    public void setDeadSwitchOn(boolean deadSwitchOn) {
        this.deadSwitchOn = deadSwitchOn;
    }

    public boolean isNormAutoConsume() {
        return normAutoConsume;
    }

    public void setNormAutoConsume(boolean normAutoConsume) {
        this.normAutoConsume = normAutoConsume;
    }

    public boolean isDeadAutoConsume() {
        return deadAutoConsume;
    }

    public void setDeadAutoConsume(boolean deadAutoConsume) {
        this.deadAutoConsume = deadAutoConsume;
    }

    public CallBackProcessor getCallBackProcessor() {
        return callBackProcessor;
    }

    public void setCallBackProcessor(CallBackProcessor callBackProcessor) {
        this.callBackProcessor = callBackProcessor;
    }

    public DeadBackProcessor getDeadBackProcessor() {
        return deadBackProcessor;
    }

    public void setDeadBackProcessor(DeadBackProcessor deadBackProcessor) {
        this.deadBackProcessor = deadBackProcessor;
    }

}
