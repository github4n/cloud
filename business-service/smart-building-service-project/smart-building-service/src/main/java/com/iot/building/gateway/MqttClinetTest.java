package com.iot.building.gateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.util.Base64Utils;

import com.alibaba.fastjson.JSON;
import com.iot.building.helper.RuleServiceDispatcherMsg;

public class MqttClinetTest {
	
	public static void main(String[] args) {
//		connection("tcp://172.16.55.104:1883","1234456");
		connection("tcp://192.168.2.100:1883","1234456");
	}
	
    private static MqttClient client;
    private static MqttConnectOptions options;
    
	public static void connection(String host,String clientId){
		try {
			if(client == null || !client.isConnected()){
				client = new MqttClient(host, clientId, new MemoryPersistence());
			}
			if(options == null){
				// MQTT的连接设置  
				options = new MqttConnectOptions();  
		        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接  
		        options.setCleanSession(false);  
		        // 设置超时时间 单位为秒  
		        options.setConnectionTimeout(60);  
		        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制  
		        options.setKeepAliveInterval(20);
		        client.connect(options); 
			}
			client.setCallback(new MqttCallback() {
				
				@Override
				public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
					System.out.println(arg0);
					System.out.println(new String(arg1.getPayload()));
				}
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken arg0) {
					System.out.print("------deliveryComplete------");
				}
				
				@Override
				public void connectionLost(Throwable arg0) {
					System.out.print("------connectionLost------"+arg0.getMessage());
				}
			});
			System.out.println(client.isConnected()+"--77777777777777777777");
			client.subscribe("iot/cloud20/v1/client/123456/cloud/json");
			client.subscribe("iot/cloud20/v1/location/000000/device/json");
			client.subscribe("iot/cloud20/v1/client/123456/device/json");
//			String json = replace(JSON.toJSON(snyScene()).toString());
//			String json = replace(JSON.toJSON(delScene()).toString());
			String json = replace(JSON.toJSON(switchScene()).toString());
//			String json = replace(JSON.toJSON(deviceMount()).toString());
//			String json = replace(JSON.toJSON(objectControl()).toString());
//			String json = replace(JSON.toJSON(delObject()).toString());
			System.out.println(json);
			publish("iot/cloud20/v1/client/000000/app/json", json);
		}catch (MqttException ee) {
			ee.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void publish(String topic,String payload) throws MqttPersistenceException, MqttException{
		client.publish(topic, payload.getBytes(), 1, false);
	}
	
	/**
	 * 获取设备列表
	 * @return
	 */
	public static RuleServiceDispatcherMsg getDeviceList(){
		RuleServiceDispatcherMsg dispatcherMsg=new RuleServiceDispatcherMsg();
		dispatcherMsg.setSERVICE("DeviceService");
		dispatcherMsg.setMETHOD("queryDeviceList");
		dispatcherMsg.setREQUEST_ID("74644dafe58042a6928ac7719c095a99");
		dispatcherMsg.setREQUEST_CLIENT_ID("123456");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("pageSize",100);
		param.put("offset",0);
		param.put("deviceType","");
		dispatcherMsg.setMSG_BODY(param);
		return dispatcherMsg;
	}
	
	/**
	 * 获取设备属性
	 * @return
	 */
	public static RuleServiceDispatcherMsg getDeviceProperty(){
		RuleServiceDispatcherMsg dispatcherMsg=new RuleServiceDispatcherMsg();
		dispatcherMsg.setSERVICE("DeviceService");
		dispatcherMsg.setMETHOD("queryDeviceProperties");
		dispatcherMsg.setREQUEST_ID("73ed0d472a193e7f42fa6a1f8d139342");
		dispatcherMsg.setREQUEST_CLIENT_ID("123456");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("deviceId","6b8977792ed83166cd8ffb84b077c94a");
		param.put("queryType",0);
		dispatcherMsg.setMSG_BODY(param);
		return dispatcherMsg;
	}
	
	/**
	 * 控制设备
	 * @return
	 */
	public static RuleServiceDispatcherMsg controlDevice(){
		RuleServiceDispatcherMsg dispatcherMsg=new RuleServiceDispatcherMsg();
		dispatcherMsg.setSERVICE("DeviceService");
		dispatcherMsg.setMETHOD("deviceControl");
		dispatcherMsg.setREQUEST_ID("29501b204499486c8f80cb5a50c0c083");
		dispatcherMsg.setREQUEST_CLIENT_ID("123456");
		Map<String,Object> param=new HashMap<String,Object>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("name", "OnOff");
		paramMap.put("type", "boolean");
		paramMap.put("data","0");
		list.add(paramMap);
		param.put("desiredStatus",list);
		param.put("deviceId","d5093d0742d3f573d328eca8e7ee112e");
		dispatcherMsg.setMSG_BODY(param);
		return dispatcherMsg;
	}
	
	/**
	 * 搜索添加设备
	 * @return
	 */
	public static RuleServiceDispatcherMsg addDevice(){
		RuleServiceDispatcherMsg dispatcherMsg=new RuleServiceDispatcherMsg();
		dispatcherMsg.setSERVICE("DeviceService");
		dispatcherMsg.setMETHOD("searchDevice");
		dispatcherMsg.setREQUEST_ID("29501b204499486c8f80cb5a50c0c083");
		dispatcherMsg.setREQUEST_CLIENT_ID("123456");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("intervalTime",10);
		param.put("searchTime",30);
		dispatcherMsg.setMSG_BODY(param);
		return dispatcherMsg;
	}
	
	/**
	 * 同步情景
	 * @return
	 */
	public static RuleServiceDispatcherMsg Scene(){
		RuleServiceDispatcherMsg dispatcherMsg=new RuleServiceDispatcherMsg();
		dispatcherMsg.setSERVICE("SceneService");
		dispatcherMsg.setMETHOD("reportSceneDefinition");
		dispatcherMsg.setREQUEST_ID("29501b204499486c8f80cb5a50c0c083");
		dispatcherMsg.setREQUEST_CLIENT_ID("123456");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("sceneName",10);
		param.put("sceneIcon",30);
		dispatcherMsg.setMSG_BODY(param);
		return dispatcherMsg;
	}
	
	/**
	 * 添加情景
	 * @return
	 */
	public static RuleServiceDispatcherMsg snyScene(){
		RuleServiceDispatcherMsg dispatcherMsg=new RuleServiceDispatcherMsg();
		dispatcherMsg.setSERVICE("SceneService");
		dispatcherMsg.setMETHOD("deviceStatusCollectionSetting");
		dispatcherMsg.setREQUEST_ID("29501b204499486c8f80cb5a50c0c083");
		dispatcherMsg.setREQUEST_CLIENT_ID("123456");
		//删除情景设备
		List<String> delList=new ArrayList<>();
//		delList.add("d5093d0742d3f573d328eca8e7ee112e");
		//修改情景设备
		List<Map<String,Object>> modfindList=new ArrayList<>();
//		Map<String,Object> modfindMap=new HashMap<String,Object>();
//		modfindMap.put("deviceId", "d5093d0742d3f573d328eca8e7ee112e");
//		List<Map<String,Object>> modfindProperytList=new ArrayList<>();
//		Map<String,Object> modfindStatus=new HashMap<String,Object>();
//		modfindStatus.put("name", "OnOff");
//		modfindStatus.put("type", "boolean");
//		modfindStatus.put("data", "0");
//		modfindProperytList.add(modfindStatus);
//		modfindMap.put("settings", modfindProperytList);
//		modfindList.add(modfindMap);
		
		//添加情景设备
		List<Map<String,Object>> propertyList=new ArrayList<>();
		//RGB
		List<Map<String,Object>> desiredStatusList=new ArrayList<>();
		Map<String,Object> propertyMap=new HashMap<String,Object>();
		propertyMap.put("deviceId", "c5c1f35ba2ad55766f8849f9b8514742");
		//Dimmable
//		List<Map<String,Object>> desiredStatusList2=new ArrayList<>();
//        Map<String,Object> propertyMap2=new HashMap<String,Object>();
//        propertyMap2.put("deviceId", "d5093d0742d3f573d328eca8e7ee112e");
        
        
		Map<String,Object> desiredStatus=new HashMap<String,Object>();
		desiredStatus.put("access","rw");
		desiredStatus.put("name", "OnOff");
		desiredStatus.put("type", "boolean");
		desiredStatus.put("data", "1");
		desiredStatusList.add(desiredStatus);
//		desiredStatusList2.add(desiredStatus);
		
		//Dimming
		Map<String,Object> desiredStatus2=new HashMap<String,Object>();
		desiredStatus2.put("access","rw");
		desiredStatus2.put("name", "Dimming");
		desiredStatus2.put("type", "int[0:100]");
		desiredStatus2.put("data", "50");
        desiredStatusList.add(desiredStatus2);
//        desiredStatusList2.add(desiredStatus2);
                           
        //Blink
        Map<String,Object> desiredStatus3=new HashMap<String,Object>();
        desiredStatus3.put("access","rw");
        desiredStatus3.put("name", "Blink");
        desiredStatus3.put("type", "unsignedInt");
        desiredStatus3.put("data", "0");
        desiredStatusList.add(desiredStatus3);
//        desiredStatusList2.add(desiredStatus3);
                    
        //Duration
        Map<String,Object> desiredStatus4=new HashMap<String,Object>();
        desiredStatus4.put("access","rw");
        desiredStatus4.put("name", "Duration");
        desiredStatus4.put("type", "unsignedInt");
        desiredStatus4.put("data", "10");
        desiredStatusList.add(desiredStatus4);
//        desiredStatusList2.add(desiredStatus4);
        
        //CCT
        Map<String,Object> desiredStatus5=new HashMap<String,Object>();
        desiredStatus5.put("access","rw");
        desiredStatus5.put("name", "CCT");
        desiredStatus5.put("type", "int[2700:6500]");
        desiredStatus5.put("data", "3946");
        desiredStatusList.add(desiredStatus5);
//        
        //RGBW
        Map<String,Object> desiredStatus6=new HashMap<String,Object>();
        desiredStatus6.put("access","rw");
        desiredStatus6.put("name", "RGBW");
        desiredStatus6.put("type", "int");
        desiredStatus6.put("data", "251723520");
        desiredStatusList.add(desiredStatus6);
//		
		propertyMap.put("settings", desiredStatusList);
//		propertyMap2.put("settings", desiredStatusList2);
        propertyList.add(propertyMap);
//        propertyList.add(propertyMap2);
		
//		Map<String,Object> propertyMap_=new HashMap<String,Object>();
//		List<Map<String,Object>> desiredStatusList_=new ArrayList<>();
//		propertyMap_.put("deviceId", "c5c1f35ba2ad55766f8849f9b8514742");
//		Map<String,Object> desiredStatus_=new HashMap<String,Object>();
//		desiredStatus_.put("name", "OnOff");
//		desiredStatus_.put("type", "boolean");
//		desiredStatus_.put("data", "1");
//		desiredStatusList_.add(desiredStatus_);
//		propertyMap_.put("settings", desiredStatusList_);
//		propertyList.add(propertyMap_);
		
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("sceneId",com.iot.util.ToolUtils.getUUID());
		param.put("sceneName",Base64Utils.encode("123456".getBytes()));
		param.put("sceneIcon","romantic");
		param.put("delaySeconds",0);
		param.put("addDevicesSetting",propertyList);
		param.put("modifiedDevicesSetting",modfindList);
		param.put("removeDevicesSetting",delList);
		param.put("triggerIftttIds",new ArrayList());
		dispatcherMsg.setMSG_BODY(param);
		return dispatcherMsg;
	}
	
	/**
	 * 切换情景
	 * @return
	 */
	public static RuleServiceDispatcherMsg switchScene(){
		RuleServiceDispatcherMsg dispatcherMsg=new RuleServiceDispatcherMsg();
		dispatcherMsg.setSERVICE("SceneService");
		dispatcherMsg.setMETHOD("sceneSwitch");
		dispatcherMsg.setREQUEST_ID("29501b204499486c8f80cb5a50c0c083");
		dispatcherMsg.setREQUEST_CLIENT_ID("123456");
		Map<String,Object> param=new HashMap<String,Object>(); 
		// 535929edb4e340a7837b57c844495dc5
        param.put("sceneId","535929edb4e340a7837b57c844495dc5");
		dispatcherMsg.setMSG_BODY(param);
		return dispatcherMsg;
	}
	
	/**
	 * 删除情景
	 * @return
	 */
	public static RuleServiceDispatcherMsg delScene(){
		RuleServiceDispatcherMsg dispatcherMsg=new RuleServiceDispatcherMsg();
		dispatcherMsg.setSERVICE("SceneService");
		dispatcherMsg.setMETHOD("deleteScene");
		dispatcherMsg.setREQUEST_ID("29501b204499486c8f80cb5a50c0c083");
		dispatcherMsg.setREQUEST_CLIENT_ID("123456");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("sceneId","ce4d43e7874b4437b888ef9b29a2e1ef");
		dispatcherMsg.setMSG_BODY(param);
		return dispatcherMsg;
	}
	
//	/**
//	 * 房间树关系
//	 * @return
//	 */
//	public static RuleServiceDispatcherMsg reportDataStructure(){
//		RuleServiceDispatcherMsg dispatcherMsg=new RuleServiceDispatcherMsg();
//		dispatcherMsg.setSERVICE("ObjectService");
//		dispatcherMsg.setMETHOD("reportDataStructure");
//		dispatcherMsg.setREQUEST_ID("29501b204499486c8f80cb5a50c0c083");
//		dispatcherMsg.setREQUEST_CLIENT_ID("123456");
//		Map<String,Object> param=new HashMap<String,Object>();
//		param.put("locationId",ToolUtils.getUUID());
//		param.put("objectType","ROOM");
//		List<Map<String,Object>> desiredStatusList=new ArrayList<Map<String,Object>>();
//		Map<String,Object> dataStructureMap=new HashMap<String,Object>();
//		dataStructureMap.put("objectId","123456789");
//		dataStructureMap.put("objectName","center");
//		dataStructureMap.put("objectType","FLOOR");
//		dataStructureMap.put("objectIcon","ICON");
//		dataStructureMap.put("parentObjectId","");
//		dataStructureMap.put("parentObjectType","BUILD");
//		desiredStatusList.add(dataStructureMap);
//		Map<String,Object> dataStructureMap2=new HashMap<String,Object>();
//		dataStructureMap2.put("objectId","987654321");
//		dataStructureMap2.put("objectName","center2");
//		dataStructureMap2.put("objectType","ROOM");
//		dataStructureMap2.put("objectIcon","ICON");
//		dataStructureMap2.put("parentObjectId","123456789");
//		dataStructureMap2.put("parentObjectType","FLOOR");
//		desiredStatusList.add(dataStructureMap2);
//		param.put("dataStructure",desiredStatusList);
//		dispatcherMsg.setMSG_BODY(param);
//		return dispatcherMsg;
//	}
//	
	/**
	 * 房间挂载
	 * @return
	 */
	public static RuleServiceDispatcherMsg deviceMount(){
		RuleServiceDispatcherMsg dispatcherMsg=new RuleServiceDispatcherMsg();
		dispatcherMsg.setSERVICE("ObjectService");
		dispatcherMsg.setMETHOD("deviceMount");
		dispatcherMsg.setREQUEST_ID("a95404d3018843daa99f7b2d5219e635");
		dispatcherMsg.setREQUEST_CLIENT_ID("123456");
		Map<String,Object> param=new HashMap<String,Object>();
		String objectId=com.iot.util.ToolUtils.getUUID();
		param.put("objectId","b1f95ea1e5604a428ab760dc9a4bf934");
		param.put("objectName",Base64Utils.encode("center".getBytes()));
		param.put("objectIcon","ICON");
		param.put("objectType","ROOM");
		List<String> addDeviceIds=new ArrayList<String>();
//		addDeviceIds.add("c5c1f35ba2ad55766f8849f9b8514742");
		addDeviceIds.add("d5093d0742d3f573d328eca8e7ee112e");
		param.put("addDeviceIds",addDeviceIds);
		param.put("removeDeviceIds",new ArrayList<String>());
		dispatcherMsg.setMSG_BODY(param);
		return dispatcherMsg;
	}
	
	/**
	 * 房间控制
	 * @return
	 */
	public static RuleServiceDispatcherMsg objectControl(){
		RuleServiceDispatcherMsg dispatcherMsg=new RuleServiceDispatcherMsg();
		dispatcherMsg.setSERVICE("ObjectService");
		dispatcherMsg.setMETHOD("objectControl");
		dispatcherMsg.setREQUEST_ID("29501b204499486c8f80cb5a50c0c083");
		dispatcherMsg.setREQUEST_CLIENT_ID("123456");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("deviceType","sfsdff");
		Map<String,Object> desiredStatusMap=new HashMap<String,Object>();
		desiredStatusMap.put("name", "AllOnOff");
		desiredStatusMap.put("type", "boolean");
		desiredStatusMap.put("data", "0");
		param.put("desiredStatus",desiredStatusMap);
		param.put("objectId","a95404d3018843daa99f7b2d5219e635");
		dispatcherMsg.setMSG_BODY(param);
		return dispatcherMsg;
	}
	
	/**
	 * 房间删除
	 * @return
	 */
	public static RuleServiceDispatcherMsg delObject(){
		RuleServiceDispatcherMsg dispatcherMsg=new RuleServiceDispatcherMsg();
		dispatcherMsg.setSERVICE("ObjectService");
		dispatcherMsg.setMETHOD("deleteObject");
		dispatcherMsg.setREQUEST_ID("29501b204499486c8f80cb5a50c0c083");
		dispatcherMsg.setREQUEST_CLIENT_ID("123456");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("objectId","cce092f009034070a3542e3d51975b35");
		dispatcherMsg.setMSG_BODY(param);
		return dispatcherMsg;
	}
	
	private static String replace(String json) {
		json=json.replace("cODE", "CODE").replace("mETHOD", "METHOD").replace("mSG_BODY", "MSG_BODY")
				.replace("rEQUEST_ID", "REQUEST_ID").replace("sERVICE", "SERVICE").replace("rEQUEST_CLIENT_ID","REQUEST_CLIENT_ID")
				.replace("rEQUEST_TOPIC", "REQUEST_TOPIC");
		return json;
	}
}
