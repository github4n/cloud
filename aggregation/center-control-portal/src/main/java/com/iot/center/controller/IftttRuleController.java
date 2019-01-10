package com.iot.center.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.iot.building.ifttt.api.AutoTobApi;
import com.iot.building.ifttt.api.IftttApi;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.center.annotation.PermissionAnnotation;
import com.iot.center.annotation.SystemLogAnnotation;
import com.iot.center.service.IftttRuleService;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.common.util.StringUtil;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.user.vo.LoginResp;
import com.iot.building.ifttt.vo.res.RuleResp;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Api(tags = "IFTTT联动接口")
@Controller
@RequestMapping("/ifttt/rule")
public class IftttRuleController {
	private final Logger logger = LoggerFactory.getLogger(IftttRuleController.class);

	@Autowired
	IftttRuleService iftttRuleService;

	@Autowired
	IftttApi iftttApi;

	@Autowired
	ScheduleApi scheduleApi;

	@Autowired
	private AutoTobApi autoTobApi;

	@Autowired
	private DeviceCoreApi deviceCoreApi;

	@PermissionAnnotation(value = "IFTTT,MY_IFTTT")
	@SystemLogAnnotation(value = "保存用户的规则")
	@ApiOperation(value = "保存", notes = "保存用户的规则")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> save(@RequestBody SaveIftttReq ruleVO) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		ruleVO.setTenantId(user.getTenantId());
		ruleVO.setLocationId(user.getLocationId());
		ruleVO.setUserId(user.getUserId());
		List<SensorVo> sensorVos = ruleVO.getSensors();
		for(SensorVo sensorVo : sensorVos){
			if(sensorVo.getProperties().contains("button")){
				logger.info("==============sensorVo.getProperties()============="+sensorVo.getProperties());
				//为button类型
				String propertiess = setButton(sensorVo);
				sensorVo.setProperties(propertiess);
			}
		}
		//return iftttRuleService.save(ruleVO);重构前
		//重构后
		return iftttRuleService.saveReconsitution(ruleVO);
	}

	public String setButton(SensorVo sensorVo){
		String properties = null;
		Map<String,Object> map = Maps.newHashMap();
		JSONArray jsonArray = JSONObject.parseArray(sensorVo.getProperties());
		String triggerValue = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerValue"));
		String propertyType = String.valueOf(((JSONObject) jsonArray.get(0)).get("propertyType"));
		String triggerSign = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerSign"));
		if(!triggerValue.contains("[") || !triggerValue.contains("]")){
			triggerValue = "["+triggerValue+"]";
		}
		properties = "[{\"propertyName\":\"button\",\"triggerValue\":\""+ triggerValue +"\",\"propertyType\":\""+propertyType+"\",\"triggerSign\":\""+triggerSign+"\"}]";
	    return properties;
	}

	//@SystemLogAnnotation(value = "删除规则")
	@PermissionAnnotation(value = "IFTTT,MY_IFTTT")
	@ApiOperation(value = "删除规则/带批量删除功能", notes = "根据ID删除规则/带批量删除功能")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public CommonResponse<ResultMsg> delete(@PathVariable("id") @ApiParam(value = "规则ID", required = true) String id) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			//重构前
			//RuleResp ruleVO = iftttApi.get(id);
			//重构后
			//RuleResp ruleVO = iftttRuleService.get(user.getTenantId(), id);

			//ifttt 中的  timer 用的,先注释掉，以后有用再放开
			/*List<SensorVo> sensorVOs = ruleVO.getSensors();
			//删除类型为TimingJob 的定时任务
			for (SensorVo sensorVO : sensorVOs) {
				//LoadTask.removeTimerTask(sensorVO.getId(), "ifttt");
				scheduleApi.delJob(ScheduleConstants.TIMING_JOB+sensorVO.getId());
			}*/
			//重构前
			//List<Integer> result1 = iftttApi.delete(id);

			//重构后  带批量删除功能
			String[] ids = id.split(",");
			for(String str:ids){
				List<Integer> result = iftttRuleService.delete(user.getTenantId(),user.getOrgId(), Long.valueOf(str),true);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}


	@ApiOperation(value = "获取单个规则", notes = "根据ID获取单个规则")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public CommonResponse<RuleResp> Cgget(@PathVariable("id") @ApiParam(value = "规则ID", required = true) Long id,@ApiParam(value = "是否为ifttt模板,0.不是，1.是", required = true) String type) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			RuleResp ruleVO = new RuleResp();
			//RuleResp ruleVO = iftttApi.get(id); //重构前
			//重构后
			if(type.equals("0")){
				ruleVO = iftttRuleService.get(user.getTenantId(),user.getOrgId(), id);
			}else if(type.equals("1")){
				Long tenantId = user.getTenantId();
				ruleVO = iftttApi.get(tenantId,id);
			}
			return CommonResponse.success(ruleVO);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
	}

	//@PermissionAnnotation(value = "MY_IFTTT")
	@ApiOperation(value = "列表", notes = "用户的规则分页列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "名称", required = false, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "pageSize", value = "每页数目", required = true, paramType = "query", dataType = "String") })
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public CommonResponse<Page<RuleResp>> listCg(@RequestParam(value = "name", required = false) String name,
											   @RequestParam(value = "spaceId", required = false) Long spaceId, @RequestParam("pageNum") Integer pageNum,
											   @RequestParam("pageSize") Integer pageSize) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			RuleListReq req = new RuleListReq();
			req.setLocationId(user.getLocationId());
			req.setTenantId(user.getTenantId());
			req.setName(name);
			req.setSpaceId(spaceId);
			req.setPageNum(pageNum);
			req.setPageSize(pageSize);
			req.setTemplateFlag(Byte.valueOf("0"));
			req.setShowTime(false);
			//重构前
			//Page<RuleResp> page1 = iftttApi.list(req);
			//重构后
			Page<RuleResp> page = iftttRuleService.list(req);
			return CommonResponse.success(page);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@PermissionAnnotation(value = "IFTTT,MY_IFTTT")
	@SystemLogAnnotation(value = "启用用户的规则")
	@ApiOperation(value = "启用规则", notes = "启用用户的规则")
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> run(@PathVariable("id") @ApiParam(value = "规则ID") Long id,
										 @RequestParam("start") @ApiParam(value = "是否启用") Boolean start) {
		try {
			boolean result = false;
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			//重构前
			//result1 = iftttApi.run(id, start);
			//重构后
			//通过id 去查找appletId
			RuleResp ruleResp = autoTobApi.get(user.getTenantId(),user.getOrgId(),id);
			GetDeviceInfoRespVo getDeviceInfoRespVo = deviceCoreApi.get(ruleResp.getTriggers().get(0).getSensorDeviceId());
			String clientId = getDeviceInfoRespVo.getParentId();
			Long appletId = ruleResp.getAppletId();
			String uploadStatus = ruleResp.getUploadStatus();
			if(StringUtil.isNotBlank(uploadStatus)){
				if(uploadStatus.equals("1")){
					logger.info("====同一个网关，只改build_tob_rule,和改网关下的========");
					//同一个网关，只改build_tob_rule,和改网关下的
					result = iftttRuleService.run(user.getTenantId(),user.getOrgId(), id,start);//build_tob_rule
                    autoTobApi.runLowerHair(id,clientId,start);
				}else if(uploadStatus.equals("0")){
					logger.info("====跨网关，改applet 和 build_tob_rule========");
					//跨网关，改applet 和 build_tob_rule
					iftttRuleService.runApplet(user.getTenantId(),user.getOrgId(),appletId,start);//applet
					result = iftttRuleService.run(user.getTenantId(),user.getOrgId(), id,start);//build_tob_rule
				}
			}
			if (result) {
				return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return new CommonResponse<ResultMsg>(400, (start ? "启动" : "停止") + "失败");
	}


	//==========================================================重构前的代码=================================================================//
	@ApiOperation(value = "列表", notes = "用户的规则分页列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "名称", required = false, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "pageSize", value = "每页数目", required = true, paramType = "query", dataType = "String") })
	@RequestMapping(value = "/listCg", method = RequestMethod.GET)
	@ResponseBody
	public CommonResponse<Page<RuleResp>> list(@RequestParam(value = "name", required = false) String name,
											   @RequestParam(value = "spaceId", required = false) Long spaceId, @RequestParam("pageNum") Integer pageNum,
											   @RequestParam("pageSize") Integer pageSize) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			RuleListReq req = new RuleListReq();
			req.setLocationId(user.getLocationId());
			req.setTenantId(user.getTenantId());
			req.setName(name);
			req.setSpaceId(spaceId);
			req.setPageNum(pageNum);
			req.setPageSize(pageSize);
			req.setTemplateFlag(Byte.valueOf("0"));
			req.setShowTime(false);

			Page<RuleResp> page = iftttApi.list(req);
			return CommonResponse.success(page);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
	}


	@SystemLogAnnotation(value = "保存用户的规则")
	@ApiOperation(value = "保存", notes = "保存用户的规则")
	@RequestMapping(value = "/saveCg", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> saveCg(@RequestBody SaveIftttReq ruleVO) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		ruleVO.setTenantId(user.getTenantId());
		ruleVO.setLocationId(user.getLocationId());
		ruleVO.setUserId(user.getUserId());
		ruleVO.setOrgId(user.getOrgId());
		return  iftttRuleService.save(ruleVO);
	}

	@SystemLogAnnotation(value = "启用用户的规则")
	@ApiOperation(value = "启用规则", notes = "启用用户的规则")
	@RequestMapping(value = "Cg/{id}", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> runCg(@PathVariable("id") @ApiParam(value = "规则ID") Long id,
										 @RequestParam("start") @ApiParam(value = "是否启用") Boolean start) {
		boolean result;
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Long tenantId = user.getTenantId();
		try {
			result = iftttApi.run(tenantId,id, start);
			if (result) {
				return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return new CommonResponse<ResultMsg>(400, (start ? "启动" : "停止") + "失败");
	}

	@ApiOperation(value = "获取规则", notes = "根据ID获取规则")
	@RequestMapping(value = "Cg/{id}", method = RequestMethod.GET)
	@ResponseBody
	public CommonResponse<RuleResp> getCg(@PathVariable("id") @ApiParam(value = "规则ID", required = true) Long id) {
		try {
			//LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			//Long tenantId = user.getTenantId();
			Long tenantId = 11L;
			RuleResp ruleVO = iftttApi.get(tenantId,id);
			return CommonResponse.success(ruleVO);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@SystemLogAnnotation(value = "删除规则")
	@ApiOperation(value = "删除规则", notes = "根据ID删除规则")
	@RequestMapping(value = "Cg/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public CommonResponse<ResultMsg> deleteCg(@PathVariable("id") @ApiParam(value = "规则ID", required = true) Long id) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			Long tenantId = user.getTenantId();
			RuleResp ruleVO = iftttApi.get(user.getTenantId(),id);
			List<SensorVo> sensorVOs = ruleVO.getSensors();
			for (SensorVo sensorVO : sensorVOs) {
				//LoadTask.removeTimerTask(sensorVO.getId(), "ifttt");
				scheduleApi.delJob(ScheduleConstants.TIMING_JOB+sensorVO.getId());
			}
			List<Integer> result = iftttApi.delete(tenantId,id);
//			if (!result) {
//				return new CommonResponse<ResultMsg>(ResultMsg.FAIL);
//			}
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}


	@ApiOperation(value = "删除规则/带批量删除功能", notes = "根据ID删除规则/带批量删除功能")
	@RequestMapping(value = "deleteWaiBu/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public CommonResponse<ResultMsg> deleteWaiBu(@PathVariable("id") @ApiParam(value = "规则ID", required = true) String id,@RequestParam(value ="clientId") String clientId) {
		try {
			autoTobApi.deleteLowerHair(Long.valueOf(id),clientId);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}


}



