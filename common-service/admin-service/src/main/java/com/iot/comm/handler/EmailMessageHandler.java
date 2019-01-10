
package com.iot.comm.handler;


import com.iot.comm.bean.EmailMsgTemplate;

/**
 * 邮件服务处理
 */
public interface EmailMessageHandler {

    /**
     * 执行入口
     *
     * @param emailMsgTemplate 信息
     */
    void execute(EmailMsgTemplate emailMsgTemplate);

    /**
     * 数据校验
     *
     * @param emailMsgTemplate 消息
     */
    public void check(EmailMsgTemplate emailMsgTemplate);

    /**
     * 业务处理
     *
     * @param emailMsgTemplate 消息
     */
    public boolean process(EmailMsgTemplate emailMsgTemplate);

    /**
     * 失败处理
     *
     * @param emailMsgTemplate 消息
     */
    public void fail(EmailMsgTemplate emailMsgTemplate);
}
