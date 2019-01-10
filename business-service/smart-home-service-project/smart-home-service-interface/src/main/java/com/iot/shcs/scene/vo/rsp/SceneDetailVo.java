package com.iot.shcs.scene.vo.rsp;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class SceneDetailVo implements Serializable {

	private static final long serialVersionUID = 2025580783894328456L;

	/** 场景id*/
	private Long sceneId;

	/** 场景项列表*/
	private List<Map<String, Object>> then;

}
