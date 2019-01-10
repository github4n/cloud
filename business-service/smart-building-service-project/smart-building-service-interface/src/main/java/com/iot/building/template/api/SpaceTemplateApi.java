package com.iot.building.template.api;

import com.iot.building.template.vo.ScheduledDetailVo;
import com.iot.building.template.vo.ScheduledVo;
import com.iot.building.template.vo.req.SpaceTemplateReq;
import com.iot.building.template.vo.rsp.SpaceTemplateResp;
import com.iot.common.helper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;

@Api("情景/ifttt的空间模板接口")
@FeignClient(value = "building-control-service")
@RequestMapping("/spaceTemplate")
public interface SpaceTemplateApi {
	
	@ApiOperation("空间模板分页数据获取")
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Page<SpaceTemplateResp> list(@RequestBody SpaceTemplateReq spaceTemplateReq);
	
	@ApiOperation("空间模板列表数据获取")
    @RequestMapping(value = "/findByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<SpaceTemplateResp> findByCondition(@RequestBody SpaceTemplateReq spaceTemplateReq);
	
	@ApiOperation("空间模板列表数据保存")
    @RequestMapping(value = "/spaceTemplateSave", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Long spaceTemplateSave(@RequestBody SpaceTemplateReq spaceTemplateReq);
	
	@ApiOperation("空间模板列表数据删除")
    @RequestMapping(value = "/spaceTemplateDelete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Integer spaceTemplateDelete(@RequestBody SpaceTemplateReq spaceTemplateReq);
	
	@ApiOperation("空间模板修改模板ID")
    @RequestMapping(value = "/spaceTemplateUpdateTemplateId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Integer spaceTemplateUpdate(@RequestBody SpaceTemplateReq spaceTemplateReq);

	@ApiOperation("空间模板ID列表数据获取")
    @RequestMapping(value = "/findTemplateIdListByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<Long> findTemplateIdListByCondition(@RequestBody SpaceTemplateReq spaceTemplateReq);

	@ApiOperation("根据日期获取数据")
    @RequestMapping(value = "/findByValidityDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<ScheduledVo> findByValidityDate(@RequestBody SpaceTemplateReq spaceTemplateReq);
	
	@ApiOperation("根据ID获取数据")
	@RequestMapping(value = "/findScheduledDetalAndLog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	ScheduledDetailVo findScheduledDetalAndLog(@RequestBody SpaceTemplateReq spaceTemplateReq);
	
	@ApiOperation("根据ID获取数据")
	@RequestMapping(value = "/beforeAndAfterSchedule", method = RequestMethod.GET)
	Map<String,Object> beforeAndAfterSchedule(@RequestParam("locationId")Long locationId,@RequestParam("currentTime")Long currentTime,@RequestParam("endTime")Long endTime,
			@RequestParam("week")String week,@RequestParam("spaceId")Long spaceId);
}
