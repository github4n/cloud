package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.device.core.ServiceModuleCacheCoreUtils;
import com.iot.device.mapper.ServiceToActionMapper;
import com.iot.device.model.ServiceModuleAction;
import com.iot.device.model.ServiceToAction;
import com.iot.device.service.IServiceModuleActionService;
import com.iot.device.service.IServiceToActionService;
import com.iot.device.vo.req.AddActionReq;
import com.iot.device.vo.req.ServiceToActionReq;
import com.iot.device.vo.rsp.ServiceToActionRsp;
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
 * 模组-to-方法表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Service
public class ServiceToActionServiceImpl extends ServiceImpl<ServiceToActionMapper, ServiceToAction> implements IServiceToActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceToActionServiceImpl.class);

    @Autowired
    private IServiceModuleActionService serviceModuleActionService;

    @Transactional
    public void addOrUpdateActionsByServiceModuleId(Long origServiceModuleId, Long targetServiceModuleId) {
        ServiceModuleCacheCoreUtils.removeServiceToActionByModule(targetServiceModuleId, SaaSContextHolder.currentTenantId());
        if (origServiceModuleId == null) {
            LOGGER.info("origServiceModuleId not null");
            return;
        }
        if (targetServiceModuleId == null) {
            LOGGER.info("targetServiceModuleId not null");
            return;
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        EntityWrapper<ServiceToAction> wrapper = new EntityWrapper<>();
        wrapper.eq("service_module_id", origServiceModuleId);
        List<ServiceToAction> serviceToActionList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(serviceToActionList)) {
            LOGGER.info("service-module-to-action not exist");
            return;
        }
        for (ServiceToAction sa : serviceToActionList) {
            Long origActionId = sa.getModuleActionId();
            //添加获取原始方法
            ServiceModuleAction action = serviceModuleActionService.addOrUpdateAction(origActionId);
            if (action == null) {
                continue;
            }
            Long targetActionId = action.getId();//获取新的方法id copy
            ServiceToAction tempServiceToAction = new ServiceToAction();
            tempServiceToAction.setModuleActionId(targetActionId);
            tempServiceToAction.setServiceModuleId(targetServiceModuleId);
            tempServiceToAction.setCreateBy(userId);
            tempServiceToAction.setTenantId(tenantId);
            tempServiceToAction.setCreateTime(new Date());
            super.insert(tempServiceToAction);
        }
    }

    @Transactional
    public void delActionsByServiceModuleId(Long serviceModuleId) {
        if (serviceModuleId == null) {
            LOGGER.info("serviceModuleId not null");
            return;
        }
        EntityWrapper<ServiceToAction> wrapper = new EntityWrapper<>();
        wrapper.eq("service_module_id", serviceModuleId);
        List<ServiceToAction> serviceToActionList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(serviceToActionList)) {
            LOGGER.info("service-module-to-action not exist");
            return;
        }
        ServiceModuleCacheCoreUtils.removeServiceToActionByModule(serviceModuleId, SaaSContextHolder.currentTenantId());
        for (ServiceToAction sa : serviceToActionList) {

            Long actionId = sa.getModuleActionId();
            //删除方法
            serviceModuleActionService.delActionByActionId(actionId);
            //删除关系
            super.deleteById(sa.getId());
        }
    }

    @Transactional
    public void addOrUpdateActionsByServiceModuleId(Long origServiceModuleId, Long targetServiceModuleId, List<AddActionReq> actionList) {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        ServiceModuleCacheCoreUtils.removeServiceToActionByModule(targetServiceModuleId, SaaSContextHolder.currentTenantId());
        if (CollectionUtils.isEmpty(actionList)) {
            return;
        }
        for (AddActionReq action : actionList) {
            if (action.getActionInfo() == null) {
                continue;
            }
            //添加方法
            ServiceModuleAction targetAction = serviceModuleActionService.addOrUpdateAction(action);
            if (targetAction == null) {
                continue;
            }
            ServiceToAction tempServiceToAction = new ServiceToAction();
            tempServiceToAction.setModuleActionId(targetAction.getId());
            tempServiceToAction.setServiceModuleId(targetServiceModuleId);
            tempServiceToAction.setCreateBy(userId);
            tempServiceToAction.setTenantId(tenantId);
            tempServiceToAction.setCreateTime(new Date());
            super.insert(tempServiceToAction);

        }
    }

    @Override
    public void save(ServiceToActionReq serviceToActionReq) {
        ServiceModuleCacheCoreUtils.removeServiceToActionByModule(serviceToActionReq.getServiceModuleId(), serviceToActionReq.getTenantId() == null ? -1L : serviceToActionReq.getTenantId());
        ServiceToAction serviceToAction = new ServiceToAction();
        serviceToAction.setTenantId(serviceToActionReq.getTenantId());
        serviceToAction.setModuleActionId(serviceToActionReq.getModuleActionId());
        serviceToAction.setServiceModuleId(serviceToActionReq.getServiceModuleId());
        serviceToAction.setCreateBy(serviceToActionReq.getCreateBy());
        serviceToAction.setCreateTime(new Date());
        serviceToAction.setUpdateBy(serviceToActionReq.getUpdateBy());
        serviceToAction.setUpdateTime(new Date());
        serviceToAction.setDescription(serviceToActionReq.getDescription());
        serviceToAction.setStatus(serviceToActionReq.getStatus());
        super.insert(serviceToAction);
    }

    @Override
    public void saveMore(ServiceToActionReq serviceToActionReq) {
        ServiceModuleCacheCoreUtils.removeServiceToActionByModule(serviceToActionReq.getServiceModuleId(), serviceToActionReq.getTenantId() == null ? -1L : serviceToActionReq.getTenantId());
        List<ServiceToAction> list = new ArrayList<>();
        Map<String, String> map = serviceToActionReq.getModuleActionIds();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            ServiceToAction serviceToAction = new ServiceToAction();
            serviceToAction.setTenantId(serviceToActionReq.getTenantId());
            serviceToAction.setModuleActionId(Long.valueOf(entry.getKey()));
            serviceToAction.setServiceModuleId(serviceToActionReq.getServiceModuleId());
            serviceToAction.setCreateBy(serviceToActionReq.getCreateBy());
            serviceToAction.setCreateTime(new Date());
            serviceToAction.setUpdateBy(serviceToActionReq.getUpdateBy());
            serviceToAction.setUpdateTime(new Date());
            serviceToAction.setDescription(serviceToActionReq.getDescription());
            serviceToAction.setStatus(Integer.parseInt(entry.getValue()));
            list.add(serviceToAction);
        }
        super.insertBatch(list);
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        if (ids.size() > 0 && ids != null) {
            ids.forEach(id -> removeServiceToActionStCacheById(id));
            super.deleteBatchIds(ids);
        }
    }

    @Override
    public void update(Long id, Integer status) {
        removeServiceToActionStCacheById(id);
        ServiceToAction serviceToAction = new ServiceToAction();
        serviceToAction.setId(id);
        serviceToAction.setStatus(status);
        serviceToAction.setUpdateTime(new Date());
        super.updateById(serviceToAction);
    }

    public void removeServiceToActionStCacheById(Long serviceToActionId) {
        ServiceToAction serviceToAction = super.selectById(serviceToActionId);
        if (serviceToAction != null) {
            ServiceModuleCacheCoreUtils.removeServiceToActionByModule(serviceToAction.getServiceModuleId(), SaaSContextHolder.currentTenantId());
        }
    }

    @Override
    public List<ServiceToActionRsp> listByModuleActionId(ArrayList<Long> ids) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id", SaaSContextHolder.currentTenantId());
        wrapper.in("module_action_id", ids);
        List<ServiceToAction> list = super.selectList(wrapper);
        List<ServiceToActionRsp> result = new ArrayList();
        list.forEach(m -> {
            ServiceToActionRsp serviceToActionRsp = new ServiceToActionRsp();
            serviceToActionRsp.setId(m.getId());
            serviceToActionRsp.setTenantId(m.getTenantId());
            serviceToActionRsp.setModuleActionId(m.getModuleActionId());
            serviceToActionRsp.setServiceModuleId(m.getServiceModuleId());
            serviceToActionRsp.setCreateBy(m.getCreateBy());
            serviceToActionRsp.setCreateTime(m.getCreateTime());
            serviceToActionRsp.setUpdateBy(m.getUpdateBy());
            serviceToActionRsp.setUpdateTime(m.getUpdateTime());
            serviceToActionRsp.setDescription(m.getDescription());
            result.add(serviceToActionRsp);
        });
        return result;
    }


    @Override
    public List<ServiceToActionRsp> listByServiceModuleId(ArrayList<Long> ids) {
        List<ServiceToActionRsp> result = new ArrayList();
        List<ServiceToAction> serviceToActionList = getServiceToActionByServiceModuleId(ids);
        if (serviceToActionList != null && serviceToActionList.size() > 0) {
            result = serviceToActionList.stream().map(serviceToAction -> {
                ServiceToActionRsp serviceToActionRsp = transServiceToActionRsp(serviceToAction);
                return serviceToActionRsp;
            }).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<ServiceToAction> getServiceToActionByServiceModuleId(List<Long> ids) {
        List<ServiceToAction> result = new ArrayList();
        List<Long> noCacheIds = listByModuleActionIdFromCache(ids, result, SaaSContextHolder.currentTenantId());
        if (noCacheIds != null && noCacheIds.size() > 0) {
            result.addAll(getServiceToActionByServiceModuleIdFromSql(noCacheIds));
        }
        return result;
    }

    private List<Long> listByModuleActionIdFromCache(List<Long> ids, List<ServiceToAction> result, Long tenantId) {
        List<Long> noCacheIds = new ArrayList<>();
        ids.forEach(serviceModuleId -> {
            List<ServiceToAction> cacheData = new ArrayList<>();
            cacheData = ServiceModuleCacheCoreUtils.getServiceToActionCache(tenantId, serviceModuleId);
            if (cacheData != null && cacheData.size() > 0) {
                result.addAll(cacheData);
            } else {
                noCacheIds.add(serviceModuleId);
            }
        });
        return noCacheIds;
    }

    private List<ServiceToAction> getServiceToActionByServiceModuleIdFromSql(List<Long> ids) {
        List<ServiceToAction> result = new ArrayList<>();
        /**
         * 数据库查询
         */
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id", SaaSContextHolder.currentTenantId());
        wrapper.in("service_module_id", ids);
        result = super.selectList(wrapper);
        /**
         * 数据库查出数据做类型转换并存入redis缓存
         */
        if (result != null && result.size() > 0) {
            Map<Long, List<ServiceToAction>> serviceToActionGroup = result.stream().collect(Collectors.groupingBy(ServiceToAction::getServiceModuleId));
            serviceToActionGroup.forEach((k, v) -> {
                ServiceModuleCacheCoreUtils.cacheServiceToActionCache(SaaSContextHolder.currentTenantId(), k, v);
            });
        }
        return result;
    }

    private ServiceToActionRsp transServiceToActionRsp(ServiceToAction serviceToAction) {
        ServiceToActionRsp serviceToActionRsp = new ServiceToActionRsp();
        serviceToActionRsp.setId(serviceToAction.getId());
        serviceToActionRsp.setTenantId(serviceToAction.getTenantId());
        serviceToActionRsp.setModuleActionId(serviceToAction.getModuleActionId());
        serviceToActionRsp.setServiceModuleId(serviceToAction.getServiceModuleId());
        serviceToActionRsp.setCreateBy(serviceToAction.getCreateBy());
        serviceToActionRsp.setCreateTime(serviceToAction.getCreateTime());
        serviceToActionRsp.setUpdateBy(serviceToAction.getUpdateBy());
        serviceToActionRsp.setUpdateTime(serviceToAction.getUpdateTime());
        serviceToActionRsp.setDescription(serviceToAction.getDescription());
        return serviceToActionRsp;
    }
}
