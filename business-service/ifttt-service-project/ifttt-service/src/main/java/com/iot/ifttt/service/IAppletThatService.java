package com.iot.ifttt.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.ifttt.entity.AppletThat;

import java.util.List;

/**
 * <p>
 * that组表 服务类
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-29
 */
public interface IAppletThatService extends IService<AppletThat> {

    List<AppletThat> getByAppletIdFromDB(Long appletId);

    /**
     * 获取this
     * @param appletId
     * @return
     */
    List<AppletThat> getByAppletId(Long appletId);

    /**
     * 根据appletId 批量删除
     * @param appletId
     * @return
     */
    boolean deleteByAppletId(Long appletId);

}
