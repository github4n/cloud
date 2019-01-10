package com.iot.smarthome.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.smarthome.mapper.ThirdPartyInfoMapper;
import com.iot.smarthome.model.ThirdPartyInfo;
import com.iot.smarthome.service.IThirdPartyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/13 9:06
 * @Modify by:
 */

@Service
public class ThirdPartyInfoServiceImpl extends ServiceImpl<ThirdPartyInfoMapper, ThirdPartyInfo> implements IThirdPartyInfoService {
    @Autowired
    private ThirdPartyInfoMapper thirdPartyInfoMapper;
}
