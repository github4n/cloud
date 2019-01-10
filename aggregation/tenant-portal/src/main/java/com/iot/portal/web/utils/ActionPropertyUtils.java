package com.iot.portal.web.utils;

import com.google.common.collect.Lists;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton: 方法属性工具
 * @Date: 16:08 2018/7/3
 * @Modify by:
 */
public class ActionPropertyUtils {


    /**
     * 解析获取请求入参参数
     *
     * @param propertyList
     * @return
     * @author lucky
     * @date 2018/7/3 16:13
     */
    public static List<ServiceModulePropertyResp> parseParamProperties(List<ServiceModulePropertyResp> propertyList) {
        List<ServiceModulePropertyResp> properties = Lists.newArrayList();
        if (CollectionUtils.isEmpty(propertyList)) {
            return properties;
        }
        for (ServiceModulePropertyResp property : propertyList) {
            if (property.getPropertyParamType() == 0) {
                properties.add(property);
            }
        }
        return properties;
    }

    /**
     * 解析获取返回参数
     *
     * @param propertyList
     * @return
     * @author lucky
     * @date 2018/7/3 16:13
     */
    public static List<ServiceModulePropertyResp> parseReturnProperties(List<ServiceModulePropertyResp> propertyList) {
        List<ServiceModulePropertyResp> properties = Lists.newArrayList();
        if (CollectionUtils.isEmpty(propertyList)) {
            return properties;
        }
        for (ServiceModulePropertyResp property : propertyList) {
            if (property.getPropertyParamType() == 1) {
                properties.add(property);
            }
        }
        return properties;
    }
}
