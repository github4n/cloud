package com.iot.building.template.service;

import com.iot.building.template.vo.ScheduledDetailVo;
import com.iot.building.template.vo.ScheduledVo;
import com.iot.building.template.vo.req.SpaceTemplateReq;
import com.iot.building.template.vo.rsp.SpaceTemplateResp;
import com.iot.common.helper.Page;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

public interface SpaceTemplateService {

	Page<SpaceTemplateResp> list(SpaceTemplateReq spaceTemplateReq);

	List<SpaceTemplateResp> findByCondition(SpaceTemplateReq spaceTemplateReq);
	
	List<Long> findTemplateIdListByCondition(SpaceTemplateReq spaceTemplateReq);

	Long spaceTemplateSave(SpaceTemplateReq spaceTemplateReq);

	Integer spaceTemplateDelete(SpaceTemplateReq spaceTemplateReq);
	
	Integer spaceTemplateUpdate(SpaceTemplateReq spaceTemplateReq);
	
	List<ScheduledVo> findByValidityDate(SpaceTemplateReq spaceTemplateReq);
	
	ScheduledDetailVo findScheduledDetalAndLog(SpaceTemplateReq spaceTemplateReq);
	
	Map<String,Object> beforeAndAfterSchedule(Long locationId,Long currentTime,Long endTime,String week,Long spaceId);

}
