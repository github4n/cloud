package com.iot.device.service;

import com.iot.common.exception.BusinessException;
import com.iot.device.model.ServiceReviewRecord;
import com.iot.device.vo.req.servicereview.GetServiceReviewReq;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IServiceReviewService {

    /**
     * 
     * 描述：保存语音服务审核记录
     * @author 李帅
     * @created 2018年10月25日 上午11:27:59
     * @since 
     * @param setServiceReviewReq
     * @return
     * @throws BusinessException
     */
    Long saveServiceReviewRecord(ServiceReviewRecord serviceReviewRecord);

    /**
     * 
     * 描述：获取语音服务审核记录
     * @author 李帅
     * @created 2018年10月25日 下午5:31:04
     * @since 
     * @param getServiceReviewReq
     * @return
     */
    List<ServiceReviewRecord> getServiceReviewRecord(GetServiceReviewReq getServiceReviewReq);

    /**
     * @despriction：获取租户id
     * @author  yeshiyuan
     * @created 2018/11/2 13:52
     * @return
     */
    Long getTenantIdById(Long id);

    /**
     * @despriction：记录置为失效
     * @author  wucheng
     * @created 2018/11/14 13:46
     * @return
     */
    void invalidRecord(Long productId, Long tenantId, Long serviceId);
}
