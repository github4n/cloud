package com.iot.shcs.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.enums.UserType;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.MicroServiceException;
import com.iot.common.exception.RemoteCallBusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.CalendarUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.device.vo.req.UpdateUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.device.vo.resp.UpdateUserDeviceInfoResp;
import com.iot.control.favorite.api.FavoriteApi;
import com.iot.device.api.*;
import com.iot.device.exception.DeviceExceptionEnum;
import com.iot.device.exception.DeviceTypeExceptionEnum;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.util.RedisKeyUtil;
import com.iot.device.vo.req.CountDownReq;
import com.iot.device.vo.req.ElectricityStatisticsReq;
import com.iot.device.vo.req.RuntimeReq;
import com.iot.device.vo.req.device.UpdateDeviceExtendReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import com.iot.device.vo.req.group.UpdateGroupDetailReq;
import com.iot.device.vo.req.ota.BatchIUpgradeDeviceVersion;
import com.iot.device.vo.req.ota.UpgradeDeviceVersionReq;
import com.iot.device.vo.rsp.ConfigurationRsp;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.file.api.FileApi;
import com.iot.file.dto.FileDto;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.redis.RedisCacheUtil;
import com.iot.report.api.DeviceActivateApi;
import com.iot.report.dto.req.DevActivatedVo;
import com.iot.report.entity.DeviceActiveInfo;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.common.email.utils.EmailUtils;
import com.iot.shcs.common.exception.BusinessExceptionEnum;
import com.iot.shcs.common.util.MQTTUtils;
import com.iot.shcs.contants.GatewayErrorCodeEnum;
import com.iot.shcs.contants.ModuleConstants;
import com.iot.shcs.device.enums.DeviceCoreExceptionEnum;
import com.iot.shcs.device.enums.OnlineStatusEnum;
import com.iot.shcs.device.queue.bean.*;
import com.iot.shcs.device.queue.sender.*;
import com.iot.shcs.device.service.DeviceActivateService;
import com.iot.shcs.device.service.IDeviceMQTTService;
import com.iot.shcs.device.service.ReportDeviceDetailsService;
import com.iot.shcs.device.utils.*;
import com.iot.shcs.device.vo.DeviceUserVO;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.helper.DispatcherRouteHelper;
import com.iot.shcs.ifttt.service.IAutoService;
import com.iot.shcs.ipc.config.IPCPropertiesConfig;
import com.iot.shcs.ota.service.OTAService;
import com.iot.shcs.scene.service.SceneDetailService;
import com.iot.shcs.security.service.impl.SecurityMqttService;
import com.iot.shcs.space.service.ISpaceDeviceService;
import com.iot.shcs.space.service.ISpaceService;
import com.iot.shcs.space.service.SpaceCoreService;
import com.iot.shcs.template.service.impl.PackageServiceImpl;
import com.iot.shcs.user.service.UserCoreService;
import com.iot.user.api.UserApi;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.FetchUserResp;
import com.iot.util.ToolUtils;
import com.iot.video.api.VideoManageApi;
import com.iot.video.dto.VideoPlanTaskDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 15:01 2018/10/11
 * @Modify by:
 */
@Slf4j
@Component("device")
public class DeviceMQTTService implements IDeviceMQTTService, CallBackProcessor {

    @Autowired
    private IPCPropertiesConfig ipcPropertiesConfig;

    @Autowired
    private UserApi userApi;

    @Autowired
    private UserCoreService userCoreService;

    @Autowired
    private CountdownCoreApi countdownCoreApi;

    @Autowired
    private ConfigurationApi configurationApi;

    @Autowired
    private VideoManageApi videoManageApi;

    @Autowired
    private ElectricityStatisticsApi electricityStatisticsApi;

    @Autowired
    private SpaceCoreService spaceCoreService;

    @Autowired
    private DeviceCoreService deviceCoreService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MqttCoreService mqttCoreService;

    @Autowired
    private PackageServiceImpl packageService;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private DeviceStateService deviceStateService;

    @Autowired
    private ActivityRecordApi activityRecordApi;

    @Autowired
    private OTAService otaService;

    @Autowired
    private OTAServiceApi otaServiceApi;

    @Autowired
    private IAutoService autoService;

    @Autowired
    private ISpaceDeviceService spaceDeviceService;

    @Autowired
    private ISpaceService spaceService;

    @Autowired
    private SceneDetailService sceneDetailService;

    @Autowired
    private SecurityMqttService securityMqttService;

    @Autowired
    private FileApi fileApi;
    @Autowired
    private DeviceDisconnectSender deviceDisconnectSender;

    @Autowired
    private DeviceConnectSender deviceConnectSender;


    @Autowired
    private SetDeviceAttrNotifySender setDeviceAttrNotifySender;
    @Autowired
    private GroupApi groupApi;

    @Autowired
    private ReportDeviceDetailsService reportDeviceDetailsService;

    @Autowired
    private RobotService robotService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private FavoriteApi favoriteApi;

    @Autowired
    private DeviceActivateService deviceActivateService;

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

    public void onMessage(MqttMsg mqttMsg) {
        DispatcherRouteHelper.dispatch(this, mqttMsg);
    }

    public void devBindReq(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("devBindReq({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> devBindReqPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String userId = (String) devBindReqPayload.get("userId");
        String area = (String) devBindReqPayload.get("area");// 地区
        String deviceUuid = MQTTUtils.parseReqTopic(reqTopic);//直连设备id
        //绑定用户和直连设备的返回结果->通知app/直连设备
        UpdateUserDeviceInfoResp response = null;
        boolean userWhetherOnline = false;
        String productName = null;
        String productIcon = null;
        MqttMsgAck ack = MqttMsgAck.successAck();
        Long tenantId = null;
        try {
            //确保直连设备存在
            GetDeviceInfoRespVo deviceInfo = deviceCoreService.getDeviceInfoByDeviceId(deviceUuid);

            tenantId = deviceInfo.getTenantId();

            //1.获取绑定的用户信息
            FetchUserResp user = userApi.getUserByUuid(userId);

            if (user == null) {
                throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
            }
            //2.检查用户是否在线
            userWhetherOnline = UserUtils.checkUserWhetherOnline(user.getState());

            if (tenantId.compareTo(user.getTenantId()) != 0) {
                throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_TENANT_ERROR);
            }

            GetProductInfoRespVo productInfo = deviceCoreService.getProductById(deviceInfo.getProductId());
            if (tenantId.compareTo(user.getTenantId()) != 0 || tenantId.compareTo(productInfo.getTenantId()) != 0) {
                throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_TENANT_ERROR);
            }

            // 删除 缓存的 devBindNotif消息
            deviceService.deleteDevBindNotifMqttMsgCache(userId, deviceUuid, tenantId);

            ListUserDeviceInfoRespVo userDeviceInfoRespVo = null;
            List<ListUserDeviceInfoRespVo> existRelationShip = deviceCoreService.listUserDevices(tenantId, deviceUuid);
            if (!CollectionUtils.isEmpty(existRelationShip)) {
                userDeviceInfoRespVo = existRelationShip.get(0);
                Long existUserId = userDeviceInfoRespVo.getUserId();
                FetchUserResp existUser = userApi.getUser(existUserId);
                if (!existUser.getUuid().equals(userId)) {
                    throw new BusinessException(DeviceExceptionEnum.DEVICE_BINDED);
                }
            }

            //3.保证一个用户目前只能有一个直连设备的需求
            deviceCoreService.checkUserOnlyDirectDevice(deviceUuid, tenantId, user.getId());
            //4.检查并获取用户默认家
            com.iot.shcs.space.vo.SpaceResp space = spaceService.findUserDefaultSpace(user.getId(), tenantId);
            //5.构建 默认设备名称+租户信息 进行存储
            Map<String, Integer> seqMap = reportDeviceDetailsService.processSpaceSeq(tenantId, user.getId(), Lists.newArrayList(deviceUuid));

            productName = productInfo.getProductName() + "_" + seqMap.get(deviceUuid);
            try {
                FileDto fileDto = fileApi.getGetUrl(productInfo.getIcon());
                productIcon = fileDto.getPresignedUrl();
            } catch (Exception e) {
                logger.warn("get product icon error", e);
            }

            if (userDeviceInfoRespVo == null) {//未绑定要进行banding
                // 6.绑定用户跟直连设备的关系
                UpdateUserDeviceInfoReq bindUserDevice =
                        UpdateUserDeviceInfoReq.builder()
                                .tenantId(tenantId)
                                .orgId(user.getOrgId())
                                .userId(user.getId())
                                .userType(UpdateUserDeviceInfoReq.ROOT_DEVICE)
                                .deviceId(deviceUuid)
                                .build();
                response = deviceCoreService.saveOrUpdateUserDevice(bindUserDevice);

                if (response == null || response.getUserId() == null || response.getDeviceId() == null) {
                    throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_BIND_ERROR);
                }
            } else {
                response = new UpdateUserDeviceInfoResp();
                BeanUtils.copyProperties(userDeviceInfoRespVo, response);
            }

            //7.添加用户和设备的策略
            mqttCoreService.addAcls(tenantId, user.getId(), userId, deviceUuid);

            //8.添加房间跟直连设备的关系
            spaceCoreService.saveOrUpdateSpaceDevice(tenantId, space.getId(), deviceUuid);

            // 9.下发 安防密码给网关
            securityMqttService.setSecurityPasswdNotif(tenantId, deviceUuid);

            // 10.修改设备unbind/reset标志 1是,0否(带套包的网关需要) + 修改设备名称
            UpdateDeviceExtendReq updateDeviceExtendReq = UpdateDeviceExtendReq.builder()
                    .area(area).deviceId(deviceUuid)
                    .unbindFlag(0).resetFlag(0).tenantId(deviceInfo.getTenantId())
                    .build();
            deviceCoreService.saveOrUpdateExtend(updateDeviceExtendReq);

            deviceCoreService.saveOrUpdateDeviceInfo(UpdateDeviceInfoReq.builder()
                    .name(productName).tenantId(tenantId).uuid(deviceUuid).build());

            // 10. 通知第三方 新增了设备
            ApplicationContextHelper.getBean(DeviceAddOrUpdateSender.class).send(DeviceAddOrUpdateMessage.builder()
                    .tenantId(tenantId)
                    .userId(user.getId())
                    .deviceId(deviceUuid)
                    .newAdd(true)
                    .build());


