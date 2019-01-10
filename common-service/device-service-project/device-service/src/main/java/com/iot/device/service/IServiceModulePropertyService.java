package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.iot.device.model.ServiceModuleProperty;
import com.iot.device.vo.req.AddServiceModulePropertyReq;
import com.iot.device.vo.req.ServiceModulePropertyReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组-属性表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
public interface IServiceModulePropertyService extends IService<ServiceModuleProperty> {

    void delPropertyByPropertyId(Long propertyId);

    ServiceModuleProperty addOrUpdateProperty(Long origPropertyId);

    ServiceModuleProperty addOrUpdateProperty(AddServiceModulePropertyReq property);

    Long saveOrUpdate(ServiceModulePropertyReq serviceModulePropertyReq);

    List<Long> saveOrUpdateBatch(List<ServiceModulePropertyReq> serviceModulePropertyReqs);

    void delete(ArrayList<Long> ids);

    PageInfo list(ServiceModulePropertyReq serviceModulePropertyReq);

    List<ServiceModulePropertyResp> listByServiceModuleId(Long serviceModuleId);

    List<ServiceModulePropertyResp> listByActionModuleId(Long actionModuleId);

    List<ServiceModulePropertyResp> listByEventModuleId(Long eventModueId);

    /**
     * @despriction：查找支持联动配置的属性
     * @author  yeshiyuan
     * @created 2018/10/23 13:55
     * @return
     */
    ModuleSupportIftttResp findSupportIftttPropertys(List<Long> serviceModuleIds, Long tenantId);

    /**
     * @despriction：修改模组属性联动属性（portal使用）
     * @author  yeshiyuan
     * @created 2018/10/23 17:19
     * @return
     */
    int updatePropertySupportIfttt(UpdateModuleSupportIftttReq req);

    ServiceModuleProperty get(Long serviceModulePropertyId);

    String getPropertyLangInfo(ServiceModuleProperty prop);
}
