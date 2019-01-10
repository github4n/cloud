package com.iot.control.scene.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.control.activity.exception.BusinessExceptionEnum;
import com.iot.control.scene.domain.*;
import com.iot.control.scene.mapper.SceneMapper;
import com.iot.control.scene.service.SceneDetailService;
import com.iot.control.scene.service.SceneService;
import com.iot.control.scene.util.RedisKeyUtil;
import com.iot.control.scene.vo.req.*;
import com.iot.control.scene.vo.rsp.*;
import com.iot.control.space.enums.SpaceEnum;
import com.iot.control.space.service.ISpaceService;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.api.DeviceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.redis.RedisCacheUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称：IOT云平台 模块名称： 功能描述：情景service实现 创建人： wujianlong 创建时间：2017年11月9日 下午2:57:16
 * 修改人： wujianlong 修改时间：2017年11月9日 下午2:57:16
 */
@Service
public class SceneServiceImpl extends ServiceImpl<SceneMapper,Scene> implements SceneService {

	private static final Logger log = LoggerFactory.getLogger(SceneServiceImpl.class);

	@Autowired
	private SceneMapper sceneMapper;

	@Autowired
	private SceneDetailService sceneDetailService;

	@Autowired
	private DeviceCoreApi deviceCoreApi;

	@Autowired
	private ISpaceService iSpaceService;

	@Override
	public SceneResp sceneById(SceneReq sceneReq) {
		String sceneListIdKey = RedisKeyUtil.getSceneListIdKey(sceneReq.getId(),sceneReq.getTenantId());
		Scene scene = RedisCacheUtil.valueObjGet(sceneListIdKey, Scene.class);
		SceneResp sceneResp = new SceneResp();
		if (scene == null) {
			EntityWrapper wrapper = new EntityWrapper();
			wrapper.eq("id",sceneReq.getId());
			wrapper.eq("tenant_id",sceneReq.getTenantId());
			wrapper.eq("	org_id",sceneReq.getOrgId());
			scene = super.selectOne(wrapper);
			if (scene == null ){
				return sceneResp = null;
			}
			RedisCacheUtil.valueObjSet(sceneListIdKey, scene, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT);
		}
		sceneResp.setId(scene.getId());
		sceneResp.setSceneName(scene.getSceneName());
		sceneResp.setSpaceId(scene.getSpaceId());
		sceneResp.setCreateBy(scene.getCreateBy());
		sceneResp.setCreateTime(scene.getCreateTime());
		sceneResp.setUpdateBy(scene.getUpdateBy());
		sceneResp.setUpdateTime(scene.getUpdateTime());
		sceneResp.setTenantId(scene.getTenantId());
		sceneResp.setOrgId(scene.getOrgId());
		sceneResp.setIcon(scene.getIcon());
		sceneResp.setSetType(scene.getSetType());
		sceneResp.setSort(scene.getSort());
		sceneResp.setUploadStatus(scene.getUploadStatus());
		sceneResp.setLocationId(scene.getLocationId());
		sceneResp.setTenantId(scene.getTenantId());
		sceneResp.setDevSceneId(scene.getDevSceneId());
		sceneResp.setSilenceStatus(scene.getSilenceStatus());
		return sceneResp;
	}

	@Override
	public List<SceneResp> sceneByParam(SceneReq sceneReq) {
		List<SceneResp> result = new ArrayList<>();
		String sceneUserListIdKey = RedisKeyUtil.getSceneUserListIdKey(sceneReq.getCreateBy(),sceneReq.getTenantId());
		List<Map> sceneIdList = RedisCacheUtil.listGetAll(sceneUserListIdKey, Map.class);
		List<String> sceneIdListKey = new ArrayList();
		if (sceneIdList.size()>0){
			sceneIdList.forEach(m -> {
				sceneIdListKey.add(RedisKeyUtil.getSceneListIdKey(new Long(m.get("sceneId").toString()),sceneReq.getTenantId()));
			});
			result = RedisCacheUtil.mget(sceneIdListKey, SceneResp.class);
		}

		else {
			result  = this.sceneByParamDoing(sceneReq);
			log.debug("sceneByParam.result {} " , JSON.toJSONString(result));
			List listCach = new ArrayList();
			result.forEach(m->{
				Map map = new HashMap();
				map.put("sceneId", m.getId());
				listCach.add(map);
				// 加入缓存
				String sceneListIdKey = RedisKeyUtil.getSceneListIdKey(m.getId(),m.getTenantId());
				RedisCacheUtil.valueObjSet(sceneListIdKey, m, new Long(300));
			});
			// 加入缓存
			RedisCacheUtil.listSet(sceneUserListIdKey, listCach);
			RedisCacheUtil.expireKey(sceneUserListIdKey, new Long(300));
		}
		return result;
	}


