package com.iot.device.service.impl;

import com.iot.common.exception.BusinessException;
import com.iot.device.mapper.ServiceReviewMapper;
import com.iot.device.model.ServiceReviewRecord;
import com.iot.device.service.IServiceReviewService;
import com.iot.device.vo.req.servicereview.GetServiceReviewReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("ServiceReviewService")
@Transactional
public class ServiceReviewServiceImpl implements IServiceReviewService {

	/**日志*/
	@SuppressWarnings("unused")
	private static final Logger loger = LoggerFactory.getLogger(ServiceReviewServiceImpl.class);

	@Autowired
	private ServiceReviewMapper serviceReviewMapper;
	
	/**
	 * 
	 * 描述：保存语音服务审核记录
	 * @author 李帅
	 * @created 2018年10月25日 上午11:28:47
	 * @since 
	 * @param serviceReviewRecord
	 * @return
	 * @throws BusinessException
	 */
	public Long saveServiceReviewRecord(ServiceReviewRecord serviceReviewRecord) {
		serviceReviewMapper.saveServiceReviewRecord(serviceReviewRecord);
		return serviceReviewRecord.getId();
	}

	/**
	 * 
	 * 描述：获取语音服务审核记录
	 * @author 李帅
	 * @created 2018年10月25日 下午5:31:13
	 * @since 
	 * @param getServiceReviewReq
	 * @return
	 */
    @Override
    public List<ServiceReviewRecord> getServiceReviewRecord(GetServiceReviewReq getServiceReviewReq) {
        return this.serviceReviewMapper.getServiceReviewRecord(getServiceReviewReq);
    }

	/**
	 * @despriction：获取租户id
	 * @author  yeshiyuan
	 * @created 2018/11/2 13:52
	 * @return
	 */
	@Override
	public Long getTenantIdById(Long id) {
		return serviceReviewMapper.getTenantById(id);
	}

	@Override
	public void invalidRecord(Long productId, Long tenantId, Long serviceId) {
		this.serviceReviewMapper.invalidRecord(productId, tenantId, serviceId);
	}
}
