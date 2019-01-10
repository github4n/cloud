package com.iot.device.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.device.api.ServiceEventApi;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.model.ServiceModuleEvent;
import com.iot.device.model.ServiceToEvent;
import com.iot.device.service.IServiceModuleEventService;
import com.iot.device.service.IServiceToEventService;
import com.iot.device.vo.req.ServiceModuleEventReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModuleEventResp;
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
 * 模组-事件表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@RestController
public class ServiceModuleEventController implements ServiceEventApi {

    @Autowired
    private IServiceModuleEventService serviceModuleEventService;

    @Autowired
    private IServiceToEventService serviceToEventService;

    public List<ServiceModuleEventResp> findEventListByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        EntityWrapper<ServiceToEvent> saWrapper = new EntityWrapper<>();
        saWrapper.eq("service_module_id", serviceModuleId);
        List<ServiceToEvent> serviceToActionList = serviceToEventService.selectList(saWrapper);
        if (CollectionUtils.isEmpty(serviceToActionList)) {
            return null;
        }
        Map<Long, ServiceToEvent> serviceToEventMap = Maps.newHashMap();
        List<Long> eventIds = Lists.newArrayList();
        for (ServiceToEvent se : serviceToActionList) {
            Long eventId = se.getModuleEventId();
            eventIds.add(eventId);
            serviceToEventMap.put(eventId, se);
        }
        EntityWrapper<ServiceModuleEvent> eventWrapper = new EntityWrapper<>();
        eventWrapper.in("id", eventIds);
        List<ServiceModuleEvent> eventList = serviceModuleEventService.selectList(eventWrapper);
        if (CollectionUtils.isEmpty(eventList)) {
            return null;
        }
        List<ServiceModuleEventResp> respList = Lists.newArrayList();
        List<Long> parentEventIds = Lists.newArrayList();
        for (ServiceModuleEvent event : eventList) {
            parentEventIds.add(event.getParentId());
            ServiceModuleEventResp target = new ServiceModuleEventResp();
            BeanCopyUtils.copyModuleEvent(event, target);
            //可选必选
            ServiceToEvent serviceToEvent = serviceToEventMap.get(event.getId());
            if (serviceToEvent!=null){
                //    propertyStatus 属性状态，0:可选,1:必选")
                target.setPropertyStatus(serviceToEvent.getStatus());
            }
            respList.add(target);
        }
        List<ServiceModuleEvent> serviceModuleEvents = serviceModuleEventService.selectBatchIds(parentEventIds);
        for (ServiceModuleEventResp serviceModuleEventResp : respList) {
            for (ServiceModuleEvent parentEvent : serviceModuleEvents) {
                if (serviceModuleEventResp.getParentId() != null && serviceModuleEventResp.getParentId().equals(parentEvent.getId())) {
                    ParentVO parentVO = new ParentVO();
                    parentVO.setId(parentEvent.getId());
                    parentVO.setName(parentEvent.getName());
                    parentVO.setCode(parentEvent.getCode());
                    serviceModuleEventResp.setParent(parentVO);
                }
            }
        }
        return respList;
    }

    public ServiceModuleEventResp getEventInfoByEventId(@RequestParam("eventId") Long eventId) {
        AssertUtils.notNull(eventId, "eventId.notnull");
        ServiceModuleEvent event = serviceModuleEventService.selectById(eventId);
        if (event == null) {
            return null;
        }
        ServiceModuleEventResp target = new ServiceModuleEventResp();
        BeanCopyUtils.copyModuleEvent(event, target);
        return target;
    }

    public List<ServiceModuleEventResp> listEventInfoByEventIds(@RequestParam("eventIds") List<Long> eventIds) {
        List<ServiceModuleEventResp> serviceModuleEventResps = Lists.newArrayList();
        List<ServiceModuleEvent> events = serviceModuleEventService.selectBatchIds(eventIds);
        if (CollectionUtils.isEmpty(events)) {
            return serviceModuleEventResps;
        }
        for (ServiceModuleEvent event : events) {
            ServiceModuleEventResp target = new ServiceModuleEventResp();
            BeanCopyUtils.copyModuleEvent(event, target);
        }
        return serviceModuleEventResps;
    }

    @Override
    public Long saveOrUpdate(@RequestBody ServiceModuleEventReq serviceModuleEventReq) {
        return serviceModuleEventService.saveOrUpdate(serviceModuleEventReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        serviceModuleEventService.delete(ids);
    }

    @Override
    public PageInfo list(@RequestBody ServiceModuleEventReq serviceModuleEventReq) {
        return serviceModuleEventService.list(serviceModuleEventReq);
    }

    @Override
    public List<ServiceModuleEventResp> listByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId) {
        return serviceModuleEventService.listByServiceModuleId(serviceModuleId);
    }

    /**
     * @despriction：查找支持联动配置的事件
     * @author  yeshiyuan
     * @created 2018/10/23 13:55
     * @return
     */
    @Override
    public ModuleSupportIftttResp findSupportIftttEvents(@RequestParam("serviceModuleIds") List<Long> serviceModuleIds, @RequestParam("tenantId") Long tenantId) {
        return serviceModuleEventService.findSupportIftttEvents(serviceModuleIds, tenantId);
    }

    /**
     * @despriction：修改模组事件联动属性（portal使用）
     * @author  yeshiyuan
     * @created 2018/10/23 17:19
     * @return
     */
    @Override
    public int updateEventSupportIfttt(@RequestBody UpdateModuleSupportIftttReq req) {
        return serviceModuleEventService.updateEventSupportIfttt(req);
    }
}

