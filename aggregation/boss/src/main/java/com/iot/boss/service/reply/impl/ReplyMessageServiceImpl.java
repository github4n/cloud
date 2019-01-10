package com.iot.boss.service.reply.impl;

import com.iot.boss.exception.BossExceptionEnum;
import com.iot.boss.service.reply.IReplyMessageService;
import com.iot.boss.vo.reply.req.AppLeaveMessageReq;
import com.iot.boss.vo.reply.req.ProductLeaveMessageReq;
import com.iot.boss.vo.reply.req.ReplyMessageReq;
import com.iot.boss.vo.reply.req.ServiceLeaveMessageReq;
import com.iot.boss.vo.reply.resp.ReplyDetailResp;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.device.api.ProductApi;
import com.iot.device.api.ProductReviewRecodApi;
import com.iot.device.api.ServiceReviewApi;
import com.iot.device.enums.product.ProductReviewProcessStatusEnum;
import com.iot.device.vo.req.product.ProductReviewRecordReq;
import com.iot.device.vo.req.servicereview.SetServiceReviewReq;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.api.AppReviewApi;
import com.iot.tenant.api.ReplyMessageRecordApi;
import com.iot.tenant.entity.ReplyMessageRecord;
import com.iot.tenant.enums.AuditStatusEnum;
import com.iot.tenant.enums.ReplyMessageType;
import com.iot.tenant.enums.ReplyObjectType;
import com.iot.tenant.vo.req.reply.AddReplyMessageReq;
import com.iot.tenant.vo.req.reply.QueryReplyMessageListReq;
import com.iot.tenant.vo.req.review.AppReviewRecordReq;
import com.iot.tenant.vo.resp.AppInfoResp;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 功能描述：回复消息service
 * 创建人： yeshiyuan
 * 创建时间：2018/11/2 11:10
 * 修改人： yeshiyuan
 * 修改时间：2018/11/2 11:10
 * 修改描述：
 */
@Service
public class ReplyMessageServiceImpl implements IReplyMessageService{

    @Autowired
    private ReplyMessageRecordApi replyMessageRecordApi;

    @Autowired
    private AppReviewApi appReviewApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private AppApi appApi;

    @Autowired
    private ServiceReviewApi serviceReviewApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ProductReviewRecodApi productReviewRecodApi;

