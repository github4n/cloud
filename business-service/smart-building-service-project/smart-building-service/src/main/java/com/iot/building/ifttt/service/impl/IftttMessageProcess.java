package com.iot.building.ifttt.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.building.helper.CenterControlDeviceStatus;
import com.iot.building.ifttt.calculatro.IActuatorExecutor;
import com.iot.building.ifttt.calculatro.SpaceActuatorExecutor;
import com.iot.building.ifttt.entity.Trigger;
import com.iot.building.ifttt.mapper.TriggerTobMapper;
import com.iot.building.scene.service.impl.SceneServiceImpl;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.common.util.StringUtil;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.ifttt.vo.ActionMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述： ifttt 执行
 * 创建人： huangxu
 * 创建时间： 2018/10/16
 */
@Slf4j
@Component
public class IftttMessageProcess extends AbsMessageProcess<ActionMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IftttMessageProcess.class);

    //private SceneServiceImpl sceneService= ApplicationContextHelper.getBean(SceneServiceImpl.class);

    @Autowired
    private SceneServiceImpl sceneService;

    @Autowired
    private SceneApi sceneApi;

    @Autowired
    private IBuildingSpaceService service;

    @Autowired
    private IBuildingSpaceService iBuildingSpaceService;

    @Autowired
    private TriggerTobMapper triggerTobMapper;

    public void processMessage(ActionMessage message) {
        //接受2B消息
        String route = message.getRoute();
        if (!"2B".equals(route)) {
            return;
        }
        //执行 设备/情景/房间
        //设置Applet时候写好的actuctor格式
        log.info("receive skill message:{}", JSON.toJSONString(message));
        Map<String, Object> msg = JSON.parseObject(message.getMessage(), Map.class);

        //判断occupancy特殊需求
        boolean flagNoOccupancy = true;
        boolean flagHaveOccupancy = true;
        int occupancyStatus = 0;
        int occupancyTrue = 0;
        int occupancyFalse = 0;
        String sensorType = (String) msg.get("sensorType");
        String[] sensorTypeStr = sensorType.split(",");
        for(String str : sensorTypeStr){
            if(str.equals("Occupancy")) {
                occupancyStatus++;
            }
        }
       /* Long appletId = (Long) msg.get("sensorType");
        //通过appletId 关联 build_tob_rule 和 tob_tregger 查询 sensorDeviceId
        List<Trigger> triggerList =  triggerTobMapper.selectByAppletId(appletId,message.getTenantId());
        Map<String, Object> deviceStatesMap = new HashMap<String, Object>();
        for (Trigger trigger:triggerList){
            if(!trigger.getSensorType().equals("Occupancy")){
                flagNoOccupancy = true;
                continue;
            }else {
                deviceStatesMap = (Map<String, Object>) CenterControlDeviceStatus.getDeviceStatus(trigger.getSensorDeviceId());
                if(occupancyStatus>0){
                    int nowOccupancyStutus = deviceStatesMap.get("Occupancy")==null?-1:Integer.parseInt(deviceStatesMap.get("Occupancy").toString());
                    LOGGER.info("Occupancy========status============**********nowOccupancyStutus="+nowOccupancyStutus);
                    flagHaveOccupancy =  isSceneClose(trigger.getSensorDeviceId(),message.getTenantId(),nowOccupancyStutus)?true:false;
                    if(flagHaveOccupancy){
                        occupancyTrue++ ;
                    }else {
                        occupancyFalse++;
                    }
                }
            }
        }*/

        /*if(flagNoOccupancy && occupancyTrue>0 && occupancyFalse ==0){*/

            try{
                String type = (String) msg.get("type");
                if(StringUtil.isNotEmpty(type)){
                    if(type.equals("sence") || type.equals("template-scene")){
                        //ifttt联调scene  或者  ifttt模板 设备类型--情景
                        //msg = "\"sceneId\":" + actuatorVo.getDeviceId() + "\",\"type\":" + type + "\"";
                        Long sceneId = Long.valueOf(msg.get("sceneId")+"");
                        sceneService.sceneExecute(message.getTenantId(),sceneId);
                    }
                    //不用
                    else if(type.equals("space")){
                        //ifttt联调space
                        //msg = "\"spaceId\":" + actuatorVo.getDeviceId() + "\",\"type\":" + type + "\",\"OnOff\":" + map.get("OnOff") + ",\"Blink\":" + map.get("Blink") + ",\"Dimming\":" +map.get("Dimming") + ",\"Duration\":" + map.get("Duration") + "\"";
                        //重构前
                        //IActuatorExecutor actuatorExecutor =  actuatorExecutor = ApplicationContextHelper.getBean(SpaceActuatorExecutor.class);
                        //msg.put("tenantId",message.getTenantId());
                        //actuatorExecutor.execute(msg);
                        /*//重构后，直接执行情景id
                        Long spaceId = Long.valueOf(msg.get("spaceId")+"");
                        //通过spaceId 查询 sceneId
                        SceneReq sceneReq = new SceneReq();
                        sceneReq.setSpaceId(spaceId);
                        List<SceneResp> list = sceneApi.sceneByParamDoing(sceneReq);
                        for(SceneResp sceneResp : list){
                            sceneService.sceneExecute(message.getTenantId(),sceneResp.getId());
                        }*/
                        Long sceneId = Long.valueOf(msg.get("sceneId")+"");
                        sceneService.sceneExecute(message.getTenantId(),sceneId);
                    }else if(type.equals("dev") || type.equals("template-business-type")){
                        //ifttt联调设备 或者 ifttt模板  业务类型--业务类型
                        // msg =  "\"deviceId\":" + actuatorVo.getDeviceId() + ",\"OnOff\":" + map.get("OnOff") + ",\"Blink\":" + map.get("Blink") + ",\"Dimming\":" +map.get("Dimming") + ",\"Duration\":" + map.get("Duration") + ",\"CCT\":" + map.get("CCT") + ",\"RGBW\":\"" + map.get("RGBW") + "\"";
                        //按照toC的说法，如果有多个执行actuctor,会发三次信息给我们
                        Map<String, Object> propertyMap = new HashMap<String, Object>();
                        String deviceId = (String) msg.get("deviceId");
                        msg.remove("deviceId");
                        iBuildingSpaceService.control(deviceId, msg);// 统一调用单控方法
                    }
                }else {
                    Map<String, Object> propertyMap = new HashMap<String, Object>();
                    String deviceId = (String) msg.get("deviceId");
                    msg.remove("deviceId");
                    iBuildingSpaceService.control(deviceId, msg);// 统一调用单控方法
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        /*}*/
    }


    private Boolean isSceneClose(String deviceId,Long orgId,Long tenantId,int nowOccupancyStutus){
        boolean flag = false;
        LOGGER.info("=====获取房间的状态=====================");
        //获取房间的状态
        //先写死spaceStatus 为0 做测试
        //int spaceStatus = 0;
        //先通过deviceId 获取spaceId，再去查询房间的状态
        Long spaceiId = service.findSpaceIdByDeviceId(deviceId,orgId,tenantId).getSpaceId();
        int spaceStatus = service.getSpaceStatus(spaceiId,orgId,tenantId);
        if(spaceStatus==0){//当房间状态为0时  返回true
            if(nowOccupancyStutus == 1){//当前Occupancy 状态为1
                flag = true;
                return flag;
            }else {
                return false;
            }
        }else if(spaceStatus==1){
            if(nowOccupancyStutus == 0){//当前Occupancy 状态为0
                flag = true;
                return flag;
            }else {
                return false;
            }
        }
        return flag;
        //return spaceStatus==0?true:false;//当房间状态为0时  返回true
    }
}
