package com.iot.ifttt.service.impl;

import com.google.common.collect.Maps;
import com.iot.ifttt.entity.IftttApi;
import com.iot.ifttt.mapper.IftttApiMapper;
import com.iot.ifttt.service.IIftttApiService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * IFTTT API 配置 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-12-19
 */
@Service
public class IftttApiServiceImpl extends ServiceImpl<IftttApiMapper, IftttApi> implements IIftttApiService {

    @Override
    public IftttApi getByName(String name) {
        IftttApi data = null;
        Map<String, Object> params = Maps.newHashMap();
        params.put("name", name);
        List<IftttApi> list = selectByMap(params);
        if (CollectionUtils.isNotEmpty(list)) {
            data = list.get(0);
        }
        return data;
    }
}
