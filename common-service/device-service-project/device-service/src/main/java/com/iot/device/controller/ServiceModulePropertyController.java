package com.iot.device.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.device.api.ServicePropertyApi;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.model.ModuleActionToProperty;
import com.iot.device.model.ModuleEventToProperty;
import com.iot.device.model.ServiceModuleProperty;
import com.iot.device.model.ServiceToProperty;
import com.iot.device.service.IModuleActionToPropertyService;
import com.iot.device.service.IModuleEventToPropertyService;
import com.iot.device.service.IServiceModuleActionService;
import com.iot.device.service.IServiceModuleEventService;
import com.iot.device.service.IServiceModulePropertyService;
import com.iot.device.service.IServiceModuleService;
import com.iot.device.service.IServiceToPropertyService;
import com.iot.device.vo.req.ServiceModulePropertyReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.device.vo.rsp.product.ParentVO;
import com.iot.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 模组-属性表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@RestController
public class ServiceModulePropertyController implements ServicePropertyApi {

    @Autowired
    private IServiceModuleService serviceModuleService;

    @Autowired
    private IServiceModuleActionService serviceModuleActionService;

    @Autowired
    private IServiceModuleEventService serviceModuleEventService;

    @Autowired
    private IServiceModulePropertyService serviceModulePropertyService;

    @Autowired
    private IServiceToPropertyService serviceToPropertyService;

    @Autowired
    private IModuleActionToPropertyService moduleActionToPropertyService;

    @Autowired
    private IModuleEventToPropertyService moduleEventToPropertyService;

