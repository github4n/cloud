package com.iot.shcs.scene.vo.req;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class ActionVo implements Serializable {

	private static final long serialVersionUID = 2025580783894328456L;

	/** 场景类型*/
	private String type;

	/** 设备uuid*/
	private String uuid;

	/** action 方法*/
	private String method;

	private Object params;

}
