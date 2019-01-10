package com.iot.building.template.controller;

import com.iot.building.template.api.SpaceTemplateApi;
import com.iot.building.template.service.SpaceTemplateService;
import com.iot.building.template.vo.ScheduledDetailVo;
import com.iot.building.template.vo.ScheduledVo;
import com.iot.building.template.vo.req.SpaceTemplateReq;
import com.iot.building.template.vo.rsp.SpaceTemplateResp;
import com.iot.common.helper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class SpaceTemplateController implements SpaceTemplateApi {
	
	@Autowired
	SpaceTemplateService spaceTemplateService;

	@Override
	public Page<SpaceTemplateResp> list(@RequestBody SpaceTemplateReq spaceTemplateReq) {
		Page<SpaceTemplateResp> page = spaceTemplateService.list(spaceTemplateReq);
		return page;
	}

	@Override
	public List<SpaceTemplateResp> findByCondition(@RequestBody SpaceTemplateReq spaceTemplateReq) {
		List<SpaceTemplateResp> list = spaceTemplateService.findByCondition(spaceTemplateReq);
		return list;
	}
	
	@Override
	public List<Long> findTemplateIdListByCondition(@RequestBody SpaceTemplateReq spaceTemplateReq) {
		List<Long> list = spaceTemplateService.findTemplateIdListByCondition(spaceTemplateReq);
		return list;
	}

	@Override
	public Long spaceTemplateSave(@RequestBody SpaceTemplateReq spaceTemplateReq) {
		Long rec = spaceTemplateService.spaceTemplateSave(spaceTemplateReq);
		return rec;
	}

	@Override
	public Integer spaceTemplateDelete(@RequestBody SpaceTemplateReq spaceTemplateReq) {
		Integer rec = spaceTemplateService.spaceTemplateDelete(spaceTemplateReq);
		return rec;
	}

	@Override
	public Integer spaceTemplateUpdate(@RequestBody SpaceTemplateReq spaceTemplateReq) {
		Integer rec = spaceTemplateService.spaceTemplateUpdate(spaceTemplateReq);
		return rec;
	}
	
	@Override
	public List<ScheduledVo> findByValidityDate(@RequestBody SpaceTemplateReq spaceTemplateReq){
		return spaceTemplateService.findByValidityDate(spaceTemplateReq);
	}

	@Override
	public ScheduledDetailVo findScheduledDetalAndLog(@RequestBody SpaceTemplateReq spaceTemplateReq) {
		return spaceTemplateService.findScheduledDetalAndLog(spaceTemplateReq);
	}

	@Override
	public Map<String, Object> beforeAndAfterSchedule(Long locationId,Long currentTime,Long endTime, String week, Long spaceId) {
		return spaceTemplateService.beforeAndAfterSchedule(locationId,currentTime,endTime, week, spaceId);
	}

}
