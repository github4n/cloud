package com.iot.smarthome.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.activity.vo.rsp.ActivityRecordResp;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.shcs.voicebox.api.VoiceBoxApi;
import com.iot.shcs.voicebox.vo.SetDevAttrDTO;
import com.iot.smarthome.api.DeviceClassifyApi;
import com.iot.smarthome.constant.DeviceAttrConst;
import com.iot.smarthome.constant.DeviceOnlineStatusEnum;
import com.iot.smarthome.constant.SmartHomeErrorCode;
import com.iot.smarthome.exception.SmartHomeException;
import com.iot.smarthome.transfor.converter.KeyValConverter;
import com.iot.smarthome.transfor.kv.YunKeyValue;
import com.iot.smarthome.util.AttributeUtil;
import com.iot.smarthome.util.DateTimeUtil;
import com.iot.smarthome.vo.ActivityVo;
import com.iot.smarthome.vo.DeviceAttributeResp;
import com.iot.smarthome.vo.DeviceInfo;
import com.iot.smarthome.vo.resp.DeviceClassifyResp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/14 13:41
 * @Modify by:
 */

@Service("smartHomeDeviceService")
public class DeviceService {
    private Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    @Autowired
    private DeviceCoreServiceApi deviceCoreServiceApi;
    @Autowired
    private DeviceStateCoreApi deviceStateCoreApi;
    @Autowired
    private DeviceClassifyApi deviceClassifyApi;
    @Autowired
    private CommonService commonService;
    @Autowired
    private VoiceBoxApi voiceBoxApi;
    @Autowired
    private ActivityRecordApi activityRecordApi;


    /**
     *  检查设备是否对外开放
     * @param deviceInfo
     */
    public void checkDeviceClassifyProductXref(GetDeviceInfoRespVo deviceInfo) {
        if (deviceInfo == null) {
            LOGGER.info("checkDeviceClassifyProductXref, deviceInfo is null.");
            throw new SmartHomeException(SmartHomeErrorCode.DEVICE_NOT_FOUND.getCode());
        }

        DeviceClassifyResp deviceClassifyResp = deviceClassifyApi.getDeviceClassifyByProductId(deviceInfo.getProductId());
        if (deviceClassifyResp == null) {
            LOGGER.info("checkDeviceClassifyProductXref, deviceId={}, productId={} deviceClassifyProductXref is null", deviceInfo.getId(), deviceInfo.getProductId());
            throw new SmartHomeException(SmartHomeErrorCode.DEVICE_NOT_FOUND.getCode());
        }
    }

    /**
     *  获取 设备的历史日志
     *
     * @param userId
     * @param tenantId
     * @param deviceId
     * @param startDate
     * @param endDate
     */
    public List<ActivityVo> queryDeviceHistoricalData(Long userId, Long tenantId, String deviceId, Date startDate, Date endDate) {
        ActivityRecordReq req = new ActivityRecordReq();
        req.setCreateBy(userId);
        req.setTenantId(tenantId);
        req.setForeignId(deviceId);
        req.setPageNum(1);
        req.setPageSize(999);
        req.setStartTime(String.valueOf(startDate.getTime()));
        req.setEndTime(String.valueOf(endDate.getTime()));

        List<ActivityVo> activityVoList = Lists.newArrayList();
        PageInfo<ActivityRecordResp> records = activityRecordApi.queryActivityRecord(req);
        if (records.getList() != null) {
            for (ActivityRecordResp r : records.getList()) {
                ActivityVo vo = new ActivityVo();
                vo.setActivity(r.getActivity());
                long time = r.getTime();
                String timeStr = DateTimeUtil.formatDateTime(time);
                vo.setTime(timeStr);
                activityVoList.add(vo);
            }
        }
        return activityVoList;
    }


