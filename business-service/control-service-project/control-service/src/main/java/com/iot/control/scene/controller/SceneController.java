package com.iot.control.scene.controller;

import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.service.SceneDetailService;
import com.iot.control.scene.service.SceneService;
import com.iot.control.scene.vo.req.*;
import com.iot.control.scene.vo.rsp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 项目名称: IOT云平台 模块名称：control服务模块 功能描述：情景api 创建人: yuChangXing 创建时间: 2018/4/16
 * 15:36 修改人: 修改时间：
 */

@RestController
public class SceneController implements SceneApi {

	@Autowired
	private SceneService sceneService;

	@Autowired
	private SceneDetailService sceneDetailService;

	private final static String SERVICE = "scene";
	private Logger log = LoggerFactory.getLogger(SceneController.class);

	@Override
	public List<SceneDetailResp> sceneDetailByParam(@RequestBody @Valid SceneDetailReq sceneDetailReq) {
		return sceneDetailService.sceneDetailByParam(sceneDetailReq);
	}

	@Override
	public List<SceneDetailResp> sceneDetailByParamDoing(@RequestBody @Valid SceneDetailReq sceneDetailReq) {
		return sceneDetailService.sceneDetailByParamDoing(sceneDetailReq);
	}


	@Override
	public int updateSceneDetail(@RequestBody  @Valid SceneDetailReq sceneDetailReq) {
		return sceneDetailService.updateSceneDetail(sceneDetailReq);
	}

	@Override
	public void insertSceneDetail(@RequestBody SceneDetailReq sceneDetailReq) {
		sceneDetailService.insert(sceneDetailReq);
	}

	@Override
	public int deleteSceneDetail(@RequestBody @Valid SceneDetailReq sceneDetailReq) {
		return sceneDetailService.deleteSceneDetail(sceneDetailReq);
	}

	@Override
	public int deleteSceneDetailByDeviceId(@RequestBody SceneDetailReq sceneDetailReq) {
		return sceneDetailService.deleteSceneDetailByDeviceId(sceneDetailReq);
	}

	@Override
	public List<SceneDetailResp> sceneDetailList(@RequestBody SceneDetailReq sceneDetailReq) {
		return sceneDetailService.sceneDetailList(sceneDetailReq);
	}

	@Override
	public int countChildBySceneId(@RequestParam("sceneId") Long sceneId) {
		return sceneDetailService.countChildBySceneId(sceneId);
	}

	@Override
	public Map<Long, Integer> countChildBySceneIds(@RequestBody List<Long> sceneIdList) {
		return sceneDetailService.countChildBySceneIds(sceneIdList);
	}

	@Override
	public List<SceneResp> sceneByParam(@RequestBody SceneReq sceneReq) {
		return sceneService.sceneByParam(sceneReq);
	}

	@Override
	public List<SceneResp> sceneByParamDoing(@RequestBody SceneReq sceneReq) {
		return sceneService.sceneByParamDoing(sceneReq);
	}

	@Override
	public int updateScene(@RequestBody @Valid SceneReq sceneReq) {
		return sceneService.updateScene(sceneReq);
	}

	@Override
	public SceneResp insertScene(@RequestBody @Valid SceneReq sceneReq) {
		return sceneService.insertScene(sceneReq);
	}

	@Override
	public int deleteScene(@RequestBody @Valid SceneReq sceneReq) {
		return sceneService.deleteScene(sceneReq);
	}

	@Override
	public SceneResp maxSortSceneBySpaceId(@RequestParam("spaceId") Long spaceId) {
		return sceneService.maxSortSceneBySpaceId(spaceId);
	}

	@Override
	public SceneResp sceneById(@RequestBody SceneReq sceneReq) {
		return sceneService.sceneById(sceneReq);
	}

	@Override
	public int countBySceneName(@RequestParam("sceneName") String sceneName, @RequestParam("userId") Long userId, @RequestParam("sceneId") Long sceneId) {
		return sceneService.countBySceneName(sceneName,userId,sceneId);
	}

	@Override
	public List<SceneResp> getSceneByIds(@RequestBody GetSceneReq getSceneReq) {
		List<Long> ids=getSceneReq.getSceneIds();
		Long tenantId=getSceneReq.getTenantId();
		Long orgId = getSceneReq.getOrgId();
		return sceneService.getSceneByIds(ids,tenantId,orgId);
	}

	@Override
	public String moveSceneSpaceId(String passWord) {
		return sceneService.moveSceneSpaceId(passWord);
	}


}
