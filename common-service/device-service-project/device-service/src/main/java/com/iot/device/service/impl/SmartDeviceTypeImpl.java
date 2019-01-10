package com.iot.device.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.device.mapper.SmartDeviceTypeMapper;
import com.iot.device.model.SmartDeviceType;
import com.iot.device.service.ISmartDeviceTypeService;
import com.iot.device.vo.req.DataPointReq;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class SmartDeviceTypeImpl extends ServiceImpl<SmartDeviceTypeMapper, SmartDeviceType> implements ISmartDeviceTypeService {

    @Override
    public void saveOrUpdateBatchSmartDeviceType(Long tenantId, Long userId, Long deviceTypeId, List<DataPointReq.SmartWraper> smartList) {
        if (CollectionUtils.isEmpty(smartList)) {
            return;
        }
        List<SmartDeviceType> targetSmartList = Lists.newArrayList();
        smartList.forEach((smart) -> {
            SmartDeviceType targetSmart = new SmartDeviceType();
            if (smart.getId() != null) {
                targetSmart.setId(smart.getId());
                targetSmart.setUpdateBy(userId);
                targetSmart.setUpdateTime(new Date());
            } else {
                targetSmart.setCreateBy(userId);
                targetSmart.setCreateTime(new Date());
            }
            targetSmart.setSmart(smart.getSmart());
            targetSmart.setDeviceTypeId(deviceTypeId);
            targetSmart.setSmartType(smart.getCode());
            targetSmart.setTenantId(tenantId);
            targetSmartList.add(targetSmart);
        });
        super.insertOrUpdateBatch(targetSmartList);
    }
}