	public List<SceneResp> sceneByParamDoing(SceneReq sceneReq) {
		EntityWrapper wrapper = new EntityWrapper();
		if (sceneReq.getId()!=null){
			wrapper.eq("id",sceneReq.getId());
		}
		if (StringUtils.isNotEmpty(sceneReq.getIds())){
			wrapper.in("id",sceneReq.getIds());
		}
		if (CollectionUtils.isNotEmpty(sceneReq.getSceneIds())){
			wrapper.in("id",sceneReq.getSceneIds());
		}
		if (StringUtils.isNotEmpty(sceneReq.getSceneName())){
			wrapper.eq("scene_name",sceneReq.getSceneName());
		}
		if (sceneReq.getSpaceId()!=null){
			wrapper.eq("space_id",sceneReq.getSpaceId());
		}
		if (sceneReq.getTenantId()!=null){
			wrapper.eq("tenant_id",sceneReq.getTenantId());
		}
		if (sceneReq.getOrgId()!=null){
			wrapper.eq("org_id",sceneReq.getOrgId());
		}
		if (StringUtils.isNotEmpty(sceneReq.getIcon())){
			wrapper.eq("icon",sceneReq.getIcon());
		}
		if (sceneReq.getSetType()!=null){
			wrapper.eq("set_type",sceneReq.getSetType());
		}
		if (sceneReq.getSort()!=null){
			wrapper.eq("sort",sceneReq.getSort());
		}
		if (sceneReq.getUploadStatus()!=null){
			wrapper.eq("upload_status",sceneReq.getUploadStatus());
		}
		if (sceneReq.getLocationId()!=null){
			wrapper.eq("location_id",sceneReq.getLocationId());
		}
		if (sceneReq.getTemplateId()!=null){
			wrapper.eq("template_id",sceneReq.getTemplateId());
		}
		if (sceneReq.getCreateBy()!=null){
			wrapper.eq("create_by",sceneReq.getCreateBy());
		}
		if (sceneReq.getUpdateBy()!=null){
			wrapper.eq("update_by",sceneReq.getUpdateBy());
		}
		if(sceneReq.getDevSceneId() != null){
			wrapper.eq("dev_scene_id", sceneReq.getDevSceneId());
		}
		if (sceneReq.getSilenceStatus() != null) {
			wrapper.eq("silence_status",sceneReq.getSilenceStatus());
		}
		wrapper.orderBy("id", false);
		List<Scene> result = super.selectList(wrapper);
		List<SceneResp> list = new ArrayList<>();
		result.forEach(m->{
			SceneResp sceneResp = new SceneResp();
			sceneResp.setId(m.getId());
			sceneResp.setSceneName(m.getSceneName());
			sceneResp.setSpaceId(m.getSpaceId());
			sceneResp.setCreateBy(m.getCreateBy());
			sceneResp.setUpdateBy(m.getUpdateBy());
			sceneResp.setCreateTime(m.getCreateTime());
			sceneResp.setUpdateTime(m.getUpdateTime());
			sceneResp.setTenantId(m.getTenantId());
			sceneResp.setOrgId(m.getOrgId());
			sceneResp.setIcon(m.getIcon());
			sceneResp.setSetType(m.getSetType());
			sceneResp.setSort(m.getSort());
			sceneResp.setUploadStatus(m.getUploadStatus());
			sceneResp.setLocationId(m.getLocationId());
			sceneResp.setTemplateId(m.getTemplateId());
			sceneResp.setDevSceneId(m.getDevSceneId());
			sceneResp.setSilenceStatus(m.getSilenceStatus());

			SceneDetailReq sceneDetailReq = new SceneDetailReq();
			sceneDetailReq.setSceneId(m.getId());
			List<SceneDetailResp> sceneDetailResp = sceneDetailService.sceneDetailByParam(sceneDetailReq);
			List deviceIdList = new ArrayList();
			sceneDetailResp.forEach(n->{
				deviceIdList.add(n.getDeviceId());
			});
			ListDeviceInfoReq listDeviceInfoReq = new ListDeviceInfoReq();
			listDeviceInfoReq.setDeviceIds(deviceIdList);
			List<ListDeviceInfoRespVo> deviceList = deviceCoreApi.listDevices(listDeviceInfoReq);
			List<String> deviceParentIdList = new ArrayList();
			deviceList.forEach(d->{
				if (StringUtils.isNotEmpty(d.getParentId())){
					deviceParentIdList.add(d.getParentId().toString());
				}
			});

			if (deviceList.size()==deviceParentIdList.size()){
				List<String> parentList = deviceParentIdList.stream().distinct().collect(Collectors.toList());
				if (parentList.size()==1){
					sceneResp.setIsDirect(1);
					sceneResp.setDirectId(parentList.get(0));
				} else {
					sceneResp.setIsDirect(0);
				}

			} else {
				sceneResp.setIsDirect(0);
			}

			list.add(sceneResp);
			// 移除缓存
			String sceneListIdKey = RedisKeyUtil.getSceneListIdKey(sceneResp.getId(),sceneResp.getTenantId());
			// 移除缓存
			RedisCacheUtil.delete(sceneListIdKey);
		});
		return list;
	}


