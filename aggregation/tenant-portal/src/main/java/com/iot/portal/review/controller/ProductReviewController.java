package com.iot.portal.review.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.portal.review.service.ProductReviewService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(description = "产品审核接口")
@RestController
@RequestMapping("/productReview")
public class ProductReviewController {

    /**
     * 产品审核
     */
    @Autowired
    private ProductReviewService productReviewService;

    /**
     * 
     * 描述：获取产品服务审核记录
     * @author 李帅
     * @created 2018年11月1日 下午8:09:18
     * @since 
     * @param portalGetServiceReviewReq
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取产品服务审核记录", notes = "获取产品服务审核记录")
    @RequestMapping(value = "/getReviewRecord", method = RequestMethod.GET)
    public CommonResponse getReviewRecord(@RequestParam("productId") Long productId) {
        return ResultMsg.SUCCESS.info(this.productReviewService.getReviewRecord(productId));
    }

    /**
     * 
     * 描述：产品服务重新申请操作
     * @author 李帅
     * @created 2018年11月1日 下午8:23:01
     * @since 
     * @param reSetServiceReviewReq
     * @return
     */
    @LoginRequired(value = Action.Normal)
	@ApiOperation(value = "产品服务重新申请操作", notes = "产品服务重新申请操作")
	@RequestMapping(value = "/reSubmitAudit", method = RequestMethod.GET)
	public CommonResponse reSubmitAudit(@RequestParam("productId") Long productId) {
		this.productReviewService.reSubmitAudit(productId);
		return ResultMsg.SUCCESS.info();
	}
	
}
