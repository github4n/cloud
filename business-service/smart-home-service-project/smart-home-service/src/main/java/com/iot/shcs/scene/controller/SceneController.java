package com.iot.shcs.scene.controller;

import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.space.api.SpaceApi;
import com.iot.shcs.device.vo.ControlReq;
import com.iot.shcs.scene.api.SceneApi;
import com.iot.shcs.scene.service.SceneDetailService;
import com.iot.shcs.scene.service.SceneService;
import com.iot.shcs.scene.service.impl.SceneServiceImpl;
import com.iot.shcs.scene.vo.req.SceneDetailReq;
import com.iot.shcs.scene.vo.req.SceneDetailReqVo;
import com.iot.shcs.scene.vo.req.SceneReq;
import com.iot.shcs.scene.vo.rsp.SceneDetailResp;
import com.iot.shcs.scene.vo.rsp.SceneDetailVo;
import com.iot.shcs.scene.vo.rsp.SceneResp;
import com.iot.shcs.space.service.ISpaceService;
import com.iot.shcs.space.vo.SpaceResp;
import com.iot.shcs.voicebox.queue.bean.SceneMessage;
import com.iot.shcs.voicebox.queue.sender.AddOrUpdateSceneSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class SceneController implements SceneApi {

	@Autowired
	private SceneService sceneService;
	@Autowired
	private SceneDetailService sceneDetailService;

	@Autowired
	private ISpaceService iSpaceService;
	private Logger log = LoggerFactory.getLogger(SceneController.class);


	@Override
	public SceneResp saveScene(@RequestBody SceneReq sceneReq) {
		if(sceneReq.getHomeId()==null){
			SpaceResp spaceResp=iSpaceService.findUserDefaultSpace(sceneReq.getUpdateBy(),sceneReq.getTenantId());
			if(spaceResp!=null){
				sceneReq.setHomeId(spaceResp.getId());
			}
		}
		log.debug("---saveScene_HomeId={}",sceneReq.getHomeId());
		SceneResp sceneResp = sceneService.saveScene(sceneReq);
		ApplicationContextHelper.getBean(AddOrUpdateSceneSender.class).send(SceneMessage.builder()
				.userId(sceneReq.getCreateBy())
				.sceneId(sceneResp.getId()).build());
		return sceneResp;
	}

	@Override
	public SceneResp updateScene(@RequestBody SceneReq sceneReq) {
		if(sceneReq.getHomeId()==null){
			SpaceResp spaceResp=iSpaceService.findUserDefaultSpace(sceneReq.getUpdateBy(),sceneReq.getTenantId());
			if(spaceResp!=null){
				sceneReq.setHomeId(spaceResp.getId());
			}
		}
		log.debug("---saveScene_HomeId={}",sceneReq.getHomeId());
		ApplicationContextHelper.getBean(AddOrUpdateSceneSender.class).send(SceneMessage.builder()
				.userId(sceneReq.getUpdateBy())
				.sceneId(sceneReq.getId()).build());
		return sceneService.updateScene(sceneReq);
	}

	@Override
	public List<SceneResp> findSceneRespListByUserId(@RequestParam("userId") Long userId,@RequestParam("tenantId") Long tenantId) {
		return sceneService.findSceneRespListByUserId(userId,tenantId);
	}

	@Override
	public List<SceneResp> findSceneRespListBySpaceId(Long spaceId, Long tenantId) {
		return sceneService.findSceneRespListBySpaceId(spaceId,tenantId);
	}

	@Override
	public void delBleScene(@RequestBody SceneReq sceneReq){
		sceneService.delBleScene(sceneReq);
	}

	@Override
	public SceneDetailVo getThen(@RequestBody SceneReq sceneReq){
		return sceneService.getSceneThen(sceneReq);
	}

	@Override
	public void addAction(@RequestBody SceneDetailReqVo actionReq){
		sceneService.addOrEditAction(actionReq);
	}

	@Override
	public void delAction(@RequestBody SceneDetailReqVo actionReq){
		sceneService.delAction(actionReq);
	}

	@Override
	public void editAction(@RequestBody SceneDetailReqVo actionReq){
		sceneService.addOrEditAction(actionReq);
	}




}
