package com.iot.center.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.iot.center.annotation.PermissionAnnotation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.vo.BusinessTypeStatistic;
import com.iot.building.index.vo.IndexDetailReq;
import com.iot.building.index.vo.IndexDetailResp;
import com.iot.building.index.vo.IndexReq;
import com.iot.building.index.vo.IndexResp;
import com.iot.building.template.api.SpaceTemplateApi;
import com.iot.center.annotation.SystemLogAnnotation;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.service.SpaceService;
import com.iot.center.vo.IndexDetailVo;
import com.iot.center.vo.IndexStr;
import com.iot.center.vo.IndexVo;
import com.iot.center.vo.IndexVoResp;
import com.iot.center.vo.InfoList;
import com.iot.common.beans.CommonResponse;
import com.iot.common.enums.APIType;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.api.DeviceStatusCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.user.vo.LoginResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "首页接口")
@Controller
@RequestMapping("/index")
public class IndexController {
	
	@Autowired
	private DeviceBusinessTypeApi deviceBusinessTypeApi;
	@Autowired
	private SpaceService spaceService;
	@Autowired
	private SpaceTemplateApi spaceTemplateApi;
	@Autowired
	private CentralControlDeviceApi centralControlDeviceApi;
	@Autowired
	private DeviceCoreApi deviceApi;
	@Autowired
	private DeviceStatusCoreApi deviceStatusCoreApi;
	
	@ApiOperation(value = "首页数据")
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public CommonResponse<List<IndexVo>> homeIndex() {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		List<IndexVo> indexList=null;
		if(user !=null) {
			Long locationId=user.getLocationId();
			Long orgId=user.getOrgId();
			indexList=spaceService.getIndexInfo(user.getTenantId(),orgId,locationId);
		}
		return CommonResponse.success(indexList);
	}

    @PermissionAnnotation(value = "HOME_PAGE")
    @SystemLogAnnotation(value = "执行启动")
    @ApiOperation(value = "执行启动")
    @ResponseBody
    @RequestMapping(value = "/executeEnable", method = RequestMethod.POST)
    public CommonResponse<ResultMsg> executeEnable(HttpServletRequest request,@RequestParam(value = "id") Long id,String start) {
        IndexReq indexReq = new IndexReq();
        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        if(start.equals("false")){//停用
            //都置为0
            spaceService.setAllEnableOff(user,1);
            return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
        }else {
        indexReq.setId(id);
        indexReq.setEnable(1);//设置启动状态为1 开启
        //启动摸个自定义首页时，需先把所有的数据的enable 置为0，再设置相应的自定义首页的enable为1
        //先置为0
        spaceService.setAllEnableOff(user,1);
        //再设置相应的自定义首页的enable为1
        spaceService.saveOrUpdateIndexContent(indexReq);
        return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
        }
    }


    @ApiOperation(value = "登录获取已经启动的自定义首页")
    @ResponseBody
    @RequestMapping(value = "/getEnableIndex", method = RequestMethod.POST)
    public CommonResponse<IndexVoResp> getEnableIndex(HttpServletRequest request) {
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        IndexResp indexResp = spaceService.getEnableIndex(user);
        //查询index_detail  通过 indexContentI
        if(indexResp == null){
            IndexVoResp indexVoResp = null;
            return CommonResponse.success(indexVoResp);
        }else {
            List<IndexDetailResp> list = spaceService.findIndexDetailByIndexId(user.getTenantId(),user.getOrgId(),indexResp.getId());
            IndexVoResp indexVoResp = new IndexVoResp();
            indexVoResp.setDataList(list);
            indexVoResp.setTitle(indexResp.getTitle());
            indexVoResp.setType(indexResp.getType());
            indexVoResp.setId(indexResp.getId());
            indexVoResp.setEnable(indexResp.getEnable());
            return CommonResponse.success(indexVoResp);
        }
    }


