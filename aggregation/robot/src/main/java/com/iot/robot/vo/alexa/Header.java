package com.iot.robot.vo.alexa;

import java.util.UUID;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/9/7 17:07
 * @Modify by:
 */
public class Header {
    private String messageId = UUID.randomUUID().toString();
    private String namespace = null;
    private String name = null;
    private String payloadVersion = "3";

    private Header() {
    }

    public Header(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    public String getMessageId() {
        return messageId;
    }
    public String getNamespace() {
        return namespace;
    }
    public String getName() {
        return name;
    }
    public String getPayloadVersion() {
        return payloadVersion;
    }
}
