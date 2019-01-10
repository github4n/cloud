package com.iot.ifttt.service;

import com.iot.ifttt.entity.AppletThis;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * this组表 服务类
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-29
 */
public interface IAppletThisService extends IService<AppletThis> {

    List<AppletThis> getByAppletIdFromDB(Long appletId);

    /**
     * 获取this
     * @param appletId
     * @return
     */
    List<AppletThis> getByAppletId(Long appletId);

    /**
     * 根据appletId 批量删除
     * @param appletId
     * @return
     */
    boolean deleteByAppletId(Long appletId);
}
