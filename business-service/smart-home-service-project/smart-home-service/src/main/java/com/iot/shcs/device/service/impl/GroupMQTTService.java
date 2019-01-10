package com.iot.shcs.device.service.impl;


import com.alibaba.fastjson.JSON;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.api.GroupApi;
import com.iot.device.exception.DeviceExceptionEnum;
import com.iot.device.vo.req.group.UpdateGroupDetailReq;
import com.iot.device.vo.req.group.UpdateGroupReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.shcs.common.exception.BusinessExceptionEnum;
import com.iot.shcs.common.util.MQTTUtils;
import com.iot.shcs.device.enums.DeviceCoreExceptionEnum;
import com.iot.shcs.device.service.IGroupMQTTService;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.helper.DispatcherRouteHelper;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("group")
public class GroupMQTTService implements IGroupMQTTService,CallBackProcessor {


    private final String GROUP_ID = "groupId";
    private final String GROUP_IDS="groups";
    private final String MEMBERS="members";
    private final String DEVICE_ID="devId";
    private final String PARENT_ID="parentId";
    @Autowired
    private MqttSdkService mqttSdkService;
    @Autowired
    private UserApi userApi;
    @Autowired
    private DeviceStateCoreApi deviceStateCoreApi;
    @Autowired
    private DeviceCoreService deviceCoreService;
    @Autowired
    private GroupApi groupApi;

    private static final int QOS = 1;

    @Override
    public void onMessage(MqttMsg mqttMsg) {
        DispatcherRouteHelper.dispatch(this, mqttMsg);
    }


