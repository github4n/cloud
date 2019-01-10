package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.device.mapper.ModuleActionToPropertyMapper;
import com.iot.device.model.ModuleActionToProperty;
import com.iot.device.model.ServiceModuleProperty;
import com.iot.device.service.IModuleActionToPropertyService;
import com.iot.device.service.IServiceModulePropertyService;
import com.iot.device.vo.req.AddServiceModulePropertyReq;
import com.iot.device.vo.req.ModuleActionToPropertyReq;
import com.iot.device.vo.rsp.ModuleActionToPropertyRsp;
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
 * 模组-方法-to-参数表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Service
public class ModuleActionToPropertyServiceImpl extends ServiceImpl<ModuleActionToPropertyMapper, ModuleActionToProperty> implements IModuleActionToPropertyService {


    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleActionToPropertyServiceImpl.class);

    @Autowired
    private IServiceModulePropertyService serviceModulePropertyService;


    @Transactional
    public void addOrUpdatePropertiesByActionId(Long origActionId, Long targetActionId) {
        if (origActionId == null) {
            LOGGER.info("origActionId not null");
            return;
        }
        if (targetActionId == null) {
            LOGGER.info("targetActionId not null");
            return;
        }
        EntityWrapper<ModuleActionToProperty> wrapper = new EntityWrapper<>();
        wrapper.eq("module_action_id", origActionId);
        List<ModuleActionToProperty> actionToPropertyList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(actionToPropertyList)) {
            LOGGER.info("action-to-properties not null");
            return;
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        for (ModuleActionToProperty ap : actionToPropertyList) {
            Long origPropertyId = ap.getModulePropertyId();//
            ServiceModuleProperty targetProperty = serviceModulePropertyService.addOrUpdateProperty(origPropertyId);
            if (targetProperty == null) {
                continue;
            }
            Long targetPropertyId = targetProperty.getId();
            ModuleActionToProperty tempActionToProperty = new ModuleActionToProperty();
            tempActionToProperty.setTenantId(tenantId);
            tempActionToProperty.setModuleActionId(targetActionId);
            tempActionToProperty.setModulePropertyId(targetPropertyId);
            tempActionToProperty.setCreateBy(userId);
            tempActionToProperty.setCreateTime(new Date());
            super.insert(tempActionToProperty);

        }
    }

    @Transactional
    public void delPropertiesByActionId(Long actionId) {
        if (actionId == null) {
            LOGGER.info("actionId not null");
            return;
        }
        EntityWrapper<ModuleActionToProperty> wrapper = new EntityWrapper<>();
        wrapper.eq("module_action_id", actionId);
        List<ModuleActionToProperty> actionToPropertyList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(actionToPropertyList)) {
            LOGGER.info("action-to-properties not null");
            return;
        }
        for (ModuleActionToProperty ap : actionToPropertyList) {
            Long propertyId = ap.getModulePropertyId();
            serviceModulePropertyService.delPropertyByPropertyId(propertyId);
            LOGGER.info("delete-ModuleActionToProperty-by-id");
            super.deleteById(ap.getId());
        }
    }

    @Transactional
    public void addOrUpdatePropertiesByActionId(Long origActionId, Long targetActionId, List<AddServiceModulePropertyReq> propertyList) {

        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        if (CollectionUtils.isEmpty(propertyList)) {
            return;
        }
        for (AddServiceModulePropertyReq property : propertyList) {

            ServiceModuleProperty targetProperty = serviceModulePropertyService.addOrUpdateProperty(property);
            if (targetProperty == null) {
                continue;
            }
            Long targetPropertyId = targetProperty.getId();
            ModuleActionToProperty tempActionToProperty = new ModuleActionToProperty();
            tempActionToProperty.setTenantId(tenantId);
            tempActionToProperty.setModuleActionId(targetActionId);
            tempActionToProperty.setModulePropertyId(targetPropertyId);
            tempActionToProperty.setCreateBy(userId);
            tempActionToProperty.setCreateTime(new Date());
            super.insert(tempActionToProperty);

        }
    }

    @Override
    public void save(ModuleActionToPropertyReq moduleActionToPropertyReq) {
        ModuleActionToProperty moduleActionToProperty = new ModuleActionToProperty();
        moduleActionToProperty.setTenantId(moduleActionToPropertyReq.getTenantId());
        moduleActionToProperty.setModuleActionId(moduleActionToPropertyReq.getModuleActionId());
        moduleActionToProperty.setModulePropertyId(moduleActionToPropertyReq.getModulePropertyId());
        moduleActionToProperty.setCreateBy(moduleActionToPropertyReq.getCreateBy());
        moduleActionToProperty.setCreateTime(new Date());
        moduleActionToProperty.setUpdateBy(moduleActionToPropertyReq.getUpdateBy());
        moduleActionToProperty.setUpdateTime(new Date());
        moduleActionToProperty.setDescription(moduleActionToPropertyReq.getDescription());
        super.insert(moduleActionToProperty);
    }

    @Override
    public void saveMore(ModuleActionToPropertyReq moduleActionToPropertyReq) {
        List<ModuleActionToProperty> list = new ArrayList<>();
        moduleActionToPropertyReq.getModulePropertyIds().forEach(m->{
            ModuleActionToProperty moduleActionToProperty = new ModuleActionToProperty();
            moduleActionToProperty.setTenantId(moduleActionToPropertyReq.getTenantId());
            moduleActionToProperty.setModuleActionId(moduleActionToPropertyReq.getModuleActionId());
            moduleActionToProperty.setModulePropertyId(Long.valueOf(m.toString()));
            moduleActionToProperty.setCreateBy(moduleActionToPropertyReq.getCreateBy());
            moduleActionToProperty.setCreateTime(new Date());
            moduleActionToProperty.setUpdateBy(moduleActionToPropertyReq.getUpdateBy());
            moduleActionToProperty.setUpdateTime(new Date());
            moduleActionToProperty.setDescription(moduleActionToPropertyReq.getDescription());
            moduleActionToProperty.setParamType(moduleActionToPropertyReq.getParamType());
            list.add(moduleActionToProperty);
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
    public List<ModuleActionToPropertyRsp> listByModuleActionId(ArrayList<Long> ids) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id",SaaSContextHolder.currentTenantId());
        wrapper.in("module_action_id",ids);
        List<ModuleActionToProperty> list = super.selectList(wrapper);
        List<ModuleActionToPropertyRsp> result = new ArrayList();
        list.forEach(m->{
            ModuleActionToPropertyRsp moduleActionToPropertyRsp = new ModuleActionToPropertyRsp();
            moduleActionToPropertyRsp.setId(m.getId());
            moduleActionToPropertyRsp.setTenantId(m.getTenantId());
            moduleActionToPropertyRsp.setModuleActionId(m.getModuleActionId());
            moduleActionToPropertyRsp.setModulePropertyId(m.getModulePropertyId());
            moduleActionToPropertyRsp.setCreateBy(m.getCreateBy());
            moduleActionToPropertyRsp.setCreateTime(m.getCreateTime());
            moduleActionToPropertyRsp.setUpdateBy(m.getUpdateBy());
            moduleActionToPropertyRsp.setUpdateTime(m.getUpdateTime());
            moduleActionToPropertyRsp.setDescription(m.getDescription());
            result.add(moduleActionToPropertyRsp);
        });
        return result;
    }

    @Override
    public List<ModuleActionToPropertyRsp> listByModuleActionIdAndModulePropertyId(Long moduleActionId, Long modulePropertyId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id",SaaSContextHolder.currentTenantId());
        wrapper.eq("module_action_id",moduleActionId);
        wrapper.eq("module_property_id",modulePropertyId);
        List<ModuleActionToProperty> list = super.selectList(wrapper);
        List<ModuleActionToPropertyRsp> result = new ArrayList();
        list.forEach(m->{
            ModuleActionToPropertyRsp moduleActionToPropertyRsp = new ModuleActionToPropertyRsp();
            moduleActionToPropertyRsp.setId(m.getId());
            moduleActionToPropertyRsp.setTenantId(m.getTenantId());
            moduleActionToPropertyRsp.setModuleActionId(m.getModuleActionId());
            moduleActionToPropertyRsp.setModulePropertyId(m.getModulePropertyId());
            moduleActionToPropertyRsp.setCreateBy(m.getCreateBy());
            moduleActionToPropertyRsp.setCreateTime(m.getCreateTime());
            moduleActionToPropertyRsp.setUpdateBy(m.getUpdateBy());
            moduleActionToPropertyRsp.setUpdateTime(m.getUpdateTime());
            moduleActionToPropertyRsp.setDescription(m.getDescription());
            result.add(moduleActionToPropertyRsp);
        });
        return result;
    }
}
