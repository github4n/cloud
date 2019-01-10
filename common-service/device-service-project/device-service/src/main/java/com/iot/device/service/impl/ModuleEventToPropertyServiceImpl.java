package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.device.mapper.ModuleEventToPropertyMapper;
import com.iot.device.model.ModuleEventToProperty;
import com.iot.device.model.ServiceModuleProperty;
import com.iot.device.service.IModuleEventToPropertyService;
import com.iot.device.service.IServiceModulePropertyService;
import com.iot.device.vo.req.AddServiceModulePropertyReq;
import com.iot.device.vo.req.ModuleEventToPropertyReq;
import com.iot.device.vo.rsp.ModuleEventToPropertyRsp;
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

/**
 * <p>
 * 模组-事件-to-属性表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Service
public class ModuleEventToPropertyServiceImpl extends ServiceImpl<ModuleEventToPropertyMapper, ModuleEventToProperty> implements IModuleEventToPropertyService {


    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleEventToPropertyServiceImpl.class);

    @Autowired
    private IServiceModulePropertyService serviceModulePropertyService;

    @Transactional
    public void delPropertiesByEventId(Long eventId) {
        if (eventId == null) {
            LOGGER.info("eventId not null");
            return;
        }
        EntityWrapper<ModuleEventToProperty> wrapper = new EntityWrapper<>();
        wrapper.eq("module_event_id", eventId);
        List<ModuleEventToProperty> moduleEventToPropertyList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(moduleEventToPropertyList)) {
            LOGGER.info("service-event-to-property not exist");
            return;
        }
        for (ModuleEventToProperty ep : moduleEventToPropertyList) {
            Long propertyId = ep.getEventPropertyId();
            serviceModulePropertyService.delPropertyByPropertyId(propertyId);
            //删除关系
            super.deleteById(ep.getId());
        }
    }

    @Transactional
    public void addOrUpdateEventProperty(Long origEventId, Long targetEventId) {
        if (origEventId == null) {
            LOGGER.info("origEventId not null");
            return;
        }
        if (targetEventId == null) {
            LOGGER.info("targetEventId not null");
            return;
        }
        EntityWrapper<ModuleEventToProperty> wrapper = new EntityWrapper<>();
        wrapper.eq("module_event_id", origEventId);
        List<ModuleEventToProperty> moduleEventToPropertyList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(moduleEventToPropertyList)) {
            LOGGER.info("service-event-to-property not exist");
            return;
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        for (ModuleEventToProperty ep : moduleEventToPropertyList) {
            Long origPropertyId = ep.getEventPropertyId();
            ServiceModuleProperty targetProperty = serviceModulePropertyService.addOrUpdateProperty(origPropertyId);
            if (targetProperty == null) {
                continue;
            }
            Long targetPropertyId = targetProperty.getId();
            ModuleEventToProperty tempEventProperty = new ModuleEventToProperty();
            tempEventProperty.setCreateBy(userId);
            tempEventProperty.setTenantId(tenantId);
            tempEventProperty.setModuleEventId(targetEventId);
            tempEventProperty.setEventPropertyId(targetPropertyId);
            tempEventProperty.setCreateTime(new Date());
            super.insert(tempEventProperty);
        }

    }

    @Transactional
    public void addOrUpdateEventProperty(Long origEventId, Long targetEventId, List<AddServiceModulePropertyReq> propertyList) {
        if (CollectionUtils.isEmpty(propertyList)) {
            return;
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        for (AddServiceModulePropertyReq ep : propertyList) {
            ServiceModuleProperty targetProperty = serviceModulePropertyService.addOrUpdateProperty(ep);
            if (targetProperty == null) {
                continue;
            }
            Long targetPropertyId = targetProperty.getId();
            ModuleEventToProperty tempEventProperty = new ModuleEventToProperty();
            tempEventProperty.setCreateBy(userId);
            tempEventProperty.setTenantId(tenantId);
            tempEventProperty.setModuleEventId(targetEventId);
            tempEventProperty.setEventPropertyId(targetPropertyId);
            tempEventProperty.setCreateTime(new Date());
            super.insert(tempEventProperty);
        }
    }

    @Override
    public void save(ModuleEventToPropertyReq moduleEventToPropertyReq) {
        ModuleEventToProperty moduleEventToProperty = new ModuleEventToProperty();
        moduleEventToProperty.setId(moduleEventToPropertyReq.getId());
        moduleEventToProperty.setTenantId(moduleEventToPropertyReq.getTenantId());
        moduleEventToProperty.setModuleEventId(moduleEventToPropertyReq.getModuleEventId());
        moduleEventToProperty.setEventPropertyId(moduleEventToPropertyReq.getEventPropertyId());
        moduleEventToProperty.setCreateBy(moduleEventToPropertyReq.getCreateBy());
        moduleEventToProperty.setCreateTime(new Date());
        moduleEventToProperty.setUpdateBy(moduleEventToPropertyReq.getUpdateBy());
        moduleEventToProperty.setUpdateTime(new Date());
        moduleEventToProperty.setDescription(moduleEventToPropertyReq.getDescription());
        super.insert(moduleEventToProperty);
    }

    @Override
    public void saveMore(ModuleEventToPropertyReq moduleEventToPropertyReq) {
        List<ModuleEventToProperty> list = new ArrayList<>();
        moduleEventToPropertyReq.getEventPropertyIds().forEach(m->{
            ModuleEventToProperty moduleEventToProperty = new ModuleEventToProperty();
            moduleEventToProperty.setTenantId(moduleEventToPropertyReq.getTenantId());
            moduleEventToProperty.setModuleEventId(moduleEventToPropertyReq.getModuleEventId());
            moduleEventToProperty.setEventPropertyId(Long.valueOf(m.toString()));
            moduleEventToProperty.setCreateBy(moduleEventToPropertyReq.getCreateBy());
            moduleEventToProperty.setCreateTime(new Date());
            moduleEventToProperty.setUpdateBy(moduleEventToPropertyReq.getUpdateBy());
            moduleEventToProperty.setUpdateTime(new Date());
            moduleEventToProperty.setDescription(moduleEventToPropertyReq.getDescription());
            list.add(moduleEventToProperty);
        });
        super.insertBatch(list);
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        if (ids.size()>0 && ids!=null) {
            super.deleteBatchIds(ids);
        }
    }

    @Override
    public List<ModuleEventToPropertyRsp> listByModuleEventId(ArrayList<Long> ids) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id",SaaSContextHolder.currentTenantId());
        wrapper.in("module_event_id",ids);
        List<ModuleEventToProperty> list = super.selectList(wrapper);
        List<ModuleEventToPropertyRsp> result = new ArrayList();
        list.forEach(m->{
            ModuleEventToPropertyRsp moduleEventToPropertyRsp = new ModuleEventToPropertyRsp();
            moduleEventToPropertyRsp.setId(m.getId());
            moduleEventToPropertyRsp.setTenantId(m.getTenantId());
            moduleEventToPropertyRsp.setModuleEventId(m.getModuleEventId());
            moduleEventToPropertyRsp.setEventPropertyId(m.getEventPropertyId());
            moduleEventToPropertyRsp.setCreateBy(m.getCreateBy());
            moduleEventToPropertyRsp.setCreateTime(m.getCreateTime());
            moduleEventToPropertyRsp.setUpdateBy(m.getUpdateBy());
            moduleEventToPropertyRsp.setUpdateTime(m.getUpdateTime());
            moduleEventToPropertyRsp.setDescription(m.getDescription());
            result.add(moduleEventToPropertyRsp);
        });
        return result;
    }
}
