package com.iot.boss.service.refund;


import com.iot.boss.vo.refund.req.RefundOperateReq;
import com.iot.boss.vo.refund.req.RefundReq;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： mao2080@sina.com
 * 创建时间：2018/11/13 20:09
 * 修改人： mao2080@sina.com
 * 修改时间：2018/11/13 20:09
 * 修改描述：
 */
public interface IRefundProcesser {

    /** 
     * 描述：执行退款
     * @author maochengyuan
     * @created 2018/11/14 9:30
     * @param req
     * @return void
     */
    String execute(RefundReq req);

    /** 
     * 描述：退款检查
     * @author maochengyuan
     * @created 2018/11/14 9:30
     * @param req
     * @return void
     */
    <T extends RefundOperateReq> T beforeRefund(RefundReq req);

    /** 
     * 描述：退款成功回调
     * @author maochengyuan
     * @created 2018/11/14 9:31
     * @param req
     * @return void
     */
    void refundSuccess(RefundOperateReq req);

    /** 
     * 描述：退款失败回调
     * @author maochengyuan
     * @created 2018/11/14 9:31
     * @param req
     * @return void
     */
    void refundFail(RefundOperateReq req);

}
