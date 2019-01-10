package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.ModuleEventToProperty;
import com.iot.device.vo.req.AddServiceModulePropertyReq;
import com.iot.device.vo.req.ModuleEventToPropertyReq;
import com.iot.device.vo.rsp.ModuleEventToPropertyRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组-事件-to-属性表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
public interface IModuleEventToPropertyService extends IService<ModuleEventToProperty> {

    void delPropertiesByEventId(Long eventId);

    void addOrUpdateEventProperty(Long origEventId, Long targetEventId);

    void addOrUpdateEventProperty(Long origEventId, Long targetEventId, List<AddServiceModulePropertyReq> propertyList);

    void save(ModuleEventToPropertyReq moduleEventToPropertyReq);

    void saveMore(ModuleEventToPropertyReq moduleEventToPropertyReq);

    void delete(ArrayList<Long> ids);

    List<ModuleEventToPropertyRsp> listByModuleEventId(ArrayList<Long> ids);
}
