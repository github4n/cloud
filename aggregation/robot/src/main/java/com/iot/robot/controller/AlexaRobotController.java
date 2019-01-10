package com.iot.robot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.redis.RedisCacheUtil;
import com.iot.robot.common.constant.VoiceBoxConst;
import com.iot.robot.interceptors.TokenInterceptor;
import com.iot.robot.norm.KeyValue;
import com.iot.robot.service.AlexaBusinessService;
import com.iot.robot.service.CommonService;
import com.iot.robot.service.DeviceService;
import com.iot.robot.service.SceneService;
import com.iot.robot.transform.AbstractTransfor;
import com.iot.robot.vo.DeviceInfo;
import com.iot.robot.vo.IntentResultResp;
import com.iot.robot.vo.SecurityCommand;
import com.iot.robot.vo.alexa.AlexaDiscoveryResponse;
import com.iot.robot.vo.alexa.ArmVo;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.SmartTokenApi;
import com.iot.user.api.UserApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */

@RestController
@RequestMapping("alexa")
@Api("alexa服务接口")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AlexaRobotController {

    private Logger log = LoggerFactory.getLogger(AlexaRobotController.class);

    @Autowired
    private UserApi userApi;
    @Autowired
    private SmartTokenApi smartTokenApi;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private AlexaBusinessService alexaBusinessService;

    @Autowired
    private CommonService commonService;

    @Resource(name = "alexaTransfor")
    private AbstractTransfor alexaTransfor;


    //smarthome技能：发现 设备与情景
    @ApiOperation("发现接口")
    @RequestMapping(value = "discovery", method = RequestMethod.GET)
    public CommonResponse<AlexaDiscoveryResponse> discovery(@RequestHeader(name = "messageid") String messageId) {
        CommonResponse<AlexaDiscoveryResponse> result = new CommonResponse<>(ResultMsg.SUCCESS);
        Long userId = TokenInterceptor.getUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        log.info("***** alexa, discovery, userId={}, messageId={}", userId, messageId);

        List<DeviceInfo> devices = deviceService.findDeviceListByUserId(tenantId, userId);
        List<SceneResp> scenes = sceneService.findSceneRespListByUserId(tenantId, userId);

        log.info("***** alexa, discovery, 设备数量:{}, 情景数量:{}", devices.size(), scenes.size());

        //设备发现方法
        AlexaDiscoveryResponse data = (AlexaDiscoveryResponse) alexaTransfor.getResponse(new List[]{devices, scenes});
        data.build(messageId);
        result.setData(data);

        log.info("***** alexa, discovery(), return data:{}", JSON.toJSONString(result));
        return result;
    }

    // smarthome技能：设备与情景控制接口
    @ApiOperation("设备与情景控制接口")
    @RequestMapping(value = "control", method = RequestMethod.POST)
    public CommonResponse<Object> control(@RequestBody JSONObject req, @RequestHeader(name = "messageid") String messageId,
                                          @RequestHeader(name = "correlationtoken") String correlationtoken) {
        String controlType = "";
        log.info("***** alexa, control, req={}", req);

        String endpointId = req.getString("endpointId");
        KeyValue vs = alexaTransfor.toCommonKeyVal(req);

        log.info("***** alexa, control, tempMsgId={}, endpointId={}, KeyValue={}", endpointId, JSON.toJSONString(vs));

        String userUuid = TokenInterceptor.getUserUUID();
        Long userId = TokenInterceptor.getUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        CommonResponse<Object> result = new CommonResponse<>(ResultMsg.SUCCESS);
        try {
            if (vs.getKey().equals(KeyValue.SCENE)) {
                // 控制情景
                controlType = "scene";
                result = alexaBusinessService.excSceneReq(endpointId, userUuid, tenantId);
            } else if (vs.getKey().equals(KeyValue.QUERY)) {
                // 设备状态查询
                controlType = "deviceStatusQuery";
                result = alexaBusinessService.deviceStatusQuery(endpointId, userId);
            } else {
                // 控制设备
                controlType = "setDevAttr";
                result = alexaBusinessService.setDevAttr(tenantId, vs, endpointId, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new CommonResponse<>(ResultMsg.FAIL);
        }

        log.info("***** alexa, control, {} return data:{}", controlType, JSON.toJSONString(result));
        return result;
    }

    //smarthome技能： 获取aws的token。反向授权，根据code获取AWS的accessToken，然后用来主动上报设备的属性变化
    @RequestMapping(value = "auth", method = RequestMethod.POST)
    public CommonResponse<Object> auth(@RequestBody JSONObject req) {
        CommonResponse<Object> result = new CommonResponse<>(ResultMsg.FAIL);
        String code = req.getString("code");
        Long userId = TokenInterceptor.getUserId();
        log.info("***** alexa, auth, code={}, userId={}", code, userId);

        boolean flag = smartTokenApi.saveTokenForAlexa(code, userId);
        log.info("***** alexa, auth, flag={}", flag);

        if (!flag) {
            return result;
        }
        result = new CommonResponse<>(ResultMsg.SUCCESS);
        return result;
    }

    //安防的控制（自定义技能）
    @RequestMapping(value = "armOrPanic", method = RequestMethod.POST)
    public CommonResponse<Object> armOrPanic(@RequestBody ArmVo vo) {
        CommonResponse<Object> result = new CommonResponse<>(ResultMsg.SUCCESS);
        IntentResultResp res = null;

        String key = vo.getType();
        String value = vo.getValue();

        log.info("***** alexa, armOrPanic(), vo={}", JSON.toJSONString(vo));

        Long userId = TokenInterceptor.getUserId();
        String userUuid = TokenInterceptor.getUserUUID();
        Long tenantId = SaaSContextHolder.currentTenantId();

        SecurityCommand securityCommand = new SecurityCommand();
        securityCommand.setCommand(key);
        securityCommand.setArmMode(value);

        if (VoiceBoxConst.SECURITY_COMMAND_PANIC.equals(key)) {
            // 报警
            log.info("***** armOrPanic, 报警");

            res = alexaBusinessService.setArmModeReq(userId, userUuid, securityCommand);
            result.setData(res);
        } else if (VoiceBoxConst.SECURITY_COMMAND_UN_PANIC.equals(key)) {
            // 取消报警(不是把安防 设置为off模式)
            log.info("***** armOrPanic, 取消报警");

            boolean exeSuccess = alexaBusinessService.setSecurityCancelSos(tenantId, userId);
            if (exeSuccess) {
                result = new CommonResponse<>(ResultMsg.SUCCESS);
            } else {
                result = new CommonResponse<>(ResultMsg.FAIL);
            }
        } else {
            // 布防
            log.info("***** armOrPanic, 布防 key={}", key);

            if (VoiceBoxConst.SECURITY_COMMAND_DISARM.equals(key)) {
                // 把安防 设置为off模式
                securityCommand.setPassword(value);
            }

            res = alexaBusinessService.setArmModeReq(userId, userUuid, securityCommand);
            result.setData(res);
        }

        log.info("***** alexa, armOrPanic(), return data:{}", JSON.toJSONString(result));
        return result;
    }

    //安防的控制（自定义技能）：查询安防的状态
    @RequestMapping(value = "securityStatusQuery", method = RequestMethod.POST)
    public CommonResponse<Object> securityStatusQuery() {
        log.info("***** alexa, securityStatusQuery start");

        Long userId = TokenInterceptor.getUserId();
        String userUuid = TokenInterceptor.getUserUUID();

        IntentResultResp intentResultResp = alexaBusinessService.securityStatusQuery(userId, userUuid);
        CommonResponse<Object> result = new CommonResponse<>(ResultMsg.SUCCESS);
        result.setData(intentResultResp);

        log.info("***** alexa, securityStatusQuery return data:{}", JSON.toJSONString(result));
        return result;
    }

    //安防的控制（自定义技能）：查询门磁或motion传感器的状态
    @RequestMapping(value = "querySensorStatus", method = RequestMethod.POST)
    public CommonResponse<String> querySensorStatus(@RequestBody ArmVo vo) {
        log.info("***** alexa, querySensorStatus, vo={}", JSON.toJSONString(vo));

        String type = vo.getType();

        Long userId = TokenInterceptor.getUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        String body = commonService.querySensorStatus(tenantId, userId, type);

        CommonResponse<String> result = new CommonResponse<>(ResultMsg.SUCCESS);
        result.setData(body);

        log.info("***** alexa, querySensorStatus, return data:{}", JSON.toJSONString(result));
        return result;
    }

    //安防的控制（自定义技能）：查询最近的5次活动日志
    @RequestMapping(value = "queryActivitys", method = RequestMethod.POST)
    public CommonResponse<String> queryActivitys() {
        log.info("***** alexa, queryActivitys start");

        //安防控制
        Long userId = TokenInterceptor.getUserId();
        String body = commonService.getActivityLogs(userId);

        CommonResponse<String> result = new CommonResponse<>(ResultMsg.SUCCESS);
        result.setData(body);

        log.info("***** alexa, queryActivitys, return data:{}", JSON.toJSONString(result));
        return result;
    }

    //情境类型与情景功能点的映射关系
    @RequestMapping(value = "init")
    public void init() {
        RedisCacheUtil.hashPut("Alexa", "type.scene", "ACTIVITY_TRIGGER", false);
        RedisCacheUtil.hashPut("Alexa", "capability.scene", "SceneController", false);

        RedisCacheUtil.hashPut("GoogleHome", "type.scene", "action.devices.types.SCENE", false);
        RedisCacheUtil.hashPut("GoogleHome", "capability.scene", "Scene", false);
    }

}