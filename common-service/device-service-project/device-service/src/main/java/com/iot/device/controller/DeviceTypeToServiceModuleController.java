package com.iot.device.controller;

import com.iot.device.api.DeviceTypeToServiceModuleApi;
import com.iot.device.service.IDeviceTypeToServiceModuleService;
import com.iot.device.vo.req.DeviceTypeToServiceModuleReq;
import com.iot.device.vo.rsp.DeviceTypeToServiceModuleResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DeviceTypeToServiceModuleController implements DeviceTypeToServiceModuleApi {

    @Autowired
    private IDeviceTypeToServiceModuleService iDeviceTypeToServiceModuleService;

    @Override
    public Long saveOrUpdate(@RequestBody DeviceTypeToServiceModuleReq deviceTypeToServiceModuleReq) {
        return iDeviceTypeToServiceModuleService.saveOrUpdate(deviceTypeToServiceModuleReq);
    }

    @Override
    public void saveMore(@RequestBody DeviceTypeToServiceModuleReq deviceTypeToServiceModuleReq) {
        iDeviceTypeToServiceModuleService.saveMore(deviceTypeToServiceModuleReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        iDeviceTypeToServiceModuleService.delete(ids);
    }

    @Override
    public List<DeviceTypeToServiceModuleResp> listByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId,@RequestParam("tenantId") Long tenantId) {
        return iDeviceTypeToServiceModuleService.listByDeviceTypeId(deviceTypeId,tenantId);
    }

    @Override
    public List<DeviceTypeToServiceModuleResp> listByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId,@RequestParam("tenantId") Long tenantId) {
        return iDeviceTypeToServiceModuleService.listByServiceModuleId(serviceModuleId,tenantId);
    }

    @Override
    public void update(@RequestParam("id") Long id,@RequestParam("status") Integer status) {
        iDeviceTypeToServiceModuleService.update(id,status);
    }

    /**
     * @despriction：校验设备类型是否有iftttType属性、方法、事件
     * @author  yeshiyuan
     * @created 2018/11/22 14:00
     */
    @Override
    public boolean checkDeviceTypeHadIftttType(@RequestParam("deviceTypeId") Long deviceTypeId) {
        return iDeviceTypeToServiceModuleService.checkDeviceTypeHadIftttType(deviceTypeId);
    }

    /**
     * @despriction：根据ifttt类型找到对应的模组信息
     * @author  yeshiyuan
     * @created 2018/11/22 15:53
     * @return
     */
    @Override
    public PackageServiceModuleDetailResp queryServiceModuleDetailByIfttt(@RequestParam("deviceTypeId") Long deviceTypeId, @RequestParam("iftttType")String iftttType) {
        return iDeviceTypeToServiceModuleService.queryServiceModuleDetailByIfttt(deviceTypeId, iftttType);
    }
}
