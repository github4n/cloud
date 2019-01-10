package com.iot.portal.review.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.device.api.ServiceReviewApi;
import com.iot.device.vo.req.servicereview.GetServiceReviewReq;
import com.iot.device.vo.req.servicereview.SetServiceReviewReq;
import com.iot.device.vo.rsp.servicereview.ServiceReviewRecordResp;
import com.iot.portal.comm.utils.ParamUtil;
import com.iot.portal.review.service.ServiceReviewService;
import com.iot.portal.review.vo.PortalGetServiceReviewReq;
import com.iot.portal.review.vo.PortalServiceReviewRecordResp;
import com.iot.portal.review.vo.ReSetServiceReviewReq;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：语音审核
 * 功能描述：语音审核
 * 创建人： 李帅
 * 创建时间：2018年11月1日 下午8:03:47
 * 修改人：李帅
 * 修改时间：2018年11月1日 下午8:03:47
 */
@Service("serviceReviewService")
public class ServiceReviewServiceImpl implements ServiceReviewService {

	/**
	 * 区域信息
	 */
	@Autowired
	private ServiceReviewApi serviceReviewApi;
	
	@Autowired
	private UserApi userApi;

	/**
	 * 
	 * 描述：获取语音服务审核记录
	 * @author 李帅
	 * @created 2018年11月1日 下午8:09:33
	 * @since 
	 * @param portalGetServiceReviewReq
	 * @return
	 */
	@Override
	public List<PortalServiceReviewRecordResp> getServiceReviewRecord(PortalGetServiceReviewReq portalGetServiceReviewReq) {
		ParamUtil.checkPrimaryKey(portalGetServiceReviewReq.getProductId(), "productId");
		Long serviceId = ParamUtil.decryptKey(portalGetServiceReviewReq.getServiceId(), "serviceId");
		
		Long tenantId = SaaSContextHolder.currentTenantId();
		GetServiceReviewReq getServiceReviewReq = new GetServiceReviewReq();
		BeanUtils.copyProperties(portalGetServiceReviewReq, getServiceReviewReq);
		getServiceReviewReq.setTenantId(tenantId);
		getServiceReviewReq.setServiceId(serviceId);
		
		List<ServiceReviewRecordResp> serviceReviewRecordResps= this.serviceReviewApi.getServiceReviewRecord(getServiceReviewReq);
		List<Long> usetIds = new ArrayList<Long>();
		Map<Long, FetchUserResp> userMap = null;
		for(ServiceReviewRecordResp serviceReviewRecordResp : serviceReviewRecordResps) {
			usetIds.add(serviceReviewRecordResp.getCreateBy());
		}
		if(usetIds.size() > 0) {
			userMap = userApi.getByUserIds(usetIds);
		}
		List<PortalServiceReviewRecordResp> portalServiceReviewRecordResps = new ArrayList<PortalServiceReviewRecordResp>();
		PortalServiceReviewRecordResp portalServiceReviewRecordResp = null;
		for(ServiceReviewRecordResp serviceReviewRecordResp : serviceReviewRecordResps) {
			portalServiceReviewRecordResp =  new PortalServiceReviewRecordResp();
			BeanUtils.copyProperties(serviceReviewRecordResp, portalServiceReviewRecordResp);
			portalServiceReviewRecordResp.setOperator(userMap.get(serviceReviewRecordResp.getCreateBy()).getUserName());
			portalServiceReviewRecordResps.add(portalServiceReviewRecordResp);
		}
		return portalServiceReviewRecordResps;
	}

	/**
	 * 
	 * 描述：语音服务重新申请操作
	 * @author 李帅
	 * @created 2018年11月1日 下午8:23:53
	 * @since 
	 * @param reSetServiceReviewReq
	 */
	@Override
	public void reSetServiceReview(ReSetServiceReviewReq reSetServiceReviewReq) {
		ParamUtil.checkPrimaryKey(reSetServiceReviewReq.getProductId(), "productId");
//		ParamUtil.checkPrimaryKey(reSetServiceReviewReq.getServiceId(), "serviceId");
		Long serviceId = ParamUtil.decryptKey(reSetServiceReviewReq.getServiceId(), "serviceId");
		ParamUtil.checkOperateDesc(reSetServiceReviewReq.getOperateDesc());
        
		Long userId = SaaSContextHolder.getCurrentUserId();
		Long tenantId = SaaSContextHolder.currentTenantId();
		SetServiceReviewReq setServiceReviewReq = new SetServiceReviewReq();
		BeanUtils.copyProperties(reSetServiceReviewReq, setServiceReviewReq);
		setServiceReviewReq.setTenantId(tenantId);
		setServiceReviewReq.setUserId(userId);
		setServiceReviewReq.setProcessStatus((byte) 0);
		setServiceReviewReq.setServiceId(serviceId);
		this.serviceReviewApi.reSetServiceReview(setServiceReviewReq);
	}
}