    @SystemLogAnnotation(value = "查询自定义首页列表")
    @ApiOperation(value = "查询自定义首页列表")
    @ResponseBody
    @RequestMapping(value = "/findCustomIndex", method = RequestMethod.POST)
    public CommonResponse<Page<IndexResp>> findCustomIndex(HttpServletRequest request,@RequestParam(value = "pageNumber") int pageNumber, @RequestParam(value = "pageSize")  int pageSize) {
	    pageSize =15;
        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        IndexReq indexReq = new IndexReq();
        indexReq.setTenantId(user.getTenantId());
        indexReq.setLocationId(user.getLocationId());
        indexReq.setPageSize(pageSize);
        indexReq.setPageNumber(pageNumber);
        Page<IndexResp> page = spaceService.findCustomIndex(indexReq);
        return CommonResponse.success(page);
    }


    @ApiOperation(value = "查询index_detail通过IndexId")
    @ResponseBody
    @RequestMapping(value = "/findIndexDetailByIndexId", method = RequestMethod.POST)
    public CommonResponse<IndexVoResp> findIndexDetailByIndexId(HttpServletRequest request,String indexContentId) {
        IndexReq indexReq = new IndexReq();
        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        Long indexContentIdStr = Long.valueOf(indexContentId);
        //查询index_content 通过id
        IndexResp indexResp = new IndexResp();
        indexResp = spaceService.findIndexContentById(user.getTenantId(),user.getOrgId(), indexContentIdStr);
        //查询index_detail  通过 indexContentI
        List<IndexDetailResp> list = spaceService.findIndexDetailByIndexId(user.getTenantId(),user.getOrgId(),indexContentIdStr);
        IndexVoResp indexVoResp = new IndexVoResp();
        indexVoResp.setDataList(list);
        indexVoResp.setTitle(indexResp.getTitle());
        indexVoResp.setType(indexResp.getType());
        indexVoResp.setId(indexResp.getId());
        return CommonResponse.success(indexVoResp);
    }

    @SystemLogAnnotation(value = "保存自定义首页")
	@ApiOperation(value = "保存自定义首页")
	@RequestMapping(value="/saveCustomIndex", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> saveLocationScene(HttpServletRequest request, String content) {
		try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            //List<IndexDetailVo> list = jsonToIndexDetailVo(content);
            //json字符串转化
            IndexStr indexStr = jsonStringToIndexStr(content);
            IndexReq indexReq = new IndexReq();
            int type = indexStr.getType();
            Long id = indexStr.getId();
            String title = indexStr.getTitle();
            List<InfoList> list = indexStr.getDataList();
            if(id !=null){
                indexReq.setId(id);
            }
            indexReq.setType(type);
            indexReq.setTitle(title);
            indexReq.setLocationId(user.getLocationId());
            indexReq.setTenantId(user.getTenantId());
            indexReq.setCreateBy(user.getUserId());
            indexReq.setCreateTime(new Date());
            //设置成不启动0
            indexReq.setEnable(0);
            Long indexContentId = spaceService.saveOrUpdateIndexContent(indexReq);
            if(type == 0){
                //删除index_detail
                if(id !=null){
                    spaceService.deleteIndexDatailByIndexId(user.getTenantId(),user.getOrgId(),id);
                }
            }else {
                //循环添加/修改到index_detail
                IndexDetailReq indexDetailReq = new IndexDetailReq();
                    if(id == null){
                        //添加
                        for (InfoList i : list){
                            indexDetailReq.setCreateBy(user.getUserId());
                            indexDetailReq.setCreateTime(new Date());
                            indexDetailReq.setUpdateBy(user.getUserId());
                            indexDetailReq.setUpdateTime(new Date());
                            indexDetailReq.setIndexContentId(indexContentId);
                            indexDetailReq.setModuleSort(i.getModuleSort());
                            indexDetailReq.setModuleName(i.getModuleName()+"");
                            indexDetailReq.setParameter(i.getParameter());
                            indexDetailReq.setFresh(i.getFresh());
                            spaceService.saveOrUpdateIndexDetail(indexDetailReq);
                        }
                    }else {
                        //修改，先删除，在添加
                        //删除
                        spaceService.deleteIndexDatailByIndexId(user.getTenantId(),user.getOrgId(),id);
                        //在添加
                        for (InfoList i : list){
                            indexDetailReq.setCreateBy(user.getUserId());
                            indexDetailReq.setCreateTime(new Date());
                            indexDetailReq.setUpdateBy(user.getUserId());
                            indexDetailReq.setUpdateTime(new Date());
                            indexDetailReq.setIndexContentId(indexContentId);
                            indexDetailReq.setModuleSort(i.getModuleSort());
                            indexDetailReq.setModuleName(i.getModuleName()+"");
                            indexDetailReq.setParameter(i.getParameter());
                            indexDetailReq.setFresh(i.getFresh());
                            spaceService.saveOrUpdateIndexDetail(indexDetailReq);
                        }
                    }
                }
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}

    @PermissionAnnotation(value = "HOME_PAGE")
    @SystemLogAnnotation(value = "删除自定义首页")
    @ApiOperation(value = "删除index_Content和index_detail")
    @RequestMapping(value="/deleteContentAndDetail", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> deleteContentAndDetail(HttpServletRequest request,Long id) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            IndexReq indexReq = new IndexReq();
            indexReq.setId(id);
            //删除index_content
            spaceService.deleteIndexContent(indexReq);
            //删除index_detail
            spaceService.deleteIndexDatailByIndexId(user.getTenantId(),user.getOrgId(),id);
            return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            throw e;
        }
    }


