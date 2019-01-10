package com.iot.boss.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.boss.service.review.ReviewService;
import com.iot.boss.vo.review.req.AppReviewReq;
import com.iot.boss.vo.review.req.BossProductAuditPageReq;
import com.iot.boss.vo.review.req.ProductReviewReq;
import com.iot.boss.vo.review.req.ServiceReviewReq;
import com.iot.boss.vo.review.req.TenantReviewReq;
import com.iot.boss.vo.servicereview.BossServiceAuditListReq;
import com.iot.boss.vo.servicereview.ReSetServiceReviewReq;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：boss聚合层
 * 功能描述：审核功能（用户审核、APP审核、产品审核、语音服务接入审核）
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 15:57
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 15:57
 * 修改描述：
 */
@Api(value = "Boss-审核管理", description = "Boss-审核管理")
@RestController
@RequestMapping("/api/reviewController")
public class ReviewController {

    private Logger log = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @ApiOperation(value = "App审核", notes = "提交App审核")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/appReview", method = RequestMethod.POST)
    public CommonResponse appReview(@RequestBody AppReviewReq req) {
        this.reviewService.appReview(req);
        return CommonResponse.success();
    }

    @ApiOperation(value = "App审核", notes = "获取App审核资料")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/getAppReviewInfo", method = RequestMethod.GET)
    public CommonResponse getAppReviewInfo(@RequestParam("appId") Long appId) {
        return CommonResponse.success(this.reviewService.getAppReviewInfo(appId));
    }

    @ApiOperation(value = "产品审核", notes = "提交产品审核")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/productAudit", method = RequestMethod.POST)
    public CommonResponse productAudit(@RequestBody ProductReviewReq req) {
        this.reviewService.productAudit(req);
        return CommonResponse.success();
    }

    @ApiOperation(value = "获取产品审核资料", notes = "获取产品审核资料")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/getProductReviewInfo", method = RequestMethod.GET)
    public CommonResponse getProductReviewInfo(@RequestParam("productId") Long productId) {
        return CommonResponse.success(this.reviewService.getProductReviewInfo(productId));
    }

    @ApiOperation(value = "租户审核", notes = "提交租户审核")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/tenantReview", method = RequestMethod.GET)
    public CommonResponse tenantReview(@RequestBody TenantReviewReq req) {
        this.reviewService.tenantReview(req);
        return CommonResponse.success();
    }

    @ApiOperation(value = "租户审核", notes = "获取租户审核资料")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/getTenantReviewInfo", method = RequestMethod.GET)
    public CommonResponse getTenantReviewInfo(@RequestParam("tenantId") Long tenantId) {
        return CommonResponse.success(this.reviewService.getTenantReviewInfo(tenantId));
    }

    @ApiOperation(value = "语音服务审核", notes = "提交语音服务审核")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/serviceReview", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse serviceReview(@RequestBody ServiceReviewReq req) {
        this.reviewService.serviceReview(req);
        return CommonResponse.success();
    }

    @ApiOperation(value = "语音服务审核", notes = "获取语音服务审核资料")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/getServiceReviewInfo", method = RequestMethod.GET)
    public CommonResponse getServiceReviewInfo(@RequestParam("productId") Long productId, @RequestParam("serviceId") Long serviceId) {
        return CommonResponse.success(this.reviewService.getServiceReviewInfo(productId, serviceId));
    }

    @LoginRequired(Action.Normal)
    @ApiOperation(value = "加载产品审核列表", notes = "加载产品审核列表")
    @RequestMapping(value = "/getProductAuditList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getProductAuditList(@RequestBody BossProductAuditPageReq pageReq) {
        return CommonResponse.success(reviewService.getProductAuditList(pageReq));
    }

    /**
     * @despriction：重开审核
     * @author  yeshiyuan
     * @created 2018/10/25 17:06
     * @return
     */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "重开产品审核", notes = "重开产品审核")
    @RequestMapping(value = "/reOpenProductAudit", method = RequestMethod.POST)
    public CommonResponse reOpenProductAudit(Long productId) {
        reviewService.reOpenProductAudit(productId);
        return CommonResponse.success();
    }
	
	@LoginRequired(Action.Normal)
    @ApiOperation(value = "加载语音服务审核列表", notes = "加载语音服务审核列表")
    @RequestMapping(value = "/getServiceAuditList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getServiceAuditList(@RequestBody BossServiceAuditListReq pageReq) {
        return CommonResponse.success(reviewService.getServiceAuditList(pageReq));
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
		this.reviewService.reSetServiceReview(reSetServiceReviewReq);
		return ResultMsg.SUCCESS.info();
	}
}