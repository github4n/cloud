package com.iot.portal.web.utils;

import com.google.common.collect.Lists;
import com.iot.portal.web.vo.PortalActionListResp;
import com.iot.portal.web.vo.PortalEventListResp;
import com.iot.portal.web.vo.PortalPropertyListResp;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:20 2018/7/4
 * @Modify by:
 */
public class ModuleUtils {

    public static List<PortalActionListResp> parseActions(List<PortalActionListResp> parentActionList, List<PortalActionListResp> productActionList) {
        if (CollectionUtils.isEmpty(parentActionList)) {
            return Lists.newArrayList();
        }
        if (CollectionUtils.isEmpty(productActionList)) {
            return parentActionList;
        } else {
            List<PortalActionListResp> targetActionList = Lists.newArrayList();
            for (PortalActionListResp parentAction : parentActionList) {
                boolean whetherActionCheck = false;
                PortalActionListResp tempAction = null;
                for (PortalActionListResp productAction : productActionList) {
                    if (parentAction.getActionInfo().getId().compareTo(productAction.getActionInfo().getParentId()) != 0) {
                        continue;
                    }
                    whetherActionCheck = true;
                    tempAction = productAction;
                    break;
                }
                if (whetherActionCheck) {
                    tempAction.setWhetherCheck(whetherActionCheck);
                } else {
                    tempAction = parentAction;
                }
                targetActionList.add(tempAction);
            }
            return targetActionList;
        }
    }

    public static List<PortalEventListResp> parseEvents(List<PortalEventListResp> parentEventList, List<PortalEventListResp> productEventList) {
        if (CollectionUtils.isEmpty(parentEventList)) {
            return Lists.newArrayList();
        }
        if (CollectionUtils.isEmpty(productEventList)) {
            return parentEventList;
        } else {
            List<PortalEventListResp> targetEventList = Lists.newArrayList();
            for (PortalEventListResp parentEvent : parentEventList) {
                boolean whetherEventCheck = false;
                PortalEventListResp tempEvent = null;
                for (PortalEventListResp productEvent : productEventList) {
                    if (parentEvent.getEventInfo().getId().compareTo(productEvent.getEventInfo().getParentId()) != 0) {
                        continue;
                    }
                    whetherEventCheck = true;
                    tempEvent = productEvent;
                    break;
                }
                if (whetherEventCheck) {
                    tempEvent.setWhetherCheck(whetherEventCheck);
                } else {
                    tempEvent = parentEvent;
                }
                targetEventList.add(tempEvent);
            }
            return targetEventList;
        }
    }

    public static List<PortalPropertyListResp> parseProperties(List<PortalPropertyListResp> parentPropertyList, List<PortalPropertyListResp> productPropertyList) {
        if (CollectionUtils.isEmpty(parentPropertyList)) {
            return Lists.newArrayList();
        }
        if (CollectionUtils.isEmpty(productPropertyList)) {
            return parentPropertyList;
        } else {
            List<PortalPropertyListResp> targetPropertyList = Lists.newArrayList();
            for (PortalPropertyListResp parentProperty : parentPropertyList) {
                boolean whetherPropertyCheck = false;
                PortalPropertyListResp tempProperty = null;
                for (PortalPropertyListResp productProperty : productPropertyList) {
                    if (parentProperty.getId().compareTo(productProperty.getParentId()) != 0) {
                        continue;
                    }
                    //相同
                    whetherPropertyCheck = true;
                    tempProperty = productProperty;
                    break;
                }
                if (whetherPropertyCheck) {
                    tempProperty.setWhetherCheck(whetherPropertyCheck);
                } else {
                    tempProperty = parentProperty;
                }
                targetPropertyList.add(tempProperty);
            }
            return targetPropertyList;
        }
    }
}
