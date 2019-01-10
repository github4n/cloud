package com.iot.widget.service;

import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.enums.OnlineStatusEnum;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.shcs.voicebox.api.VoiceBoxApi;
import com.iot.shcs.voicebox.vo.SetDevAttrDTO;
import com.iot.user.api.UserApi;
import com.iot.widget.exception.UserWidgetExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/7 11:15
 * 修改人:
 * 修改时间：
 */

@Service("widgetDeviceService")
public class DeviceService {
    private Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    @Autowired
    private DeviceCoreServiceApi deviceCoreServiceApi;
    @Autowired
    private DeviceStateCoreApi deviceStateCoreApi;
    @Autowired
    private UserApi userApi;
    @Autowired
    private VoiceBoxApi voiceBoxApi;

    /**
     *  执行设备请求
     *
     * @param tenantId
     * @param userId
     * @param deviceId
     * @param changeAttrMap
     * @return
     */
    public boolean setDevAttr(Long tenantId, Long userId, String deviceId, Map<String, Object> changeAttrMap) {
        // 判断用户是否关联设备
        ListUserDeviceInfoRespVo userDevice = deviceCoreServiceApi.getUserDevice(tenantId, userId, deviceId);
        if (userDevice == null) {
            LOGGER.info("***** setDevAttr, device unbound with user, deviceId={}, userId={}", deviceId, userId);
            throw new BusinessException(UserWidgetExceptionEnum.DEVICE_NOT_FOUND);
        }

        // 判断设备是否存在
        GetDeviceInfoRespVo deviceResp = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
        if (deviceResp == null) {
            throw new BusinessException(UserWidgetExceptionEnum.DEVICE_NOT_FOUND);
        }

        // 判断设备在线状态
        GetDeviceStatusInfoRespVo deviceStatusInfo = deviceCoreServiceApi.getDeviceStatusByDeviceId(deviceResp.getTenantId(), deviceId);
        if (!OnlineStatusEnum.CONNECTED.getCode().equals(deviceStatusInfo.getOnlineStatus())) {
            LOGGER.info("***** setDevAttr, device is disconnected, deviceId={}", deviceId);
            throw new BusinessException(UserWidgetExceptionEnum.DEVICE_OFFLINE);
        }

        // 控制之前设备属性状态
        Map<String, Object> oldStateMap = null;
        try {
            oldStateMap = deviceStateCoreApi.get(tenantId, deviceId);

            LOGGER.info("***** setDevAttr, will changeAttrMap={}, oldStateMap={}", changeAttrMap, oldStateMap);

            SetDevAttrDTO devAttrDTO = new SetDevAttrDTO();
            devAttrDTO.setDeviceId(deviceId);
            devAttrDTO.setAttr(changeAttrMap);

            voiceBoxApi.setDevAttrReq(devAttrDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(UserWidgetExceptionEnum.UN_KNOWN_ERROR);
        }

        LOGGER.info("***** setDevAttr, after setDevAttrReq, query final attr status, deviceId={}", deviceId);

        if (oldStateMap == null) {
            oldStateMap = Maps.newHashMap();
        }

        Map<String, Object> theSameAttrMap = Maps.newHashMap();
        try {
            // 确认 控制是否成功
            // 先睡眠600毫秒
            Thread.sleep(600L);

            LOGGER.info("***** setDevAttr, changeAttrMap.size={}", changeAttrMap.size());

            for (int loopCount = 0; loopCount < 6; loopCount++) {
                // 当前最新的 设备属性状态
                Map<String, Object> newStateMap = deviceStateCoreApi.get(tenantId, deviceId);
                LOGGER.info("***** loopCount = {}, newStateMap={}", loopCount, newStateMap);

                if (newStateMap != null) {
                    Iterator iter = changeAttrMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String targetKey = (String) entry.getKey();
                        Object targetVal = entry.getValue();

                        // 旧属性值
                        Object oldValue = oldStateMap.get(targetKey);
                        // 新值
                        Object newValue = newStateMap.get(targetKey);

                        LOGGER.info("***** loopCount = {}, targetKey={}, targetVal={}, oldValue={}, newValue={}", loopCount, targetKey, targetVal, oldValue, newValue);
                        if (newValue != null) {
                            if (oldValue == null) {
                                // 旧属性值为空, 新属性值不为空 (说明值有改变了)
                                LOGGER.info("***** break for loop, 【 newValue != null && oldValue == null 】");
                                theSameAttrMap.put(targetKey, targetVal);
                                continue;
                            }

                            if (targetVal instanceof Integer) {
                                // 整数
                                Integer oldValue_i = Integer.parseInt(String.valueOf(oldValue));
                                Integer newValue_i = Integer.parseInt(String.valueOf(newValue));

                                if (oldValue_i.compareTo(newValue_i) != 0) {
                                    LOGGER.info("***** break for loop, oldValue_i={}, newValue_i={}", oldValue_i, newValue_i);
                                    theSameAttrMap.put(targetKey, targetVal);
                                    continue;
                                } else if (newValue_i.compareTo((Integer) targetVal) == 0) {
                                    LOGGER.info("***** break for loop, targetVal={}, newValue_i={}", targetVal, newValue_i);
                                    theSameAttrMap.put(targetKey, targetVal);
                                    continue;
                                }
                            } else {
                                // 非整数
                                String oldValue_s = String.valueOf(oldValue);
                                String newValue_s = String.valueOf(newValue);
                                if (!oldValue_s.equals(newValue_s)) {
                                    LOGGER.info("***** break for loop, oldValue_s={}, newValue_s={}", oldValue_s, newValue_s);
                                    theSameAttrMap.put(targetKey, targetVal);
                                    continue;
                                } else if (newValue_s.equals(String.valueOf(targetVal))) {
                                    LOGGER.info("***** break for loop, targetVal={}, newValue_s={}", targetVal, newValue_s);
                                    theSameAttrMap.put(targetKey, targetVal);
                                    continue;
                                }
                            }
                        }
                    }
                }

                LOGGER.info("***** setDevAttr, theSameAttrMap.size={}, changeAttrMap.size={}", theSameAttrMap.size(), changeAttrMap.size());

                if (theSameAttrMap.size() == changeAttrMap.size()) {
                    LOGGER.info("***** setDevAttr, break for loop");
                    return true;
                }

                try {
                    // 睡眠400毫秒再查询一次
                    Thread.sleep(400L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOGGER.error("setDevAttr execute timeout, deviceId={}, userId={}, tenantId={}, changeAttrMap={}", deviceId, userId, tenantId, changeAttrMap);

        // 请求超时
        throw new BusinessException(UserWidgetExceptionEnum.TIMEOUT);
    }
}
