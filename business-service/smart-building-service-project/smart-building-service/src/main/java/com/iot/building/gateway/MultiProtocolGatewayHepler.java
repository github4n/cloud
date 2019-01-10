package com.iot.building.gateway;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.building.ifttt.vo.ActuatorVo;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.utils.ValueUtils;
import com.iot.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.helper.Constants;
import com.iot.building.listener.MQTTClientListener;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.util.ToolUtils;

public class MultiProtocolGatewayHepler {

	private static final Logger loger = LoggerFactory.getLogger(MultiProtocolGatewayHepler.class);

	private final static int QOS = 1;

	private static MqttSdkService mqttSdkService=ApplicationContextHelper.getBean(MqttSdkService.class);

	private static Environment dnvironment=ApplicationContextHelper.getBean(Environment.class);


	/**
	 * 创建ifttt---device
	 * @param clientId
	 * @param ruleVO
	 * @throws Exception
	 */
	public static void createIfttt(String clientId,SaveIftttReq ruleVO)throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("ifttt");
		msg.setMethod("create_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String, Object> param = Maps.newHashMap();
		param.put("uuid", ruleVO.getId()+"");
		param.put("name", ruleVO.getName());
		param.put("icon","getup.png");
		param.put("enabled",1);
		Map<String, Object> ifParam = Maps.newHashMap();
		Map<String, Object> valid = Maps.newHashMap();
		int[] week = new int[]{ 0, 1, 2, 3, 4, 5, 6 };
		valid.put("begin","00:00");
		valid.put("end","00:00");
		valid.put("week",week);
		ifParam.put("valid",valid);
		String type = null;
		if(CollectionUtils.isNotEmpty(ruleVO.getRelations())){
			type = ruleVO.getRelations().get(0).getType();
			if(type.equals("AND")){
				ifParam.put("logic","and");
			}else if(type.equals("OR")){
				ifParam.put("logic","or");
			}
		}else {
			ifParam.put("logic", "or");
		}
		List<Map<String, Object>> thenParam = Lists.newArrayList();
		List<Map<String, Object>> trigger = Lists.newArrayList();
		Map<String, Object> operate = Maps.newHashMap();
		operate.put("operate",1);
		if(StringUtil.isNotBlank(ruleVO.getType()) && ruleVO.getType().equals("template")){//模板生成具体的ifttt
			for (SensorVo sensorVo : ruleVO.getSensorSingle()){
				Map<String, Object> triggerChild = Maps.newHashMap();
				triggerChild.put("type","device");
				triggerChild.put("did",sensorVo.getDeviceId());
				if(sensorVo.getType().equals("Sensor_Doorlock")){
					triggerChild.put("trigger","prop.Door");
					JSONArray jsonArray = JSONObject.parseArray(sensorVo.getProperties());
					String triggerValue = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerValue"));
					if(triggerValue.equals("1")){
						triggerChild.put("range",1);
					}else if(triggerValue.equals("0")){
						triggerChild.put("range",0);
					}
				}else if(sensorVo.getType().equals("key_remote13")){
					triggerChild.put("trigger","event.button");
					JSONArray jsonArray = JSONObject.parseArray(sensorVo.getProperties());
					String triggerValue = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerValue"));
					if(triggerValue.contains("[") || triggerValue.contains("]")){
						String bb = triggerValue.substring(1,triggerValue.length()-1);
						String[] cc = bb.split(",");
						int[] ints = new int[cc.length];
						for(int i=0;i<cc.length;i++){
							ints[i] = Integer.parseInt(cc[i]);
						}
						triggerChild.put("range",ints);
					}else {
						String[] cc = triggerValue.split(",");
						int[] ints = new int[cc.length];
						for(int i=0;i<cc.length;i++){
							ints[i] = Integer.parseInt(cc[i]);
						}
						triggerChild.put("range",ints);
					}
				}else if(sensorVo.getType().equals("Sensor_Waterleak")){
					triggerChild.put("trigger","prop.Alarm");
					JSONArray jsonArray = JSONObject.parseArray(sensorVo.getProperties());
					String triggerValue = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerValue"));
					if(triggerValue.equals("1")){
						triggerChild.put("range",1);
					}else if(triggerValue.equals("0")){
						triggerChild.put("range",0);
					}
				}else if(sensorVo.getType().equals("Sensor_PIR")){
					triggerChild.put("trigger","prop.Occupancy");
					JSONArray jsonArray = JSONObject.parseArray(sensorVo.getProperties());
					String triggerValue = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerValue"));
					if(triggerValue.equals("1")){
						triggerChild.put("range",1);
					}else if(triggerValue.equals("0")){
						triggerChild.put("range",0);
					}
				}else {
					JSONArray jsonArray = JSONObject.parseArray(sensorVo.getProperties());
					String triggerValue = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerValue"));
					if(triggerValue.equals("1")){
						triggerChild.put("range",1);
					}else if(triggerValue.equals("0")){
						triggerChild.put("range",0);
					}
					triggerChild.put("trigger",sensorVo.getType());
					triggerChild.put("range",1);
				}
				triggerChild.put("compare",108);
				trigger.add(triggerChild);
			}
			ifParam.put("trigger",trigger);
			ifParam.put("logic", "or");//模板的为or
			for(ActuatorVo actuatorVo : ruleVO.getActuatorSingle()){
				Map<String, Object> thenChild = Maps.newHashMap();
				if(actuatorVo.getType().equals("sence")){//ifttt  业务类型--情景
					thenChild.put("type","scene");
					thenChild.put("uuid",actuatorVo.getDeviceId());
					thenChild.put("method","operate");
					thenChild.put("params",operate);
					thenParam.add(thenChild);
				}else {//ifttt  业务类型--业务类型
					thenChild.put("type","device");
					thenChild.put("uuid",actuatorVo.getDeviceId());
					thenChild.put("method","set_prop");
					Map<String, Object> params = JSON.parseObject(actuatorVo.getProperties(), Map.class);
					Map<String, Object> params2 = JSON.parseObject(params.get("device").toString(), Map.class);
					//转换RGBW
					if(params2.containsKey("RGBW")){
						int RGBW = com.iot.building.helper.ToolUtils.convertToColorInt(params2.get("RGBW").toString());
						params2.put("RGBW",RGBW);
					}
					if(params2.containsKey("Blink")){
						params2.remove("Blink");
					}
					if(params2.containsKey("Duration")){
						params2.remove("Duration");
					}
					if(params2.get("OnOff").toString().equals("0")){
						params2.remove("RGBW");
						params2.remove("Dimming");
					}
                    Map<String,Object> newMap=new LinkedHashMap();
                    if(params2.containsKey("OnOff")) {
                        newMap.put("OnOff", params2.get("OnOff"));
                    }
                    for(String key:params2.keySet()) {
                        if(!key.equals("OnOff")) {
                            newMap.put(key, params2.get(key));
                        }
                    }
					params.put("device",newMap);
					thenChild.put("params",params.get("device"));
					thenParam.add(thenChild);
				}
			}
		}else {//普通的设备生成ifttt
			for (SensorVo sensorVo : ruleVO.getSensors()){
				Map<String, Object> triggerChild = Maps.newHashMap();
				triggerChild.put("type","device");
				triggerChild.put("did",sensorVo.getDeviceId());
				if(sensorVo.getType().equals("Sensor_Doorlock")){
					triggerChild.put("trigger","prop.Door");
					JSONArray jsonArray = JSONObject.parseArray(sensorVo.getProperties());
					String triggerValue = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerValue"));
					if(triggerValue.equals("1")){
						triggerChild.put("range",1);
					}else if(triggerValue.equals("0")){
						triggerChild.put("range",0);
					}
				}else if(sensorVo.getType().equals("key_remote13")){
					triggerChild.put("trigger","event.button");
					JSONArray jsonArray = JSONObject.parseArray(sensorVo.getProperties());
					String triggerValue = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerValue"));
					String bb = triggerValue.substring(1,triggerValue.length()-1);
					String[] cc = bb.split(",");
					int[] ints = new int[cc.length];
					for(int i=0;i<cc.length;i++){
						ints[i] = Integer.parseInt(cc[i]);
					}
					triggerChild.put("range",ints);
				}else {
					JSONArray jsonArray = JSONObject.parseArray(sensorVo.getProperties());
					String triggerValue = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerValue"));
					if(triggerValue.equals("1")){
						triggerChild.put("range",1);
					}else if(triggerValue.equals("0")){
						triggerChild.put("range",0);
					}
					triggerChild.put("trigger",sensorVo.getType());
				}
				triggerChild.put("compare",108);
				trigger.add(triggerChild);
			}
			ifParam.put("trigger",trigger);
			Map<String,Object> map = new HashMap<String,Object>();
			for(ActuatorVo actuatorVo : ruleVO.getActuators()){
				Map<String, Object> thenChild = Maps.newHashMap();
				if(actuatorVo.getType().equals("sence")){//情景
					thenChild.put("type","scene");
					thenChild.put("uuid",actuatorVo.getDeviceId());
					thenChild.put("method","operate");
					thenChild.put("params",operate);
					thenParam.add(thenChild);
				}else if(actuatorVo.getType().equals("space")){//空间
					thenChild.put("type","scene");
					map = ValueUtils.jsonStringToMap(actuatorVo.getProperties());
					thenChild.put("uuid",map.get("senceId").toString());
					thenChild.put("method","operate");
					thenChild.put("params",operate);
					thenParam.add(thenChild);
				}
				else {//设备
					thenChild.put("type","device");
					thenChild.put("uuid",actuatorVo.getDeviceId());
					thenChild.put("method","set_prop");
					Map<String, Object> params = JSON.parseObject(actuatorVo.getProperties(), Map.class);
					Map<String, Object> params2 = JSON.parseObject(params.get("device").toString(), Map.class);
					//转换RGBW
					if(params2.containsKey("RGBW")){
						int RGBW = com.iot.building.helper.ToolUtils.convertToColorInt(params2.get("RGBW").toString());
						params2.put("RGBW",RGBW);
					}
					if(params2.containsKey("Blink")){
						params2.remove("Blink");
					}
					if(params2.containsKey("Duration")){
						params2.remove("Duration");
					}
					if(params2.get("OnOff").toString().equals("0")){
						params2.remove("RGBW");
						params2.remove("Dimming");
					}
                    Map<String,Object> newMap=new LinkedHashMap();
                    if(params2.containsKey("OnOff")) {
                        newMap.put("OnOff", params2.get("OnOff"));
                    }
                    for(String key:params2.keySet()) {
                        if(!key.equals("OnOff")) {
                            newMap.put(key, params2.get(key));
                        }
                    }
                    params.put("device",newMap);
					thenChild.put("params",params.get("device"));
					thenParam.add(thenChild);
				}
			}
		}
		param.put("if",ifParam);
		param.put("then",thenParam);
		msg.setTopic("iot/v1/c/"+clientId+"/ifttt/create_req");
		msg.setPayload(param);
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}


