package com.iot.ifttt.service;

import com.iot.ifttt.entity.IftttDevice;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * IFTTT 关联设备 配置 服务类
 * </p>
 *
 * @author laiguiming
 * @since 2018-12-19
 */
public interface IIftttDeviceService extends IService<IftttDevice> {

    /**
     * 根据api 获取设备配置
     * @param id
     * @param tenantId
     * @return
     */
    List<IftttDevice> getListByApi(Long id,Long tenantId);
}
