package com.iot.robot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.util.Maps;
import com.google.common.collect.Lists;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.util.StringUtil;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.robot.common.constant.VoiceBoxConst;
import com.iot.robot.common.exception.RobotException;
import com.iot.robot.norm.KeyValue;
import com.iot.robot.transform.AbstractTransfor;
import com.iot.robot.transform.convertor.AbstractConvertor;
import com.iot.robot.transform.convertor.YunKeyValue;
import com.iot.robot.utils.ErrorCodeKeys;
import com.iot.robot.utils.google.GoogleErrorCode;
import com.iot.robot.vo.SecurityCommand;
import com.iot.robot.vo.google.GoogleQueryRes;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Descrpiton: googleHome业务处理service
 * @Author: yuChangXing
 * @Date: 2018/10/9 16:30
 * @Modify by:
 */

@Service
public class GoogleHomeBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleHomeBusinessService.class);

    @Autowired
    private CommonService commonService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private List<AbstractConvertor> convertor;

    @Resource(name="googleTransfor")
    private AbstractTransfor googleTransfor;


    /**
     *  查询安防状态
     */
    public String securityStatusQuery(Long userId, String userUuid) {
        LOGGER.info("securityStatusQuery, userId={}, userUuid={}", userId, userUuid);

        String responseContent = VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE;
        try {
            MqttMsg mqttMsgResp = securityService.getStatusReq(userId, userUuid);
            if (mqttMsgResp == null) {
                LOGGER.info("securityStatusQuery, mqttMsgResp is null.");
                return VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE;
            }

            if (mqttMsgResp.getAck() != null &&
                    mqttMsgResp.getAck().getCode() != MqttMsgAck.SUCCESS) {
                // 查询安防状态失败
                if (StringUtil.isNotBlank(mqttMsgResp.getAck().getDesc())) {
                    responseContent = mqttMsgResp.getAck().getDesc();
                }
                return responseContent;
            }

            // 0:撤防,1:在家布防,3:离家布防,4:离家布防延迟,5:在家布防延迟,6:未就绪
            Map<String, Object> resultPayload = (Map<String, Object>)mqttMsgResp.getPayload();
            Object statusObj = resultPayload.get("status");
            if (statusObj != null) {
                String statusStr = String.valueOf(statusObj);
                switch (statusStr) {
                    case "0":
                        responseContent = "Security system is closed.";
                        break;
                    case "1":
                        responseContent = "Stay mode is activite.";
                        break;
                    case "3":
                        responseContent = "Away mode is activite.";
                        break;
                    case "4":
                        responseContent = "Away mode is activiting.";
                        break;
                    case "5":
                        responseContent = "Stay mode is activiting.";
                        break;
                    case "6":
                        responseContent = "Some security devices are not ready. Please check your security status.";
                        break;
                    default:
                        responseContent = VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE;
                        LOGGER.info("securityStatusQuery, invalid security status={}", statusStr);
                        break;
                }
            }
        } catch (RobotException e) {
            e.printStackTrace();

            if (ErrorCodeKeys.INVALID_USER.equals(e.getErrorCode())) {
                responseContent = "Sorry, you do not have permission to operate.";
            } else if(ErrorCodeKeys.TIMEOUT.equals(e.getErrorCode())){
                responseContent = "Request timeout, please try again later.";
            } else if(ErrorCodeKeys.UN_KNOW_ERROR.equals(e.getErrorCode())){
                responseContent = VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE;
            } else if(ErrorCodeKeys.UNBOUND_GATEWAY.equals(e.getErrorCode())){
                responseContent = "You haven't add any security devices yet.";
            }

        } catch (Exception e) {
            e.printStackTrace();

            responseContent = VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE;
        }

        return responseContent;
    }

    /**
     *  布置安防请求
     *
     * @param userId
     * @param userUuid
     * @param securityCommand
     */
    public String setArmModeReq(Long userId, String userUuid, SecurityCommand securityCommand) {
        LOGGER.info("setArmModeReq, userId={}, userUuid={}, securityCommand={}", userId, userUuid, JSON.toJSONString(securityCommand));

        String responseContent = VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE;
        try {
            MqttMsg mqttMsgResp = securityService.setArmModeReq(userId, userUuid, securityCommand);
            if (mqttMsgResp == null) {
                LOGGER.info("setArmModeReq, mqttMsgResp is null.");
                return VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE;
            }

            if (mqttMsgResp.getAck() != null &&
                    mqttMsgResp.getAck().getCode() != MqttMsgAck.SUCCESS) {
                // 切换 安防模式失败
                if (StringUtil.isNotBlank(mqttMsgResp.getAck().getDesc())) {
                    responseContent = mqttMsgResp.getAck().getDesc();
                }
                return responseContent;
            }

            // 处理切换结果
            if (VoiceBoxConst.SECURITY_COMMAND_PANIC.equals(securityCommand.getCommand())) {
                // 报警
                responseContent = "ok";
            } else {
                Map<String, Object> resultPayload = (Map<String, Object>)mqttMsgResp.getPayload();
                Object statusObj = resultPayload.get("status");
                if (statusObj != null) {
                    String statusStr = String.valueOf(statusObj);
                    // 0:撤防,1:在家布防,3:离家布防,4:无效密码,5:未就绪(bypass使用)
                    switch (statusStr) {
                        case "0":
                            responseContent = "OK, your security system closure success.";
                            break;
                        case "1":
                            responseContent = "OK, the stay mode is starting.";
                            break;
                        case "3":
                            responseContent = "OK, the away mode is starting.";
                            break;
                        case "4":
                            break;
                        case "5":
                            responseContent = "Some security devices are not ready. Please check your security status.";
                            break;
                        default:
                            responseContent = VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE;
                            LOGGER.info("setArmModeReq, invalid security status={}", statusStr);
                            break;
                    }
                }
            }

        } catch (RobotException e) {
            e.printStackTrace();

            if (ErrorCodeKeys.INVALID_USER.equals(e.getErrorCode())) {
                responseContent = "Sorry, you do not have permission to operate.";
            } else if(ErrorCodeKeys.PWD_ERROR.equals(e.getErrorCode())){
                responseContent ="Password error, please confirm the password.";
            } else if(ErrorCodeKeys.TIMEOUT.equals(e.getErrorCode())){
                responseContent = "Request timeout, please try again later.";
            } else if(ErrorCodeKeys.UN_KNOW_ERROR.equals(e.getErrorCode())){
                responseContent = VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE;
            } else if(ErrorCodeKeys.UNBOUND_GATEWAY.equals(e.getErrorCode())){
                responseContent = "You haven't add any security devices yet.";
            }

        } catch (Exception e) {
            e.printStackTrace();

            responseContent = VoiceBoxConst.DEFAULT_ERROR_TIP_MESSAGE;
        }

        return responseContent;
    }


    /**
     *  设备控制(设置设备属性)
     */
    public String setDevAttr(Long tenantId, KeyValue kv, String endpointId, Long userId) {
        LOGGER.info("setDevAttr, endpointId={}, userId={}, kv={}", endpointId, userId, JSON.toJSONString(kv));

        String googleErrorCode = null;
        try {
            List<KeyValue> kvList = Lists.newArrayList();
            kvList.add(kv);
            deviceService.setDevAttrFromKV(tenantId, kvList, endpointId, userId);
        } catch (RobotException e) {
            e.printStackTrace();

            googleErrorCode = GoogleErrorCode.getErrorCodeByYunCode(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();

            googleErrorCode = GoogleErrorCode.UN_KNOW_ERROR.getGoogleCode();
        }

        LOGGER.info("setDevAttr, endpointId={}, userId={}, kv={}, final googleErrorCode = {}", endpointId, userId, JSON.toJSONString(kv), googleErrorCode);
        return googleErrorCode;
    }


    /**
     * 执行场景请求
     *
     * @param sceneId
     * @return
     */
    public CommonResponse excSceneReq(String sceneId, String userUuid, Long tenantId) {
        LOGGER.info("excSceneReq, sceneId={}, userUuid={}", sceneId, userUuid);

        CommonResponse<Object> response = new CommonResponse<>(ResultMsg.SUCCESS);

        try {
            // 发起请求
            sceneService.excSceneReq(sceneId, userUuid, tenantId);
            LOGGER.info("excSceneReq, success.");
        } catch (RobotException e) {
            e.printStackTrace();

            response = new CommonResponse(ResultMsg.FAIL);

            // 解析错误码
            response.setDesc(GoogleErrorCode.getErrorCodeByYunCode(e.getErrorCode()));
        } catch (Exception e) {
            e.printStackTrace();

            response = new CommonResponse(ResultMsg.FAIL);
            response.setDesc(GoogleErrorCode.UN_KNOW_ERROR.getGoogleCode());
        }

        LOGGER.info("excSceneReq, sceneId={}, userUuid={}, final response = {}", sceneId, userUuid, JSON.toJSONString(response));
        return response;
    }

    /**
     * 获取设备状态
     *
     */
    public GoogleQueryRes deviceStatusQuery(JSONArray devices, Long userId) {
        LOGGER.info("deviceStatusQuery, userId={}", userId);

        GoogleQueryRes queryRes = new GoogleQueryRes();

        for (Object deviceTemp : devices) {
            JSONObject device = (JSONObject)deviceTemp;
            String endpointId = device.getString("id");

            Map<String,Object> deviceInfoMap = Maps.newHashMap();

            try {
                Long tenantId = SaaSContextHolder.currentTenantId();
                Map<String, Object> devAttrMap = deviceService.findDevAttrMap(tenantId, endpointId, userId);
                LOGGER.info("deviceStatusQuery, endpointId={}, devAttrMap={}", endpointId, devAttrMap);

                deviceInfoMap.put("online", true);

                // 循环解析属性
                Iterator iter = devAttrMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String)entry.getKey();
                    Object val = entry.getValue();

                    YunKeyValue ykv = new YunKeyValue();
                    ykv.setKey(key);
                    ykv.setValue(val);
                    LOGGER.info("deviceStatusQuery, current ykv={}", JSON.toJSONString(ykv));

                    int n = convertor.indexOf(ykv);
                    if (n == -1) {
                        LOGGER.info("deviceStatusQuery, key={} not found convertor!", key);
                        continue;
                    }

                    KeyValue kv = convertor.get(n).toCommonKV(ykv);
                    if (kv == null) {
                        LOGGER.info("deviceStatusQuery, after toCommonKV, googleHome kv is null");
                        continue;
                    }

                    JSONObject attr = googleTransfor.getSelfKeyVal(kv);
                    LOGGER.info("deviceStatusQuery, after getSelfKeyVal, googleHome attr={}", attr);
                    if (attr == null || attr.isEmpty()) {
                        continue;
                    }

                    String gkey = attr.getString("key");
                    Object gval = attr.get("value");
                    deviceInfoMap.put(gkey, gval);
                }

            } catch (RobotException e) {
                e.printStackTrace();

                if (ErrorCodeKeys.DEVICE_NOT_FOUND.equals(e.getErrorCode())) {
                    // 设备不存在
                    deviceInfoMap.put("online", false);
                }else if (ErrorCodeKeys.DEVICE_OFFLINE.equals(e.getErrorCode())) {
                    // 设备离线
                    deviceInfoMap.put("online", false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            queryRes.add(endpointId, deviceInfoMap);
        }

        return queryRes;
    }
}
