package com.iot.tenant.service.impl;

import com.iot.tenant.domain.AppProductInfo;
import com.iot.tenant.mapper.AppProductInfoMapper;
import com.iot.tenant.service.IAppProductInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * app关联产品配网信息 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-07-09
 */
@Service
public class AppProductInfoServiceImpl extends ServiceImpl<AppProductInfoMapper, AppProductInfo> implements IAppProductInfoService {

}