    public List<ServiceModulePropertyResp> findPropertyListByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
//        ServiceModule serviceModule = serviceModuleService.selectById(serviceModuleId);
//        if (serviceModule == null) {
//            throw new BusinessException(ServiceModuleExceptionEnum.SERVICE_MODULE_NOT_EXIST);
//        }
        EntityWrapper<ServiceToProperty> spWrapper = new EntityWrapper<>();
        spWrapper.eq("service_module_id", serviceModuleId);
        List<ServiceToProperty> serviceToPropertyList = serviceToPropertyService.selectList(spWrapper);
        if (CollectionUtils.isEmpty(serviceToPropertyList)) {
            return null;
        }
        Map<Long, ServiceToProperty> serviceToPropertyMap = Maps.newHashMap();
        List<Long> propertyIds = Lists.newArrayList();
        for (ServiceToProperty sp : serviceToPropertyList) {
            Long propertyId = sp.getModulePropertyId();
            propertyIds.add(propertyId);
            serviceToPropertyMap.put(propertyId, sp);
        }
        EntityWrapper<ServiceModuleProperty> propertyWrapper = new EntityWrapper<>();
        propertyWrapper.in("id", propertyIds);
        List<ServiceModuleProperty> propertyList = serviceModulePropertyService.selectList(propertyWrapper);
        if (CollectionUtils.isEmpty(propertyList)) {
            return null;
        }
        List<ServiceModulePropertyResp> respList = Lists.newArrayList();
        List<Long> parentPropIds = Lists.newArrayList();
        for (ServiceModuleProperty property : propertyList) {
            parentPropIds.add(property.getParentId());
            ServiceModulePropertyResp target = new ServiceModulePropertyResp();
            BeanCopyUtils.copyModuleProperty(property, target);
            //可选必选
            ServiceToProperty serviceToProperty = serviceToPropertyMap.get(property.getId());
            if (serviceToProperty!=null){
                //    propertyStatus 属性状态，0:可选,1:必选")
                target.setPropertyStatus(serviceToProperty.getStatus());
            }
            String allowedValues = serviceModulePropertyService.getPropertyLangInfo(property);
            target.setAllowedValues(allowedValues);
            respList.add(target);
        }
        List<ServiceModuleProperty> serviceModuleProperties = serviceModulePropertyService.selectBatchIds(parentPropIds);
        for (ServiceModulePropertyResp serviceModulePropertyResp : respList) {
            for (ServiceModuleProperty parentProp : serviceModuleProperties) {
                if (serviceModulePropertyResp.getParentId() != null && serviceModulePropertyResp.getParentId().equals(parentProp.getId())) {
                    ParentVO parentVO = new ParentVO();
                    parentVO.setId(parentProp.getId());
                    parentVO.setCode(parentProp.getCode());
                    parentVO.setName(parentProp.getName());
                    serviceModulePropertyResp.setParent(parentVO);
                }
            }
        }
        return respList;
    }

    @Override
    public List<ServiceModulePropertyResp> findPropertyListByServiceModuleIds(@RequestBody List<Long> serviceModuleIds) {
        EntityWrapper<ServiceToProperty> spWrapper = new EntityWrapper<>();
        spWrapper.in("service_module_id", serviceModuleIds);
        List<ServiceToProperty> serviceToPropertyList = serviceToPropertyService.selectList(spWrapper);
        if (CollectionUtils.isEmpty(serviceToPropertyList)) {
            return null;
        }
        Map<Long, ServiceToProperty> serviceToPropertyMap = Maps.newHashMap();
        List<Long> propertyIds = Lists.newArrayList();
        for (ServiceToProperty sp : serviceToPropertyList) {
            Long propertyId = sp.getModulePropertyId();
            propertyIds.add(propertyId);
            serviceToPropertyMap.put(propertyId, sp);
        }
        EntityWrapper<ServiceModuleProperty> propertyWrapper = new EntityWrapper<>();
        propertyWrapper.in("id", propertyIds);
        List<ServiceModuleProperty> propertyList = serviceModulePropertyService.selectList(propertyWrapper);
        if (CollectionUtils.isEmpty(propertyList)) {
            return null;
        }
        List<ServiceModulePropertyResp> respList = Lists.newArrayList();
        List<Long> parentPropIds = Lists.newArrayList();
        for (ServiceModuleProperty property : propertyList) {
            parentPropIds.add(property.getParentId());
            ServiceModulePropertyResp target = new ServiceModulePropertyResp();
            BeanCopyUtils.copyModuleProperty(property, target);
            //可选必选
            ServiceToProperty serviceToProperty = serviceToPropertyMap.get(property.getId());
            if (serviceToProperty!=null){
                //    propertyStatus 属性状态，0:可选,1:必选")
                target.setPropertyStatus(serviceToProperty.getStatus());
            }
//            String allowedValues = serviceModulePropertyService.getPropertyLangInfo(property);
//            target.setAllowedValues(allowedValues);
            respList.add(target);
        }
//        List<ServiceModuleProperty> serviceModuleProperties = serviceModulePropertyService.selectBatchIds(parentPropIds);
//        for (ServiceModulePropertyResp serviceModulePropertyResp : respList) {
//            for (ServiceModuleProperty parentProp : serviceModuleProperties) {
//                if (serviceModulePropertyResp.getParentId() != null && serviceModulePropertyResp.getParentId().equals(parentProp.getId())) {
//                    ParentVO parentVO = new ParentVO();
//                    parentVO.setId(parentProp.getId());
//                    parentVO.setCode(parentProp.getCode());
//                    parentVO.setName(parentProp.getName());
//                    serviceModulePropertyResp.setParent(parentVO);
//                }
//            }
//        }
        return respList;
    }

    /**
     * 通过方法id获取属性列表
     *
     * @param actionId
     * @return
     * @author lucky
     * @date 2018/6/29 17:09
     */
    public List<ServiceModulePropertyResp> findPropertyListByActionId(@RequestParam("actionId") Long actionId) {
        AssertUtils.notNull(actionId, "actionId.notnull");
//        ServiceModuleAction action = serviceModuleActionService.selectById(actionId);
//        if (action == null) {
//            throw new BusinessException(ServiceModuleExceptionEnum.MODULE_ACTION_NOT_EXIST);
//        }

        EntityWrapper<ModuleActionToProperty> saWrapper = new EntityWrapper<>();
        saWrapper.eq("module_action_id", actionId);
        List<ModuleActionToProperty> actionToPropertyList = moduleActionToPropertyService.selectList(saWrapper);
        if (CollectionUtils.isEmpty(actionToPropertyList)) {
            return null;
        }
        Map<Long, ModuleActionToProperty> moduleActionToPropertyMap = Maps.newHashMap();
        List<Long> propertyIds = Lists.newArrayList();
        for (ModuleActionToProperty ap : actionToPropertyList) {
            Long propertyId = ap.getModulePropertyId();
            propertyIds.add(propertyId);
            moduleActionToPropertyMap.put(propertyId, ap);
        }
        EntityWrapper<ServiceModuleProperty> propertyWrapper = new EntityWrapper<>();
        propertyWrapper.in("id", propertyIds);
        List<ServiceModuleProperty> propertyList = serviceModulePropertyService.selectList(propertyWrapper);
        if (CollectionUtils.isEmpty(propertyList)) {
            return null;
        }
        List<ServiceModulePropertyResp> respList = Lists.newArrayList();
        for(ModuleActionToProperty ap : actionToPropertyList) {
            ServiceModulePropertyResp target = new ServiceModulePropertyResp();
            for (ServiceModuleProperty property : propertyList) {
                if (property.getId().compareTo(ap.getModulePropertyId()) == 0){
                    BeanCopyUtils.copyModuleProperty(property, target);
                    target.setPropertyParamType(ap.getParamType());
                    // 多语言处理
                    String allowedValues = serviceModulePropertyService.getPropertyLangInfo(property);
                    target.setAllowedValues(allowedValues);
                    //可选必选
                    ModuleActionToProperty moduleActionToProperty = moduleActionToPropertyMap.get(property.getId());
                    if (moduleActionToProperty !=null ) {
                        //    propertyStatus 属性状态，0:可选,1:必选")
                        target.setPropertyStatus(moduleActionToProperty.getStatus());
                    }

                    break;
                }
            }
            respList.add(target);
        }
        return respList;
    }

    public List<ServiceModulePropertyResp> findPropertyListByEventId(@RequestParam("eventId") Long eventId) {
        AssertUtils.notNull(eventId, "eventId.notnull");
//        ServiceModuleEvent event = serviceModuleEventService.selectById(eventId);
//        if (event == null) {
//            throw new BusinessException(ServiceModuleExceptionEnum.MODULE_EVENT_NOT_EXIST);
//        }
        EntityWrapper<ModuleEventToProperty> epWrapper = new EntityWrapper<>();
        epWrapper.eq("module_event_id", eventId);
        List<ModuleEventToProperty> moduleEventToPropertyList = moduleEventToPropertyService.selectList(epWrapper);
        if (CollectionUtils.isEmpty(moduleEventToPropertyList)) {
            return null;
        }
        Map<Long, ModuleEventToProperty> moduleEventToPropertyMap = Maps.newHashMap();
        List<Long> propertyIds = Lists.newArrayList();
        for (ModuleEventToProperty ep : moduleEventToPropertyList) {
            Long propertyId = ep.getEventPropertyId();
            propertyIds.add(propertyId);
            moduleEventToPropertyMap.put(propertyId, ep);
        }
        EntityWrapper<ServiceModuleProperty> propertyWrapper = new EntityWrapper<>();
        propertyWrapper.in("id", propertyIds);
        List<ServiceModuleProperty> propertyList = serviceModulePropertyService.selectList(propertyWrapper);
        if (CollectionUtils.isEmpty(propertyList)) {
            return null;
        }
        List<ServiceModulePropertyResp> respList = Lists.newArrayList();
        for (ServiceModuleProperty property : propertyList) {
            ServiceModulePropertyResp target = new ServiceModulePropertyResp();
            BeanCopyUtils.copyModuleProperty(property, target);
            String allowedValues = serviceModulePropertyService.getPropertyLangInfo(property);
            target.setAllowedValues(allowedValues);
            //可选必选
            ModuleEventToProperty moduleEventToProperty = moduleEventToPropertyMap.get(property.getId());
            if (moduleEventToProperty!=null){
                //    propertyStatus 属性状态，0:可选,1:必选")
                target.setPropertyStatus(moduleEventToProperty.getStatus());
            }
            respList.add(target);
        }
        return respList;
    }

    public ServiceModulePropertyResp getPropertyInfoByPropertyId(@RequestParam("propertyId") Long propertyId) {
        AssertUtils.notNull(propertyId, "propertyId.notnull");

        ServiceModuleProperty property = serviceModulePropertyService.get(propertyId);
        if (property == null) {
            return null;
        }
        ServiceModulePropertyResp target = new ServiceModulePropertyResp();
        BeanCopyUtils.copyModuleProperty(property, target);
        String allowedValues = serviceModulePropertyService.getPropertyLangInfo(property);
        target.setAllowedValues(allowedValues);
        return target;
    }

    public List<ServiceModulePropertyResp> listPropertyInfoByPropertyIds(@RequestBody List<Long> ids) {
        List<ServiceModulePropertyResp> result = Lists.newArrayList();
        List<ServiceModuleProperty> properties = serviceModulePropertyService.selectBatchIds(ids);
        if (CollectionUtils.isEmpty(properties)) {
            return result;
        }
        for (ServiceModuleProperty property : properties) {
            ServiceModulePropertyResp target = new ServiceModulePropertyResp();
            BeanCopyUtils.copyModuleProperty(property, target);
            String allowedValues = serviceModulePropertyService.getPropertyLangInfo(property);
            target.setAllowedValues(allowedValues);
            result.add(target);
        }
        return result;
    }

    @Override
    public Long saveOrUpdate(@RequestBody ServiceModulePropertyReq serviceModulePropertyReq) {
        return serviceModulePropertyService.saveOrUpdate(serviceModulePropertyReq);
    }

    @Override
    public List<Long> saveOrUpdateBatch(@RequestBody List<ServiceModulePropertyReq> serviceModulePropertyReqs) {
        return serviceModulePropertyService.saveOrUpdateBatch(serviceModulePropertyReqs);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        serviceModulePropertyService.delete(ids);
    }

    @Override
    public PageInfo list(@RequestBody ServiceModulePropertyReq serviceModulePropertyReq) {
        return serviceModulePropertyService.list(serviceModulePropertyReq);
    }

    @Override
    public List<ServiceModulePropertyResp> listByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId) {
        return serviceModulePropertyService.listByServiceModuleId(serviceModuleId);
    }

    @Override
    public List<ServiceModulePropertyResp> listByActionModuleId(@RequestParam("actionModuleId") Long actionModuleId) {
        return serviceModulePropertyService.listByActionModuleId(actionModuleId);
    }

    @Override
    public List<ServiceModulePropertyResp> listByEventModuleId(@RequestParam("eventModuleId") Long eventModuleId) {
        return serviceModulePropertyService.listByEventModuleId(eventModuleId);
    }

    /**
     * @despriction：查找支持联动配置的属性
     * @author  yeshiyuan
     * @created 2018/10/23 13:55
     * @return
     */
    @Override
    public ModuleSupportIftttResp findSupportIftttProperties(@RequestParam("serviceModuleIds") List<Long> serviceModuleIds, @RequestParam("tenantId") Long tenantId) {
        return serviceModulePropertyService.findSupportIftttPropertys(serviceModuleIds, tenantId);
    }

    /**
     * @despriction：修改模组属性联动属性（portal使用）
     * @author  yeshiyuan
     * @created 2018/10/23 17:19
     * @return
     */
    @Override
    public int updatePropertySupportIfttt(@RequestBody UpdateModuleSupportIftttReq req) {
        return serviceModulePropertyService.updatePropertySupportIfttt(req);
    }
}

