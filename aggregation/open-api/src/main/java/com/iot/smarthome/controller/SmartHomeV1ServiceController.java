package com.iot.smarthome.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.util.StringUtil;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.smarthome.constant.SmartHomeConstant;
import com.iot.smarthome.constant.SmartHomeErrorCode;
import com.iot.smarthome.exception.SmartHomeException;
import com.iot.smarthome.interceptor.SmartHomeTokenInterceptor;
import com.iot.smarthome.service.DeviceService;
import com.iot.smarthome.transfor.converter.KeyValConverter;
import com.iot.smarthome.transfor.kv.YunKeyValue;
import com.iot.smarthome.util.ResponseUtil;
import com.iot.smarthome.vo.ActivityVo;
import com.iot.smarthome.vo.DeviceAttributeHistoricalResp;
import com.iot.smarthome.vo.DeviceDiscoverResp;
import com.iot.smarthome.vo.DeviceExecuteResp;
import com.iot.smarthome.vo.DeviceInfo;
import com.iot.user.api.SmartTokenApi;
import com.iot.user.api.UserApi;
import com.iot.user.vo.SmartTokenResp;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/14 9:44
 * @Modify by:
 */

@RestController
@RequestMapping("/smarthome/v1")
@Api("smartHome对外接口,v1版本")
public class SmartHomeV1ServiceController {
    private Logger LOGGER = LoggerFactory.getLogger(SmartHomeV1ServiceController.class);

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Autowired
    private UserApi userApi;
    @Autowired
    private SmartTokenApi smartTokenApi;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceCoreServiceApi deviceCoreServiceApi;

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping(value = "/service", method = RequestMethod.POST)
    public JSONObject service(@RequestBody JSONObject req) {
        LOGGER.info("***** SmartHomeV1ServiceController, service, req={}", req.toJSONString());

        Map<String, Object> resultMap = null;
        JSONObject payload = req.getJSONObject("payload");
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        String localAccessToken = SmartHomeTokenInterceptor.getLocalAccessToken();

        String command = payload.getString("command");
        try {
            if (StringUtil.isBlank(command)) {
                LOGGER.error("service error, command is null, userId={}, tenantId={}", userId, tenantId);
                throw new SmartHomeException(SmartHomeErrorCode.PARAMETER_ERROR.getCode());
            }

            switch (command) {
                case "action.user.disconnect":
                    // 用户disconnect
                    resultMap = this.userDisconnect(userId, localAccessToken);
                    break;
                case "action.device.discover":
                    // 同步设备
                    resultMap = this.discoverDevices(userId, tenantId);
                    break;
                case "action.device.attribute.query":
                    // 获取设备最新属性-值
                    String deviceId = payload.getString("deviceId");
                    resultMap = deviceService.queryDeviceAttribute(userId, tenantId, deviceId);
                    break;
                case "action.device.execute":
                    // 执行设备
                    resultMap = this.execute(userId, tenantId, payload);
                    break;
                case "action.device.attribute.historical":
                    // 获取设备历史活动日志
                    resultMap = this.queryDeviceHistoricalData(userId, tenantId, payload);
                    break;
                default:
                    // 不支持协议
                    resultMap = ResponseUtil.buildErrorPayload(SmartHomeErrorCode.NOT_SUPPORTED.getCode());
                    break;
            }
        } catch (SmartHomeException e) {
            resultMap = ResponseUtil.buildErrorPayload(e.getErrorCode());

            e.printStackTrace();
        } catch (Exception e) {
            // 未知错误
            LOGGER.error("service error, userId={}, tenantId={}", userId, tenantId);
            resultMap = ResponseUtil.buildErrorPayload(SmartHomeErrorCode.UN_KNOWN_ERROR.getCode());

            e.printStackTrace();
        }

        JSONObject resultJsonObj = new JSONObject(resultMap);
        LOGGER.info("***** SmartHomeV1ServiceController, service, response data ={}", resultJsonObj);
        return resultJsonObj;
    }

    /**
     * 获取 设备的历史日志
     *
     * @param userId
     * @param tenantId
     * @param payload
     * @return
     */
    public Map<String, Object> queryDeviceHistoricalData(Long userId, Long tenantId, JSONObject payload) {
        String deviceId = payload.getString("deviceId");
        String startDateStr = payload.getString("startDate");
        String endDateStr = payload.getString("endDate");

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StringUtil.isBlank(deviceId) || startDate == null || endDate == null) {
            // 参数有误
            return ResponseUtil.buildErrorPayload(SmartHomeErrorCode.PARAMETER_ERROR.getCode());
        }

