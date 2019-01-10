package com.iot.tenant.controller;

import com.iot.common.helper.Page;
import com.iot.tenant.api.ReplyMessageRecordApi;
import com.iot.tenant.entity.ReplyMessageRecord;
import com.iot.tenant.service.IReplyMessageRecordService;
import com.iot.tenant.vo.req.reply.AddReplyMessageReq;
import com.iot.tenant.vo.req.reply.PageQueryReplyMessageReq;
import com.iot.tenant.vo.req.reply.QueryReplyMessageListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：回复消息记录controller
 * 创建人： yeshiyuan
 * 创建时间：2018/11/1 19:55
 * 修改人： yeshiyuan
 * 修改时间：2018/11/1 19:55
 * 修改描述：
 */
@RestController
public class ReplyMessageRecordController implements ReplyMessageRecordApi {

    @Autowired
    private IReplyMessageRecordService replyMessageRecordService;

    /**
     * @despriction：添加
     * @author  yeshiyuan
     * @created 2018/11/1 20:09
     * @return
     */
    @Override
    public int add(@RequestBody AddReplyMessageReq messageReq) {
        AddReplyMessageReq.checkParam(messageReq);
        return replyMessageRecordService.add(messageReq);
    }

    /**
     * @despriction：分页查询回复消息列表
     * @author  yeshiyuan
     * @created 2018/11/1 20:34
     * @return
     */
    @Override
    public Page pageQuery(@RequestBody PageQueryReplyMessageReq pageReq) {
        return replyMessageRecordService.pageQuery(pageReq);
    }

    /**
     * @despriction：查询回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 10:09
     * @return
     */
    @Override
    public List<ReplyMessageRecord> queryReplyList(@RequestBody QueryReplyMessageListReq req) {
        return replyMessageRecordService.queryReplyList(req);
    }
}
