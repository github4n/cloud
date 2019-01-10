package com.iot.tenant.service.impl;

import com.iot.tenant.domain.AppVersion;
import com.iot.tenant.mapper.AppVersionMapper;
import com.iot.tenant.service.IAppVersionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用版本记录 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-12
 */
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements IAppVersionService {

}
