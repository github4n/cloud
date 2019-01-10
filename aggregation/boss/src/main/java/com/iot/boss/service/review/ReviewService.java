package com.iot.boss.service.review;

import com.iot.boss.vo.product.resp.BossProductAuditListResp;
import com.iot.boss.vo.review.req.AppReviewReq;
import com.iot.boss.vo.review.req.BossProductAuditPageReq;
import com.iot.boss.vo.review.req.ProductReviewReq;
import com.iot.boss.vo.review.req.ServiceReviewReq;
import com.iot.boss.vo.review.req.TenantReviewReq;
import com.iot.boss.vo.review.resp.DetailInfoResp;
import com.iot.boss.vo.servicereview.BossServiceAuditListReq;
import com.iot.boss.vo.servicereview.BossServiceAuditListResp;
import com.iot.boss.vo.servicereview.ReSetServiceReviewReq;
import com.iot.common.helper.Page;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：boss聚合层
 * 功能描述：审核功能接口定义
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 15:57
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 15:57
 * 修改描述：
 */
public interface ReviewService {

    /**
     * 描述：提交App审核
     * @author maochengyuan
     * @created 2018/10/23 17:28
     * @param req 审核信息
     * @return void
     */
    void appReview(AppReviewReq req);

    /**
     * 描述：获取App审核资料
     * @author maochengyuan
     * @created 2018/10/23 17:31
     * @param appId appId
     * @return com.iot.boss.vo.review.resp.DetailInfoResp
     */
    DetailInfoResp getAppReviewInfo(Long appId);

    /**
     * 描述：提交产品审核
     * @author maochengyuan
     * @created 2018/10/23 17:28
     * @param req 审核信息
     * @return void
     */
    void productAudit(ProductReviewReq req);

    /**
     * 描述：获取产品审核资料
     * @author maochengyuan
     * @created 2018/10/23 17:31
     * @param productId 产品id
     * @return com.iot.boss.vo.review.resp.DetailInfoResp
     */
    DetailInfoResp getProductReviewInfo(Long productId);

    /**
     * 描述：提交租户审核
     * @author maochengyuan
     * @created 2018/10/23 17:28
     * @param req 审核信息
     * @return void
     */
    void tenantReview(TenantReviewReq req);

    /**
     * 描述：获取租户审核资料
     * @author maochengyuan
     * @created 2018/10/23 17:31
     * @param tenantId 租户id
     * @return com.iot.boss.vo.review.resp.DetailInfoResp
     */
    DetailInfoResp getTenantReviewInfo(Long tenantId);

    /**
     * 描述：提交语音服务审核
     * @author maochengyuan
     * @created 2018/10/23 17:28
     * @param req 审核信息
     * @return void
     */
    void serviceReview(ServiceReviewReq req);

    /**
     * 描述：获取语音服务审核资料
     * @author maochengyuan
     * @created 2018/10/23 17:31
     * @param productId 产品id
     * @param serviceId 语音服务id
     * @return com.iot.boss.vo.review.resp.DetailInfoResp
     */
    DetailInfoResp getServiceReviewInfo(Long productId, Long serviceId);

    /**
      * @despriction：获取产品审核列表数据
      * @author  yeshiyuan
      * @created 2018/10/25 10:29
      * @return
      */
    Page<BossProductAuditListResp> getProductAuditList(BossProductAuditPageReq pageReq);

    /**
     * @despriction：重开产品审核
     * @author  yeshiyuan
     * @created 2018/10/25 17:06
     * @return
     */
    void reOpenProductAudit(Long productId);
	
	/**
     * 
     * 描述：获取语音服务审核列表数据
     * @author 李帅
     * @created 2018年10月26日 上午11:30:32
     * @since 
     * @param pageReq
     * @return
     */
   Page<BossServiceAuditListResp> getServiceAuditList(BossServiceAuditListReq pageReq);
   
	/**
	 * 
	 * 描述：语音服务重新申请操作
	 * @author 李帅
	 * @created 2018年11月1日 下午8:23:46
	 * @since 
	 * @param reSetServiceReviewReq
	 */
	void reSetServiceReview(ReSetServiceReviewReq reSetServiceReviewReq);
}
