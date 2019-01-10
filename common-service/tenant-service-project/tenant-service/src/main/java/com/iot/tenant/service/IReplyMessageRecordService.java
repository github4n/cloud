package com.iot.tenant.service;

import com.iot.common.helper.Page;
import com.iot.tenant.entity.ReplyMessageRecord;
import com.iot.tenant.vo.req.reply.AddReplyMessageReq;
import com.iot.tenant.vo.req.reply.PageQueryReplyMessageReq;
import com.iot.tenant.vo.req.reply.QueryReplyMessageListReq;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：回复消息记录service
 * 创建人： yeshiyuan
 * 创建时间：2018/11/1 19:51
 * 修改人： yeshiyuan
 * 修改时间：2018/11/1 19:51
 * 修改描述：
 */
public interface IReplyMessageRecordService {

    /**
     * @despriction：添加
     * @author  yeshiyuan
     * @created 2018/11/1 20:09
     * @return
     */
    int add(AddReplyMessageReq messageReq);

    /**
     * @despriction：分页查询
     * @author  yeshiyuan
     * @created 2018/11/1 20:44
     * @return
     */
    Page<ReplyMessageRecord> pageQuery(PageQueryReplyMessageReq pageReq);

    /**
      * @despriction：查询回复详情
      * @author  yeshiyuan
      * @created 2018/11/2 10:09
      * @return
      */
    List<ReplyMessageRecord> queryReplyList(QueryReplyMessageListReq req);
}
