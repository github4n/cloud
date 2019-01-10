package com.iot.device.service.impl;

import com.iot.device.model.ProductDataPoint;
import com.iot.device.mapper.ProductDataPointMapper;
import com.iot.device.service.IProductDataPointService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
@Service
public class ProductDataPointServiceImpl extends ServiceImpl<ProductDataPointMapper, ProductDataPoint> implements IProductDataPointService {
	
}
