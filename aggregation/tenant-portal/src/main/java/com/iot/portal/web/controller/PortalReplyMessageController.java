package com.iot.portal.web.controller;

import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.device.api.ProductApi;
import com.iot.device.api.ProductReviewRecodApi;
import com.iot.device.api.ServiceReviewApi;
import com.iot.device.enums.product.ProductReviewProcessStatusEnum;
import com.iot.device.vo.req.product.ProductReviewRecordReq;
import com.iot.device.vo.req.servicereview.SetServiceReviewReq;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.portal.web.vo.reply.req.AppFeedbackReq;
import com.iot.portal.web.vo.reply.req.ProductFeedbackReq;
import com.iot.portal.web.vo.reply.req.ReplyMessageReq;
import com.iot.portal.web.vo.reply.req.ServiceFeedbackReq;
import com.iot.portal.web.vo.reply.resp.ReplyDetailResp;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 功能描述：portal回复消息api
 * 创建人： yeshiyuan
 * 创建时间：2018/11/2 11:05
 * 修改人： yeshiyuan
 * 修改时间：2018/11/2 11:05
 * 修改描述：
 */
@Api(tags = "portal回复消息记录api")
@RestController
@RequestMapping(value = "/api/replyMessage")
public class PortalReplyMessageController {

    @Autowired
    private ReplyMessageRecordApi replyMessageRecordApi;
    @Autowired
    private AppApi appApi;
    @Autowired
    private AppReviewApi appReviewApi;
    @Autowired
    private UserApi userApi;
    @Autowired
    private ServiceReviewApi serviceReviewApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private ProductReviewRecodApi productReviewRecodApi;


    /**
      * @despriction：app反馈
      * @author  yeshiyuan
      * @created 2018/11/2 16:00
      * @return
      */
    @LoginRequired
    @ApiOperation(value = "app打包反馈", notes = "app打包反馈")
    @RequestMapping(value = "/appPackFeedback", method = RequestMethod.POST)
    public CommonResponse appPackFeedback(@RequestBody AppFeedbackReq req) {
        AppInfoResp appInfoResp = appApi.getAppById(req.getAppId());
        if (appInfoResp == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "app isn't exist");
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        AppReviewRecordReq appReviewRecordReq = new AppReviewRecordReq();
        appReviewRecordReq.setAppId(req.getAppId());
        appReviewRecordReq.setCreateBy(userId);
        appReviewRecordReq.setCreateTime(new Date());
        appReviewRecordReq.setOperateDesc(req.getFeedbackContent());
        appReviewRecordReq.setOperateTime(new Date());
        appReviewRecordReq.setProcessStatus(AuditStatusEnum.FeedbackMessage.getAuditStatus());
        Long reviewId = appReviewApi.addRecord(appReviewRecordReq);
        AddReplyMessageReq addReq = new AddReplyMessageReq(appInfoResp.getTenantId(),
                ReplyObjectType.app_package.getValue(), reviewId, req.getFeedbackContent(), ReplyMessageType.FEEDBACK.getValue(),userId, new Date());
        replyMessageRecordApi.add(addReq);
        return CommonResponse.success();
    }

    /**
     * @despriction：app打包回复
     * @author  yeshiyuan
     * @created 2018/11/2 11:08
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "app打包回复", notes = "app打包回复")
    @RequestMapping(value = "/appPackReply", method = RequestMethod.POST)
    public CommonResponse appPackReply(@RequestBody ReplyMessageReq req){
        Long tenantId = appReviewApi.getTenantIdById(req.getObjectId());
        if (tenantId == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The review record about the APP does not exist.");
        }
        this.reply(req, ReplyObjectType.app_package.getValue(), tenantId);
        return CommonResponse.success();
    }

    /**
      * @despriction：app回复详情
      * @author  yeshiyuan
      * @created 2018/11/2 17:06
      * @return
      */
    @LoginRequired
    @ApiOperation(value = "app回复详情", notes = "app回复详情")
    @RequestMapping(value = "/queryAppReplyDetail", method = RequestMethod.GET)
    public CommonResponse queryAppReplyDetail(Long objectId) {
        QueryReplyMessageListReq listReq = new QueryReplyMessageListReq(objectId, ReplyObjectType.app_package.getValue(), SaaSContextHolder.currentTenantId() );
        List<ReplyDetailResp> resps = replyDetailList(listReq);
        return CommonResponse.success(resps);
    }


