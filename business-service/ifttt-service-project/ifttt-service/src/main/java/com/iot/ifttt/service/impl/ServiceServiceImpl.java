package com.iot.ifttt.service.impl;

import com.iot.ifttt.entity.Service;
import com.iot.ifttt.mapper.ServiceMapper;
import com.iot.ifttt.service.IServiceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-27
 */
@org.springframework.stereotype.Service
public class ServiceServiceImpl extends ServiceImpl<ServiceMapper, Service> implements IServiceService {

}
