package com.iot.device.controller;

import com.iot.device.api.DeviceTypeToStyleApi;
import com.iot.device.service.IDeviceTypeToStyleService;
import com.iot.device.vo.req.DeviceTypeToStyleReq;
import com.iot.device.vo.rsp.DeviceTypeToStyleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DeviceTypeToStyleController implements DeviceTypeToStyleApi{


    @Autowired
    private IDeviceTypeToStyleService iDeviceTypeToStyleService;

    @Override
    public Long saveOrUpdate(@RequestBody DeviceTypeToStyleReq deviceTypeToStyleReq) {
        return iDeviceTypeToStyleService.saveOrUpdate(deviceTypeToStyleReq);
    }

    @Override
    public void saveMore(@RequestBody DeviceTypeToStyleReq deviceTypeToStyleReq) {
        iDeviceTypeToStyleService.saveMore(deviceTypeToStyleReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        iDeviceTypeToStyleService.delete(ids);
    }

    @Override
    public List<DeviceTypeToStyleResp> listDeviceTypeStyleByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId) {
        return iDeviceTypeToStyleService.listDeviceTypeStyleByDeviceTypeId(deviceTypeId);
    }
}
