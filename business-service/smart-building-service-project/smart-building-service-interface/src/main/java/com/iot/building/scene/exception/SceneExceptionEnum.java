package com.iot.building.scene.exception;


import com.iot.common.exception.IBusinessException;

public enum SceneExceptionEnum implements IBusinessException {

	SCENE_ERROR("scene.error"),
	SCENE_ADD_ERROR("scene.add.error"),
	DEVICE_ADN_USER_UNBINDING("device.and.user.unbinding"),

	SCENE_NAME_NOT_ALLOW_EMPTY(100, "scene.name.not.allow.empty"),
	SCENE_NAME_IS_EXIST(110, "scene.name.is.exist"),

	// scene action is empty
	SCENE_ACTION_IS_EMPTY(115, "scene.action.is.empty"),
	//SCENE_DEVICES_NOT_ALLOW_EMPTY(120, "scene.devices.not.allow.empty"),

	// scene未绑定 直连设备
	SCENE_UNBOUND_DIRECT_DEVICE(120, "scene.unbound.direct.device"),

	// 情景不存在
	SCENE_NOT_EXIST(130, "scene.not.exist"),

	// 参数 sceneDetail 为空
	PARAM_SCENE_DETAIL_IS_NULL(500, "param.scene.detail.is.null"),

	// 参数 sceneDetail.sceneId 为空
	PARAM_SCENE_DETAIL_SCENE_ID_IS_NULL(501, "param.scene.detail.sceneId.is.null"),

	// 参数 sceneDetail.deviceId 为空
	PARAM_SCENE_DETAIL_DEVICE_ID_IS_NULL(502, "param.scene.detail.deviceId.is.null"),

	// 情景规则不存在
	SCENE_ACTION_NOT_EXIST(600, "scene.action.not.exist"),

	// payload里action参数错误
	PAYLOAD_ACTION_ERROR(1000, "payload.action.error"),


	// 用户未绑定网关
	USER_UNBOUND_GATEWAY(10001, "user.unbound.gateway")
	;

	private int code;

	private String messageKey;

	SceneExceptionEnum(String messageKey) {
		code = 0;
		this.messageKey = messageKey;
	}

	SceneExceptionEnum(int code, String messageKey) {
		this.code = code;
		this.messageKey = messageKey;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMessageKey() {
		return messageKey;
	}

}
