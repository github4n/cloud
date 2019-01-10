package com.iot.widget.controller;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.widget.constant.WidgetConstants;
import com.iot.widget.exception.UserWidgetExceptionEnum;
import com.iot.widget.service.DeviceService;
import com.iot.widget.service.SceneService;
import com.iot.widget.service.SecurityService;
import com.iot.widget.utils.ResponseUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 项目名称: IOT云平台
 * 模块名称：ios端widget功能接口
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/3 21:45
 * 修改人:
 * 修改时间：
 */

@Api(description = "ios端widget功能接口")
@RestController
@RequestMapping("/widget")
public class WidgetControlControllerV1 {
    private Logger LOGGER = LoggerFactory.getLogger(WidgetControlControllerV1.class);

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserApi userApi;


    @RequestMapping(value = "/control/v1", method = RequestMethod.POST)
    public JSONObject controlV1(@RequestBody JSONObject req) {
        LOGGER.info("***** WidgetControlControllerV1, controlV1, req={}", req.toJSONString());

        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();
        String userUuid = userApi.getUuid(userId);

        Map<String, Object> resultMap = null;

        String command = req.getString("command");
        JSONObject payload = req.getJSONObject("payload");
        try {
            if (StringUtil.isBlank(command)) {
                LOGGER.error("controlV1 error, command is null, userId={}, tenantId={}", userId, tenantId);
                throw new BusinessException(UserWidgetExceptionEnum.PARAMETER_ERROR);
            }

            switch (command) {
                case "action.security.setArmModeReq":
                    // 控制安防 请求
                    String homeId = payload.getString("homeId");
                    String armMode = payload.getString("armMode");
                    Map<String, Object> respPayload = securityService.setArmModeReq(tenantId, userUuid, homeId, armMode);
                    resultMap = ResponseUtil.buildSuccessControlResponse(WidgetConstants.COMMAND_SET_ARM_MODE_RESP, respPayload);
                    break;
                case "action.device.setDevAttrReq":
                    // 控制设备 请求
                    String deviceId = payload.getString("devId");
                    Map<String, Object> attr = payload.getJSONObject("attr");
                    boolean exeSuccess = deviceService.setDevAttr(tenantId, userId, deviceId, attr);
                    if (exeSuccess) {
                        resultMap = ResponseUtil.buildSuccessControlResponse(WidgetConstants.COMMAND_SET_DEV_ATTR_RESP, payload);
                    }
                    break;
                case "action.scene.excSceneReq":
                    // 控制情景 请求
                    String sceneId = payload.getString("sceneId");
                    sceneService.excSceneReq(tenantId, userUuid, sceneId);
                    resultMap = ResponseUtil.buildSuccessControlResponse(WidgetConstants.COMMAND_EXC_SCENE_RESP, payload);
                    break;
                default:
                    // 不支持协议
                    resultMap = ResponseUtil.buildFailControlResponse(command, payload, UserWidgetExceptionEnum.NOT_SUPPORTED.getMessageKey());
                    break;
            }

        } catch (BusinessException e) {
            e.printStackTrace();
            resultMap = ResponseUtil.buildFailControlResponse(command, payload, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();

            // 未知错误
            LOGGER.error("service error, userId={}, tenantId={}", userId, tenantId);
            resultMap = ResponseUtil.buildFailControlResponse(command, payload, UserWidgetExceptionEnum.UN_KNOWN_ERROR.getMessageKey());
        }

        JSONObject resultJsonObj = new JSONObject(resultMap);
        LOGGER.info("***** WidgetControlControllerV1, controlV1, response data ={}", resultJsonObj);
        return resultJsonObj;
    }


}
