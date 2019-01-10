package com.iot.ifttt.service;

import com.iot.ifttt.entity.IftttApi;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * IFTTT API 配置 服务类
 * </p>
 *
 * @author laiguiming
 * @since 2018-12-19
 */
public interface IIftttApiService extends IService<IftttApi> {

    /**
     * 根据名称获取数据
     * @param name
     * @return
     */
    IftttApi getByName(String name);
}
