package com.iot.building.common.service.impl;

import java.util.List;

import com.iot.building.ifttt.entity.Rule;
import com.iot.building.ifttt.mapper.RuleTobMapper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.iot.building.common.service.ShortcutService;
import com.iot.building.common.util.ToolUtil;
import com.iot.building.excepiton.BusinessExceptionEnum;
import com.iot.building.helper.Constants;
import com.iot.building.ifttt.api.AutoTobApi;
import com.iot.building.ifttt.service.IAutoTobService;
import com.iot.building.ifttt.service.IftttService;
import com.iot.building.ifttt.service.RuleService;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.scene.service.SceneService;
import com.iot.building.scene.vo.req.LocationSceneReq;
import com.iot.building.scene.vo.resp.LocationSceneResp;
import com.iot.building.shortcut.vo.ShortcutVo;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.building.template.service.TemplateService;
import com.iot.building.template.vo.req.TemplateReq;
import com.iot.building.template.vo.rsp.TemplateResp;
import com.iot.common.exception.BusinessException;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.control.space.vo.SpaceResp;

@Service
@Transactional
public class ShortcutServiceImpl implements ShortcutService {
	
	@Autowired
	private SceneService sceneService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private SceneApi sceneApi;
	@Autowired
	private RuleService ruleService;
	@Autowired
	private IftttService iftttService;
	@Autowired
	private AutoTobApi autoTobApi;
	@Autowired
	private IAutoTobService autoTobService;
	@Autowired
	private com.iot.control.space.api.SpaceApi spaceApi;
	@Autowired
	private IBuildingSpaceService buildingSpaceService;

