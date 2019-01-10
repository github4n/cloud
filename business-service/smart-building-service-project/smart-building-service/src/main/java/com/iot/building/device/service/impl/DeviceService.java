package com.iot.building.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.iot.building.device.service.DeviceTobService;
import com.iot.building.group.api.GroupApi;
import com.iot.building.ifttt.api.AutoTobApi;
import com.iot.building.ifttt.entity.Trigger;
import com.iot.building.ifttt.mapper.TriggerTobMapper;
import com.iot.building.ifttt.util.RedisKeyUtil;
import com.iot.building.ifttt.vo.TriggerVo;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.utils.ValueUtils;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.ifttt.api.IftttApi;
import com.iot.ifttt.vo.AppletItemVo;
import com.iot.ifttt.vo.AppletThisVo;
import com.iot.ifttt.vo.AppletVo;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.redis.RedisCacheUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.iot.building.gateway.MultiProtocolGatewayHepler;
import com.iot.building.helper.Constants;
import com.iot.common.util.StringUtil;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import java.util.List;
import java.util.Map;

@Service
public class DeviceService implements DeviceTobService{

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    private static Environment dnvironment= ApplicationContextHelper.getBean(Environment.class);

    private static MqttSdkService mqttSdkService=ApplicationContextHelper.getBean(MqttSdkService.class);

    private final static int QOS = 1;
    @Autowired
    private DeviceCoreApi deviceCoreApi;
    @Autowired
    private SpaceApi spaceApi;
    @Autowired
    private AutoTobApi autoTobApi;
    @Autowired
    private IftttApi iftttApi;
    @Autowired
    private SceneApi sceneApi;
    @Autowired
    private SpaceDeviceApi spaceDeviceApi;
    @Autowired
    private GroupApi groupApi;
    @Autowired
    private TriggerTobMapper triggerTobMapper;

    /**
     * 删除scene
     * @param deviceId
     * @param clientId
     * @Author 林济煌
     */
    public void deleteScene(Long orgId,String deviceId,Long tenantId,String clientId) throws Exception {
        SceneDetailReq sceneDetailReq = new SceneDetailReq();
        sceneDetailReq.setDeviceId(deviceId);
        sceneDetailReq.setTenantId(tenantId);
        sceneDetailReq.setOrgId(orgId);
        List<SceneDetailResp> list = sceneApi.sceneDetailByParam(sceneDetailReq);
        //删除情景，先删除sceneDetail
        sceneApi.deleteSceneDetailByDeviceId(sceneDetailReq);
        //再删除scene
            /*for (SceneDetailResp sceneDetailResp : list) {
                SceneReq sceneReq = new SceneReq();
                sceneReq.setId(sceneDetailResp.getSceneId());
                sceneReq.setCreateBy(sceneDetailResp.getCreateBy());
                sceneReq.setTenantId(sceneDetailResp.getTenantId());
                sceneApi.deleteScene(sceneReq);
            }*/
        //把主表的upload_status为0
        for(SceneDetailResp sceneDetailResp : list){
            SceneReq sceneReq = new SceneReq();
            sceneReq.setId(sceneDetailResp.getSceneId());
            //sceneReq.setUploadStatus(0);
            sceneReq.setTenantId(sceneDetailResp.getTenantId());
            sceneReq.setOrgId(orgId);
            sceneApi.updateScene(sceneReq);
            //TO DO  删除网关情景
            MultiProtocolGatewayHepler multiProtocolGatewayHepler = new MultiProtocolGatewayHepler();
            //multiProtocolGatewayHepler.deleteScene(clientId,sceneDetailResp.getSceneId());
            SceneDetailResp sceneDetail = new SceneDetailResp();
            sceneDetail.setDeviceId(deviceId);
            multiProtocolGatewayHepler.delScene(clientId,sceneDetailResp.getSceneId(),sceneDetail);
        }
    }

    /**
     * 删除设备
     * @param deviceId
     * @param clientId
     * @Author 林济煌
     */
    public  void deleteDevice(String deviceId,String clientId){
        //List<String> deviceIds = deviceCoreApi.deleteByDeviceId(deviceId + "");
        //TO DO  删除网关里面的子设备
        //for(String str : deviceIds){
        MultiProtocolGatewayHepler multiProtocolGatewayHepler = new MultiProtocolGatewayHepler();
        multiProtocolGatewayHepler.deleteDevice(clientId,deviceId);
        //}
    }

