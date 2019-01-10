package com.iot.shcs.device.service.impl;

import com.google.common.collect.Lists;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.space.api.SpaceApi;
import com.iot.device.api.OTAServiceApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.req.device.AddCommDeviceStateInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.common.util.DeviceUtils;
import com.iot.shcs.common.util.RedisKeyUtil;
import com.iot.shcs.device.enums.DeviceCoreExceptionEnum;
import com.iot.shcs.device.enums.OnlineStatusEnum;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.space.service.ISpaceService;
import com.iot.shcs.space.service.SpaceCoreService;
import com.iot.shcs.space.vo.SpaceDeviceVo;
import com.iot.user.api.UserApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xfz @Descrpiton: @Date: 10:54 2018/5/10 @Modify by:
 */
@Slf4j
@Service
public class DeviceService {

    @Autowired
    private DeviceCoreService deviceCoreService;

    @Autowired
    private ProductCoreApi productCoreApi;

    @Autowired
    private ISpaceService spaceService;
    @Autowired
    private SpaceCoreService spaceCoreService;
    @Autowired
    private UserApi userApi;
    @Autowired
    private OTAServiceApi otaServiceApi;
    @Autowired
    private SpaceApi spaceApi;

    /**
     * 根据deviceUuid获取直连设备
     *
     * @param deviceUuid
     * @return DeviceResp
     * @author yuChangXing
     * @created 2018/07/26 16:49
     */
    public GetDeviceInfoRespVo getDirectDeviceByDeviceUuid(String deviceUuid) {
        GetDeviceInfoRespVo deviceResp = null;

        if (StringUtil.isNotBlank(deviceUuid)) {
            deviceResp = deviceCoreService.getDeviceInfoByDeviceId(deviceUuid);
            if (deviceResp.getIsDirectDevice() == null || deviceResp.getIsDirectDevice().intValue() == Constants.IS_NOT_DIRECT_DEVICE) {
                // 非直连设备
                if (deviceResp.getParentId() != null) {
                    deviceResp = deviceCoreService.getDeviceInfoByDeviceId(deviceResp.getParentId());
                }
            }
        }

        return deviceResp;
    }

    /**
     * 根据deviceUuid 获取直连设备的area
     *
     * @param deviceId
     * @return area
     * @author yuChangXing
     * @created 2018/07/26 16:49
     */
    public String getAreaByDeviceUuid(String deviceId) {
        String area = null;
        GetDeviceInfoRespVo deviceResp = this.getDirectDeviceByDeviceUuid(deviceId);
        if (deviceResp != null) {
            GetDeviceExtendInfoRespVo deviceExtendResp = deviceCoreService.getDeviceExtendByDeviceId(deviceResp.getTenantId(), deviceResp.getUuid());
            if (deviceExtendResp != null) {
                area = deviceExtendResp.getArea();
            }
        }

        log.info("getAreaByDeviceUuid(), deviceId={} 获取到的 area={}", deviceId, area);
        return area;
    }

    /**
     * 根据userId 获取其中的一个 直连设备的area
     *
     * @param userId
     * @return area
     * @author yuChangXing
     * @created 2018/08/01 21:11
     */
    public String getAreaByUserId(Long userId) {
        String area = null;
        Long tenantId = SaaSContextHolder.currentTenantId();//todo
        List<DeviceResp> deviceRespList = deviceCoreService.findDirectDeviceListByUserId(tenantId, userId);
        if (CollectionUtils.isNotEmpty(deviceRespList)) {
            for (DeviceResp vo : deviceRespList) {
                GetDeviceExtendInfoRespVo deviceExtendResp = deviceCoreService.getDeviceExtendByDeviceId(tenantId, vo.getDeviceId());
                if (deviceExtendResp != null) {
                    area = deviceExtendResp.getArea();
                }
                if (!StringUtils.isEmpty(area)) {
                    break;
                }
            }

        } else {
            log.info("getAreaByUserId(), userId={} 没有直连设备", userId);
        }
        log.info("getAreaByUserId(), userId={} 获取到的 area={}", userId, area);
        return area;
    }

    public boolean isKitProduct(Long productId) {
        boolean isKitProduct = false;

        if (productId == null) {
            return isKitProduct;
        }

        GetProductInfoRespVo productResp = productCoreApi.getByProductId(productId);
        if (productResp != null && productResp.getIsKit() != null && productResp.getIsKit() == 1) {
            isKitProduct = true;
        }

        return isKitProduct;
    }

