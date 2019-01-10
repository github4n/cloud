package com.iot.shcs.module.vo.utils;

import com.google.common.collect.Lists;
import com.iot.shcs.module.vo.resp.ListActionListResp;
import com.iot.shcs.module.vo.resp.ListEventListResp;
import com.iot.shcs.module.vo.resp.ListPropertyListResp;
import org.springframework.util.CollectionUtils;

import java.util.List;

/** @Author: xfz @Descrpiton: @Date: 17:20 2018/7/4 @Modify by: */
public class ModuleUtils {

  public static List<ListActionListResp> parseActions(
      List<ListActionListResp> parentActionList, List<ListActionListResp> productActionList) {
    if (CollectionUtils.isEmpty(parentActionList)) {
      return Lists.newArrayList();
    }
    if (CollectionUtils.isEmpty(productActionList)) {
      return parentActionList;
    } else {
      List<ListActionListResp> targetActionList = Lists.newArrayList();
      for (ListActionListResp parentAction : parentActionList) {
        boolean whetherActionCheck = false;
        ListActionListResp tempAction = null;
        for (ListActionListResp productAction : productActionList) {
          if (parentAction
                  .getActionInfo()
                  .getId()
                  .compareTo(productAction.getActionInfo().getParentId())
              != 0) {
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

  public static List<ListEventListResp> parseEvents(
      List<ListEventListResp> parentEventList, List<ListEventListResp> productEventList) {
    if (CollectionUtils.isEmpty(parentEventList)) {
      return Lists.newArrayList();
    }
    if (CollectionUtils.isEmpty(productEventList)) {
      return parentEventList;
    } else {
      List<ListEventListResp> targetEventList = Lists.newArrayList();
      for (ListEventListResp parentEvent : parentEventList) {
        boolean whetherEventCheck = false;
        ListEventListResp tempEvent = null;
        for (ListEventListResp productEvent : productEventList) {
          if (parentEvent
                  .getEventInfo()
                  .getId()
                  .compareTo(productEvent.getEventInfo().getParentId())
              != 0) {
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

  public static List<ListPropertyListResp> parseProperties(
      List<ListPropertyListResp> parentPropertyList,
      List<ListPropertyListResp> productPropertyList) {
    if (CollectionUtils.isEmpty(parentPropertyList)) {
      return Lists.newArrayList();
    }
    if (CollectionUtils.isEmpty(productPropertyList)) {
      return parentPropertyList;
    } else {
      List<ListPropertyListResp> targetPropertyList = Lists.newArrayList();
      for (ListPropertyListResp parentProperty : parentPropertyList) {
        boolean whetherPropertyCheck = false;
        ListPropertyListResp tempProperty = null;
        for (ListPropertyListResp productProperty : productPropertyList) {
          if (parentProperty.getId().compareTo(productProperty.getParentId()) != 0) {
            continue;
          }
          // 相同
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
