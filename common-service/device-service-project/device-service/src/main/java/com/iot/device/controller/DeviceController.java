package com.iot.device.controller;

import com.iot.device.api.DeviceApi;
import com.iot.device.service.ICountdownService;
import com.iot.device.service.IDeviceExtendService;
import com.iot.device.service.IDeviceService;
import com.iot.device.service.IDeviceStatusService;
import com.iot.device.service.IProductService;
import com.iot.device.vo.rsp.DeviceResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@RestController
public class DeviceController implements DeviceApi {

    public static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private IDeviceService deviceService;


    @Autowired
    private IDeviceStatusService deviceStatusService;

    @Autowired
    private ICountdownService countdownService;

    @Autowired
    private IDeviceExtendService deviceExtendService;

    @Autowired
    private IProductService productService;



    @Override
    public Map<String,Long> findUuidProductIdMap(@RequestParam("uuIdList") List<String> uuIdList) {
        Map<String, Map<String,Long>> rootMap = deviceService.findUuidProductIdMap(uuIdList);
        Map<String, Long> map= rootMap.keySet().parallelStream().map(k->(rootMap.get(k).get("productId")+";"+k).split(";")).collect(Collectors.toMap(e -> e[1], e ->Long.parseLong(e[0]==null?0+"":e[0])));
        return map;
    }

    /**
     * 描述：查设备id与租户id对应关系
     * @author nongchongwei
     * @date 2018/11/1 16:40
     * @param
     * @return
     */
    @Override
    public Map<String, Long> findUuidTenantIdMap(@RequestParam("uuIdList") List<String> uuIdList) {
        Map<String, Map<String,Long>> rootMap = deviceService.findUuidTenantIdMap(uuIdList);
        Map<String, Long> map= rootMap.keySet().parallelStream().map(k->(rootMap.get(k).get("tenantId")+";"+k).split(";")).collect(Collectors.toMap(e -> e[1], e ->Long.parseLong(e[0]==null?0+"":e[0])));
        return map;
    }

    @Override
    public List<DeviceResp> getVersionByDeviceIdList(@RequestParam("deviceIdList")  List<String> deviceIdList) {
        return deviceService.getVersionByDeviceIdList(deviceIdList);
    }

}
