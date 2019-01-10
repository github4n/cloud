package com.iot.shcs.module.service;

import com.google.common.collect.Lists;
import com.iot.device.api.ServiceActionApi;
import com.iot.device.api.ServiceEventApi;
import com.iot.device.api.ServiceModuleApi;
import com.iot.device.api.ServicePropertyApi;
import com.iot.device.vo.rsp.ServiceModuleActionResp;
import com.iot.device.vo.rsp.ServiceModuleEventResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.shcs.module.vo.resp.GetActionInfoResp;
import com.iot.shcs.module.vo.resp.GetEventInfoResp;
import com.iot.shcs.module.vo.resp.GetPropertyInfoResp;
import com.iot.shcs.module.vo.resp.ListActionListResp;
import com.iot.shcs.module.vo.resp.ListEventListResp;
import com.iot.shcs.module.vo.utils.ActionPropertyUtils;
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
public class ModuleCoreService {
    @Autowired
    private ServiceActionApi serviceActionApi;

    @Autowired
    private ServiceEventApi serviceEventApi;

    @Autowired
    private ServicePropertyApi servicePropertyApi;

    @Autowired
    private ServiceModuleApi serviceModuleApi;


    public List<ListActionListResp> findActionsByServiceModuleId(Long serviceModuleId) {
        List<GetActionInfoResp> actionInfoRespList = findActionListByServiceModuleId(serviceModuleId);
        List<ListActionListResp> portalActionListRespList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(actionInfoRespList)) {
            for (GetActionInfoResp actionInfo : actionInfoRespList) {
                ListActionListResp target = new ListActionListResp();
                BeanUtils.copyProperties(actionInfo, target);
                portalActionListRespList.add(target);
            }
            return portalActionListRespList;
        }
        return portalActionListRespList;
    }

    public List<ListEventListResp> findEventsByServiceModuleId(Long serviceModuleId) {
        List<GetEventInfoResp> eventInfoRespList = findEventListByServiceModuleId(serviceModuleId);
        List<ListEventListResp> portalEventListRespList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(eventInfoRespList)) {
            for (GetEventInfoResp eventInfo : eventInfoRespList) {
                ListEventListResp target = new ListEventListResp();
                BeanUtils.copyProperties(eventInfo, target);
                portalEventListRespList.add(target);
            }
            return portalEventListRespList;
        }
        return portalEventListRespList;
    }


    public List<GetActionInfoResp> findActionListByServiceModuleId(Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<GetActionInfoResp> returnList = Lists.newArrayList();
        List<ServiceModuleActionResp> actionRespList =
                serviceActionApi.findActionListByServiceModuleId(serviceModuleId);
        if (CollectionUtils.isEmpty(actionRespList)) {
            return returnList;
        }
        for (ServiceModuleActionResp action : actionRespList) {

            GetActionInfoResp target = new GetActionInfoResp();
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

    public List<GetEventInfoResp> findEventListByServiceModuleId(Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<GetEventInfoResp> returnList = Lists.newArrayList();
        List<ServiceModuleEventResp> eventRespList = serviceEventApi.findEventListByServiceModuleId(serviceModuleId);
        if (CollectionUtils.isEmpty(eventRespList)) {
            return returnList;
        }
        for (ServiceModuleEventResp event : eventRespList) {
            GetEventInfoResp target = new GetEventInfoResp();
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

    public List<GetPropertyInfoResp> findPropertyInfoListByServiceModuleId(Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<GetPropertyInfoResp> infoRespList = Lists.newArrayList();
        List<ServiceModulePropertyResp> propertyRespList = findPropertyListByServiceModuleId(serviceModuleId);
        if (CollectionUtils.isEmpty(propertyRespList)) {
            return infoRespList;
        }
        for (ServiceModulePropertyResp property : propertyRespList) {
            GetPropertyInfoResp target = new GetPropertyInfoResp();
            BeanUtils.copyProperties(property, target);
            infoRespList.add(target);
        }
        return infoRespList;
    }

    public List<ServiceModulePropertyResp> findPropertiesByServiceModuleId(Long serviceModuleId) {
        List<GetPropertyInfoResp> propertyInfoRespList = findPropertyInfoListByServiceModuleId(serviceModuleId);
        List<ServiceModulePropertyResp> portalPropertyList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(propertyInfoRespList)) {
            for (GetPropertyInfoResp propertyInfo : propertyInfoRespList) {
                ServiceModulePropertyResp target = new ServiceModulePropertyResp();
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