    /**
     * 删除ifttt
     * @param deviceId
     * @param check
     * @param tenantId
     * @return
     */
    public boolean deleteIfttt(Long orgId,String deviceId,boolean check,Long tenantId,String clientId){
        boolean flag = false;
        try{
            SaveIftttReq ruleVO = new SaveIftttReq();
            ruleVO.setSensorDeviceId(deviceId);
            ruleVO.setActuatorDeviceId(deviceId);
            ruleVO.setTenantId(tenantId);
            ruleVO.setOrgId(orgId);
            List<TriggerVo> triggerList = autoTobApi.getTriggerTobListByDeviceId(ruleVO);
            LOGGER.info("====triggerList.size()==="+triggerList.size());
            if (triggerList != null) {
                for (TriggerVo triggerVo : triggerList) {
                    LOGGER.info("====triggerVo.getSensorType()==="+triggerVo.getSensorType());
                    LOGGER.info("====triggerVo.getActuctorType()==="+triggerVo.getActuctorType());
                    if (StringUtil.isNotEmpty(triggerVo.getSensorDeviceId()) && triggerVo.getSensorDeviceId().equals(deviceId)) {
                        LOGGER.info("====triggerVo.getSensorDeviceId()==="+triggerVo.getSensorDeviceId());
                        triggerVo.setSensorProperties(null);
                        triggerVo.setSensorType(null);
                        triggerVo.setSensorPosition(null);
                        triggerVo.setSensorDeviceId(null);
                        //通过ruleId 查找appletId
                        SaveIftttReq ruleVOStr = new SaveIftttReq();
                        ruleVOStr.setId(triggerVo.getRuleId());
                        ruleVOStr.setOrgId(orgId);
                        List<RuleResp> ruleRespList = autoTobApi.getRuleList(ruleVOStr);
                        if(CollectionUtils.isNotEmpty(ruleRespList)){
                            for(RuleResp ruleResp:ruleRespList){
                                AppletVo appletVo = iftttApi.get(ruleResp.getAppletId());
                                //中控删除一整条ifttt，tob,toc的一起删除2018.11.26 20:28 杰斌说的,杰斌说的,杰斌说的
                                LOGGER.info("================triggerVo.getRuleId()===="+triggerVo.getRuleId());
                                autoTobApi.delete(orgId,tenantId,triggerVo.getRuleId(),true) ;
                                LOGGER.info("================autoTobApi.delete====sensor==========success");
                            }
                        }
                    }else if (StringUtil.isNotEmpty(triggerVo.getActuctorDeviceId()) && triggerVo.getActuctorDeviceId().equals(deviceId)) {
                        LOGGER.info("====triggerVo.getActuctorDeviceId()==="+triggerVo.getActuctorDeviceId());
                        triggerVo.setActuctorProperties(null);
                        triggerVo.setActuctorType(null);
                        triggerVo.setActuctorPosition(null);
                        triggerVo.setActuctorDeviceId(null);
                        //通过ruleId 查找appletId
                        SaveIftttReq ruleVOStr = new SaveIftttReq();
                        ruleVOStr.setId(triggerVo.getRuleId());
                        ruleVOStr.setOrgId(orgId);
                        List<RuleResp> ruleRespList = autoTobApi.getRuleList(ruleVOStr);
                        if(CollectionUtils.isNotEmpty(ruleRespList)){
                            for(RuleResp ruleResp:ruleRespList){
                                AppletVo appletVo = iftttApi.get(ruleResp.getAppletId());
                                //中控删除一整条ifttt，tob,toc的一起删除2018.11.26 20:28 杰斌说的,杰斌说的,杰斌说的
                                autoTobApi.delete(orgId,tenantId,triggerVo.getRuleId(),true) ;
                                LOGGER.info("================autoTobApi.delete====actuator==========success");
                            }
                        }
                    }
                    LOGGER.info("================autoTobApi.delete==============success");
                    //删除网关
                    MultiProtocolGatewayHepler multiProtocolGatewayHepler = new MultiProtocolGatewayHepler();
                    multiProtocolGatewayHepler.deleteIfttt(clientId,triggerVo.getRuleId());

                }
            }
            flag = true;
        }catch (Exception e){
            flag = false;
            e.printStackTrace();
        }finally {
            return flag;
        }
    }

