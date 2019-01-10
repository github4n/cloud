package com.iot.robot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.robot.common.constant.DeviceOnlineStatusEnum;
import com.iot.robot.common.constant.VoiceBoxConst;
import com.iot.robot.common.exception.RobotException;
import com.iot.robot.norm.KeyValue;
import com.iot.robot.norm.OnOffKeyValue;
import com.iot.robot.norm.StrobeKeyValue;
import com.iot.robot.norm.WarnigKeyValue;
import com.iot.robot.transform.AbstractTransfor;
import com.iot.robot.transform.convertor.AbstractConvertor;
import com.iot.robot.transform.convertor.ValueConvertor;
import com.iot.robot.transform.convertor.YunKeyValue;
import com.iot.robot.utils.ErrorCodeKeys;
import com.iot.robot.utils.alexa.AlexaErrorCode;
import com.iot.robot.vo.IntentResultResp;
import com.iot.robot.vo.SecurityCommand;
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
 * @Descrpiton: alexa业务处理service
 * @Author: yuChangXing
 * @Date: 2018/10/9 16:20
 * @Modify by:
 */

@Service
public class AlexaBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlexaBusinessService.class);


    @Autowired
    private DeviceService deviceService;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private List<AbstractConvertor> convertor;

    @Resource(name="alexaTransfor")
    private AbstractTransfor alexaTransfor;


    /**
     *  查询安防状态
     */
    public IntentResultResp securityStatusQuery(Long userId, String userUuid) {
        LOGGER.info("securityStatusQuery, userId={}, userUuid={}", userId, userUuid);

        IntentResultResp intentResultResp = new IntentResultResp();

        try {
            MqttMsg mqttMsgResp = securityService.getStatusReq(userId, userUuid);
            if (mqttMsgResp == null) {
                LOGGER.info("securityStatusQuery, mqttMsgResp is null.");
                intentResultResp.setCode(IntentResultResp.ERROR);
                return intentResultResp;
            }

            if (mqttMsgResp.getAck() != null &&
                    mqttMsgResp.getAck().getCode() != MqttMsgAck.SUCCESS) {
                // 查询安防状态失败
                intentResultResp.setCode(mqttMsgResp.getAck().getCode());
                intentResultResp.setErrorMsg(mqttMsgResp.getAck().getDesc());
                return intentResultResp;
            }

            // 0:撤防,1:在家布防,3:离家布防,4:离家布防延迟,5:在家布防延迟,6:未就绪
            Map<String, Object> resultPayload = (Map<String, Object>)mqttMsgResp.getPayload();
            Object statusObj = resultPayload.get("status");
            LOGGER.info("securityStatusQuery, statusObj={}", statusObj);
            if (statusObj != null) {
                Integer status = Integer.parseInt(String.valueOf(statusObj));
                intentResultResp.setCode(status);
            }
        } catch (RobotException e) {
            e.printStackTrace();

            if (ErrorCodeKeys.INVALID_USER.equals(e.getErrorCode())) {
                intentResultResp.setCode(IntentResultResp.INVALIDUSER);
            } else if(ErrorCodeKeys.TIMEOUT.equals(e.getErrorCode())){
                intentResultResp.setCode(IntentResultResp.ERROR);
            } else if(ErrorCodeKeys.UN_KNOW_ERROR.equals(e.getErrorCode())){
                intentResultResp.setCode(IntentResultResp.ERROR);
            } else if(ErrorCodeKeys.UNBOUND_GATEWAY.equals(e.getErrorCode())){
                intentResultResp.setCode(IntentResultResp.STATUS_UNREADY);
            }
        } catch (Exception e) {
            e.printStackTrace();

            intentResultResp.setCode(IntentResultResp.ERROR);
        }

        return intentResultResp;
    }

    /**
     * 取消报警(不是把安防 设置为off模式)
     */
    public boolean setSecurityCancelSos(Long tenantId, Long userId) {
        String directDeviceUuid = deviceService.getUserGateWayUuid(tenantId, userId);
        if (directDeviceUuid == null) {
            LOGGER.info("setSecurityCancelSos, user directDeviceUuid is null, userId={}", userId);
            return false;
        }

        WarnigKeyValue wk = new WarnigKeyValue();
        wk.setFixedValue("off");
        StrobeKeyValue sk = new StrobeKeyValue();
        sk.setFixedValue("off");

        boolean exeSuccess = false;
        try {
            Map<String, Object> changeAttrMap = parseToYunKeyValue(wk, sk);
            exeSuccess = deviceService.setDevAttr(userId, tenantId, directDeviceUuid, changeAttrMap);
        } catch (RobotException e) {
            e.printStackTrace();
        }

        return exeSuccess;
    }


    // 解析为 YunKeyValue
    private Map<String, Object> parseToYunKeyValue(KeyValue... kvs) {
        Map<String, Object> attrMap = Maps.newHashMap();

        int i = -1;
        // robot控制属性 转 云端
        for (KeyValue kv : kvs) {
            if ((i = convertor.indexOf(kv)) <= 0) {
                //错误
                LOGGER.info("***** parseToYunKeyValue, 找不到属性转换器, kv={}", JSON.toJSONString(kv));
                continue;
            }

            ValueConvertor c = convertor.get(i);
            YunKeyValue ykv = c.valueConvert(kv, null);
            attrMap.put(ykv.getKey(), ykv.getValue());
        }

        return attrMap;
    }

    /**
     *  布置安防请求
     *
     * @param userId
     * @param userUuid
     * @param securityCommand
     */
    public IntentResultResp setArmModeReq(Long userId, String userUuid, SecurityCommand securityCommand) {
        LOGGER.info("setArmModeReq, userId={}, userUuid={}, securityCommand={}", userId, userUuid, JSON.toJSONString(securityCommand));

        IntentResultResp intentResultResp = new IntentResultResp();

        try {
            MqttMsg mqttMsgResp = securityService.setArmModeReq(userId, userUuid, securityCommand);
            if (mqttMsgResp == null) {
                LOGGER.info("setArmModeReq, mqttMsgResp is null.");
                intentResultResp.setCode(IntentResultResp.ERROR);
                return intentResultResp;
            }

            if (mqttMsgResp.getAck() != null &&
                    mqttMsgResp.getAck().getCode() != MqttMsgAck.SUCCESS) {
                // 切换 安防模式失败
                intentResultResp.setCode(mqttMsgResp.getAck().getCode());
                intentResultResp.setErrorMsg(mqttMsgResp.getAck().getDesc());
                return intentResultResp;
            }

            // 处理切换结果
            if (VoiceBoxConst.SECURITY_COMMAND_PANIC.equals(securityCommand.getCommand())) {
                // 报警
                intentResultResp.setCode(IntentResultResp.SUCCESS);
            } else {
                Map<String, Object> resultPayload = (Map<String, Object>)mqttMsgResp.getPayload();
                Object statusObj = resultPayload.get("status");
                if (statusObj != null) {
                    Integer status = Integer.parseInt(String.valueOf(statusObj));
                    intentResultResp.setCode(status);
                    if (status == 5) {
                        // 某些安防设备 未就绪

                        List<Map<String, Object>> triggerList = (List<Map<String, Object>>) resultPayload.get("trigger");
                        LOGGER.info("setArmModeReq, triggerList={}", JSON.toJSONString(triggerList));
                        /*for (Map<String, Object> objMap : triggerList) {
                            String deviceId = String.valueOf(objMap.get("devId"));
                        }*/
                    }
                }
            }

        } catch (RobotException e) {
            e.printStackTrace();

            if (ErrorCodeKeys.INVALID_USER.equals(e.getErrorCode())) {
                intentResultResp.setCode(IntentResultResp.INVALIDUSER);
            } else if(ErrorCodeKeys.PWD_ERROR.equals(e.getErrorCode())){
                intentResultResp.setCode(IntentResultResp.PWERROR);
            } else if(ErrorCodeKeys.TIMEOUT.equals(e.getErrorCode())){
                intentResultResp.setCode(IntentResultResp.ERROR);
            } else if(ErrorCodeKeys.UN_KNOW_ERROR.equals(e.getErrorCode())){
                intentResultResp.setCode(IntentResultResp.ERROR);
            } else if(ErrorCodeKeys.UNBOUND_GATEWAY.equals(e.getErrorCode())){
                intentResultResp.setCode(IntentResultResp.UNREADY);
            }

        } catch (Exception e) {
            e.printStackTrace();
            intentResultResp.setCode(IntentResultResp.ERROR);
        }

        return intentResultResp;
    }

    /**
     *  设备控制(设置设备属性)
     */
    public CommonResponse setDevAttr(Long tenantId, KeyValue kv, String endpointId, Long userId) {
        LOGGER.info("setDevAttr, endpointId={}, userId={}, kv={}", endpointId, userId, JSON.toJSONString(kv));

        CommonResponse<Object> response = new CommonResponse<>(ResultMsg.SUCCESS);

        AlexaErrorCode errorCode = null;
        try {
            List<KeyValue> kvList = Lists.newArrayList();
            kvList.add(kv);
            boolean exeSuccess = deviceService.setDevAttrFromKV(tenantId, kvList, endpointId, userId);
            if (exeSuccess) {
                // 控制成功
                JSONArray respArray = new JSONArray();
                JSONObject respObj = alexaTransfor.getSelfKeyVal(kv);
                respArray.add(respObj);

                response.setData(respArray);
            }
        } catch (RobotException e) {
            e.printStackTrace();

            errorCode = AlexaErrorCode.getAlexaErrorCodeByYunCode(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();

            errorCode = AlexaErrorCode.INTERNAL_ERROR;
        }

        if (errorCode != null) {
            // 执行出错
            response = new CommonResponse(ResultMsg.FAIL);

            Map<String, String> errorMap = Maps.newHashMap();
            errorMap.put("type", errorCode.getAlexaCode());
            errorMap.put("message", errorCode.getDesc());

            response.setData(errorMap);
        }

        return response;
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
            AlexaErrorCode errorCode = AlexaErrorCode.getAlexaErrorCodeByYunCode(e.getErrorCode());
            Map<String, String> errorMap = Maps.newHashMap();
            errorMap.put("type", errorCode.getAlexaCode());
            errorMap.put("message", errorCode.getDesc());
            response.setData(errorMap);
        } catch (Exception e) {
            e.printStackTrace();

            response = new CommonResponse<>(ResultMsg.FAIL);
        }

        return response;
    }

    /**
     * 获取设备状态
     *
     * @param deviceId
     */
    public CommonResponse deviceStatusQuery(String deviceId, Long userId) {
        LOGGER.info("deviceStatusQuery, deviceId={}", deviceId);

        CommonResponse<Object> response = new CommonResponse<>(ResultMsg.SUCCESS);

        JSONArray res = new JSONArray();

        try {
            Long tenantId = SaaSContextHolder.currentTenantId();
            Map<String, Object> devAttrMap = deviceService.findDevAttrMap(tenantId, deviceId, userId);
            LOGGER.info("deviceStatusQuery, devAttrMap={}", devAttrMap);

            // 设备在线 信息
            JSONObject connectivityJson = alexaTransfor.getConnectivity(DeviceOnlineStatusEnum.CONNECTED.getCode());
            res.add(connectivityJson);

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
                    LOGGER.info("deviceStatusQuery, after toCommonKV, alexa kv is null");
                    continue;
                }

                JSONObject attr = alexaTransfor.getSelfKeyVal(kv);
                LOGGER.info("deviceStatusQuery, after getSelfKeyVal, alexa attr={}", attr);
                if (attr == null || attr.isEmpty()) {
                    continue;
                }

                res.add(attr);
            }

            response.setData(res);

        } catch (RobotException e) {
            e.printStackTrace();

            if (ErrorCodeKeys.DEVICE_NOT_FOUND.equals(e.getErrorCode())) {
                // 设备不存在
                AlexaErrorCode errorCode = AlexaErrorCode.getAlexaErrorCodeByYunCode(e.getErrorCode());
                Map<String, String> errorMap = Maps.newHashMap();
                errorMap.put("type", errorCode.getAlexaCode());
                errorMap.put("message", errorCode.getDesc());

                response = new CommonResponse<>(ResultMsg.FAIL);
                response.setData(errorMap);
                return response;

            }else if (ErrorCodeKeys.DEVICE_OFFLINE.equals(e.getErrorCode())) {
                // 设备离线
                JSONObject connectivityJson = alexaTransfor.getConnectivity(DeviceOnlineStatusEnum.DISCONNECTED.getCode());
                res.add(connectivityJson);

                // 设备离线, 返回设备OnOff状态=0
                KeyValue<Byte> kv = new OnOffKeyValue();
                kv.setFixedValue(Byte.valueOf("0"));
                JSONObject offJson = alexaTransfor.getSelfKeyVal(kv);
                res.add(offJson);

                response.setData(res);
                return response;
            }

        } catch (Exception e) {
            e.printStackTrace();

            Map<String, String> errorMap = Maps.newHashMap();
            errorMap.put("type", AlexaErrorCode.INTERNAL_ERROR.getAlexaCode());
            errorMap.put("message", AlexaErrorCode.INTERNAL_ERROR.getDesc());

            response = new CommonResponse<>(ResultMsg.FAIL);
            response.setData(errorMap);
            return response;
        }

        return response;
    }
}
