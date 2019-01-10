package com.iot.device.service.impl;

import com.iot.device.mapper.DeviceTypeToGoodsMapper;
import com.iot.device.model.DeviceTypeToGoods;
import com.iot.device.service.IDeviceTypeToGoodsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备类型增值服务关联表 服务实现类
 * </p>
 *
 * @author wangxi
 * @since 2018-12-25
 */
@Service
public class DeviceTypeToGoodsServiceImpl extends ServiceImpl<DeviceTypeToGoodsMapper, DeviceTypeToGoods> implements IDeviceTypeToGoodsService {

}
