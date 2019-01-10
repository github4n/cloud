package com.iot.portal.service;

import com.google.common.collect.Lists;
import com.iot.device.api.ServiceActionApi;
import com.iot.device.api.ServiceEventApi;
import com.iot.device.api.ServiceModuleApi;
import com.iot.device.api.ServicePropertyApi;
import com.iot.device.vo.rsp.ServiceModuleActionResp;
import com.iot.device.vo.rsp.ServiceModuleEventResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.portal.web.utils.ActionPropertyUtils;
import com.iot.portal.web.vo.PortalActionInfoResp;
import com.iot.portal.web.vo.PortalActionListResp;
import com.iot.portal.web.vo.PortalEventInfoResp;
import com.iot.portal.web.vo.PortalEventListResp;
import com.iot.portal.web.vo.PortalPropertyInfoResp;
import com.iot.portal.web.vo.PortalPropertyListResp;
import com.iot.util.AssertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:52 2018/7/3
 * @Modify by:
 */
@Component
public class ServiceModuleCoreService {
    @Autowired
    private ServiceActionApi serviceActionApi;

    @Autowired
    private ServiceEventApi serviceEventApi;

    @Autowired
    private ServicePropertyApi servicePropertyApi;

    @Autowired
    private ServiceModuleApi serviceModuleApi;

    public List<PortalActionListResp> findActionsByServiceModuleId(Long serviceModuleId) {
        List<PortalActionInfoResp> actionInfoRespList = findActionListByServiceModuleId(serviceModuleId);
        List<PortalActionListResp> portalActionListRespList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(actionInfoRespList)) {
            for (PortalActionInfoResp actionInfo : actionInfoRespList) {
                PortalActionListResp target = new PortalActionListResp();
                BeanUtils.copyProperties(actionInfo, target);
//                List<ServiceModulePropertyResp> paramPropertyList = actionInfo.getParamPropertyList();
//                List<ServiceModulePropertyResp> returnPropertyList = actionInfo.getReturnPropertyList();
//                target.setParamPropertyList(paramPropertyList);
//                target.setReturnPropertyList(returnPropertyList);
                portalActionListRespList.add(target);
            }
            return portalActionListRespList;
        }
        return portalActionListRespList;
    }

    public List<PortalEventListResp> findEventsByServiceModuleId(Long serviceModuleId) {
        List<PortalEventInfoResp> eventInfoRespList = findEventListByServiceModuleId(serviceModuleId);
        List<PortalEventListResp> portalEventListRespList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(eventInfoRespList)) {
            for (PortalEventInfoResp eventInfo : eventInfoRespList) {
                PortalEventListResp target = new PortalEventListResp();
                BeanUtils.copyProperties(eventInfo, target);
                portalEventListRespList.add(target);
            }
            return portalEventListRespList;
        }
        return portalEventListRespList;
    }


    public List<PortalActionInfoResp> findActionListByServiceModuleId(Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<PortalActionInfoResp> returnList = Lists.newArrayList();
        List<ServiceModuleActionResp> actionRespList =
                serviceActionApi.findActionListByServiceModuleId(serviceModuleId);
        if (CollectionUtils.isEmpty(actionRespList)) {
            return returnList;
        }
        for (ServiceModuleActionResp action : actionRespList) {

            PortalActionInfoResp target = new PortalActionInfoResp();
            Long actionId = action.getId();
            List<ServiceModulePropertyResp> propertyRespList = servicePropertyApi.findPropertyListByActionId(actionId);
            target.setActionInfo(action);
            target.setParamPropertyList(
                    ActionPropertyUtils.parseParamProperties(propertyRespList)); // 请求参数
            target.setReturnPropertyList(
                    ActionPropertyUtils.parseReturnProperties(propertyRespList)); // 返回参数
            returnList.add(target);
        }
        return returnList;
    }

    public List<PortalEventInfoResp> findEventListByServiceModuleId(Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<PortalEventInfoResp> returnList = Lists.newArrayList();
        List<ServiceModuleEventResp> eventRespList = serviceEventApi.findEventListByServiceModuleId(serviceModuleId);
        if (CollectionUtils.isEmpty(eventRespList)) {
            return returnList;
        }
        for (ServiceModuleEventResp event : eventRespList) {
            PortalEventInfoResp target = new PortalEventInfoResp();
            Long eventId = event.getId();
            List<ServiceModulePropertyResp> propertyRespList = servicePropertyApi.findPropertyListByEventId(eventId);
            target.setEventInfo(event);
            target.setPropertyList(propertyRespList); // 参数
            returnList.add(target);
        }
        return returnList;
    }

    public List<ServiceModulePropertyResp> findPropertyListByServiceModuleId(Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<ServiceModulePropertyResp> propertyRespList = servicePropertyApi.findPropertyListByServiceModuleId(serviceModuleId);

        return propertyRespList;
    }

    public List<PortalPropertyInfoResp> findPropertyInfoListByServiceModuleId(Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<PortalPropertyInfoResp> infoRespList = Lists.newArrayList();
        List<ServiceModulePropertyResp> propertyRespList = findPropertyListByServiceModuleId(serviceModuleId);
        if (CollectionUtils.isEmpty(propertyRespList)) {
            return infoRespList;
        }
        for (ServiceModulePropertyResp property : propertyRespList) {
            PortalPropertyInfoResp target = new PortalPropertyInfoResp();
            BeanUtils.copyProperties(property, target);
            infoRespList.add(target);
        }
        return infoRespList;
    }

    public List<PortalPropertyListResp> findPropertiesByServiceModuleId(Long serviceModuleId) {
        List<PortalPropertyInfoResp> propertyInfoRespList = findPropertyInfoListByServiceModuleId(serviceModuleId);
        List<PortalPropertyListResp> portalPropertyList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(propertyInfoRespList)) {
            for (PortalPropertyInfoResp propertyInfo : propertyInfoRespList) {
                PortalPropertyListResp target = new PortalPropertyListResp();
                BeanUtils.copyProperties(propertyInfo, target);
                portalPropertyList.add(target);
            }
            return portalPropertyList;
        }
        return portalPropertyList;
    }

    public void copyDeviceTypeModuleToProductId(Long productId, Long deviceTypeId) {
        serviceModuleApi.copyModule(deviceTypeId, productId);
    }
}
