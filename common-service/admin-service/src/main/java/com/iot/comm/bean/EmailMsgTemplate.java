
package com.iot.comm.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 邮件消息模板
 */

public class EmailMsgTemplate implements Serializable {
    /**
     * 接受者邮件
     */
    private List<String> receiveEmails;

    private String title;
    /**
     * 组装后的模板内容JSON字符串
     */
    private String context;
    /**
     * 邮件通道  smtp.163.com
     */
    private String channel;

    public EmailMsgTemplate() {
    }

    public EmailMsgTemplate(List<String> receiveEmails, String title, String context, String channel) {
        this.receiveEmails = receiveEmails;

        this.title = title;
        this.context = context;
        this.channel = channel;
    }

    public List<String> getReceiveEmails() {
        return receiveEmails;
    }

    public void setReceiveEmails(List<String> receiveEmails) {
        this.receiveEmails = receiveEmails;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