	@Autowired
	private RuleTobMapper ruleTobMapper;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ShortcutServiceImpl.class);

	@Override
	public List<ShortcutVo> getShortcutList(ShortcutVo vo) {
		List<ShortcutVo> voList= Lists.newArrayList();
		SpaceResp resp=spaceApi.findSpaceInfoBySpaceId(vo.getTenantId(), vo.getSpaceId());
		//如果空间类型为房间 查询具体的scene and ifttt
		if(resp !=null && resp.getType().equals(Constants.SPACE_ROOM)){
			//房间的情景
			getRoomScene(vo, voList);
			//房间的ifttt
			getRoomIfttt(vo, voList);
		}else {//如果是其他类型  查询模板和组合情景
			//1.组合情景
			getLocalSceneShortcutList(vo, voList);
			//2.模板组合javascript:void(0)
			getSceneTemplateShortcutList(vo, voList);
			//3.ifttt模板组合
			getIftttTemplateShortcutList(vo, voList);
		}
		return voList;
	}

	private void getRoomIfttt(ShortcutVo vo, List<ShortcutVo> voList) {
		RuleListReq req = new RuleListReq();
		req.setLocationId(vo.getLocationId());
		req.setTenantId(vo.getTenantId());
		req.setSpaceId(vo.getSpaceId());
		req.setPageNum(1);
		req.setPageSize(100);
		req.setTemplateFlag(Byte.valueOf("0"));
		req.setShowTime(false);
		List<RuleResp> iftttList=autoTobService.list(req).getResult();
		if(CollectionUtils.isNotEmpty(iftttList)) {
			iftttList.forEach(ifttt->{
				ShortcutVo vo_=getShortcutVo(ifttt.getId(),ifttt.getName(),Constants.SHORTCHT_ROOM_IFTTT,
						ifttt.getTenantId(),ifttt.getLocationId());
				voList.add(vo_);
			});
		}
	}
	
	private ShortcutVo getShortcutVo(Long id,String name,String type,Long tenantId,Long locationId) {
		ShortcutVo vo_=new ShortcutVo();
		vo_.setId(id);
		vo_.setName(name);
		vo_.setType(type);
		vo_.setTenantId(tenantId);
		vo_.setLocationId(locationId);
		return vo_;
	}

	private void getRoomScene(ShortcutVo vo, List<ShortcutVo> voList) {
		SceneReq sceneReq =new SceneReq();
		sceneReq.setTenantId(vo.getTenantId());
		sceneReq.setSpaceId(vo.getSpaceId());
		List<SceneResp> sceneList=sceneService.findSceneDetailList(sceneReq);
		if(CollectionUtils.isNotEmpty(sceneList)) {
			sceneList.forEach(scene->{
				ShortcutVo vo_=getShortcutVo(scene.getId(),scene.getSceneName(),Constants.SHORTCHT_ROOM_SCENE,
						scene.getTenantId(),scene.getLocationId());
				voList.add(vo_);
			});
		}
	}

	private void getLocalSceneShortcutList(ShortcutVo vo, List<ShortcutVo> voList) {
		List<LocationSceneResp> locationSceneList=getLocalListSecneByBuildOrFloor(vo.getOrgId(),vo.getSpaceId(),vo.getTenantId(),vo.getLocationId());
		if(CollectionUtils.isNotEmpty(locationSceneList)) {
			locationSceneList.forEach(resp->{
				ShortcutVo vo_=getShortcutVo(resp.getId(),resp.getName(),Constants.SHORTCHT_LOCATIONSCENE,
						resp.getTenantId(),resp.getLocationId());
				voList.add(vo_);
			});
		}
	}
	
	public List<LocationSceneResp> getLocalListSecneByBuildOrFloor(Long orgId, Long spaceId,Long tenantId,Long locationId){
		LocationSceneReq locationSceneReq=new LocationSceneReq();
		locationSceneReq.setTenantId(tenantId);
		locationSceneReq.setOrgId(orgId);
		locationSceneReq.setLocationId(locationId);
		locationSceneReq.setShortcut(1);//生成快捷方式的
		List<LocationSceneResp> locationSceneList=null;
		if(spaceId !=null) {
			if(spaceId.compareTo(-1L)==0) {
				locationSceneReq.setBuildId(spaceId);
				locationSceneReq.setFloorId(spaceId);
			}else {
				SpaceResp resp=spaceApi.findSpaceInfoBySpaceId(tenantId, spaceId);
				if(resp.getType().equals(Constants.SPACE_BUILD)) {
					locationSceneReq.setBuildId(spaceId);
					locationSceneReq.setFloorId(-1L);
				}else if(resp.getType().equals(Constants.SPACE_FLOOR)) {
					locationSceneReq.setFloorId(spaceId);
				}else if(resp.getType().equals(Constants.SPACE_ROOM)) {
					return null;
				}
			}
			locationSceneList=sceneService.findLocationSceneList(locationSceneReq);
		}
		return locationSceneList;
	}
	
	private void getSceneTemplateShortcutList(ShortcutVo vo, List<ShortcutVo> voList){
		TemplateReq templateReq =new TemplateReq();
		templateReq.setTenantId(vo.getTenantId());
		templateReq.setLocationId(vo.getLocationId());
		templateReq.setShortcut(1);//存在快捷方式
		templateReq.setTemplateType(Constants.SHORTCHT_SCENE);
		List<TemplateResp> templateSceneList=templateService.findTemplateList(templateReq);
		if(CollectionUtils.isNotEmpty(templateSceneList)) {
			templateSceneList.forEach(resp->{
				ShortcutVo vo_=getShortcutVo(resp.getId(),resp.getName(),Constants.SHORTCHT_SCENE,
						resp.getTenantId(),resp.getLocationId());
				voList.add(vo_);
			});
		}
	}
	
	private void getIftttTemplateShortcutList(ShortcutVo vo, List<ShortcutVo> voList){
		RuleListReq ruleReq = new RuleListReq();
		ruleReq.setTenantId(vo.getTenantId());
		ruleReq.setLocationId(vo.getLocationId());
		ruleReq.setTemplateFlag(Byte.valueOf("1"));
		ruleReq.setShowTime(false);
		ruleReq.setShortcut(1);//存在快捷方式
		List<RuleResp> templateList=ruleService.listNoPage(ruleReq);
		if(CollectionUtils.isNotEmpty(templateList)) {
			templateList.forEach(resp->{
				ShortcutVo vo_=getShortcutVo(resp.getId(),resp.getName(),Constants.SHORTCHT_IFTTT,
						resp.getTenantId(),resp.getLocationId());
				voList.add(vo_);
			});
		}
	}

	@Override
	public void excute(ShortcutVo vo) {
		if(Constants.SHORTCHT_IFTTT.equals(vo.getType())) {
			setIftttEnableByTemplate(vo);
		}else if(Constants.SHORTCHT_SCENE.equals(vo.getType())) {
			excuteSceneByTemplate(vo); 
		}else if(Constants.SHORTCHT_LOCATIONSCENE.equals(vo.getType())) {
			excuteSceneByLocalScene(vo);
		}else if(Constants.SHORTCHT_ROOM_SCENE.equals(vo.getType())) {
			sceneService.sceneExecute(vo.getTenantId(),vo.getId());
		}else if(Constants.SHORTCHT_ROOM_IFTTT.equals(vo.getType())) {
			roomIftttExcute(vo);
		}
	}
	
	private void roomIftttExcute(ShortcutVo vo) {
		RuleResp rule=autoTobService.get(vo.getTenantId(),vo.getOrgId(), vo.getId());
		setIftttOnOff(vo.getFlag(), rule);
	}
	
	@Override
	public void scheduleExcute(ShortcutVo vo) {
		LOGGER.info("---------------schedule Excute start---------------------");
		LOGGER.info(vo.getName()+" "+vo.getType());
		int type=0;
		if(vo.getType().toUpperCase().contains("IFTTT")) {
			type=1;
		}
		Integer count=buildingSpaceService.countExistCalendar(ToolUtil.localToUTC(),type,vo.getLocationId(),vo.getOrgId(),vo.getTenantId());
		if(count>0) {
			LOGGER.info("---------------exist calendar---------------------");
			throw new BusinessException(BusinessExceptionEnum.EXIST_CALENDAR);
		}
		if(Constants.SCHEDULE_ROOM_SCENE.equals(vo.getType())) {
			LOGGER.info("---------------schedule scene---------------------");
			sceneService.sceneExecute(vo.getTenantId(),vo.getId());
		}else if(Constants.SCHEDULE_ROOM_IFTTT.equals(vo.getType())) {
			LOGGER.info("---------------schedule ifttt---------------------");
			roomIftttExcute(vo);
		}else if(Constants.SCHEDULE_LOCATION.equals(vo.getType())) {
			LOGGER.info("---------------schedule location---------------------");
			excuteSceneByLocalScene(vo);
		}else if(Constants.SCHEDULE_SCENE_TEMPLATE.equals(vo.getType())) {
			LOGGER.info("---------------schedule scene_template---------------------");
			excuteSceneByTemplate(vo); 
		}else if(Constants.SCHEDULE_IFTTT_TEMPLATE.equals(vo.getType())) {
			LOGGER.info("---------------schedule ifttt_template---------------------");
			setIftttEnableByTemplate(vo);
		}
		LOGGER.info("---------------schedule Excute end---------------------");
	}

	private void setIftttEnableByTemplate(ShortcutVo vo) {
		// 根据模板查具体的IFTTT 
		SaveIftttReq iftttReq=new SaveIftttReq();
		iftttReq.setTemplateId(vo.getId());
		List<RuleResp> ruleList=autoTobService.getRuleList(iftttReq);
		if(CollectionUtils.isNotEmpty(ruleList)) {
			ruleList.forEach(rule->{
				setIftttOnOff(vo.getFlag(), rule);
			});
		}else {
			LOGGER.info("tempalte :"+vo.getId()+" 没有找到具体的ifTTT ...");
		}
	}

	private void setIftttOnOff(int status, RuleResp rule) {
		Boolean flag=status==0?false:true;//使能开关
		autoTobService.setIftttOnOff(flag,rule);
		/*autoTobApi.runApplet(rule.getTenantId(), rule.getId(),flag);
		autoTobService.run(rule.getId(),flag);*/
	}

	private void excuteSceneByLocalScene(ShortcutVo vo) {
		List<Long> sceneIds = sceneService.findSceneIds(vo.getId());
		if(CollectionUtils.isNotEmpty(sceneIds)) {
			for(Long sceneId:sceneIds){
				sceneService.sceneExecute(vo.getTenantId(),sceneId);
			}
		}
	}

	private void excuteSceneByTemplate(ShortcutVo vo) {
		SceneReq sceneReq =new SceneReq();
		sceneReq.setTenantId(vo.getTenantId());
		sceneReq.setTemplateId(vo.getId());
		List<SceneResp> respList=sceneApi.sceneByParamDoing(sceneReq);
		if(CollectionUtils.isNotEmpty(respList)) {
			respList.forEach(resp->{
				sceneService.sceneExecute(resp.getTenantId(),resp.getId());
			});
		}
	}
}
