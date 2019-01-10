package com.iot.robot.rabbit.consumer.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.util.Maps;
import com.google.common.collect.Lists;
import com.iot.common.beans.CommonResponse;
import com.iot.control.device.api.UserDeviceCoreApi;
import com.iot.control.device.vo.req.ListUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.robot.norm.KeyValue;
import com.iot.robot.service.CommonService;
import com.iot.robot.transform.AbstractTransfor;
import com.iot.robot.transform.convertor.AbstractConvertor;
import com.iot.robot.transform.convertor.YunKeyValue;
import com.iot.robot.utils.alexa.ChangeStateUtil;
import com.iot.robot.vo.SmartVoiceBoxNotifyVo;
import com.iot.robot.vo.alexa.AlexaChangeStateReport;
import com.iot.robot.vo.google.ReportStateVo;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.user.api.SmartTokenApi;
import com.iot.user.api.UserApi;
import com.iot.user.constant.SmartHomeConstants;
import com.iot.user.vo.SmartTokenResp;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Descrpiton: device公共的 process
 * @Author: yuChangXing
 * @Date: 2018/10/15 10:03
 * @Modify by:
 */

@Component
public class DeviceCommonProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceCommonProcess.class);

    @Autowired
    private List<AbstractConvertor> convertor;
    @Autowired
    private UserApi userApi;
    @Autowired
    private SmartTokenApi smartTokenApi;
    @Autowired
    private CommonService commonService;
    @Autowired
    private UserDeviceCoreApi userDeviceCoreApi;
    @Autowired
    private DeviceCoreServiceApi deviceCoreServiceApi;

    @Resource(name="alexaTransfor")
    private AbstractTransfor alexaTransfor;


    public void processDeviceStatusChange(SmartVoiceBoxNotifyVo notifyVo) {
        try {
            LOGGER.debug("***** processDeviceStatusChange, notifyVo={}", JSONObject.toJSON(notifyVo));

            String deviceId = notifyVo.getDeviceId();

            GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
            if (deviceInfoRespVo == null) {
                LOGGER.debug("***** processDeviceStatusChange, deviceInfoRespVo is null");
                return;
            }

            Long tenantId = deviceInfoRespVo.getTenantId();

            ListUserDeviceInfoReq params = new ListUserDeviceInfoReq();
            params.setDeviceId(deviceId);
            params.setTenantId(tenantId);

            List<ListUserDeviceInfoRespVo> list = userDeviceCoreApi.listUserDevice(params);
            if (CollectionUtils.isEmpty(list)) {
                LOGGER.debug("***** processDeviceStatusChange, userDeviceList is empty, params.jsonString={}", JSONObject.toJSON(params));
                return;
            }

            Long userId = list.get(0).getUserId();
            List<SmartTokenResp> smartTokenList = smartTokenApi.findSmartTokenListByUserId(userId);
            if (CollectionUtils.isEmpty(smartTokenList)) {
                LOGGER.debug("***** processDeviceStatusChange, smartTokenList is empty.");
                return;
            }

            String userUuid = userApi.getUuid(userId);
            notifyVo.setUserUUID(userUuid);

            Map<String, Object> changeAttrs = notifyVo.getAttrMap();
            Integer onlineStatus = notifyVo.getOnlineStatus();

            LOGGER.debug("***** processDeviceStatusChange, smartTokenList.size()={}", smartTokenList.size());

            for (SmartTokenResp smartToken : smartTokenList) {
                LOGGER.debug("***** processDeviceStatusChange, current smartToken={}", JSON.toJSONString(smartToken));

                if (smartToken == null) {
                    LOGGER.debug("***** processDeviceStatusChange, current smartToken is null");
                    continue;
                }

                Integer smartType = smartToken.getSmart();
                if (smartType == null) {
                    LOGGER.debug("***** processDeviceStatusChange, current smartType is null, smartToken={}", JSON.toJSONString(smartToken));
                    continue;
                }

                if (commonService.deviceFunctionIsEmpty(deviceId)) {
                    LOGGER.debug("***** processDeviceStatusChange, deviceId={} property is empty, end report state. userId={}, smartType={}", deviceId, userId, smartType);
                    return ;
                }

                if (smartType == SmartHomeConstants.ALEXA) {
                    // 处理 alexa 设备状态通知
                    handleAlexa(tenantId, userId, changeAttrs, notifyVo, onlineStatus, smartToken);
                } else if (smartType == SmartHomeConstants.GOOGLE_HOME) {
                    // 处理 googleHome 设备状态通知
                    handleGoogleHome(tenantId, userId, changeAttrs, notifyVo, onlineStatus, smartToken);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 处理 googleHome 设备状态通知
    private void handleGoogleHome(Long tenantId, Long userId, Map<String, Object> changeAttrs, SmartVoiceBoxNotifyVo notifyVo,
                                  Integer onlineStatus, SmartTokenResp smartToken) {
        try {
            if (changeAttrs != null && changeAttrs.size() > 0) {
                LOGGER.debug("***** handleGoogleHome, 处理 googleHome 设备状态通知 start. changeAttrs={}", changeAttrs);

                Map<String, Object> allAttrs = Maps.newHashMap();

                // 获取状态信息
                Map<String, Object> attrs = commonService.getDeviceStatus(tenantId, notifyVo.getDeviceId());
                if (attrs != null) {
                    allAttrs.putAll(attrs);
                }

                allAttrs.putAll(changeAttrs);

                // 上报
                ReportStateVo reportStateVo = new ReportStateVo();
                reportStateVo.setDeviceId(notifyVo.getDeviceId());
                reportStateVo.setAttrMap(allAttrs);

                List<ReportStateVo> reportStateVoList = Lists.newArrayList();
                reportStateVoList.add(reportStateVo);
                commonService.handlerGoogleHomeReportState(reportStateVoList, notifyVo.getUserUUID(), null);

                LOGGER.debug("***** handleGoogleHome, 处理 googleHome 设备状态通知 end.");
            } else if (onlineStatus != null) {
                // 离线、在线
                LOGGER.debug("***** handleGoogleHome(), 处理 googleHome 设备状态通知 start. onlineStatus={}", onlineStatus);

                Map<String, Object> allAttrs = Maps.newHashMap();
                // 获取状态信息
                Map<String, Object> attrs = commonService.getDeviceStatus(tenantId, notifyVo.getDeviceId());
                if (attrs != null) {
                    allAttrs.putAll(attrs);
                }

                if (onlineStatus == 0) {
                    allAttrs.put("online", false);
                } else if (onlineStatus == 1) {
                    allAttrs.put("online", true);
                }

                // 上报
                ReportStateVo reportStateVo = new ReportStateVo();
                reportStateVo.setDeviceId(notifyVo.getDeviceId());
                reportStateVo.setAttrMap(allAttrs);

                List<ReportStateVo> reportStateVoList = Lists.newArrayList();
                reportStateVoList.add(reportStateVo);
                commonService.handlerGoogleHomeReportState(reportStateVoList, notifyVo.getUserUUID(), null);

                LOGGER.debug("***** handleGoogleHome(), 处理 googleHome 设备状态通知 end.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 处理 alexa 设备状态通知
    private void handleAlexa(Long tenantId, Long userId, Map<String, Object> changeAttrs, SmartVoiceBoxNotifyVo notifyVo,
                             Integer onlineStatus, SmartTokenResp smartToken) {
        LOGGER.debug("***** handleAlexa, 处理 alexa 设备状态通知 start.");

        AlexaChangeStateReport stateReport = new AlexaChangeStateReport();
        stateReport.setEndPointId(notifyVo.getDeviceId());
        stateReport.setCause("APP_INTERACTION");
        stateReport.setToken(smartToken.getAccessToken());

        try {
            if (changeAttrs != null && changeAttrs.size() > 0) {
                LOGGER.debug("***** handleAlexa, changeAttrs={}", changeAttrs);

                JSONObject connectivity = new JSONObject();
                connectivity.put("value", "OK");
                stateReport.addState("Alexa.EndpointHealth", "connectivity", connectivity);

                // 改变的值要上报
                this.handlerAlexaMapData(changeAttrs, null, stateReport, true);

                // 状态改变
                Map<String, Object> attrs = commonService.getDeviceStatus(tenantId, notifyVo.getDeviceId());
                if (attrs != null) {
                    // 原有未改变的值也要上报
                    this.handlerAlexaMapData(attrs, changeAttrs, stateReport, false);
                }

            } else if (onlineStatus != null) {
                // 离线、在线
                JSONObject connectivity = new JSONObject();
                connectivity.put("value", "OK");

                if (onlineStatus == 0) {
                    connectivity.put("value", "UNREACHABLE");
                } else if (onlineStatus == 1) {
                    connectivity.put("value", "OK");
                }

                stateReport.addChangeState("Alexa.EndpointHealth", "connectivity", connectivity);
            }

            String jsonResult = JSONObject.toJSONString(stateReport);
            LOGGER.debug("***** handleAlexa, stateReport.json={}", jsonResult);
            CommonResponse commonResponse = ChangeStateUtil.send(smartToken.getAccessToken(), jsonResult);
            commonService.dealAlexaReportResult(userId, commonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("***** handleAlexa, 处理 alexa 设备状态失败, 因为:{}", e.getMessage());
        }
    }

    private void handlerAlexaMapData(Map<String, Object> m, Map<String, Object> changeAttrs, AlexaChangeStateReport s, boolean change) {
        m.forEach((key, val) -> {
            if (changeAttrs == null || !changeAttrs.containsKey(key)) {
                YunKeyValue ykv = new YunKeyValue();
                ykv.setKey(key);
                ykv.setValue(val);
                LOGGER.debug("***** handlerAlexaMapData, change={}, yun_kv={}", change, JSON.toJSONString(ykv));

                int n = convertor.indexOf(ykv);
                LOGGER.debug("***** handlerAlexaMapData, change={}, n={}", change, n);
                if (n != -1) {
                    // 云端 --> robot
                    KeyValue kv = convertor.get(n).toCommonKV(ykv);
                    LOGGER.debug("***** handlerAlexaMapData, change={}, robot_kv={}", change, JSON.toJSONString(kv));

                    if (kv != null) {
                        // robot --> 第三方
                        JSONObject self = alexaTransfor.getSelfKeyVal(kv);
                        LOGGER.debug("***** handlerAlexaMapData, change={}, alexa_kv={}", change, self);
                        if (!self.isEmpty()) {
                            if (change) {
                                s.addChangeState(self.getString("namespace"), self.getString("name"), self.get("value"));
                            } else {
                                s.addState(self.getString("namespace"), self.getString("name"), self.get("value"));
                            }
                        }
                    }
                }
            }
        });
    }
}
