package com.iot.center.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.scene.api.SceneControlApi;
import com.iot.building.scene.vo.req.LocationSceneDetailReq;
import com.iot.building.scene.vo.req.LocationSceneReq;
import com.iot.building.scene.vo.resp.LocationSceneDetailResp;
import com.iot.building.scene.vo.resp.LocationSceneRelationResp;
import com.iot.building.scene.vo.resp.LocationSceneResp;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneAddReq;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.req.ProductReq;
import com.iot.device.vo.req.device.ListProductInfoReq;
import com.iot.device.vo.rsp.device.ListProductRespVo;
import com.iot.user.vo.LoginResp;

/**
 * 
 * 项目名称：立达信IOT云平台 模块名称： 功能描述：情景service实现 创建人： wujianlong 创建时间：2017年11月9日
 * 下午2:57:16 修改人： wujianlong 修改时间：2017年11月9日 下午2:57:16
 */
@Service
public class SceneService {

	private static final Logger log = LoggerFactory.getLogger(SceneService.class);
	
	@Autowired
	private SceneControlApi sceneControlApi;
	@Autowired
	private SceneApi sceneApi;
	@Autowired
	private com.iot.control.space.api.SpaceApi commonSpaecApi;
	@Autowired
	private ProductCoreApi productApi;
	
	public void saveSceneDetail(String deviceTarValues, Long userId, Integer setType, Long locationId) {
		SceneAddReq sceneAddReq = new SceneAddReq();
		sceneAddReq.setDeviceTarValues(deviceTarValues);
		sceneAddReq.setUserId(userId);
		sceneAddReq.setSetType(setType);
		sceneAddReq.setLocationId(locationId);
		sceneControlApi.saveSceneAndSceneDetail(sceneAddReq);
	}
	
	public void sceneExecute(Long tenantId, Long sceneId) {
		sceneControlApi.sceneExecute(tenantId, sceneId);
	}
	
	public List<SceneDetailResp> findSceneDetailListBySpaceId(SceneDetailReq sceneDetailReq){
		return sceneControlApi.findSceneDetailInfo(sceneDetailReq);
	}
	
	public List<SceneResp> findSceneDetailList(Long orgId,Long tenantId, Long spaceId){
		return sceneControlApi.findSceneDetailList(orgId,tenantId, spaceId);
	}
	
	public List<SceneDetailResp> findSceneDetailInfo(SceneDetailReq sceneDetailReq){
		return sceneControlApi.findSceneDetailInfo(sceneDetailReq);
	}

	public void updateSceneDetailInfo(Long sceneId, String deviceTarValues, Long userId, Integer setType) {
		SceneAddReq sceneAddReq = new SceneAddReq();
		sceneAddReq.setDeviceTarValues(deviceTarValues);
		sceneAddReq.setUserId(userId);
		sceneAddReq.setSetType(setType);
		sceneAddReq.setSceneId(sceneId);
		sceneControlApi.updateSceneDetailInfo(sceneAddReq);
	}
	
	public void deleteSceneDetail(@RequestParam("tenantId")Long tenantId, @RequestParam("sceneId")Long sceneId, @RequestParam("spaceId")Long spaceId, @RequestParam("userId")Long userId) {
		sceneControlApi.deleteSceneDetail(tenantId,sceneId,spaceId,userId);
	}
	
	public List<ListProductRespVo> findProductListByTenantId(@RequestBody ProductReq product){
		ListProductInfoReq params=new ListProductInfoReq();
		return productApi.listProducts(params);
	}

	/**
	 *  保存/修改整校的locationScene
	 * @param locationSceneReq
	 */
	public Long saveLocationScene(@RequestBody LocationSceneReq locationSceneReq) {
		return sceneControlApi.saveLocationScene(locationSceneReq);
	}

	/**
	 * 查询整校locationScene的列表，通过tenantId查询
	 * @param locationSceneReq
	 * @return
	 */
    public List<LocationSceneResp> findLocationSceneList(@RequestBody LocationSceneReq locationSceneReq) {
    	List<LocationSceneResp> list = sceneControlApi.findLocationSceneList(locationSceneReq);
		list = getCommonList(list,locationSceneReq);
		return list;
    }

