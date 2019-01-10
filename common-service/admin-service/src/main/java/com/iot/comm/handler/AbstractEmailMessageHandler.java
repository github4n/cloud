
package com.iot.comm.handler;


import com.iot.comm.bean.EmailMsgTemplate;

/**
 * 抽象hander
 */
public abstract class AbstractEmailMessageHandler implements EmailMessageHandler {

    /**
     * 执行入口
     *
     * @param emailMsgTemplate 信息
     */
    public void execute(EmailMsgTemplate emailMsgTemplate) {
        check(emailMsgTemplate);
        try {
            if (!process(emailMsgTemplate)) {
                fail(emailMsgTemplate);
            }
        } catch (Exception e) {
            fail(emailMsgTemplate);
        }
    }

    /**
     * 数据校验
     *
     * @param emailMsgTemplate 信息
     */
    public abstract void check(EmailMsgTemplate emailMsgTemplate);

    /**
     * 业务处理
     *
     * @param emailMsgTemplate 信息
     * @return boolean
     */
    @Override
    public abstract boolean process(EmailMsgTemplate emailMsgTemplate);

    /**
     * 失败处理
     *
     * @param emailMsgTemplate 信息
     */
    @Override
    public abstract void fail(EmailMsgTemplate emailMsgTemplate);
}
