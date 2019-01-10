package com.iot.robot.service;

import com.alibaba.fastjson.JSONArray;
import com.google.api.client.util.Maps;
import com.google.common.collect.Lists;
import com.iot.common.util.StringUtil;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.api.DataPointApi;
import com.iot.device.api.DeviceCatalogApi;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.vo.req.DeviceCatalogReq;
import com.iot.device.vo.rsp.DeviceCatalogRes;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.robot.common.constant.DeviceAttrConst;
import com.iot.robot.common.constant.DeviceOnlineStatusEnum;
import com.iot.robot.common.constant.ModuleConstants;
import com.iot.robot.common.exception.RobotException;
import com.iot.robot.norm.KeyValue;
import com.iot.robot.transform.convertor.YunKeyValue;
import com.iot.robot.utils.ErrorCodeKeys;
import com.iot.robot.vo.DeviceInfo;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.shcs.voicebox.api.VoiceBoxApi;
import com.iot.shcs.voicebox.vo.SetDevAttrDTO;
import com.iot.user.api.UserApi;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Descrpiton: 设备相关操作service
 * @Author: yuChangXing
 * @Date: 2018/10/8 11:47
 * @Modify by:
 */

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    @Autowired
    private CommonService commonService;
    @Autowired
    private DeviceCoreServiceApi deviceCoreServiceApi;
    @Autowired
    private DeviceStateCoreApi deviceStateCoreApi;
    @Autowired
    private DataPointApi dataPointApi;
    @Autowired
    private UserApi userApi;

    @Autowired
    private DeviceTypeApi deviceTypeApi;

    @Autowired
    private VoiceBoxApi voiceBoxApi;



    /**
     *  获取用户 网关(gateWay) 的uuid
     * @param userId
     * @return
     */
    public String getUserGateWayUuid(Long tenantId, Long userId) {
        List<DeviceResp> directDeviceList = deviceCoreServiceApi.findDirectDeviceListByUserId(tenantId, userId);
        if (CollectionUtils.isEmpty(directDeviceList)) {
            return null;
        }

        String directDeviceUuid = null;
        for (DeviceResp dev : directDeviceList) {
            GetProductInfoRespVo pro = deviceCoreServiceApi.getProductById(dev.getProductId());
            if (this.isGateWayProduct(pro)) {
                directDeviceUuid = dev.getDeviceId();
                break;
            }
        }

        return directDeviceUuid;
    }

    /**
     * 获取用户的 设备信息
     *
     * @param userId
     * @return
     */
    public List<DeviceInfo> findDeviceListByUserId(Long tenantId, Long userId) {
        List<DeviceInfo> deviceInfoList = Lists.newArrayList();
        List<DeviceResp> deviceRespList = deviceCoreServiceApi.findDeviceListByUserId(tenantId, userId);
        if (CollectionUtils.isNotEmpty(deviceRespList)) {
            for (DeviceResp dr : deviceRespList) {
                DeviceInfo devInfo = new DeviceInfo();
                devInfo.setDeviceId(dr.getDeviceId());
                devInfo.setTenantId(dr.getTenantId());
                devInfo.setProductId(dr.getProductId());
                devInfo.setDeviceName(dr.getName());
                devInfo.setIsDirectDevice(dr.getIsDirectDevice());

                devInfo.setDeviceTypeId(dr.getDeviceTypeId());
                devInfo.setDeviceType(dr.getDevType());

                /*List<DeviceFunResp> deviceFunRespList = dataPointApi.findDataPointListByDeviceId(dr.getDeviceId());
                devInfo.setDeviceFunList(deviceFunRespList);*/
                List<ServiceModulePropertyResp> serviceModulePropertyList = commonService.findPropertyListByDeviceId(dr.getDeviceId());
                // 去掉重复的 code
                if (CollectionUtils.isNotEmpty(serviceModulePropertyList)) {
                    List<ServiceModulePropertyResp> resultList = Lists.newArrayList();
                    Map<String, String> resultMap = Maps.newHashMap();
                    for (ServiceModulePropertyResp resp : serviceModulePropertyList) {
                        if (resp != null) {
                            if (resultMap.get(resp.getCode()) == null) {
                                resultMap.put(resp.getCode(), resp.getCode());
                                resultList.add(resp);
                            } else {
                                LOGGER.info("***** findDeviceListByUserId, repeat property={}", JSONArray.toJSONString(resp));
                            }
                        }
                    }

                    devInfo.setServiceModulePropertyList(resultList);
                }

                deviceInfoList.add(devInfo);
            }
        }

        return deviceInfoList;
    }

    public GetDeviceInfoRespVo getDeviceInfoRespVo(String deviceId) {
        return deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
    }

    public GetDeviceStatusInfoRespVo getDeviceStatusByDeviceId(Long tenantId, String deviceId) {
        return deviceCoreServiceApi.getDeviceStatusByDeviceId(tenantId, deviceId);
    }

    /**
     * 获取设备状态
     *
     * @param deviceId
     * @param userId
     */
    public Map<String, Object> findDevAttrMap(Long tenantId, String deviceId, Long userId) {
        GetDeviceInfoRespVo deviceResp = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
        LOGGER.info("***** findDevAttrMap, deviceResp.json={}", JSONArray.toJSONString(deviceResp));

        if (deviceResp == null) {
            // 设备不存在
            throw new RobotException(ErrorCodeKeys.DEVICE_NOT_FOUND);
        }

        GetDeviceStatusInfoRespVo deviceStatusInfo = deviceCoreServiceApi.getDeviceStatusByDeviceId(deviceResp.getTenantId(), deviceId);

        ListUserDeviceInfoRespVo userDeviceRelationShipResp = deviceCoreServiceApi.getUserDevice(tenantId, userId, deviceId);
        if (userDeviceRelationShipResp == null) {
            // 用户未关联设备
            LOGGER.info("***** findDevAttrMap, device unbound with user, deviceId={}, userId={}", deviceId, userId);
            throw new RobotException(ErrorCodeKeys.DEVICE_NOT_FOUND);
        }

        if (!DeviceOnlineStatusEnum.CONNECTED.getCode().equals(deviceStatusInfo.getOnlineStatus())) {
            // 设备离线
            throw new RobotException(ErrorCodeKeys.DEVICE_OFFLINE);
        }

        Map<String, Object> stateMap = deviceStateCoreApi.get(tenantId, deviceId);
        if (stateMap == null) {
            stateMap = Maps.newHashMap();
        }

        return stateMap;
    }

    /**
     *  设备控制(设置设备属性)
     *
     * @param kvList
     * @param deviceId
     * @param userId
     */
    public boolean setDevAttrFromKV(Long tenantId, List<KeyValue> kvList, String deviceId, Long userId) {
        // 判断用户是否关联设备
        ListUserDeviceInfoRespVo userDevice = deviceCoreServiceApi.getUserDevice(tenantId, userId, deviceId);
        if (userDevice == null) {
            LOGGER.info("***** setDevAttrFromKV, device unbound with user, deviceId={}, userId={}", deviceId, userId);
            throw new RobotException(ErrorCodeKeys.DEVICE_NOT_FOUND);
        }

        // 判断设备是否存在
        GetDeviceInfoRespVo deviceResp = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
        if (deviceResp == null) {
            throw new RobotException(ErrorCodeKeys.DEVICE_NOT_FOUND);
        }

        // 判断设备在线状态
        GetDeviceStatusInfoRespVo deviceStatusInfo = deviceCoreServiceApi.getDeviceStatusByDeviceId(deviceResp.getTenantId(), deviceId);
        if (!DeviceOnlineStatusEnum.CONNECTED.getCode().equals(deviceStatusInfo.getOnlineStatus())) {
            LOGGER.info("***** setDevAttrFromKV, device is disconnected, deviceId={}", deviceId);
            throw new RobotException(ErrorCodeKeys.DEVICE_OFFLINE);
        }

        // 需要改变的属性
        Map<String, Object> changeAttrMap = Maps.newHashMap();
        try {
            for (KeyValue kv : kvList) {
                YunKeyValue yunKeyValue = commonService.parseKeyValue2YunKeyValue(kv, deviceId);
                changeAttrMap.put(yunKeyValue.getKey(), yunKeyValue.getValue());
            }
            LOGGER.info("***** setDevAttrFromKV, will changeAttrMap={}", changeAttrMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RobotException(ErrorCodeKeys.UN_KNOW_ERROR);
        }

        // 判断设备开关状态
        if (!changeAttrMap.containsKey(KeyValue.ONOFF)) {
            this.checkOnOff(tenantId, deviceId);
        }

        // 发起控制
        boolean result = this.setDevAttr(userId, tenantId, deviceId, changeAttrMap);
        return result;
    }

    /**
     *  判断 设备开关状态
     *
     * @param tenantId
     * @param deviceId
     */
    private void checkOnOff(Long tenantId, String deviceId) {
        // 控制之前 设备属性状态
        Map<String, Object> oldStateMap = deviceStateCoreApi.get(tenantId, deviceId);
        LOGGER.info("***** checkOnOff, oldStateMap={}", oldStateMap);
        if (oldStateMap != null) {
            if (oldStateMap.containsKey(KeyValue.ONOFF)) {
                Object onOffTemp = oldStateMap.get(KeyValue.ONOFF);
                String onOffStr = onOffTemp == null ? "" : String.valueOf(onOffTemp);
                if (DeviceAttrConst.DEVICE_ON_OFF_0.equals(onOffStr)) {
                    LOGGER.info("***** checkOnOff, device onOff is off, deviceId={}", deviceId);
                    throw new RobotException(ErrorCodeKeys.DEVICE_TURNED_OFF);
                }
            }
        }
    }

    /**
     *  控制设备
     *
     * @param tenantId
     * @param deviceId
     * @param changeAttrMap
     * @return
     */
    public boolean setDevAttr(Long userId, Long tenantId, String deviceId, Map<String, Object> changeAttrMap) {
        // 控制之前设备属性状态
        Map<String, Object> oldStateMap = null;
        try {
            oldStateMap = deviceStateCoreApi.get(tenantId, deviceId);

            LOGGER.info("***** setDevAttr, will changeAttrMap={}, oldStateMap={}", changeAttrMap, oldStateMap);

            // 判断是否IPC
            boolean isIPC = commonService.isIPC(deviceId);
            Map<String, Object> payloadMap = null;
            if (isIPC) {
                ListUserDeviceInfoRespVo userDevice = deviceCoreServiceApi.getUserDevice(tenantId, userId, deviceId);
                String userUuid = userApi.getUuid(userId);
                payloadMap = Maps.newHashMap();
                payloadMap.put("userId", userUuid);
                payloadMap.put("password", userDevice.getPassword());
            }

            SetDevAttrDTO devAttrDTO = new SetDevAttrDTO();
            devAttrDTO.setDeviceId(deviceId);
            devAttrDTO.setPayloadMap(payloadMap);
            devAttrDTO.setAttr(changeAttrMap);

            voiceBoxApi.setDevAttrReq(devAttrDTO);
        } catch (Exception e) {
            e.printStackTrace();

            throw new RobotException(ErrorCodeKeys.UN_KNOW_ERROR);
        }

        LOGGER.info("***** setDevAttr, after setDevAttrReq, query final attr status, deviceId={}", deviceId);

        if (oldStateMap == null) {
            oldStateMap = Maps.newHashMap();
        }

        // 确认 控制是否成功
        try {
            // 先睡眠600毫秒
            Thread.sleep(600L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, Object> theSameAttrMap = Maps.newHashMap();
        try {
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
        throw new RobotException(ErrorCodeKeys.TIMEOUT);
    }

    /**
     * 是否 gateway 产品
     * @return
     */
    public boolean isGateWayProduct(GetProductInfoRespVo productResp) {
        if (productResp == null || productResp.getIsDirectDevice() == null || productResp.getIsDirectDevice() == 0) {
            return false;
        }

        DeviceTypeResp deviceTypeRes = deviceTypeApi.getDeviceTypeById(productResp.getDeviceTypeId());
        if (deviceTypeRes!=null){
            if(deviceTypeRes.getDeviceCatalogId() != null && deviceTypeRes.getDeviceCatalogId() == 1){
                return true;
            }else{
                return false;
            }
        } else {
            return false;
        }
//        if (StringUtil.isNotBlank(productResp.getModel())) {
//            String model = productResp.getModel().toLowerCase();
//            if (model.contains(".gateway.") || model.contains(".siren_hub.")) {
//                return true;
//            }
//        }
    }

}
