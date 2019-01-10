package com.iot.tenant.service.impl;

import com.iot.common.beans.BeanUtil;
import com.iot.common.helper.Page;
import com.iot.tenant.entity.ReplyMessageRecord;
import com.iot.tenant.mapper.ReplyMessageRecordMapper;
import com.iot.tenant.service.IReplyMessageRecordService;
import com.iot.tenant.vo.req.reply.AddReplyMessageReq;
import com.iot.tenant.vo.req.reply.PageQueryReplyMessageReq;
import com.iot.tenant.vo.req.reply.QueryReplyMessageListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class ReplyMessageRecordServiceImpl implements IReplyMessageRecordService{

    @Autowired
    private ReplyMessageRecordMapper replyMessageRecordMapper;

    /**
     * @despriction：添加
     * @author  yeshiyuan
     * @created 2018/11/1 20:09
     * @return
     */
    @Override
    public int add(AddReplyMessageReq messageReq) {
        return replyMessageRecordMapper.add(messageReq);
    }

    /**
     * @despriction：分页查询
     * @author  yeshiyuan
     * @created 2018/11/1 20:44
     * @return
     */
    @Override
    public Page<ReplyMessageRecord> pageQuery(PageQueryReplyMessageReq pageReq) {
        com.baomidou.mybatisplus.plugins.Page<ReplyMessageRecord> page = new com.baomidou.mybatisplus.plugins.Page<>(pageReq.getPageNum(),pageReq.getPageSize());
        List<ReplyMessageRecord> list = replyMessageRecordMapper.pageQuery(page, pageReq);
        page.setRecords(list);
        Page<ReplyMessageRecord> myPage = new Page<>();
        BeanUtil.copyProperties(page, myPage);
        return myPage;
    }

    /**
     * @despriction：查询回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 10:09
     * @return
     */
    @Override
    public List<ReplyMessageRecord> queryReplyList(QueryReplyMessageListReq req) {
        return replyMessageRecordMapper.findByObjectIdAndObjectTypeAndTenantId(req.getObjectId(), req.getObjectType(), req.getTenantId());
    }
}