            if (ack.getCode() == MqttMsgAck.SUCCESS) {
                deviceActivateService.recordActiveInfo(tenantId,deviceUuid,null,productInfo.getDeviceTypeId());
            }
        } catch (RemoteCallBusinessException e) {
            log.debug("devBindReq-remote-business-error:{}", e);
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, e.getMessage());//已进行国际化处理返回
        } catch (MicroServiceException e) {
            log.debug("devBindReq-mircoservice-error:{}", e);
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, e.getMessage());//已进行国际化处理返回
        } catch (BusinessException e) {
            log.debug("devBindReq-local-business-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.BUSINESS_ERROR, e.getMessage());//本地调用国际化处理
        } catch (Exception e) {
            log.error("devBindReq-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.BUSINESS_ERROR, DeviceCoreExceptionEnum.SYSTEM_ERROR.getMessageKey());
        } finally {
            // 10响应给设备
            String respTopic = DeviceConstants.buildTopic(deviceUuid, DeviceMethodUtils.DEV_BIND_RESP);
            Map<String, Object> payload = Maps.newHashMap();
            payload.put("userId", userId);
            if (ack.getCode() == MqttMsgAck.SUCCESS && response != null) {
                payload.put("userType", response.getUserType());
                payload.put("password", response.getPassword());
            }
            payload.put("timestamp", DeviceConstants.SIMPLE_DATE_FORMAT.format(new Date()));
            reqMqttMsg.setPayload(payload);
            reqMqttMsg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE, DeviceMethodUtils.DEV_BIND_RESP,
                    respTopic, payload, ack, reqMqttMsg.getSeq(), reqMqttMsg.getSrcAddr());
            log.info("devBindReq-响应给设备({}, {})", reqMqttMsg, respTopic);
            mqttCoreService.sendMessage(reqMqttMsg);

            // 11通知到app
            // 在线才发通知给APP
            log.info("devBindReq-通知到app? {}", userWhetherOnline);
            if (userWhetherOnline) {
                respTopic = DeviceConstants.buildTopic(userId, DeviceMethodUtils.DEV_BIND_NOTIF);

                payload = Maps.newHashMap();
                payload.put("devId", deviceUuid);
                if (ack.getCode() == MqttMsgAck.SUCCESS && response != null) {
                    payload.put("userType", response.getUserType());
                    payload.put("password", response.getPassword());
                    payload.put("productName", productName);
                    payload.put("icon", productIcon);
                }
                payload.put("timestamp", DeviceConstants.SIMPLE_DATE_FORMAT.format(new Date()));
                reqMqttMsg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE, DeviceMethodUtils.DEV_BIND_NOTIF,
                        respTopic, payload, ack, reqMqttMsg.getSeq(), reqMqttMsg.getSrcAddr());

                log.info("devBindReq-通知到app(reqMqttMsg:{},topic:{})", reqMqttMsg, respTopic);
                mqttCoreService.sendMessage(reqMqttMsg);
                deviceService.addDevBindNotifMqttMsgToCache(reqMqttMsg, userId, deviceUuid, tenantId);
            }
        }
    }

    public void devUnbindReq(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("devUnbindReq({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> devUnBindReqPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String unbindDevId = (String) devUnBindReqPayload.get("unbindDevId");
        String unbindUserId = (String) devUnBindReqPayload.get("unbindUserId");

        // 默认为true(兼容6.14版app恢复出厂设置时 不带resetFlag)
        Boolean resetFlag = Boolean.TRUE;
        Object resetFlagObj = devUnBindReqPayload.get("resetFlag");
        if (resetFlagObj != null) {
            String value = String.valueOf(resetFlagObj);
            resetFlag = "1".equals(value);
        }
        String userId = MQTTUtils.parseReqTopic(reqTopic);
        MqttMsgAck ack = MqttMsgAck.successAck();
        try {
            FetchUserResp user = userCoreService.getUserByUuid(userId);
            Long tenantId = user.getTenantId();
            Long fetchUserId = user.getId();
            ListUserDeviceInfoRespVo relationShip = deviceCoreService.getUserDevice(tenantId, fetchUserId, unbindDevId);
            if (relationShip == null) {
                throw new BusinessException(DeviceCoreExceptionEnum.USER_DEVICE_NOT_EXIST);
            }
            if (UserType.ROOT.getName().equals(relationShip.getUserType())) {
                //A. 解绑 主账号和设备的绑定
                String respTopic = DeviceConstants.buildTopic(unbindDevId, DeviceMethodUtils.DEV_UNBIND_NOTIF);

                Map<String, Object> payload = Maps.newHashMap();
                payload.put("userId", unbindUserId);
                payload.put("devId", unbindDevId);
                payload.put("resetFlag", resetFlag);
                payload.put("timestamp", DeviceConstants.SIMPLE_DATE_FORMAT.format(new Date()));
                reqMqttMsg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE
                        , DeviceMethodUtils.DEV_UNBIND_NOTIF, respTopic, payload, ack, reqMqttMsg.getSeq(), reqMqttMsg.getSrcAddr());

                log.info("devUnbindNotif-通知给设备({}, {})", reqMqttMsg, respTopic);
                //直接做解绑 update 1.1.27 协议   ===>>>> 自己执行 confirm
                this.devUnbindConfirm(reqMqttMsg, respTopic);
            } else if (UserType.SUB.getName().equals(relationShip.getUserType())) {
                //B. 子账户的解绑
                reqMqttMsg = this.unbindSubDeviceReq(reqMqttMsg, reqTopic, unbindDevId, unbindUserId, ack, tenantId);
            }
        } catch (RemoteCallBusinessException e) {
            log.debug("devUnbindResp-remote-error:{}", e);
            Map<String, Object> payload = Maps.newHashMap();
            payload.put("timestamp", DeviceConstants.SIMPLE_DATE_FORMAT.format(new Date()));
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
            reqMqttMsg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE
                    , DeviceMethodUtils.DEV_UNBIND_RESP, null, payload, ack, reqMqttMsg.getSeq(), reqMqttMsg.getSrcAddr());
            //响应给app 报设备不存在
            devUnbindResp(reqMqttMsg, reqTopic);
        } catch (BusinessException e) {
            log.debug("devUnbindResp-local-error:{}", e);
            Map<String, Object> payload = Maps.newHashMap();
            payload.put("timestamp", DeviceConstants.SIMPLE_DATE_FORMAT.format(new Date()));
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
            reqMqttMsg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE
                    , DeviceMethodUtils.DEV_UNBIND_RESP, null, payload, ack, reqMqttMsg.getSeq(), reqMqttMsg.getSrcAddr());
            devUnbindResp(reqMqttMsg, reqTopic);
        } catch (Exception e) {
            log.error("devUnbindResp-error:{}", e);
            Map<String, Object> payload = Maps.newHashMap();
            payload.put("timestamp", DeviceConstants.SIMPLE_DATE_FORMAT.format(new Date()));
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.ERROR, DeviceCoreExceptionEnum.SYSTEM_ERROR.getMessageKey());
            reqMqttMsg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE
                    , DeviceMethodUtils.DEV_UNBIND_RESP, null, payload, ack, reqMqttMsg.getSeq(), reqMqttMsg.getSrcAddr());
            devUnbindResp(reqMqttMsg, reqTopic);
        }
    }

    private MqttMsg unbindSubDeviceReq(MqttMsg reqMqttMsg, String reqTopic, String unbindDevId, String unbindUserId, MqttMsgAck ack, Long tenantId) {
        FetchUserResp user;
        try {
            user = userCoreService.getUserByUuid(unbindUserId);
            //1.删除子账户和设备的关系
            deviceCoreService.delBatchSubDeviceId(tenantId, user.getId(), Lists.newArrayList(unbindDevId));
//            deviceCoreService.delUserDeviceParams(tenantId, user.getId(), unbindDevId);
            //2.删除子账户app的订阅权限
            mqttCoreService.separationAcls(tenantId, user.getId(), unbindUserId, unbindDevId);
        } catch (RemoteCallBusinessException e) {
            log.debug("devUnbindResp-child-remote-error:{}", e);
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("devUnbindResp-child-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.ERROR, DeviceCoreExceptionEnum.SYSTEM_ERROR.getMessageKey());
        } finally {
            // 1.响应给app
            Map<String, Object> payload = Maps.newHashMap();
            payload.put("timestamp", DeviceConstants.SIMPLE_DATE_FORMAT.format(new Date()));
            reqMqttMsg.setPayload(payload);
            reqMqttMsg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE, DeviceMethodUtils.DEV_UNBIND_RESP
                    , null, payload, ack, reqMqttMsg.getSeq(), reqMqttMsg.getSrcAddr());
            //响应给app
            devUnbindResp(reqMqttMsg, reqTopic);

            //2. 广播通知消息
            payload = Maps.newHashMap();
            payload.put("userId", unbindUserId);
            payload.put("devId", unbindDevId);
            payload.put("timestamp", DeviceConstants.SIMPLE_DATE_FORMAT.format(new Date()));


            //广播通知用户
            String respTopic = DeviceConstants.buildTopic(unbindUserId, DeviceMethodUtils.DEV_UNBIND_NOTIF);
            reqMqttMsg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE, DeviceMethodUtils.DEV_UNBIND_NOTIF
                    , respTopic, payload, ack, reqMqttMsg.getSeq(), reqMqttMsg.getSrcAddr());
            log.debug("通知到app({}, {})", reqMqttMsg, respTopic);
            mqttCoreService.sendMessage(reqMqttMsg);

            //广播通知设备
            respTopic = DeviceConstants.buildTopic(unbindDevId, DeviceMethodUtils.DEV_UNBIND_NOTIF);
            reqMqttMsg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE
                    , DeviceMethodUtils.DEV_UNBIND_NOTIF, respTopic, payload, ack, reqMqttMsg.getSeq(), reqMqttMsg.getSrcAddr());
            log.debug("通知到设备({}, {})", reqMqttMsg, respTopic);
            mqttCoreService.sendMessage(reqMqttMsg);
        }
        return reqMqttMsg;
    }

    private void devUnbindResp(MqttMsg reqMqttMsg, String reqTopic) {
        String userId = MQTTUtils.parseReqTopic(reqTopic);
        String respTopic = DeviceConstants.buildTopic(userId, DeviceMethodUtils.DEV_UNBIND_RESP);
        reqMqttMsg.setTopic(respTopic);
        log.debug("devUnbindResp-响应给app({}, {})", reqMqttMsg, respTopic);
        mqttCoreService.sendMessage(reqMqttMsg);
    }

    public void devUnbindConfirm(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("devUnbindConfirm({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> devUnbindConfirmPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String directDevId = (String) devUnbindConfirmPayload.get("devId");
        String userUUID = (String) devUnbindConfirmPayload.get("userId");
        Boolean resetFlag = (Boolean) devUnbindConfirmPayload.get("resetFlag");

        MqttMsgAck ack = MqttMsgAck.successAck();
        try {
            FetchUserResp user = userApi.getUserByUuid(userUUID);
            Long tenantId = user.getTenantId();
            Long userId = user.getId();

            //1. del multi sub deviceIds
            this.delMultiSubByParentDevId(directDevId, tenantId, userId);
            //2. del direct relation info
            this.delDirectRelationInfo(directDevId, userUUID, resetFlag, tenantId, userId);

        } catch (RemoteCallBusinessException e) {
            log.debug("devUnbindConfirm-remote-error:{}", e);
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (BusinessException e) {
            log.debug("devUnbindConfirm-local-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("devUnbindConfirm-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.ERROR
                    , DeviceCoreExceptionEnum.SYSTEM_ERROR.getMessageKey());
        } finally {
            //通知用户
            sendNotifyMsg(userUUID, reqMqttMsg.getSeq(), reqMqttMsg.getSrcAddr(), ack);
            //通知直连设备
            String respTopic = DeviceConstants.buildTopic(directDevId, DeviceMethodUtils.DEV_UNBIND_NOTIF);
            DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE
                    , DeviceMethodUtils.DEV_UNBIND_NOTIF, respTopic
                    , reqMqttMsg.getPayload(), ack, reqMqttMsg.getSeq(), MQTTUtils.DEFAULT_CLOUD_SOURCE_ADDR);
            //响应
            mqttCoreService.sendMessage(reqMqttMsg);

        }
    }

    private void delDirectRelationInfo(String directDevId, String userUUID, Boolean resetFlag, Long tenantId, Long userId) {
        /**判断是否是IPC设备*/
        GetDeviceTypeInfoRespVo deviceTypeInfo = deviceCoreService.getDeviceTypeByDeviceId(directDevId);
        if (deviceTypeInfo != null && ModuleConstants.IPC_TYPE_NAME.equals(deviceTypeInfo.getType())) {
            this.videoManageApi.planUnBandingDevice(tenantId, directDevId);
        }
        // 2. update direct device
        this.updateDeviceResetAndUnBindFlag(tenantId, directDevId, resetFlag);
        // 2.1 delete direct relation info
        reportDeviceDetailsService.deleteMultiDeviceRelation(tenantId, Lists.newArrayList(directDevId),userId);
        // 2.2 delete user to device relation and reset device parent NULL
        deviceCoreService.delBatchSubDeviceId(tenantId, userId, Lists.newArrayList(directDevId));//删除用户关系 【并清空parentId】
        // 2.3 unbind user to device acls
        mqttCoreService.separationAcls(tenantId, userId, userUUID, directDevId);
        // 2.4 notify robot to delete direct deviceId
        robotService.sendDeleteBatchDevIds(tenantId, userId, Lists.newArrayList(directDevId));
    }

    private void delMultiSubByParentDevId(String devId, Long tenantId, Long userId) {
        List<String> childDevIds = deviceCoreService.listDeviceIdsByParentId(devId);
        if (!CollectionUtils.isEmpty(childDevIds)) {
            reportDeviceDetailsService.deleteMultiDeviceRelation(tenantId, childDevIds,userId);//删除安防、房间、auto、scene
            deviceCoreService.delBatchSubDeviceId(tenantId, userId, childDevIds);//删除用户关系 【并清空parentId】
            robotService.sendDeleteBatchDevIds(tenantId, userId, childDevIds);// 通知 robot
        }
    }

    public void getDevListReq(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("getDevListReq({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> getDevListReqPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String homeId = (String) getDevListReqPayload.get("homeId");
        Integer pageSize = (Integer) getDevListReqPayload.get("pageSize");
        Integer offset = (Integer) getDevListReqPayload.get("offset");
        String[] split = reqTopic.split("/");

        String userUUID = MQTTUtils.parseReqTopic(reqTopic);
        Long userId = null;

        Map<String, Object> payload = new HashMap<>();
        MqttMsgAck ack = MqttMsgAck.successAck();
        try {
            Long tenantId = null;
            FetchUserResp fetchUserResp = userApi.getUserByUuid(userUUID);
            if (fetchUserResp != null) {
                userId = fetchUserResp.getId();
            }
            if (StringUtils.isEmpty(homeId)) {
                throw new BusinessException(DeviceCoreExceptionEnum.HOMEID_NOT_NULL);
            }
            tenantId = fetchUserResp.getTenantId();
            //获取家下面的设备列表
            payload = spaceService.getHomeDevListAndCount(tenantId, userId, Long.valueOf(homeId));
        } catch (RemoteCallBusinessException e) {
            e.printStackTrace();
            log.debug("getDevListReq-remote-error:{}", e);
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (BusinessException e) {
            e.printStackTrace();
            log.debug("getDevListReq-local-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getDevListReq-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.ERROR, DeviceCoreExceptionEnum.SYSTEM_ERROR.getMessageKey());
        } finally {
            // 响应给APP
            payload.put("timestamp", DeviceConstants.SIMPLE_DATE_FORMAT.format(new Date()));
            String respTopic = DeviceConstants.buildTopic(userUUID, DeviceMethodUtils.GET_DEV_LIST_RESP);
            MqttMsg msg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE
                    , DeviceMethodUtils.GET_DEV_LIST_RESP, respTopic,
                    payload, ack, reqMqttMsg.getSeq(), reqMqttMsg.getSrcAddr());

            log.debug("getDevListResp-响应到app({}, {})", msg, respTopic);
            mqttCoreService.sendMessage(reqMqttMsg);
        }
    }

    public void updateDevBaseInfo(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("updateDevBaseInfo({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> updateDevBasicsPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceId = (String) updateDevBasicsPayload.get("devId");
        Map<String, Object> attrMap = (Map<String, Object>) updateDevBasicsPayload.get("attr");

        String productModel = (String) updateDevBasicsPayload.get("productId");
        MqttMsgAck ack = MqttMsgAck.successAck();
        Boolean unbindFlag = false;
        Boolean resetFlag = false;
        Map<String, Object> payload = Maps.newHashMap();
        try {
            if (StringUtils.isEmpty(productModel)) {
                throw new BusinessException(ProductExceptionEnum.PRODUCT_ID_NOTNULL);
            }
            GetProductInfoRespVo productResp = deviceCoreService.getByProductModel(productModel);
            if (productResp == null) {
                throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
            }
            Long tenantId = productResp.getTenantId();

            GetDeviceInfoRespVo device = deviceCoreService.getDeviceInfoByDeviceId(deviceId);
            if (device != null) {
                //检查 避免 不同设备使用了同样的证书id/deviceId 【不同产品使用了同一个deviceId 导致的异常】
                if (productResp.getId().compareTo(device.getProductId()) != 0) {
                    //产品id不一致 通知开发
                    emailSender.sendEmailNotify(
                            EmailUtils.getDeviceProductChangeTitle(deviceId),
                            EmailUtils.getDeviceProductChangeContent(deviceId, device.getProductId()
                                    , productResp.getId()));
                    throw new BusinessException(BusinessExceptionEnum.DEVICE_LICENSE_ERROR);
                }
                // 检查 避免 同一个证书ID烧录到了两个不同的设备上面
                String mac = (String) updateDevBasicsPayload.get("mac");
                mac = StringUtil.isBlank(mac) ? "" : mac.trim();
                if (!StringUtil.isBlank(device.getMac()) && !mac.equals(device.getMac())) {
                    emailSender.sendEmailNotify(
                            EmailUtils.getDeviceMacChangeTitle(deviceId),
                            EmailUtils.getDeviceMacChangeContent(deviceId, device.getMac(), mac));
                    throw new BusinessException(BusinessExceptionEnum.DEVICE_MAC_ERROR);
                }
            }
            if (device == null) {
                log.debug("---- 设备记录不存在(deviceUuid={}), 创建新 device到数据库中 ---- ", deviceId);
                // 新增 device
                UpdateDeviceInfoReq deviceInfoParam = UpdateDeviceInfoReq
                        .builder()
                        .uuid(deviceId)
                        .isDirectDevice(1)
                        .productId(productResp.getId())
                        .deviceTypeId(productResp.getDeviceTypeId())
                        .tenantId(tenantId)
                        .build();
                device = deviceCoreService.saveOrUpdateDeviceInfo(deviceInfoParam);

                UpdateDeviceStatusReq deviceStatusParam = UpdateDeviceStatusReq
                        .builder()
                        .deviceId(deviceId)
                        .tenantId(tenantId)
                        .onlineStatus(OnlineStatusEnum.CONNECTED.getCode())
                        .build();
                deviceCoreService.saveOrUpdateDeviceStatus(deviceStatusParam);

            }

            /*
             * 手机发起恢复出厂设置, 网关在线, 网关本身也会更新ResetRandom -> 不再下发reset标志
             * 手机发起恢复出厂设置, 网关离线, 网关ResetRandom不变 ->  下发reset标志,让网关恢复出厂设置, 但是不能更新wifi配置
             * 手动重置网关, 网关ResetRandom会变化 -> 云端处理恢复删除直连设备和子设备的逻辑
             * */
            String deviceResetRandom = device.getResetRandom() == null ? StringUtil.EMPTY : device.getResetRandom();
            String resetRandom = (String) updateDevBasicsPayload.get("resetRandom");
            log.info("---- updateDevBaseInfo(dbResetRandom:{},newResetRandom:{})------ ", deviceResetRandom, resetRandom);
            Boolean isGatewayReset = StringUtil.isNotBlank(resetRandom) && !resetRandom.equals(deviceResetRandom);
            GetDeviceExtendInfoRespVo deviceExtendResp = deviceCoreService.getDeviceExtendByDeviceId(tenantId, deviceId);

            log.info("---- updateDevBaseInfo(isGatewayReset:{},deviceExtendResp:{})------ ", isGatewayReset, JSON.toJSONString(deviceExtendResp));

            Boolean isFactoryReset = deviceExtendResp != null && deviceExtendResp.getResetFlag() != null && "1".equalsIgnoreCase(deviceExtendResp.getResetFlag().toString());
            unbindFlag = deviceExtendResp != null && deviceExtendResp.getUnbindFlag() != null && "1".equalsIgnoreCase(deviceExtendResp.getUnbindFlag().toString());
            if (isFactoryReset && !isGatewayReset) {
                resetFlag = true;
            } else if (!isFactoryReset && isGatewayReset) {
                log.info("---- updateDevBaseInfo(reset deviceId:{},------ ", deviceId);
                this.removeDirectlyDevice(deviceId);
            }

            //2.构建ota操作
            /******************ota 开始处理****************************/
            List<UpgradeDeviceVersionReq> upgradeDeviceVersionReqList = new ArrayList<>();
            List<Map<String, Object>> returnOta = null;
            //记录设备ota信息
            String key = RedisKeyUtil.getUpgradeDeviceStatusKey(deviceId);
            RedisCacheUtil.delete(key);
            returnOta = otaService.buildOtaForUpdateDeviceInfo(deviceId, updateDevBasicsPayload, upgradeDeviceVersionReqList);
            //校验升级
            otaService.checkAndUpgrade(updateDevBasicsPayload, deviceId, productResp.getId());
            //设备版本对应关系记录
            BatchIUpgradeDeviceVersion batchIUpgradeDeviceVersion = new BatchIUpgradeDeviceVersion();
            batchIUpgradeDeviceVersion.setUpgradeDeviceVersionReqList(upgradeDeviceVersionReqList);
            otaServiceApi.batchInsertUpgradeDeviceVersion(batchIUpgradeDeviceVersion);
            /******************ota 结束处理****************************/
            payload.put("ota", returnOta);


            //3.更新设备信息
            deviceService.buildUpdateBaseDeviceInfo(tenantId, deviceId, updateDevBasicsPayload, returnOta, resetRandom, productResp.getId());
            log.info("---- buildUpdateBaseDeviceInfo success------ ");
            //4. 保存属性值
            deviceService.insertDeviceStatus(device, attrMap);

            List<ListUserDeviceInfoRespVo> userDeviceInfoRespList = deviceCoreService.listUserDevices(tenantId, deviceId);

            List<DeviceUserVO> deviceUserVOs = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(userDeviceInfoRespList)) {
                for (ListUserDeviceInfoRespVo userDeviceInfoResp : userDeviceInfoRespList) {
                    DeviceUserVO deviceUserVO = new DeviceUserVO();
                    //转化userId
                    FetchUserResp fetchUserResp = userApi.getUser(userDeviceInfoResp.getUserId());
                    deviceUserVO.setUserId(fetchUserResp.getUuid() + "");
                    deviceUserVO.setUserType(userDeviceInfoResp.getUserType());
                    deviceUserVO.setPassword(userDeviceInfoResp.getPassword());
                    deviceUserVOs.add(deviceUserVO);
                }
            }

            payload.put("accounts", deviceUserVOs);
            //查询设备对应的产品
            //查询设备类型
            log.info("productResp.getDeviceTypeId(): " + productResp.getDeviceTypeId());
            GetDeviceTypeInfoRespVo deviceTypeResp = deviceCoreService.getDeviceTypeById(productResp.getDeviceTypeId());
            //5.构建ipc设备信息
            buildIPC(payload, deviceTypeResp, deviceId);
            //同步IPC信息 end
            //6.同步plug设备信息
            buildWifiPlugs(payload, deviceTypeResp);

        } catch (RemoteCallBusinessException e) {
            e.printStackTrace();
            log.debug("updateDevBaseInfo-remote-error: {}", e.toString());
            payload = Maps.newHashMap();
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, e.getMessage());

        } catch (BusinessException e) {
            e.printStackTrace();
            log.debug("updateDevBaseInfo-local-error: {}", e.toString());
            payload = Maps.newHashMap();
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("updateDevBaseInfo-system.error:{}", e.toString());
            payload = Maps.newHashMap();
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.ERROR, DeviceCoreExceptionEnum.SYSTEM_ERROR.getMessageKey());
        } finally {
            payload.put("unbindFlag", unbindFlag);
            payload.put("resetFlag", resetFlag);

            String respTopic = DeviceConstants.buildTopic(deviceId, DeviceMethodUtils.SYN_DEV_BASE_CONFIGURE_TOPIC);
            MqttMsg synDevBasicConfigMsg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE
                    , DeviceMethodUtils.SYN_DEV_BASE_CONFIGURE, respTopic,
                    payload, ack, reqMqttMsg.getSeq(), reqMqttMsg.getSrcAddr());

            log.debug("updateDevBaseInfo-同步到设备之上({}, {})", synDevBasicConfigMsg, respTopic);
            mqttCoreService.sendMessage(synDevBasicConfigMsg);
        }
    }

    public void devStatusNotif(MqttMsg reqMqttMsg, String reqTopic) {
        try {
            log.debug("devStatusNotif({}, {})", reqMqttMsg, reqTopic);
            Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
            String subDeviceId = String.valueOf(payload.get("devId"));
            // 离线:0, 在线:1(协议文档里目前写 true/false, 网关实际上报来的是0/1)
            Integer online = (Integer) payload.get("online");
            OnlineStatusEnum onlineStatus = OnlineStatusEnum.getByValue(online);

            String deviceId = MQTTUtils.parseReqTopic(reqTopic);//直连设备id
            GetDeviceInfoRespVo subDeviceResp = deviceCoreService.getDeviceInfoByDeviceId(subDeviceId);
            if (subDeviceResp == null || !subDeviceResp.getParentId().equals(deviceId)) {
                log.error("devStatusNotif-parentId-error.orig-parentId:{},nowParentId:{}", subDeviceResp != null ? subDeviceResp.getParentId() : null, deviceId);
                return;
            }
            Long tenantId = subDeviceResp.getTenantId();
            log.debug("devStatusNotif-sendConnectionMessage({}, {},{})", tenantId, subDeviceId, onlineStatus);
            sendConnectionMessage(tenantId, subDeviceId, onlineStatus);
        } catch (Exception e) {
            log.error("devStatusNotif-error.\n{}", e);
        }
    }


    private void sendConnectionMessage(Long tenantId, String deviceUuid, OnlineStatusEnum onlineStatus) {
        log.debug("devStatusNotif-sendConnectionMessage.tenantId:{},deviceId:{},online:{}", tenantId, deviceUuid, onlineStatus);
        if (OnlineStatusEnum.DISCONNECTED.compareTo(onlineStatus) == 0) {
            deviceDisconnectSender.send(DeviceDisconnectMessage.builder()
                    .deviceId(deviceUuid)
                    .status(onlineStatus.getCode())
                    .tenantId(tenantId).build());

            log.debug("devStatusNotif-sendConnectionMessage-DISCONNECTED-success");
        } else if (OnlineStatusEnum.CONNECTED.compareTo(onlineStatus) == 0) {
            deviceConnectSender.send(DeviceConnectMessage.builder()
                    .deviceId(deviceUuid)
                    .status(onlineStatus.getCode())
                    .tenantId(tenantId).build());
            log.debug("devStatusNotif-sendConnectionMessage-CONNECTED-success");
        }
    }

    /**
     * 小网关分包上传设备更新
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    public void updateDevDetails(MqttMsg reqMqttMsg, String reqTopic) {
        Map<String, Object> setDevAttrReqPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        List<Map<String, Object>> subDevs = (List<Map<String, Object>>) setDevAttrReqPayload.get("subDev");
        Integer totalSubDevNum = setDevAttrReqPayload.get("totalSubDevNum") == null ? null : (Integer) setDevAttrReqPayload.get("totalSubDevNum");
        if (totalSubDevNum == null || subDevs.size() == totalSubDevNum) {
            //兼容旧版本和只有一个包的情况
            reportDeviceDetailsService.doUpdateDevDetails(reqMqttMsg, reqTopic);
        } else {
            String deviceId = MQTTUtils.parseReqTopic(reqTopic);
            String batchNum = (String) setDevAttrReqPayload.get("batchNum");
            String cacheKey = RedisKeyUtils.getUpdateSubDeviceDetailKey(deviceId, batchNum);
            String lockKey = RedisKeyUtils.getUpdateSubDeviceDetailLockKey(deviceId, batchNum);
            String lockValue = UUID.randomUUID().toString();
            //获取锁
            getUpdateDevDetailsLock(lockKey, lockValue);
            try {
                List<String> cacheDevs = RedisCacheUtil.listGetAll(cacheKey);
                int currentTotal = cacheDevs.size() + subDevs.size();
                if (currentTotal == totalSubDevNum) {
                    //最后一个包，做更新处理
                    List<Map<String, Object>> subDevsCache = new ArrayList<>();
                    cacheDevs.forEach(s -> subDevsCache.add(JSON.parseObject(s, Map.class)));
                    subDevs.addAll(subDevsCache);
                    reportDeviceDetailsService.doUpdateDevDetails(reqMqttMsg, reqTopic);
                    RedisCacheUtil.delete(cacheKey);
                } else {
                    //不是最后一个包，放入缓存
                    RedisCacheUtil.listLeftPushAll(cacheKey, subDevs);
                    RedisCacheUtil.expireKey(cacheKey, 600);
                }
            } finally {
                //释放锁
                String lockValueCache = RedisCacheUtil.valueGet(lockKey);
                if (lockValue.equals(lockValueCache)) {
                    RedisCacheUtil.delete(lockKey);
                }
            }
        }
    }

    /**
     * 循环获取锁，直到拿到为止
     *
     * @param lockKey
     * @param lockValue
     */
    private void getUpdateDevDetailsLock(String lockKey, String lockValue) {
        Boolean getLockFlag = false;
        do {
            if (RedisCacheUtil.setNx(lockKey, lockValue, 10)) {
                log.debug("拿到锁");
                getLockFlag = true;
            } else {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("没拿到锁");
            }
        } while (!getLockFlag);

    }

    public void setDevAttr(String deviceId, Map<String, Object> payloadMap, Map<String, Object> attr) {
        MqttMsg setDevAttrReqMsg = new MqttMsg();

        GetDeviceInfoRespVo devInfo = deviceCoreService.getDeviceInfoByDeviceId(deviceId);

        List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = deviceCoreService.listUserDevices(devInfo.getTenantId(), deviceId);
        if (CollectionUtils.isEmpty(userDeviceInfoRespVoList)) {
            log.debug("sendUserRefreshMQTTNotify-获取不到user_device 信息 关于deviceId:{}", deviceId);
            return;
        }
        Long userId = userDeviceInfoRespVoList.get(0).getUserId();
        String userUuid = userApi.getUuid(userId);
        setDevAttrReqMsg.setService(DeviceMethodUtils.DEV_SERVICE);
        setDevAttrReqMsg.setMethod(DeviceMethodUtils.SET_DEV_ATTR_REQ);
        // 模仿app端向设备发送消息
        setDevAttrReqMsg.setSrcAddr("0."+userUuid);
        Map<String, Object> payload = Maps.newHashMap();
        if (payloadMap != null) {
            payload.putAll(payloadMap);
        }
        payload.put("devId", deviceId);
        payload.put("attr", attr);
        setDevAttrReqMsg.setPayload(payload);
        setDevAttrReqMsg.setSeq(StringUtil.getRandomString(10));
        //查找父级设备ID
        String directId = devInfo.getParentId();  // 直连设备ID
        if (StringUtil.isBlank(directId)) {
            directId = deviceId;
        }
        String respTopic = DeviceConstants.buildTopic(directId, DeviceMethodUtils.SET_DEV_ATTR_REQ);
        log.debug("setDevAttr，发送MQTT到设备({}, {})", setDevAttrReqMsg, respTopic);
        setDevAttrReqMsg.setTopic(respTopic);

        mqttCoreService.sendMessage(setDevAttrReqMsg);
    }

    public void setDevAttr(String deviceId, Map<String, Object> attr) {
        this.setDevAttr(deviceId, null, attr);
    }

    public void setDevAttrNotif(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("setDevAttrNotif({}, {})", reqMqttMsg, reqTopic);

        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        Map<String, Object> attrMap = (Map<String, Object>) payload.get("attr");
        String subDeviceId = (String) payload.get("devId");
        String deviceId = MQTTUtils.parseReqTopic(reqTopic);
        GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreService.getDeviceInfoByDeviceId(deviceId);
        if (deviceInfoRespVo == null) {
            return;
        }

        Long tenantId = deviceInfoRespVo.getTenantId();
        //mq 再次投递发送
        setDeviceAttrNotifySender.send(SetDeviceAttrMessage.builder()
                .tenantId(tenantId)
                .attrMap(attrMap).parentDeviceId(deviceId).subDeviceId(subDeviceId).payload(payload).build());

    }

    public void devEventNotif(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("devEventNotif({}, {})", reqMqttMsg, reqTopic);
        Long tenantId = SaaSContextHolder.currentTenantId();
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String devId = (String) payload.get("devId"); //可能是直连设备也可能是子设备
        String event = (String) payload.get("event");
        List<Object> arguments = (List<Object>) payload.get("arguments");
        String directDevId = MQTTUtils.parseReqTopic(reqTopic);

        //mq 再次投递发送
        ApplicationContextHelper.getBean(DevEventNotifySender.class).send(DevEventMessage.builder()
                .tenantId(tenantId)
                .parentDeviceId(directDevId)
                .devId(devId)
                .event(event)
                .seq(reqMqttMsg.getSeq())
                .srcAddress(reqMqttMsg.getSrcAddr())
                .arguments(arguments)
                .build());

    }

    public void getDevAttrReq(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("getDevAttrReq({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> setDevAttrReqPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceId = (String) setDevAttrReqPayload.get("devId");

        // 转发MQTT到设备
        doGetDevAttrReq(reqMqttMsg, deviceId);
    }

    private void doGetDevAttrReq(MqttMsg getDevAttrReqMsg, String deviceId) {
        // 转发MQTT到设备
        String getDevAttrRespToDevTopic = "iot/v1/c/" + deviceId + "/device/getDevAttrReq";
        log.debug("doGetDevAttrReq-转发MQTT到设备({}, {})", getDevAttrReqMsg, getDevAttrRespToDevTopic);
        getDevAttrReqMsg.setTopic(getDevAttrRespToDevTopic);

        mqttCoreService.sendMessage(getDevAttrReqMsg);
    }

    public MqttMsg getDevAttrResp(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("getDevAttrResp({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> getDevAttrReqPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceId = (String) getDevAttrReqPayload.get("devId");
        // 转发到app
        String respTopic = DeviceConstants.buildTopic(deviceId, DeviceMethodUtils.GET_DEV_ATTR_RESP);
        log.debug("getDevAttrResp-转发到app({}, {})", reqMqttMsg, respTopic);
        reqMqttMsg.setTopic(respTopic);

        mqttCoreService.sendMessage(reqMqttMsg);

        return reqMqttMsg;
    }

    public void getRoomDevListReq(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("getRoomDevListReq({}, {})", reqMqttMsg, reqTopic);
        MqttMsgAck ack = MqttMsgAck.successAck();

        String userId = ToolUtils.getClientId(reqTopic);
        Map<String, Object> resultMap = null;
        try {
            Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
            String roomId = String.valueOf(payload.get("roomId"));
            String homeId = String.valueOf(payload.get("homeId"));
            FetchUserResp user = userApi.getUserByUuid(userId);
            Long tenantId = user.getTenantId();
            //1.获取房间下面的设备
            resultMap = spaceCoreService.findSpaceDeviceList(tenantId, user.getId(), roomId, homeId);
        } catch (RemoteCallBusinessException e) {
            e.printStackTrace();
            log.debug("getRoomDevListReq-remote-error: {}", e);
            resultMap = Maps.newHashMap();
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (BusinessException e) {
            e.printStackTrace();
            log.debug("getRoomDevListReq-local-error: {}", e);
            resultMap = Maps.newHashMap();
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getRoomDevListReq-system.error:{}", e);
            resultMap = Maps.newHashMap();
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.ERROR, DeviceCoreExceptionEnum.SYSTEM_ERROR.getMessageKey());
        } finally {
            // 响应给app
            reqMqttMsg.setMethod(DeviceMethodUtils.GET_ROOM_DEV_LIST_RESP);
            reqMqttMsg.setAck(ack);
            reqMqttMsg.setPayload(resultMap);
            String respTopic = DeviceConstants.buildTopic(userId, DeviceMethodUtils.GET_ROOM_DEV_LIST_RESP);
            reqMqttMsg.setTopic(respTopic);
            log.debug("getRoomDevListResp({}, {})", reqMqttMsg, respTopic);
            mqttCoreService.sendMessage(reqMqttMsg);
        }
    }

    /**
     * 获取设备图标
     *
     * @return
     */
    private String getIcon(GetDeviceInfoRespVo device, GetProductInfoRespVo productResp) {
        String icon = null;
        if (device == null) {
            if (productResp.getModel() != null && productResp.getModel().contains(".")) {
                String deviceIcon = productResp.getModel().split("\\.")[1];
                icon = deviceIcon;
            }
        } else {
            if (StringUtils.isEmpty(device.getIcon())) {
                if (productResp.getModel() != null && productResp.getModel().contains(".")) {
                    String deviceIcon = productResp.getModel().split("\\.")[1];
                    icon = deviceIcon;
                }
            } else {
                icon = device.getIcon();
            }
        }

        return icon;
    }


    public void addDevNotif(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("addDevNotif({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> addDevNotifyPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String subDevId = (String) addDevNotifyPayload.get("devId");
        String productModel = (String) addDevNotifyPayload.get("productId");
        String baseModel = (String) addDevNotifyPayload.get("base");
        String mac = (String) addDevNotifyPayload.get("mac");

        String deviceId = MQTTUtils.parseReqTopic(reqTopic);
        MqttMsgAck ack = reqMqttMsg.getAck();

        String userUuid = null;
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = null;
        List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = deviceCoreService.listUserDevices(tenantId, deviceId);
        if (!CollectionUtils.isEmpty(userDeviceInfoRespVoList)) {
            userId = userDeviceInfoRespVoList.get(0).getUserId();
        }
        FetchUserResp fetchUserResp = userApi.getUser(userId);
        userUuid = fetchUserResp.getUuid();

        try {
            if (ack != null && ack.getCode() != MqttMsgAck.SUCCESS) {
                return;
            }
            //1.检查model
            if (StringUtils.isEmpty(productModel)) {
                throw new BusinessException(ProductExceptionEnum.PRODUCT_ID_NOTNULL);
            }
//            if (StringUtils.isEmpty(baseModel)){
//                throw new BusinessException(ProductExceptionEnum.BASE_NOTNULL);
//            }
            GetProductInfoRespVo productResp = new GetProductInfoRespVo();
            productResp = deviceCoreService.getByProductModel(productModel);
            if (productResp == null) {
                if (StringUtils.isEmpty(baseModel)) {
                    throw new BusinessException(ProductExceptionEnum.BASE_NOTNULL);
                }
                productResp = deviceCoreService.getByProductModel(baseModel);
            }
            if (productResp == null) {
                throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
            }
            String productName = deviceService.buildDeviceName(SaaSContextHolder.currentTenantId(), productResp, userId);
            //2.检查设备信息-无新增
            GetDeviceInfoRespVo device = deviceCoreService.getDeviceInfoByDeviceId(subDevId);
            // 获取设备图标
            String icon = getIcon(device, productResp);

            // 新增 后修改 device and 设置子设备的 parentId
            UpdateDeviceInfoReq subDeviceInfo = UpdateDeviceInfoReq.builder()
                    .uuid(subDevId)
                    .parentId(deviceId)
                    .productId(productResp.getId())
                    .deviceTypeId(productResp.getDeviceTypeId())
                    .icon(icon)
                    .mac(mac)
                    .name(productName)
                    .tenantId(tenantId)
                    .isDirectDevice(0)
                    .build();
            deviceCoreService.saveOrUpdateDeviceInfo(subDeviceInfo);

            // 3.设置子设备状态为 connected
            UpdateDeviceStatusReq statusReq = UpdateDeviceStatusReq.builder()
                    .tenantId(tenantId)
                    .deviceId(subDevId)
                    .onlineStatus("connected")
                    .build();
            //赋予motion_sensor 状态 默认值为active
            if (productResp.getModel().contains("sensor")) {
                statusReq.setActiveStatus(1);
            }
            deviceCoreService.saveOrUpdateDeviceStatus(statusReq);


            log.debug("addDevNotif({}, {},{})", reqMqttMsg, subDeviceInfo, statusReq);

            if (StringUtil.isNotBlank(productName)) {
                addDevNotifyPayload.put("productName", productName);
            }
            GetDeviceTypeInfoRespVo deviceTypeInfo = deviceCoreService.getDeviceTypeById(productResp.getDeviceTypeId());
            if (deviceTypeInfo == null || StringUtils.isEmpty(deviceTypeInfo.getType())) {
                throw new BusinessException(DeviceTypeExceptionEnum.DEVICETYP_NOT_EXIST);
            }
            addDevNotifyPayload.put("devType", deviceTypeInfo.getType());
            //4.互踢操作
            // 新主人id
            Long newOwnerUserId = deviceCoreService.getRootUserIdByDeviceId(tenantId, deviceId);
            // 旧的主人id
            Long oldOwnerUserId = deviceCoreService.getRootUserIdByDeviceId(tenantId, subDevId);
            if (oldOwnerUserId == null) {
                // subDevId 未跟 user绑定过
                // 建立 subDevId 与 newOwnerUserId关系(user_device)
                UpdateUserDeviceInfoReq userDeviceInfo = UpdateUserDeviceInfoReq.builder()
                        .userId(newOwnerUserId)
                        .userType(UpdateUserDeviceInfoReq.ROOT_DEVICE)
                        .tenantId(tenantId)
                        .deviceId(subDevId)
                        .build();
                deviceCoreService.saveOrUpdateUserDevice(userDeviceInfo);
            } else {
                //手动复位子设备，不管是不是加在原来的账户中，都要当做重新添加
                log.debug("addDevNotif(), oldOwnerUserId({}) == newOwnerUserId({})", oldOwnerUserId, newOwnerUserId);
                // subDevId 被别的 user绑定了
                // 移除 subDevId 与 oldOwnerUserId关系(user_device)
                deviceCoreService.delChildDeviceByDeviceId(tenantId, subDevId, oldOwnerUserId);
                deleteDeviceRelationships(subDevId);

                // 建立 subDevId 与 newOwnerUserId关系(user_device)
                UpdateUserDeviceInfoReq userDeviceInfo = UpdateUserDeviceInfoReq.builder()
                        .userId(newOwnerUserId)
                        .userType(UpdateUserDeviceInfoReq.ROOT_DEVICE)
                        .tenantId(tenantId)
                        .deviceId(subDevId)
                        .build();
                deviceCoreService.saveOrUpdateUserDevice(userDeviceInfo);
            }
            // 6.设置 subDeviceId与房间关系(space_device)
            spaceCoreService.bindSpaceByDeviceId(deviceId, subDevId, tenantId);

            //7.恢复灯的默认属性
            deviceStateService.recoveryDefaultState(tenantId, subDevId);

            // 8. 通知第三方 新增了设备
            ApplicationContextHelper.getBean(DeviceAddOrUpdateSender.class).send(DeviceAddOrUpdateMessage.builder()
                    .tenantId(tenantId)
                    .userId(newOwnerUserId)
                    .deviceId(subDevId)
                    .newAdd(true)
                    .build());

            if (ack.getCode() == MqttMsgAck.SUCCESS) {
                deviceActivateService.recordActiveInfo(tenantId,subDevId,deviceId,productResp.getDeviceTypeId());
            }
        } catch (RemoteCallBusinessException e) {
            log.debug("addDevNotif-remote-error.:{}", e);
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (BusinessException e) {
            log.debug("addDevNotif-local-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("addDevNotif-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.ERROR, DeviceCoreExceptionEnum.SYSTEM_ERROR.getMessageKey());
        } finally {
            // 通知给app
            reqMqttMsg.setMethod(DeviceMethodUtils.ADD_DEV_NOTIF);
            reqMqttMsg.setAck(ack);
            String respTopic = DeviceConstants.buildTopic(userUuid, DeviceMethodUtils.ADD_DEV_NOTIF);
            reqMqttMsg.setTopic(respTopic);
            log.debug("addDevNotif-响应内容{}", reqMqttMsg);
            mqttCoreService.sendMessage(reqMqttMsg);
        }

    }

    public void delDevReq(MqttMsg reqMqttMsg, String reqTopic) {
        MqttMsgAck ack = MqttMsgAck.successAck();
        String userUUID = null;
        try {
            log.debug("delDevReq({}, {})", reqMqttMsg, reqTopic);
            Map<String, Object> addDevNotifyPayload = (Map<String, Object>) reqMqttMsg.getPayload();
            String subDevId = (String) addDevNotifyPayload.get("devId");
            userUUID = MQTTUtils.parseReqTopic(reqTopic);
            //获取直连设备 转发直连设备删除
            GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(subDevId);

            String parentDeviceUUID = deviceResp.getParentId();
            if (!StringUtils.isEmpty(parentDeviceUUID)) {
                // 转发给gateway
                reqMqttMsg.setMethod(DeviceMethodUtils.DEL_DEV_REQ);
                reqMqttMsg.setAck(ack);
                String respTopic = DeviceConstants.buildTopic(deviceResp.getParentId(), DeviceMethodUtils.DEL_DEV_REQ);
                reqMqttMsg.setTopic(respTopic);
                log.info("delDevReq-gateway:{}", reqMqttMsg);
                mqttCoreService.sendMessage(reqMqttMsg);
            }
        } catch (RemoteCallBusinessException e) {
            log.debug("delDevReq-remote-error.:{}", e);
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (BusinessException e) {
            log.debug("delDevReq-local-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("delDevReq-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.ERROR, DeviceCoreExceptionEnum.SYSTEM_ERROR.getMessageKey());
        } finally {
            // 响应给app
            reqMqttMsg.setMethod(DeviceMethodUtils.DEL_DEV_RESP);
            reqMqttMsg.setAck(ack);
            String respTopic = DeviceConstants.buildTopic(userUUID, DeviceMethodUtils.DEL_DEV_RESP);
            reqMqttMsg.setTopic(respTopic);
            log.debug("delDevResp-app:{}", reqMqttMsg);
            mqttCoreService.sendMessage(reqMqttMsg);
        }
    }

    public void delDevResp(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("delDevResp({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> delDevRespPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String subDevId = (String) delDevRespPayload.get("devId");
        String directDeviceId = MQTTUtils.parseReqTopic(reqTopic);
        try {
            if (reqMqttMsg.getAck() == null) {
                log.debug("*****delDevResp() error! because delDevRespMsgAck is null.");
                return;
            }
            if (reqMqttMsg.getAck().getCode() != MqttMsgAck.SUCCESS) {
                if (reqMqttMsg.getAck().getCode() == GatewayErrorCodeEnum.INVALID_DEVICE_ID.getCodeValue()) {
                    log.debug("*****delDevResp****网关成功，再次删除提示失败，云端做删除处理");

                    GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(subDevId);
                    if (deviceResp == null) {
                        log.debug("*****delDevResp() error! subDevId={} not in database!", subDevId);
                        return;
                    }
                    if (!directDeviceId.equals(deviceResp.getParentId())) {
                        log.debug("*****delDevResp() error! 当前deviceResp.getParentId() != directDeviceId, deviceResp.getParentId()={}, directDeviceId={}", deviceResp.getParentId(), directDeviceId);
                        return;
                    }

                    Long tenantId = deviceResp.getTenantId();
                    Long userId = deviceCoreService.getRootUserIdByDeviceId(tenantId, directDeviceId);
                    if (userId == null) {
                        log.debug("delDevResp() error not get userId by directDeviceId :{} and tenantId:{}", directDeviceId, tenantId);
                        return;
                    }
                    //删除相关数据
                    reportDeviceDetailsService.deleteMultiDeviceRelation(tenantId, Lists.newArrayList(subDevId),userId);
                    // 删除子设备
                    deviceCoreService.delChildDeviceByDeviceId(deviceResp.getTenantId(), subDevId, userId);

                    //7.恢复灯的默认属性
                    deviceStateService.recoveryDefaultState(tenantId, subDevId);
                    return;
                }
            }
            log.debug("delDevResp-success");
        } catch (RemoteCallBusinessException e) {
            log.debug("delDevResp-remote-error:{}", e);
        } catch (BusinessException e) {
            log.debug("delDevResp-local-error:{}", e);
        } catch (Exception e) {
            log.error("delDevResp-error:{}", e);
        }
    }

    public void delDevNotif(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("delDevNotif({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> delDevRespPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String subDevId = (String) delDevRespPayload.get("devId");
        String directDeviceId = MQTTUtils.parseReqTopic(reqTopic);
        MqttMsgAck delDevRespMsgAck = reqMqttMsg.getAck();
        try {
            if (delDevRespMsgAck == null) {
                log.debug("*****delDevNotif() error! because delDevRespMsgAck is null.");
                return;
            }
            if (delDevRespMsgAck.getCode() == MqttMsgAck.SUCCESS) {
                GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(subDevId);
                if (deviceResp == null) {
                    log.debug("*****delDevNotif() error! subDevId={} not in database!", subDevId);
                    return;
                }
                if (!directDeviceId.equals(deviceResp.getParentId())) {
                    log.debug("*****delDevNotif() error! 当前deviceResp.getParentId() != directDeviceId, deviceResp.getParentId()={}, directDeviceId={}", deviceResp.getParentId(), directDeviceId);
                    return;
                }
                Long tenantId = deviceResp.getTenantId();

                Long userId = deviceCoreService.getRootUserIdByDeviceId(tenantId, directDeviceId);
                if (userId == null) {
                    log.debug("delDevNotif() error not get userId by subDevId:{} and tenantId:{}", subDevId, tenantId);
                    return;
                }

                //删除相关数据
                reportDeviceDetailsService.deleteMultiDeviceRelation(tenantId, Lists.newArrayList(subDevId),userId);
                // 删除子设备
                deviceCoreService.delChildDeviceByDeviceId(deviceResp.getTenantId(), subDevId, userId);

                // 通知robot删除设备
                ApplicationContextHelper.getBean(DeviceDeleteSender.class).send(DeviceDeleteMessage.builder()
                        .tenantId(tenantId)
                        .deviceId(subDevId)
                        .userId(userId)
                        .build());

                //7.恢复灯的默认属性
                deviceStateService.recoveryDefaultState(tenantId, subDevId);
            }
            log.debug("delDevNotif-success");
        } catch (RemoteCallBusinessException e) {
            log.debug("delDevNotif-remote-error:{}", e);
        } catch (BusinessException e) {
            log.debug("delDevNotif-local-error:{}", e);
        } catch (Exception e) {
            log.error("delDevNotif-error:{}", e);
        }
    }

    public void getDevInfoReq(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("getDevInfoReq({}, {})", reqMqttMsg, reqTopic);

        MqttMsgAck ack = MqttMsgAck.successAck();
        Map<String, Object> respPayload = new HashMap<>();
        String userId = ToolUtils.getClientId(reqTopic);  // 获取主题中的userId
        try {
            Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
            String deviceId = String.valueOf(payload.get("devId"));
            respPayload = deviceService.buildDeviceInfoByDeviceId(SaaSContextHolder.currentTenantId(), deviceId);
            respPayload.put("userId", userId);
        } catch (RemoteCallBusinessException e) {
            log.debug("getDevInfoReq-remote-error.:{}", e);
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (BusinessException e) {
            log.debug("getDevInfoReq-local-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("getDevInfoReq-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.ERROR, DeviceCoreExceptionEnum.SYSTEM_ERROR.getMessageKey());
        } finally {
            reqMqttMsg.setMethod(DeviceMethodUtils.GET_DEV_INFO_RESP);
            reqMqttMsg.setAck(ack);
            reqMqttMsg.setPayload(respPayload);
            String respTopic = DeviceConstants.buildTopic(userId, DeviceMethodUtils.GET_DEV_INFO_RESP);
            reqMqttMsg.setTopic(respTopic);
            log.debug("getDevInfoResp({}, {})", reqMqttMsg, respTopic);
            mqttCoreService.sendMessage(reqMqttMsg);
        }
    }

    public void setDevInfoNotif(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("setDevInfoNotif({}, {})", reqMqttMsg, reqTopic);
        Long tenantId = SaaSContextHolder.currentTenantId();
        MqttMsgAck ack = MqttMsgAck.successAck();
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String userUuid = (String) payload.get("userId");
        Map<String, Object> respPayload = new HashMap<>();
        try {
            MqttMsgAck setDevInfoRespAck = reqMqttMsg.getAck();
            if (setDevInfoRespAck == null || setDevInfoRespAck.getCode() != MqttMsgAck.SUCCESS) {
                // 返回网关的失败信息
                ack = MqttMsgAck.failureAck(setDevInfoRespAck.getCode(), setDevInfoRespAck.getDesc());
                return;
            }

            String deviceUuid = String.valueOf(payload.get("devId"));

            FetchUserResp user = userApi.getUserByUuid(userUuid);
            Long userId = user.getId();
            //修改设备基础信息
            deviceService.setDevInfo(payload, userId, user.getTenantId());

            // 设备信息变更 通知第三方(alexa/googleHome)
            ApplicationContextHelper.getBean(DeviceAddOrUpdateSender.class).send(DeviceAddOrUpdateMessage.builder()
                    .tenantId(tenantId)
                    .userId(user.getId())
                    .deviceId(deviceUuid)
                    .newAdd(false)
                    .build());
        } catch (RemoteCallBusinessException e) {
            log.debug("setDevInfoNotif-remote-error.:{}", e);
            ack = MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (BusinessException e) {
            log.debug("setDevInfoNotif-local-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.BUSINESS_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("setDevInfoNotif-error:{}", e);
            ack = DeviceCoreUtils.buildLocaleByLocalMessage(MqttMsgAck.ERROR, DeviceCoreExceptionEnum.SYSTEM_ERROR.getMessageKey());
        } finally {
            reqMqttMsg.setMethod(DeviceMethodUtils.SET_DEV_INFO_RESP);
            reqMqttMsg.setAck(ack);
            reqMqttMsg.setPayload(respPayload);
            String respTopic = DeviceConstants.buildTopic(userUuid, DeviceMethodUtils.SET_DEV_INFO_RESP);
            reqMqttMsg.setTopic(respTopic);
            log.debug("setDevInfoNotif({}, {})", reqMqttMsg, respTopic);
            mqttCoreService.sendMessage(reqMqttMsg);

        }

    }

    public void statusNotify(MqttMsg reqMqttMsg, String reqTopic) {

    }

    public void connect(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("connect-device({}, {})", reqMqttMsg, reqTopic);
        String deviceId = MQTTUtils.parseReqTopic(reqTopic);

        try {
            Long tenantId = SaaSContextHolder.currentTenantId();
            Long lastLoginTime = null;
            // 暂时不加这个设备上线时间判断
            //UserOrDeviceConnectUtil.isUpdateStatus(deviceId, tenantId, reqMqttMsg);
//            if (lastLoginTime != null) {
            DeviceConnectMessage message = DeviceConnectMessage.builder().tenantId(tenantId).deviceId(deviceId).status(OnlineStatusEnum.CONNECTED.getCode()).lastLoginTime(lastLoginTime).build();
            deviceConnectSender.send(message);
            recordActivateInfo(deviceId);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("connect-device-error", e);
        }
    }

    private void recordActivateInfo(String deviceId) {
        if(StringUtil.isBlank(deviceId)){
            return;
        }
        try{
            RedisCacheUtil.setAdd(ModuleConstants.DEVONLINE,deviceId);
            RedisCacheUtil.setAdd(ModuleConstants.DEVACTIVE + CalendarUtil.getNowDate(),deviceId);
        }catch (Exception e){
            logger.error("",e);
        }
    }

    public void disconnect(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("disconnect({}, {})", reqMqttMsg, reqTopic);
        try {
            String deviceId = MQTTUtils.parseReqTopic(reqTopic);
            Long tenantId = SaaSContextHolder.currentTenantId();
            deviceDisconnectSender.send(DeviceDisconnectMessage.builder()
                    .deviceId(deviceId)
                    .tenantId(tenantId)
                    .status(OnlineStatusEnum.DISCONNECTED.getCode()).build());
            recordDisconnect(deviceId);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("disconnect-device-error", e);
        }
    }

    private void recordDisconnect(String deviceId) {
        if(StringUtil.isBlank(deviceId)){
            return;
        }
        try{
            RedisCacheUtil.setDel(ModuleConstants.DEVONLINE,deviceId);
        }catch (Exception e){
            logger.error("",e);
        }
    }

    public void updateDevOrder(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("updateDevOrder-message({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceIds = String.valueOf(payload.get("deviceIds"));
        //String 转list
        String[] deviceIdArray = deviceIds.split(",");

        //循环修改排序
        int index = 1;

        List<com.iot.shcs.space.vo.SpaceDeviceReq> spaceDeviceReqs = Lists.newArrayList();

        for (String id : deviceIdArray) {
            com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq = com.iot.shcs.space.vo.SpaceDeviceReq.builder()
                    .id(Long.parseLong(id))
                    .order(index)
                    .build();
            spaceDeviceReqs.add(spaceDeviceReq);
            index++;

        }
        if (!CollectionUtils.isEmpty(spaceDeviceReqs)) {
            spaceDeviceService.updateSpaceDevices(spaceDeviceReqs);
        }


    }

    public void setCountDownReq(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("setCountDownReq({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> setCountDownReqPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceId = (String) setCountDownReqPayload.get("devId");

        // 转发MQTT到设备
        String setCountDownReqToDevTopic = "iot/v1/c/" + deviceId + "/device/setCountDownReq";
        log.debug("setCountDownReq-转发MQTT到设备({}, {})", reqMqttMsg, setCountDownReqToDevTopic);
        reqMqttMsg.setTopic(setCountDownReqToDevTopic);

        mqttCoreService.sendMessage(reqMqttMsg);
    }

    public MqttMsg setCountDownResp(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("setCountDownResp({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> setCountDownRespPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String userId = (String) setCountDownRespPayload.get("userId");

        String[] split = reqTopic.split("/");
        String deviceId = split[3];

        // 转发到app
        String getCountDownRespToAppTopic = "iot/v1/c/" + userId + "/device/setCountDownResp";
        log.debug("setCountDownResp-转发到app({}, {})", reqMqttMsg, getCountDownRespToAppTopic);
        insertCountDown(deviceId, setCountDownRespPayload);
        reqMqttMsg.setTopic(getCountDownRespToAppTopic);
        mqttCoreService.sendMessage(reqMqttMsg);
        return reqMqttMsg;
    }

    public void setCountDownEnableReq(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("setCountDownEnableReq({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> setCountDownReqPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceId = (String) setCountDownReqPayload.get("devId");

        // 转发MQTT到设备
        String setCountDownEnableReqToDevTopic = "iot/v1/c/" + deviceId + "/device/setCountDownEnableReq";
        log.debug("setCountDownEnableReq-转发MQTT到设备({}, {})", reqMqttMsg, reqTopic);
        reqMqttMsg.setTopic(setCountDownEnableReqToDevTopic);
        mqttCoreService.sendMessage(reqMqttMsg);

    }

    public MqttMsg setCountDownEnableResp(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("setCountDownEnableResp({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> setCountDownEnableRespPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String userId = (String) setCountDownEnableRespPayload.get("userId");
        String[] split = reqTopic.split("/");
        String deviceId = split[3];

        // 转发到app
        String getCountDownEnableRespToAppTopic = "iot/v1/c/" + userId + "/device/setCountDownEnableResp";
        log.debug("setCountDownEnableResp-转发到app({}, {})", reqMqttMsg, getCountDownEnableRespToAppTopic);
        insertCountDown(deviceId, setCountDownEnableRespPayload);
        reqMqttMsg.setTopic(getCountDownEnableRespToAppTopic);

        mqttCoreService.sendMessage(reqMqttMsg);

        return reqMqttMsg;
    }

    public void updateEnergyReq(MqttMsg reqMqttMsg, String reqTopic) {
        log.info("updateEnergyReq({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> reqPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceId = reqTopic.split("/")[3];

        MqttMsgAck ack = new MqttMsgAck();
        ack.setCode(MqttMsgAck.SUCCESS);
        ack.setDesc("Success.");
        try {
            GetDeviceInfoRespVo deviceInfo = deviceCoreService.getDeviceInfoByDeviceId(deviceId);

            Integer step = reqPayload.get("step") != null ? (Integer) reqPayload.get("step") : null;
            Double electricValue = reqPayload.get("value") != null ? Double.valueOf(reqPayload.get("value").toString()) : null;
            Long runtimeValue = reqPayload.get("runtime") != null ? Long.valueOf(reqPayload.get("runtime").toString()) : null;
            String area = reqPayload.get("area") != null ? reqPayload.get("area").toString() : null;
            Date time = null;
            if (reqPayload.get("time") != null) {
                time = ToolUtils.timestampToDate(reqPayload.get("time").toString());
            }
            if (step == null || time == null || electricValue == null || runtimeValue == null || area == null) {
                ack.setCode(MqttMsgAck.BUSINESS_ERROR);
                ack.setDesc("Fail. Parameter is not complete.");
                return;
            }
            //时区转换
            ZoneId zoneId = TimeZone.getTimeZone(area).toZoneId();
            LocalDateTime timeReceive = LocalDateTime.now(zoneId);
            Date localTime = new Date(java.sql.Timestamp.valueOf(timeReceive).getTime());


            ElectricityStatisticsReq electrictyStatistics = new ElectricityStatisticsReq();
            electrictyStatistics.setDeviceId(deviceId);
            electrictyStatistics.setStep(step);
            electrictyStatistics.setTime(time);
            electrictyStatistics.setElectricValue(electricValue);
            Long tenantId = deviceInfo.getTenantId();
            Long orgId = SaaSContextHolder.getCurrentOrgId();
            electrictyStatistics.setTenantId(tenantId);
            electrictyStatistics.setOrgId(orgId);
            electrictyStatistics.setArea(area);
            electrictyStatistics.setLocaltime(localTime);

            RuntimeReq runtime = new RuntimeReq();
            runtime.setDeviceId(deviceId);
            runtime.setStep(step);
            runtime.setTime(time);
            runtime.setRuntime(runtimeValue);
            runtime.setTenantId(tenantId);
            runtime.setOrgId(orgId);
            runtime.setArea(area);
            runtime.setLocaltime(localTime);


            List<ListUserDeviceInfoRespVo> userDeviceList = deviceCoreService.listUserDevices(tenantId, deviceId);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userDeviceList)) {
                for (ListUserDeviceInfoRespVo userDevice : userDeviceList) {
                    if (!userDevice.getUserType().isEmpty() && userDevice.getUserType().equals("root")) {
                        Long userId = userDevice.getUserId();
                        electrictyStatistics.setUserId(userId);
                        runtime.setUserId(userId);
                        break;
                    }
                }
            } else {
                ack.setCode(MqttMsgAck.BUSINESS_ERROR);
                ack.setDesc("device is unbind");
                return;
            }
            Boolean electricityRes = electricityStatisticsApi.insertElectricityStatistics(electrictyStatistics);
            Boolean runtimeRes = electricityStatisticsApi.insertRuntime(runtime);
            if (electricityRes == false || runtimeRes == false) {
                ack.setCode(MqttMsgAck.BUSINESS_ERROR);
                ack.setDesc("Fail. insert ElectricityStatistics fail.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            ack.setCode(MqttMsgAck.BUSINESS_ERROR);
            ack.setDesc("Fail.updateEnergyReq-error. " + e);
            log.error("updateEnergyReq-error", e);
        }

        // 响应给设备
        finally {
            MqttMsg updateEnergyRespMsg = new MqttMsg();
            updateEnergyRespMsg.setService(reqMqttMsg.getService());
            updateEnergyRespMsg.setMethod("updateEnergyResp");
            updateEnergyRespMsg.setSeq(reqMqttMsg.getSeq());
            updateEnergyRespMsg.setSrcAddr(reqMqttMsg.getSrcAddr());
            Map<String, Object> respPayload = Maps.newHashMap();
            updateEnergyRespMsg.setPayload(respPayload);
            updateEnergyRespMsg.setAck(ack);

            String updateEnergyRespTopic = "iot/v1/c/" + deviceId + "/device/updateEnergyResp";
            log.debug("updateEnergyReq-转发MQTT到设备({}, {})", updateEnergyRespMsg, updateEnergyRespTopic);
            updateEnergyRespMsg.setTopic(updateEnergyRespTopic);

            mqttCoreService.sendMessage(updateEnergyRespMsg);
        }
    }

    public MqttMsgAck commEnergyProcess(String deviceId, Integer step, Date time, Double electricValue, String area) {
        MqttMsgAck ack = MqttMsgAck.successAck();
        if (step == null || time == null || electricValue == null || area == null) {
            ack.setCode(MqttMsgAck.BUSINESS_ERROR);
            ack.setDesc("Fail. Parameter is not complete.");
            return ack;
        }

        GetDeviceInfoRespVo deviceInfo = deviceCoreService.getDeviceInfoByDeviceId(deviceId);
        Long tenantId = deviceInfo.getTenantId();
        //时区转换
        ZoneId zoneId = TimeZone.getTimeZone(area).toZoneId();
        LocalDateTime timeReceive = LocalDateTime.now(zoneId);
        Date localTime = new Date(java.sql.Timestamp.valueOf(timeReceive).getTime());


        ElectricityStatisticsReq electrictyStatistics = new ElectricityStatisticsReq();
        electrictyStatistics.setDeviceId(deviceId);
        electrictyStatistics.setStep(step);
        electrictyStatistics.setTime(time);
        electrictyStatistics.setElectricValue(electricValue);

        Long orgId = SaaSContextHolder.getCurrentOrgId();
        electrictyStatistics.setTenantId(tenantId);
        electrictyStatistics.setOrgId(orgId);
        electrictyStatistics.setArea(area);
        electrictyStatistics.setLocaltime(localTime);


        List<ListUserDeviceInfoRespVo> userDeviceList = deviceCoreService.listUserDevices(tenantId, deviceId);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userDeviceList)) {
            for (ListUserDeviceInfoRespVo userDevice : userDeviceList) {
                if (!userDevice.getUserType().isEmpty() && userDevice.getUserType().equals("root")) {
                    Long userId = userDevice.getUserId();
                    electrictyStatistics.setUserId(userId);

                    break;
                }
            }
        } else {
            ack.setCode(MqttMsgAck.BUSINESS_ERROR);
            ack.setDesc("device is unbind");
            return ack;
        }
        Boolean electricityRes = electricityStatisticsApi.insertElectricityStatistics(electrictyStatistics);
        if (electricityRes == false) {
            ack.setCode(MqttMsgAck.BUSINESS_ERROR);
            ack.setDesc("Fail. insert ElectricityStatistics fail.");
        }
        return ack;
    }

    public void updateRuntimeReq(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("updateEnergyReq({}, {})", reqMqttMsg, reqTopic);
        Map<String, Object> reqPayload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceId = reqTopic.split("/")[3];

        MqttMsgAck ack = new MqttMsgAck();
        ack.setCode(MqttMsgAck.SUCCESS);
        ack.setDesc("Success.");
        try {
            Integer step = null;

            step = reqPayload.get("step") != null ? (Integer) reqPayload.get("step") : null;
            Long runtimeValue = null;
            runtimeValue = reqPayload.get("runtime") != null ? Long.valueOf(reqPayload.get("runtime").toString()) : null;
            Date time = null;
            if (reqPayload.get("time") != null) {
                time = ToolUtils.timestampToDate(reqPayload.get("time").toString());
            }

            ack = this.commRuntimeProcess(deviceId, step, time, runtimeValue);
        } catch (Exception e) {
            ack.setCode(MqttMsgAck.BUSINESS_ERROR);
            ack.setDesc("Fail.updateRuntimeReq-error. " + e);
            log.error("updateRuntimeReq-error", e);
        } finally {// 响应给设备
            MqttMsg updateRuntimeRespMsg = new MqttMsg();
            updateRuntimeRespMsg.setService(reqMqttMsg.getService());
            updateRuntimeRespMsg.setMethod("updateRuntimeResp");
            updateRuntimeRespMsg.setSeq(reqMqttMsg.getSeq());
            updateRuntimeRespMsg.setSrcAddr(reqMqttMsg.getSrcAddr());
            Map<String, Object> respPayload = Maps.newHashMap();
            updateRuntimeRespMsg.setPayload(respPayload);
            updateRuntimeRespMsg.setAck(ack);

            String updateRuntimeRespTopic = "iot/v1/c/" + deviceId + "/device/updateRuntimeResp";
            log.debug("updateRuntimeResp-转发MQTT到设备({}, {})", updateRuntimeRespMsg, updateRuntimeRespTopic);
            updateRuntimeRespMsg.setTopic(updateRuntimeRespTopic);

            mqttCoreService.sendMessage(updateRuntimeRespMsg);
        }
    }

    public MqttMsgAck commRuntimeProcess(String deviceId, Integer step, Date time, Long runtimeValue) {
        MqttMsgAck ack = MqttMsgAck.successAck();
        if (step == null || time == null || runtimeValue == null) {
            ack.setCode(MqttMsgAck.BUSINESS_ERROR);
            ack.setDesc("Fail. Parameter is not complete.");
            return ack;
        }
        GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreService.getDeviceInfoByDeviceId(deviceId);
        Long tenantId = deviceInfoRespVo.getTenantId();
        RuntimeReq runtime = new RuntimeReq();
        runtime.setDeviceId(deviceId);
        runtime.setStep(step);
        runtime.setTime(time);
        runtime.setRuntime(runtimeValue);

        Long orgId = SaaSContextHolder.getCurrentOrgId();
        runtime.setTenantId(tenantId);
        runtime.setOrgId(orgId);

        List<ListUserDeviceInfoRespVo> userDeviceList = deviceCoreService.listUserDevices(tenantId, deviceId);
        if (!CollectionUtils.isEmpty(userDeviceList)) {
            for (ListUserDeviceInfoRespVo userDevice : userDeviceList) {
                if (!userDevice.getUserType().isEmpty() && userDevice.getUserType().equals("root")) {
                    Long userId = userDevice.getUserId();
                    runtime.setUserId(userId);
                    break;
                }
            }
        } else {
            ack.setCode(MqttMsgAck.BUSINESS_ERROR);
            ack.setDesc("device is unbind");
            return ack;
        }
        Boolean res = electricityStatisticsApi.insertRuntime(runtime);
        if (res == false) {
            ack.setCode(MqttMsgAck.BUSINESS_ERROR);
            ack.setDesc("Fail. insert ElectricityStatistics fail.");
        }
        return ack;
    }

    public void devStautsNotif(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("devStautsNotif({}, {})", reqMqttMsg, reqTopic);
    }

    public void setDevAttrResp(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("setDevAttrResp({}, {})", reqMqttMsg, reqTopic);
    }


    /**
     * app -->cloud iot/v1/s/[userId]/device/devActionReq
     * <p>
     * cloud -->device  iot/v1/c/[devId]/device/devActionReq
     *
     * @param reqMqttMsg
     * @param reqTopic
     * @return
     * @author lucky
     * @date 2018/10/23 11:51
     */
    @Override
    public void devActionReq(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("devActionReq({}, {})", reqMqttMsg, reqTopic);
        reqMqttMsg.setMethod(DeviceMethodUtils.DEL_ACTION_REQ);
        MqttMsgAck ack = MqttMsgAck.successAck();
        reqMqttMsg.setAck(ack);
        reqMqttMsg.setPayload(reqMqttMsg.getPayload());
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceId = (String) payload.get("devId");
        String respTopic = DeviceConstants.buildTopic(deviceId, DeviceMethodUtils.DEL_ACTION_REQ);
        reqMqttMsg.setTopic(respTopic);
        log.debug("devActionReq-change({}, {})", reqMqttMsg, respTopic);
        mqttCoreService.sendMessage(reqMqttMsg);
    }

    /**
     * device --> cloud  iot/v1/s/[devId]/device/devActionResp
     * <p>
     * cloud --> app  iot/v1/c/[userId]/device/devActionResp
     *
     * @param reqMqttMsg
     * @param reqTopic
     * @return
     * @author lucky
     * @date 2018/10/23 11:51
     */
    @Override
    public void devActionResp(MqttMsg reqMqttMsg, String reqTopic) {
        log.debug("receive device devActionResp({}, {})", reqMqttMsg, reqTopic);
        reqMqttMsg.setMethod(DeviceMethodUtils.DEL_ACTION_RESP);
        MqttMsgAck ack = MqttMsgAck.successAck();
        reqMqttMsg.setAck(ack);
        reqMqttMsg.setPayload(reqMqttMsg.getPayload());
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceId = (String) payload.get("devId");
        GetDeviceInfoRespVo deviceInfo = deviceCoreService.getDeviceInfoByDeviceId(deviceId);
        if (deviceInfo == null) {
            log.debug("devActionResp receive not find device info.");
            return;
        }
        Long tenantId = deviceInfo.getTenantId();
        List<ListUserDeviceInfoRespVo> userDeviceList = deviceCoreService.listUserDevices(tenantId, deviceId);
        if (CollectionUtils.isEmpty(userDeviceList)) {
            log.debug("devActionResp not find userDevice info by tenantId:{}, deviceId:{}.", tenantId, deviceId);
            return;
        }
        Long userId = userDeviceList.get(0).getUserId();
        FetchUserResp userInfo = userApi.getUser(userId);
        if (userInfo == null) {
            log.debug("devActionResp not find userInfo info by userId:{}.", tenantId, deviceId);
            return;
        }
        String userUUID = userInfo.getUuid();

        String respTopic = DeviceConstants.buildTopic(userUUID, DeviceMethodUtils.DEL_ACTION_RESP);
        reqMqttMsg.setTopic(respTopic);
        log.debug("devActionResp to app({}, {})", reqMqttMsg, respTopic);
        mqttCoreService.sendMessage(reqMqttMsg);
    }

    /**
     * 删除 与设备关联的数据
     *
     * @param deviceUuid 设备uuid
     * @throws BusinessException
     */
    private void deleteDeviceRelationships(String deviceUuid) {
        try {
            GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(deviceUuid);
            if (deviceResp == null) {
                log.debug("deleteDeviceRelationships device not exist.{}", deviceUuid);
                return;
            }
            Long tenantId = deviceResp.getTenantId();
            boolean isDirectDevice = false;
            if (deviceResp.getIsDirectDevice() != null
                    && deviceResp.getIsDirectDevice().intValue() == Constants.IS_DIRECT_DEVICE) {
                isDirectDevice = true;
            }

            if (isDirectDevice) {
                // 网关删除所有安防信息
                securityMqttService.deleteSecurityInfo(tenantId, deviceUuid, deviceResp.getProductId());
            } else {
                //子设备只删除安防规则 “if” 中的设备
                securityMqttService.deleteSubDevSecurityInfo(tenantId, deviceUuid);
                //删除组成员中的子设备
                delDevFromGroupDetail(deviceResp);
            }

            // 解除设备与房间关系
            spaceDeviceService.deleteSpaceDeviceByDeviceId(tenantId, deviceResp.getUuid());

            // 解除设备与情景关系
            sceneDetailService.deleteSceneDetailByDeviceId(deviceResp.getUuid(), deviceResp.getTenantId());


            log.debug("delete-ifttt-deviceId:{}", deviceResp.getId());
            // 解除设备与IFTTT关系
            autoService.delByDeviceId(deviceUuid, deviceResp.getTenantId());
            if (isDirectDevice) {
                autoService.delByDirectDeviceId(deviceResp.getUuid(), deviceResp.getTenantId());
            }
            //软删除活动记录
            ActivityRecordReq recordReq = new ActivityRecordReq();
            recordReq.setForeignId(deviceUuid);
            activityRecordApi.delActivityRecord(recordReq);
        } catch (Exception e) {
            log.error("deleteDeviceRelationships-error--->", e);
        }
    }

    private void delDevFromGroupDetail(GetDeviceInfoRespVo deviceResp){
        try {
            String deviceUuid=deviceResp.getUuid();
            Long tenantId=deviceResp.getTenantId();
            logger.info("------delDevFromGroupMember deviceUuid={},tenantId={}",deviceUuid,tenantId);
            //防止上层传递过来的deviceUuid为空导致删除所有的tenantId为2的数据
            if(StringUtil.isEmpty(deviceUuid)){
                return;
            }
            UpdateGroupDetailReq updateGroupDetailReq=new UpdateGroupDetailReq();
            updateGroupDetailReq.setTenantId(tenantId);
            updateGroupDetailReq.setDeviceId(deviceUuid);
            Boolean isDel=groupApi.delGroupDetial(updateGroupDetailReq);
            logger.info("------delDevFromGroupMember  isDel={}",isDel);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("------delDevFromGroupMember erro");
        }
    }
    private void updateDeviceResetAndUnBindFlag(Long tenantId, String devUUid, Boolean resetFlag) {

        UpdateDeviceExtendReq deviceExtendReq = new UpdateDeviceExtendReq();
        deviceExtendReq.setDeviceId(devUUid);
        deviceExtendReq.setArea("");//设置为null
        if (resetFlag != null && resetFlag) {
            deviceExtendReq.setResetFlag(1);
            deviceExtendReq.setFirstUploadSubDev(1);
        } else {
            //不恢复出产设置 需要重置那些 ifttt配置吗？ 目前重置造成IOT-15636 bug
            deviceExtendReq.setUnbindFlag(1);
//            deviceExtendReq.setResetFlag(0);
//            deviceExtendReq.setUnbindFlag(0);
        }
        deviceExtendReq.setTenantId(tenantId);
        deviceCoreService.saveOrUpdateExtend(deviceExtendReq);
    }

    private void sendNotifyMsg(String topicId, String seq, String srcAddr, MqttMsgAck ack) {
        // 发送响应消息
        Map<String, Object> payload = Maps.newHashMap();
        payload.put("timestamp", DeviceConstants.SIMPLE_DATE_FORMAT.format(new Date()));

        String respTopic = DeviceConstants.buildTopic(topicId, DeviceMethodUtils.DEV_UNBIND_RESP);
        MqttMsg msg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE, DeviceMethodUtils.DEV_UNBIND_RESP, respTopic, payload, ack, seq, MQTTUtils.DEFAULT_CLOUD_SOURCE_ADDR);
        log.debug("sendNotifyMsg-响应到app({}, {})", msg, respTopic);
        mqttCoreService.sendMessage(msg);
    }

    public void buildIPC(Map<String, Object> payload, GetDeviceTypeInfoRespVo deviceTypeResp, String deviceId) {
        if (null != deviceTypeResp && ModuleConstants.IPC_TYPE_NAME.equals(deviceTypeResp.getType())) {

            String planId = videoManageApi.getPlanId(deviceId);
            if (null != planId && !"".equals(planId)) {
                List<Map<String, Object>> taskMapList = new ArrayList<>();
                Map<String, Object> recordConfigMap = new HashMap<>();
                List<VideoPlanTaskDto> taskDetailList = videoManageApi.getSyncTaskInfo(planId);
                if (null != taskDetailList && taskDetailList.size() > 0) {
                    VideoPlanTaskDto videoPlanTaskDto = taskDetailList.get(0);
                    boolean planExecStatus = videoPlanTaskDto.getPlanExecStatus() != 0;
                    boolean taskEnable = videoPlanTaskDto.getPlanStatus() != 0;
                    String recordMethod = videoPlanTaskDto.getPackageType() == 0 ? "all" : "event";
                    recordConfigMap.put("recordEnable", planExecStatus);
                    recordConfigMap.put("taskEnable", taskEnable);
                    recordConfigMap.put("recordMethod", recordMethod);
                    recordConfigMap.put("planId", planId);
                    recordConfigMap.put("planCycle", videoPlanTaskDto.getPlanCycle());
                    for (VideoPlanTaskDto taskDetail : taskDetailList) {
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("startTime", taskDetail.getExecuteStartTime());
                        taskMap.put("endTime", taskDetail.getExecuteEndTime());
                        taskMap.put("dayIndex", StringUtil.isEmpty(taskDetail.getTaskDate()) ? 0 : Integer.parseInt(taskDetail.getTaskDate()));
                        taskMapList.add(taskMap);
                        recordConfigMap.put("taskList", taskMapList);
                    }
                    payload.put("recordConfig", recordConfigMap);
                }
            }
            Map<String, Object> sysConfig = Maps.newHashMap();
            sysConfig.put("preRecordLen", ipcPropertiesConfig.getPreRecordLen());
            sysConfig.put("evtIntervalLen", ipcPropertiesConfig.getEvtIntervalLen());
            sysConfig.put("evtRecordLen", ipcPropertiesConfig.getEvtRecordLen());
            sysConfig.put("segmentLen", ipcPropertiesConfig.getSegmentLen());
            payload.put("sysConfig", sysConfig);
        }
    }

    /**
     * 构建wifiPlugs
     *
     * @param payload
     * @param deviceTypeResp
     */
    public void buildWifiPlugs(Map<String, Object> payload, GetDeviceTypeInfoRespVo deviceTypeResp) {
        if (deviceTypeResp != null && "wifi_plug".equals(deviceTypeResp.getType())) {
            List<ConfigurationRsp> configList = Lists.newArrayList();
            List<Map<String, Object>> reportList = Lists.newArrayList();
            Long tenantId = SaaSContextHolder.currentTenantId();
            //从configuration表中读取配置
            configList = configurationApi.selectConfigByTenantId(tenantId);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(configList)) {
                Map<String, Object> reportMap = Maps.newHashMap();
                reportMap.put("reportType", "plug_energy_report");
                for (ConfigurationRsp config : configList) {
                    String param = config.getParam();
                    if ("interval".equals(param)) {
                        String valueStr = config.getValue();
                        JSONObject jsonObject = JSONObject.parseObject(valueStr);
                        Long value = jsonObject.getString("value") != null ? Long.parseLong(jsonObject.getString("value")) : null;
                        reportMap.put("interval", value);
                    }
                    if ("delay".equals(param)) {
                        String valueStr = config.getValue();
                        JSONObject jsonObject = JSONObject.parseObject(valueStr);
                        Long value = jsonObject.getString("value") != null ? Long.parseLong(jsonObject.getString("value")) : null;
                        reportMap.put("delay", value);
                    }

                }
                reportList.add(reportMap);
                payload.put("report", reportList);
            }
        }
    }

    private void removeDirectlyDevice(String deviceUUId) {
        GetDeviceInfoRespVo deviceInfo = deviceCoreService.getDeviceInfoByDeviceId(deviceUUId);
        Long tenantId = deviceInfo.getTenantId();
        FetchUserResp user = null;
        List<ListUserDeviceInfoRespVo> userDeviceList = deviceCoreService.listUserDevices(tenantId, deviceUUId);//only one relationship
        log.debug("********updateDevBaseInfo-ListUserDeviceInfoRespVo "+JSON.toJSONString(userDeviceList));
        if (!CollectionUtils.isEmpty(userDeviceList)) {
            for (ListUserDeviceInfoRespVo ud : userDeviceList) {
                Long targetUserId = ud.getUserId();

                user = userApi.getUser(targetUserId);
                String userId = user.getUuid();

                log.info("updateDevBaseInfo-del-unBoundRelationship by userId:{},deviceId:{}", userId, deviceUUId);
                deviceCoreService.delChildDeviceByDeviceId(tenantId, deviceUUId, user.getId());
                mqttCoreService.separationAcls(tenantId, user.getId(), userId, deviceUUId);

                //add 20181107 by xfz 还要触发 如果同一个租户下不同账户给绑定了 要通知app前一个账号进行移除
                String respTopic = DeviceConstants.buildTopic(userId, DeviceMethodUtils.DEV_UNBIND_NOTIF);

                Map<String, Object> payloadMap = Maps.newHashMap();
                payloadMap.put("devId", deviceUUId);
                payloadMap.put("userId", userId);
                payloadMap.put("restFlag", 1);
                payloadMap.put("timestamp", new Date().getTime());
                payloadMap.put("devName", deviceInfo.getName());

                MqttMsg synDevBasicConfigMsg = DeviceCoreUtils.buildMqttMsg(DeviceMethodUtils.DEV_SERVICE
                        , DeviceMethodUtils.DEV_UNBIND_NOTIF, respTopic,
                        payloadMap, MqttMsgAck.successAck(), UUID.randomUUID().toString(), MQTTUtils.DEFAULT_CLOUD_SOURCE_ADDR);

                log.debug("updateDevBaseInfo-del-devUnbindNotif userId:{},unbind deviceId:{}", userId, deviceUUId);
                mqttCoreService.sendMessage(synDevBasicConfigMsg);

            }
        }

        log.info("updateDevBaseInfo-del-deleteDeviceRelationships by deviceId:{}", deviceUUId);
        List<String> childDeviceIds = deviceCoreService.listDeviceIdsByParentId(deviceUUId);
        if (!CollectionUtils.isEmpty(childDeviceIds)) {
            reportDeviceDetailsService.deleteMultiDeviceRelation(tenantId, childDeviceIds,user.getId());
            deviceCoreService.delBatchSubDeviceId(tenantId, user.getId(), childDeviceIds);
        }

        log.info("***** end remove directly device, deviceId: {}", deviceUUId);
    }


    private void insertCountDown(String deviceId, Map<String, Object> payloadMap) {
        if (CollectionUtils.isEmpty(payloadMap)) {
            return;
        }

        String userId = (String) payloadMap.get("userId");
        Long countdown = Long.parseLong(payloadMap.get("countDown").toString());
        Integer enable = payloadMap.get("enable") != null ? (Integer) payloadMap.get("enable") : DeviceConstants.DISABLE;
        CountDownReq countDownReq = new CountDownReq();

        FetchUserResp user = userApi.getUserByUuid(userId);
        countDownReq.setTenantId(user.getTenantId());
        countDownReq.setUserId(user.getId());
        countDownReq.setDeviceId(deviceId);
        countDownReq.setCountdown(countdown);
        countDownReq.setIsEnable(enable);
        countdownCoreApi.addCountDown(countDownReq);
    }
}