	@Override
	public int updateScene(SceneReq sceneReq) {
		Scene scene = new Scene();
		scene.setId(sceneReq.getId());
		scene.setSceneName(sceneReq.getSceneName());
		scene.setSpaceId(sceneReq.getSpaceId());
		scene.setUpdateBy(sceneReq.getUpdateBy());
		scene.setUpdateTime(new Date());
		scene.setTenantId(sceneReq.getTenantId());
		scene.setOrgId(sceneReq.getOrgId());
		scene.setIcon(sceneReq.getIcon());
		scene.setSetType(sceneReq.getSetType());
		scene.setSort(sceneReq.getSort());
		scene.setUploadStatus(sceneReq.getUploadStatus());
		scene.setLocationId(sceneReq.getLocationId());
		scene.setTenantId(sceneReq.getTenantId());
		scene.setDevSceneId(sceneReq.getDevSceneId());
		scene.setSilenceStatus(sceneReq.getSilenceStatus());
		// 移除缓存
		String sceneUserListIdKey = RedisKeyUtil.getSceneUserListIdKey(scene.getUpdateBy(),scene.getTenantId());
		List<Map> sceneList = RedisCacheUtil.listGetAll(sceneUserListIdKey, Map.class);
		List<String> sceneListIdKey = new ArrayList();
		sceneList.forEach(n -> {
			sceneListIdKey.add(RedisKeyUtil.getSceneListIdKey(new Long(n.get("sceneId").toString()),scene.getTenantId()));
		});
		// 移除缓存
		RedisCacheUtil.delete(sceneUserListIdKey);
		RedisCacheUtil.delete(sceneListIdKey);
		// 移除缓存
		String sceneListObjectIdKey = RedisKeyUtil.getSceneListIdKey(scene.getId(),scene.getTenantId());
		// 移除缓存
		RedisCacheUtil.delete(sceneListObjectIdKey);
		Boolean isOk = super.updateById(scene);
		return isOk?1:0;
	}


	@Override
	public SceneResp insertScene(SceneReq sceneReq) {
		Scene scene = new Scene();
		scene.setSceneName(sceneReq.getSceneName());
		scene.setSpaceId(sceneReq.getSpaceId());
		scene.setCreateBy(sceneReq.getCreateBy());
		scene.setUpdateBy(sceneReq.getUpdateBy());
		scene.setCreateTime(new Date());
		scene.setUpdateTime(new Date());
		scene.setTenantId(sceneReq.getTenantId());
		scene.setOrgId(sceneReq.getOrgId());
		scene.setIcon(sceneReq.getIcon());
		scene.setSetType(sceneReq.getSetType());
		scene.setSort(sceneReq.getSort());
		scene.setUploadStatus(sceneReq.getUploadStatus());
		scene.setLocationId(sceneReq.getLocationId());
		scene.setTemplateId(sceneReq.getTemplateId());
		scene.setDevSceneId(sceneReq.getDevSceneId());
		scene.setSilenceStatus(sceneReq.getSilenceStatus());
		super.insert(scene);
		SceneResp sceneResp = new SceneResp();
		sceneResp.setId(scene.getId());
		// 移除缓存
		String sceneUserListIdKey = RedisKeyUtil.getSceneUserListIdKey(scene.getCreateBy(),scene.getTenantId());
		List<Map> sceneList = RedisCacheUtil.listGetAll(sceneUserListIdKey, Map.class);
		List<String> sceneListIdKey = new ArrayList();
		sceneList.forEach(n -> {
			sceneListIdKey.add(RedisKeyUtil.getSceneListIdKey(new Long(n.get("sceneId").toString()),scene.getTenantId()));
		});
		// 移除缓存
		RedisCacheUtil.delete(sceneUserListIdKey);
		RedisCacheUtil.delete(sceneListIdKey);
		// 移除缓存
		String sceneListObjectIdKey = RedisKeyUtil.getSceneListIdKey(scene.getId(),scene.getTenantId());
		// 移除缓存
		RedisCacheUtil.delete(sceneListObjectIdKey);
		return sceneResp;
	}

