package com.iot.portal.review.service;

import java.util.List;

import com.iot.portal.review.vo.PortalGetServiceReviewReq;
import com.iot.portal.review.vo.PortalServiceReviewRecordResp;
import com.iot.portal.review.vo.ReSetServiceReviewReq;


/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：语音审核
 * 功能描述：语音审核
 * 创建人： 李帅
 * 创建时间：2018年11月1日 下午8:03:54
 * 修改人：李帅
 * 修改时间：2018年11月1日 下午8:03:54
 */
public interface ServiceReviewService {

    /**
     * 
     * 描述：获取语音服务审核记录
     * @author 李帅
     * @created 2018年11月1日 下午8:09:27
     * @since 
     * @param portalGetServiceReviewReq
     * @return
     */
	List<PortalServiceReviewRecordResp> getServiceReviewRecord(PortalGetServiceReviewReq portalGetServiceReviewReq);

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
