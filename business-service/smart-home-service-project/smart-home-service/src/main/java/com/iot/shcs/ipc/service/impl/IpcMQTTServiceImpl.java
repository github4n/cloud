package com.iot.shcs.ipc.service.impl;

import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ExceptionEnum;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.device.vo.req.UpdateUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.device.vo.resp.UpdateUserDeviceInfoResp;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.file.api.FileApi;
import com.iot.file.api.VideoFileApi;
import com.iot.file.dto.FileDto;
import com.iot.file.vo.FileInfoRedisVo;
import com.iot.locale.LocaleMessageSourceService;
import com.iot.message.api.MessageApi;
import com.iot.message.enums.MessageTempType;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.redis.RedisCacheUtil;
import com.iot.shcs.contants.AppPushMessageKey;
import com.iot.shcs.contants.ModuleConstants;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.helper.DispatcherRouteHelper;
import com.iot.shcs.ipc.exception.BusinessExceptionEnum;
import com.iot.shcs.ipc.service.IpcMQTTService;
import com.iot.shcs.ipc.util.CommonUtil;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.tenant.api.UserVirtualOrgApi;
import com.iot.tenant.vo.resp.UserDefaultOrgInfoResp;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.util.ToolUtils;
import com.iot.video.api.VideoManageApi;
import com.iot.video.api.VideoPlanApi;
import com.iot.video.dto.VideoPlanTaskDto;
import com.iot.video.entity.VideoEvent;
import com.iot.video.entity.VideoFile;
import com.iot.video.vo.redis.RedisVideoPlanInfoVo;
import com.iot.video.vo.resp.CheckBeforeUploadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 项目名称：IOT云平台
 * 模块名称：IPC操作接口
 * 功能描述：IPC操作接口
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月24日 11:34
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月24日 11:34
 */
@Service("ipcMQTTService")
public class IpcMQTTServiceImpl implements IpcMQTTService, CallBackProcessor {

    /**
     * 日志
     */
    private static final Logger loger = LoggerFactory.getLogger(IpcMQTTServiceImpl.class);

    private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int QOS = 1;
    /**
     * 线程池
     */
    private static ExecutorService excutor = Executors.newCachedThreadPool();
    @Autowired
    private MqttSdkService messageService;
    @Autowired
    private FileApi fileApi;

    @Autowired
    private DeviceCoreService deviceCoreService;

    @Autowired
    private UserApi userApi;
    @Autowired
    private MessageApi messageApi;
    @Autowired
    private ActivityRecordApi activityRecordApi;
    @Autowired
    private UserVirtualOrgApi userVirtualOrgApi;
    /**
     * 视频管理API
     */
    @Autowired
    private VideoManageApi videoManageApi;

    @Autowired
    private VideoPlanApi videoPlanApi;

    @Autowired
    private VideoFileApi videoFileApi;

    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;

    @Value("${rate.device-event-notice-app}")
    private Long deviceEventNoticeAppRate;

