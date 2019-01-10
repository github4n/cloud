package com.iot.device.web;

import com.google.common.collect.Lists;
import com.iot.device.api.DeviceStatusCoreApi;
import com.iot.device.business.DeviceStatusBusinessService;
import com.iot.device.business.core.DeviceCoreBusinessService;
import com.iot.device.model.DeviceStatus;
import com.iot.device.service.IDeviceStatusService;
import com.iot.device.vo.req.device.ListDeviceStateReq;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceStatusRespVo;
import com.iot.device.web.utils.DeviceStatusCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:03 2018/9/25
 * @Modify by:
 */
@RestController
public class DeviceStatusCoreController implements DeviceStatusCoreApi {

    @Autowired
    private DeviceCoreBusinessService deviceCoreBusinessService;


    @Autowired
    private DeviceStatusBusinessService deviceStatusBusinessService;

    @Autowired
    private IDeviceStatusService deviceStatusService;

    @Override
    public List<ListDeviceStatusRespVo> listDeviceStatus(@RequestBody @Validated ListDeviceStateReq params) {
        List<ListDeviceStatusRespVo> resultDataList = Lists.newArrayList();
        List<DeviceStatus> sourceDataList = deviceStatusBusinessService.listBatchDeviceStatus(params.getTenantId(), params.getDeviceIds());
        if (CollectionUtils.isEmpty(sourceDataList)) {
            return resultDataList;
        }
        sourceDataList.forEach(source -> {
            ListDeviceStatusRespVo target = new ListDeviceStatusRespVo();
            DeviceStatusCopyUtils.copyProperties(source, target);
            resultDataList.add(target);
        });
        return resultDataList;
    }

    @Override
    public GetDeviceStatusInfoRespVo get(@RequestParam(value = "tenantId", required = true) Long tenantId
            , @RequestParam(value = "deviceId", required = true) String deviceId) {
        GetDeviceStatusInfoRespVo resultData = null;
        DeviceStatus sourceData = deviceStatusBusinessService.getDevice(tenantId, deviceId);
        if (null != sourceData) {
            resultData = new GetDeviceStatusInfoRespVo();
            DeviceStatusCopyUtils.copyProperties(sourceData, resultData);
        }
        return resultData;
    }

    @Override
    public void saveOrUpdate(@RequestBody @Validated UpdateDeviceStatusReq params) {

        deviceStatusBusinessService.saveOrUpdate(params);
    }

    @Override
    public void saveOrUpdateBatch(@RequestBody @Validated List<UpdateDeviceStatusReq> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return;
        }

        deviceStatusBusinessService.saveOrUpdateBatch(paramsList);
    }

}
