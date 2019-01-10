package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.SmartDataPoint;
import com.iot.device.vo.req.voicebox.SearchSmartDataPointReq;
import com.iot.device.vo.req.voicebox.SmartDataPointReq;
import com.iot.device.vo.rsp.voicebox.SmartDataPointResp;

import java.util.List;

public interface ISmartDataPointService extends IService<SmartDataPoint>  {

	boolean updateBatchByIdAndDataPointId(List<SmartDataPoint> upss);

    boolean insertBatchDataPoint(List<SmartDataPoint> dataPointList);

    boolean delBatchDataPoint(List<Long> dels);

    SmartDataPointResp getSmartDataPoint(Long tenantId, Long propertyId, Integer smart);

    List<SmartDataPoint> findSmartDataPointList(Long tenantId, Long propertyId);

    List<SmartDataPoint> findBySmartDataPointReq(SearchSmartDataPointReq req);

    void copySmartDataPoint(Long parentPropertyId, Long newPropertyId);

    void delByPropertyIdAndTenantId(Long propertyId, Long tenantId);

    void delByPropertyIds(List<Long> propertyIds);

    void createSmartDataPoint(List<SmartDataPointReq> smartList, Long tenantId, Long propertyId, Long createBy);
}
