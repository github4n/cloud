package com.iot.portal.review.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.device.api.ProductApi;
import com.iot.device.api.ProductReviewRecodApi;
import com.iot.device.vo.req.product.ReopenAuditReq;
import com.iot.device.vo.rsp.product.ProductReviewRecordResp;
import com.iot.device.vo.rsp.servicereview.ServiceReviewRecordResp;
import com.iot.portal.comm.utils.ParamUtil;
import com.iot.portal.review.service.ProductReviewService;
import com.iot.portal.review.vo.PortalProductReviewRecordResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：产品审核
 * 功能描述：产品审核
 * 创建人： 李帅
 * 创建时间：2018年11月1日 下午8:03:47
 * 修改人：李帅
 * 修改时间：2018年11月1日 下午8:03:47
 */
@Service("productReviewService")
public class ProductReviewServiceImpl implements ProductReviewService {

	/**
	 * 产品信息
	 */
	@Autowired
	private ProductReviewRecodApi productReviewRecodApi;

	@Autowired
	private ProductApi productApi;
	
	@Autowired
	private UserApi userApi;
	
	/**
	 * 
	 * 描述：获取产品服务审核记录
	 * @author 李帅
	 * @created 2018年11月1日 下午8:09:33
	 * @since 
	 * @param portalGetServiceReviewReq
	 * @return
	 */
	@Override
	public List<PortalProductReviewRecordResp> getReviewRecord(Long productId) {
		ParamUtil.checkPrimaryKey(productId, "productId");
		List<ProductReviewRecordResp> records = this.productReviewRecodApi.getReviewRecord(productId);
		if(records == null || records.size() == 0) {
			return null;
		}
		List<PortalProductReviewRecordResp> recordResps= new ArrayList<PortalProductReviewRecordResp>();
		PortalProductReviewRecordResp recordResp = null;
		List<Long> createBys = new ArrayList<Long>();
		for(ProductReviewRecordResp productReviewRecordResp : records) {
			recordResp = new PortalProductReviewRecordResp();
			BeanUtils.copyProperties(productReviewRecordResp, recordResp);
			recordResps.add(recordResp);
			createBys.add(productReviewRecordResp.getCreateBy());
		}
		Map<Long, FetchUserResp> userMap = null;
		if(createBys.size() > 0) {
			userMap = userApi.getByUserIds(createBys);
		}
		for(PortalProductReviewRecordResp portalProductReviewRecordResp : recordResps) {
			if(userMap != null && portalProductReviewRecordResp.getCreateBy() != null
					&& userMap.get(portalProductReviewRecordResp.getCreateBy()) != null) {
				portalProductReviewRecordResp.setOperator(userMap.get(portalProductReviewRecordResp.getCreateBy()).getUserName());
			}
		}
		return recordResps;
	}

	/**
	 * 
	 * 描述：产品服务重新申请操作
	 * @author 李帅
	 * @created 2018年11月1日 下午8:23:53
	 * @since 
	 * @param reSetServiceReviewReq
	 */
	@Override
	public void reSubmitAudit(Long productId) {
		ParamUtil.checkPrimaryKey(productId, "productId");
        
		Long userId = SaaSContextHolder.getCurrentUserId();
		ReopenAuditReq reopenAuditReq = new ReopenAuditReq();
		reopenAuditReq.setOperateTime(new Date());;
		reopenAuditReq.setProductId(productId);
		reopenAuditReq.setUserId(userId);
		this.productApi.reOpenAudit(reopenAuditReq);
	}
	
}