	private List<LocationSceneResp> getCommonList(List<LocationSceneResp> list,LocationSceneReq locationSceneReq) {

		List<Long> spaceIds=Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(list)) {
			list.forEach(resp->{
				if(resp.getBuildId() !=null) {
					spaceIds.add(resp.getBuildId());
				}
				if(resp.getFloorId() !=null) {
					spaceIds.add(resp.getFloorId());
				}
			});
		}
		//查询空间名称
		Map<Long,String> spaecNameMap=Maps.newHashMap();
		if(CollectionUtils.isNotEmpty(spaceIds)) {
			SpaceAndSpaceDeviceVo req =new SpaceAndSpaceDeviceVo();
			req.setTenantId(locationSceneReq.getTenantId());
			req.setSpaceIds(spaceIds);
			List<SpaceResp> respList=commonSpaecApi.findSpaceInfoBySpaceIds(req);
			if(CollectionUtils.isNotEmpty(respList)) {
				respList.forEach(resp->{
					spaecNameMap.put(resp.getId(), resp.getName());
				});
			}
		}
		if(CollectionUtils.isNotEmpty(list)) {
			list.forEach(resp->{
				if(resp.getBuildId() !=null) {
					resp.setBuildName(spaecNameMap.get(resp.getBuildId()));;
				}
				if(resp.getFloorId() !=null) {
					resp.setFloorName(spaecNameMap.get(resp.getFloorId()));
				}
			});
		}
		return list;
	}

	/**
	 * 保存/修改整校的locationSceneDetail
	 * @param locationSceneDetailReq
	 */
	public void saveOrUpdateLocationSceneDetail(@RequestBody LocationSceneDetailReq locationSceneDetailReq) {
		sceneControlApi.saveOrUpdateLocationSceneDetail(locationSceneDetailReq);
    }

	/**
	 * 查询整校locationSceneDetail的列表，通过tenantId和locationSceneId查询
	 * @param locationSceneDetailReq
	 * @return
	 */
	public List<LocationSceneDetailResp> findLocationSceneDetailList(@RequestBody LocationSceneDetailReq locationSceneDetailReq) {
		List<LocationSceneDetailResp> list = sceneControlApi.findLocationSceneDetailList(locationSceneDetailReq);
		SceneReq sceneReq=new SceneReq();
		sceneReq.setTenantId(locationSceneDetailReq.getTenantId());
		String ids="";
		if(CollectionUtils.isNotEmpty(list)) {
			for(LocationSceneDetailResp resp:list) {
				ids=ids+","+resp.getSceneId();
			}
	    }
		//批量查询scene
		Map<Long,String> sceneName=Maps.newHashMap();
		if(StringUtils.isNotBlank(ids)) {
			sceneReq.setIds(ids);
			List<SceneResp> respList=sceneApi.sceneByParamDoing(sceneReq);
			if(CollectionUtils.isNotEmpty(respList)) {
				respList.forEach(resp->{
					sceneName.put(resp.getId(), resp.getSceneName());
				});
			}
		}
		//组装名称
		if(CollectionUtils.isNotEmpty(list)) {
			for(LocationSceneDetailResp resp:list) {
				resp.setSceneName(sceneName.get(resp.getSceneId()));
			}
	    }
		return list;
	}

	/**
	 * 删除location_scene表中的数据,通过id
	 * @param id
	 */
	public void deleteLocationScene(@RequestParam("tenantId")Long tenantId, @RequestParam("id")Long id) {
		sceneControlApi.deleteLocationScene(tenantId, id);
	}

	/**
	 * 删除location_scene_detail表中的数据,通过tenantId和locationSceneId
	 * @param tenantId
	 * @param locationSceneId
	 */
	public void deleteLocationSceneDetail(@RequestParam("tenantId")Long tenantId, @RequestParam("locationSceneId")Long locationSceneId) {
		sceneControlApi.deleteLocationSceneDetail(tenantId,locationSceneId);
	}

	public void deleteLocationSceneDetailStr(@RequestParam("tenantId")Long tenantId, @RequestParam("locationSceneId")Long locationSceneId) {
		sceneControlApi.deleteLocationSceneDetailStr(tenantId, locationSceneId);
	}

	public List<Map<String,Object>> findTree(Long orgId,Long tenantId, Long locationId){
		return sceneControlApi.findTree(orgId,tenantId, locationId);
	}

	public Page<LocationSceneResp> findLocationSceneListStr(@RequestBody LocationSceneReq locationSceneReq) {
		Page<LocationSceneResp> page=sceneControlApi.findLocationSceneListStr(locationSceneReq);
		List<LocationSceneResp> list=getCommonList(page.getResult(),locationSceneReq);
		page.setResult(list);
		return page;
	}

	public void updateLocationScene(@RequestBody LocationSceneReq locationSceneReq) {
		sceneControlApi.updateLocationScene(locationSceneReq);
	}

	/**
	 * 通过locationSceneId 查询 sceneId的集合
	 * @param locationSceneId
	 * @return
	 */
	public List<Long> findSceneIds(@RequestParam("tenantId")Long tenantId, @RequestParam("locationSceneId") Long locationSceneId) {
		List<Long> list = sceneControlApi.findSceneIds(tenantId, locationSceneId);
		return list;
	}

	/**
	 * 获取location_scene_list 的列表
	 * @return
	 */
    public List<LocationSceneRelationResp> findLocationSceneRelationList(Long tenantId,Long orgId) {
		List<LocationSceneRelationResp> list = sceneControlApi.findLocationSceneRelationList(orgId,tenantId);
		return list;
    }

    public CommonResponse<List<SceneResp>> querySceneList(SceneDetailReq req) {
        List<SceneResp> list = sceneControlApi.querySceneList(req);
        return CommonResponse.success(list);
    }

    public void delLocalSceneById(Long id, Long tenantId) {
        deleteLocationScene(tenantId, id);
        //删除location_scene_detail表中的数据,通过tenantId和locationSceneId
        deleteLocationSceneDetail(tenantId,id);
    }
    
    public CommonResponse<ResultMsg> issueScene(SceneReq req) {
    	sceneControlApi.issueScene(req);
        return CommonResponse.success(ResultMsg.SUCCESS);
    }

	public List<LocationSceneResp> findLocationSceneListByName(@RequestBody LocationSceneReq req) {
		List<LocationSceneResp> list = sceneControlApi.findLocationSceneListByName(req);
		return list;
	}
}