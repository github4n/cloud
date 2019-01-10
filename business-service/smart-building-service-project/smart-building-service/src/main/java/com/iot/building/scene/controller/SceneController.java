package com.iot.building.scene.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.scene.api.SceneControlApi;
import com.iot.building.scene.service.SceneService;
import com.iot.building.scene.vo.req.LocationSceneDetailReq;
import com.iot.building.scene.vo.req.LocationSceneRelationReq;
import com.iot.building.scene.vo.req.LocationSceneReq;
import com.iot.building.scene.vo.req.SceneTemplateManualReq;
import com.iot.building.scene.vo.resp.LocationSceneDetailResp;
import com.iot.building.scene.vo.resp.LocationSceneRelationResp;
import com.iot.building.scene.vo.resp.LocationSceneResp;
import com.iot.common.helper.Page;
import com.iot.control.scene.vo.req.SceneAddReq;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.control.scene.vo.rsp.SceneResp;

/**
 * 项目名称: IOT云平台 
 * 模块名称：control服务模块 
 * 功能描述：情景api 
 * 创建人: linjihuang 
 * 创建时间: 2018/4/16 15:36 
 * 修改人: 
 * 修改时间：
 */

@RestController
public class SceneController implements SceneControlApi {

	@Autowired
	private SceneService sceneService;
	private final static String SERVICE = "scene";
	private Logger log = LoggerFactory.getLogger(SceneController.class);

	@Override
	public void saveSceneAndSceneDetail(@RequestBody SceneAddReq sceneAddReq) {
		sceneService.saveSceneAndSceneDetail(sceneAddReq.getDeviceTarValues(), sceneAddReq.getUserId(),
				sceneAddReq.getSetType(), sceneAddReq.getLocationId());
	}

	@Override
	public List<SceneDetailResp> findSceneDetailInfo(@RequestBody SceneDetailReq sceneDetailReq) {
		return sceneService.findSceneDetailInfo(sceneDetailReq);
	}

	public void updateSceneDetailInfo(@RequestBody SceneAddReq sceneAddReq) {
		sceneService.updateSceneDetailInfo(sceneAddReq.getSceneId(), sceneAddReq.getDeviceTarValues(), sceneAddReq.getUserId(), sceneAddReq.getSetType());
	}

	@Override
	public void deleteSceneDetail(Long tenantId,@RequestParam("sceneId") Long sceneId, @RequestParam("spaceId") Long spaceId,
			@RequestParam("userId") Long userId) {
		sceneService.deleteSceneDetail(tenantId, sceneId, spaceId, userId);
	}

	@Override
	public void sceneExecute(Long tenantId, @RequestParam("sceneId") Long sceneId) {
		sceneService.sceneExecute(tenantId, sceneId);
	}

	@Override
	public List<SceneResp> findSceneDetailList(@RequestParam("orgId") Long orgId, Long tenantId,@RequestParam("spaceId") Long spaceId) {
		SceneReq sceneReq = new SceneReq();
		sceneReq.setTenantId(tenantId);
		sceneReq.setOrgId(orgId);
		sceneReq.setSpaceId(spaceId);
		return sceneService.findSceneDetailList(sceneReq);
	}

	@Override
	public void saveOrUpdateLocationScene(@RequestBody LocationSceneReq locationSceneReq) {
		sceneService.saveOrUpdateLocationScene(locationSceneReq);
	}

	@Override
	public Long saveLocationScene(@RequestBody LocationSceneReq locationSceneReq) {
		return sceneService.saveLocationScene(locationSceneReq);
	}

	@Override
	public List<LocationSceneResp> findLocationSceneList(@RequestBody LocationSceneReq locationSceneReq) {
		List<LocationSceneResp> list = sceneService.findLocationSceneList(locationSceneReq);
		return list;
	}

	@Override
	public Page<LocationSceneResp> findLocationSceneListStr(@RequestBody LocationSceneReq locationSceneReq) {
		return sceneService.findLocationSceneListStr(locationSceneReq);
	}

	@Override
	public void saveOrUpdateLocationSceneDetail(@RequestBody LocationSceneDetailReq locationSceneDetailReq) {
		sceneService.saveOrUpdateLocationSceneDetail(locationSceneDetailReq);
	}

	@Override
	public List<LocationSceneDetailResp> findLocationSceneDetailList(
			@RequestBody LocationSceneDetailReq locationSceneDetailReq) {
		List<LocationSceneDetailResp> list = sceneService.findLocationSceneDetailList(locationSceneDetailReq);
		return list;
	}

	@Override
	public void deleteLocationScene(Long tenantId,@RequestParam("id") Long id) {
		sceneService.deleteLocationScene(id);
	}

	@Override
	public void deleteLocationSceneDetail(@RequestParam("tenantId") Long tenantId,
			@RequestParam("locationSceneId") Long locationSceneId) {
		sceneService.deleteLocationSceneDetail(tenantId, locationSceneId);
	}

	@Override
	public void deleteLocationSceneDetailStr(Long tenantId,@RequestParam("locationSceneId") Long locationSceneId) {
		sceneService.deleteLocationSceneDetailStr(locationSceneId);
	}

	@Override
	public void updateLocationScene(@RequestBody LocationSceneReq locationSceneReq) {
		sceneService.updateLocationScene(locationSceneReq);

	}

	@Override
	public List<Long> findSceneIds(Long tenantId,@RequestParam("locationSceneId") Long locationSceneId) {
		List<Long> list = sceneService.findSceneIds(locationSceneId);
		return list;
	}

	@Override
	public List<Map<String, Object>> findTree(@RequestParam("orgId")Long orgId, @RequestParam("tenantId")Long tenantId,@RequestParam("locationId") Long locationId) {
		return sceneService.findTree(orgId, tenantId, locationId);
	}

	@Override
	public void saveLocationSceneRelation(@RequestBody LocationSceneRelationReq locationSceneRelationReq) {
		sceneService.saveLocationSceneRelation(locationSceneRelationReq);
	}

	@Override
	public List<LocationSceneRelationResp> findLocationSceneRelationList(Long orgId, Long tenantId) {
		List<LocationSceneRelationResp> list = sceneService.findLocationSceneRelationList();
		return list;
	}

	@Override
	public List<SceneResp> querySceneList(@RequestBody SceneDetailReq req) {
		return sceneService.querySceneList(req);
	}

	@Override
	public Long getSpaceSceneStatus(Long tenantId, Long spaceId) {
		return sceneService.getSpaceSceneStatus(tenantId, spaceId);
	}

	@Override
	public Long setSpaceSceneStatus(Long tenantId,Long sceneId) {
		return sceneService.setSpaceSceneStatus(tenantId,sceneId);
	}

	@Override
	public void sceneTemplateManualInit(@RequestBody SceneTemplateManualReq req) {
		sceneService.sceneTemplateManualInit(req);
	}

	@Override
	public void templateManualInit(@RequestBody SceneTemplateManualReq req) {
		sceneService.templateManualInit(req);
	}

	@Override
	public void issueScene(@RequestBody SceneReq req) {
		sceneService.issueScene(req);
	}

	@Override
	public List<LocationSceneResp> findLocationSceneListByName(@RequestBody LocationSceneReq req) {
		return sceneService.findLocationSceneListByName(req);
	}
}