    public void buildUpdateBaseDeviceInfo(Long tenantId,
                                          String deviceId,
                                          Map<String, Object> updateDevBasicsPayload,
                                          List<Map<String, Object>> returnOta,
                                          String resetRandom,
                                          Long productId) {
        String mac = (String) updateDevBasicsPayload.get("mac");
        String version = (String) updateDevBasicsPayload.get("version"); // 软件版本
        // add hwVersion http 1.1.21doc
        String hwVersion = (String) updateDevBasicsPayload.get("hwVersion");
        String devModel = (String) updateDevBasicsPayload.get("devModel");
        String supplier = (String) updateDevBasicsPayload.get("supplier");

        UpdateDeviceInfoReq updateDeviceInfoReq = UpdateDeviceInfoReq.builder()
                .tenantId(tenantId)
                .uuid(deviceId)
                .mac(mac)
                .version(version)
                .resetRandom(resetRandom)
                .productId(productId)
                .hwVersion(hwVersion)
                .devModel(devModel)
                .supplier(supplier)
                .isDirectDevice(1)
                .build();
        if (CollectionUtils.isNotEmpty(returnOta)) {
            // 更新软件版本
            version = (String) returnOta.get(0).get("version");
            updateDeviceInfoReq.setVersion(version);
        }
        deviceCoreService.saveOrUpdateDeviceInfo(updateDeviceInfoReq);


        deviceCoreService.saveOrUpdateDeviceStatus(UpdateDeviceStatusReq.builder()
                .deviceId(deviceId)
                .tenantId(tenantId)
                .onlineStatus(OnlineStatusEnum.CONNECTED.getCode())
                .build());
    }

    /**
     * 构造设备信息
     *
     * @param deviceId
     * @return
     */
    public Map<String, Object> buildDeviceInfoByDeviceId(Long tenantId, String deviceId) {
        Map<String, Object> respPayload = new HashMap<>();
        List<String> deviceIds = new ArrayList<>();
        deviceIds.add(deviceId);
        List<DeviceResp> deviceList = deviceCoreService.findDeviceListByDeviceIds(tenantId, deviceIds);
        if (CollectionUtils.isEmpty(deviceList)) {
            return respPayload;
        }
        DeviceResp device = deviceList.get(0);
        // 一个设备只能挂载一个空间下（业务需求定）
        SpaceDeviceVo space = spaceCoreService.getSpaceByDeviceId(deviceId, tenantId);

        //        FetchUserResp user = userApi.getUser(device.getUserId());
        respPayload.put("devId", deviceId);
        //        respPayload.put("userId", user.getUuid());
        respPayload.put("name", device.getName());
        respPayload.put("icon", device.getIcon());
        respPayload.put("homeId", space.getParentId());
        respPayload.put("roomId", space.getId());
        respPayload.put("timestamp", System.currentTimeMillis());
        String devModel = "";
        String hwVersion = "";
        String mac = "";
        String supplier = "";
        String version = "";
        devModel = device.getDevModel();
        if (StringUtils.isEmpty(devModel)) {
            GetProductInfoRespVo productResp = productCoreApi.getByProductId(device.getProductId());
            if (productResp != null) {
                devModel = productResp.getModel();
            }
        }
        hwVersion = device.getHwVersion();
        //将mac地址转换成大写
        mac = device.getMac().toUpperCase();
        if (!mac.contains(":")) {
            String reg = "(.{2})";
            mac = mac.replaceAll(reg, "$1:");
            int last = mac.lastIndexOf(":");
            mac = mac.substring(0, last);
        }
        supplier = device.getSupplier();

        version = otaServiceApi.getOtaDeviceVersion(deviceId);
        respPayload.put("devModel", devModel);
        respPayload.put("hwVersion", hwVersion);
        respPayload.put("mac", mac);
        respPayload.put("supplier", supplier);
        respPayload.put("version", version);

        return respPayload;
    }

    public void setDevInfo(Map<String, Object> payload, Long userId, Long tenantId) {
        String deviceUuid = String.valueOf(payload.get("devId"));
        List<ListUserDeviceInfoRespVo> devices = deviceCoreService.listUserDevices(tenantId, userId);

        if (CollectionUtils.isNotEmpty(devices)) {
            List<String> deviceIds = Lists.newArrayList();
            devices.forEach(userDevice -> {
                deviceIds.add(userDevice.getDeviceId());
            });
            List<ListDeviceInfoRespVo> deviceInfoList = deviceCoreService.listDevicesByDeviceIds(deviceIds);
            // 检查设备名字是否重复
            String deviceName = String.valueOf(payload.get("name"));
            for (ListDeviceInfoRespVo deviceInfo : deviceInfoList) {
                // 将要被修改的 deviceUuid
                if (deviceInfo.getUuid().equals(deviceUuid)) {
                    continue;
                }
                // 只要判断 device.id != deviceId的记录
                if ((!StringUtils.isEmpty(deviceInfo.getName())) && deviceInfo.getName().equals(deviceName)) {
                    throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_NAME_EXIST);
                }
            }
        }
        String homeId = String.valueOf(payload.get("homeId"));
        String roomId = String.valueOf(payload.get("roomId"));
        String icon = String.valueOf(payload.get("icon"));
        String name = String.valueOf(payload.get("name"));

        // 更新设备信息
        UpdateDeviceInfoReq updateDeviceInfoReq = UpdateDeviceInfoReq.builder()
                .tenantId(tenantId)
                .uuid(deviceUuid)
                .name(name)
                .icon(icon)
                .build();

