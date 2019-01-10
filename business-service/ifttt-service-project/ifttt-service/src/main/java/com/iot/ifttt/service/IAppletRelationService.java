package com.iot.ifttt.service;

import com.iot.ifttt.entity.AppletRelation;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 程序关联表 服务类
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-29
 */
public interface IAppletRelationService extends IService<AppletRelation> {

    /**
     * 获取关联applet
     * @param type
     * @param key
     * @return
     */
    List<Long> getAppletByKey(String type, String key);
}