    /**
     * @despriction：第三方服务接入反馈
     * @author  yeshiyuan
     * @created 2018/11/2 16:00
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "第三方服务接入反馈", notes = "第三方服务接入反馈")
    @RequestMapping(value = "/serviceFeedback", method = RequestMethod.POST)
    public CommonResponse serviceFeedback(@RequestBody ServiceFeedbackReq req) {
        ProductResp productResp = productApi.getProductById(req.getProductId());
        if (productResp == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The product does not exist.");
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        SetServiceReviewReq reviewReq = new SetServiceReviewReq(productResp.getTenantId(), userId, req.getServiceId(), req.getProductId(),AuditStatusEnum.FeedbackMessage.getAuditStatus(), req.getOperateDesc());
        Long reviewId = serviceReviewApi.addRecord(reviewReq);
        AddReplyMessageReq addReq = new AddReplyMessageReq(productResp.getTenantId(),
                ReplyObjectType.service_review.getValue(), reviewId, req.getOperateDesc(), ReplyMessageType.FEEDBACK.getValue(),userId, new Date());
        replyMessageRecordApi.add(addReq);
        return CommonResponse.success();
    }


    /**
     * @despriction：第三方接入回复
     * @author  yeshiyuan
     * @created 2018/11/2 11:08
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "第三方接入回复", notes = "第三方接入回复")
    @RequestMapping(value = "/serviceReply", method = RequestMethod.POST)
    public CommonResponse serviceReply(@RequestBody ReplyMessageReq req){
        Long tenantId = serviceReviewApi.getTenantIdById(req.getObjectId());
        if (tenantId == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The review record about the service does not exist.");
        }
        this.reply(req, ReplyObjectType.service_review.getValue(), tenantId);
        return CommonResponse.success();
    }

    /**
     * @despriction：第三方接入回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 14:01
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "第三方接入回复详情", notes = "第三方接入回复详情")
    @RequestMapping(value = "/queryServiceReplyDetail", method = RequestMethod.GET)
    public CommonResponse queryServiceReplyDetail(@RequestParam Long objectId) {
        Long tenantId = serviceReviewApi.getTenantIdById(objectId);
        if (tenantId == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The review record about the service does not exist.");
        }
        QueryReplyMessageListReq listReq = new QueryReplyMessageListReq(objectId, ReplyObjectType.service_review.getValue(), tenantId );
        List<ReplyDetailResp> resps = this.replyDetailList(listReq);
        return CommonResponse.success(resps);
    }

    /**
     * @despriction：产品反馈
     * @author  yeshiyuan
     * @created 2018/11/2 16:00
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "产品反馈", notes = "产品反馈")
    @RequestMapping(value = "/productFeedback", method = RequestMethod.POST)
    public CommonResponse productFeedback(@RequestBody ProductFeedbackReq req) {
        ProductResp productResp = productApi.getProductById(req.getProductId());
        if (productResp == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The product does not exist.");
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        ProductReviewRecordReq reviewRecordReq = new ProductReviewRecordReq();
        reviewRecordReq.setOperateDesc(req.getFeedbackContent());
        reviewRecordReq.setProcessStatus(ProductReviewProcessStatusEnum.FeedbackMessage.getValue());
        reviewRecordReq.setProductId(req.getProductId());
        reviewRecordReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        reviewRecordReq.setOperateTime(new Date());
        Long reviewId = productReviewRecodApi.addRecord(reviewRecordReq);
        AddReplyMessageReq addReq = new AddReplyMessageReq(productResp.getTenantId(),
                ReplyObjectType.service_review.getValue(), reviewId, req.getFeedbackContent(), ReplyMessageType.FEEDBACK.getValue(),userId, new Date());
        replyMessageRecordApi.add(addReq);
        return CommonResponse.success();
    }


    /**
     * @despriction：产品回复
     * @author  yeshiyuan
     * @created 2018/11/2 11:08
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "产品回复", notes = "产品回复")
    @RequestMapping(value = "/productReply", method = RequestMethod.POST)
    public CommonResponse productReply(@RequestBody ReplyMessageReq req){
        Long tenantId = productReviewRecodApi.getTenantIdById(req.getObjectId());
        if (tenantId == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The review record about the product does not exist.");
        }
        this.reply(req, ReplyObjectType.product_review.getValue(), tenantId);
        return CommonResponse.success();
    }

    /**
     * @despriction：产品回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 14:01
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "产品回复详情", notes = "产品回复详情")
    @RequestMapping(value = "/queryProductReplyDetail", method = RequestMethod.GET)
    public CommonResponse queryProductReplyDetail(@RequestParam Long objectId) {
        Long tenantId = productReviewRecodApi.getTenantIdById(objectId);
        if (tenantId == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The review record about the product does not exist.");
        }
        QueryReplyMessageListReq listReq = new QueryReplyMessageListReq(objectId, ReplyObjectType.product_review.getValue(), tenantId );
        List<ReplyDetailResp> resps = this.replyDetailList(listReq);
        return CommonResponse.success(resps);
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
                resp.setUserName(userMap.get(o.getCreateBy()).getNickname());
                resps.add(resp);
            });
        }
        return resps;
    }
}