    public static Trigger getTrigger(TriggerVo source) {
        Trigger target = new Trigger();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setLineId(source.getLineId());
        target.setSourceLabel(source.getSourceLabel());
        target.setStart(source.getStart());
        target.setEnd(source.getEnd());
        target.setDestinationLabel(source.getDestinationLabel());
        target.setInvocationPolicy(source.getInvocationPolicy());
        target.setStatusTrigger(source.getStatusTrigger());
        target.setRuleId(source.getRuleId());
        target.setAppletId(source.getAppletId());
        target.setSensorPosition(source.getSensorPosition());
        target.setSensorType(source.getSensorType());
        target.setSensorProperties(source.getSensorProperties());
        target.setSensorDeviceId(source.getSensorDeviceId());
        target.setActuctorPosition(source.getActuctorPosition());
        target.setActuctorType(source.getActuctorType());
        target.setActuctorProperties(source.getActuctorProperties());
        target.setActuctorDeviceId(source.getActuctorDeviceId());
        return target;
    }

    /**
     * 删除子设备和删除对应的情景，空间，ifttt
     * @param deviceId
     * @param tenantId
     * @param check
     * @param clientId
     * @Author 林济煌
     * @return
     */
    @Override
    public boolean deleteDeviceRelation(Long orgId, String deviceId, Long tenantId,boolean check,String clientId) {
        boolean flag = false;
        try{
            //删除设备----网关的
            LOGGER.info("==============deleteDeviceRelation=============start");
            LOGGER.info("==============check============="+check);
            if(check){
                LOGGER.info("==============deleteDevice=============start");
                deleteDevice(deviceId,clientId);
                LOGGER.info("==============deleteDevice=============success");
            }
            //删除scene----库+网关的
            LOGGER.info("==============deleteScene=============start");
            deleteScene(orgId,deviceId,tenantId,clientId);
            LOGGER.info("==============deleteScene=============success");
            //按杰斌的意思是，设备可能没有归属，直接删除ifttt----库+网关的
            LOGGER.info("==============deleteIfttt=============start");
            flag = deleteIfttt(orgId,deviceId,check,tenantId,clientId);
            LOGGER.info("==============deleteIfttt=============success");
        }catch (Exception e){
            flag = false;
            e.printStackTrace();
        }finally {
            return flag;
        }
    }

    /**
     * 重启网关(网关重置)resetReq
     * @param clientId
     * @Author 林济煌
     * @return
     */
    @Override
    public void resetReq(String clientId) {
        LOGGER.info("==============重启网关(网关重置)resetReq=============start");
        MultiProtocolGatewayHepler multiProtocolGatewayHepler = new MultiProtocolGatewayHepler();
        multiProtocolGatewayHepler.resetReq(clientId);
        LOGGER.info("==============重启网关(网关重置)resetReq=============success");
    }


    /**
     *  根据deviceUuid获取直连设备
     *
     * @param deviceUuid
     * @return DeviceResp
     * @author yuChangXing
     * @created 2018/07/26 16:49
     */
    public GetDeviceInfoRespVo getDirectDeviceByDeviceUuid(String deviceUuid) {
        GetDeviceInfoRespVo deviceResp = null;
        if (StringUtil.isNotBlank(deviceUuid)) {
            deviceResp = deviceCoreApi.get(deviceUuid);
            if (deviceResp.getIsDirectDevice() == null || deviceResp.getIsDirectDevice().intValue() == Constants.IS_NOT_DIRECT_DEVICE) {
                // 非直连设备
                if (deviceResp.getParentId() != null) {
                    deviceResp = deviceCoreApi.get(deviceResp.getParentId());
                }
            }
        }
        return deviceResp;
    }

    /**
     * 获取网关下的所有子设备
     *
     * @throws Exception
     */
    public void getDeviceList(String deviceId) throws Exception {
        MultiProtocolGatewayHepler.queryReq(deviceId);;
    }
}
