package com.iot.shcs.ifttt.service;

import com.iot.shcs.ifttt.entity.AutomationItem;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 联动子项 服务类
 * </p>
 *
 * @author laiguiming
 * @since 2018-10-15
 */
public interface IAutomationItemService extends IService<AutomationItem> {

    /**
     * 根据条件获取子项主键集
     *
     * @param type
     * @param objectId
     * @return
     */
    List<AutomationItem> getItemByParam(String type, String objectId, Long tenantId);

    /**
     * 根据条件删除子项
     *
     * @param type
     * @param objectId
     * @return
     */
    boolean delItemByParam(String type, String objectId, Long tenantId);

    /**
     * 根据appletId删除子项
     *
     * @param appletId
     * @return
     */
    boolean delItemByAppletId(Long appletId, Long tenantId);


}