    @Override
    public void getfsurlReq(MqttMsg mqttMsgParam, String topicParam) {
        // 定义响应map
        Map<String, Object> responseMap = new HashMap<String, Object>();
        Map<String, Object> payloadMap = (Map<String, Object>) JsonUtil.convertJsonStrToMap(mqttMsgParam.getPayload().toString());
        // 重新组装返回值
        MqttMsg mqttMsg = new MqttMsg();
        mqttMsg.setMethod("getfsurlResp");
        mqttMsg.setSrcAddr("cloud");
        mqttMsg.setService(mqttMsgParam.getService());
        mqttMsg.setSeq(mqttMsgParam.getSeq());
        String deviceId = CommonUtil.getDeviceOrUserIdIdByTopic(mqttMsgParam.getTopic());
        String topic = "iot/v1/c/" + deviceId + "/getfsurlResp";
        mqttMsg.setTopic(topic);
        try {
            //计划是否为空
            String planId = (String) payloadMap.get("planId");
            if (StringUtil.isBlank(planId)) {
                loger.debug("getfsurlReq error-> planId : " + planId + " is blank");
                MqttMsgAck mqttMsgAck = new MqttMsgAck(BusinessExceptionEnum.ERROR.getCode(), "planId is empty");
                mqttMsg.setAck(mqttMsgAck);
                messageService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
                return;
            }
            //校验设备是否已绑定计划、计划是否过期——防止解绑之后IPC还上传数据或者计划过期没通知到ipc停止录影
            CheckBeforeUploadResult result = videoPlanApi.checkBeforeUpload(planId, deviceId);
            if (!result.isPaasFlag()) {
                loger.debug("getfsurlReq fail-> {}", result.getDesc());
                MqttMsgAck mqttMsgAck = new MqttMsgAck(BusinessExceptionEnum.AUTH_ERROR.getCode(), result.getDesc());
                mqttMsg.setAck(mqttMsgAck);
                messageService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
                return;
            }
            //根据设备ID去查租户ID
            GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(deviceId);
            Long tenantId = deviceResp.getTenantId();
            String fileType = payloadMap.get("fileType").toString();
            FileDto fileDto = videoFileApi.getUploadUrl(tenantId, planId, fileType);
            responseMap.put("url", fileDto.getPresignedUrl());
            responseMap.put("timestamp", SIMPLE_DATE_FORMAT.format(new Date()));
            responseMap.put("fileType", fileType);
            responseMap.put("fileName", fileDto.getFileName());
            mqttMsg.setPayload(responseMap);
            MqttMsgAck mqttMsgAck = new MqttMsgAck(BusinessExceptionEnum.SUCCESS.getCode(), BusinessExceptionEnum.SUCCESS.getMessageKey());
            mqttMsg.setAck(mqttMsgAck);
            //云端响应
            messageService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        } catch (Exception e) {
            loger.error("getfsurlReq error-> planId : " + payloadMap.get("planId"), e);
            MqttMsgAck mqttMsgAck = new MqttMsgAck(BusinessExceptionEnum.ERROR.getCode(), BusinessExceptionEnum.ERROR.getMessageKey());
            mqttMsg.setAck(mqttMsgAck);
            messageService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }

    }

