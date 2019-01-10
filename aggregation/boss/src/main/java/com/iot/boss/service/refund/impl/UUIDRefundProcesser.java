package com.iot.boss.service.refund.impl;

import com.iot.boss.exception.BossExceptionEnum;
import com.iot.boss.vo.refund.req.RefundOperateReq;
import com.iot.boss.vo.refund.req.RefundReq;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.device.api.UUIDManegerApi;
import com.iot.device.vo.req.uuid.UUIDRefundOperateReq;
import com.iot.device.vo.req.uuid.UUIDRefundReq;
import com.iot.device.vo.req.uuid.UUidApplyRecordInfo;
import com.iot.payment.api.OrderApi;
import com.iot.payment.entity.order.OrderRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： mao2080@sina.com
 * 创建时间：2018/11/13 20:13
 * 修改人： mao2080@sina.com
 * 修改时间：2018/11/13 20:13
 * 修改描述：
 */
@Service
public class UUIDRefundProcesser extends RefundProcesser {

	@Autowired
	private UUIDManegerApi uuidManegerApi;
	
	@Autowired
    private OrderApi orderApi;

	@Override
	public RefundOperateReq beforeRefund(RefundReq req) {
		if(req == null){
			throw new BusinessException(BossExceptionEnum.UUIDREFUNDREQ_IS_NULL, "入参为空");
		}
		if(req.getTenantId() == null) {
			throw new BusinessException(BossExceptionEnum.TENANTID_IS_NULL, "租户ID为空");
		}
		if(StringUtil.isEmpty(req.getOrderId())) {
			throw new BusinessException(BossExceptionEnum.ORDERID_IS_NULL, "订单ID为空");
		}
		ArrayList<String> orderIds=new ArrayList<String>();
		orderIds.add(req.getOrderId());
        Map<String, OrderRecord> orderRecordMap = orderApi.getOrderRecordByOrderIds(orderIds);
        if(orderRecordMap == null || orderRecordMap.get(req.getOrderId()) == null) {
        	throw new BusinessException(BossExceptionEnum.ORDERID_NOT_EXIST, "订单不存在");
        }else if(orderRecordMap.get(req.getOrderId()).getTotalPrice().intValue() < req.getRefundSum().intValue()) {
        	throw new BusinessException(BossExceptionEnum.REFUND_EXCEED_ORDER, "退款金额超过订单金额");
        }
		UUIDRefundReq uuidRefundReq = new UUIDRefundReq();
		uuidRefundReq.setOrderId(req.getOrderId());
		uuidRefundReq.setTenantId(req.getTenantId());
		UUidApplyRecordInfo applyRecordInfo = uuidManegerApi.beforeUUIDRefund(uuidRefundReq);
		
		RefundOperateReq refundOperateReq = new RefundOperateReq();
		BeanUtils.copyProperties(req, refundOperateReq);
		refundOperateReq.setUserId(applyRecordInfo.getUserId());
		refundOperateReq.setRefundObjectId(applyRecordInfo.getId());
		return refundOperateReq;
	}

	@Override
	public void refundSuccess(RefundOperateReq req) {
		UUIDRefundOperateReq refundOperateReq = new UUIDRefundOperateReq();
		BeanUtils.copyProperties(req, refundOperateReq);
		uuidManegerApi.refundSuccess(refundOperateReq);
	}

	@Override
	public void refundFail(RefundOperateReq req) {
		UUIDRefundOperateReq refundOperateReq = new UUIDRefundOperateReq();
		BeanUtils.copyProperties(req, refundOperateReq);
		uuidManegerApi.refundFail(refundOperateReq);
	}

}