    /**
    * 方法描述： iot/v1/s/[userId]/group/delGroupReq
    * 创建时间： 2018.12.3
    */
    @Override
    public void delGroupReq(MqttMsg reqMqttMsg, String reqTopic) {
        logger.info("****delGroupReq ({}, {})",reqMqttMsg,reqTopic);
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        //获取用户uuid，groupId，tenantId
        String userUuid = MQTTUtils.parseReqTopic(reqTopic);
        FetchUserResp fetchUserResp=userApi.getUserByUuid(userUuid);
        Long tenantId=fetchUserResp.getTenantId();

        String deviceUuid= String.valueOf(payload.get(PARENT_ID));
        if(StringUtil.isEmpty(deviceUuid)||deviceUuid=="null"){
            String subUuId=String.valueOf(payload.get(DEVICE_ID));
            deviceUuid=getDirectUuidBySubDevUuid(subUuId);
            logger.info("delGroupReq deviceUuid is{}",deviceUuid);
        }

        //转发给网关：topic:iot/v1/c/[devId]/group/delGroupReq
        reqMqttMsg.setTopic(buildClientTopic(MQTTUtils.getMethodFromTopic(reqTopic), deviceUuid));
        logger.info("****delGroupReq:reqMqttMsg userUuid={},groupId={},tenantId={},deviceUuid={},topic={}",userUuid,tenantId,deviceUuid,reqMqttMsg.getTopic());
        mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), reqMqttMsg, QOS);
    }
    /**
    * 方法描述： iot/v1/s/[devId]/group/delGroupResp
    * 创建时间： 2018.12.3
    */

    @Override
    public void delGroupResp(MqttMsg reqMqttMsg, String reqTopic) {
        logger.info("****delGroupResp ({}, {})",reqMqttMsg,reqTopic);
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceUuid = MQTTUtils.parseReqTopic(reqTopic);
        FetchUserResp fetchUserResp=getUserByDeviceUuid(deviceUuid);
        String userUuid=fetchUserResp.getUuid();
        Long userId=fetchUserResp.getId();
        Long tenantId=fetchUserResp.getTenantId();
        List groups=(List)payload.get(GROUP_IDS);
        logger.info("***delGroupResp {}",JSON.toJSONString(groups));
        try{
            if (reqMqttMsg.getAck().getCode() == 200) {

                //删除group和groupDetail表
                UpdateGroupReq updateGroupReq=new UpdateGroupReq();
                updateGroupReq.setCreateBy(userId);
                updateGroupReq.setTenantId(tenantId);
                updateGroupReq.setGroupIds(groups);
                logger.info("***delGroupResp {}",JSON.toJSONString(updateGroupReq));
                groupApi.delGroupById(updateGroupReq);
            }else {
                throw new Exception("delGroupResp ack is not 200");
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("delGroupResp error: "+e.getMessage());
            reqMqttMsg.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey()));
        }finally {
            //转发给用户：iot/v1/c/[userId]/group/delGroupResp
            reqMqttMsg.setTopic(buildClientTopic(MQTTUtils.getMethodFromTopic(reqTopic), userUuid));
            logger.info("****delGroupResp_reqMqttMsg userUuid={},groupId={},tenantId={},deviceUuid={},topic={}",userUuid,tenantId,deviceUuid,reqMqttMsg.getTopic());
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), reqMqttMsg, QOS);
        }
    }

    /**
    * 方法描述： iot/v1/s/[userId]/group/addGroupMemberReq
    * 创建时间：2018.12.3
    */
    @Override
    public void addGroupMemberReq(MqttMsg reqMqttMsg, String reqTopic) {
        logger.info("****addGroupMemberReq ({}, {})",reqMqttMsg,reqTopic);
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String userUuid = MQTTUtils.parseReqTopic(reqTopic);
        Long groupId=Long.parseLong(String.valueOf(payload.get(GROUP_ID)));
        FetchUserResp fetchUserResp=userApi.getUserByUuid(userUuid);
        Long tenantId=fetchUserResp.getTenantId();

        String deviceUuid= String.valueOf(payload.get(PARENT_ID));
        if(StringUtil.isEmpty(deviceUuid)||deviceUuid=="null"){
            String subUuId=String.valueOf(payload.get(DEVICE_ID));
            deviceUuid=getDirectUuidBySubDevUuid(subUuId);
            logger.info("addGroupMemberReq deviceUuid is{}",deviceUuid);
        }

        //转发给用户：iot/v1/c/[devId]/group/addGroupMemberReq
        reqMqttMsg.setTopic(buildClientTopic(MQTTUtils.getMethodFromTopic(reqTopic), deviceUuid));
        logger.info("****reqMqttMsg userUuid={},groupId={},tenantId={},deviceUuid={},topic={}",userUuid,groupId,tenantId,deviceUuid,reqMqttMsg.getTopic());
        mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), reqMqttMsg, QOS);
    }

    /**
    * 方法描述：iot/v1/s/[userId]/group/addGroupMemberResp
    * 创建时间：2018.12.3
    */
    @Override
    public void addGroupMemberResp(MqttMsg reqMqttMsg, String reqTopic) {
        logger.info("****addGroupMemberResp ({}, {})",reqMqttMsg,reqTopic);
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceUuid = MQTTUtils.parseReqTopic(reqTopic);
        FetchUserResp fetchUserResp=getUserByDeviceUuid(deviceUuid);
        String userUuid=fetchUserResp.getUuid();
        Long userId=fetchUserResp.getId();
        Long tenantId=fetchUserResp.getTenantId();
        Long groupId=Long.parseLong(String.valueOf(payload.get(GROUP_ID)));
        List<String> members=(List<String>)payload.get(MEMBERS);
        UpdateGroupDetailReq updateGroupDetailReq=new UpdateGroupDetailReq();
        Boolean flag=null;
        try {
            if (reqMqttMsg.getAck().getCode() == 200) {
                updateGroupDetailReq.setTenantId(tenantId);
                updateGroupDetailReq.setCreateBy(userId);
                updateGroupDetailReq.setMembers(members);
                updateGroupDetailReq.setCreateTime(new Date());
                updateGroupDetailReq.setGroupId(groupId);
                updateGroupDetailReq.setUpdateTime(new Date());
                flag=groupApi.saveGroupDetialBatch(updateGroupDetailReq);
            }else {
                throw new Exception("addGroupMemberResp ack is not 200");
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("addGroupMemberResp error: "+e.getMessage());
            reqMqttMsg.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey()));
        }finally {
            reqMqttMsg.setTopic(buildClientTopic(MQTTUtils.getMethodFromTopic(reqTopic), userUuid));
            logger.info("****addGroupMemberResp_reqMqttMsg userUuid={},groupId={},tenantId={},deviceUuid={},topic={},updateGroupDetailReq={},insertFlag={}",userUuid,groupId,tenantId,deviceUuid,reqMqttMsg.getTopic(), JSON.toJSONString(updateGroupDetailReq),flag);
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), reqMqttMsg, QOS);
        }
    }

    /**
    * 方法描述： iot/v1/s/[userId]/group/delGroupMemberReq
    * 创建时间： 2018.12.3
    */
    @Override
    public void delGroupMemberReq(MqttMsg reqMqttMsg, String reqTopic) {
        logger.info("****delGroupMemberReq ({}, {})",reqMqttMsg,reqTopic);
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String userUuid = MQTTUtils.parseReqTopic(reqTopic);
        Long groupId=Long.parseLong(String.valueOf(payload.get(GROUP_ID)));
        FetchUserResp fetchUserResp=userApi.getUserByUuid(userUuid);
        Long tenantId=fetchUserResp.getTenantId();

        String deviceUuid= String.valueOf(payload.get(PARENT_ID));
        if(StringUtil.isEmpty(deviceUuid)||deviceUuid=="null"){
            String subUuId=String.valueOf(payload.get(DEVICE_ID));
            deviceUuid=getDirectUuidBySubDevUuid(subUuId);
            logger.info("delGroupMemberReq deviceUuid is{}",deviceUuid);
        }

        //转发给网关：iot/v1/c/[devId]/group/delGroupMemberReq
        reqMqttMsg.setTopic(buildClientTopic(MQTTUtils.getMethodFromTopic(reqTopic), deviceUuid));
        logger.info("****reqMqttMsg userUuid={},groupId={},tenantId={},deviceUuid={},topic={}",userUuid,groupId,tenantId,deviceUuid,reqMqttMsg.getTopic());
        mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), reqMqttMsg, QOS);
    }

    /**
    * 方法描述： iot/v1/s/[devId]/group/delGroupMemberResp
    * 创建时间：
    */
    @Override
    public void delGroupMemberResp(MqttMsg reqMqttMsg, String reqTopic) {
        logger.info("****delGroupMemberResp ({}, {})",reqMqttMsg,reqTopic);
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceUuid = MQTTUtils.parseReqTopic(reqTopic);
        FetchUserResp fetchUserResp=getUserByDeviceUuid(deviceUuid);
        String userUuid=fetchUserResp.getUuid();
        Long userId=fetchUserResp.getId();
        Long tenantId=fetchUserResp.getTenantId();
        Long groupId=Long.parseLong(String.valueOf(payload.get(GROUP_ID)));
        List<String> members=(List<String>)payload.get(MEMBERS);
        try {
            if (reqMqttMsg.getAck().getCode() == 200) {
                UpdateGroupDetailReq updateGroupDetailReq = new UpdateGroupDetailReq();
                updateGroupDetailReq.setMembers(members);
                updateGroupDetailReq.setGroupId(groupId);
                updateGroupDetailReq.setTenantId(tenantId);
                updateGroupDetailReq.setCreateBy(userId);
                groupApi.delGroupDetial(updateGroupDetailReq);
            }else {
                throw new Exception("delGroupMemberResp ack is not 200");
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("delGroupMemberResp error: "+e.getMessage());
            reqMqttMsg.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey()));

        }finally {
            reqMqttMsg.setTopic(buildClientTopic(MQTTUtils.getMethodFromTopic(reqTopic), userUuid));
            logger.info("****delGroupMemberResp_reqMqttMsg userUuid={},groupId={},tenantId={},deviceUuid={},topic={}",userUuid,groupId,tenantId,deviceUuid,reqMqttMsg.getTopic());
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), reqMqttMsg, QOS);
        }


    }


    /**
    * 方法描述： iot/v1/s/[userId]/group/getGroupMemberReq
    * 创建时间： 2018.12.3
    */
    @Override
    public void getGroupMemberReq(MqttMsg reqMqttMsg, String reqTopic) {
        logger.info("****getGroupMemberReq ({}, {})",reqMqttMsg,reqTopic);
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String userUuid = MQTTUtils.parseReqTopic(reqTopic);
        Long groupId=Long.parseLong(String.valueOf(payload.get(GROUP_ID)));
        FetchUserResp fetchUserResp=userApi.getUserByUuid(userUuid);
        Long tenantId=fetchUserResp.getTenantId();

        String deviceUuid= String.valueOf(payload.get(PARENT_ID));
        if(StringUtil.isEmpty(deviceUuid)||deviceUuid=="null"){
            String subUuId=String.valueOf(payload.get(DEVICE_ID));
            deviceUuid=getDirectUuidBySubDevUuid(subUuId);
            logger.info("getGroupMemberReq deviceUuid is{}",deviceUuid);
        }

        reqMqttMsg.setTopic(buildClientTopic(MQTTUtils.getMethodFromTopic(reqTopic), deviceUuid));
        //转发网关：iot/v1/c/[devId]/group/getGroupMemberReq
        logger.info("****getGroupMemberReq_reqMqttMsg userUuid={},groupId={},tenantId={},deviceUuid={},topic={}",userUuid,groupId,tenantId,deviceUuid,reqMqttMsg.getTopic());
        mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), reqMqttMsg, QOS);
    }

    @Override
    public void getGroupMemberResp(MqttMsg reqMqttMsg, String reqTopic) {
        logger.info("****getGroupMemberResp ({}, {})",reqMqttMsg,reqTopic);
        String deviceUuid = MQTTUtils.parseReqTopic(reqTopic);
        FetchUserResp fetchUserResp=getUserByDeviceUuid(deviceUuid);
        String userUuid=fetchUserResp.getUuid();
        try {
            if (reqMqttMsg.getAck().getCode() == 200) {

            }else {
                throw new Exception("getGroupMemberResp ack is not 200");
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("excGroupResp error "+e.getMessage());
            reqMqttMsg.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey()));
        }finally {
            reqMqttMsg.setTopic(buildClientTopic(MQTTUtils.getMethodFromTopic(reqTopic), userUuid));
            logger.info("****getGroupMemberResp_reqMqttMsg userUuid={},groupId={},tenantId={},deviceUuid={},topic={}",userUuid,deviceUuid,reqMqttMsg.getTopic());
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), reqMqttMsg, QOS);
        }

    }

    /**
    * 方法描述： iot/v1/s/[userId]/group/excGroupReq
    * 创建时间：
    */
    @Override
    public void excGroupReq(MqttMsg reqMqttMsg, String reqTopic) {
        logger.info("****excGroupReq ({}, {})",reqMqttMsg,reqTopic);
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String userUuid = MQTTUtils.parseReqTopic(reqTopic);
        Long groupId=Long.parseLong(String.valueOf(payload.get(GROUP_ID)));
        FetchUserResp fetchUserResp=userApi.getUserByUuid(userUuid);
        Long tenantId=fetchUserResp.getTenantId();

        String deviceUuid= String.valueOf(payload.get(PARENT_ID));
        if(StringUtil.isEmpty(deviceUuid)||deviceUuid=="null"){
            String subUuId=String.valueOf(payload.get(DEVICE_ID));
            deviceUuid=getDirectUuidBySubDevUuid(subUuId);
            logger.info("excGroupReq deviceUuid is{}",deviceUuid);
        }
        //转发给网关：iot/v1/c/[devId]/group/excGroupReq
        reqMqttMsg.setTopic(buildClientTopic(MQTTUtils.getMethodFromTopic(reqTopic), deviceUuid));
        logger.info("****excGroupReq_reqMqttMsg userUuid={},groupId={},tenantId={},deviceUuid={},topic={}",userUuid,groupId,tenantId,deviceUuid,reqMqttMsg.getTopic());
        mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), reqMqttMsg, QOS);
    }
    /**
    * 方法描述： iot/v1/s/[devId]/group/excGroupResp
    * 创建时间：
    */
    @Override
    public void excGroupResp(MqttMsg reqMqttMsg, String reqTopic) {
        logger.info("****excGroupResp ({}, {})",reqMqttMsg,reqTopic);
        Map<String, Object> payload = (Map<String, Object>) reqMqttMsg.getPayload();
        String deviceUuid = MQTTUtils.parseReqTopic(reqTopic);
        FetchUserResp fetchUserResp=getUserByDeviceUuid(deviceUuid);
        String userUuid=fetchUserResp.getUuid();
        try{
            if (reqMqttMsg.getAck().getCode() == 200) {

            }else {
                throw new Exception("excGroupResp ack is not 200");
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("excGroupResp error "+e.getMessage());
            reqMqttMsg.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey()));
        }finally {
            //转发给用户
            reqMqttMsg.setTopic(buildClientTopic(MQTTUtils.getMethodFromTopic(reqTopic), userUuid));
            logger.info("****excGroupResp_reqMqttMsg userUuid={},groupId={},tenantId={},deviceUuid={},topic={}",userUuid,deviceUuid,reqMqttMsg.getTopic());
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), reqMqttMsg, QOS);
        }
    }

    /**
    * 方法描述 构建发送给app的Topic
    */
    private String buildClientTopic(String method, String Uuid) {
        return Constants.TOPIC_CLIENT_PREFIX + Uuid + "/" + "group" + "/" + method;
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

    private String getDirectUuidBySubDevUuid(String subDevUuid){
        GetDeviceInfoRespVo getDeviceInfoRespVo=deviceCoreService.getDeviceInfoByDeviceId(subDevUuid);
        String deviceUuId=getDeviceInfoRespVo.getParentId();
        if(StringUtil.isEmpty(deviceUuId)){
            logger.info("******deviceUuid is null!!!");
            throw new BusinessException(DeviceCoreExceptionEnum.DIRECT_DEVICE_IS_NULL);
        }
        return deviceUuId;
    }


}
