package com.iot.portal.review.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.portal.review.service.ServiceReviewService;
import com.iot.portal.review.vo.PortalGetServiceReviewReq;
import com.iot.portal.review.vo.ReSetServiceReviewReq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(description = "语音审核接口")
@RestController
@RequestMapping("/serviceReview")
public class ServiceReviewController {

    /**
     * 语音审核
     */
    @Autowired
    private ServiceReviewService serviceReviewService;

    /**
     * 
     * 描述：获取语音服务审核记录
     * @author 李帅
     * @created 2018年11月1日 下午8:09:18
     * @since 
     * @param portalGetServiceReviewReq
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取语音服务审核记录", notes = "获取语音服务审核记录")
    @RequestMapping(value = "/getServiceReviewRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getServiceReviewRecord(@RequestBody PortalGetServiceReviewReq portalGetServiceReviewReq) {
        return ResultMsg.SUCCESS.info(this.serviceReviewService.getServiceReviewRecord(portalGetServiceReviewReq));
    }

    /**
     * 
     * 描述：语音服务重新申请操作
     * @author 李帅
     * @created 2018年11月1日 下午8:23:01
     * @since 
     * @param reSetServiceReviewReq
     * @return
     */
    @LoginRequired(value = Action.Normal)
	@ApiOperation(value = "语音服务重新申请操作", notes = "语音服务重新申请操作")
	@RequestMapping(value = "/reSetServiceReview", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse reSetServiceReview(@RequestBody ReSetServiceReviewReq reSetServiceReviewReq) {
		this.serviceReviewService.reSetServiceReview(reSetServiceReviewReq);
		return ResultMsg.SUCCESS.info();
	}
    
}
