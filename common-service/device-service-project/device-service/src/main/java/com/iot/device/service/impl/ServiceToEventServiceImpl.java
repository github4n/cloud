package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.device.core.ServiceModuleCacheCoreUtils;
import com.iot.device.mapper.ServiceToEventMapper;
import com.iot.device.model.ServiceModuleEvent;
import com.iot.device.model.ServiceToEvent;
import com.iot.device.service.IServiceModuleEventService;
import com.iot.device.service.IServiceToEventService;
import com.iot.device.vo.req.AddEventReq;
import com.iot.device.vo.req.ServiceToEventReq;
import com.iot.device.vo.rsp.ServiceToEventRsp;
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
 * 模组-to-事件表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Service
public class ServiceToEventServiceImpl extends ServiceImpl<ServiceToEventMapper, ServiceToEvent> implements IServiceToEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceToEventServiceImpl.class);

    @Autowired
    private IServiceModuleEventService serviceModuleEventService;

    @Transactional
    public void addOrUpdateEventsByServiceModuleId(Long origServiceModuleId, Long targetServiceModuleId) {
        ServiceModuleCacheCoreUtils.removeServiceToEventByModule(targetServiceModuleId, SaaSContextHolder.currentTenantId());
        if (origServiceModuleId == null) {
            LOGGER.info("origServiceModuleId not null");
            return;
        }
        if (targetServiceModuleId == null) {
            LOGGER.info("targetServiceModuleId not null");
            return;
        }
        EntityWrapper<ServiceToEvent> wrapper = new EntityWrapper<>();
        wrapper.eq("service_module_id", origServiceModuleId);
        List<ServiceToEvent> serviceToEventList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(serviceToEventList)) {
            LOGGER.info("service-module-to-event not exist");
            return;
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        for (ServiceToEvent se : serviceToEventList) {
            Long origEventId = se.getModuleEventId();
            ServiceModuleEvent event = serviceModuleEventService.addOrEvent(origEventId);
            if (event == null) {
                continue;
            }
            Long targetEventId = event.getId();
            ServiceToEvent tempServiceToEvent = new ServiceToEvent();
            tempServiceToEvent.setCreateBy(userId);
            tempServiceToEvent.setTenantId(tenantId);
            tempServiceToEvent.setCreateTime(new Date());
            tempServiceToEvent.setModuleEventId(targetEventId);
            tempServiceToEvent.setServiceModuleId(targetServiceModuleId);
            super.insert(tempServiceToEvent);
        }
    }

    @Transactional
    public void delEventsByServiceModuleId(Long serviceModuleId) {
        if (serviceModuleId == null) {
            LOGGER.info("serviceModuleId not null");
            return;
        }
        EntityWrapper<ServiceToEvent> wrapper = new EntityWrapper<>();
        wrapper.eq("service_module_id", serviceModuleId);
        List<ServiceToEvent> serviceToEventList = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(serviceToEventList)) {
            LOGGER.info("service-module-to-event not exist");
            return;
        }
        ServiceModuleCacheCoreUtils.removeServiceToEventByModule(serviceModuleId, SaaSContextHolder.currentTenantId());
        for (ServiceToEvent se : serviceToEventList) {
            Long eventId = se.getModuleEventId();
            //删除事件
            serviceModuleEventService.delEventByEventId(eventId);
            //删除关系
            super.deleteById(se.getId());
        }
    }

    @Transactional
    public void addOrUpdateEventsByServiceModuleId(Long origServiceModuleId, Long targetServiceModuleId, List<AddEventReq> eventList) {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        ServiceModuleCacheCoreUtils.removeServiceToEventByModule(targetServiceModuleId, tenantId);
        if (CollectionUtils.isEmpty(eventList)) {
            return;
        }
        for (AddEventReq eventReq : eventList) {
            //添加事件
            if (eventReq.getEventInfo() == null) {
                continue;
            }
            ServiceModuleEvent targetEvent = serviceModuleEventService.addOrEvent(eventReq);
            if (targetEvent == null) {
                continue;
            }
            Long targetEventId = targetEvent.getId();
            ServiceToEvent tempServiceToEvent = new ServiceToEvent();
            tempServiceToEvent.setCreateBy(userId);
            tempServiceToEvent.setTenantId(tenantId);
            tempServiceToEvent.setCreateTime(new Date());
            tempServiceToEvent.setModuleEventId(targetEventId);
            tempServiceToEvent.setServiceModuleId(targetServiceModuleId);
            super.insert(tempServiceToEvent);
        }
    }

    @Override
    public void save(ServiceToEventReq serviceToEventReq) {
        ServiceModuleCacheCoreUtils.removeServiceToEventByModule(serviceToEventReq.getServiceModuleId(), serviceToEventReq.getTenantId() == null ? -1L : serviceToEventReq.getTenantId());
        ServiceToEvent serviceToEvent = new ServiceToEvent();
        serviceToEvent.setTenantId(serviceToEventReq.getTenantId());
        serviceToEvent.setModuleEventId(serviceToEventReq.getModuleEventId());
        serviceToEvent.setServiceModuleId(serviceToEventReq.getServiceModuleId());
        serviceToEvent.setCreateBy(serviceToEventReq.getCreateBy());
        serviceToEvent.setCreateTime(new Date());
        serviceToEvent.setUpdateBy(serviceToEventReq.getUpdateBy());
        serviceToEvent.setUpdateTime(new Date());
        serviceToEvent.setDescription(serviceToEventReq.getDescription());
        serviceToEvent.setStatus(serviceToEventReq.getStatus());
        super.insert(serviceToEvent);
    }

    @Override
    public void saveMore(ServiceToEventReq serviceToEventReq) {
        ServiceModuleCacheCoreUtils.removeServiceToEventByModule(serviceToEventReq.getServiceModuleId(), serviceToEventReq.getTenantId() == null ? -1L : serviceToEventReq.getTenantId());
        List<ServiceToEvent> list = new ArrayList<>();
        Map<String, String> map = serviceToEventReq.getModuleEventIds();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            ServiceToEvent serviceToEvent = new ServiceToEvent();
            serviceToEvent.setTenantId(serviceToEventReq.getTenantId());
            serviceToEvent.setModuleEventId(Long.valueOf(entry.getKey()));
            serviceToEvent.setServiceModuleId(serviceToEventReq.getServiceModuleId());
            serviceToEvent.setCreateBy(serviceToEventReq.getCreateBy());
            serviceToEvent.setCreateTime(new Date());
            serviceToEvent.setUpdateBy(serviceToEventReq.getUpdateBy());
            serviceToEvent.setUpdateTime(new Date());
            serviceToEvent.setDescription(serviceToEventReq.getDescription());
            serviceToEvent.setStatus(Integer.parseInt(entry.getValue()));
            list.add(serviceToEvent);
        }
        super.insertBatch(list);
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        if (ids.size() > 0 && ids != null) {
            ids.forEach(id -> removeServiceToEventStCacheById(id));
            super.deleteBatchIds(ids);
        }
    }

    @Override
    public void update(Long id, Integer status) {
        removeServiceToEventStCacheById(id);
        ServiceToEvent serviceToEvent = new ServiceToEvent();
        serviceToEvent.setId(id);
        serviceToEvent.setStatus(status);
        serviceToEvent.setUpdateTime(new Date());
        super.updateById(serviceToEvent);
    }

    public void removeServiceToEventStCacheById(Long serviceToEventId) {
        ServiceToEvent serviceToEvent = super.selectById(serviceToEventId);
        if (serviceToEvent != null) {
            ServiceModuleCacheCoreUtils.removeServiceToEventByModule(serviceToEvent.getServiceModuleId(), SaaSContextHolder.currentTenantId());
        }
    }

    @Override
    public List<ServiceToEventRsp> listByModuleEventId(ArrayList<Long> ids) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id", SaaSContextHolder.currentTenantId());
        wrapper.in("module_event_id", ids);
        List<ServiceToEvent> list = super.selectList(wrapper);
        List<ServiceToEventRsp> result = new ArrayList();
        list.forEach(m -> {
            ServiceToEventRsp serviceToEventRsp = new ServiceToEventRsp();
            serviceToEventRsp.setId(m.getId());
            serviceToEventRsp.setTenantId(m.getTenantId());
            serviceToEventRsp.setModuleEventId(m.getModuleEventId());
            serviceToEventRsp.setServiceModuleId(m.getServiceModuleId());
            serviceToEventRsp.setCreateBy(m.getCreateBy());
            serviceToEventRsp.setCreateTime(m.getCreateTime());
            serviceToEventRsp.setUpdateBy(m.getUpdateBy());
            serviceToEventRsp.setUpdateTime(m.getUpdateTime());
            serviceToEventRsp.setDescription(m.getDescription());
            result.add(serviceToEventRsp);
        });
        return result;
    }

    @Override
    public List<ServiceToEventRsp> listByServiceModuleId(ArrayList<Long> ids) {
        List<ServiceToEventRsp> result = new ArrayList<>();
        List<ServiceToEvent> dataList = getServiceToEventListByServiceModuleId(ids);
        result = dataList.stream().map(serviceToEvent -> {
            ServiceToEventRsp serviceToEventRsp = transServiceToEventRsp(serviceToEvent);
            return serviceToEventRsp;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<ServiceToEvent> getServiceToEventListByServiceModuleId(ArrayList<Long> ids) {
        List<ServiceToEvent> result = new ArrayList();
        List<Long> noCacheIds = listByServiceModuleIdFromCache(ids, result, SaaSContextHolder.currentTenantId());
        if (noCacheIds != null && noCacheIds.size() > 0) {
            result.addAll(listByServiceModuleIdFromSql(noCacheIds));
        }
        return result;
    }

    private ArrayList<Long> listByServiceModuleIdFromCache(ArrayList<Long> ids, List<ServiceToEvent> result, Long tenantId) {
        ArrayList<Long> noCacheIds = new ArrayList<>();
        ids.forEach(serviceModuleId -> {
            List<ServiceToEvent> cacheData = ServiceModuleCacheCoreUtils.getServiceToEventCache(tenantId, serviceModuleId);
            if (cacheData != null && cacheData.size() > 0) {
                result.addAll(cacheData);
            } else {
                noCacheIds.add(serviceModuleId);
            }
        });
        return noCacheIds;
    }

    private List<ServiceToEvent> listByServiceModuleIdFromSql(List<Long> ids) {
        List<ServiceToEvent> result = new ArrayList<>();
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
            Long tenantId = SaaSContextHolder.currentTenantId();
            Map<Long, List<ServiceToEvent>> serviceToEventGroup = result.stream().collect(Collectors.groupingBy(ServiceToEvent::getServiceModuleId));
            serviceToEventGroup.forEach((k, v) -> ServiceModuleCacheCoreUtils.cacheServiceToEventCache(tenantId, k, v));
        }
        return result;
    }

    private ServiceToEventRsp transServiceToEventRsp(ServiceToEvent m) {
        ServiceToEventRsp serviceToEventRsp = new ServiceToEventRsp();
        serviceToEventRsp.setId(m.getId());
        serviceToEventRsp.setTenantId(m.getTenantId());
        serviceToEventRsp.setModuleEventId(m.getModuleEventId());
        serviceToEventRsp.setServiceModuleId(m.getServiceModuleId());
        serviceToEventRsp.setCreateBy(m.getCreateBy());
        serviceToEventRsp.setCreateTime(m.getCreateTime());
        serviceToEventRsp.setUpdateBy(m.getUpdateBy());
        serviceToEventRsp.setUpdateTime(m.getUpdateTime());
        serviceToEventRsp.setDescription(m.getDescription());
        return serviceToEventRsp;
    }
}