    /**
     *  控制设备
     *
     * @param userId
     * @param tenantId
     * @param kvList
     * @return
     */
    public boolean setDevAttr(Long userId, Long tenantId, String deviceId, List<YunKeyValue> kvList) {
        LOGGER.info("***** setDevAttr, userId={}, tenantId={}, deviceId={}, yunKeyValue={}", userId, tenantId, JSON.toJSONString(kvList));

        if (StringUtils.isBlank(deviceId)) {
            // 设备不存在
            LOGGER.info("setDevAttr, deviceId is null.");
            throw new SmartHomeException(SmartHomeErrorCode.DEVICE_NOT_FOUND.getCode());
        }

        GetDeviceInfoRespVo deviceResp = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
        this.checkDeviceClassifyProductXref(deviceResp);

        // 判断用户是否关联设备
        ListUserDeviceInfoRespVo userDevice = deviceCoreServiceApi.getUserDevice(tenantId, userId, deviceId);
        if (userDevice == null) {
            LOGGER.info("setDevAttr, device unbound with user, deviceId={}, userId={}", deviceId, userId);
            throw new SmartHomeException(SmartHomeErrorCode.DEVICE_NOT_FOUND.getCode());
        }

        // 判断设备在线状态
        GetDeviceStatusInfoRespVo deviceStatusInfo = deviceCoreServiceApi.getDeviceStatusByDeviceId(deviceResp.getTenantId(), deviceId);
        if (!DeviceOnlineStatusEnum.CONNECTED.getCode().equals(deviceStatusInfo.getOnlineStatus())) {
            // 设备离线
            LOGGER.info("***** setDevAttr, device is disconnected, deviceId={}", deviceId);
            throw new SmartHomeException(SmartHomeErrorCode.DEVICE_OFFLINE.getCode());
        }

        // 需要改变的属性
        Map<String, Object> changeAttrMap = Maps.newHashMap();
        for (YunKeyValue kv : kvList) {
            changeAttrMap.put(kv.getKey(), kv.getValue());
        }

        // 判断设备开关状态
        if (!changeAttrMap.containsKey(KeyValConverter.ONOFF)) {
            this.checkOnOff(tenantId, deviceId);
        }

        // 控制之前设备属性状态
        Map<String, Object> oldStateMap = null;
        try {
            oldStateMap = deviceStateCoreApi.get(tenantId, deviceId);
            LOGGER.info("***** setDevAttr, oldStateMap={}", oldStateMap);

            SetDevAttrDTO devAttrDTO = new SetDevAttrDTO();
            devAttrDTO.setDeviceId(deviceId);
            devAttrDTO.setAttr(changeAttrMap);

            voiceBoxApi.setDevAttrReq(devAttrDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SmartHomeException(SmartHomeErrorCode.INTERNAL_ERROR.getCode());
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

        // 请求超时
        throw new SmartHomeException(SmartHomeErrorCode.TIMEOUT.getCode());
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
            if (oldStateMap.containsKey(KeyValConverter.ONOFF)) {
                Object onOffTemp = oldStateMap.get(KeyValConverter.ONOFF);
                String onOffStr = onOffTemp == null ? "" : String.valueOf(onOffTemp);
                if (DeviceAttrConst.DEVICE_ON_OFF_0.equals(onOffStr)) {
                    LOGGER.info("***** checkOnOff, device onOff is off, deviceId={}", deviceId);
                    throw new SmartHomeException(SmartHomeErrorCode.DEVICE_TURNED_OFF.getCode());
                }
            }
        }
    }


    /**
     *  获取设备最新属性-值
     *
     * @param userId
     * @param tenantId
     * @param deviceId
     * @return
     */
    public Map<String, Object> queryDeviceAttribute(Long userId, Long tenantId, String deviceId) {
        LOGGER.info("queryDeviceAttribute, userId={}, tenantId={}, deviceId={}", userId, tenantId, deviceId);

        if (StringUtils.isBlank(deviceId)) {
            // 设备不存在
            LOGGER.info("queryDeviceAttribute, deviceId is null.");
            throw new SmartHomeException(SmartHomeErrorCode.DEVICE_NOT_FOUND.getCode());
        }

        GetDeviceInfoRespVo deviceResp = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
        this.checkDeviceClassifyProductXref(deviceResp);

        // 判断用户是否关联设备
        ListUserDeviceInfoRespVo userDevice = deviceCoreServiceApi.getUserDevice(tenantId, userId, deviceId);
        if (userDevice == null) {
            LOGGER.info("queryDeviceAttribute, device unbound with user, deviceId={}, userId={}", deviceId, userId);
            throw new SmartHomeException(SmartHomeErrorCode.DEVICE_NOT_FOUND.getCode());
        }

        // 判断设备在线状态
        GetDeviceStatusInfoRespVo deviceStatusInfo = deviceCoreServiceApi.getDeviceStatusByDeviceId(deviceResp.getTenantId(), deviceId);
        if (!DeviceOnlineStatusEnum.CONNECTED.getCode().equals(deviceStatusInfo.getOnlineStatus())) {
            // 设备离线
            LOGGER.info("***** queryDeviceAttribute, device is disconnected, deviceId={}", deviceId);
            throw new SmartHomeException(SmartHomeErrorCode.DEVICE_OFFLINE.getCode());
        }

        // 设备最新的 属性值
        Map<String, Object> currentStateMap = deviceStateCoreApi.get(tenantId, deviceId);
        if (currentStateMap == null) {
            currentStateMap = Maps.newHashMap();
        }

        DeviceAttributeResp deviceAttributeResp = new DeviceAttributeResp();
        deviceAttributeResp.setDeviceId(deviceId);
        deviceAttributeResp.setAttrMap(currentStateMap);

        return deviceAttributeResp.buildMsg();
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
            LOGGER.info("findDeviceListByUserId, deviceRespList.size={}", deviceRespList.size());

            for (DeviceResp dr : deviceRespList) {
                DeviceInfo devInfo = new DeviceInfo();
                devInfo.setDeviceId(dr.getDeviceId());
                devInfo.setName(dr.getName());
                devInfo.setDeviceVersion(dr.getVersion());

                DeviceClassifyResp deviceClassifyResp = deviceClassifyApi.getDeviceClassifyByProductId(dr.getProductId());
                if (deviceClassifyResp == null) {
                    LOGGER.info("findDeviceListByUserId, deviceId={} DeviceClassifyProductXref is null, will not be sync", dr.getDeviceId());
                    continue;
                }
                devInfo.setType(deviceClassifyResp.getTypeCode());

                List<ServiceModulePropertyResp> serviceModulePropertyList = commonService.findPropertyListByProductId(dr.getProductId());
                if (CollectionUtils.isNotEmpty(serviceModulePropertyList)) {
                    LOGGER.info("findDeviceListByUserId, deviceId={}, serviceModulePropertyList.size={}", serviceModulePropertyList.size());
                    for (ServiceModulePropertyResp propertyResp : serviceModulePropertyList) {
                        Map<String, Object> attrMap = Maps.newHashMap();
                        attrMap.put("name", propertyResp.getCode());
                        attrMap.put("unit", propertyResp.getDescription());
                        // 数据类型
                        String dataType = AttributeUtil.parseParamType(propertyResp.getParamType());
                        attrMap.put("type", dataType);
                        attrMap.put("rw", AttributeUtil.parseRwStatus(propertyResp.getRwStatus()));
                        Object minObj = AttributeUtil.parseMinMax(dataType, propertyResp.getMinValue());
                        if (minObj != null) {
                            attrMap.put("min", minObj);
                        }
                        Object maxObj = AttributeUtil.parseMinMax(dataType, propertyResp.getMaxValue());
                        if (maxObj != null) {
                            attrMap.put("max", maxObj);
                        }
                        devInfo.addAttrMap(attrMap);
                    }
                } else {
                    LOGGER.info("findDeviceListByUserId, deviceId={}, serviceModulePropertyList is empty");
                }

                deviceInfoList.add(devInfo);
            }
        } else {
            LOGGER.info("findDeviceListByUserId, deviceRespList is empty");
        }

        return deviceInfoList;
    }
}
