package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.ServiceToProperty;
import com.iot.device.vo.req.AddServiceModulePropertyReq;
import com.iot.device.vo.req.ServiceToPropertyReq;
import com.iot.device.vo.rsp.ServiceToPropertyRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组-to-属性表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
public interface IServiceToPropertyService extends IService<ServiceToProperty> {

    void addOrUpdatePropertiesByServiceModuleId(Long origServiceModuleId, Long targetServiceModuleId);

    void delPropertiesByServiceModuleId(Long serviceModuleId);

    void addOrUpdatePropertiesByServiceModuleId(Long origServiceModuleId, Long targetServiceModuleId, List<AddServiceModulePropertyReq> propertyList);

    void save(ServiceToPropertyReq serviceToPropertyReq);

    void saveMore(ServiceToPropertyReq serviceToPropertyReq);

    void delete(ArrayList<Long> ids);

    List<ServiceToPropertyRsp> listByModulePropertyId(ArrayList<Long> ids);

    List<ServiceToPropertyRsp> listByServiceModuleId(ArrayList<Long> ids);

    void update(Long id,Integer status);

    List<ServiceToProperty> getServiceToPropertyListByServiceModuleId(List<Long> serviceModuleIds);
}
