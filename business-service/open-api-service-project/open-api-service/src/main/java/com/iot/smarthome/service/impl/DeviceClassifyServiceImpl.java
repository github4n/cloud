package com.iot.smarthome.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.smarthome.mapper.DeviceClassifyMapper;
import com.iot.smarthome.model.DeviceClassify;
import com.iot.smarthome.service.IDeviceClassifyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/13 9:06
 * @Modify by:
 */

@Service
public class DeviceClassifyServiceImpl extends ServiceImpl<DeviceClassifyMapper, DeviceClassify> implements IDeviceClassifyService {
    @Autowired
    private DeviceClassifyMapper deviceClassifyMapper;


    /**
     *  获取产品归属的分类
     * @param productId
     * @return
     */
    public DeviceClassify getDeviceClassifyByProductId(Long productId) {
        return deviceClassifyMapper.getDeviceClassifyByProductId(productId);
    }

    /**
     *  根据 分类编码 获取分类
     * @param typeCode
     * @return
     */
    public DeviceClassify getByTypeCode(String typeCode) {
        if (StringUtils.isBlank(typeCode)) {
            return null;
        }

        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.eq("type_code", typeCode);
        DeviceClassify deviceClassify = super.selectOne(wrapper);
        return deviceClassify;
    }
}
