package com.iot.robot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.robot.common.constant.DeviceOnlineStatusEnum;
import com.iot.robot.common.constant.VoiceBoxConst;
import com.iot.robot.interceptors.TokenInterceptor;
import com.iot.robot.norm.KeyValue;
import com.iot.robot.service.CommonService;
import com.iot.robot.service.DeviceService;
import com.iot.robot.service.GoogleHomeBusinessService;
import com.iot.robot.service.SceneService;
import com.iot.robot.transform.AbstractTransfor;
import com.iot.robot.transform.convertor.AbstractConvertor;
import com.iot.robot.vo.DeviceInfo;
import com.iot.robot.vo.SecurityCommand;
import com.iot.robot.vo.google.GoogleControlRes;
import com.iot.robot.vo.google.GoogleEndpoint;
import com.iot.robot.vo.google.GoogleHomeResponse;
import com.iot.robot.vo.google.GoogleQueryRes;
import com.iot.robot.vo.google.ReportStateVo;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.SmartTokenApi;
import com.iot.user.api.UserApi;
import com.iot.user.constant.SmartHomeConstants;
import com.iot.user.vo.SmartTokenResp;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */
@RestController
@RequestMapping("google")
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class GoogleRobotController {

    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private Logger log = LoggerFactory.getLogger(GoogleRobotController.class);
    @Autowired
    private UserApi userApi;
    @Autowired
    private SmartTokenApi smartTokenApi;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private GoogleHomeBusinessService googleHomeBusinessService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private List<AbstractConvertor> convertor;

    @Resource(name = "googleTransfor")
    private AbstractTransfor googleTransfor;

    private Map<String, Method> methodCache = new HashMap<>();

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public Object test(@RequestBody JSONObject req) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("time", new Date());
        return result;
    }

    //设备与情景的控制（smarthome技能）
    @ApiOperation("google-smarthomer入口")
    @RequestMapping(value = "services", method = RequestMethod.POST)
    public Object services(@RequestBody JSONObject req) {
        String requestId = req.getString("requestId");
        JSONArray inputs = req.getJSONArray("inputs");
        JSONObject intentJson = inputs.getJSONObject(0);
        String intent = intentJson.getString("intent");

        log.info("***** googleHome, services, requestId={}, req={}", requestId, req.toJSONString());

        String userUuid = TokenInterceptor.getUserUUID();
        Long userId = TokenInterceptor.getUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        if ("action.devices.SYNC".equals(intent)) {
            // 设备发现
            log.info("***** googleHome, SYNC start");
            GoogleHomeResponse res = this.sync();
            res.setRequestId(requestId);

            // 异步执行 report state
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 延时30秒再上报
                        Thread.sleep(1000 * 30);
                        if (res.returnDevices() != null) {
                            Map<String, String> deviceUuidMap = Maps.newHashMap();

                            for (GoogleEndpoint device : res.returnDevices()) {
                                if (device.getType().equals("action.devices.types.SCENE")) {
                                    continue;
                                }
                                String deviceUuid = device.getId();
                                deviceUuidMap.put(deviceUuid, deviceUuid);
                            }

                            handleReportState(tenantId, deviceUuidMap, userId, userUuid, requestId);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            log.info("***** googleHome, SYNC end, final return data:{}", JSONObject.toJSON(res));
            return res;

        } else if ("action.devices.QUERY".equals(intent)) {
            // 设备属性查询
            log.info("***** googleHome, QUERY start");
            JSONObject payload = intentJson.getJSONObject("payload");
            JSONArray devices = payload.getJSONArray("devices");

            GoogleQueryRes res = googleHomeBusinessService.deviceStatusQuery(devices, userId);
            res.setRequestId(requestId);

            // 异步执行 report state
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> deviceUuidMap = Maps.newHashMap();

                    for (Object device : devices) {
                        String deviceUuid = ((JSONObject) device).getString("id");
                        GetDeviceInfoRespVo deviceResp = deviceService.getDeviceInfoRespVo(deviceUuid);
                        if (deviceResp == null) {
                            continue;
                        }
                        deviceUuidMap.put(deviceUuid, deviceUuid);
                    }

                    handleReportState(tenantId, deviceUuidMap, userId, userUuid, requestId);
                }
            });

            log.info("***** googleHome, QUERY end, final return data:{}", JSONObject.toJSON(res));
            return res;

        } else if ("action.devices.EXECUTE".equals(intent)) {
            // 执行
            log.info("***** googleHome, EXECUTE start");

            JSONObject payload = intentJson.getJSONObject("payload");
            JSONObject commonds = (JSONObject) payload.getJSONArray("commands").get(0);
            GoogleControlRes res = this.execu(commonds);
            res.setRequestId(requestId);

            log.info("***** action.devices.EXECUTE --> 返回的数据:{}", JSONObject.toJSON(res));

            // 异步执行 report state
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> deviceUuidMap = Maps.newHashMap();

                    JSONArray devices = commonds.getJSONArray("devices");
                    for (Object device : devices) {
                        String deviceUuid = ((JSONObject) device).getString("id");
                        GetDeviceInfoRespVo deviceResp = deviceService.getDeviceInfoRespVo(deviceUuid);
                        if (deviceResp == null) {
                            continue;
                        }
                        deviceUuidMap.put(deviceUuid, deviceUuid);
                    }

                    handleReportState(tenantId, deviceUuidMap, userId, userUuid, requestId);
                }
            });

            log.info("***** googleHome, EXECUTE end, final return data:{}", JSONObject.toJSON(res));
            return res;

        } else if ("action.devices.DISCONNECT".equals(intent)) {
            log.info("***** googleHome, DISCONNECT start, userId={}", userId);

            smartTokenApi.deleteSmartTokenByUserIdAndSmart(userId, SmartHomeConstants.GOOGLE_HOME);

            log.info("***** googleHome, DISCONNECT end");
        }

        CommonResponse<Object> result = new CommonResponse<>(ResultMsg.SUCCESS);
        return result;
    }

    // 处理 上报设备状态
    private void handleReportState(Long tenantId, Map<String, String> deviceUuidMap, Long userId, String userUuid, String requestId) {
        log.info("***** handleReportState(), deviceUuidMap={}, userId={}, userUuid={}, requestId={}", deviceUuidMap, userId, userUuid, requestId);

        SmartTokenResp googleHomeToken = smartTokenApi.getSmartTokenByUserIdAndSmart(userId, SmartHomeConstants.GOOGLE_HOME);
        if (googleHomeToken != null) {
            List<ReportStateVo> reportStateVoList = Lists.newArrayList();
            deviceUuidMap.forEach((deviceUuid, val) -> {
                Map<String, Object> obj = commonService.getDeviceStatus(tenantId, deviceUuid);
                if (obj != null) {
                    GetDeviceStatusInfoRespVo statusInfo = deviceService.getDeviceStatusByDeviceId(tenantId, deviceUuid);
                    Boolean onlineStatus = null;
                    if (DeviceOnlineStatusEnum.CONNECTED.getCode().equals(statusInfo.getOnlineStatus())) {
                        onlineStatus = true;
                    } else if(DeviceOnlineStatusEnum.DISCONNECTED.getCode().equals(statusInfo.getOnlineStatus())) {
                        onlineStatus = false;
                    }

                    if (onlineStatus != null) {
                        obj.put("online", onlineStatus);
                    }

                    ReportStateVo reportStateVo = new ReportStateVo();
                    reportStateVo.setDeviceId(deviceUuid);
                    reportStateVo.setAttrMap(obj);
                    reportStateVoList.add(reportStateVo);
                }
            });

            log.info("***** reportStateVoList.size()={}", reportStateVoList.size());
            commonService.handlerGoogleHomeReportState(reportStateVoList, userUuid, requestId);
        }
    }


    //安防的控制（自定义技能）
    @ApiOperation("google-custom入口")
    @RequestMapping(value = "customAction", method = RequestMethod.POST)
    public Object customAction(@RequestBody JSONObject body) throws IOException {
        String responseId = body.getString("responseId");
        log.info("***** googleHome, customAction(), responseId={}, body={}", body.toJSONString());

        body = body.getJSONObject("queryResult");
        // 方法名
        String intent = body.getJSONObject("intent").getString("displayName");
        body = body.getJSONObject("parameters");
        try {
            Method m = null;
            if ((m = methodCache.get(intent)) == null) {
                m = this.getClass().getDeclaredMethod(intent, JSONObject.class);
                methodCache.put(intent, m);
            }

            if (m == null) {
                log.error("***** googleHome, customAction, method(intent={}) not found.", intent);
                body.put("fulfillmentText", VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE);
                return body;
            }

            String result = (String) m.invoke(this, body);
            log.info("***** googleHome, customAction, final return result={}", result);

            body.clear();
            body.put("fulfillmentText", result);
        } catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            body.put("fulfillmentText", VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE);
        }

        log.info("***** googleHome, customAction, final return body={}", body);
        return body;
    }

    private GoogleHomeResponse sync() {
        Long userId = TokenInterceptor.getUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        List<DeviceInfo> devices = deviceService.findDeviceListByUserId(tenantId, userId);
        List<SceneResp> scenes = sceneService.findSceneRespListByUserId(tenantId, userId);
        log.info("***** googleHome, sync, 设备数量:{}, 情景数量:{}", devices.size(), scenes.size());

        GoogleHomeResponse res = (GoogleHomeResponse) googleTransfor.getResponse(new List[]{devices, scenes});
        res.setAgentUserId(TokenInterceptor.getUserUUID());
        return res;
    }

    private GoogleControlRes execu(JSONObject commonds) {
        GoogleControlRes res = new GoogleControlRes();

        JSONArray execution = commonds.getJSONArray("execution");
        JSONArray devices = commonds.getJSONArray("devices");

        String userUuid = TokenInterceptor.getUserUUID();
        Long userId = TokenInterceptor.getUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        execution.forEach((commond) -> {
            List<String> endpointIds = new ArrayList<>();
            Map<String, Object> states = new HashMap<String, Object>();

            Map<String, Object> commondRes = new HashMap<String, Object>();
            commondRes.put("status", "SUCCESS");
            commondRes.put("ids", endpointIds);

            JSONObject commondJson = (JSONObject) commond;

            devices.forEach((device) -> {
                int i = 0;
                // 默认错误码 https://developers.google.com/actions/smarthome/create#actiondevicesexecute
                String errorCode = "unknownError";

                String endpointId = ((JSONObject) device).getString("id");
                commondJson.put("endpointId", endpointId);
                KeyValue kv = googleTransfor.toCommonKeyVal(commondJson);
                log.info("***** googleHome, execu, deviceId={}, kv.json={}", endpointId, JSON.toJSONString(kv));

                endpointIds.add(endpointId);
                Boolean flag = false;
                if (kv.getKey().equals(KeyValue.SCENE)) {
                    // 控制情景
                    CommonResponse commonResponse = googleHomeBusinessService.excSceneReq(endpointId, userUuid, tenantId);
                    if (commonResponse.getCode() == ResultMsg.SUCCESS.getCode()) {
                        flag = true;
                    } else {
                        errorCode = commonResponse.getDesc();
                        flag = false;
                    }

                } else {
                    // 控制设备
                    JSONObject attr = googleTransfor.getSelfKeyVal(kv);
                    states.put(attr.getString("key"), attr.get("value"));

                    String googleErrorCode = googleHomeBusinessService.setDevAttr(tenantId, kv, endpointId, userId);
                    if (googleErrorCode == null) {
                        flag = Boolean.TRUE;
                    } else {
                        flag = Boolean.FALSE;
                        errorCode = googleErrorCode;
                    }
                }

                if (flag == null || !flag) {
                    commondRes.put("status", "ERROR");
                    commondRes.put("errorCode", errorCode);
                } else {
                    states.put("online", true);
                    commondRes.put("states", states);
                }

                res.add(commondRes);
            });
        });
        return res;
    }

    // stay、away 模式切换
    private String armIntent(JSONObject args) {
        String key = VoiceBoxConst.SECURITY_COMMAND_ARM;
        String keyValue = args.getString("arm");
        log.info("***** armIntent(), key={}, value={}", key, keyValue);

        Long userId = TokenInterceptor.getUserId();
        String userUuid = TokenInterceptor.getUserUUID();

        SecurityCommand securityCommand = new SecurityCommand();
        securityCommand.setCommand(key);
        securityCommand.setArmMode(keyValue);

        String result = googleHomeBusinessService.setArmModeReq(userId, userUuid, securityCommand);
        return result;
    }

    // 把安防 设置为off模式
    private String disArmIntent(JSONObject args) {
        String key = VoiceBoxConst.SECURITY_COMMAND_DISARM;
        String keyValue = "";
        String pw = args.getString("password");

        log.info("***** disArmIntent(), key={}, keyValue={}, pw={}", key, keyValue, pw);
        if (pw == null) {
            return "Password error, please confirm the password.";
        }

        Long userId = TokenInterceptor.getUserId();
        String userUuid = TokenInterceptor.getUserUUID();

        SecurityCommand securityCommand = new SecurityCommand();
        securityCommand.setCommand(key);
        securityCommand.setArmMode("off");
        securityCommand.setPassword(pw.split("\\.")[0]);

        String result = googleHomeBusinessService.setArmModeReq(userId, userUuid, securityCommand);
        return result;
    }

    // 报警
    private String panicIntent(JSONObject args) {
        String key = VoiceBoxConst.SECURITY_COMMAND_PANIC;
        String keyValue = "panic";

        log.info("***** panicIntent(), key={}, keyValue={}", key, keyValue);

        Long userId = TokenInterceptor.getUserId();
        String userUuid = TokenInterceptor.getUserUUID();

        SecurityCommand securityCommand = new SecurityCommand();
        securityCommand.setCommand(key);
        securityCommand.setArmMode(keyValue);

        String result = googleHomeBusinessService.setArmModeReq(userId, userUuid, securityCommand);
        return result;
    }

    // 查询最近的5次活动日志
    private String queryActivitysIntent(JSONObject args) {
        log.info("***** googleHome, queryActivitysIntent, args={}", args);

        Long userId = TokenInterceptor.getUserId();
        String body = commonService.getActivityLogs(userId);

        log.info("***** googleHome, queryActivitysIntent, return data:{}", body);
        return body;
    }

    // 查询安防的状态
    private String queryArmStatusIntent(JSONObject args) {
        log.info("***** googleHome, queryArmStatusIntent start");

        Long userId = TokenInterceptor.getUserId();
        String userUuid = TokenInterceptor.getUserUUID();

        String result = googleHomeBusinessService.securityStatusQuery(userId, userUuid);
        log.info("***** googleHome, queryArmStatusIntent return data:{}", result);
        return result;
    }

    // 查询门磁或motion传感器的状态
    private String querySensorStatusIntent(JSONObject args) {
        log.info("***** googleHome, querySensorStatusIntent, args={}", args);

        String type = args.getString("type");

        Long userId = TokenInterceptor.getUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        String body = commonService.querySensorStatus(tenantId, userId, type);
        if (body == null) {
            body = VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE;
        }

        log.info("***** googleHome, querySensorStatusIntent, return data:{}", body);
        return body;
    }
}