        deviceCoreService.saveOrUpdateDeviceInfo(updateDeviceInfoReq);

//        deviceApi.getDeviceByDeviceUUID(deviceUuid);
        // 修改房间设备信息
        spaceCoreService.updateSpaceByUserId(userId, tenantId, deviceUuid, roomId, homeId);
    }

    /**
     * 插入记录
     *
     * @param deviceInfo
     * @param attrMap
     * @return
     */
    public String insertDeviceStatus(
            GetDeviceInfoRespVo deviceInfo, Map<String, Object> attrMap) {
        if (MapUtils.isEmpty(attrMap)) {
            return StringUtil.EMPTY;
        }

        Long tenantId = deviceInfo.getTenantId();
        String deviceId = deviceInfo.getUuid();
        String deviceName = deviceInfo.getName();

        StringBuilder sb = new StringBuilder();
        List<UpdateDeviceStateReq> deviceStateList = Lists.newArrayList();
        List<AddCommDeviceStateInfoReq> deviceStatusInfoList = Lists.newArrayList();

        for (Map.Entry<String, Object> entry : attrMap.entrySet()) {
            AddCommDeviceStateInfoReq deviceStatusInfo = new AddCommDeviceStateInfoReq();
            deviceStatusInfo.setPropertyName(entry.getKey());
            deviceStatusInfo.setPropertyValue(entry.getValue().toString());
            deviceStatusInfo.setPropertyDesc(null);
            deviceStatusInfoList.add(deviceStatusInfo);

            if (StringUtil.isNotBlank(sb.toString())) {
                sb = sb.append("\n");
            }
            String value =
                    DeviceUtils.changeValue(deviceName, entry.getKey(), entry.getValue().toString());
            sb = sb.append(value);
        }

        UpdateDeviceStateReq deviceStateReq = UpdateDeviceStateReq.builder()
                .tenantId(tenantId)
                .deviceId(deviceId)
                .stateList(deviceStatusInfoList)
                .build();
        deviceStateList.add(deviceStateReq);

        deviceCoreService.saveOrUpdateDeviceStates(deviceStateList);

        return sb.toString();
    }

    /**
     * 获取设备名称
     *
     * @return
     */
    public String buildDeviceName(Long tenantId, GetProductInfoRespVo productResp, Long userId) {
        String deviceName = "default";

        if (StringUtil.isNotBlank(productResp.getProductName())) {
            deviceName = productResp.getProductName();
        }

        if (userId != null) {
            Integer seq = spaceService.getDeviceSeqByUserId(tenantId, userId);
            deviceName = deviceName + "_" + seq;
        }

        return deviceName;
    }

    public boolean isXXSensor(String devModel, String xx) {
        if (StringUtil.isNotBlank(devModel)) {
            return devModel.toLowerCase().contains(xx);
        }
        return false;
    }

    /**
     * 把 通知给app的devBindNotif消息缓存到redis
     *
     * @param mqttMsg
     * @param userUuid
     * @param deviceId
     * @param tenantId
     */
    public void addDevBindNotifMqttMsgToCache(MqttMsg mqttMsg, String userUuid, String deviceId, Long tenantId) {
        if (mqttMsg == null) {
            log.info("addMqttMsgToCache() error, beacuse mqttMsg is null.");
            return;
        }

        RedisCacheUtil.valueObjSet(RedisKeyUtil.getDevBindNotifIdKey(userUuid, deviceId, tenantId), mqttMsg, 120L);
    }

    /**
     * 获取用户和设备的绑定消息通知数据
     *
     * @param userUuid
     * @param deviceId
     * @param tenantId
     * @return
     */
    public MqttMsg getDevBindNotifMqttMsgFromCache(String userUuid, String deviceId, Long tenantId) {
        MqttMsg mqttMsg = null;
        if (StringUtil.isBlank(userUuid) || StringUtil.isBlank(deviceId) || tenantId == null) {
            mqttMsg = new MqttMsg();
            mqttMsg.setAck(MqttMsgAck.failureAck(-1, "parameter is error."));
        } else {
            String key = RedisKeyUtil.getDevBindNotifIdKey(userUuid, deviceId, tenantId);
            mqttMsg = RedisCacheUtil.valueObjGet(key, MqttMsg.class);
            if (mqttMsg == null) {
                mqttMsg = new MqttMsg();
                mqttMsg.setAck(MqttMsgAck.failureAck(-1, "user unbind the device or cache data is expire."));
            } else {
                if (mqttMsg.getAck() != null && mqttMsg.getAck().getCode() == 200) {
                    RedisCacheUtil.delete(key);
                }
            }
        }

        return mqttMsg;
    }

    /**
     * 删除 缓存的 devBindNotif消息
     *
     * @param userUuid
     * @param deviceId
     * @param tenantId
     */
    public void deleteDevBindNotifMqttMsgCache(String userUuid, String deviceId, Long tenantId) {
        RedisCacheUtil.delete(RedisKeyUtil.getDevBindNotifIdKey(userUuid, deviceId, tenantId));
    }
}
