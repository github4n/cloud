package com.iot.boss.util;

import com.google.common.collect.Lists;
import com.iot.boss.vo.module.BossActionInfoResp;
import com.iot.boss.vo.module.BossEventInfoResp;
import com.iot.boss.vo.module.BossPropertyInfoResp;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:20 2018/7/4
 * @Modify by:
 */
public class ModuleUtils {

    public static List<BossActionInfoResp> parseActions(List<BossActionInfoResp> parentActionList, List<BossActionInfoResp> productActionList) {
        if (CollectionUtils.isEmpty(parentActionList)) {
            return Lists.newArrayList();
        }
        if (CollectionUtils.isEmpty(productActionList)) {
            return Lists.newArrayList();
        } else {
            List<BossActionInfoResp> targetActionList = Lists.newArrayList();
            for (BossActionInfoResp parentAction : parentActionList) {
                BossActionInfoResp tempAction = null;
                for (BossActionInfoResp productAction : productActionList) {
                    if (parentAction.getActionInfo().getId().compareTo(productAction.getActionInfo().getParentId()) != 0) {
                        continue;
                    }
                    tempAction = productAction;
                    break;
                }
                if (tempAction!=null) {
                    targetActionList.add(tempAction);
                }
            }
            return targetActionList;
        }
    }

    public static List<BossEventInfoResp> parseEvents(List<BossEventInfoResp> parentEventList, List<BossEventInfoResp> productEventList) {
        if (CollectionUtils.isEmpty(parentEventList)) {
            return Lists.newArrayList();
        }
        if (CollectionUtils.isEmpty(productEventList)) {
            return parentEventList;
        } else {
            List<BossEventInfoResp> targetEventList = Lists.newArrayList();
            for (BossEventInfoResp parentEvent : parentEventList) {
                boolean whetherEventCheck = false;
                BossEventInfoResp tempEvent = null;
                for (BossEventInfoResp productEvent : productEventList) {
                    if (parentEvent.getEventInfo().getId().compareTo(productEvent.getEventInfo().getParentId()) != 0) {
                        continue;
                    }
                    whetherEventCheck = true;
                    tempEvent = productEvent;
                    break;
                }
                if (tempEvent!=null) {
                    targetEventList.add(tempEvent);
                }
            }
            return targetEventList;
        }
    }

    public static List<BossPropertyInfoResp> parseProperties(List<BossPropertyInfoResp> parentPropertyList, List<BossPropertyInfoResp> productPropertyList) {
        if (CollectionUtils.isEmpty(parentPropertyList)) {
            return Lists.newArrayList();
        }
        if (CollectionUtils.isEmpty(productPropertyList)) {
            return parentPropertyList;
        } else {
            List<BossPropertyInfoResp> targetPropertyList = Lists.newArrayList();
            for (BossPropertyInfoResp parentProperty : parentPropertyList) {
                boolean whetherPropertyCheck = false;
                BossPropertyInfoResp tempProperty = null;
                for (BossPropertyInfoResp productProperty : productPropertyList) {
                    if (parentProperty.getId().compareTo(productProperty.getParentId()) != 0) {
                        continue;
                    }
                    //相同
                    whetherPropertyCheck = true;
                    tempProperty = productProperty;
                    break;
                }
                /*if (!whetherPropertyCheck) {
                    tempProperty = parentProperty;
                }
                targetPropertyList.add(tempProperty);*/
                if (tempProperty!=null) {
                    targetPropertyList.add(tempProperty);
                }
            }
            return targetPropertyList;
        }
    }
}