	public static List<IndexDetailVo> jsonToIndexDetailVo(String content) {
		String jsonstr="[{\"type\":0,\"dataList\":[{\"moduleSort\":1,\"moduleName\":\"电量统计\",\"parameter\":\"1111\",\"fresh\":1}]}]";
		List<IndexDetailVo> list = Lists.newArrayList();
		//json字符串转成集合
        List array1 = JSONArray.parseArray(content);
        Map<String,Object> map  = (Map<String, Object>) array1.get(0);
        int type = (int) map.get("type");
        String title = (String) map.get("title");
        JSONArray dataList = (JSONArray) map.get("dataList");
        if(dataList == null || dataList.size()==0){
            if(map.get("id") == null){
                IndexDetailVo indexDetailVo = new IndexDetailVo(type,title);
                list.add(indexDetailVo);
            }else {
                Long id = Long.valueOf(map.get("id") + "");
                IndexDetailVo indexDetailVo = new IndexDetailVo(id, type, title);
                list.add(indexDetailVo);
            }
        }else {
            for(int i=0;i<dataList.size();i++){
                IndexDetailVo indexDetailVo = null;
                JSONObject job = dataList.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                int moduleSort = (int) ((JSONObject)job).get("moduleSort");
                //Long indexDetailId = (Long)((JSONObject)job).get("id");
                int moduleName = ((JSONObject)job).get("moduleName")==null?666:Integer.valueOf(((JSONObject)job).get("moduleName")+"");
                String moduleNameStr = null;
                String freshStr = null;
                if(moduleName != 666){
                    moduleNameStr = moduleName + "";
                }
                String parameter = ((JSONObject)job).get("parameter")==null?"":(String) ((JSONObject)job).get("parameter");
                int fresh = ((JSONObject)job).get("fresh")==null?666:Integer.valueOf(((JSONObject)job).get("fresh")+"");
                if(fresh !=666){
                    freshStr = fresh+"";
                }
                if(map.get("id")==null){
                    indexDetailVo  = new IndexDetailVo(moduleSort,moduleNameStr,parameter,freshStr,type,title);
                }else {
                    Long id = Long.valueOf(map.get("id")+"");
                    indexDetailVo  = new IndexDetailVo(id,moduleSort,moduleNameStr,parameter,freshStr,type,title);
                }
                list.add(indexDetailVo);
            }
        }
        return list;
	}
	
