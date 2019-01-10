package com.iot.device.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.device.core.ServiceModuleCacheCoreUtils;
import com.iot.device.enums.ModuleIftttTypeEnum;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.exception.ServiceModuleExceptionEnum;
import com.iot.device.mapper.ServiceModuleEventMapper;
import com.iot.device.model.ServiceModuleEvent;
import com.iot.device.model.ServiceToEvent;
import com.iot.device.model.enums.DevelopStatusEnum;
import com.iot.device.model.enums.PropertyStatusEnum;
import com.iot.device.service.IModuleEventToPropertyService;
import com.iot.device.service.IServiceModuleEventService;
import com.iot.device.service.IServiceToEventService;
import com.iot.device.vo.req.AddEventReq;
import com.iot.device.vo.req.ModuleIftttReq;
import com.iot.device.vo.req.ServiceModuleEventReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleIftttDataResp;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModuleEventResp;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 模组-事件表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Service
public class ServiceModuleEventServiceImpl extends ServiceImpl<ServiceModuleEventMapper, ServiceModuleEvent> implements IServiceModuleEventService {


    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleEventServiceImpl.class);
    @Autowired
    private IModuleEventToPropertyService moduleEventToPropertyService;

    @Autowired
    private ServiceModuleEventMapper serviceModuleEventMapper;


    @Autowired
    private IServiceToEventService iServiceToEventService;

    @Transactional
    public void delEventByEventId(Long eventId) {
        ServiceModuleCacheCoreUtils.removeServiceModuleEventCache(eventId);
        if (eventId == null) {
            LOGGER.info("eventId not null");
            return;
        }
        ServiceModuleEvent event = super.selectById(eventId);
        if (event == null) {
            LOGGER.info("event not exist");
            return;
        }
        moduleEventToPropertyService.delPropertiesByEventId(eventId);

        super.deleteById(event.getId());
    }

    @Transactional
    public ServiceModuleEvent addOrEvent(Long origEventId) {
        if (origEventId == null) {
            LOGGER.info("origEventId not null");
            return null;
        }
        ServiceModuleEvent event = super.selectById(origEventId);
        if (event == null) {
            LOGGER.info("event not exist");
            return null;
        }
        Long parentEventId;
        if (event.getParentId() == null) {
            parentEventId = origEventId;
        } else {
            parentEventId = event.getParentId();
        }
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        ServiceModuleEvent tempEvent = new ServiceModuleEvent();
        BeanUtils.copyProperties(event, tempEvent);
        tempEvent.setId(null);
        tempEvent.setParentId(parentEventId);
        tempEvent.setCreateBy(userId);
        tempEvent.setTenantId(tenantId);
        tempEvent.setCreateTime(new Date());
        super.insert(tempEvent);

        Long targetEventId = tempEvent.getId();
        moduleEventToPropertyService.addOrUpdateEventProperty(origEventId, targetEventId);
        return tempEvent;
    }

    @Transactional
    public ServiceModuleEvent addOrEvent(AddEventReq eventReq) {

        Long parentEventId;
        if (eventReq.getEventInfo().getParentId() != null) {
            ServiceModuleEvent origEvent = super.selectById(eventReq.getEventInfo().getParentId());
            if (origEvent == null) {
                throw new BusinessException(ServiceModuleExceptionEnum.MODULE_EVENT_NOT_EXIST);
            }
            parentEventId = origEvent.getId();
        } else {
            ServiceModuleEvent origEvent = super.selectById(eventReq.getEventInfo().getId());
            if (origEvent == null) {
                throw new BusinessException(ServiceModuleExceptionEnum.MODULE_EVENT_NOT_EXIST);
            }
            parentEventId = origEvent.getId();
        }

        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        ServiceModuleEvent tempEvent = new ServiceModuleEvent();
        BeanUtils.copyProperties(eventReq.getEventInfo(), tempEvent);
        tempEvent.setDevelopStatus(DevelopStatusEnum.getByValue(eventReq.getEventInfo().getDevelopStatus()));
        tempEvent.setPropertyStatus(PropertyStatusEnum.getByValue(eventReq.getEventInfo().getPropertyStatus()));
        tempEvent.setDevelopStatus(DevelopStatusEnum.OPERATING);
        tempEvent.setId(null);
        tempEvent.setParentId(parentEventId);
        tempEvent.setCreateBy(userId);
        tempEvent.setTenantId(tenantId);
        tempEvent.setCreateTime(new Date());
        super.insert(tempEvent);

        Long origEventId = parentEventId;
        Long targetEventId = tempEvent.getId();
        moduleEventToPropertyService.addOrUpdateEventProperty(origEventId, targetEventId, eventReq.getPropertyList());
        return tempEvent;
    }

    @Override
    public Long saveOrUpdate(ServiceModuleEventReq serviceModuleEventReq) {
        ServiceModuleEvent serviceModuleEvent = null;
        if (serviceModuleEventReq.getId() != null && serviceModuleEventReq.getId() > 0) {
            serviceModuleEvent = super.selectById(serviceModuleEventReq.getId());
            if (serviceModuleEvent == null) {
                throw new BusinessException(DevelopInfoEnum.DEVELOP_NOT_EXIST);
            }
            ServiceModuleCacheCoreUtils.removeServiceModuleEventCache(serviceModuleEventReq.getId());
            serviceModuleEvent.setUpdateTime(new Date());
            serviceModuleEvent.setUpdateBy(serviceModuleEventReq.getUpdateBy());
        } else {
            serviceModuleEvent = new ServiceModuleEvent();
            serviceModuleEvent.setUpdateTime(new Date());
            serviceModuleEvent.setUpdateBy(serviceModuleEventReq.getUpdateBy());
            serviceModuleEvent.setCreateTime(new Date());
            serviceModuleEvent.setCreateBy(serviceModuleEventReq.getCreateBy());
        }
        serviceModuleEvent.setParentId(null);
        serviceModuleEvent.setTenantId(serviceModuleEventReq.getTenantId());
        serviceModuleEvent.setServiceModuleId(serviceModuleEventReq.getServiceModuleId());
        serviceModuleEvent.setVersion(serviceModuleEventReq.getVersion());
        serviceModuleEvent.setName(serviceModuleEventReq.getName());
        serviceModuleEvent.setCode(serviceModuleEventReq.getCode());
        serviceModuleEvent.setTags(serviceModuleEventReq.getTags());
        serviceModuleEvent.setApiLevel(serviceModuleEventReq.getApiLevel());
        if (serviceModuleEventReq.getDevelopStatus()==null || serviceModuleEventReq.getDevelopStatus()==0){
            serviceModuleEvent.setDevelopStatus(DevelopStatusEnum.IDLE);
        } else if (serviceModuleEventReq.getDevelopStatus()==1){
            serviceModuleEvent.setDevelopStatus(DevelopStatusEnum.OPERATING);
        } else {
            serviceModuleEvent.setDevelopStatus(DevelopStatusEnum.OPERATED);
        }
//        if (serviceModuleEventReq.getPropertyStatus()==0){
//            serviceModuleEvent.setPropertyStatus(PropertyStatusEnum.OPTIONAL);
//        } else {
//            serviceModuleEvent.setPropertyStatus(PropertyStatusEnum.MANDATORY);
//        }
        serviceModuleEvent.setParams(serviceModuleEventReq.getParams());
        serviceModuleEvent.setTestCase(serviceModuleEventReq.getTestCase());
        serviceModuleEvent.setDescription(serviceModuleEventReq.getDescription());
        serviceModuleEvent.setIftttType(serviceModuleEventReq.getIftttType());
        super.insertOrUpdate(serviceModuleEvent);
        return serviceModuleEvent.getId();
    }

    @Override
    public void delete(ArrayList<Long> ids) {
        if(!CollectionUtils.isEmpty(ids)) {
            ServiceModuleCacheCoreUtils.batchRemoveServiceModuleEventCache(ids);
            //检查是否被关联
            ids.forEach(eventId -> {
                EntityWrapper wrapper = new EntityWrapper<>();
                wrapper.eq("module_event_id", eventId);
                int count = iServiceToEventService.selectCount(wrapper);
                if (count > 0) {
                    throw new BusinessException(ServiceModuleExceptionEnum.MODULE_EVENT_IS_USED);
                }
            });
            super.deleteBatchIds(ids);
            ArrayList<Long> idsResult = new ArrayList<Long>();
            moduleEventToPropertyService.listByModuleEventId(ids).forEach(m -> {
                idsResult.add(m.getId());
            });
            moduleEventToPropertyService.delete(idsResult);
            idsResult.clear();
            iServiceToEventService.listByModuleEventId(ids).forEach(n -> {
                idsResult.add(n.getId());
            });
            iServiceToEventService.delete(idsResult);
            idsResult.clear();
        }
    }

    @Override
    public PageInfo list(ServiceModuleEventReq serviceModuleEventReq) {
        Page page = new Page<>(CommonUtil.getPageNum(serviceModuleEventReq),CommonUtil.getPageSize(serviceModuleEventReq));
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.isNull("parent_id");
        wrapper.eq("tenant_id", SaaSContextHolder.currentTenantId());
        if (StringUtils.isNotEmpty(serviceModuleEventReq.getSearchValues())){
            wrapper.andNew(true, "")
                    .like("name", serviceModuleEventReq.getSearchValues(), SqlLike.DEFAULT)
                    .or().like("code", serviceModuleEventReq.getSearchValues(), SqlLike.DEFAULT);
        }
        wrapper.orderDesc(Arrays.asList("create_time"));
        List<ServiceModuleEvent> list = serviceModuleEventMapper.selectPage(page,wrapper);
        List<ServiceModuleEventResp> respList = new ArrayList<>();
        list.forEach(m->{
            ServiceModuleEventResp serviceModuleEventResp = new ServiceModuleEventResp();
            serviceModuleEventResp.setId(m.getId());
            serviceModuleEventResp.setParentId(m.getParentId());
            serviceModuleEventResp.setTenantId(m.getTenantId());
            serviceModuleEventResp.setServiceModuleId(m.getServiceModuleId());
            serviceModuleEventResp.setVersion(m.getVersion());
            serviceModuleEventResp.setName(m.getName());
            serviceModuleEventResp.setCode(m.getCode());
            serviceModuleEventResp.setTags(m.getTags());
            serviceModuleEventResp.setApiLevel(m.getApiLevel());
            serviceModuleEventResp.setDevelopStatus(m.getDevelopStatus()!=null?(Integer)m.getDevelopStatus().getValue():null);
//            serviceModuleEventResp.setPropertyStatus(m.getPropertyStatus()!=null?(Integer)m.getPropertyStatus().getValue():null);
            serviceModuleEventResp.setParams(m.getParams());
            serviceModuleEventResp.setTestCase(m.getTestCase());
            serviceModuleEventResp.setCreateBy(m.getCreateBy());
            serviceModuleEventResp.setCreateTime(m.getCreateTime());
            serviceModuleEventResp.setUpdateBy(m.getUpdateBy());
            serviceModuleEventResp.setUpdateTime(m.getUpdateTime());
            serviceModuleEventResp.setDescription(m.getDescription());
            serviceModuleEventResp.setEventId(m.getEventId());
            serviceModuleEventResp.setIftttType(m.getIftttType());
            respList.add(serviceModuleEventResp);
        });
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(respList);
        pageInfo.setTotal(page.getTotal());
        pageInfo.setPages(page.getPages());
        pageInfo.setPageSize(page.getSize());
        pageInfo.setPageNum(serviceModuleEventReq.getPageNum());
        return pageInfo;
    }

    @Override
    public List<ServiceModuleEventResp> listByServiceModuleId(Long serviceModuleId) {
        //List<ServiceModuleEvent> list = serviceModuleEventMapper.listByServiceModuleId(serviceModuleId,SaaSContextHolder.currentTenantId());
        List<ServiceModuleEvent> list = getServiceModuleEventByServiceModuleId(serviceModuleId);
        List<ServiceModuleEventResp> respList = new ArrayList<>();
        list.forEach(m->{
            ServiceModuleEventResp serviceModuleEventResp = new ServiceModuleEventResp();
            serviceModuleEventResp.setId(m.getId());
            serviceModuleEventResp.setParentId(m.getParentId());
            serviceModuleEventResp.setTenantId(m.getTenantId());
            serviceModuleEventResp.setServiceModuleId(m.getServiceModuleId());
            serviceModuleEventResp.setVersion(m.getVersion());
            serviceModuleEventResp.setName(m.getName());
            serviceModuleEventResp.setCode(m.getCode());
            serviceModuleEventResp.setTags(m.getTags());
            serviceModuleEventResp.setApiLevel(m.getApiLevel());
            serviceModuleEventResp.setDevelopStatus(m.getDevelopStatus()!=null?(Integer)m.getDevelopStatus().getValue():null);
//            serviceModuleEventResp.setPropertyStatus(m.getPropertyStatus()!=null?(Integer)m.getPropertyStatus().getValue():null);
            serviceModuleEventResp.setParams(m.getParams());
            serviceModuleEventResp.setTestCase(m.getTestCase());
            serviceModuleEventResp.setCreateBy(m.getCreateBy());
            serviceModuleEventResp.setCreateTime(m.getCreateTime());
            serviceModuleEventResp.setUpdateBy(m.getUpdateBy());
            serviceModuleEventResp.setUpdateTime(m.getUpdateTime());
            serviceModuleEventResp.setDescription(m.getDescription());
            serviceModuleEventResp.setEventId(m.getEventId());
            serviceModuleEventResp.setIftttType(m.getIftttType());
            serviceModuleEventResp.setStatus(m.getStatus());
            respList.add(serviceModuleEventResp);
        });
        return respList;
    }

    public List<ServiceModuleEvent> getServiceModuleEventByServiceModuleId(Long serviceModuleId){
        List<ServiceModuleEvent> eventList = new ArrayList<>();
        ArrayList<Long> serviceModuleIds = new ArrayList<>();
        serviceModuleIds.add(serviceModuleId);
        List<ServiceToEvent> serviceToEventList = iServiceToEventService.getServiceToEventListByServiceModuleId(serviceModuleIds);
        if(serviceToEventList != null && serviceToEventList.size() >0){
            Map<Long,ServiceToEvent> serviceToEventMap = serviceToEventList.stream()
                    .collect(Collectors.toMap(ServiceToEvent::getModuleEventId, serviceToEvent -> serviceToEvent, (k1, k2) -> k1));
            List<Long> eventIds = serviceToEventList.stream().map(ServiceToEvent::getModuleEventId).collect(Collectors.toList());
            eventList = getServiceModuleEventListByIds(eventIds);
            eventList.forEach(e->{
                ServiceToEvent serviceToEvent= serviceToEventMap.get(e.getId());
                if(serviceToEvent != null){
                    e.setStatus(serviceToEvent.getStatus());
                    e.setEventId(serviceToEvent.getId());
                }
            });
        }


        return eventList;
    }



    /**
     * @despriction：查找支持联动配置的事件
     * @author  yeshiyuan
     * @created 2018/10/23 13:55
     * @return
     */
    @Override
    public ModuleSupportIftttResp findSupportIftttEvents(List<Long> serviceModuleIds, Long tenantId) {
        ModuleSupportIftttResp resp = new ModuleSupportIftttResp();
        List<ModuleIftttDataResp> ifList = new ArrayList<>();
        List<ModuleIftttDataResp> thenList = new ArrayList<>();
        List<ServiceModuleEvent> events = serviceModuleEventMapper.supportIftttEvent(serviceModuleIds);
        events.forEach(event -> {
            if (ModuleIftttTypeEnum.IF.getValue().equals(event.getIftttType())) {
                ModuleIftttDataResp dataResp = new ModuleIftttDataResp(event.getId(),event.getName(), ModuleIftttTypeEnum.checkChooseIf(event.getPortalIftttType()));
                ifList.add(dataResp);
            } else if (ModuleIftttTypeEnum.THEN.getValue().equals(event.getIftttType())) {
                ModuleIftttDataResp dataResp = new ModuleIftttDataResp(event.getId(),event.getName(), ModuleIftttTypeEnum.checkChooseThen(event.getPortalIftttType()));
                thenList.add(dataResp);
            } else if (ModuleIftttTypeEnum.IF_TEHN.getValue().equals(event.getIftttType())) {
                ModuleIftttDataResp ifData = new ModuleIftttDataResp(event.getId(),event.getName(), ModuleIftttTypeEnum.checkChooseIf(event.getPortalIftttType()));
                ifList.add(ifData);
                ModuleIftttDataResp thenData = new ModuleIftttDataResp(event.getId(),event.getName(), ModuleIftttTypeEnum.checkChooseThen(event.getPortalIftttType()));
                thenList.add(thenData);
            }
        });
        resp.setIfList(ifList);
        resp.setThenList(thenList);
        return resp;
    }

    /**
     * @despriction：修改模组事件联动属性（portal使用）
     * @author  yeshiyuan
     * @created 2018/10/23 17:19
     * @return
     */
    @Transactional
    @Override
    public int updateEventSupportIfttt(UpdateModuleSupportIftttReq req) {
        int i = 0;
        if (req.getList() != null && !req.getList().isEmpty()) {
            List<ServiceModuleEvent> events = serviceModuleEventMapper.supportIftttEvent(req.getServiceModuleIds());
            Map<Long, Integer> eventMap = events.stream().collect(Collectors.toMap(ServiceModuleEvent::getId, a-> a.getIftttType()));
            for (ModuleIftttReq o : req.getList()) {
                Integer portalIftttType = 0;
                if (o.getIfData()!=null && o.getIfData().intValue() == 1 ) {
                    portalIftttType = ModuleIftttTypeEnum.IF.getValue();
                }
                if (o.getThenData()!=null && o.getThenData().intValue() == 1) {
                    portalIftttType+=ModuleIftttTypeEnum.THEN.getValue();
                }
                Integer iftttType = eventMap.get(o.getId());
                if (iftttType == null) {
                    continue;
                }
                if (portalIftttType.compareTo(iftttType) == 1 ) {
                    throw new BusinessException(ServiceModuleExceptionEnum.MODULE_PORTAL_IFTTTTYPE_ERROR);
                }
                i +=serviceModuleEventMapper.updatePortalIftttType(o.getId(), req.getTenantId(), portalIftttType, req.getUserId(), new Date());
            }
        }
        return i;
    }

    public List<ServiceModuleEvent> getServiceModuleEventListByIds(List<Long> ids) {
        List<ServiceModuleEvent> result = new ArrayList<>();
        List<Long> noCacheIds = getServiceModuleEventListByIdsFromCache(ids, result);
        if (noCacheIds != null && noCacheIds.size() > 0) {
            List<ServiceModuleEvent> dataList = getServiceModuleEventListByIdsFromSql(noCacheIds);
            if (dataList != null && dataList.size() > 0) {
                result.addAll(dataList);
            }
        }
        return result;
    }

    private List<Long> getServiceModuleEventListByIdsFromCache(List<Long> ids, List<ServiceModuleEvent> result) {
        List<Long> noCacheIds = new ArrayList<>();
        List<ServiceModuleEvent> cacheData = ServiceModuleCacheCoreUtils.batchGetServiceMouleEventCache(ids);
        if (cacheData != null && cacheData.size() > 0) {
            List<Long> cacheIds = cacheData.stream().map(ServiceModuleEvent::getId).collect(Collectors.toList());
            noCacheIds = ids.stream().filter(id -> !cacheIds.contains(id)).collect(Collectors.toList());
            result.addAll(cacheData);
        } else {
            noCacheIds = ids;
        }
        return noCacheIds;
    }

    private List<ServiceModuleEvent> getServiceModuleEventListByIdsFromSql(List<Long> ids) {
        EntityWrapper<ServiceModuleEvent> eventWrapper = new EntityWrapper<>();
        eventWrapper.in("id", ids);
        List<ServiceModuleEvent> eventList = this.selectList(eventWrapper);
        if (eventList != null && eventList.size() > 0) {
            /**
             * 放入缓存
             */
            ServiceModuleCacheCoreUtils.batchSetServiceModuleEventCache(eventList);
        }
        return eventList;
    }
}
