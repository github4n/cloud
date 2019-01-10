package com.iot.smarthome.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.smarthome.api.DeviceClassifyApi;
import com.iot.smarthome.service.CommonService;
import com.iot.smarthome.util.AttributeUtil;
import com.iot.smarthome.vo.DeviceTypeAttributeResp;
import com.iot.smarthome.vo.resp.DeviceClassifyResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/14 9:44
 * @Modify by:
 */

@Deprecated
@RestController
@RequestMapping("/smarthome/v1/device")
@Api("smartHome设备分类属性 对外接口,v1版本")
public class SmartHomeV1DeviceTypeAttributeController {
    private Logger LOGGER = LoggerFactory.getLogger(SmartHomeV1DeviceTypeAttributeController.class);

    @Autowired
    private CommonService commonService;
    @Autowired
    private DeviceClassifyApi deviceClassifyApi;

    @Deprecated
    @ApiOperation("获取设备分类支持的属性")
    @RequestMapping(value = "/attribute/{deviceType}", method = RequestMethod.GET)
    public JSONObject supportAttributes(@PathVariable("deviceType") String deviceType) {
        LOGGER.info("***** SmartHomeV1DeviceTypeAttributeController, supportAttributes, deviceType={}", deviceType);

        DeviceTypeAttributeResp deviceTypeAttributeResp = new DeviceTypeAttributeResp();

        DeviceClassifyResp deviceClassifyResp = deviceClassifyApi.getByTypeCode(deviceType);
        LOGGER.info("***** deviceClassifyResp={}", JSON.toJSONString(deviceClassifyResp));
        if (deviceClassifyResp != null && deviceClassifyResp.getProductId() != null) {
            List<ServiceModulePropertyResp> serviceModulePropertyList = commonService.findPropertyListByProductId(deviceClassifyResp.getProductId());
            LOGGER.info("***** serviceModulePropertyList={}", JSON.toJSONString(serviceModulePropertyList));
            if (CollectionUtils.isNotEmpty(serviceModulePropertyList)) {
                for (ServiceModulePropertyResp propertyResp : serviceModulePropertyList) {
                    Map<String, Object> attr = Maps.newHashMap();
                    attr.put("name", propertyResp.getCode());
                    attr.put("unit", propertyResp.getDescription());

                    // 数据类型
                    String dataType = AttributeUtil.parseParamType(propertyResp.getParamType());
                    attr.put("type", dataType);
                    attr.put("rw", AttributeUtil.parseRwStatus(propertyResp.getRwStatus()));

                    Object minObj = AttributeUtil.parseMinMax(dataType, propertyResp.getMinValue());
                    if (minObj != null) {
                        attr.put("min", minObj);
                    }
                    Object maxObj = AttributeUtil.parseMinMax(dataType, propertyResp.getMaxValue());
                    if (maxObj != null) {
                        attr.put("max", maxObj);
                    }

                    deviceTypeAttributeResp.addAttr(attr);
                }
            }
        }

        Map<String, Object> resultMap = deviceTypeAttributeResp.buildMsg();
        JSONObject resultJsonObj = new JSONObject(resultMap);
        LOGGER.info("***** SmartHomeV1DeviceTypeAttributeController, supportAttributes, response data ={}", resultJsonObj);
        return resultJsonObj;
    }
}
