package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.ModuleActionToProperty;
import com.iot.device.vo.req.AddServiceModulePropertyReq;
import com.iot.device.vo.req.ModuleActionToPropertyReq;
import com.iot.device.vo.rsp.ModuleActionToPropertyRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组-方法-to-参数表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
public interface IModuleActionToPropertyService extends IService<ModuleActionToProperty> {

    void addOrUpdatePropertiesByActionId(Long origActionId, Long targetActionId);

    void delPropertiesByActionId(Long actionId);

    //添加参数
    void addOrUpdatePropertiesByActionId(Long origActionId, Long targetActionId, List<AddServiceModulePropertyReq> propertyList);

    void save(ModuleActionToPropertyReq moduleActionToPropertyReq);

    void saveMore(ModuleActionToPropertyReq moduleActionToPropertyReq);

    void delete(ArrayList<Long> ids);

    List<ModuleActionToPropertyRsp> listByModuleActionId(ArrayList<Long> ids);

    List<ModuleActionToPropertyRsp> listByModuleActionIdAndModulePropertyId(Long moduleActionId, Long modulePropertyId);

}
