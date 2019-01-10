package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.SmartDeviceType;
import com.iot.device.vo.req.DataPointReq;

import java.util.List;

public interface ISmartDeviceTypeService extends IService<SmartDeviceType> {

    void saveOrUpdateBatchSmartDeviceType(Long tenantId, Long userId, Long deviceTypeId, List<DataPointReq.SmartWraper> smartList);
}
