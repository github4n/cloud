package com.iot.shcs.user.service;

import com.iot.common.util.CalendarUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.activity.api.OnlineStatusRecordApi;
import com.iot.control.activity.vo.req.OnlineStatusRecordReq;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.common.util.UserOrDeviceConnectUtil;
import com.iot.shcs.contants.ModuleConstants;
import com.iot.shcs.device.enums.OnlineStatusEnum;
import com.iot.shcs.device.enums.OnlineTypeEnum;
import com.iot.shcs.device.service.impl.DeviceMQTTService;
import com.iot.shcs.helper.DispatcherRouteHelper;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.UpdateUserReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

@Service("user")
public class UserMQTTService implements CallBackProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserMQTTService.class);

    @Autowired
    private UserApi userApi;

    @Autowired
    private OnlineStatusRecordApi onlineStatusRecordApi;

    @Resource(name="device")
    private DeviceMQTTService deviceService;

    @Override
    public void onMessage(MqttMsg mqttMsg) {
        DispatcherRouteHelper.dispatch(this, mqttMsg);
    }

    /**
     * 设备上线
     *
     * @param connectMsg   消息
     * @param connectTopic 主题
     */
    public void connect(MqttMsg connectMsg, String connectTopic) {
        LOGGER.info("user connect({}, {})", connectMsg, connectTopic);
        try {
            String[] split = connectTopic.split("/");
            String id = split[3];
            FetchUserResp user = userApi.getUserByUuid(id);
            if (user == null) {
                LOGGER.info("---- connect() error! userUUID not in database. userUUID={} ---- ", id);
                return;
            }
//            if (UserOrDeviceConnectUtil.isUpdateStatus(user.getId(),OnlineStatusEnum.CONNECTED.getCode(),connectMsg)) {
                updateOnlineStatus(id, user, OnlineStatusEnum.CONNECTED.getCode());
//            } else {
//                LOGGER.info("user connect status is newest, no need to update connect status");
//            }
            recordActivateInfo(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("user connect error", e);
        }
    }

    private void recordActivateInfo(String userId) {
        if(StringUtil.isBlank(userId)){
            return;
        }
        try{
            RedisCacheUtil.setAdd(ModuleConstants.USERONLINE,userId);
            RedisCacheUtil.setAdd(ModuleConstants.USERACTIVE + CalendarUtil.getNowDate(),userId);
        }catch (Exception e){
            logger.error("",e);
        }
    }

    /**
     * 设备下线
     *
     * @param disConnectMsg   消息
     * @param disConnectTopic 主题
     */
    public void disconnect(MqttMsg disConnectMsg, String disConnectTopic) {
        LOGGER.debug("user disconnect({}, {})", disConnectMsg, disConnectTopic);
        try {
            String[] split = disConnectTopic.split("/");
            String id = split[3];
            FetchUserResp user = userApi.getUserByUuid(id);
            if (user == null) {
                LOGGER.info("---- disconnect() error! userUUID not in database. userUUID={} ---- ", id);
                return;
            }
//            if (UserOrDeviceConnectUtil.isUpdateStatus(user.getId(), OnlineStatusEnum.CONNECTED.getCode(), disConnectMsg)) {
                updateOnlineStatus(id, user, OnlineStatusEnum.CONNECTED.getCode());
//            } else {
//                LOGGER.info("user connect status is newest, no need to update connect status");
//            }
            recordDisconnect(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("user-disconnect-error", e);
        }
    }

    private void recordDisconnect(String userId) {
        if(StringUtil.isBlank(userId)){
            return;
        }
        try{
            RedisCacheUtil.setDel(ModuleConstants.USERONLINE,userId);
        }catch (Exception e){
            logger.error("",e);
        }
    }

    private void updateOnlineStatus(String userUUID, FetchUserResp user, String status) {
        if (StringUtils.isEmpty(status)) {
            logger.debug("userOnline-update-status.notnull.userUUID:{}", userUUID);
            return;
        }
        Byte state = 3;
        if (OnlineStatusEnum.CONNECTED.getCode().equals(status)) {
            state = 2;
        } else if (OnlineStatusEnum.DISCONNECTED.getCode().equals(status)) {
            state = 3;
        }
        if (user.getState().compareTo(state) != 0) {
            UpdateUserReq req = new UpdateUserReq();
            req.setState(state);
            req.setId(user.getId());
            userApi.updateUser(req);
        }
        OnlineStatusRecordReq onlineStatusRecord = new OnlineStatusRecordReq();
        onlineStatusRecord.setId(userUUID);
        onlineStatusRecord.setType(OnlineTypeEnum.USER.getCode());
        onlineStatusRecord.setStatus(status);
        onlineStatusRecord.setRecordTime(new Date());
        onlineStatusRecord.setTenantId(SaaSContextHolder.currentTenantId());
        onlineStatusRecordApi.saveOnlineStatusRecord(onlineStatusRecord);
    }
}
