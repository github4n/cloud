package com.iot.building.helper;

import java.util.List;
import java.util.Map;

import com.iot.control.scene.vo.rsp.SceneDetailResp;


public class MultiProtocolGatewayDTO  {

	private String clientId;
	private Long objectId;
	private String deviceId;
	private String objectName;
	private String type;
	private String requestId;
	private String host;
	private Integer durationTime;
	private List<String> addDeviceIds;
	private List<String> removeDeviceIds;
	private Map<String,Object> targerValue;
	private Long sceneId;
	private String sceneName;
	private List<DevicePropertyDTO> addDeviceMapList;
	private SceneDetailResp sceneDetail;
	private boolean isSilent;
	
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
	public SceneDetailResp getSceneDetail() {
		return sceneDetail;
	}
	public void setSceneDetail(SceneDetailResp sceneDetail) {
		this.sceneDetail = sceneDetail;
	}
	public Integer getDurationTime() {
		return durationTime;
	}
	public void setDurationTime(Integer durationTime) {
		this.durationTime = durationTime;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public boolean isSilent() {
		return isSilent;
	}
	public void setSilent(boolean isSilent) {
		this.isSilent = isSilent;
	}
    
}
