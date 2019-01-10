package com.iot.ifttt.service;

import com.iot.ifttt.entity.Applet;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 应用程序表 服务类
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-28
 */
public interface IAppletService extends IService<Applet> {

    /**
     * 获取状态
     * @param id
     * @return
     */
    String getStatus(Long id);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean delete(Long id);

    /**
     * 修改
     * @param applet
     * @return
     */
    boolean update(Applet applet);
}