    /**
     * @despriction：app审核回复
     * @author  yeshiyuan
     * @created 2018/11/2 11:17
     * @return
     */
    @Override
    public void appReviewReply(ReplyMessageReq req) {
        Long tenantId = appReviewApi.getTenantIdById(req.getObjectId());
        if (tenantId == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "The review record about the APP does not exist.");
        }
        this.reply(req, ReplyObjectType.app_package.getValue(), tenantId);
    }

    /**
     * @despriction：查询app审核回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 14:01
     * @return
     */
    @Override
    public List<ReplyDetailResp> queryAppReviewReplyDetail(Long objectId) {
        Long tenantId = appReviewApi.getTenantIdById(objectId);
        if (tenantId == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "The review record about the APP does not exist.");
        }
        QueryReplyMessageListReq listReq = new QueryReplyMessageListReq(objectId, ReplyObjectType.app_package.getValue(), tenantId );
        return replyDetailList(listReq);
    }

    /**
     * @despriction：app留言
     * @author  yeshiyuan
     * @created 2018/11/2 11:17
     * @return
     */
    @Override
    public void appLeaveMessage(AppLeaveMessageReq req) {
        AppInfoResp appInfoResp = appApi.getAppById(req.getAppId());
        if (appInfoResp == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "app isn't exist");
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        AppReviewRecordReq appReviewRecordReq = new AppReviewRecordReq();
        appReviewRecordReq.setAppId(req.getAppId());
        appReviewRecordReq.setCreateBy(userId);
        appReviewRecordReq.setCreateTime(new Date());
        appReviewRecordReq.setOperateDesc(req.getOperateDesc());
        appReviewRecordReq.setOperateTime(new Date());
        appReviewRecordReq.setProcessStatus(AuditStatusEnum.LeaveMessage.getAuditStatus());
        Long reviewId = appReviewApi.addRecord(appReviewRecordReq);
        AddReplyMessageReq addReq = new AddReplyMessageReq(appInfoResp.getTenantId(),
                ReplyObjectType.app_package.getValue(), reviewId, req.getOperateDesc(), ReplyMessageType.REPLY.getValue(),userId, new Date());
        replyMessageRecordApi.add(addReq);
    }

    /**
     * @despriction：第三方接入回复
     * @author  yeshiyuan
     * @created 2018/11/2 11:17
     * @return
     */
    @Override
    public void serviceReply(ReplyMessageReq req) {
        Long tenantId = serviceReviewApi.getTenantIdById(req.getObjectId());
        if (tenantId == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "The review record about the service does not exist.");
        }
        this.reply(req, ReplyObjectType.service_review.getValue(), tenantId);
    }
    /**
     * @despriction：第三方接入回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 14:01
     * @return
     */
    @Override
    public List<ReplyDetailResp> queryServiceReplyDetail(Long objectId) {
        Long tenantId = serviceReviewApi.getTenantIdById(objectId);
        if (tenantId == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "The review record about the service does not exist.");
        }
        QueryReplyMessageListReq listReq = new QueryReplyMessageListReq(objectId, ReplyObjectType.service_review.getValue(), tenantId );

        return replyDetailList(listReq);
    }

    /**
     * @despriction：第三方接入留言
     * @author  yeshiyuan
     * @created 2018/11/2 11:17
     * @return
     */
    @Override
    public void serviceLeaveMessage(ServiceLeaveMessageReq req) {
        ProductResp productResp = productApi.getProductById(req.getProductId());
        if (productResp == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "The product does not exist.");
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        SetServiceReviewReq reviewReq = new SetServiceReviewReq(productResp.getTenantId(), userId, req.getServiceId(), req.getProductId(),AuditStatusEnum.LeaveMessage.getAuditStatus() , req.getOperateDesc());
        Long reviewId = serviceReviewApi.addRecord(reviewReq);
        AddReplyMessageReq addReq = new AddReplyMessageReq(productResp.getTenantId(),
                ReplyObjectType.service_review.getValue(), reviewId, req.getOperateDesc(), ReplyMessageType.REPLY.getValue(),userId, new Date());
        replyMessageRecordApi.add(addReq);
    }

    /**
     * @despriction：产品回复
     * @author  yeshiyuan
     * @created 2018/11/2 11:17
     * @return
     */
    @Override
    public void productReply(ReplyMessageReq req) {
        Long tenantId = productReviewRecodApi.getTenantIdById(req.getObjectId());
        if (tenantId == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "The review record about the product does not exist.");
        }
        this.reply(req, ReplyObjectType.product_review.getValue(), tenantId);
    }
    /**
     * @despriction：产品回复回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 14:01
     * @return
     */
    @Override
    public List<ReplyDetailResp> queryProductReplyDetail(Long objectId) {
        Long tenantId = productReviewRecodApi.getTenantIdById(objectId);
        if (tenantId == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "The review record about the product does not exist.");
        }
        QueryReplyMessageListReq listReq = new QueryReplyMessageListReq(objectId, ReplyObjectType.product_review.getValue(), tenantId );
        return replyDetailList(listReq);
    }

    /**
     * @despriction：产品留言
     * @author  yeshiyuan
     * @created 2018/11/2 11:17
     * @return
     */
    @Override
    public void productLeaveMessage(ProductLeaveMessageReq req) {
        ProductResp productResp = productApi.getProductById(req.getProductId());
        if (productResp == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "The product does not exist.");
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        ProductReviewRecordReq reviewRecordReq = new ProductReviewRecordReq();
        reviewRecordReq.setOperateDesc(req.getOperateDesc());
        reviewRecordReq.setProcessStatus(ProductReviewProcessStatusEnum.LeaveMessage.getValue());
        reviewRecordReq.setProductId(req.getProductId());
        reviewRecordReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        reviewRecordReq.setOperateTime(new Date());
        Long reviewId = productReviewRecodApi.addRecord(reviewRecordReq);
        AddReplyMessageReq addReq = new AddReplyMessageReq(productResp.getTenantId(),
                ReplyObjectType.service_review.getValue(), reviewId, req.getOperateDesc(), ReplyMessageType.REPLY.getValue(),userId, new Date());
        replyMessageRecordApi.add(addReq);
    }

    /**
      * @despriction：回复
      * @author  yeshiyuan
      * @created 2018/11/3 14:24
      * @return
      */
    private void reply(ReplyMessageReq req, String objectType, Long tenantId) {
        AddReplyMessageReq addReplyMessageReq = new AddReplyMessageReq();
        BeanUtil.copyProperties(req, addReplyMessageReq);
        addReplyMessageReq.setObjectType(objectType);
        addReplyMessageReq.setTenantId(tenantId);
        addReplyMessageReq.setMessageType(ReplyMessageType.REPLY.getValue());
        addReplyMessageReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        addReplyMessageReq.setCreateTime(new Date());
        replyMessageRecordApi.add(addReplyMessageReq);
    }

    /**
      * @despriction：回复详情
      * @author  yeshiyuan
      * @created 2018/11/3 14:29
      * @return
      */
    private List<ReplyDetailResp> replyDetailList(QueryReplyMessageListReq req) {
        List<ReplyMessageRecord> list = replyMessageRecordApi.queryReplyList(req);
        List<ReplyDetailResp> resps = new ArrayList<>();
        if (!list.isEmpty()) {
            Set<Long> userIds = list.stream().map(ReplyMessageRecord::getCreateBy).collect(Collectors.toSet());
            Map<Long,FetchUserResp> userMap = userApi.getByUserIds(new ArrayList<>(userIds));
            list.forEach(o -> {
                ReplyDetailResp resp = new ReplyDetailResp();
                BeanUtil.copyProperties(o, resp);
                resp.setUserName(userMap.get(o.getCreateBy()).getUserName());
                resps.add(resp);
            });
        }
        return resps;
    }
}
