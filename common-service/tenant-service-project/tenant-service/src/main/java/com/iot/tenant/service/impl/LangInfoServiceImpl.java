package com.iot.tenant.service.impl;

import com.iot.tenant.domain.LangInfo;
import com.iot.tenant.mapper.LangInfoMapper;
import com.iot.tenant.service.ILangInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 多语言管理 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-07-09
 */
@Service
public class LangInfoServiceImpl extends ServiceImpl<LangInfoMapper, LangInfo> implements ILangInfoService {

}
