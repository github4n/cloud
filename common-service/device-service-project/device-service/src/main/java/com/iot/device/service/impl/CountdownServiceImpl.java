package com.iot.device.service.impl;

import com.iot.device.model.Countdown;
import com.iot.device.mapper.CountdownMapper;
import com.iot.device.service.ICountdownService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHQ
 * @since 2018-04-28
 */
@Service
public class CountdownServiceImpl extends ServiceImpl<CountdownMapper, Countdown> implements ICountdownService {

}
