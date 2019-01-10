package com.iot.building.callback.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iot.building.callback.ListenerCallback;
import com.iot.common.enums.APIType;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.vo.req.device.AddCommDeviceStateInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;

/**
 * 设备状态上报回调
 *
 * @author fenglijian
 */
public class DeviceReportCallback implements ListenerCallback {

    private static final Logger logger = LoggerFactory.getLogger(DeviceReportCallback.class);

    private SpaceDeviceApi spaceDeviceApi = ApplicationContextHelper.getBean(SpaceDeviceApi.class);

    private DeviceStateCoreApi deviceStateCoreApi = ApplicationContextHelper.getBean(DeviceStateCoreApi.class);

    /**
     * 处理不同类型的网关上报的数据
     */
    @Override
    public void callback(GetDeviceInfoRespVo device, Map<String, Object> map, APIType apiType) {
        // 多协议网关
        if ("MultiProtocolGateway".equals(apiType.MultiProtocolGateway.name())) {
            String deviceId = device.getUuid();
            try {
                saveReportStatus(device, map);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将多协议sensor上报的数据保存
     *
     * @throws BusinessException
     */
    public void saveReportStatus(GetDeviceInfoRespVo device, Map<String, Object> map) throws BusinessException {
        if (StringUtils.isNotBlank(device.getUuid())) {
            SpaceDeviceReq req = new SpaceDeviceReq();
            req.setDeviceId(device.getUuid());
            req.setTenantId(device.getTenantId());
            SpaceDeviceResp space = spaceDeviceApi.findSpaceDeviceByCondition(req).get(0);
            List<AddCommDeviceStateInfoReq> infoList = new ArrayList<>();
            if (MapUtils.isNotEmpty(map)) {
                for (String key : map.keySet()) {
                	AddCommDeviceStateInfoReq reqInfo = new AddCommDeviceStateInfoReq();
                    reqInfo.setPropertyName(key);
                    reqInfo.setPropertyValue(map.get(key).toString());
                    infoList.add(reqInfo);
                }
            }
            try {
            	UpdateDeviceStateReq deviceStateReq=new UpdateDeviceStateReq();
                deviceStateReq.setDeviceId(device.getUuid());
                deviceStateReq.setStateList(infoList);
                deviceStateReq.setTenantId(device.getTenantId());
                deviceStateCoreApi.saveOrUpdate(deviceStateReq);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }

    }

}
