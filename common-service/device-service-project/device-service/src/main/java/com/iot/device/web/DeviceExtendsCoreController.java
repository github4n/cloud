package com.iot.device.web;

import com.google.common.collect.Lists;
import com.iot.device.api.DeviceExtendsCoreApi;
import com.iot.device.business.DeviceExtendBusinessService;
import com.iot.device.business.core.DeviceCoreBusinessService;
import com.iot.device.model.DeviceExtend;
import com.iot.device.vo.req.device.ListDeviceExtendReq;
import com.iot.device.vo.req.device.UpdateDeviceExtendReq;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceExtendRespVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:03 2018/9/25
 * @Modify by:
 */
@Slf4j
@RestController
public class DeviceExtendsCoreController implements DeviceExtendsCoreApi {

    @Autowired
    private DeviceCoreBusinessService deviceCoreBusinessService;


    @Autowired
    private DeviceExtendBusinessService deviceExtendBusinessService;

    @Override
    public List<ListDeviceExtendRespVo> listDeviceExtends(@RequestBody @Validated ListDeviceExtendReq params) {
        List<ListDeviceExtendRespVo> resultDataList = Lists.newArrayList();
        List<DeviceExtend> sourceDataList = deviceExtendBusinessService.listBatchDeviceExtends(params.getTenantId(), params.getDeviceIds());
        if (CollectionUtils.isEmpty(sourceDataList)) {
            return resultDataList;
        }
        sourceDataList.forEach(source -> {
            ListDeviceExtendRespVo target = new ListDeviceExtendRespVo();
            BeanUtils.copyProperties(source, target);
            resultDataList.add(target);
        });
        return resultDataList;
    }

    @Override
    public GetDeviceExtendInfoRespVo get(@RequestParam(value = "tenantId", required = true) Long tenantId
            , @RequestParam(value = "deviceId", required = true) String deviceId) {
        GetDeviceExtendInfoRespVo resultData = null;
        DeviceExtend sourceData = deviceExtendBusinessService.getDeviceExtend(tenantId, deviceId);
        if (null != sourceData) {
            resultData = new GetDeviceExtendInfoRespVo();
            BeanUtils.copyProperties(sourceData, resultData);
        }
        return resultData;
    }

    @Override
    public void saveOrUpdate(@RequestBody @Validated UpdateDeviceExtendReq params) {
        log.debug("saveOrUpdateDeviceExtend:{}", params.toString());
        deviceExtendBusinessService.saveOrUpdate(params);
    }

    @Override
    public void saveOrUpdateBatch(@RequestBody @Validated List<UpdateDeviceExtendReq> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return;
        }
        deviceExtendBusinessService.saveOrUpdateBatch(paramsList);
    }

}
