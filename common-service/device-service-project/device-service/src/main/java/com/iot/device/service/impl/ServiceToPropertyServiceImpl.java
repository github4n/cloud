package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.device.core.ServiceModuleCacheCoreUtils;
import com.iot.device.mapper.ServiceToPropertyMapper;
import com.iot.device.model.ServiceModuleProperty;
import com.iot.device.model.ServiceToProperty;
import com.iot.device.service.IServiceModulePropertyService;
import com.iot.device.service.IServiceToPropertyService;
import com.iot.device.vo.req.AddServiceModulePropertyReq;
import com.iot.device.vo.req.ServiceToPropertyReq;
import com.iot.device.vo.rsp.ServiceToPropertyRsp;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 模组-to-属性表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Service
public class ServiceToPropertyServiceImpl extends ServiceImpl<ServiceToPropertyMapper, ServiceToProperty> implements IServiceToPropertyService {


    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceToPropertyServiceImpl.class);

    @Autowired
    private IServiceModulePropertyService serviceModulePropertyService;


    @Transactional
    public void addOrUpdatePropertiesByServiceModuleId(Long origServiceModuleId, Long targetServiceModuleId) {
        ServiceModuleCacheCoreUtils.removeServiceToPropertyByModule(targetServiceModuleId, SaaSContextHolder.currentTenantId());
        if (origServiceModuleId == null) {
            LOGGER.info("origServiceModuleId not null");
            return;
        }
        if (targetServiceModuleId == null) {
            LOGGER.info("targetServiceModuleId not null");
            return;
        }
        EntityWrapper<ServiceToProperty> wrapper = new EntityWrapper<>();
        wrapper.eq("service_module_id", origServiceModuleId);
        List<ServiceToProperty> serviceToPropertyList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(serviceToPropertyList)) {
            LOGGER.info("service-module-to-property not exist");
            return;
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        for (ServiceToProperty sp : serviceToPropertyList) {
            Long origPropertyId = sp.getModulePropertyId();
            ServiceModuleProperty property = serviceModulePropertyService.addOrUpdateProperty(origPropertyId);
            if (property == null) {
                continue;
            }
            Long targetPropertyId = property.getId();
            ServiceToProperty tempServiceToProperty = new ServiceToProperty();
            tempServiceToProperty.setCreateBy(userId);
            tempServiceToProperty.setTenantId(tenantId);
            tempServiceToProperty.setCreateTime(new Date());
            tempServiceToProperty.setModulePropertyId(targetPropertyId);
            tempServiceToProperty.setServiceModuleId(targetServiceModuleId);
            super.insert(tempServiceToProperty);
        }
    }


    @Transactional
    public void delPropertiesByServiceModuleId(Long serviceModuleId) {
        if (serviceModuleId == null) {
            LOGGER.info("serviceModuleId not null");
            return;
        }
        EntityWrapper<ServiceToProperty> wrapper = new EntityWrapper<>();
        wrapper.eq("service_module_id", serviceModuleId);
        List<ServiceToProperty> serviceToPropertyList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(serviceToPropertyList)) {
            LOGGER.info("service-module-to-property not exist");
            return;
        }
        ServiceModuleCacheCoreUtils.removeServiceToPropertyByModule(serviceModuleId, SaaSContextHolder.currentTenantId());
        for (ServiceToProperty sa : serviceToPropertyList) {
            Long propertyId = sa.getModulePropertyId();
            //删除属性
            serviceModulePropertyService.delPropertyByPropertyId(propertyId);
            //删除关系
            super.deleteById(sa.getId());
        }
    }

    @Transactional
    public void addOrUpdatePropertiesByServiceModuleId(Long origServiceModuleId, Long targetServiceModuleId, List<AddServiceModulePropertyReq> propertyList) {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        ServiceModuleCacheCoreUtils.removeServiceToPropertyByModule(targetServiceModuleId, tenantId);
        if (CollectionUtils.isEmpty(propertyList)) {
            return;
        }
        for (AddServiceModulePropertyReq sp : propertyList) {
            ServiceModuleProperty property = serviceModulePropertyService.addOrUpdateProperty(sp);
            if (property == null) {
                continue;
            }
            Long targetPropertyId = property.getId();
            ServiceToProperty tempServiceToProperty = new ServiceToProperty();
            tempServiceToProperty.setCreateBy(userId);
            tempServiceToProperty.setTenantId(tenantId);
            tempServiceToProperty.setCreateTime(new Date());
            tempServiceToProperty.setModulePropertyId(targetPropertyId);
            tempServiceToProperty.setServiceModuleId(targetServiceModuleId);
            super.insert(tempServiceToProperty);
        }
    }

    @Override
    public void save(ServiceToPropertyReq serviceToPropertyReq) {
        ServiceModuleCacheCoreUtils.removeServiceToPropertyByModule(serviceToPropertyReq.getServiceModuleId(), serviceToPropertyReq.getTenantId() == null ? -1L : serviceToPropertyReq.getTenantId());
        ServiceToProperty serviceToProperty = new ServiceToProperty();
        serviceToProperty.setTenantId(serviceToPropertyReq.getTenantId());
        serviceToProperty.setModulePropertyId(serviceToPropertyReq.getModulePropertyId());
        serviceToProperty.setServiceModuleId(serviceToPropertyReq.getServiceModuleId());
        serviceToProperty.setCreateBy(serviceToPropertyReq.getCreateBy());
        serviceToProperty.setCreateTime(new Date());
        serviceToProperty.setUpdateBy(serviceToPropertyReq.getUpdateBy());
        serviceToProperty.setUpdateTime(new Date());
        serviceToProperty.setDescription(serviceToPropertyReq.getDescription());
        serviceToProperty.setStatus(serviceToPropertyReq.getStatus());
        super.insert(serviceToProperty);
    }

    @Override
    public void saveMore(ServiceToPropertyReq serviceToPropertyReq) {
        ServiceModuleCacheCoreUtils.removeServiceToPropertyByModule(serviceToPropertyReq.getServiceModuleId(), serviceToPropertyReq.getTenantId() == null ? -1L : serviceToPropertyReq.getTenantId());
        List<ServiceToProperty> list = new ArrayList<>();
        Map<String, String> map = serviceToPropertyReq.getModulePropertyIds();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            ServiceToProperty serviceToProperty = new ServiceToProperty();
            serviceToProperty.setTenantId(serviceToPropertyReq.getTenantId());
            serviceToProperty.setModulePropertyId(Long.valueOf(entry.getKey()));
            serviceToProperty.setServiceModuleId(serviceToPropertyReq.getServiceModuleId());
            serviceToProperty.setCreateBy(serviceToPropertyReq.getCreateBy());
            serviceToProperty.setCreateTime(new Date());
            serviceToProperty.setUpdateBy(serviceToPropertyReq.getUpdateBy());
            serviceToProperty.setUpdateTime(new Date());
            serviceToProperty.setDescription(serviceToPropertyReq.getDescription());
            serviceToProperty.setStatus(Integer.parseInt(entry.getValue()));
            list.add(serviceToProperty);
        }
        super.insertBatch(list);
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        if (ids.size() > 0 && ids != null) {
            ids.forEach(id -> removeServiceToPropertyStCacheById(id));
            super.deleteBatchIds(ids);
        }
    }

    @Override
    public void update(Long id, Integer status) {
        removeServiceToPropertyStCacheById(id);
        ServiceToProperty serviceToProperty = new ServiceToProperty();
        serviceToProperty.setId(id);
        serviceToProperty.setStatus(status);
        serviceToProperty.setUpdateTime(new Date());
        super.updateById(serviceToProperty);
    }

    public void removeServiceToPropertyStCacheById(Long serviceToPropertyId) {
        ServiceToProperty serviceToProperty = super.selectById(serviceToPropertyId);
        if (serviceToProperty != null) {
            ServiceModuleCacheCoreUtils.removeServiceToPropertyByModule(serviceToProperty.getServiceModuleId(), SaaSContextHolder.currentTenantId());
        }
    }

    @Override
    public List<ServiceToPropertyRsp> listByModulePropertyId(ArrayList<Long> ids) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id", SaaSContextHolder.currentTenantId());
        wrapper.in("module_property_id", ids);
        List<ServiceToProperty> list = super.selectList(wrapper);
        List<ServiceToPropertyRsp> result = new ArrayList();
        list.forEach(m -> {
            ServiceToPropertyRsp serviceToPropertyRsp = new ServiceToPropertyRsp();
            serviceToPropertyRsp.setId(m.getId());
            serviceToPropertyRsp.setTenantId(m.getTenantId());
            serviceToPropertyRsp.setModulePropertyId(m.getModulePropertyId());
            serviceToPropertyRsp.setServiceModuleId(m.getServiceModuleId());
            serviceToPropertyRsp.setCreateBy(m.getCreateBy());
            serviceToPropertyRsp.setCreateTime(m.getCreateTime());
            serviceToPropertyRsp.setUpdateBy(m.getUpdateBy());
            serviceToPropertyRsp.setUpdateTime(m.getUpdateTime());
            serviceToPropertyRsp.setDescription(m.getDescription());
            result.add(serviceToPropertyRsp);
        });
        return result;
    }

    @Override
    public List<ServiceToPropertyRsp> listByServiceModuleId(ArrayList<Long> ids) {
        List<ServiceToPropertyRsp> result = new ArrayList();
        List<ServiceToProperty> serviceToPropertieList = getServiceToPropertyListByServiceModuleId(ids);
        if (serviceToPropertieList != null && serviceToPropertieList.size() > 0) {
            result = serviceToPropertieList.stream().map(serviceToProperty -> {
                ServiceToPropertyRsp serviceToPropertyRsp = transServiceToPropertyRsp(serviceToProperty);
                return serviceToPropertyRsp;
            }).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<ServiceToProperty> getServiceToPropertyListByServiceModuleId(List<Long> serviceModuleIds) {
        List<ServiceToProperty> result = new ArrayList();
        List<Long> noCacheIds = listByServiceModuleIdFromCache(serviceModuleIds, result, SaaSContextHolder.currentTenantId());
        if (noCacheIds != null && noCacheIds.size() > 0) {
            result.addAll(listByServiceModuleIdFromSql(noCacheIds));
        }
        return result;
    }

    private ArrayList<Long> listByServiceModuleIdFromCache(List<Long> ids, List<ServiceToProperty> result, Long tenantId) {
        ArrayList<Long> noCacheIds = new ArrayList<>();
        ids.forEach(serviceModuleId -> {
            List<ServiceToProperty> cacheData = ServiceModuleCacheCoreUtils.getServiceToPropertyCache(tenantId, serviceModuleId);
            if (cacheData != null && cacheData.size() > 0) {
                result.addAll(cacheData);
            } else {

                noCacheIds.add(serviceModuleId);
            }
        });
        return noCacheIds;
    }

    private List<ServiceToProperty> listByServiceModuleIdFromSql(List<Long> ids) {
        /**
         * 数据库查询
         */
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id", SaaSContextHolder.currentTenantId());
        wrapper.in("service_module_id", ids);
        List<ServiceToProperty> list = super.selectList(wrapper);
        /**
         * 数据库查出数据做类型转换并存入redis缓存
         */
        if (list != null && list.size() > 0) {
            Long tenantId = SaaSContextHolder.currentTenantId();

            Map<Long, List<ServiceToProperty>> serviceToPropertyGroup = list.stream().collect(Collectors.groupingBy(ServiceToProperty::getServiceModuleId));
            serviceToPropertyGroup.forEach((k, v) -> ServiceModuleCacheCoreUtils.cacheServiceToPropertyCache(tenantId, k, v));
        }
        return list;
    }

    private ServiceToPropertyRsp transServiceToPropertyRsp(ServiceToProperty m) {
        ServiceToPropertyRsp serviceToPropertyRsp = new ServiceToPropertyRsp();
        serviceToPropertyRsp.setId(m.getId());
        serviceToPropertyRsp.setTenantId(m.getTenantId());
        serviceToPropertyRsp.setModulePropertyId(m.getModulePropertyId());
        serviceToPropertyRsp.setServiceModuleId(m.getServiceModuleId());
        serviceToPropertyRsp.setCreateBy(m.getCreateBy());
        serviceToPropertyRsp.setCreateTime(m.getCreateTime());
        serviceToPropertyRsp.setUpdateBy(m.getUpdateBy());
        serviceToPropertyRsp.setUpdateTime(m.getUpdateTime());
        serviceToPropertyRsp.setDescription(m.getDescription());
        return serviceToPropertyRsp;
    }

}
