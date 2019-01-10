
package com.iot.comm.handler;

import com.iot.comm.bean.MobileMsgTemplate;

public interface SmsMessageHandler {
    /**
     * 执行入口
     *
     * @param mobileMsgTemplate 信息
     */
    void execute(MobileMsgTemplate mobileMsgTemplate);

    /**
     * 数据校验
     *
     * @param mobileMsgTemplate 信息
     */
    void check(MobileMsgTemplate mobileMsgTemplate);

    /**
     * 业务处理
     *
     * @param mobileMsgTemplate 信息
     * @return boolean
     */
    boolean process(MobileMsgTemplate mobileMsgTemplate);

    /**
     * 失败处理
     *
     * @param mobileMsgTemplate 信息
     */
    void fail(MobileMsgTemplate mobileMsgTemplate);
}
