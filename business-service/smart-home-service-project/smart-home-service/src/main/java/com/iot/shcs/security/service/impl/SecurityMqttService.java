package com.iot.shcs.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.MD5SaltUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.device.api.UserDeviceCoreApi;
import com.iot.control.device.vo.req.GetUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.space.api.SpaceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.exception.DeviceExceptionEnum;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.ifttt.api.IftttAccessApi;
import com.iot.ifttt.common.ServiceEnum;
import com.iot.ifttt.vo.CheckNotifyReq;
import com.iot.locale.LocaleMessageSourceService;
import com.iot.message.api.MessageApi;
import com.iot.message.enums.MessageTempType;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.common.exception.BusinessExceptionEnum;
import com.iot.shcs.common.util.DeviceUtils;
import com.iot.shcs.common.util.MQTTUtils;
import com.iot.shcs.contants.AppPushMessageKey;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.device.service.impl.DeviceService;
import com.iot.shcs.device.utils.DeviceCoreUtils;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.helper.DispatcherRouteHelper;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.shcs.security.constant.ArmModeEnum;
import com.iot.shcs.security.constant.SecurityConstants;
import com.iot.shcs.security.domain.Security;
import com.iot.shcs.security.exception.SecurityExceptionEnum;
import com.iot.shcs.security.queue.bean.GetStatusRespMessage;
import com.iot.shcs.security.queue.bean.SetArmModeRespMessage;
import com.iot.shcs.security.queue.sender.GetStatusRespSender;
import com.iot.shcs.security.queue.sender.SetArmModeRespSender;
import com.iot.shcs.security.service.SecurityRuleService;
import com.iot.shcs.security.service.SecurityService;
import com.iot.shcs.security.vo.SecurityRule;
import com.iot.shcs.space.service.impl.SpaceServiceImpl;
import com.iot.shcs.space.vo.SpaceDeviceResp;
import com.iot.shcs.space.vo.SpaceResp;
import com.iot.shcs.widget.service.UserWidgetService;
import com.iot.tenant.api.UserVirtualOrgApi;
import com.iot.tenant.vo.resp.UserDefaultOrgInfoResp;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/5/9 17:58
 * 修改人:
 * 修改时间：
 */

