package com.iot.shcs.scene.vo.req;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class SceneDetailReqVo implements Serializable {

	private static final long serialVersionUID = 2025580783894328456L;

	private Long tenantId;

	private String userUuid;

	private Long userId;

	private String seq;

	/** 场景id*/
	private Long sceneId;

	/** 场景项列表*/
	private ActionVo action;

}
