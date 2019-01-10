package com.iot.building.callback.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.building.callback.ListenerCallback;
import com.iot.building.device.service.IDeviceRemoteService;
import com.iot.building.device.vo.DeviceRemoteControlResp;
import com.iot.building.gateway.MultiProtocolGatewayHepler;
import com.iot.building.group.service.IGroupService;
import com.iot.building.group.vo.GroupResp;
import com.iot.building.helper.Constants;
import com.iot.building.scene.service.SceneService;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.common.enums.APIType;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;

public class NewDeviceRemoteControlCallBack implements ListenerCallback {

    private static final Logger log = LoggerFactory.getLogger(NewDeviceRemoteControlCallBack.class);

    private IDeviceRemoteService deviceRemoteService = ApplicationContextHelper.getBean(IDeviceRemoteService.class);

    private IBuildingSpaceService spaceService = (IBuildingSpaceService) ApplicationContextHelper.getBean(IBuildingSpaceService.class);

    private SceneService sceneService = (SceneService) ApplicationContextHelper.getBean(SceneService.class);

    private IGroupService iGroupService = (IGroupService) ApplicationContextHelper.getBean(IGroupService.class);

    @Override
    public void callback(GetDeviceInfoRespVo device, Map<String, Object> map, APIType apiType) {
        long start = System.currentTimeMillis();
        log.info("............进入遥控器回调。。。。。。。。。。。。。");
        try {
            // 根据传进来的DeviceTypeId 判断是否是遥控器 不是就跳过 是就响应事件
            if (filter(device, map)) {
                String event = map.get("event") == null ? "" : map.get("event").toString();
                log.info(" event :"+event);
                if (StringUtils.isNotBlank(event)) {
                    excuteKeyFunction(device, event);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        log.info("++++++++++遥控器回调处理耗时（ms）=" + (end - start));
        log.info("............遥控器回调end end。。。。。。。。。。。。。");
    }

    /**
     * 过滤条件
     *
     * @param device
     * @param map
     */
    private Boolean filter(GetDeviceInfoRespVo device, Map<String, Object> map) {
        List<DeviceRemoteControlResp> deviceRemoteControls = deviceRemoteService.listDeviceRemoteControlByBusinessTypeId(device.getTenantId(), device.getBusinessTypeId());
        if (deviceRemoteControls != null && deviceRemoteControls.size() > 0) {
            return true;
        } else {
            log.info("............不存在此遥控器");
            return false;
        }
    }

    /**
     * 获取遥控器按键对应的功能点
     *
     * @param businessTypeId
     * @param event
     * @return
     * @throws Exception
     */
    private DeviceRemoteControlResp getKeyTypeValue(GetDeviceInfoRespVo device, String event) throws Exception {
        //获取遥控器的全部的键
        List<DeviceRemoteControlResp> remoteControlList = deviceRemoteService.listDeviceRemoteControlByBusinessTypeId(device.getTenantId(), device.getBusinessTypeId());
        //根据本次事件获取对应的键的业务
        return getRemoteControlByKey(event, remoteControlList);
    }

    /**
     * 获取遥控器的功能键
     *
     * @param event
     * @param remoteControlList
     * @return
     */
    private DeviceRemoteControlResp getRemoteControlByKey(String event, List<DeviceRemoteControlResp> remoteControlList) {
        //这边的代码是为了兼容旧版本 之前旧版本网关不是传 1,2,3,4 是传onoff dimdec 的字符串
        //与杰斌定死 目前是固定是开关 1, 变强 2, 变弱 3, 场景切换 4  后续根据产品的需求修改
    	DeviceRemoteControlResp deviceRemoteControl = null;
        if (CollectionUtils.isNotEmpty(remoteControlList)) {
            for (DeviceRemoteControlResp remoteControl : remoteControlList) {
                //开关 固定 是 1
                if ("onoff".equals(event) && remoteControl.getKeyCode().equals("1")) {
                    deviceRemoteControl = remoteControl;
                    break;
                }
                //变强 固定 是 2
                if ("dimdec".equals(event) && remoteControl.getKeyCode().equals("2")) {
                    deviceRemoteControl = remoteControl;
                    break;
                }
                //变弱 固定 是 3
                if ("diminc".equals(event) && remoteControl.getKeyCode().equals("3")) {
                    deviceRemoteControl = remoteControl;
                    break;
                }
                //场景循环 固定 是 4
                if (event.startsWith("scene_") && remoteControl.getKeyCode().equals("4")) {
                    deviceRemoteControl = remoteControl;
                    break;
                }
            }
        }
        return deviceRemoteControl;
    }

    /**
     * 执行遥控器按键的功能
     *
     * @param device
     * @param keyCode
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void excuteKeyFunction(GetDeviceInfoRespVo device, String keyCode) throws Exception {
        // 根据设备ID查询该遥控器的归属房间
        SpaceDeviceResp spaceDevice = spaceService.findSpaceIdByDeviceId(device.getUuid(),device.getOrgId(),device.getTenantId());
        log.info("----- 根据设备ID查询该遥控器的归属房间 :"+JSON.toJSONString(spaceDevice));
        if (spaceDevice != null) {
            //获取遥控器按键对应的功能点
            DeviceRemoteControlResp deviceRemoteControl = getKeyTypeValue(device, keyCode);
            log.info("----- 获取遥控器按键对应的功能点 :"+JSON.toJSONString(deviceRemoteControl));
            if (deviceRemoteControl != null) {
                Map<String, Object> propertyMap = new HashMap<>();
                if (StringUtils.isNotBlank(deviceRemoteControl.getDefaultValue())) {
                    propertyMap = JSON.parseObject(deviceRemoteControl.getDefaultValue(), Map.class);
                }
                dispatchExcute(spaceDevice.getSpaceId(), deviceRemoteControl, device, propertyMap);
            }
        }
    }

    /**
     * 根据控制器定义的类型走不同业务
     *
     * @param spaceId
     * @param deviceRemoteControl
     * @param propertyMap
     */
    private void dispatchExcute(Long spaceId, DeviceRemoteControlResp deviceRemoteControl, GetDeviceInfoRespVo deviceResp,
                                Map<String, Object> propertyMap) {
        List<GroupResp> groupRespList = Lists.newArrayList();
        log.info("遥控器发送的业务(deviceRemoteControl-type):" + deviceRemoteControl.getType());
        log.info("遥控器发送的业务(deviceRemoteControl-function):" + deviceRemoteControl.getFunction());
        if ((!org.springframework.util.StringUtils.isEmpty(deviceRemoteControl.getType())) && (!org.springframework.util.StringUtils.isEmpty(deviceRemoteControl.getFunction()))) {
            //type 是 GROUP 的情况
            if ("GROUP".equals(deviceRemoteControl.getFunction())) {
                switch (deviceRemoteControl.getType()) {
                    case "ONOFF":
                        propertyMap = getOnOff(deviceResp, propertyMap);
                        groupRespList = iGroupService.getGroupListByRemoteId(deviceResp.getUuid());
                        groupControl(deviceResp, propertyMap, groupRespList);
                        break;
                    case "DIMMING_ADD":
                        propertyMap = addDimming(spaceId, propertyMap);
                        groupRespList = iGroupService.getGroupListByRemoteId(deviceResp.getUuid());
                        groupControl(deviceResp, propertyMap, groupRespList);
                        break;
                    case "DIMMING_SUB":
                        propertyMap = subDimming(spaceId, propertyMap);
                        groupRespList = iGroupService.getGroupListByRemoteId(deviceResp.getUuid());
                        groupControl(deviceResp, propertyMap, groupRespList);
                        break;
                    default:
                        sceneService.excuteSceneBySpaceAndSceneName(deviceResp.getTenantId(),spaceId, deviceRemoteControl.getType());
                        break;
                }
            }
            //type 是 SCENE 的情况
            if ("SCENE".equals(deviceRemoteControl.getType())) {
                switch (deviceRemoteControl.getType()) {
                    case "SCENE_SITHCH":
                        sceneService.sceneExecuteNext(deviceResp.getTenantId(),spaceId);
                        break;
                    case "ON":
                    case "OFF":
                    case "ONOFF":
                        //如果房间遥控器灯缓存不存在 默认是0 也就是开的状态
                        int onOff = Constants.REMOTE_ONOFF_STATUS.get(deviceResp.getUuid()) == null
                                ? 0 : Constants.REMOTE_ONOFF_STATUS.get(deviceResp.getUuid());

                        String name = (onOff == 1 ? "CLOSE" : "OPEN");
                        //如果之前是open 改成 CLOSE 之前是CLOSE改成OPEN
                        int flag = (onOff == 1 ? 0 : 1);
                        sceneService.excuteSceneBySpaceAndSceneName(deviceResp.getTenantId(),spaceId, name);
                        //执行成功之后更新该遥控器的缓存状态
                        Constants.REMOTE_ONOFF_STATUS.put(deviceResp.getUuid(), flag);
                        break;
                    default:
                        sceneService.excuteSceneBySpaceAndSceneName(deviceResp.getTenantId(),spaceId, deviceRemoteControl.getType());
                        break;
                }
            }
        }
    }

    private Map<String, Object> getOnOff(GetDeviceInfoRespVo deviceResp, Map<String, Object> propertyMap) {
        int OnOff = Constants.REMOTE_ONOFF_STATUS.get(deviceResp.getUuid()) == null
                ? Integer.parseInt(propertyMap.get("OnOff").toString())
                : Constants.REMOTE_ONOFF_STATUS.get(deviceResp.getUuid());
        if (OnOff == 1) {
            OnOff = 0;
        } else {
            OnOff = 1;
        }
        Constants.REMOTE_ONOFF_STATUS.put(deviceResp.getUuid(), OnOff);
        propertyMap.put("OnOff", OnOff);
        return propertyMap;
    }

    private void groupControl(GetDeviceInfoRespVo deviceResp, Map<String, Object> propertyMap, List<GroupResp> groupRespList) {
        for (GroupResp groupResp : groupRespList) {
            try {
                MultiProtocolGatewayHepler.groupControl(deviceResp.getParentId(), groupResp.getGroupId().toString(),
                        propertyMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 累加dimming
     *
     * @param spaceId
     * @param porperty
     * @return
     */
    private Map<String, Object> addDimming(Long spaceId, Map<String, Object> porperty) {
        int dimming = Constants.SPACE_DIMMING_STATUS.get(spaceId) == null
                ? Integer.parseInt(porperty.get("Dimming").toString())
                : Constants.SPACE_DIMMING_STATUS.get(spaceId);
        dimming += 30;
        if (dimming > 100) {
            dimming = 100;
        }
        Constants.SPACE_DIMMING_STATUS.put(spaceId, dimming);
        porperty.put("Dimming", dimming);
        return porperty;
    }

    /**
     * 递减dimming
     *
     * @param spaceId
     * @param porperty
     * @return
     */
    private Map<String, Object> subDimming(Long spaceId, Map<String, Object> porperty) {
        int dimming = Constants.SPACE_DIMMING_STATUS.get(spaceId) == null
                ? Integer.parseInt(porperty.get("Dimming").toString())
                : Constants.SPACE_DIMMING_STATUS.get(spaceId);
        dimming -= 30;
        if (dimming < 10) {
            dimming = 10;
        }
        Constants.SPACE_DIMMING_STATUS.put(spaceId, dimming);
        porperty.put("Dimming", dimming);
        return porperty;
    }

    public static void main(String[] args) {
        Integer m = null;
        int onOff = m == null ? 0 : m;
        String name = onOff == 1 ? "CLOSE" : "OPEN";
        int flag = onOff == 1 ? 0 : 1;
        System.out.println(onOff + "=====" + name + "====" + flag);
    }
}