    @ApiOperation(value = "加载设备用途统计")
    @RequestMapping(value="/businessTypeStatistic")
    @ResponseBody
    public CommonResponse<List<BusinessTypeStatistic>> businessTypeStatistic() {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            BusinessTypeStatistic statistic=new BusinessTypeStatistic();
            statistic.setTenantId(user.getTenantId());
            statistic.setOrgId(user.getOrgId());
            statistic.setLocationId(user.getLocationId());
            List<BusinessTypeStatistic> list=deviceBusinessTypeApi.findStatistic(statistic);
            List<BusinessTypeStatistic> havaList=Lists.newArrayList();
            if(CollectionUtils.isNotEmpty(list)) {
            	for(BusinessTypeStatistic stat:list) {
            		if(stat.getTotal().compareTo(0L)>0) {
            			havaList.add(stat);
            		}
            	}
            }
            //添加网关的统计
            havaList.add(countMultiProtocolGateway(user.getTenantId(), user.getLocationId()));
            return CommonResponse.success(havaList);
        } catch (BusinessException e) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_LOGIN);
        }
    }
    
    private BusinessTypeStatistic countMultiProtocolGateway(Long tenantId,Long locationId) {
    	List<GetDeviceInfoRespVo> voList= centralControlDeviceApi.findDirectDeviceByDeviceCatgory(APIType.MultiProtocolGateway.name(),
    			tenantId,locationId);
    	Long total=0L;Long online=0L;
    	BusinessTypeStatistic statistic=new BusinessTypeStatistic();
    	if(CollectionUtils.isNotEmpty(voList)) {
    		for(GetDeviceInfoRespVo resp:voList) {
    			total++;
    			GetDeviceInfoRespVo vo = deviceApi.get(resp.getUuid());
    			GetDeviceStatusInfoRespVo onlineStatus=deviceStatusCoreApi.get(vo.getTenantId(), resp.getUuid());
    			if(onlineStatus !=null && StringUtils.isNotBlank(onlineStatus.getOnlineStatus()) 
    					&& onlineStatus.getOnlineStatus().equals(com.iot.center.helper.Constants.ONLINE)) {
    				online++;
    			}
    		}
    	}
    	statistic.setTotal(total);
    	statistic.setOnline(online);
    	statistic.setName("MultiProtocolGateway");
    	return statistic;
    }

    public static IndexStr jsonStringToIndexStr(String content){
	    String test = "[{\"title\":\"huangxu\",\"type\":1,\"dataList\":[{\"createBy\":\"55\",\"createTime\":1540544143000,\"fresh\":\"0\",\"id\":\"211\",\"indexContentId\":\"1055744630264131585\",\"moduleName\":\"0\",\"moduleSort\":1,\"parameter\":\"33\",\"updateBy\":\"55\",\"updateTime\":1540544143000},{\"createBy\":\"55\",\"createTime\":1540544143000,\"fresh\":2,\"id\":\"212\",\"indexContentId\":\"1055744630264131585\",\"moduleName\":2,\"moduleSort\":2,\"parameter\":\"22\",\"updateBy\":\"55\",\"updateTime\":1540544143000},{\"createBy\":\"55\",\"createTime\":1540544143000,\"fresh\":\"\",\"id\":\"213\",\"indexContentId\":\"1055744630264131585\",\"moduleName\":\"\",\"moduleSort\":3,\"parameter\":\"\",\"updateBy\":\"55\",\"updateTime\":1540544143000},{\"createBy\":\"55\",\"createTime\":1540544143000,\"fresh\":\"\",\"id\":\"214\",\"indexContentId\":\"1055744630264131585\",\"moduleName\":\"\",\"moduleSort\":4,\"parameter\":\"\",\"updateBy\":\"55\",\"updateTime\":1540544143000}],\"id\":\"1055744630264131585\"}]";
	    String realContent = content.substring(1,content.length()-1);
        System.out.println("realContent的值："+realContent);
        IndexStr indexStr = JSON.parseObject(realContent,IndexStr.class);
        //IndexStr indexStr2 = ((List<IndexStr>)JsonUtil.fromJsonArray(content,IndexStr.class)).get(0);
        return indexStr;
    }
    
    @ApiOperation(value = "首页前后的schedule")
    @ResponseBody
    @RequestMapping(value = "/beforeAndAfterSchedule")
    public CommonResponse<Map<String,Object>> beforeAndAfterSchedule(long currentTime,long endTime,String week,Long spaceId) {
    	 LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
         return CommonResponse.success(spaceTemplateApi.beforeAndAfterSchedule(user.getLocationId(),currentTime,endTime, week, spaceId));
    }

}