	/**
	 * 4.3	子设备方法调用
	 * @param clientId
	 * @param deviceId
	 * @param propertyMap
	 * @throws Exception
	 */
	public static void invokeReq(String clientId,String deviceId,Map<String,Object> propertyMap)throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("subdevice");
		msg.setMethod("invoke_req");
		Map<String, Object> param = Maps.newHashMap();
		param.put("did", deviceId);
		param.put("method", "set_rgb");
		List<Integer> params=new ArrayList<Integer>();//TODO 具体参数要具体修改
		param.put("params", params);

		msg.setSeq(ToolUtils.getUUID().substring(0, 10));
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		msg.setTopic("iot/v1/c/"+clientId+"/subdevice/invoke_req");
		msg.setPayload(param);
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 重启网关(网关重置)
	 * @param clientId
	 * @Author 林济煌
	 */
	public static void resetReq(String clientId){
		MqttMsg msg =new MqttMsg();
		msg.setService("device");
		msg.setMethod("reset_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String, Object> param = Maps.newHashMap();
		msg.setTopic("iot/v1/c/"+clientId+"/scene/delete_req");
		msg.setPayload(param);
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 删除情景
	 * @param clientId
	 * @param sceneId
	 * @Author 林济煌
	 */
	public static void deleteScene(String clientId,Long sceneId){
		MqttMsg msg =new MqttMsg();
		msg.setService("scene");
		msg.setMethod("delete_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String, Object> param = Maps.newHashMap();
		param.put("uuid", String.valueOf(sceneId));
		msg.setTopic("iot/v1/c/"+clientId+"/scene/delete_req");
		msg.setPayload(param);
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 删除设备
	 * @param clientId
	 * @param deviceId
	 * @Author 林济煌
	 */
	public static void deleteDevice(String clientId,String deviceId){
		MqttMsg msg =new MqttMsg();
		msg.setService("subdevice");
		msg.setMethod("remove_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String, Object> param = Maps.newHashMap();
		param.put("did", deviceId);
		msg.setTopic("iot/v1/c/"+clientId+"/subdevice/remove_req");
		msg.setPayload(param);
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 删除ifttt
	 * @param clientId
	 * @param ruleId
	 */
	public static void deleteIfttt(String clientId,Long ruleId){
		MqttMsg msg =new MqttMsg();
		msg.setService("ifttt");
		msg.setMethod("delete_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String, Object> param = Maps.newHashMap();
		param.put("uuid", ruleId);
		msg.setTopic("iot/v1/c/"+clientId+"/ifttt/delete_req");
		msg.setPayload(param);
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 开启/关闭联动
	 * @param clientId
	 * @param ruleId
	 * @param start
	 */
	public static void runIfttt(String clientId,Long ruleId,Boolean start){
		MqttMsg msg =new MqttMsg();
		msg.setService("ifttt");
		msg.setMethod("enable_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String, Object> param = Maps.newHashMap();
		param.put("uuid", ruleId);
		if(start == true){
			param.put("enabled", 1);
		}else if(start == false){
			param.put("enabled", 0);
		}
		msg.setTopic("iot/v1/c/"+clientId+"/ifttt/delete_req");
		msg.setPayload(param);
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 情景执行
	 * @param clientId
	 * @param sceneId 情景ID
	 * @return
	 */
	public static void executeScene(String clientId,Long sceneId) throws Exception{
		//第三版新协议修改
		MqttMsg msg =new MqttMsg();
		msg.setService("scene");
		msg.setMethod("operate_req");
		Map<String, Object> param = Maps.newHashMap();
		param.put("uuid", String.valueOf(sceneId));
		msg.setSeq(ToolUtils.getUUID().substring(0, 10));
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		msg.setTopic("iot/v1/c/"+clientId+"/scene/operate_req");
		msg.setPayload(param);
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 编辑场景信息
	 * @param clientId
	 * @param sceneId 情景ID
	 * @param sceneDetail
	 * @return
	 */
	public static void modifiedScene(String clientId,Long sceneId,SceneDetailResp sceneDetail) throws Exception{
		//第三版新协议修改
		MqttMsg msg =new MqttMsg();
		msg.setService("scene");
		msg.setMethod("update_action_req");
		Map<String, Object> param = Maps.newHashMap();
		param.put("uuid", sceneId.toString());
		Map<String, Object> action = Maps.newHashMap();
		action.put("type", "device");
		action.put("uuid", sceneDetail.getDeviceId());
		action.put("method", "set_prop");
		action.put("params", sceneDetail.getTargetValue());
		param.put("action", action);
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		msg.setTopic("iot/v1/c/"+clientId+"/scene/update_action_req");
		msg.setPayload(param);
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 添加场景项
	 * @param clientId
	 * @param sceneId 情景ID
	 * @param sceneDetail 情景详情
	 * @return
	 */
	public static void addScene(String clientId,Long sceneId, SceneDetailResp sceneDetail, boolean isSilent) throws Exception{
		//第三版协议修改
		MqttMsg msg =new MqttMsg();
		msg.setService("scene");
		msg.setMethod("add_action_req");
		Map<String, Object> param = Maps.newHashMap();
		param.put("uuid", String.valueOf(sceneId));
		Map<String, Object> action = Maps.newHashMap();
		Map<String, Object> attr = JSON.parseObject(sceneDetail.getTargetValue());
		if (isSilent) {
			action.put("mode", "add");
		}
		action.put("type", "device");
		action.put("uuid", sceneDetail.getDeviceId());
		action.put("method", "set_prop");
		action.put("params", attr);
		param.put("action", action);
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		msg.setTopic("iot/v1/c/"+clientId+"/scene/add_action_req");
		msg.setPayload(param);
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 删除情景项
	 * @param clientId
	 * @param sceneId 情景ID
	 * @param sceneDetail 情景详情
	 * @return
	 */
	public static void delScene(String clientId,Long sceneId, SceneDetailResp sceneDetail) throws Exception{
		//修改第三版协议
		MqttMsg msg =new MqttMsg();
		msg.setService("scene");
		msg.setMethod("del_action_req");
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("uuid", sceneId.toString());
		Map<String,Object> attr=new HashMap<String, Object>();
		attr.put("type", "device");
		attr.put("uuid", sceneDetail.getDeviceId());
		param.put("action", attr);
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		msg.setTopic("iot/v1/c/"+clientId+"/scene/del_action_req");
		msg.setPayload(param);
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 群控开关
	 * @param clientId
	 * @param objectId  房间Id
	 * @return
	 */
	public static void groupControl(String clientId,String objectId,Map<String,Object> targerValue) throws Exception{
		//第三版修改协议
		MqttMsg msg =new MqttMsg();
		msg.setService("group");
		msg.setMethod("operate_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("uuid", objectId);
		param.put("method", "set_prop");
		param.put("params", targerValue);
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/group/operate_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}



	/**
	 * 控制设备
	 * @param clientId
	 * 属性格式 ：{"desiredStatus":[{"name":"OnOff","type":"boolean","data":"1"}],"deviceId":"a914697f12bbd758fce856f85d38dcb4"}
	 */
	public static void deviceControl(String clientId,String deviceId,Map<String,Object> propertyMap) throws Exception{
		//第三版修改协议
		MqttMsg msg =new MqttMsg();
		msg.setService("subdevice");
		msg.setMethod("set_prop_req");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		propertyMap.remove("deviceId");
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("did", deviceId);
		for (String key : propertyMap.keySet()) {
			if(key.equals("switch")){
				propertyMap.put("OnOff",Integer.parseInt(propertyMap.get("switch").toString()));
				propertyMap.remove("switch");
			}else if (key.equals("RGBW")) {
				int RGBW = com.iot.building.helper.ToolUtils.convertToColorInt(propertyMap.get("RGBW").toString());
				propertyMap.put("RGBW", RGBW);
			}else {
				propertyMap.put(key,Integer.parseInt(propertyMap.get(key).toString()));
			}
		}
		param.put("params", propertyMap);
		msg.setSeq("123456");
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/subdevice/set_prop_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 添加设备
	 * @param clientId
	 */
	public static void addDevice(String clientId, Integer time) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("subdevice");
		msg.setMethod("add_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("time", time);
		param.put("net", "zigbee");
//		param.put("code", "xxx");
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/subdevice/add_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 获取设备状态属性
	 * @param clientId
	 */
	public static void getDevAttrReq(String clientId,String deviceId, List<String> attr) throws Exception{
		//第三版新协议
		MqttMsg msg =new MqttMsg();
		msg.setService("subdevice");
		msg.setMethod("get_prop_req");
		msg.setSeq("123456");
		Map<String,Object> param=new HashMap<String, Object>();
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		param.put("did", deviceId);
		param.put("params", attr);
		msg.setPayload(param);
		msg.setClientId(clientId);
		msg.setRequestId(com.iot.util.ToolUtils.getUUID());
		msg.setTopic("iot/v1/c/"+clientId+"/subdevice/get_prop_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 添加分组成员
	 * @param clientId
	 * @param objectId  房间Id
	 * @param members  需要添加的设备id
	 * @return
	 */
	public static void mount(String clientId,String objectId,List<String> members) throws Exception{
		//第三版新协议
		MqttMsg msg =new MqttMsg();
		msg.setService("group");
		msg.setMethod("add_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("uuid", objectId);
		param.put("members", members);
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/group/add_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 删除分组成员
	 * @param clientId
	 * @param objectId  房间Id
	 * @param members  需要添加的设备id
	 * @return
	 */
	public static void removeMount(String clientId,String objectId,List<String> members) throws Exception{
		//第三版新协议
		MqttMsg msg =new MqttMsg();
		msg.setService("group");
		msg.setMethod("remove_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("uuid", objectId);
		param.put("members", members);
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/group/remove_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 添加分组
	 * @param clientId
	 * @param objectId  房间Id
	 * @param objectName 房间名称
	 * @param icon      图标
	 * @param model  产品model
	 * @param members  需要添加的设备id
	 * @return
	 */
	public static void createGroup(String clientId,String objectId,String objectName, String icon, String model, List<String> members) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("group");
		msg.setMethod("create_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("uuid", objectId);
		param.put("name", objectName);
		param.put("icon", icon);
		param.put("model", model);
		param.put("members", members);
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/group/create_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 删除分组
	 * @param clientId
	 * @param objectId  房间Id
	 * @return
	 */
	public static void deleteGroup(String clientId,String objectId) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("group");
		msg.setMethod("delete_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("uuid", objectId);
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/group/delete_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 编辑分组信息
	 * @param clientId
	 * @param objectId
	 * @param objectName
	 * @param icon
	 * @throws Exception
	 */
	public static void editGroup(String clientId,String objectId,String objectName, String icon) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("group");
		msg.setMethod("update_base_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("uuid", objectId);
		param.put("name", objectName);
		param.put("icon", icon);
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/group/update_base_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 查询分组成员列表
	 * @param clientId
	 * @param objectId
	 * @throws Exception
	 */
	public static void queryGroupMembersList(String clientId,String objectId) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("group");
		msg.setMethod("query_members_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("uuid", objectId);
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/group/query_members_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 查询分组列表
	 * @param clientId
	 * @throws Exception
	 */
	public static void queryGroupList(String clientId) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("group");
		msg.setMethod("list_req");
		msg.setSeq("123456");
		Map<String,Object> param=new HashMap<String, Object>();
		msg.setPayload(param);
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		msg.setTopic("iot/v1/c/"+clientId+"/group/list_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 查询子设备列表
	 * @param clientId
	 * @return
	 */
	public static void queryReq(String clientId) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("subdevice");
		msg.setMethod("query_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("did", "ffffffffffffffffffffffffffffffff");
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/subdevice/query_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 编辑子设备名称
	 * @param clientId
	 * @return
	 */
	public static void renameReq(String clientId, GetDeviceInfoRespVo device, String position, Long spaceId) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("subdevice");
		msg.setMethod("rename_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		String name = device.getName() + "|" + device.getBusinessTypeId() + "|" +
				device.getLocationId() + "|" + device.getTenantId() + "|" +
				spaceId + "|" + position + "|9999";
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("did", device.getUuid());
		param.put("name", name);
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/subdevice/query_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 更新OTA包
	 * @param clientId
	 * @param url  ota 路径
	 * @param md5  ota md5
	 * @return
	 */
	public static void otaReq(String clientId, String deviceId, String url, String md5) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("ota");
		msg.setMethod("ota_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("did", deviceId);
		param.put("url", url);
		param.put("md5", md5);
		param.put("proc", "dnld install");
		param.put("mode", "normal");
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/ota/ota_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 设置网关MQTT地址
	 * @param clientId
	 * @param host
	 * @return
	 */
	public static void setMqttHostReq(String clientId, String host) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("device");
		msg.setMethod("set_mqtt_host_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		Map<String,Object> mqttParamMap=new HashMap<String, Object>();
		List<Map<String, Object>> mqtt = Lists.newArrayList();
		mqttParamMap.put("host", host);
		mqttParamMap.put("port", 8883);
		mqtt.add(mqttParamMap);
		param.put("mqtt", mqtt);
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/device/set_mqtt_host_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 重启网关自身
	 * @param clientId
	 * @return
	 */
	public static void rebootReq(String clientId) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("device");
		msg.setMethod("reboot_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/device/reboot_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 编辑网关设备名称
	 * @param clientId
	 * @param name
	 * @return
	 */
	public static void renameReq(String clientId, String name) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("device");
		msg.setMethod("rename_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("name", name);
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/device/rename_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 获取设备的版本信息和状态
	 * @param clientId
	 * @param deviceId
	 * @return
	 */
	public static void otaQueryReq(String clientId, String deviceId) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("ota");
		msg.setMethod("query_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("did", deviceId);
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/ota/query_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 强控绑定分组协议
	 * @param clientId
	 * @param deviceId
	 * @param spaceId
	 * @return
	 */
	public static void setPropReq(String clientId, String deviceId, Long spaceId) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("subdevice");
		msg.setMethod("set_prop_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("GroupId", spaceId);
		param.put("did", deviceId);
		param.put("params", params);
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/subdevice/set_prop_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}

	/**
	 * 4.3  实时查询子设备属性值
	 * @param clientId
	 * @param deviceId
	 * @throws Exception
	 */
	public static void updatePropReq(String clientId,String deviceId,List<String> params)throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("subdevice");
		msg.setMethod("update_prop_req");
		Map<String, Object> param = Maps.newHashMap();
		param.put("did", deviceId);
		param.put("params", params);
		msg.setSeq(ToolUtils.getUUID().substring(0, 10));
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		msg.setTopic("iot/v1/c/"+clientId+"/subdevice/update_prop_req");
		msg.setPayload(param);
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}
	
	/**
	 * 获取网关信息
	 * @param clientId
	 * @return
	 */
	public static void getInfoReq(String clientId) throws Exception{
		MqttMsg msg =new MqttMsg();
		msg.setService("device");
		msg.setMethod("get_info_req");
		msg.setSeq("123456");
		String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		msg.setSrcAddr("0."+uuid);
		Map<String,Object> param=new HashMap<String, Object>();
		msg.setPayload(param);
		msg.setTopic("iot/v1/c/"+clientId+"/device/get_info_req");
		mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
	}
}
