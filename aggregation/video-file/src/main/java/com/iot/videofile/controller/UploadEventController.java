package com.iot.videofile.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.device.api.UserDeviceCoreApi;
import com.iot.control.device.vo.req.ListUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceExtendsCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.locale.LocaleMessageSourceService;
import com.iot.message.api.MessageApi;
import com.iot.message.enums.MessageTempType;
import com.iot.redis.RedisCacheUtil;
import com.iot.tenant.api.UserVirtualOrgApi;
import com.iot.tenant.vo.resp.UserDefaultOrgInfoResp;
import com.iot.user.api.UserApi;
import com.iot.util.AssertUtils;
import com.iot.video.api.VideoManageApi;
import com.iot.video.api.VideoPlanApi;
import com.iot.video.entity.VideoEvent;
import com.iot.video.vo.resp.CheckBeforeUploadResult;
import com.iot.videofile.utils.CertUtil;
import com.iot.videofile.vo.UploadEventVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@Slf4j
@RestController
@RequestMapping(value = "/api/video")
public class UploadEventController {

    @Autowired
    private DeviceCoreApi deviceCoreApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private MessageApi messageApi;
    @Autowired

    private ActivityRecordApi activityRecordApi;

    @Autowired
    private UserVirtualOrgApi userVirtualOrgApi;

    @Autowired
    private UserDeviceCoreApi userDeviceCoreApi;

    @Autowired
    private DeviceExtendsCoreApi deviceExtendsCoreApi;

    @Autowired
    private VideoManageApi videoManageApi;

    @Autowired
    private VideoPlanApi videoPlanApi;

    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;

    @Value("${rate.device-event-notice-app}")
    private Long deviceEventNoticeAppRate;

    /**事件上报通知app redis记录是否已发送*/
    public static final String UPLOADEVENT_NOTICE_APP = "uploadEvent_notice_app:";
    /**IPC激活*/
    public static final String IPC_DETECTED_ACTIVITY="ipc.detected.activity";
    /**设备*/
    public final static String ACTIVITY_RECORD_DEVICE = "DEVICE";

    /**线程池*/
    private static ExecutorService excutor = Executors.newCachedThreadPool();