        DeviceAttributeHistoricalResp historicalResp = new DeviceAttributeHistoricalResp();
        historicalResp.setDeviceId(deviceId);

        GetDeviceInfoRespVo deviceResp = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
        deviceService.checkDeviceClassifyProductXref(deviceResp);

        // 判断用户是否关联设备
        ListUserDeviceInfoRespVo userDevice = deviceCoreServiceApi.getUserDevice(tenantId, userId, deviceId);
        if (userDevice != null) {
            List<ActivityVo> activityList = deviceService.queryDeviceHistoricalData(userId, tenantId, deviceId, startDate, endDate);
            historicalResp.setActivityList(activityList);
        }

        return historicalResp.buildMsg();
    }

    public Map<String, Object> execute(Long userId, Long tenantId, JSONObject payload) {
        JSONArray devices = payload.getJSONArray("devices");
        int deviceCount = devices.size();

        //CountDownLatch latch = new CountDownLatch(deviceCount);
        long t1 = System.currentTimeMillis();

        DeviceExecuteResp executeResp = new DeviceExecuteResp();
        for (int i = 0; i < deviceCount; i++) {
            /*final int index = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {*/
                    Map<String, Object> deviceInfoMap = Maps.newHashMap();
                    JSONObject deviceObj = (JSONObject) devices.get(i);
                    String deviceId = deviceObj.getString("deviceId");
                    try {
                        JSONObject attr = deviceObj.getJSONObject("attr");
                        YunKeyValue yunKeyValue = KeyValConverter.parseKeyVal(attr);
                        LOGGER.info("execute, index={}, deviceId={}, yunKeyValue={}", i, deviceId, JSON.toJSONString(yunKeyValue));

                        List<YunKeyValue> kvList = Lists.newArrayList();
                        kvList.add(yunKeyValue);

                        deviceInfoMap.put("deviceId", deviceId);

                        boolean exeSuccess = deviceService.setDevAttr(userId, tenantId, deviceId, kvList);
                        LOGGER.info("execute, exeSuccess={}, deviceId={}, userId={}", exeSuccess, deviceId, userId);
                        if (exeSuccess) {
                            deviceInfoMap.put("status", "SUCCESS");

                            Map<String, Object> states = Maps.newHashMap();
                            states.put(yunKeyValue.getKey(), yunKeyValue.getValue());
                            deviceInfoMap.put("states", states);
                        }
                    } catch (SmartHomeException e) {
                        e.printStackTrace();

                        deviceInfoMap.put("status", "ERROR");
                        deviceInfoMap.put("errorCode", e.getErrorCode());
                    } catch (Exception e) {
                        e.printStackTrace();

                        deviceInfoMap.put("status", "ERROR");
                        deviceInfoMap.put("errorCode", SmartHomeErrorCode.UN_KNOWN_ERROR.getCode());
                    }

                    executeResp.addDevice(deviceInfoMap);
                //}
            //});
        }

        /*try {
            latch.wait();
            long t2 = System.currentTimeMillis();
            double seconds = ((t2-t1)*1.0)/1000;
            LOGGER.info("execute, seconds = {}", seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        long t2 = System.currentTimeMillis();
        double seconds = ((t2-t1)*1.0)/1000;
        LOGGER.info("execute, seconds = {}", seconds);

        return executeResp.buildMsg();
    }

    /**
     * 同步设备信息
     *
     * @param userId
     * @param tenantId
     * @return
     */
    public Map<String, Object> discoverDevices(Long userId, Long tenantId) {
        DeviceDiscoverResp deviceDiscoverResp = new DeviceDiscoverResp();

        List<DeviceInfo> deviceInfoList = deviceService.findDeviceListByUserId(tenantId, userId);
        if (deviceInfoList != null) {
            deviceDiscoverResp.addDeviceList(deviceInfoList);
        }

        Map<String, Object> resultMap = deviceDiscoverResp.buildMsg();
        return resultMap;
    }

    /**
     * 用户disconnect
     *
     * @param userId
     * @param localAccessToken
     * @return
     */
    private Map<String, Object> userDisconnect(Long userId, String localAccessToken) {
        SmartTokenResp smartTokenResp = smartTokenApi.getByLocalAccessToken(localAccessToken);
        if (smartTokenResp != null) {
            smartTokenApi.deleteByUserIdAndLocalAccessToken(userId, localAccessToken);
            userApi.deleteOauthToken(SmartHomeConstant.CLIENT_TYPE, localAccessToken, smartTokenResp.getLocalRefreshToken());
        }
        Map<String, Object> resultMap = ResponseUtil.buildSuccessPayload();
        return resultMap;
    }
}