	@Override
	public int deleteScene(SceneReq sceneReq) {
		Scene scene = new Scene();
		scene.setId(sceneReq.getId());
		scene.setCreateBy(sceneReq.getCreateBy());
		scene.setTenantId(sceneReq.getTenantId());
		// 移除缓存
		String sceneUserListIdKey = RedisKeyUtil.getSceneUserListIdKey(scene.getCreateBy(),scene.getTenantId());
		List<Map> sceneList = RedisCacheUtil.listGetAll(sceneUserListIdKey, Map.class);
		List<String> sceneListIdKey = new ArrayList();
		sceneList.forEach(n -> {
			sceneListIdKey.add(RedisKeyUtil.getSceneListIdKey(new Long(n.get("sceneId").toString()),scene.getTenantId()));
		});
		// 移除缓存
		RedisCacheUtil.delete(sceneUserListIdKey);
		RedisCacheUtil.delete(sceneListIdKey);
		Boolean isOk = super.deleteById(sceneReq);
		return isOk?1:0;
	}

	@Override
	public SceneResp maxSortSceneBySpaceId(Long spaceId) {
		return sceneMapper.maxSortSceneBySpaceId(spaceId);
	}

	@Override
	public int countBySceneName(String sceneName, Long userId, Long sceneId) {
		return sceneMapper.countBySceneName(sceneName,userId,sceneId);
	}

	@Override
	public List<SceneResp> getSceneByIds(List<Long> ids,Long tenantId, Long orgId) {
		if(CollectionUtils.isEmpty(ids)){
			return null;
		}
		if(tenantId==null){
			return null;
		}
		List<SceneResp> sceneResps=new ArrayList<>();
		for(Long id:ids) {
			SceneReq sceneReq = new SceneReq();
			sceneReq.setId(id);
			sceneReq.setTenantId(tenantId);
			sceneReq.setOrgId(orgId);
			SceneResp sceneResp = sceneById(sceneReq);
			sceneResps.add(sceneResp);
		}
		return sceneResps;
	}

	@Override
	public String moveSceneSpaceId(String passWord) {
		if(!"88888888".equals(passWord)){
			return "passWord is wrong!!!";
		}

		EntityWrapper en=new EntityWrapper();
		//获取所有的scene
		List<Scene> scenes=super.selectList(en);
		List<Scene> updateScenes=new ArrayList<>();
		for(Scene s:scenes){

			Long userId=s.getCreateBy();
			Long tenantId=s.getTenantId();
			if(userId==null ||tenantId==null){
				continue;
			}
			//查找主家id
			SpaceReq spaceReq=new SpaceReq();
			spaceReq.setTenantId(tenantId);
			spaceReq.setUserId(userId);
			spaceReq.setType(SpaceEnum.HOME.getCode());

			SpaceResp spaceResp=new SpaceResp();
			List<SpaceResp> spaceList=iSpaceService.findSpaceByCondition(spaceReq);
			if (CollectionUtils.isNotEmpty(spaceList)) {
				spaceResp = spaceList.get(0);
			}else {
				continue;
			}
			//已经存在spaceId的不修改
			if(s.getSpaceId()!=null){
				continue;
			}
			Long homeId=spaceResp.getId();
			s.setSpaceId(homeId);
			s.setUpdateTime(new Date());
			updateScenes.add(s);
		}

		if(CollectionUtils.isEmpty(updateScenes)){
			return "scene spaceId has all been moved!";
		}
		super.updateBatchById(updateScenes);
		return "move scene spaceId ok!";
	}


}
