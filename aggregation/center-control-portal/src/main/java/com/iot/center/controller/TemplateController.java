package com.iot.center.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.iot.center.annotation.PermissionAnnotation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.scene.vo.resp.LocationSceneRelationResp;
import com.iot.building.template.api.TemplateTobApi;
import com.iot.building.template.vo.ScheduledDetailVo;
import com.iot.building.template.vo.ScheduledVo;
import com.iot.building.template.vo.req.BuildTemplateReq;
import com.iot.building.template.vo.req.SaveIftttTemplateReq;
import com.iot.building.template.vo.req.SpaceTemplateReq;
import com.iot.building.template.vo.req.TemplateDetailReq;
import com.iot.building.template.vo.req.TemplateReq;
import com.iot.building.template.vo.rsp.SceneTemplateResp;
import com.iot.building.template.vo.rsp.TemplateResp;
import com.iot.center.annotation.SystemLogAnnotation;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.helper.Constants;
import com.iot.center.service.SceneService;
import com.iot.center.service.TemplateService;
import com.iot.center.vo.ScheduleVo;
import com.iot.center.vo.SpaceListVO;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.device.vo.rsp.DataPointResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.vo.LoginResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "模板接口") 
@RestController
@RequestMapping("/template")
public class TemplateController {

	@Autowired
	private SceneService sceneService;
	@Autowired
	private TemplateTobApi templateTobApi;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private DeviceBusinessTypeApi deviceBusinessTypeApi;

