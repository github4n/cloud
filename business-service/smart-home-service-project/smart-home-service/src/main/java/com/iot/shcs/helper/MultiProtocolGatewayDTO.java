package com.iot.shcs.helper;


import com.iot.shcs.scene.vo.req.SceneDetailReq;
import com.iot.shcs.scene.vo.rsp.SceneDetailResp;

import java.util.List;
import java.util.Map;


public class MultiProtocolGatewayDTO  {

	private String clientId;
	private Long objectId;
	private String deviceId;
	private String objectName;
	private String type;
	private String requestId;
	private List<String> addDeviceIds;
	private List<String> removeDeviceIds;
	private Map<String,Object> targerValue;
	private Long sceneId;
	private String sceneName;
	private List<DevicePropertyDTO> addDeviceMapList;
	private SceneDetailResp sceneDetailResp;
	
	public Map<String, Object> getTargerValue() {
		return targerValue;
	}
	public void setTargerValue(Map<String, Object> targerValue) {
		this.targerValue = targerValue;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getAddDeviceIds() {
		return addDeviceIds;
	}
	public void setAddDeviceIds(List<String> addDeviceIds) {
		this.addDeviceIds = addDeviceIds;
	}
	public List<String> getRemoveDeviceIds() {
		return removeDeviceIds;
	}
	public void setRemoveDeviceIds(List<String> removeDeviceIds) {
		this.removeDeviceIds = removeDeviceIds;
	}
    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
	public Long getSceneId() {
		return sceneId;
	}
	public void setSceneId(Long sceneId) {
		this.sceneId = sceneId;
	}
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public List<DevicePropertyDTO> getAddDeviceMapList() {
		return addDeviceMapList;
	}
	public void setAddDeviceMapList(List<DevicePropertyDTO> addDeviceMapList) {
		this.addDeviceMapList = addDeviceMapList;
	}

	public SceneDetailResp getSceneDetailResp() {
		return sceneDetailResp;
	}

	public void setSceneDetailResp(SceneDetailResp sceneDetailResp) {
		this.sceneDetailResp = sceneDetailResp;
	}
}