    @ApiOperation("上报事件消息")
    @LoginRequired(value = Action.Skip)
    @RequestMapping(value = "/uploadEvent", method = RequestMethod.POST)
    public CommonResponse uploadEvent(@RequestHeader("ssl_client_s_dn") String cert,  @RequestBody UploadEventVo uploadEventVo) {
        try {
            String deviceId =  CertUtil.getDeviceId(cert);
            VideoEvent videoEvent = new VideoEvent();
            if (uploadEventVo != null) {
                //事件id
                videoEvent.setEventUuid(uploadEventVo.getEvtId());
                //事件触发时间
                videoEvent.setEventOddurTime(uploadEventVo.getTriggerTime());
                //事件类型
                videoEvent.setEventCode(uploadEventVo.getEvtType());
                //计划id
                videoEvent.setPlanId(uploadEventVo.getPlanId());
                //事件名称
                videoEvent.setEventName(uploadEventVo.getDevName());
            } else {
                log.error("param is null");
            }

            if (StringUtil.isNotBlank(videoEvent.getPlanId())) {
                boolean addEventFlag = true;
                //校验设备是否已绑定计划、计划是否过期——防止解绑之后IPC还上传数据或者计划过期没通知到ipc停止录影
                CheckBeforeUploadResult result = videoPlanApi.checkBeforeUpload(videoEvent.getPlanId(), deviceId);
                if (!result.isPaasFlag()) {
                    log.debug("insertEvent fail -> {}", result.getDesc());
                    addEventFlag = false;
                }
                if (addEventFlag) {
                    //有绑定计划在插入video_event
                    videoManageApi.insertVideoEvent(videoEvent);
                }
            }
            GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(deviceId);
            Long tenantId = deviceResp.getTenantId();
            //添加活动记录
            //1、先查找设备信息，绑定的主账号(未绑定的话直接结束)
            Long rootUserId = getRootUserIdByDeviceId(tenantId, deviceId);
            if (rootUserId == null) {
                // 抛出异常
                log.error("userId is null");
            }
            //封装活动日志
            ActivityRecordReq activityRecordReq = new ActivityRecordReq();
            activityRecordReq.setCreateBy(rootUserId);
            activityRecordReq.setType(ACTIVITY_RECORD_DEVICE);
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
            String noticeAppKey = UPLOADEVENT_NOTICE_APP + deviceId;
            String hasSendFlag = RedisCacheUtil.valueGet(noticeAppKey);
            if (StringUtil.isBlank(hasSendFlag)) {
                noticMap = new HashMap<>();
                RedisCacheUtil.valueSet(noticeAppKey, "hasSend", deviceEventNoticeAppRate);
                //查询设备的p2pId
                GetDeviceExtendInfoRespVo deviceExtendInfo = getDeviceExtendByDeviceId(tenantId, deviceId);
                String p2pId = deviceExtendInfo.getP2pId();
                noticMap.put("templateId", MessageTempType.IOS00001.getTempId());   //这里暂时是测试代码，具体模板未定
                noticMap.put("message", localeMessageSourceService.getMessage(IPC_DETECTED_ACTIVITY, new Object[]{deviceResp.getName()}));
                //推送给app的消息参数
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("deviceId", deviceId);
                paramMap.put("p2pId", p2pId);
                noticMap.put("customDictionary", JsonUtil.toJson(paramMap).toString());
            }
            //系统推送
            asySaveInform(videoEvent, deviceId, noticMap);
        } catch (Exception e) {
            log.error("uploadEvent error:", e);
        }
        return CommonResponse.success();
    }

    public Long getRootUserIdByDeviceId(Long tenantId, String deviceId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
        ListUserDeviceInfoReq params = ListUserDeviceInfoReq.builder()
                .tenantId(tenantId)
                .userId(null)
                .deviceId(deviceId).build();
        List<ListUserDeviceInfoRespVo> userDeviceInfoList = userDeviceCoreApi.listUserDevice(params);
        if (!CollectionUtils.isEmpty(userDeviceInfoList)) {
            for (ListUserDeviceInfoRespVo userDeviceInfo : userDeviceInfoList) {
                if (userDeviceInfo.getUserType().equals("root")) {
                    return userDeviceInfo.getUserId();
                }
            }
        }
        return null;
    }

    public GetDeviceExtendInfoRespVo getDeviceExtendByDeviceId(Long tenantId, String deviceId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
        return deviceExtendsCoreApi.get(tenantId, deviceId);
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
                    GetDeviceInfoRespVo deviceInfoRespVo = getDeviceInfoByDeviceId(deviceId);
                    Long tenantId = deviceInfoRespVo.getTenantId();

                    List<ListUserDeviceInfoRespVo> userDeviceInfoRespList = listUserDevices(tenantId, deviceId);

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
                                }
                            }
                        } catch (Exception e) {
                            log.error("send message error :" + e);
                        }
                    }
                } catch (Exception e) {
                    log.error("send message error :" + e);
                }
            }
        });
    }

    public GetDeviceInfoRespVo getDeviceInfoByDeviceId(String deviceId) {
        return deviceCoreApi.get(deviceId);
    }

    public List<ListUserDeviceInfoRespVo> listUserDevices(Long tenantId, String deviceId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
        ListUserDeviceInfoReq params = ListUserDeviceInfoReq.builder()
                .tenantId(tenantId)
                .userId(null)
                .deviceId(deviceId).build();
        List<ListUserDeviceInfoRespVo> userDeviceInfoList = userDeviceCoreApi.listUserDevice(params);
        return userDeviceInfoList;
    }
}