@Service("security")
public class SecurityMqttService implements CallBackProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SecurityMqttService.class);
    private static final int QOS = 1;
    private final String HOME_ID = "homeId";
    private final String SECURITY_TYPE = "securityType";
    private final String SECURITY_ID = "securityId";
    @Autowired
    private SecurityService securityService;
    @Autowired
    private MqttSdkService mqttSdkService;
    @Autowired
    private SpaceServiceImpl spaceService;
    @Autowired
    private ActivityRecordApi activityRecordApi;
    @Autowired
    private DeviceCoreService deviceCoreService;
    @Autowired
    private ProductCoreApi productCoreApi;
    @Autowired
    private SpaceApi spaceApi;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserApi userApi;
    @Autowired
    private UserVirtualOrgApi userVirtualOrgApi;
    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;
    @Autowired
    private MessageApi messageApi;
    @Autowired
    private SecurityRuleService securityRuleService;
    @Autowired
    private IftttAccessApi iftttAccessApi;
    @Autowired
    private DeviceCoreApi deviceCoreApi;
    @Autowired
    private UserDeviceCoreApi userDeviceCoreApi;
    @Autowired
    private UserWidgetService userWidgetService;

    //mqtt消息转发
    @Override
    public void onMessage(MqttMsg mqttMsg) {
        try {
            if (mqttMsg == null) {
                return;
            }
            DispatcherRouteHelper.dispatch(this, mqttMsg);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 布置安防请求(把消息转发给 device)
     * <p>
     * iot/v1/s/[userId]/security/setArmModeReq     (app --> cloud)
     * iot/v1/c/[devId]/security/setArmModeReq      (cloud --> dev)
     *
     * @param mqttMsg
     * @param topic
     */
    //此方法云端只做转发工作
    public void setArmModeReq(MqttMsg mqttMsg, String topic) {
        logger.info("setArmModeReq({}, {})", mqttMsg, topic);
        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        List<String> deviceUuidList = null;
        String userUuid = MQTTUtils.parseReqTopic(topic);
        MqttMsgAck ack = MqttMsgAck.successAck();
        try {
            Long homeId = MQTTUtils.getMustLong(payload, HOME_ID);
            deviceUuidList = getDirectDeviceUuidBySpaceId(homeId);
        } catch (BusinessException e) {
            ack.setCode(e.getCode());
            ack.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("setArmModeReq-system-error", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            mqttMsg.setAck(ack);
            if (ack.getCode() == MqttMsgAck.SUCCESS) {
                //success notify device
                mqttMsg.setAck(null);
                deviceUuidList.forEach(deviceUuid -> {
                    mqttMsg.setTopic(buildClientSecurityTopic(MQTTUtils.getMethodFromTopic(topic), deviceUuid));
                    mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
                });
            } else {
                //error notify user
                mqttMsg.setTopic(Constants.TOPIC_SERVER_PREFIX + userUuid + "/security/setArmModeResp");
                mqttMsg.setMethod("setArmModeResp");
                mqttMsg.setAck(ack);
                //mq 再次投递发送
                ApplicationContextHelper.getBean(SetArmModeRespSender.class).send(SetArmModeRespMessage.builder()
                        .mqttMsg(mqttMsg).topic(topic).userUuid(userUuid).erroCode(1000).build());
            }
        }
    }

    /**
     * 布置安防响应(把消息转发给 app)
     * <p>
     * iot/v1/s/[devId]/security/setArmModeResp    (dev --> cloud)
     * iot/v1/c/[userId]/security/setArmModeResp   (cloud --> app)
     *
     * @param mqttMsg
     * @param topic
     */

    //此方法云端只做转发
    public void setArmModeResp(MqttMsg mqttMsg, String topic) {
        logger.info("setArmModeResp({}, {})", mqttMsg, topic);
        String deviceUuid = MQTTUtils.parseReqTopic(topic);
        FetchUserResp fetchUserResp = getUserByDeviceUuid(deviceUuid);
        String userUuid = fetchUserResp.getUuid();

        //mq 再次投递发送
        ApplicationContextHelper.getBean(SetArmModeRespSender.class).send(SetArmModeRespMessage.builder()
                .mqttMsg(mqttMsg).topic(topic).deviceUuid(deviceUuid).userUuid(userUuid).build());

    }

    /**
     * 忽略未就绪设备请求(把消息转发给 device)
     * <p>
     * iot/v1/s/[userId]/security/bypassReq     (app --> cloud)
     * iot/v1/c/[devId]/security/bypassReq      (cloud --> dev)
     *
     * @param mqttMsg
     * @param topic
     */
    //此方法云端只做转发
    public void bypassReq(MqttMsg mqttMsg, String topic) {
        logger.info("bypassReq({}, {})", mqttMsg, topic);
        MqttMsgAck ack = MqttMsgAck.successAck();
        String userUuid = null;
        List<String> deviceUuidList = null;
        try {
            userUuid = MQTTUtils.parseReqTopic(topic);
            Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
            Long homeId = MQTTUtils.getMustLong(payload, HOME_ID);
            deviceUuidList = getDirectDeviceUuidBySpaceId(homeId);
        } catch (BusinessException e) {
            ack.setCode(e.getCode());
            ack.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("bypassReq-system-error", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            mqttMsg.setAck(ack);
            if (ack.getCode() == MqttMsgAck.SUCCESS) {
                //success notify device
                mqttMsg.setAck(null);

                deviceUuidList.forEach(deviceUuid -> {
                    mqttMsg.setTopic(buildClientSecurityTopic(MQTTUtils.getMethodFromTopic(topic), deviceUuid));
                    mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
                });
            } else {
                //error notify user
                mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + userUuid + "/security/bypassResp");
                mqttMsg.setMethod("bypassResp");
                mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
            }
        }
    }

    /**
     * 忽略未就绪设备响应(把消息转发给 app)
     * <p>
     * iot/v1/s/[devId]/security/bypassResp      (dev --> cloud)
     * iot/v1/c/[userId]/security/bypassResp     (cloud --> app)
     *
     * @param mqttMsg
     * @param topic
     */
    //此方法云端只做转发
    public void bypassResp(MqttMsg mqttMsg, String topic) {
        logger.info("bypassResp({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();

        String deviceUuid = MQTTUtils.parseReqTopic(topic);

        FetchUserResp fetchUserResp = getUserByDeviceUuid(deviceUuid);
        String userUuid = fetchUserResp.getUuid();

        try {
            if (mqttMsg.getAck().getCode() == 200) {
                String result = (String) payload.get("result");
                if ("OK".equals(result)) {
                    // ********** 云端不需要操作，以 8.5安防状态更新通知 为准(只在这个里面 执行“布置安防模式”) **********
                }
            } else {
                logger.error("*****bypassResp()  error! --> errorCode={}, errorMsg={}", mqttMsg.getAck().getCode(), mqttMsg.getAck().getDesc());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mqttMsg.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey()));
        } finally {
            mqttMsg.setTopic(buildClientSecurityTopic(MQTTUtils.getMethodFromTopic(topic), userUuid));
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }

    /**
     * 安防状态更新通知【安防状态切换时 就会上报 这个通知(如 从stay模式 切换到 awaya模式)】
     * <p>
     * iot/v1/cb/[devId]/security/statusChangedNotif     (device 广播给--> cloud、app)
     *
     * @param mqttMsg
     * @param topic
     */
    public void statusChangedNotif(MqttMsg mqttMsg, String topic) {

        logger.info("statusChangedNotif({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        //通过topic获取deviceUuid
        String deviceUuid = MQTTUtils.parseReqTopic(topic);
        FetchUserResp fetchUserResp = getUserByDeviceUuid(deviceUuid);
        Long tenantId=fetchUserResp.getTenantId();
        logger.debug("deviceUuid and tenantId({}, {})",deviceUuid,tenantId);
        try {
            if (mqttMsg.getAck().getCode() == 200) {
                // 获取用户家id
                Long spaceId = getSpaceIdByDeviceUuid(deviceUuid,tenantId);
                //通过spaceId找到对应的security_id
                Long securityId=getSecurityIdBySpaceId(spaceId,tenantId);
                int remaining = 0;

                // 安防状态, 0:撤防,1:在家布防,3:离家布防,4:离家布防延迟,5:在家布防延迟,6:未就绪
                //11:在家布防延时报警；12：离家布防延时报警
                String status = String.valueOf(payload.get("status"));
                logger.info("*****statusChangedNotif() 获取到的 status={},获得的spaceId={}", status,spaceId);

                if ("4".equals(status) || "5".equals(status)) {
                    // 布防延迟剩余时间(秒), 当status=4或5时, remaining有效.其他状态,可以不回该字段
                    remaining = MQTTUtils.getMustInteger(payload, "remaining");
                    logger.info("*****statusChangedNotif() 获取到的 remaining={}",remaining);
                }

                Integer delay=0;
                if("11".equals(status) || "12".equals(status)){

                    delay=MQTTUtils.getMustInteger(payload, "delay");

                    //延时报警时候发送推送
                    String userUuid=fetchUserResp.getUuid();
                    GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(deviceUuid);

                    String appPushMessageKey= AppPushMessageKey.SECURITY_TRIGGER_DELAY;

                    String path = "alarm";
                    Map<String, Object> dataMap = Maps.newHashMap();
                    dataMap.put("deviceId", deviceResp.getUuid());
                    dataMap.put("path", path);
                    Map<String, Object> payloadMap = Maps.newHashMap();
                    payloadMap.put("data", dataMap);

                    // 封装推送至app消息内容
                    Map<String, String> noticeMap = Maps.newHashMap();
                    noticeMap.put("templateId", MessageTempType.IOS00001.getTempId());
                    noticeMap.put("message", localeMessageSourceService.getMessage(appPushMessageKey, new Object[]{deviceResp.getName(),delay.toString()}));
                    noticeMap.put("customDictionary", JsonUtil.toJson(payloadMap));
                    //推送消息
                    messageApi.systemSinglePush(fetchUserResp.getUuid(), noticeMap, 10);
                    logger.debug("*****statusChangedNotif() 获取到的deviceName={}，获取到的 delay={},获取的用户uuid={}",deviceResp.getName(),delay,userUuid);
                }

                //1.设置security表中的arm_mode字段
                //2.将security_rule表中的enable字段
                setArmMode(spaceId,tenantId, fetchUserResp.getId(),status,securityId);

            } else {
                logger.error("*****statusChangedNotif() error! --> errorCode={}, errorMsg={}", mqttMsg.getAck().getCode(), mqttMsg.getAck().getDesc());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安防运行状态通知【安防里的 子设备触发 就会上报 这个通知(如 门磁打开触发 报警) 】
     * <p>
     * iot/v1/cb/[devId]/security/stateChangedNotif        (device 广播给--> cloud、app)
     *
     * @param mqttMsg
     * @param topic
     */
    //云端只放到活动日志中
    public void stateChangedNotif(MqttMsg mqttMsg, String topic) {
        logger.info("stateChangedNotif({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();

        String deviceUuid = MQTTUtils.parseReqTopic(topic);

        try {
            if (mqttMsg.getAck().getCode() == 200) {

                // 安防运行状态,
                // 联动运行状态, 0:满足条件联动任务开始执行, 1:联动任务执行结束(暂不实现)
                String state = String.valueOf(payload.get("state"));
                String type = String.valueOf(payload.get("type"));
                Long securityId = MQTTUtils.getMustLong(payload, SECURITY_ID);

                List<Map<String, Object>> triggerList = (List<Map<String, Object>>) payload.get("trigger");
                if (CollectionUtils.isNotEmpty(triggerList)) {
                    FetchUserResp fetchUserResp = getUserByDeviceUuid(deviceUuid);
                    StringBuilder deviceNames = new StringBuilder();
                    List<String> devIds = Lists.newArrayList();
                    String tamperDeviceName=null;
                    String path = "alarm";
                    String appPushMessageKey = AppPushMessageKey.SECURITY_TRIGGER;
                    List<ActivityRecordReq> list = new ArrayList<>();
                    for (Map<String, Object> map : triggerList) {
                        logger.info("*****stateChangedNotif() 获取到的 state={}, securityId={}, map={}", state, securityId, map);

                        if (MapUtils.isNotEmpty(map)) {
                            String objId = (String) map.get("devId");
                            String trigType = (String) map.get("trigType");

                            String icon = null;
                            String activity = null;

                            if ("dev".equals(trigType) || "device".equals(trigType)) {
                                GetDeviceInfoRespVo subDevice = deviceCoreService.getDeviceInfoByDeviceId(objId);
                                logger.info("*****stateChangedNotif, subDevice={}", JSON.toJSONString(subDevice));

                                icon = subDevice.getIcon();
                                String deviceName = subDevice.getName();
                                String attrName = (String) map.get("attr");
                                String eventName=(String) map.get("event");
                                String attrValue = String.valueOf(map.get("value"));
                                attrName= StringUtil.isEmpty(attrName)?eventName:attrName;
                                activity = DeviceUtils.changeValue(deviceName, attrName, attrValue);

                                // 防拆报警处理
                                if ("tamper".equalsIgnoreCase(attrName)) {
                                    if ("1".equals(attrValue)) {
                                        path = "tamper";
                                        appPushMessageKey = AppPushMessageKey.ANTI_TAMPER_TRIGGER;
                                        tamperDeviceName=deviceName;
                                    }
                                }

                                devIds.add(objId);
                                if (deviceNames.length() == 0) {
                                    deviceNames.append(deviceName);
                                } else {
                                    deviceNames.append(" and " + deviceName);
                                }
                            }else if ("app".equals(trigType)) {
                                icon = "panic";
                                activity = "SOS mode is enabled";
                            }
                            //如果是防拆报警就不加入活动日志
                            if(StringUtil.isEmpty(activity)){
                                continue;
                            }
                            ActivityRecordReq activityRecordReq = new ActivityRecordReq();
                            activityRecordReq.setCreateBy(fetchUserResp.getId());
                            activityRecordReq.setType(Constants.ACTIVITY_RECORD_SECURITY);
                            activityRecordReq.setIcon(icon);
                            activityRecordReq.setActivity(activity);
                            activityRecordReq.setForeignId(objId);
                            activityRecordReq.setTime(new Date());
                            activityRecordReq.setDelFlag(0);
                            activityRecordReq.setTenantId(SaaSContextHolder.currentTenantId());
                            logger.info("*****保存安防日志 activityRecordReq jsonString={}", JSON.toJSON(activityRecordReq));
                            list.add(activityRecordReq);
                        }
                    }
                    activityRecordApi.saveActivityRecord(list);

                    logger.info("**** stateChangedNotif, appPushMessageKey={}, deviceNames={}, devIds={}, type={}, path={}", appPushMessageKey, deviceNames, devIds, type, path);
                    if (deviceNames.length() > 0) {
                        Map<String, Object> dataMap = Maps.newHashMap();
                        dataMap.put("deviceId", devIds);
                        dataMap.put("type", type);
                        dataMap.put("path", path);
                        if(StringUtil.isNotEmpty(tamperDeviceName)){
                            dataMap.put("deviceName",tamperDeviceName);
                        }
                        Map<String, Object> payloadMap = Maps.newHashMap();
                        payloadMap.put("data", dataMap);

                        // 封装推送至app消息内容
                        Map<String, String> noticeMap = Maps.newHashMap();
                        noticeMap.put("templateId", MessageTempType.IOS00001.getTempId());
                        noticeMap.put("message", localeMessageSourceService.getMessage(appPushMessageKey, new Object[]{deviceNames.toString()}));
                        noticeMap.put("customDictionary", JsonUtil.toJson(payloadMap));
                        messageApi.systemSinglePush(fetchUserResp.getUuid(), noticeMap, 10);
                    }
                }
            } else {
                logger.error("*****stateChangedNotif() error! --> errorCode={}, errorMsg={}", mqttMsg.getAck().getCode(), mqttMsg.getAck().getDesc());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //报警事件 ifttt.com 检测
            checkAlarm(deviceUuid);
        }
    }

    /**
     * 检测安防报警
     *
     * @param deviceUuid
     */
    private void checkAlarm(String deviceUuid) {
        //logger.debug("===checkAlarm 检测安防报警,deviceUuid:"+deviceUuid);
        try{
            Long tenantId = getTenantId(deviceUuid);
            //logger.debug("===checkAlarm 租户主键："+tenantId);
            if (tenantId != null) {
                //获取userId
                Long userId = null;
                GetUserDeviceInfoReq req = new GetUserDeviceInfoReq();
                List<String> deviceIds = Lists.newArrayList();
                deviceIds.add(deviceUuid);
                req.setDeviceIds(deviceIds);
                req.setTenantId(tenantId);
                List<ListUserDeviceInfoRespVo> deviceInfoRespVos = userDeviceCoreApi.listBatchUserDevice(req);
                if (CollectionUtils.isNotEmpty(deviceInfoRespVos)) {
                    ListUserDeviceInfoRespVo deviceInfoRespVo = deviceInfoRespVos.get(0);
                    userId = deviceInfoRespVo.getUserId();
                }

                //logger.debug("===checkAlarm 用户主键："+userId+"/");
                //用户存在
                if (userId != null) {
                    CheckNotifyReq checkNotifyReq = new CheckNotifyReq();
                    checkNotifyReq.setType(ServiceEnum.SECURITY.getCode());
                    Map<String, Object> fields = Maps.newHashMap();
                    fields.put("userId", userId.toString());
                    checkNotifyReq.setFields(fields);
                    iftttAccessApi.checkNotify(checkNotifyReq);
                    //logger.debug("===checkAlarm 发送安防报警检测...");
                }
            }
        }catch (Exception e){
            logger.error("发生安防报警通知失败",e);
        }
    }

    /**
     * 获取租户主键
     *
     * @param subDevId
     * @return
     */
    private Long getTenantId(String subDevId) {
        Long tenantId;
        GetDeviceInfoRespVo subDeviceInfo = deviceCoreApi.get(subDevId);
        if (StringUtils.isNotEmpty(subDeviceInfo.getParentId())) {
            GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(subDeviceInfo.getParentId());
            tenantId = deviceInfo.getTenantId();
        } else {
            tenantId = subDeviceInfo.getTenantId();
        }
        return tenantId;
    }

    /**
     * 获取安防状态请求(把消息转发给 device)(需要到设备上去同步，设备上的倒计时信息比较准确。)
     * <p>
     * iot/v1/s/[userId]/security/getStatusReq   (app --> cloud)
     * iot/v1/c/[devId]/security/getStatusReq    (cloud --> dev)
     *
     * @param mqttMsg
     * @param topic
     */
    //云端只做转发
    public void getStatusReq(MqttMsg mqttMsg, String topic) {
        logger.info("getStatusReq({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        List<String> deviceUuidList = null;
        String userUuid = MQTTUtils.parseReqTopic(topic);
        MqttMsgAck ack = MqttMsgAck.successAck();

        try {
            Long homeId = MQTTUtils.getMustLong(payload, HOME_ID);
            deviceUuidList = getDirectDeviceUuidBySpaceId(homeId);
        } catch (BusinessException e) {
            ack.setCode(e.getCode());
            ack.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("getStatusReq-system-error", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            mqttMsg.setAck(ack);
            if (ack.getCode() == MqttMsgAck.SUCCESS) {
                //success notify device
                mqttMsg.setAck(null);
                deviceUuidList.forEach(deviceUuid -> {
                    mqttMsg.setTopic(buildClientSecurityTopic(MQTTUtils.getMethodFromTopic(topic), deviceUuid));
                    mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
                });
            } else {
                //error notify user
//                mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + userUuid + "/security/getStatusResp");
//                mqttMsg.setMethod("getStatusResp");
//                mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
                //error notify user
                mqttMsg.setTopic(Constants.TOPIC_SERVER_PREFIX + userUuid + "/security/getStatusResp");
                mqttMsg.setMethod("getStatusResp");
                mqttMsg.setAck(ack);
                //mq 再次投递发送
                ApplicationContextHelper.getBean(GetStatusRespSender.class).send(GetStatusRespMessage.builder()
                        .mqttMsg(mqttMsg).topic(topic).userUuid(userUuid).erroCode(1000).build());
            }
        }
    }

    /**
     * 获取安防状态响应(把消息转发给 app)(需要到设备上去同步，设备上的倒计时信息比较准确。)
     * <p>
     * iot/v1/s/[devId]/security/getStatusResp    (dev --> cloud)
     * iot/v1/c/[userId]/security/getStatusResp   (cloud --> app)
     *
     * @param mqttMsg
     * @param topic
     */
    //云端转发
    public void getStatusResp(MqttMsg mqttMsg, String topic) {

        String deviceUuid = MQTTUtils.parseReqTopic(topic);
        FetchUserResp fetchUserResp = getUserByDeviceUuid(deviceUuid);
        String userUuid = fetchUserResp.getUuid();

        //mq 再次投递发送
        ApplicationContextHelper.getBean(GetStatusRespSender.class).send(GetStatusRespMessage.builder()
               .mqttMsg(mqttMsg).topic(topic).deviceUuid(deviceUuid).userUuid(userUuid).build());

    }

    /**
     * 设置安防密码请求
     * <p>
     * iot/v1/s/[userId]/security/setSecurityPasswdReq      (app --> cloud)
     *
     * @param mqttMsg
     * @param topic
     */
    //云端转发
    public void setSecurityPasswdReq(MqttMsg mqttMsg, String topic) {
        logger.info("setSecurityPasswdReq({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();

        String userUuid = MQTTUtils.parseReqTopic(topic);

        // 家id
        Long homeId = Long.parseLong(String.valueOf(payload.get(HOME_ID)));
        // 旧的安防密码, 如果初始注册没有密码，可以填空
        String oldPasswd = (String) payload.get("oldPasswd");
        // 新的安防密码
        String newPasswd = (String) payload.get("newPasswd");

        MqttMsgAck ack = MqttMsgAck.successAck();
        Map<String, Object> returnPayload = Maps.newHashMap();

        try {
            FetchUserResp fetchUserResp = userApi.getUserByUuid(userUuid);
            Long tenantId = fetchUserResp.getTenantId();
            Security security = securityService.getBySpaceId(homeId,tenantId);
            if (StringUtils.isBlank(oldPasswd)) {
                // 初始注册
                if (security != null) {
                    ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, localeMessageSourceService.getMessage(SecurityExceptionEnum.INPUT_OLD_SECURITY_PASSWORD.getMessageKey()));
                    return;
                }

                // 创建安防记录(保存安防密码)
                security = securityService.createSecurity(tenantId, fetchUserResp.getId(), homeId, newPasswd);
            } else {
                // 修改安防密码
                if (security == null) {
                    throw new BusinessException(SecurityExceptionEnum.SECURITY_NOT_CREATED);
                }

                if (!security.getPassword().equals(oldPasswd)) {
                    ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, localeMessageSourceService.getMessage(SecurityExceptionEnum.OLD_SECURITY_PASSWORD_IS_INCORRECT.getMessageKey()));
                    return;
                }

                // 更新 安防密码
                securityService.updatePasswordById(security, fetchUserResp.getId(), newPasswd);
            }

            returnPayload.put("result", 0);

            // 通知网关 安防密码被修改
            setSecurityPasswdNotif(homeId);

        } catch (BusinessException e) {
            mqttMsg.setAck(MqttMsgAck.failureAck(e.getCode(), localeMessageSourceService.getMessage(e.getMessage())));
        } catch (Exception e) {
            e.printStackTrace();
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            // 设置安防密码响应
            // iot/v1/c/[userId]/security/setSecurityPasswdResp       (cloud --> app)
            mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + userUuid + "/security/setSecurityPasswdResp");
            mqttMsg.setMethod("setSecurityPasswdResp");
            mqttMsg.setAck(ack);
            mqttMsg.setPayload(returnPayload);
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }

    /**
     * 重置安防密码请求(忘记安防密码后，可以输入用户密码来重置安防密码)
     * <p>
     * iot/v1/s/[userId]/security/resetSecurityPasswdReq      (app --> cloud)
     *
     * @param mqttMsg
     * @param topic
     */
//云端只修改security表
    public void resetSecurityPasswdReq(MqttMsg mqttMsg, String topic) {
        logger.info("resetSecurityPasswdReq({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();

        String userUuid = MQTTUtils.parseReqTopic(topic);

        // 家id
        Long homeId = Long.parseLong(String.valueOf(payload.get(HOME_ID)));
        // 用户登录密码
        String userPasswd = (String) payload.get("userPasswd");
        // 新的安防密码
        String newPasswd = (String) payload.get("newPasswd");

        MqttMsgAck ack = MqttMsgAck.successAck();
        Map<String, Object> returnPayload = Maps.newHashMap();

        try {
            FetchUserResp fetchUserResp = userApi.getUserByUuid(userUuid);
            Security security = securityService.getBySpaceId(homeId,fetchUserResp.getTenantId());

            if (MD5SaltUtil.verify(userPasswd, fetchUserResp.getPassword())){
                // 修改密码
                securityService.updatePasswordById(security, fetchUserResp.getId(), newPasswd);
                returnPayload.put("result", 0);

                // 通知网关 安防密码被修改
                setSecurityPasswdNotif(homeId);
            } else {
                ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, localeMessageSourceService.getMessage(SecurityExceptionEnum.USER_PASSWORD_IS_INCORRECT.getMessageKey()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            // 设置安防密码响应
            // iot/v1/c/[userId]/security/resetSecurityPasswdResp       (cloud --> app)
            mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + userUuid + "/security/resetSecurityPasswdResp");
            mqttMsg.setMethod("resetSecurityPasswdResp");
            mqttMsg.setPayload(returnPayload);
            mqttMsg.setAck(ack);
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }



    /**
     * 下发 安防密码给网关
     *
     * @param tenantId
     *
     * @param deviceUuid
     */
    //云端下发
    public void setSecurityPasswdNotif(Long tenantId, String deviceUuid) {
        if (StringUtils.isBlank(deviceUuid)) {
            logger.info("*****setSecurityPasswdNotif(String deviceUuid) 执行结束! 因为 deviceUuid is null");
            return;
        }

        try {
            Long spaceId = getSpaceIdByDeviceUuid(deviceUuid,tenantId);
            setSecurityPasswdNotif(deviceUuid, spaceId);
        } catch (Exception e) {
            logger.info("*****setSecurityPasswdNotif(String deviceUuid) 执行结束! 因为={}", e.getMessage());
        }
    }


    /**
     *  处理 安防恢复默认值
     */
    private Long dealSecurityReset(Long tenantId, String deviceId, Long productId) {

        logger.debug("***** dealSecurityReset(), deviceResp={}", deviceId);
        if (deviceId == null || productId == null) {
            return null;
        }
        GetProductInfoRespVo productResp = productCoreApi.getByProductId(productId);
        logger.debug("***** dealSecurityReset(), productResp={}", JSON.toJSON(productResp));
        if (productResp == null) {
            return null;
        }
        Long userId = null;
        try {
            if (DeviceCoreUtils.isGateWayProduct(productResp)) {

                userId = securityService.securityResetFactory(deviceId,tenantId);
                SecurityRule req = new SecurityRule();
                Long spaceId = getSpaceIdByDeviceUuid(deviceId, tenantId);
                Long securityId = getSecurityIdBySpaceId(spaceId, tenantId);
                if(securityId==null||tenantId==null){
                    return null;
                }
                req.setSecurityId(securityId);
                req.setTenantId(tenantId);
                logger.info("***** dealSecurityReset,userId={} spaceId={}, securityId={},tenantId={}!", userId,spaceId,securityId,tenantId);
                List<SecurityRule> securityRuleList = securityRuleService.list(req);
                if (CollectionUtils.isNotEmpty(securityRuleList)) {
                    for (SecurityRule securityRule : securityRuleList) {
                        securityRuleService.deleteByPrimaryKey(tenantId,securityRule.getSecurityId(), securityRule.getType());
                    }
                    logger.debug("***** deleteSecurityInfo(), userId={}, delete SecurityRule Success!", userId);
                }
                userWidgetService.deleteSecurityWidget(userId);
            }
        }catch (Exception e){
            logger.info("*****dealSecurityReset error 111{}", e.getMessage());
        }
        return userId;
    }




    /**
     *  删除安防信息
     */

    public void deleteSecurityInfo(Long tenantId, String deviceId, Long productId) {

        logger.info("***** deleteSecurityInfo(), userId={}, deleteSecurityRule={}", deviceId);
        // 安防恢复默认值
        this.dealSecurityReset(tenantId, deviceId, productId);

    }
    //删除子设备相关的安防规则，if中的设备信息
    public void deleteSubDevSecurityInfo(Long tenantId, String deviceId) {
        try {
            logger.info("***** deleteSubDevSecurityInfo, deleteSubDevSecurityInfo={}", deviceId);

            Long spaceId = getSpaceIdByDeviceUuid(deviceId, tenantId);
            String subDeviceUuid = deviceId;
            Long securityId = getSecurityIdBySpaceId(spaceId, tenantId);
            String stayType="stay";
            String awayType="away";
            logger.info("***** tenantId={},spaceId={},subDeviceUuid={},securityId={}", tenantId,spaceId,subDeviceUuid,securityId);
            SecurityRule stayRule = securityRuleService.selectBySecurityIdAndType(tenantId,securityId,stayType);
            if(stayRule!=null){
                deleteSubDevIfcondition(stayRule,subDeviceUuid);
            }
            SecurityRule awayRule=securityRuleService.selectBySecurityIdAndType(tenantId,securityId,awayType);
            if(awayRule!=null){
                deleteSubDevIfcondition(awayRule,subDeviceUuid);
            }
            logger.info("***** SecurityStayRule{},SecurityAwayRule",JSON.toJSONString(stayRule),JSON.toJSONString(awayRule));
        }catch (Exception e){
            logger.info("***** deleteSubDevSecurityInfo{}",e.getMessage());
        }
    }

    //删除子设备中的if条件
    public void deleteSubDevIfcondition(SecurityRule ruleResp ,String subDeviceUuid){
        Map<String,Object> ifMap=JSON.parseObject(ruleResp.getIfCondition(),Map.class);
        List<Map<String,String>> ifList=(List<Map<String,String>>)ifMap.get("trigger");
        List<Map<String,String>> list=new ArrayList<>();
        logger.info("***********the ifmap={} ,the ifList is={}",JSON.toJSONString(ifMap),JSON.toJSONString(ifList));
        int size=ifList.size();
       for(int i=0;i<size;i++){
           String devUuid=ifList.get(i).get("devId");
            //不是子设备时候添加到list中
           if(!devUuid.equals(subDeviceUuid)){
                list.add(ifList.get(i));
           }
       }
       ifMap.put("trigger",list);
       logger.info("***********delete the ifmap={} ,the list is={}",JSON.toJSONString(ifMap),JSON.toJSONString(list));
       ruleResp.setIfCondition(JSON.toJSONString(ifMap));
       securityRuleService.saveSecurityRule(ruleResp);

    }


    /**
     * 下发 安防密码给网关(通过 homeId --> deviceId)
     *
     * @param homeId
     */
    //云端只做转发
    public void setSecurityPasswdNotif(Long homeId) {
        if (homeId == null) {
            logger.info("*****setSecurityPasswdNotif(Long homeId) 执行结束! 因为 homeId is null");
            return;
        }
        try {
            List<String> deviceUuidList = getDirectDeviceUuidBySpaceId(homeId);
            deviceUuidList.forEach(deviceUuid -> {
                setSecurityPasswdNotif(deviceUuid, homeId);
            });
        } catch (Exception e) {
            logger.info("*****setSecurityPasswdNotif(Long homeId) 执行结束! 因为={}", e.getMessage());
        }
    }

    /**
     * 设置安防密码通知
     * 1）云端修改密码后，如果网关在线下发设置安防密码通知
     * 2）网关上线后，下发设置安防密码通知
     * 3）绑定网关后，下发设置安防密码通知
     * <p>
     * iot/v1/c/[devId]/security/setSecurityPasswdNotif        (cloud --> dev)
     */
    //只修改security
    private void setSecurityPasswdNotif(String directDeviceUuid, Long homeId) {

        try {
            if (StringUtils.isBlank(directDeviceUuid) || homeId == null) {
                logger.info("*****执行 安防密码通知 失败! 因为 directDeviceUuid={} 或者 homeId={} is null", directDeviceUuid, homeId);
                return;
            }

            GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(directDeviceUuid);
            logger.info("deviceResp={}", JSON.toJSON(deviceResp));
            if (deviceResp == null) {
                logger.info("*****setSecurityPasswdNotif() 执行结束! 因为 deviceResp或者deviceResp.getOnlineStatus() is null");
                return;
            }
            //非直连设备不做下发处理
            if (deviceResp.getIsDirectDevice() == null || deviceResp.getIsDirectDevice() == 0) {
                logger.info("*****setSecurityPasswdNotif() 非直连设备不做下发安防密码~~");
                return;
            }
            GetDeviceStatusInfoRespVo deviceStatusInfo = deviceCoreService.getDeviceStatusByDeviceId(deviceResp.getTenantId(), directDeviceUuid);
            if ("disconnected".equals(deviceStatusInfo.getOnlineStatus())) {
                logger.info("*****setSecurityPasswdNotif() 执行结束! 因为设备onlineStatus = disconnected");
                return;
            }
            Long tenantId=deviceResp.getTenantId();
            Security security = securityService.getBySpaceId(homeId,tenantId);
            if (security == null) {
                logger.info("*****setSecurityPasswdNotif() 执行结束! 因为安防记录 security is null");
                return;
            }

            MqttMsg mqttMsg = new MqttMsg();
            mqttMsg.setService("security");
            mqttMsg.setMethod("setSecurityPasswdNotif");
            mqttMsg.setSeq(System.currentTimeMillis() + "");
            mqttMsg.setSrcAddr(MQTTUtils.DEFAULT_CLOUD_SOURCE_ADDR);

            Map<String, Object> returnPayload = Maps.newHashMap();
            returnPayload.put("passwd", security.getPassword());
            mqttMsg.setPayload(returnPayload);

            mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + directDeviceUuid + "/security/setSecurityPasswdNotif");
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        } catch (Exception e) {
            logger.info("*****setSecurityPasswdNotif() 执行失败! 因为={}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 设置安防规则请求(把消息转发给 device)
     * <p>
     * iot/v1/s/[userId]/security/setSecurityRuleReq    (app --> cloud)
     * iot/v1/c/[devId]/security/setSecurityRuleReq     (cloud -->dev)
     *
     * @param mqttMsg
     * @param topic
     */
    public void setSecurityRuleReq(MqttMsg mqttMsg, String topic) {
        logger.info("setSecurityRuleReq({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();

        String userUuid = MQTTUtils.parseReqTopic(topic);

        MqttMsgAck ack = MqttMsgAck.successAck();

        // 网关
        List<String> deviceUuidList = Lists.newArrayList();

        try {
            //
            Long securityId = MQTTUtils.getMustLong(payload, SECURITY_ID);
            //通过securtiy id 中存了spaceId找到
            Security ruleResp = securityService.getSecurityById(securityId);
            logger.info("*****ruleResp json string={}", JSON.toJSON(ruleResp));
            if (ruleResp == null) {
                // 响应给app
                ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, localeMessageSourceService.getMessage(SecurityExceptionEnum.SECURITY_RULE_NOT_EXIST.getMessageKey()));
            } else {
                //通过spaceId找到对应的直连设备
                deviceUuidList = getDirectDeviceUuidBySpaceId(ruleResp.getSpaceId());
                logger.info("*****通过spaceId 获取 direct device, deviceUuidList={}", deviceUuidList);
                if (CollectionUtils.isEmpty(deviceUuidList)) {
                    // 响应给app
                    ack = MqttMsgAck.failureAck(SecurityExceptionEnum.UNBOUND_GATEWAY.getCode(), localeMessageSourceService.getMessage(SecurityExceptionEnum.UNBOUND_GATEWAY.getMessageKey()));
                }
            }
        } catch (BusinessException e) {
            ack = MqttMsgAck.failureAck(e.getCode(), localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("setSecurityRuleReq-system-error, {}", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            mqttMsg.setAck(ack);
            if (ack.getCode() == MqttMsgAck.SUCCESS) {
                //success notify device
                mqttMsg.setAck(null);
                deviceUuidList.forEach(deviceUuid -> {
                    mqttMsg.setTopic(buildClientSecurityTopic(MQTTUtils.getMethodFromTopic(topic), deviceUuid));
                    mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
                });
            } else {
                //error notify user
                mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + userUuid + "/security/setSecurityRuleResp");
                mqttMsg.setMethod("setSecurityRuleResp");
                mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
            }
        }
    }

    /**
     * 设置安防规则响应(把消息转发给 app)
     * <p>
     * iot/v1/s/[devId]/security/setSecurityRuleResp    (dev --> cloud)
     * iot/v1/c/[userId]/security/setSecurityRuleResp   (cloud --> app)
     *
     * @param mqttMsg
     * @param topic
     */
    public void setSecurityRuleResp(MqttMsg mqttMsg, String topic) {
        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();

        String deviceUuid = MQTTUtils.parseReqTopic(topic);

        FetchUserResp fetchUserResp = getUserByDeviceUuid(deviceUuid);
        String userUuid = fetchUserResp.getUuid();
        Long tenantId=fetchUserResp.getTenantId();

        try {
            if (mqttMsg.getAck().getCode() == 200) {

                //保存security_rule规则
               SecurityRule securityRule=securityRuleService.getRuleBean(mqttMsg);
               securityRule.setTenantId(tenantId);
               securityRuleService.saveSecurityRule(securityRule);
            } else {
                logger.error("*****setSecurityRuleResp() error! --> errorCode={}, errorMsg={}", mqttMsg.getAck().getCode(), mqttMsg.getAck().getDesc());
            }

        } catch (BusinessException e) {
            mqttMsg.setAck(MqttMsgAck.failureAck(e.getCode(), localeMessageSourceService.getMessage(e.getMessage())));
        } catch (Exception e) {
            e.printStackTrace();
            mqttMsg.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey()));
        } finally {
            mqttMsg.setTopic(buildClientSecurityTopic(MQTTUtils.getMethodFromTopic(topic), userUuid));
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }

    /**
     * 获取安防规则请求
     * <p>
     * iot/v1/s/[userId]/security/getSecurityRuleReq       (app --> cloud)
     *
     * @param mqttMsg
     * @param topic
     */
    public void getSecurityRuleReq(MqttMsg mqttMsg, String topic) {
        logger.info("*******getSecurityRuleReq****mqttMsg{},topic{}:",mqttMsg,topic);
        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();

        // 参数
        String userUuid = MQTTUtils.parseReqTopic(topic);
        String securityType = (String) payload.get(SECURITY_TYPE);

        // 返回的 数据封装
        MqttMsgAck ack = MqttMsgAck.successAck();
        Map<String, Object> returnPayload = Maps.newHashMap();

        try {
            Long homeId = MQTTUtils.getMustLong(payload, HOME_ID);
            FetchUserResp fetchUserResp = userApi.getUserByUuid(userUuid);
            Long tenantId = fetchUserResp.getTenantId();
            Security security=securityService.getBySpaceId(homeId,tenantId);
            if (security == null) {
                logger.error("*****getSecurityRuleReq() error! --> homeId={} 对应的 security安防记录未创建", homeId);
                ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, localeMessageSourceService.getMessage(SecurityExceptionEnum.SECURITY_NOT_CREATED.getMessageKey()));
                return;
            }
            logger.info("*******getSecurityRuleReq****security{}",security.toString());

            SecurityRule req=securityRuleService.selectBySecurityIdAndType(tenantId,security.getId(),securityType);

            Long ruleId = null;
            if (req == null) {
                logger.error("***** homeId={}, securityType={} 的 iftttRule还未创建", homeId, securityType);
                if (!ArmModeEnum.off.getArmMode().equals(securityType)) {
                    //保存security_rule表
                    //延时生效away 30s ；stay 0 s
                    req=new SecurityRule();
                    if (ArmModeEnum.away.getArmMode().equals(securityType)) {
                        req.setDefer(30);
                    } else {
                        req.setDefer(0);
                    }
                    //根据要求sos情况下delay字段应该为0
                    if(ArmModeEnum.panic.getArmMode().equals(securityType)){
                        req.setDelay(0);
                    }else {
                        req.setDelay(30);
                    }
                    req.setSecurityId(security.getId());
                    req.setType(securityType);
                    req.setEnabled(0);
                    req.setTenantId(tenantId);
                    req.setIfCondition(JSON.toJSONString(getDefaultIf()));
                    req.setThenCondition(JSON.toJSONString(getDefaultThen()));
                    ruleId = securityRuleService.saveSecurityRule(req);
                }
            } else {
                ruleId = req.getSecurityId();
            }

            returnPayload = buildSecurityRulePayload(tenantId,ruleId,securityType);

        } catch (BusinessException e) {
            ack = MqttMsgAck.failureAck(e.getCode(), localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            // 获取安防规则响应
            // iot/v1/c/[userId]/security/getSecurityRuleResp       (cloud --> app)
            mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + userUuid + "/security/getSecurityRuleResp");
            mqttMsg.setMethod("getSecurityRuleResp");
            mqttMsg.setAck(ack);
            mqttMsg.setPayload(returnPayload);
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }
    private Map<String,Object> getDefaultIf(){
        Map<String, Object> trigger = Maps.newHashMap();
        List<Map<String, Object>> tempList=new ArrayList<>();
        trigger.put("trigger",tempList);
        return trigger;
    }
    private List<Map<String, Object>> getDefaultThen() {
        Map<String, Object> attr = Maps.newHashMap();
        Map<String,Object>  tempMap=Maps.newHashMap();
        attr.put("warning_duration", 60);
        attr.put("warning_mode", "fire");
        attr.put("siren_level", "high");
        attr.put("strobe", "on");
        attr.put("strobe_level", "high");
        tempMap.put("attr",attr);
        tempMap.put("thenType", "dev");
        List<Map<String, Object>> thenList = Lists.newArrayList();
        thenList.add(tempMap);
        return thenList;
    }
    /**
     * 根据 ruleId 构建返回的 payload数据
     *
     * @param
     * @return
     */
    public Map<String, Object> buildSecurityRulePayload(Long tenantId,Long securityId,String type) {

        SecurityRule ruleResp = securityRuleService.selectBySecurityIdAndType(tenantId,securityId,type);

        if (ruleResp == null) {
            throw new BusinessException(SecurityExceptionEnum.SECURITY_RULE_NOT_EXIST);
        }

        Map<String, Object> returnPayload = Maps.newHashMap();
        Map<String, Object> ifMap = Maps.newHashMap();
        List<Map<String, Object>> triggerList = Lists.newArrayList();
        List<Map<String, Object>> thenList = Lists.newArrayList();

        Integer enabled = 0;
        if (ruleResp.getEnabled() != null) {
            if (SecurityConstants.STOP.equals(ruleResp.getEnabled())) {
                enabled = 0;
            } else if (SecurityConstants.RUNNING.equals(ruleResp.getEnabled())) {
                enabled = 1;
            }
        }

        Integer defer = 0;
        if (ruleResp.getDefer() != null) {
            defer = ruleResp.getDefer();
        }

        Integer delay = 0;
        if (ruleResp.getDelay() != null) {
            delay = ruleResp.getDelay();
        }

        if(StringUtil.isNotEmpty(ruleResp.getIfCondition()))
        {
            returnPayload.put("if", JSON.parseObject(ruleResp.getIfCondition(),Map.class));
        }else {
            String ifCondition=JSON.toJSONString(getDefaultIf());
            returnPayload.put("if", JSON.parseObject(ifCondition,Map.class));
        }

        if(StringUtil.isNotEmpty(ruleResp.getThenCondition())){
            returnPayload.put("then", JSON.parseObject(ruleResp.getThenCondition(),List.class));
        }else {
            String thenCondition=JSON.toJSONString(getDefaultThen());
            returnPayload.put("then", JSON.parseObject(thenCondition,List.class));
        }

        returnPayload.put(SECURITY_ID, securityId);
        returnPayload.put(SECURITY_TYPE, ruleResp.getType());
        returnPayload.put("enabled", enabled);
        returnPayload.put("defer", defer);
        returnPayload.put("delay",delay);
        logger.info("**************returnPayload is:"+returnPayload);

        return returnPayload;
    }

    /**
     * 布置安防模式
     *
     * @param spaceId
     * @param userId
     * @param status
     */
    private void setArmMode(Long spaceId, Long tenantId,Long userId, String status,Long securityId) {
        logger.info("*****homeId={}, userId={}, status={},tenantId", spaceId, userId, status,tenantId);

        // 布置安防模式
        // 安防状态, 0:撤防,1:在家布防,3:离家布防,4:离家布防延迟,5:在家布防延迟,6:未就绪
        //11在家延时报警，离家延时报警
        switch (status) {
            case "0":
                logger.info("*** 撤防 ***");
                securityService.updateArmModeBySpaceId(spaceId,tenantId, userId, ArmModeEnum.off);
                securityService.setSecurityStatus(SecurityConstants.SECURITY_TYPE_OFF, securityId,tenantId);
                createActivityRecord(spaceId, ArmModeEnum.Disarm, userId);
                break;
            case "1":
                logger.info("*** 在家布防 ***");
                securityService.updateArmModeBySpaceId(spaceId,tenantId, userId, ArmModeEnum.stay);
                securityService.setSecurityStatus(SecurityConstants.SECURITY_TYPE_STAY,securityId,tenantId);
                createActivityRecord(spaceId, ArmModeEnum.Stay, userId);
                break;
            case "3":
                logger.info("*** 离家布防 ***");
                securityService.updateArmModeBySpaceId(spaceId,tenantId, userId, ArmModeEnum.away);
                securityService.setSecurityStatus(SecurityConstants.SECURITY_TYPE_AWAY, securityId,tenantId);
                createActivityRecord(spaceId, ArmModeEnum.Away, userId);
                break;
//            case "4":
//                logger.info("*** 离家布防延迟 ***");
//                securityService.updateArmModeBySpaceId(spaceId,tenantId, userId, ArmModeEnum.away);
//                securityService.setSecurityStatus(SecurityConstants.SECURITY_TYPE_AWAY, securityId,tenantId);
//                //createActivityRecord(spaceId, ArmModeEnum.away, userId);
//                break;
//            case "5":
//                logger.info("*** 在家布防延迟 ***");
//                securityService.updateArmModeBySpaceId(spaceId,tenantId, userId, ArmModeEnum.stay);
//                securityService.setSecurityStatus(SecurityConstants.SECURITY_TYPE_STAY, securityId,tenantId);
//                //createActivityRecord(spaceId, ArmModeEnum.stay, userId);
//                break;
//            case "6":
//                logger.info("*** 未就绪 ***");
//                break;
//            case "11":
//                logger.info("**  在家布防延时报警 **");
//                securityService.updateArmModeBySpaceId(spaceId,tenantId, userId, ArmModeEnum.stay);
//                securityService.setSecurityStatus(SecurityConstants.SECURITY_TYPE_STAY,securityId,tenantId);
//                //createActivityRecord(spaceId, ArmModeEnum.stay, userId);
//                break;
//            case "12":
//                logger.info("**  离家布防延时报警 **");
//                securityService.updateArmModeBySpaceId(spaceId,tenantId, userId, ArmModeEnum.away);
//                securityService.setSecurityStatus(SecurityConstants.SECURITY_TYPE_AWAY, securityId,tenantId);
//                //createActivityRecord(spaceId, ArmModeEnum.away, userId);
//                break;
        }
    }

    /**
     * 获取 直接设备id
     *
     * @param spaceId
     * @return
     */
    private List<String> getDirectDeviceUuidBySpaceId(Long spaceId) {
        // 直连设备deviceId
        List<String> resultDeviceUuidList = spaceService.getDirectDeviceUuidBySpaceId(SaaSContextHolder.currentTenantId(), spaceId);
        if (resultDeviceUuidList.size() < 1) {
            logger.error("***** getDirectDeviceUuidBySpaceId() error! directDeviceUuid is null");
            throw new BusinessException(SecurityExceptionEnum.UNBOUND_GATEWAY);
        }

        return resultDeviceUuidList;
    }

    /**
     * 获取 spaceId
     *
     * @param deviceUuid
     * @return
     */
    public Long getSpaceIdByDeviceUuid(String deviceUuid,Long tenantId) {
        Long spaceId = null;
        SpaceDeviceResp spaceDeviceResp = spaceService.getSpaceDeviceByDeviceUuid(deviceUuid,tenantId);
        if (spaceDeviceResp != null) {
            spaceId=spaceDeviceResp.getSpaceId();
            SpaceResp spaceResp=spaceService.findSpaceInfoBySpaceId(tenantId,spaceId);
            logger.info("***** getSpaceIdByDeviceUuid={}",JSON.toJSONString(spaceResp));
            if(spaceResp!=null){
                Long parentId=spaceResp.getParentId();
                if(parentId==-1){
                    //等于-1说明这是家房间
                    spaceId=spaceResp.getId();
                }else {
                    spaceId=parentId;
                }
                logger.info("***** getSpaceIdByDeviceUuid deviceUuid={}, spaceId={},parentId={}",deviceUuid,spaceId,parentId);
            }
        }

        if (spaceId == null) {
            logger.error("***** 通过 deviceUuid 获取到的 spaceId is null");
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }

        return spaceId;
    }

    private Long getSecurityIdBySpaceId(Long spaceId,Long tenantId){
        Long securityId=null;
        Security security=securityService.getBySpaceId(spaceId,tenantId);
        if(security!=null){
            securityId=security.getId();
        }
        return securityId;
    }

    /**
     * 获取 用户
     *
     * @param deviceUuid
     * @return
     */
    private FetchUserResp getUserByDeviceUuid(String deviceUuid) {
        FetchUserResp fetchUserResp = null;
        GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreService.getDeviceInfoByDeviceId(deviceUuid);
        if (deviceInfoRespVo == null) {
            logger.error("***** getUserByDeviceUuid() error! deviceInfo is empty");
            throw new BusinessException(DeviceExceptionEnum.DEVICE_NOT_EXIST);
        }
        Long tenantId = deviceInfoRespVo.getTenantId();

        List<ListUserDeviceInfoRespVo> userDeviceInfoRespList = deviceCoreService.listUserDevices(tenantId, deviceUuid);

        if (CollectionUtils.isEmpty(userDeviceInfoRespList)) {
            logger.error("***** getUserByDeviceUuid() error! userDeviceInfoRespList is empty");
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }

        ListUserDeviceInfoRespVo userDeviceInfoResp = userDeviceInfoRespList.get(0);
        Long userId = userDeviceInfoResp.getUserId();

        fetchUserResp = userApi.getUser(userId);

        return fetchUserResp;
    }

    /**
     * 构建 security模块的 topic
     *
     * @param method  方法名
     * @param objUUid 对象uuid(userUuid、deviceUuid)
     * @return
     */
    private String buildClientSecurityTopic(String method, String objUUid) {
        return Constants.TOPIC_CLIENT_PREFIX + objUUid + "/" + "security" + "/" + method;
    }

    /**
     * 保存活动日志（ActivityRecordReq）
     *
     * @param homeId
     * @param armModeEnum
     * @param userId
     * @return
     */
    private void createActivityRecord(Long homeId, ArmModeEnum armModeEnum, Long userId) {
        String foreignId = null;
        String icon = armModeEnum.getArmMode();

        switch (armModeEnum) {
            case stay:
            case away:
                SecurityRule req = new SecurityRule();
                Long tenantId=SaaSContextHolder.currentTenantId();
                Long securityId=getSecurityIdBySpaceId(homeId,tenantId);
                req.setSecurityId(securityId);
                List<SecurityRule> respPage=securityRuleService.list(req);
                if (respPage == null ) {
                    logger.error("*****保存安防活动日志失败! --> homeId={}, securityType={}", homeId, armModeEnum.getArmMode());
                    return;
                }

                SecurityRule ruleResp = respPage.get(0);
                foreignId = ruleResp.getId() + "";

                break;
            case off:
            case panic:
                break;
        }

        ActivityRecordReq activityRecordReq = new ActivityRecordReq();
        activityRecordReq.setActivity( armModeEnum.getArmMode() + " mode is enabled");
        activityRecordReq.setDeviceName(activityRecordReq.getActivity());
        activityRecordReq.setIcon(icon);
        activityRecordReq.setType(Constants.ACTIVITY_RECORD_SECURITY);
        activityRecordReq.setForeignId(foreignId);
        activityRecordReq.setCreateBy(userId);

        UserDefaultOrgInfoResp userOrgInfoResp = userVirtualOrgApi.getDefaultUsedOrgInfoByUserId(userId);
        if (userOrgInfoResp != null) {
            activityRecordReq.setTenantId(userOrgInfoResp.getTenantId());
            activityRecordReq.setOrgId(userOrgInfoResp.getId());
        }
        List<ActivityRecordReq> list = Lists.newArrayList();
        list.add(activityRecordReq);
        activityRecordApi.saveActivityRecord(list);
    }
}