    @ApiOperation(value = "IFTTT模板列表(带分页的)", notes = "IFTTT模板列表(带分页的)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "templateType", value = "类型", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页数目", required = true, paramType = "query", dataType = "String") })
    @RequestMapping(value = "/template/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Page<RuleResp>> templateList(@RequestParam(value = "name", required = false) String name,
                                                       @RequestParam(value = "templateType", required = false) String templateType,
                                                       @RequestParam("pageNum") String pageNum, @RequestParam("pageSize") String pageSize) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            Page<RuleResp> list = templateService.findTemplateList(name, templateType, pageNum, pageSize,
                    user.getTenantId());
            return CommonResponse.success(list);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @RequestMapping(value = "/template/listNoPage", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<List<RuleResp>> templateListNoPage(@RequestParam(value = "deployMentId", required = false) Long deployMentId) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            Long tenantId = user.getTenantId();
            //Long tenantId = 11L;
            List<RuleResp> list = templateService.findTemplateListNoPage(tenantId,deployMentId);
            return CommonResponse.success(list);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @ApiOperation(value = "模板列表", notes = "模板列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = false, paramType = "query", dataType = "String") })
    @RequestMapping(value = "/all/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<List<Map<String, Object>>> getAllTemplateList(
            @RequestParam(value = "name", required = false) String name) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            List<Map<String, Object>> list = templateService.getAllTemplateList(name, user.getTenantId(),user.getLocationId());
            return CommonResponse.success(list);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PermissionAnnotation(value = "MY_IFTTT_TEMPLATE")
    @SystemLogAnnotation(value = "新增IFTTT模板")
    @ApiOperation(value = "新增IFTTT模板", notes = "新增IFTTT模板")
    @RequestMapping(value = "/template/add", method = RequestMethod.POST)
    @ResponseBody
    public  CommonResponse<ResultMsg> templateAdd(@RequestBody SaveIftttTemplateReq iftttReq) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            iftttReq.setLocationId(user.getLocationId());
            iftttReq.setTenantId(user.getTenantId());
            iftttReq.setIftttType(Constants.IFTTT_2B_FLAG);
            iftttReq.setUserId(user.getUserId());
            iftttReq.setType(Constants.IFTTT_TYPE_TEMPLATE);
            boolean flag = templateService.findTemplateListByName(iftttReq);
            if(iftttReq.getId() == null){//新增
                if(!flag){
                    return new CommonResponse(500,"名称重复!");
                }
            }
            templateService.addTemplate(iftttReq);
            return  CommonResponse.success();
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PermissionAnnotation(value = "MY_IFTTT_TEMPLATE")
    @SystemLogAnnotation(value = "修改IFTTT模板")
    @ApiOperation(value = "修改IFTTT模板", notes = "修改IFTTT模板")
    @RequestMapping(value = "/template/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<String> templateUpdate(@RequestBody SaveIftttTemplateReq iftttReq) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            iftttReq.setLocationId(user.getLocationId());
            templateService.updateTemplate(iftttReq);
            return CommonResponse.success();
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PermissionAnnotation(value = "MY_IFTTT_TEMPLATE")
    @SystemLogAnnotation(value = "删除IFTTT模板")
    @ApiOperation(value = "删除IFTTT模板/带批量删除功能", notes = "删除IFTTT模板/带批量删除功能")
    @ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "模板ID", required = true, dataType = "Long") })
    @RequestMapping(value = "/template/delete", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse<String> templateDelete(@RequestParam(value = "id", required = true) String id) {
        try {
            Long tenantId = SaaSContextHolder.currentTenantId();
            String[] ids = id.split(",");
            for(String str:ids){
                templateService.deleteTemplate(Long.valueOf(str),tenantId);
            }
            return CommonResponse.success();
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @ApiOperation(value = "获取业务类型列表", notes = "获取业务类型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businessType", value = "产品名称", required = false, paramType = "query", dataType = "String") })
    @RequestMapping(value = "/product/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse<List<DeviceBusinessTypeResp>> productList(
            @RequestParam(value = "businessType", required = false) String businessType) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            //Long tenantId = user.getTenantId();
            //获得业务类型
            List<DeviceBusinessTypeResp> businessTypeList = deviceBusinessTypeApi.findBusinessTypeList(user.getOrgId(),11L,businessType);

           /* //获取deviceTypeId，productName，deviceTypeName 通过productId
            List<Long> productIds = Lists.newArrayList();
            for(DeviceBusinessTypeResp deviceBusinessTypeResp : businessTypeList){
                //去除重复的productId
                if(!productIds.contains(deviceBusinessTypeResp.getProductId())){
                    productIds.add(deviceBusinessTypeResp.getProductId());
                }
            }
            ListProductInfoReq params = new ListProductInfoReq();
            params.setProductIds(productIds);
            List<ListProductRespVo> listProductRespVos = productCoreApi.listProducts(params);
            //ListProductInfoReq listProductInfoReq = new ListProductInfoReq();
            //List<ProductResp> productRespList = centralControlDeviceApi.listProducts(productIds);
            for (DeviceBusinessTypeResp deviceBusinessTypeResp : businessTypeList){
                Long productStr = deviceBusinessTypeResp.getProductId();
                for (ListProductRespVo listProductRespVo : listProductRespVos){
                    if(productStr.longValue() == listProductRespVo.getId().longValue()){
                        deviceBusinessTypeResp.setDeviceTypeId(listProductRespVo.getDeviceTypeId());
                        // todo find deviceTypeName
//                        deviceBusinessTypeResp.setDeviceTypeName(listProductRespVo.getDeviceTypeName());
                        deviceBusinessTypeResp.setProductName(listProductRespVo.getProductName());
                    }
                }
            }*/
            return CommonResponse.success(businessTypeList);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @ApiOperation(value = "根据产品ID获取产品功能点", notes = "根据产品ID获取产品功能点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品ID", required = true, paramType = "query", dataType = "Long") })
    @RequestMapping(value = "/product/datapoint", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse<List<DataPointResp>> findDataPointListByProductId(
            @RequestParam(value = "productId", required = true) Long productId) {
        List<DataPointResp> list = templateService.findDataPointListByProductId(productId);
        return CommonResponse.success(list);
    }

    @PermissionAnnotation(value = "SPACE")
    @SystemLogAnnotation(value = "新增schedled")
    @ApiOperation(value = "新增schedled", notes = "新增schedled")
    @RequestMapping(value = "/space/save")
    @ResponseBody
    public CommonResponse<String> spaceTemplateSave(@RequestBody ScheduleVo vo) {
        try {
        	SpaceTemplateReq spaceTemplateReq = commonSpaceTemplateReq(vo);
            templateService.addSpaceTemplate(spaceTemplateReq);
            return CommonResponse.success();
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }
//    	{"startTimes":["1543248000000"],"endTimes":["1543507200000"],"week":"1,2,3","runTime":"02:00","spaceId":11,"templateType":"ifttt_template","loopType":"1","templateIds":["4359"],"id":"","business":"1"}

	private SpaceTemplateReq commonSpaceTemplateReq(ScheduleVo vo) {
		SpaceTemplateReq spaceTemplateReq=new SpaceTemplateReq();
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		spaceTemplateReq.setLocationId(user.getLocationId());
		spaceTemplateReq.setTenantId(user.getTenantId());
		spaceTemplateReq.setUpdateBy(user.getUserId());
		spaceTemplateReq.setBusiness(vo.getBusiness());
		spaceTemplateReq.setLoopType(vo.getLoopType());
		spaceTemplateReq.setRunTime(vo.getRunTime());
		spaceTemplateReq.setSpaceId(vo.getSpaceId());
		spaceTemplateReq.setTemplateType(vo.getTemplateType());
		spaceTemplateReq.setWeek(vo.getWeek());
		spaceTemplateReq.setTemplateName(vo.getTemplateName());
		spaceTemplateReq.setId(vo.getId());
		spaceTemplateReq.setZone(vo.getZone());
		List<Long> startTime=Lists.newArrayList();startTime.add(Long.valueOf(vo.getStartTimes()[0]));
		List<Long> endTime=Lists.newArrayList();endTime.add(Long.valueOf(vo.getEndTimes()[0]));
		List<Long> templateIds=Lists.newArrayList();templateIds.add(Long.valueOf(vo.getTemplateIds()[0]));
		spaceTemplateReq.setTemplateIds(templateIds);
		spaceTemplateReq.setStartTimes(startTime);
		spaceTemplateReq.setEndTimes(endTime);
		return spaceTemplateReq;
	}

    @PermissionAnnotation(value = "SPACE")
    @ApiOperation(value = "获取schedled详情", notes = "获取schedled详情")
    @RequestMapping(value = "/space/detail", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ScheduledDetailVo> spaceTemplateDetail(Long id) {
    	try {
    		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    		return CommonResponse.success(templateService.addSpaceTemplate(id,user.getTenantId(),user.getLocationId(),null));
    	} catch (BusinessException e) {
    		e.printStackTrace();
    		throw e;
    	}
    }

    @ApiOperation(value = "获取location_scene_relation的列表集合", notes = "获取location_scene_relation的列表集合")
    @ResponseBody
    @RequestMapping(value = "/findLocationSceneRelationList", method = RequestMethod.POST)
    public CommonResponse<List<LocationSceneRelationResp>> findLocationSceneRelationList(HttpServletRequest request, Long locationSceneId) throws ParseException {
        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        List<LocationSceneRelationResp> listStr = sceneService.findLocationSceneRelationList(user.getTenantId(),user.getOrgId());
        List<LocationSceneRelationResp> list = Lists.newArrayList();
        LocationSceneRelationResp str = new LocationSceneRelationResp();
        LocationSceneRelationResp str2 = new LocationSceneRelationResp();
        boolean flag = false;
        for (LocationSceneRelationResp t:listStr) {
            t.setStartCron(zhuanhua(t.getStartCron()));
            t.setEndCron(zhuanhua(t.getEndCron()));
            flag = belongCalendar(t.getStartCron(),t.getEndCron());
            if(flag){//当前有执行情景
                str = t;
                list.add(str);
                //listStr.remove(t);
            }
        }
        if(list.size() ==0){
            str = null;
            list.add(str);
        }
        for(LocationSceneRelationResp t2:listStr){
            if(str!=null){//当前有执行情景
                flag = nextBelongCalendar(str.getEndCron(),t2.getStartCron(),null);
                if (flag){//有下一个情景
                    str2 = t2;
                    list.add(str2);
                }
            }else {//没有当前情景
                flag = nextBelongCalendar(null,t2.getStartCron(),new Date());
                if(flag){
                    str2 = t2;
                    list.add(str2);
                }
            }
        }
        if(list.size() ==0){
            str = null;
            list.add(str);
        }
        CommonResponse<List<LocationSceneRelationResp>> result = new CommonResponse<>(ResultMsg.SUCCESS,list);
        return result;
    }

    //获取当前时间的整校情景
    public static boolean belongCalendar(String begin, String end) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");//设置日期格式
        Date now =null;
        Date beginTime = null;
        Date endTime = null;
        now = df.parse(df.format(new Date()));
        boolean flag = false;
        if(begin.contains("*") || end.contains("*")){
            return false;
        }else {
            beginTime = df.parse(begin);
            endTime = df.parse(end);
            flag =  now.getTime() >= beginTime.getTime() && now.getTime() <= endTime.getTime();
            //System.out.println("flag:"+flag);
        }
        return flag;
    }
    //获取当前时间下一个的整校情景
    public static boolean nextBelongCalendar(String lastEnd, String nextStart,Date date) throws ParseException {
        Date now =null;
        Date lastEndTime = null;
        Date nextStartTime = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");//设置日期格式
        now = df.parse(df.format(new Date()));
        if(date ==null){
            if(lastEnd.contains("*") || nextStart.contains("*")){
                return false;
            }else {
                lastEndTime = df.parse(lastEnd);
                nextStartTime = df.parse(nextStart);
                boolean flag =  lastEndTime.getTime() <= nextStartTime.getTime();
                //System.out.println("flag:"+flag);
                return flag;
            }
        }else {
            if(nextStart.contains("*")){
                return false;
            }else {
                nextStartTime = df.parse(nextStart);
                boolean flag =  now.getTime() <=  nextStartTime.getTime();
                //System.out.println("flag:"+flag);
                return flag;
            }
        }
    }


    public static String  zhuanhua(String a) {
        String b = a.split("-")[0].replace("?","");
        String[]  c = b.split("\\s+");
        String d = c[5]+"-"+c[4]+"-"+c[3] +" "+c[2]+":"+c[1]+":"+c[0];
        //System.out.println(d);
        return d;
    }

    @PermissionAnnotation(value = "SPACE")
    @SystemLogAnnotation(value = "修改空间模板关系")
    @ApiOperation(value = "修改空间模板关系", notes = "修改空间模板关系")
    @RequestMapping(value = "/space/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Map<String, Object>> spaceTemplateUpdate(@RequestBody ScheduleVo vo) {
        try {
            SpaceTemplateReq spaceTemplateReq = commonSpaceTemplateReq(vo);
            templateService.updateSpaceTemplate(spaceTemplateReq);
            return CommonResponse.success();
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @ApiOperation(value = "查询执行日志和任务计划", notes = "查询执行日志和任务计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spaceId", value = "空间ID", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "dateType", value = "时间类型", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "year", value = "年份", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "dateData", value = "月份或者星期", required = true, paramType = "query", dataType = "Integer") })
    @RequestMapping(value = "/log/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse<List<ScheduledVo>> logList(@RequestParam(value = "spaceId", required = true) Long spaceId,
                                                       @RequestParam(value = "startTime", required = true) Long startTime,
                                                       @RequestParam(value = "timeDiff", required = true) Long timeDiff,
                                                       Long endTime,String week) {
        try {
        	Long monthTime=1000*3600*24*31L;
        	if(endTime==null) {
        		endTime=startTime+monthTime;
        	}
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            Long tenantId = user.getTenantId();
            Long locationId = user.getLocationId();
            return CommonResponse.success(templateService.findLogList(locationId,spaceId, startTime, endTime, tenantId,week,timeDiff));
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PermissionAnnotation(value = "SPACE")
    @SystemLogAnnotation(value = "删除空间模板关系")
    @ApiOperation(value = "删除空间模板关系", notes = "删除空间模板关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "空间模板关系ID", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "templateType", value = "模板类型", required = true, paramType = "query", dataType = "String") })
    @RequestMapping(value = "/space/delete", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse<Integer> spaceTemplateDelete(@RequestParam(value = "id", required = true) Long id,
                                                       @RequestParam(value = "templateType", required = true) String templateType) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            Integer rec = templateService.deleteSpaceTemplate(id, templateType);
            return CommonResponse.success(rec);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @ApiOperation(value = "SCENE模板分页", notes = "SCENE模板分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "templateType", value = "类型", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页数目", required = true, paramType = "query", dataType = "Integer") })
    @RequestMapping(value = "/template/scenePage", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Page<TemplateResp>> findSceneTemplatePage(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "templateType", required = true) String templateType,
            @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            TemplateReq templateReq = new TemplateReq();
            templateReq.setName(name == null ? "" : name);
            templateReq.setTemplateType(Constants.SCHEDULE_SCENE);
            templateReq.setPageNum(pageNum);
            templateReq.setPageSize(pageSize);
            templateReq.setTenantId(user.getTenantId());
            templateReq.setLocationId(user.getLocationId());
            Page<TemplateResp> page = templateTobApi.findTemplateList(templateReq);
            return CommonResponse.success(page);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @ApiOperation(value = "SCENE模板", notes = "SCENE模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "templateId", value = "模板ID", required = true, paramType = "query", dataType = "String") })
    @RequestMapping(value = "/template/getSceneTemplate", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<SceneTemplateResp> findSceneTemplate(
            @RequestParam(value = "templateId", required = true) Long templateId) {
        try {
            SceneTemplateResp sceneTemplateResp = templateService.getSceneTemplate(templateId);
            return CommonResponse.success(sceneTemplateResp);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PermissionAnnotation(value = "SCENE,SCENE_TEMPLATE")
    @SystemLogAnnotation(value = "保存SCENE模板")
    @ApiOperation(value = "保存SCENE模板", notes = "保存SCENE模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "模板名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceTarValue", value = "产品目标值", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deployId", value = "部署ID", required = true, paramType = "query", dataType = "Long")})
    @RequestMapping(value = "/template/saveSceneTemplate", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> saveSceneTemplate(@RequestParam(value = "name", required = true) String name,
                                                       @RequestParam(value = "deviceTarValue", required = true) String deviceTarValue,
                                                       @RequestParam(value = "deployId", required = true) Long deployId,
                                                       @RequestParam(value = "silenceStatus", required = true) Integer silenceStatus,
                                                       @RequestParam(value = "shortcut", required = true) Integer shortcut,
    												   Long id) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            TemplateReq templateReq = new TemplateReq();
            templateReq.setLocationId(user.getLocationId());
            templateReq.setTenantId(user.getTenantId());
            templateReq.setTemplateType("scene");
            templateReq.setDeployId(deployId);
            templateReq.setName(name);
            templateReq.setShortcut(shortcut);
            templateReq.setSilenceStatus(silenceStatus);
            templateReq.setId(id);
            List<TemplateDetailReq> templateDetailReqs = JSON.parseArray(deviceTarValue, TemplateDetailReq.class);
            for (TemplateDetailReq templateDetailReq : templateDetailReqs) {
                templateDetailReq.setName(name);
                templateDetailReq.setTenantId(user.getTenantId());
                if (templateDetailReq.getTemplateId() != null) {
                    templateReq.setId(templateDetailReq.getTemplateId());
                }
            }

            BuildTemplateReq buildTemplateReq = new BuildTemplateReq();
            buildTemplateReq.setTenantId(user.getTenantId());
            buildTemplateReq.setCreateBy(user.getUserId());
            buildTemplateReq.setUpdateBy(user.getUserId());
            buildTemplateReq.setTemplateReq(templateReq);
            buildTemplateReq.setTemplateDetailList(templateDetailReqs);
            templateService.sceneTemplateBuild(buildTemplateReq);
            return CommonResponse.success(ResultMsg.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionEnum.SCENE_TEMPLATE_NAME_IS_EXIST);
        }
    }

    @PermissionAnnotation(value = "SCENE,SCENE_TEMPLATE")
    @SystemLogAnnotation(value = "删除SCENE模板")
    @ApiOperation(value = "删除SCENE模板", notes = "删除SCENE模板")
    @ApiImplicitParams({ @ApiImplicitParam(name = "templateId", value = "模板ID", required = true, dataType = "Long") })
    @RequestMapping(value = "/template/sceneTemplateDelete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> sceneTemplateDelete(
            @RequestParam(value = "templateId", required = true) Long templateId) {
        try {
            templateService.sceneTemplateDelete(templateId);
            return CommonResponse.success(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PermissionAnnotation(value = "SCENE,SCENE_TEMPLATE")
    @SystemLogAnnotation(value = "删除SCENE模板")
    @ApiOperation(value = "批量删除SCENE模板", notes = "批量删除SCENE模板")
    @ApiImplicitParams({ @ApiImplicitParam(name = "templateIds", value = "模板ID逗号相隔集合", required = true, dataType = "Long") })
    @RequestMapping(value = "/template/sceneTemplateDeleteBatch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> sceneTemplateDeleteBatch(
    		@RequestParam(value = "templateIds", required = true) String templateIds) {
    	try {
    		String[] templateIdArry=templateIds.split(",");
    		for(String templateIdStr:templateIdArry) {
    			Long templateId=Long.valueOf(templateIdStr);
    			templateService.sceneTemplateDelete(templateId);
    		}
    		return CommonResponse.success(ResultMsg.SUCCESS);
    	} catch (BusinessException e) {
    		e.printStackTrace();
    		throw e;
    	}
    }

    @ApiOperation(value = "SCENE模板列表", notes = "SCENE模板列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "模板ID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "spaceId", value = "空间ID", required = true, paramType = "query", dataType = "Long")})
    @RequestMapping(value = "/template/findSceneSpaceTemplateList", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<TemplateResp> findSceneSpaceTemplateList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "spaceId", required = true) Long spaceId) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            TemplateReq templateReq = new TemplateReq();
            templateReq.setSpaceId(spaceId);
            templateReq.setTenantId(user.getTenantId());
            templateReq.setLocationId(user.getLocationId());
            templateReq.setTemplateType(Constants.SCENE_TEMPLATE);
            if (StringUtils.isNotBlank(name)) {
                templateReq.setName(name);
            }
            TemplateResp templateResp = templateService.findSceneSpaceTemplateList(templateReq);
            return CommonResponse.success(templateResp);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PermissionAnnotation(value = "SCENE,SCENE_TEMPLATE,SPACE")
    @SystemLogAnnotation(value = "保存空间情景模板关系")
    @ApiOperation(value = "保存空间情景模板关系", notes = "保存空间情景模板关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spaceId", value = "空间ID", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "templateIds", value = "模板ID", required = true, paramType = "query", dataType = "String") })
    @RequestMapping(value = "/space/saveTemplateMount", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> saveTemplateMount(@RequestParam(value = "spaceId", required = true) Long spaceId,
                                                       @RequestParam(value = "templateIds", required = true) String templateIds) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            SpaceTemplateReq spaceTemplateReq = new SpaceTemplateReq();
            spaceTemplateReq.setTemplateType(Constants.SCENE_TEMPLATE);
            spaceTemplateReq.setTenantId(user.getTenantId());
            spaceTemplateReq.setLocationId(user.getLocationId());
            spaceTemplateReq.setCreateBy(user.getUserId());
            spaceTemplateReq.setCreateTime(new Date());
            spaceTemplateReq.setUpdateTime(new Date());
            spaceTemplateReq.setUpdateBy(user.getUserId());
            spaceTemplateReq.setSpaceId(spaceId);
            String[] str = templateIds.split(",");
            for (String templateId : str) {
                spaceTemplateReq.setTemplateId(Long.valueOf(templateId));
                templateService.saveTemplateMount(spaceTemplateReq);
            }
            return CommonResponse.success(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PermissionAnnotation(value = "SCENE,SCENE_TEMPLATE,SPACE")
    @SystemLogAnnotation(value = "空间模板列表数据删除")
    @ApiOperation(value = "空间模板列表数据删除", notes = "空间模板列表数据删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spaceId", value = "空间ID", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "templateIds", value = "模板ID", required = true, paramType = "query", dataType = "String") })
    @RequestMapping(value = "/space/deleteTemplateMount", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> deleteTemplateMount(@RequestParam(value = "spaceId", required = true) Long spaceId,
                                                         @RequestParam(value = "templateIds", required = true) String templateIds) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            SpaceTemplateReq spaceTemplateReq = new SpaceTemplateReq();
            spaceTemplateReq.setTemplateType(Constants.SCENE_TEMPLATE);
            spaceTemplateReq.setTenantId(user.getTenantId());
            spaceTemplateReq.setLocationId(user.getLocationId());
            spaceTemplateReq.setSpaceId(spaceId);
            String[] str = templateIds.split(",");
            for (String templateId : str) {
                spaceTemplateReq.setTemplateId(Long.valueOf(templateId));
                templateService.deleteTemplateMount(spaceTemplateReq);
            }
            return CommonResponse.success(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //@SystemLogAnnotation(value = "手动执行情景版本的初始化")
    @ApiOperation(value = "手动执行情景版本的初始化")
    @RequestMapping(value = "/scene/templateInit", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> deleteTemplateMount(@RequestBody SpaceListVO vo) throws InterruptedException {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            List<Long> spaceIds = Arrays.asList(vo.getSpaceIds());
            templateService.sceneTemplateInit(spaceIds,user,vo.getType());
            return CommonResponse.success(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @ApiOperation(value = "查询业务类型列表", notes = "查询业务类型列表〃")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businessType", value = "业务类型列表", required = false, paramType = "query", dataType = "String") })
    @RequestMapping(value = "/businessType/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse<List<DeviceBusinessTypeResp>> businessTypeList(
            @RequestParam(value = "businessType", required = false) String businessType) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            List<DeviceBusinessTypeResp> deviceBusinessTypeList = templateService.findBusinessTypeList(user);
            return CommonResponse.success(deviceBusinessTypeList);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
    }

	@ApiOperation(value = "查询对应类型SPACE的模版情景", notes = "查询对应类型SPACE的模版情景")
	@ApiImplicitParams({@ApiImplicitParam(name = "spaceId", value = "空间ID", required = true, dataType = "Long"),
	                    @ApiImplicitParam(name = "tpye", value = "space属性", required = true, dataType = "String"),
						@ApiImplicitParam(name = "tenantId", value = "租户ID", required = true, dataType = "Long"),
						@ApiImplicitParam(name = "name", value = "楼层名称", required = true, dataType = "String")})
		@RequestMapping(value = "/queryListBySpace")
	@ResponseBody
	public CommonResponse<List<Map<String, Object>>> getListBySpace(@RequestParam(required = true) Long spaceId,
																	@RequestParam(required = true) String type) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		return CommonResponse.success(templateService.getListBySpace(user.getTenantId(),null,user.getLocationId(), spaceId, type, null));
	}

}