    /**
     * 描述：获取文件服务put预签名url
     *
     * @param deviceId
     * @param planId
     * @param fileType
     * @return
     * @author 李帅
     * @created 2018年7月16日 下午8:06:40
     * @since
     */
    @Override
    public Map<String, Object> getFilePreSignUrls(String deviceId, String planId, String fileType) {
        // 定义响应map
        Map<String, Object> responseMap = new HashMap<String, Object>();
        try {
            //计划是否为空
            if (StringUtil.isBlank(planId)) {
                loger.debug("getfsurlReq error-> planId : " + planId + " is blank");
                throw new BusinessException(BusinessExceptionEnum.PLANID_IS_NULL);
            }
            //校验设备是否已绑定计划、计划是否过期——防止解绑之后IPC还上传数据或者计划过期没通知到ipc停止录影
            CheckBeforeUploadResult result = videoPlanApi.checkBeforeUpload(planId, deviceId);
            if (!result.isPaasFlag()) {
                loger.debug("getfsurlReq fail-> {}", result.getDesc());
                throw new BusinessException(BusinessExceptionEnum.DEVICEID_IS_INCORRECT);
            }
            //根据设备ID去查租户ID
            GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(deviceId);
            Long tenantId = deviceResp.getTenantId();
            FileDto fileDto = videoFileApi.getUploadUrl(tenantId, planId, fileType);
            responseMap.put("url", fileDto.getPresignedUrl());
            responseMap.put("timestamp", SIMPLE_DATE_FORMAT.format(new Date()));
            responseMap.put("fileType", fileType);
            responseMap.put("fileName", fileDto.getFileName());
        } catch (Exception e) {
            loger.error("getFilePreSignUrls error-> planId : " + planId, e);
        }
        return responseMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void uploadFileInfo(MqttMsg mqttMsgParam, String topicParam) {
        try {
            VideoFile videoFile = new VideoFile();
            //设备id
            String deviceId = CommonUtil.getDeviceOrUserIdIdByTopic(mqttMsgParam.getTopic());
            videoFile.setDeviceId(deviceId);
            //文件信息
            Map<String, Object> map = JsonUtil.convertJsonStrToMap(mqttMsgParam.getPayload().toString());
            Map<String, Object> fileInfoMap = (Map<String, Object>) map.get("file");
            //计划id
            videoFile.setPlanId(map.get("planId") == null ? "" : map.get("planId").toString());
            //文件名
            String fileName = fileInfoMap.get("fn").toString();
            String fileId = null;
            //获取文件信息
            FileInfoRedisVo vo = null;
            if (StringUtil.isNotEmpty(fileName)) {
                fileId = fileName.substring(0, fileName.lastIndexOf("."));
                videoFile.setFileId(fileId);
                vo = videoFileApi.getFileInfoFromRedis(fileId);
                //文件信息不存在的话则不保存
                if (vo == null) {
                    loger.debug("this video file info is expire, so don't save -> data:" + JsonUtil.toJson(fileInfoMap));
                    return;
                }
                videoFile.setFileType(vo.getFileType());
                videoFile.setFilePath(vo.getFilePath());
                videoFile.setTenantId(vo.getTenantId());
            }
            //校验设备是否已绑定计划、计划是否过期——防止解绑之后IPC还上传数据或者计划过期没通知到ipc停止录影
            CheckBeforeUploadResult result = videoPlanApi.checkBeforeUpload(videoFile.getPlanId(), deviceId);
            if (!result.isPaasFlag()) {
                loger.debug("uploadFileInfo fail -> desc: {} ,filePath : {}", result.getDesc(), videoFile.getFilePath());
                //解绑期间IPC还上传视频，在这里清理掉
                fileApi.deleteObjectByPath(videoFile.getFilePath());
                return;
            }
            //事件id
            videoFile.setEventUuid(fileInfoMap.get("evtId") == null ? "" : fileInfoMap.get("evtId").toString());
            //开始录影时间
            if (null != fileInfoMap.get("startTime") && !"".equals(fileInfoMap.get("startTime"))) {
                try {
                    videoFile.setVideoStartTime(ToolUtils.stringToDate(fileInfoMap.get("startTime").toString(), "yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e) {
                    loger.error("uploadFileInfo ParseException", e);
                }
            }
            //视频时长
            if (fileInfoMap.containsKey("length") && StringUtil.isNotEmpty(fileInfoMap.get("length").toString())) {
                videoFile.setVideoLength(Float.parseFloat(fileInfoMap.get("length").toString()));
            }
            //文件大小
            videoFile.setFileSize(Integer.parseInt(fileInfoMap.get("size").toString()));
            //文件拍摄角度
            videoFile.setRotation(fileInfoMap.get("rotation") == null ? 0 : Integer.parseInt(fileInfoMap.get("rotation").toString()));
            videoManageApi.createVideoFile(videoFile);
            //把redis中的缓存文件信息删除
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH");
            videoFileApi.deleteFileInfoFromRedis(fileId, dateFormat.format(vo.getCreateTime()));
            loger.debug("deviceId-> " + deviceId + "," + "fileId-> " + videoFile.getFileId() + "  PlanId->" + videoFile.getPlanId());
        } catch (Exception e) {
            loger.error("uploadFileInfo error->", e);
        }

    }

    @Override
    public void uploadEvent(MqttMsg mqttMsgParam, String topicParam) {
        try {
            Map<String, Object> payloadMap = JsonUtil.convertJsonStrToMap(mqttMsgParam.getPayload().toString());
            String deviceId = CommonUtil.getDeviceOrUserIdIdByTopic(mqttMsgParam.getTopic());
            VideoEvent videoEvent = new VideoEvent();
            //事件id
            videoEvent.setEventUuid(payloadMap.get("evtId").toString());
            //事件触发时间
            videoEvent.setEventOddurTime(ToolUtils.stringToDate(payloadMap.get("triggerTime").toString(), "yyyy-MM-dd HH:mm:ss"));
            //事件类型
            videoEvent.setEventCode(payloadMap.get("evtType").toString());
            //计划id
            videoEvent.setPlanId(payloadMap.get("planId") == null ? "" : payloadMap.get("planId").toString());
            //事件名称
            videoEvent.setEventName(payloadMap.get("devName") == null ? "" : payloadMap.get("devName").toString());
            if (StringUtil.isNotBlank(videoEvent.getPlanId())) {
                boolean addEventFlag = true;
                //校验设备是否已绑定计划、计划是否过期——防止解绑之后IPC还上传数据或者计划过期没通知到ipc停止录影
                CheckBeforeUploadResult result = videoPlanApi.checkBeforeUpload(videoEvent.getPlanId(), deviceId);
                if (!result.isPaasFlag()) {
                    loger.debug("insertEvent fail -> {}", result.getDesc());
                    addEventFlag = false;
                }
                if (addEventFlag) {
                    //有绑定计划在插入video_event
                    videoManageApi.insertVideoEvent(videoEvent);
                }
            }
            GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(deviceId);
            Long tenantId = deviceResp.getTenantId();
            //添加活动记录
            //1、先查找设备信息，绑定的主账号(未绑定的话直接结束)
            Long rootUserId = deviceCoreService.getRootUserIdByDeviceId(tenantId, deviceId);
            if (rootUserId == null) {
               return;
            }
            //封装活动日志
            ActivityRecordReq activityRecordReq = new ActivityRecordReq();
            activityRecordReq.setCreateBy(rootUserId);
            activityRecordReq.setType(Constants.ACTIVITY_RECORD_DEVICE);
            activityRecordReq.setIcon(deviceResp.getIcon());
            activityRecordReq.setDeviceName(deviceResp.getName());
            String language = LocaleContextHolder.getLocale().toString();
            if ("zh_CN".equals(language)) {
                activityRecordReq.setActivity("IPC移动侦测");
            } else {
                activityRecordReq.setActivity("IPC Motion Detection");
            }
            activityRecordReq.setForeignId(deviceId);
            UserDefaultOrgInfoResp userOrgInfoResp = userVirtualOrgApi.getDefaultUsedOrgInfoByUserId(rootUserId);
            if (userOrgInfoResp != null) {
                activityRecordReq.setTenantId(userOrgInfoResp.getTenantId());
                activityRecordReq.setOrgId(userOrgInfoResp.getId());
            }
            List<ActivityRecordReq> list = new ArrayList<>();
            list.add(activityRecordReq);
            activityRecordApi.saveActivityRecord(list);
            //封装推送至app消息内容
            Map<String, String> noticMap = null;
            //推送至手机(一分钟内只能推送一次，用redis记录)
            String noticeAppKey = ModuleConstants.UPLOADEVENT_NOTICE_APP + deviceId;
            String hasSendFlag = RedisCacheUtil.valueGet(noticeAppKey);
            if (StringUtil.isBlank(hasSendFlag)) {
                noticMap = new HashMap<>();
                RedisCacheUtil.valueSet(noticeAppKey, "hasSend", deviceEventNoticeAppRate);
                //查询设备的p2pId
                GetDeviceExtendInfoRespVo deviceExtendInfo = deviceCoreService.getDeviceExtendByDeviceId(tenantId, deviceId);
                String p2pId = deviceExtendInfo.getP2pId();
                noticMap.put("templateId", MessageTempType.IOS00001.getTempId());   //这里暂时是测试代码，具体模板未定
                String appPushMessageKey= AppPushMessageKey.IPC_DETECTED_ACTIVITY;
                noticMap.put("message", localeMessageSourceService.getMessage(appPushMessageKey, new Object[]{deviceResp.getName()}));
                //推送给app的消息参数
                Map<String, Object> paramMap = new HashMap<>();
                //paramMap.put("eventType",StringUtil.join(getEventTypeName(videoEvent.getEventCode()),","));
                paramMap.put("deviceId", deviceId);
                paramMap.put("p2pId", p2pId);
                // paramMap.put("eventOddurTime",videoEvent.getEventOddurTime());
                noticMap.put("customDictionary", JsonUtil.toJson(paramMap).toString());
            }


            //系统推送
            asySaveInform(videoEvent, deviceId, noticMap);
        } catch (Exception e) {
            loger.error("uploadEvent error:", e);
        }
    }

    @Override
    public void notifyDeviceRecordConfig(final String planId, final String deviceId) {
        excutor.submit(new Runnable() {
            public void run() {
                MqttMsg mqttMsg = new MqttMsg();
                mqttMsg.setMethod("setCloudRecordConfig");
                mqttMsg.setSrcAddr("cloud");
                mqttMsg.setService("IPC");
                mqttMsg.setSeq(StringUtil.getRandomNumber(10));
                try {
                    String devId = deviceId;
                    if (StringUtil.isBlank(deviceId)) {
                        devId = videoManageApi.getDeviceId(planId);
                        if (StringUtil.isEmpty(devId)) {
                            return;
                        }
                    }
                    String topic = "iot/v1/c/" + devId + "/setCloudRecordConfig";
                    Map<String, Object> responseMap = new HashMap<String, Object>();

                    Map<String, Object> recordConfigMap = new HashMap<String, Object>();
                    List<Map<String, Object>> taskMapList = new ArrayList<Map<String, Object>>();

                    if (null != planId && !"".equals(planId)) {
                        List<VideoPlanTaskDto> taskDetailList = videoManageApi.getSyncTaskInfo(planId);
                        if (null != taskDetailList && taskDetailList.size() > 0) {
                            VideoPlanTaskDto videoPlanTaskDto = taskDetailList.get(0);
                            boolean planExecStatus = videoPlanTaskDto.getPlanExecStatus() == 1;
                            boolean taskEnable = videoPlanTaskDto.getPlanStatus() != 0;
                            //套餐类型 录影类型，事件录影或全时录影 0:all 1:event
                            String recordMethod = videoPlanTaskDto.getPackageType() == 0 ? "all" : "event";
                            int account = videoPlanTaskDto.getAmount() == null ? 0 : videoPlanTaskDto.getAmount();
                            RedisVideoPlanInfoVo redisVideoPlanInfoVo = new RedisVideoPlanInfoVo(planId, deviceId, videoPlanTaskDto.getPackageType(), account);
                            videoPlanApi.updateVideoPlanInfoOfRedis(redisVideoPlanInfoVo);
                            recordConfigMap.put("planId", planId);
                            recordConfigMap.put("recordEnable", planExecStatus);
                            recordConfigMap.put("taskEnable", taskEnable);
                            recordConfigMap.put("recordMethod", recordMethod);
                            recordConfigMap.put("planCycle", videoPlanTaskDto.getPlanCycle());
                            if (taskEnable) {
                                Map<String, Object> taskMap = null;
                                for (VideoPlanTaskDto taskDetail : taskDetailList) {
                                    taskMap = new HashMap<String, Object>();
                                    taskMap.put("dayIndex", StringUtil.isEmpty(taskDetail.getTaskDate()) ? 0 : Integer.parseInt(taskDetail.getTaskDate()));
                                    taskMap.put("startTime", taskDetail.getExecuteStartTime());
                                    taskMap.put("endTime", taskDetail.getExecuteEndTime());
                                    taskMapList.add(taskMap);
                                }
                                recordConfigMap.put("taskList", taskMapList);
                            }
                            responseMap.put("recordConfig", recordConfigMap);
                        }
                    }
                    mqttMsg.setPayload(responseMap);

                    MqttMsgAck mqttMsgAck = new MqttMsgAck(BusinessExceptionEnum.SUCCESS.getCode(), BusinessExceptionEnum.SUCCESS.getMessageKey());
                    mqttMsg.setAck(mqttMsgAck);
                    //云端响应
                    mqttMsg.setTopic(topic);
                    messageService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
                } catch (Exception e) {
                    loger.error("", e);
                    throw new BusinessException(BusinessExceptionEnum.NOTIFY_DEVICE_FAILED, e);
                }
            }
        });
    }


    @Override
    public void getEventNotifReq(MqttMsg mqttMsgParam, String topicParam) {
        // 重新组装返回值
        MqttMsg mqttMsg = new MqttMsg();
        mqttMsg.setService(mqttMsgParam.getService());
        mqttMsg.setMethod("getEventNotifResp");
        mqttMsg.setSeq(mqttMsgParam.getSeq());
        mqttMsg.setSrcAddr("cloud");

        String userUuId = CommonUtil.getDeviceOrUserIdIdByTopic(mqttMsgParam.getTopic());
        String topic = "iot/v1/c/" + userUuId + "/getEventNotifResp";
        mqttMsg.setTopic(topic);

        try {
            Map<String, Object> responseMap = new HashMap<String, Object>();
            Map<String, Object> payloadMap = (Map<String, Object>) JsonUtil.convertJsonStrToMap(mqttMsgParam.getPayload().toString());
            //用户uuid
            FetchUserResp user = userApi.getUserByUuid(userUuId);
            Long userId = user.getId();
            Long tenantId = user.getTenantId();

            String deviceId = (String) payloadMap.get("devId");
            //事件通知使能（0：开启，1：关闭）
            Integer eventNotifEnabledVal = 0;

            List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = deviceCoreService.listUserDevices(tenantId, userId, deviceId);
            if (!CollectionUtils.isEmpty(userDeviceInfoRespVoList)) {
                eventNotifEnabledVal = userDeviceInfoRespVoList.get(0).getEventNotifyEnabled();
            }

            boolean eventNotifEnabled = false;
            if (null != eventNotifEnabledVal) {
                if (0 == eventNotifEnabledVal) {
                    eventNotifEnabled = true;
                }
            }
            responseMap.put("eventNotifEnabled", eventNotifEnabled);
            responseMap.put("timestamp", SIMPLE_DATE_FORMAT.format(new Date()));
            mqttMsg.setPayload(responseMap);

            MqttMsgAck mqttMsgAck = new MqttMsgAck(BusinessExceptionEnum.SUCCESS.getCode(), BusinessExceptionEnum.SUCCESS.getMessageKey());
            mqttMsg.setAck(mqttMsgAck);
            messageService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        } catch (Exception e) {
            loger.error("error->", e);
            MqttMsgAck mqttMsgAck = new MqttMsgAck(BusinessExceptionEnum.ERROR.getCode(), BusinessExceptionEnum.ERROR.getMessageKey());
            mqttMsg.setAck(mqttMsgAck);
            messageService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }


    }

    @Override
    public void setEventNotifReq(MqttMsg mqttMsgParam, String topicParam) {
        // 重新组装返回值
        MqttMsg mqttMsg = new MqttMsg();
        mqttMsg.setService(mqttMsgParam.getService());
        mqttMsg.setMethod("setEventNotifResp");
        mqttMsg.setSeq(mqttMsgParam.getSeq());
        mqttMsg.setSrcAddr("cloud");

        String userUuId = CommonUtil.getDeviceOrUserIdIdByTopic(mqttMsgParam.getTopic());
        String topic = "iot/v1/c/" + userUuId + "/setEventNotifResp";
        mqttMsg.setTopic(topic);

        try {
            Map<String, Object> payloadMap = (Map<String, Object>) JsonUtil.convertJsonStrToMap(mqttMsgParam.getPayload().toString());
            //用户uuid
            FetchUserResp user = userApi.getUserByUuid(userUuId);
            Long userId = user.getId();
            Long tenantId = user.getTenantId();

            String deviceId = (String) payloadMap.get("devId");
            boolean eventNotifEnabled = (boolean) payloadMap.get("eventNotifEnabled");

            //事件通知使能（0：开启，1：关闭）
            Integer eventNotifEnabledVal = 1;
            if (eventNotifEnabled) {
                eventNotifEnabledVal = 0;
            }
            UpdateUserDeviceInfoResp userDeviceInfo = deviceCoreService.saveOrUpdateUserDevice(UpdateUserDeviceInfoReq.builder()
                    .tenantId(tenantId)
                    .userId(userId)
                    .deviceId(deviceId)
                    .eventNotifyEnabled(eventNotifEnabledVal)
                    .build());

            MqttMsgAck mqttMsgAck = new MqttMsgAck(BusinessExceptionEnum.SUCCESS.getCode(), BusinessExceptionEnum.SUCCESS.getMessageKey());
            if (userDeviceInfo == null) {
                mqttMsgAck = new MqttMsgAck(BusinessExceptionEnum.ERROR.getCode(), BusinessExceptionEnum.ERROR.getMessageKey());
            }
            Map<String, Object> responseMap = new HashMap<String, Object>();
            responseMap.put("timestamp", SIMPLE_DATE_FORMAT.format(new Date()));
            mqttMsg.setPayload(responseMap);
            mqttMsg.setAck(mqttMsgAck);
            messageService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        } catch (Exception e) {
            loger.error("error->", e);
            MqttMsgAck mqttMsgAck = new MqttMsgAck(BusinessExceptionEnum.ERROR.getCode(), BusinessExceptionEnum.ERROR.getMessageKey());
            mqttMsg.setAck(mqttMsgAck);
            messageService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }

    /**
     * 描述：异步推送
     *
     * @param event
     * @param deviceId
     * @author zhouzongwei
     * @created 2017年9月19日 下午4:55:41
     * @since
     */
    private void asySaveInform(final VideoEvent event, final String deviceId, Map<String, String> noticMap) {
        excutor.submit(new Runnable() {
            public void run() {
                try {
                    //封装消息体
                    Map<String, Object> responseMap = new HashMap<String, Object>(); // 定义响应map
                    //获取设备上的账号信息
                    GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreService.getDeviceInfoByDeviceId(deviceId);
                    Long tenantId = deviceInfoRespVo.getTenantId();

                    List<ListUserDeviceInfoRespVo> userDeviceInfoRespList = deviceCoreService.listUserDevices(tenantId, deviceId);

                    if (userDeviceInfoRespList != null && userDeviceInfoRespList.size() > 0) {
                        try {
                            for (int i = 0; i < userDeviceInfoRespList.size(); i++) {
                                ListUserDeviceInfoRespVo userDeviceInfoResp = userDeviceInfoRespList.get(i);
                                boolean eventNotifEnabled = false;
                                String userId = null;
                                if (null != userDeviceInfoResp) {
                                    if (userDeviceInfoResp.getEventNotifyEnabled() != null && userDeviceInfoResp.getEventNotifyEnabled().toString().equals("0")) {
                                        eventNotifEnabled = true;
                                    }
                                    userId = userDeviceInfoResp.getUserId().toString();
                                    //获取用户对应uuid
                                    userId = userApi.getUuid(Long.valueOf(userId));
                                }

                                //开启事件通知
                                if (eventNotifEnabled) {
                                    if (noticMap != null) {
                                        messageApi.systemSinglePush(userId, noticMap, 1);
                                    }
                                    //组装消息格式：事件类型:\n "+"\"devicename\""+" 2017/12/12 12:12:00（触发事件）
                                    Map<String, Object> eventNotiMap = new HashMap<String, Object>();
                                    eventNotiMap.put("eventType", getEventTypeName(event.getEventCode()));
                                    eventNotiMap.put("deviceId", deviceId);
                                    eventNotiMap.put("deviceName", event.getEventName());
                                    eventNotiMap.put("eventOddurTime", event.getEventOddurTime());
                                    String topic = "iot/v1/c/" + userId + "/uploadEventNotify";
                                    responseMap.put("informContent", eventNotiMap);

                                    MqttMsg mqttMsg = new MqttMsg();
                                    mqttMsg.setMethod("uploadEventNotify");
                                    mqttMsg.setPayload(responseMap);
                                    mqttMsg.setSrcAddr("cloud");
                                    mqttMsg.setService("IPC");
                                    mqttMsg.setSeq(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8));
                                    MqttMsgAck mqttMsgAck = new MqttMsgAck(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getMessageKey());
                                    mqttMsg.setAck(mqttMsgAck);
                                    //云端响应
                                    mqttMsg.setTopic(topic);
                                    messageService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
                                }
                            }
                        } catch (Exception e) {
                            loger.error("", e);
                        }
                    }
                } catch (Exception e) {
                    loger.error("", e);
                }
            }
        });
    }

    /**
     * 描述：翻译事件类型名称
     *
     * @param eventType
     * @return
     * @author zhouzongwei
     * @created 2017年9月13日 下午2:28:02
     * @since
     */
    public List<String> getEventTypeName(String eventType) {
        List<String> list = new ArrayList<String>();
        if (StringUtil.isNotEmpty(eventType)) {
            if (eventType.length() == 4) {
                if ('1' == eventType.charAt(0)) {
                    list.add("SOUND");
                }
                if ('1' == eventType.charAt(1)) {
                    list.add("MOTION");
                }
                if ('1' == eventType.charAt(2)) {
                    list.add("PIR");
                }
                if ('1' == eventType.charAt(3)) {
                    list.add("TEMPERATURE");
                }
            }
        }
        return list;
    }

    @Override
    public void onMessage(MqttMsg mqttMsg) {
        DispatcherRouteHelper.dispatch(this, mqttMsg);
    }
}
