package com.iot.ifttt.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.ifttt.entity.AppletItem;

import java.util.List;

/**
 * <p>
 * 子规则表 服务类
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-29
 */
public interface IAppletItemService extends IService<AppletItem> {

    /**
     * 获取item
     *
     * @param eventId
     * @return
     */
    List<AppletItem> getByEventId(Long eventId, String type);

    /**
     * 根据appletId 批量删除
     *
     * @param eventId
     * @return
     */
    boolean deleteByEventId(Long eventId, String type);


    /**
     * 根据itemId获取appletId
     *
     * @param itemId
     * @return
     */
    Long getAppletIdByItem(Long itemId);

    /**
     * 根据主键删除
     *
     * @param itemId
     * @return
     */
    boolean deleteByItemId(Long itemId);
}
