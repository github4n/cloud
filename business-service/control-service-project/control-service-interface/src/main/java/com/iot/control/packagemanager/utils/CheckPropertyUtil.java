package com.iot.control.packagemanager.utils;

import com.alibaba.fastjson.JSON;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.execption.StrategyExceptionEnum;
import com.iot.device.vo.rsp.servicemodule.PropertyResp;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CheckPropertyUtil {
    /**
     *@description：校验属性中的值是否正确
     *@author wucheng
     *@params [currentVal, resp]
     *@create 2018/12/5 16:09
     *@return void
     */
    public static void compareProperty(Object currentVal, PropertyResp resp) {
        if (currentVal != null) {
            if (resp != null) {
                // paramType: "参数类型(0:int, 1:float, 2:bool, 3:enum, 4:string")
                if (resp.getParamType() == 0) { // int
                    int val = Integer.parseInt(currentVal.toString());
                    int minVal = Integer.parseInt(resp.getMinVal());
                    int maxVal = Integer.parseInt(resp.getMaxVal());
                    if (val < minVal || val > maxVal) {
                        throw new BusinessException(StrategyExceptionEnum.PROPERTY_ERROR, "property value error");
                    }
                } else if (resp.getParamType() == 1) { // float
                    Float val = Float.parseFloat(currentVal.toString());
                    Float minVal = Float.parseFloat(resp.getMinVal());
                    Float maxVal = Float.parseFloat(resp.getMaxVal());
                    if (val < minVal || val > maxVal) {
                        throw new BusinessException(StrategyExceptionEnum.PROPERTY_ERROR, "property value error");
                    }
                } else if (resp.getParamType() == 2) { // bool
                    compareEnumOrBooleanType(currentVal, resp);
                } else if (resp.getParamType() == 3) {  //enum
                    compareEnumOrBooleanType(currentVal, resp);
                } else if (resp.getParamType() == 4) {

                }
            } else {
                throw new BusinessException(StrategyExceptionEnum.PROPERTY_NOT_EXIST, "property is not exit");
            }
        } else {
            log.info("属性名称：" + resp.getName() + " 值没有设置");
        }
    }
    /**
     *@description：校验属性类型enum 和 boolean属性值是否正确
     *@author wucheng
     *@params [currentVal, resp]
     *@create 2018/12/5 16:09
     *@return void
     */
    public static void compareEnumOrBooleanType(Object currentVal, PropertyResp resp) {
        if (StringUtil.isNotBlank(resp.getAllowedValues())) {
            List<PropertyEnumType> jsonArray = JSON.parseArray(resp.getAllowedValues(), PropertyEnumType.class);
            if (null != jsonArray && jsonArray.size() > 0) { // .substring(t.getValue().lastIndexOf("_") + 1, t.getValue().length())暂时校验这个值，不去掉前缀paramsName_属性id_value
                List<PropertyEnumType> result = jsonArray.stream()
                        .filter(t-> t.getValue().equals(currentVal))
                        .collect(Collectors.toList());
                if (result.size() <= 0) {
                    throw new BusinessException(StrategyExceptionEnum.PROPERTY_NOT_EXIST, "property is not exit");
                }
            }
        }
    }
}
