package com.iot.center.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.building.ifttt.api.IftttApi;
import com.iot.building.ifttt.constant.IftttConstants;
import com.iot.building.ifttt.vo.ActuatorVo;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.center.utils.CronDateUtils;
import com.iot.center.utils.ValueUtils;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.device.api.DeviceCoreApi;
import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.vo.AddJobReq;
import com.iot.building.ifttt.api.AutoTobApi;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IftttRuleService  {

	private final static Logger logger = LoggerFactory.getLogger(IftttRuleService.class);

	public final static String IFTTT_TRIGGER_TIMER = "timer";
	public final static String IFTTT_2B_FLAG = "_2B";

	@Autowired
	SceneApi sceneApi;
	@Autowired
	IftttApi iftttApi;
	@Autowired
	ScheduleApi scheduleApi;

	//重构后ifttt调用的api
	@Autowired
	AutoTobApi autoTobApi;

	@Autowired
	DeviceCoreApi deviceCoreApi;

	public void addQuartzReconsitution(List<SensorVo> sensors){
		for (SensorVo sensorVO : sensors) {
			if(sensorVO.getType().equals(IftttConstants.TRIGGER_TYPE_TIMIMG)){
				SensorVo sensorMapperVo=new SensorVo();
				sensorMapperVo.setId(sensorVO.getId());
				sensorMapperVo.setRuleId(sensorVO.getRuleId());
				sensorMapperVo.setProperties(sensorVO.getProperties());
				sensorMapperVo.setType(sensorVO.getType());
				List<Map<String, Object>>maps=Lists.newArrayList();
				maps=(List<Map<String, Object>>)JSON.parse(sensorMapperVo.getProperties());
				for (Map<String, Object> map : maps) {
					String dateString=(String)map.get("triggerValue");
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					Date date=null;
					try {
						date = sdf.parse(dateString);
					} catch (Exception e) {
					}
					//LoadTask.timerTask(date, sensorMapperVo.getId(), sensorMapperVo,"ifttt");
					String cron = CronDateUtils.getCron(date);//获得quartz时间表达式
					AddJobReq req = new AddJobReq();
					req.setData(sensorMapperVo);
					req.setCron(cron);
					req.setJobClass(ScheduleConstants.TIMING_JOB);
					req.setJobName(ScheduleConstants.TIMING_JOB + sensorMapperVo.getId());
					scheduleApi.addJob(req);
				}
			}
		}
	}

	public CommonResponse<ResultMsg> saveReconsitution(SaveIftttReq ruleVO) {
		ruleVO.setTemplateFlag(Byte.valueOf("0"));
		ruleVO.setIsMulti(Byte.valueOf("1"));
		ruleVO.setIftttType(IFTTT_2B_FLAG);
		Long appletId = null;
		Long buildToRuleId = null;
		//判断name是否重复
		List<RuleResp> list =  autoTobApi.getRuleListByName(ruleVO);
		if(ruleVO.getId() == null){//修改的时候不需要校验保存的名字是否重复
			if(list.size() !=0){
				//名字有重复
				return new CommonResponse(500,"名称重复!");
			}
		}
		//如果有主键则先删除旧规则
		if (ruleVO.getId() != null && ruleVO.getId() != 0) {
			delete(ruleVO.getTenantId(),ruleVO.getOrgId(),ruleVO.getId(), false);
		}
		//判断是同网关还是跨网关
		boolean isTemplateFlag = false;
		if(StringUtil.isNotBlank(ruleVO.getType()) && ruleVO.getType().equals("template")){
			logger.info("=======isTemplateFlag============="+isTemplateFlag);
			isTemplateFlag = true;
		}
		List<String> deviceIds = Lists.newArrayList();
		List<Long> sceneIds = Lists.newArrayList();
		boolean isSingleGatewayFlag = false;
		Map<String,Object> map = new HashMap<String,Object>();
		if(!isTemplateFlag) {//非模板
			//获取sensor的deviceId集合
			for (SensorVo sensorVo : ruleVO.getSensors()) {
				deviceIds.add(sensorVo.getDeviceId());
			}
			for(ActuatorVo actuatorVo : ruleVO.getActuators()){
				if(StringUtil.isNotBlank(actuatorVo.getType())){
					if (actuatorVo.getType().equals("sence")) {//情景
						sceneIds.add(Long.valueOf(actuatorVo.getDeviceId()));
					} else if(actuatorVo.getType().equals("space")){//空间
						map = ValueUtils.jsonStringToMap(actuatorVo.getProperties());
						sceneIds.add(Long.valueOf(map.get("senceId").toString()));
					}else {//设备
						deviceIds.add(actuatorVo.getDeviceId());
					}
				}else {//设备
					deviceIds.add(actuatorVo.getDeviceId());
				}
			}
			ruleVO.setDeviceIds(deviceIds);
			ruleVO.setSceneIds(sceneIds);
			isSingleGatewayFlag = autoTobApi.isSingleGateway(ruleVO);
		}
		if(isSingleGatewayFlag){//单网关
			String client = deviceCoreApi.get(ruleVO.getSensors().get(0).getDeviceId()).getParentId();
			if(ruleVO.getId() !=null){
				//修改，要删除网关里的ifttt
				autoTobApi.deleteLowerHair(ruleVO.getId(),client);
			}
			ruleVO.setUploadStatus("1");
			//重构后，分四步
			//第一步先保存联动信息（sensor、actuator、or或and）,表：applet,applet_this,applet_that,applet_item
			appletId = autoTobApi.saveAuto(ruleVO);
			//第二步保存/修改ifttt记录（保存/修改build_tob_rule表）,相当于之前的ifttt_rule
			buildToRuleId = autoTobApi.saveBuildTobRule(ruleVO,appletId);
			//第三步保存点、线、sensor的位置、类型、属性，actuctor的位置、类型、属性  表为tob_trigger,相当于之前的ifttt_trigger
			autoTobApi.saveTobTrigger(ruleVO,buildToRuleId);
			//第四步保存tob_relation,相当于之前的ifttt_relation
			autoTobApi.saveTobRelation(ruleVO,buildToRuleId);
			if (buildToRuleId==0) {
				return new CommonResponse<ResultMsg>(ResultMsg.FAIL);
			}
			//创建下发给网关
			ruleVO.setClientId(client);
			ruleVO.setId(buildToRuleId);
			autoTobApi.createLowerHair(ruleVO);
		}else {
			ruleVO.setUploadStatus("0");//跨网关//重构后，分四步
			//第一步先保存联动信息（sensor、actuator、or或and）,表：applet,applet_this,applet_that,applet_item
			appletId = autoTobApi.saveAuto(ruleVO);
			//第二步保存/修改ifttt记录（保存/修改build_tob_rule表）,相当于之前的ifttt_rule
			buildToRuleId = autoTobApi.saveBuildTobRule(ruleVO,appletId);
			//第三步保存点、线、sensor的位置、类型、属性，actuctor的位置、类型、属性  表为tob_trigger,相当于之前的ifttt_trigger
			autoTobApi.saveTobTrigger(ruleVO,buildToRuleId);
			//第四步保存tob_relation,相当于之前的ifttt_relation
			autoTobApi.saveTobRelation(ruleVO,buildToRuleId);
			if (buildToRuleId==0) {
				return new CommonResponse<ResultMsg>(ResultMsg.FAIL);
			}
		}

		return CommonResponse.success();
	}

	public void deleteAll(Long orgId,Long appletId,Long buildToRuleId,Long tenantId){
		autoTobApi.deleteAll(orgId,appletId,buildToRuleId,tenantId);
	}
	/**
	 * 删除ifttt （重构后的）
	 * @param id
	 * @return
	 */
	public List<Integer> delete(Long tenantId,Long orgId,Long id,boolean flag) {
		return autoTobApi.delete(tenantId,orgId,id,flag) ;
	}

	/**
	 * 获取单个 build_tob_rule 的详情
	 * @param id
	 * @return
	 */
	public RuleResp get(Long tenantId,Long orgId,Long id) {
		RuleResp ruleResp = autoTobApi.get(tenantId,orgId,id);
		return ruleResp;
	}

	/**
	 * 列表  表build_tob_rule
	 * @param req
	 * @return
	 */
	public Page<RuleResp> list(RuleListReq req) {
		List<RuleResp> list = Lists.newArrayList();
		return autoTobApi.list(req);
	}

	/**
	 * 启用规则
	 * @param id
	 * @param start
	 * @return
	 */
	public boolean run(Long tenantId,Long orgId,Long id, Boolean start) {
		boolean flag = false;
		flag = autoTobApi.run(tenantId,orgId, id,start);
		return flag;
	}

	public void runApplet(Long tenantId,Long orgId, Long id, Boolean start) {
		autoTobApi.runApplet(tenantId,orgId, id,start);
	}


	//===============================================================重构前的代码==============================================//

	public void addQuartz(List<SensorVo> sensors){
		for (SensorVo sensorVO : sensors) {
			if(sensorVO.getType().equals(IftttConstants.TRIGGER_TYPE_TIMIMG)){
				SensorVo sensorMapperVo=new SensorVo();
				sensorMapperVo.setId(sensorVO.getId());
				sensorMapperVo.setRuleId(sensorVO.getRuleId());
				sensorMapperVo.setProperties(sensorVO.getProperties());
				sensorMapperVo.setType(sensorVO.getType());
				List<Map<String, Object>>maps=Lists.newArrayList();
				maps=(List<Map<String, Object>>)JSON.parse(sensorMapperVo.getProperties());
				for (Map<String, Object> map : maps) {
					String dateString=(String)map.get("triggerValue");
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					Date date=null;
					try {
						date = sdf.parse(dateString);
					} catch (Exception e) {
					}
					//LoadTask.timerTask(date, sensorMapperVo.getId(), sensorMapperVo,"ifttt");
					String cron = CronDateUtils.getCron(date);//获得quartz时间表达式
					AddJobReq req = new AddJobReq();
					req.setData(sensorMapperVo);
					req.setCron(cron);
					req.setJobClass(ScheduleConstants.TIMING_JOB);
					req.setJobName(ScheduleConstants.TIMING_JOB + sensorMapperVo.getId());
					scheduleApi.addJob(req);
				}
			}
		}
	}

	public CommonResponse<ResultMsg> save(SaveIftttReq ruleVO) {
		ruleVO.setTemplateFlag(Byte.valueOf("0"));
		ruleVO.setIsMulti(Byte.valueOf("1"));
		ruleVO.setIftttType(IFTTT_2B_FLAG);

		//判断name是否重复
		List<RuleResp> list = iftttApi.getRuleListByName(ruleVO);
		if(ruleVO.getId() == null){//修改的时候不需要校验保存的名字是否重复
			if(list.size() !=0){
				//名字有重复
				return new CommonResponse<ResultMsg>(ResultMsg.FAIL);
			}
		}
		List<SensorVo> sensors = ruleVO.getSensors();
		if(!CollectionUtils.isEmpty(sensors)){
			for(SensorVo sensor : sensors){
//			   if(IFTTT_TRIGGER_TIMER.equals(sensor.getType())){
				sensor.setType(sensor.getType()+IFTTT_2B_FLAG);
//			   }
			}
		}
		Long ruleId = iftttApi.save(ruleVO);
		if (ruleId==0) {
			return new CommonResponse<ResultMsg>(ResultMsg.FAIL);
		}
		RuleResp newRuleVO = iftttApi.get(ruleVO.getTenantId(),ruleId);
		addQuartz(newRuleVO.getSensors());

		return CommonResponse.success();
	}

}
