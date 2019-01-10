package com.iot.smarthome.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.smarthome.mapper.DeviceClassifyProductXrefMapper;
import com.iot.smarthome.model.DeviceClassifyProductXref;
import com.iot.smarthome.service.IDeviceClassifyProductXrefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/13 9:06
 * @Modify by:
 */

@Service
public class DeviceClassifyProductXrefServiceImpl extends ServiceImpl<DeviceClassifyProductXrefMapper, DeviceClassifyProductXref> implements IDeviceClassifyProductXrefService {
    @Autowired
    private DeviceClassifyProductXrefMapper deviceClassifyProductXrefMapper;
}
