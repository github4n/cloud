package com.iot.building.gateway;

import java.util.List;
import java.util.Map;

import com.iot.building.helper.MultiProtocolGatewayDTO;
import com.iot.building.helper.ThreadPoolUtil;
import com.iot.common.exception.BusinessException;
import com.iot.control.scene.vo.rsp.SceneDetailResp;


public class MultiProtocolGatewayNewThread  {

	/**
	 * 房间挂载设备
	 * @param clientId  
	 * @param objectId  房间Id
	 * @param objectName 方面名称 
	 * @param type      房间类型
	 * @param addDeviceIds  需要添加的设备id
	 * @param removeDeviceIds  需要删除的设备id
	 * @return
	 */
	public void deviceMountSpace(String clientId,Long objectId,String objectName,String type,List<String> addDeviceIds,List<String> removeDeviceIds) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setObjectId(objectId);
		dto.setObjectName(objectName);
		dto.setType(type);
		dto.setRemoveDeviceIds(removeDeviceIds);
		dto.setAddDeviceIds(addDeviceIds);
		commonThread(dto,"deviceMountSpace");
	}
	
	/**
	 * 删除网关设备挂载
	 * @param clientId  
	 * @param objectId  房间Id
	 * @return
	 */
	public void delSpace(String clientId,Long objectId) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setObjectId(objectId);
		commonThread(dto,"delSpace");
	}
	
	/**
	 * 群控开关
	 * @param clientId  
	 * @param objectId  房间Id
	 * @param onOff  开关值 1开 0关
	 * @return
	 */
	public void groupControl(String clientId,Long objectId,Map<String,Object> targerValue) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setObjectId(objectId);
		dto.setTargerValue(targerValue);
		commonThread(dto,"groupControl");
	}
	
	/**
	 * 获取设备类别请求
	 * @param clientId mqttClientId
	 */
	public void getDeviceList(String clientId) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		commonThread(dto,"getDeviceList");
	}
	
	/**
	 * 获取设备属性
	 * @param clientId 
	 * @param deviceId 
	 */
	public void getDeviceProperty(String clientId,String deviceId) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setDeviceId(deviceId);
		commonThread(dto,"getDeviceProperty");
	}
	
	/**
	 * 控制设备
	 * @param clientId   
	 * 属性格式 ：{"desiredStatus":[{"name":"OnOff","type":"boolean","data":"1"}],"deviceId":"a914697f12bbd758fce856f85d38dcb4"}
	 */
	public void deviceControl(String clientId,String deviceId,Map<String,Object> propertyMap) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setDeviceId(deviceId);
		dto.setTargerValue(propertyMap);
		commonThread(dto,"deviceControl");
	}
	
	/**
	 * 添加设备
	 * @param clientId   
	 */
	public void addDevice(String clientId, Integer time) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setDeviceId(clientId);
		dto.setDurationTime(time);
		commonThread(dto,"addDevice");
	}
	
	/**
	 * 同步情景信息
	 * @param clientId 
	 * @param sceneId 情景ID
	 * @param name    情景名称
	 * @param addDevicePropertyList 类型 DevicePropertyDTO 
	 * @return
	 */
	public void synScene(String clientId,Long sceneId,SceneDetailResp sceneDetail, boolean isSilent) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setSceneId(sceneId);
		dto.setSceneDetail(sceneDetail);
		dto.setSilent(isSilent);
		commonThread(dto,"synScene");
	}
	
	/**
	 * 更新情景信息
	 * @param clientId 
	 * @param sceneId 情景ID
	 * @param name    情景名称
	 * @param modifiedDeviceMapList 类型 DevicePropertyDTO 
	 * @return
	 */
	public void modifiedScene(String clientId,Long sceneId,SceneDetailResp sceneDetail) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setSceneId(sceneId);
		dto.setSceneDetail(sceneDetail);
		commonThread(dto,"modifiedScene");
	}
	
	/**
	 * 情景执行
	 * @param clientId 
	 * @param sceneId 情景ID
	 * @return
	 */
	public void executeScene(String clientId,Long sceneId) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setSceneId(sceneId);
		commonThread(dto,"executeScene");
	}
	
	/**
	 * 情景删除
	 * @param clientId 
	 * @param sceneId 情景ID
	 * @return
	 */
	public void delScene(String clientId,Long sceneId,SceneDetailResp sceneDetail) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setSceneId(sceneId);
		dto.setSceneDetail(sceneDetail);
		commonThread(dto,"delScene");
	}
	
	/**
	 * 设置网关MQTT地址
	 * @param clientId 
	 * @param host
	 * @return
	 */
	public void setMqttAddr(String clientId, String host) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setHost(host);
		commonThread(dto,"setMqttAddr");
	}
	
	/**
	 * 重启网关
	 * @param clientId 
	 * @return
	 */
	public void rebootReq(String clientId) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		commonThread(dto,"rebootReq");
	}
	
	/**
	 * 编辑网关设备名称
	 * @param clientId 
	 * @param name 
	 * @return
	 */
	public void renameReq(String clientId, String name) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setObjectName(name);
		commonThread(dto,"renameReq");
	}
	
	/**
	 * 编辑网关设备名称
	 * @param clientId 
	 * @param name 
	 * @return
	 */
	public void otaQueryReq(String clientId, String deviceId) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setDeviceId(deviceId);
		commonThread(dto,"otaQueryReq");
	}
	
	/**
	 * 删除情景(非子项)
	 * @param clientId 
	 * @param name 
	 * @return
	 */
	public void deleteScene(String clientId, Long sceneId) throws BusinessException{
		MultiProtocolGatewayDTO dto=new MultiProtocolGatewayDTO();
		dto.setClientId(clientId);
		dto.setSceneId(sceneId);
		commonThread(dto,"deleteScene");
	}
	
	/**
	 * 
	 * @param multiProtocolGatewayDTO
	 * @param mothed
	 */
	private void commonThread(MultiProtocolGatewayDTO multiProtocolGatewayDTO,String mothed){
		MultiProtocolGatewayExcuteThread newThread=new MultiProtocolGatewayExcuteThread(multiProtocolGatewayDTO,mothed);
		ThreadPoolUtil.instance().execute(newThread);
	}
	
	/**
	 * 和网关交互使用新的线程
	 * @author wanglei
	 *
	 */
	class MultiProtocolGatewayExcuteThread extends Thread{
		
		private MultiProtocolGatewayDTO dto;
		
		private String mothed;
		
		public MultiProtocolGatewayExcuteThread(MultiProtocolGatewayDTO multiProtocolGatewayDTO,String mothed){
			this.dto=multiProtocolGatewayDTO;
			this.mothed=mothed;
		}
		
		@Override
		public void run() {
			try {
				switch(mothed){
					case "groupControl":
//						MultiProtocolGatewayHepler.groupControl(dto.getClientId(),dto.getObjectId(),dto.getTargerValue());
						break;
					case "getDeviceList":
						MultiProtocolGatewayHepler.queryReq(dto.getClientId());
						break;
					case "getDeviceProperty":
//						MultiProtocolGatewayHepler.getDeviceProperty(dto.getClientId(),dto.getDeviceId());
						break;
					case "deviceControl":
						MultiProtocolGatewayHepler.deviceControl(dto.getClientId(),dto.getDeviceId(),dto.getTargerValue());
						break;
					case "addDevice":
						MultiProtocolGatewayHepler.addDevice(dto.getClientId(), dto.getDurationTime());
						break;
					case "synScene":
						MultiProtocolGatewayHepler.addScene(dto.getClientId(), dto.getSceneId(), dto.getSceneDetail(), dto.isSilent());
						break;
					case "executeScene":
						MultiProtocolGatewayHepler.executeScene(dto.getClientId(), dto.getSceneId());
						break;
					case "delScene":
						MultiProtocolGatewayHepler.delScene(dto.getClientId(), dto.getSceneId(), dto.getSceneDetail());
						break;
					case "deleteScene":
						MultiProtocolGatewayHepler.deleteScene(dto.getClientId(), dto.getSceneId());
						break;
					case "modifiedScene":
						MultiProtocolGatewayHepler.modifiedScene(dto.getClientId(), dto.getSceneId(), dto.getSceneDetail());
						break;
					case "setMqttAddr":
						MultiProtocolGatewayHepler.setMqttHostReq(dto.getClientId(), dto.getHost());
						break;
					case "rebootReq":
						MultiProtocolGatewayHepler.rebootReq(dto.getClientId());
						break;
					case "renameReq":
						MultiProtocolGatewayHepler.renameReq(dto.getClientId(), dto.getObjectName());
						break;
					case "otaQueryReq":
						MultiProtocolGatewayHepler.otaQueryReq(dto.getClientId(), dto.getDeviceId());
						break;
					default :
						System.out.println("default");
						break;
				};
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	}
	
	
	
}
