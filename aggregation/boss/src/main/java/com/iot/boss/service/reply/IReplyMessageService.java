package com.iot.boss.service.reply;

import com.iot.boss.vo.reply.req.AppLeaveMessageReq;
import com.iot.boss.vo.reply.req.ProductLeaveMessageReq;
import com.iot.boss.vo.reply.req.ReplyMessageReq;
import com.iot.boss.vo.reply.req.ServiceLeaveMessageReq;
import com.iot.boss.vo.reply.resp.ReplyDetailResp;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/11/2 11:10
 * 修改人： yeshiyuan
 * 修改时间：2018/11/2 11:10
 * 修改描述：
 */
public interface IReplyMessageService {

    /**
      * @despriction：app审核回复
      * @author  yeshiyuan
      * @created 2018/11/2 11:17
      * @return
      */
    void appReviewReply(ReplyMessageReq req);

    /**
     * @despriction：查询app审核回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 14:01
     * @return
     */
    List<ReplyDetailResp> queryAppReviewReplyDetail(Long objectId);

    /**
     * @despriction：app留言
     * @author  yeshiyuan
     * @created 2018/11/2 11:17
     * @return
     */
    void appLeaveMessage(AppLeaveMessageReq req);

    /**
     * @despriction：第三方接入回复
     * @author  yeshiyuan
     * @created 2018/11/2 11:17
     * @return
     */
    void serviceReply(ReplyMessageReq req);

    /**
     * @despriction：第三方接入回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 14:01
     * @return
     */
    List<ReplyDetailResp> queryServiceReplyDetail(Long objectId);

    /**
     * @despriction：第三方接入留言
     * @author  yeshiyuan
     * @created 2018/11/2 11:17
     * @return
     */
    void serviceLeaveMessage(ServiceLeaveMessageReq req);

    /**
     * @despriction：产品回复
     * @author  yeshiyuan
     * @created 2018/11/2 11:17
     * @return
     */
    void productReply(ReplyMessageReq req);

    /**
     * @despriction：产品回复回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 14:01
     * @return
     */
    List<ReplyDetailResp> queryProductReplyDetail(Long objectId);

    /**
     * @despriction：产品留言
     * @author  yeshiyuan
     * @created 2018/11/2 11:17
     * @return
     */
    void productLeaveMessage(ProductLeaveMessageReq req);

}
