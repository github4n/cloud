package com.iot.building.allocation.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.iot.airswitch.api.AirSwitchApi;
import com.iot.building.allocation.entity.Allocation;
import com.iot.building.allocation.entity.ExecuteLog;
import com.iot.building.allocation.enums.AllocationEnum;
import com.iot.building.allocation.job.AllocationOTAJob;
import com.iot.building.allocation.job.DevicePropertyInitJob;
import com.iot.building.allocation.mapper.AllocationMapper;
import com.iot.building.allocation.mapper.AllocationNameMapper;
import com.iot.building.allocation.mapper.ExecuteLogMapper;
import com.iot.building.allocation.service.IAllocationService;
import com.iot.building.allocation.vo.AllocationNameReq;
import com.iot.building.allocation.vo.AllocationNameResp;
import com.iot.building.allocation.vo.AllocationReq;
import com.iot.building.allocation.vo.AllocationResp;
import com.iot.building.allocation.vo.ExecuteLogReq;
import com.iot.building.group.service.IGroupService;
import com.iot.building.helper.Constants;
import com.iot.building.helper.ThreadPoolUtil;
import com.iot.building.ifttt.entity.Rule;
import com.iot.building.ifttt.mapper.RuleTobMapper;
import com.iot.building.ifttt.service.IAutoTobService;
import com.iot.building.ifttt.service.IftttService;
import com.iot.building.ifttt.service.RuleService;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.scene.service.SceneService;
import com.iot.building.scene.vo.req.SceneTemplateManualReq;
import com.iot.common.beans.BeanUtil;
import com.iot.common.enums.APIType;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;

/**
 * @Author: Xieby
 * @Date: 2018/8/31
 * @Description: *
 */
@Service
public class AllocationServiceImpl implements IAllocationService {

    private Logger log = LoggerFactory.getLogger(IAllocationService.class);

    @Autowired
    private SpaceApi spaceApi;
    @Autowired
    private SpaceDeviceApi spaceDeviceApi;
    @Autowired
    private DeviceTypeCoreApi deviceTypeApi;
    @Autowired
    private CentralControlDeviceApi centralControlDeviceApi;
    @Autowired
    private ExecuteLogMapper logMapper;
    @Autowired
    private AllocationMapper allocationMapper;
    @Autowired
    private AllocationNameMapper allocationNameMapper;
    @Autowired
    private IAutoTobService autoTobService;
    @Autowired
    private IftttService iftttService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private AirSwitchApi airSwitchApi;
    @Autowired
    private IGroupService groupService;
    @Autowired
    private RuleTobMapper ruleTobMapper;
    @Autowired
    private SceneService sceneService;

    @Override
    public Page<AllocationNameResp> getPageList(AllocationNameReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<AllocationNameResp> list = allocationNameMapper.getList(req);
        PageInfo<AllocationNameResp> info = new PageInfo(list);
        Page<AllocationNameResp> page = new Page<AllocationNameResp>();
        BeanUtil.copyProperties(info, page);
        page.setResult(info.getList());
        return page;
    }

    @Override
    public Page<AllocationResp> getInfoList(AllocationReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<AllocationResp> list = allocationMapper.getList(req);
//        for (AllocationResp allocation : list) {
//            List<ExecuteLog> logs = queryLogList(req.getTenantId(),req.getLocationId(), allocation.getId());
//            if (CollectionUtils.isEmpty(logs)) {
//                allocation.setExeStatus(Constants.EXECUTE_STATUS_NO_START);
//                allocation.setExeResult(Constants.EXECUTE_RESULT_SUCCESS);
//            } else {
//                ExecuteLog lastLog = logs.get(0);
//                allocation.setExeStatus(Constants.EXECUTE_STATUS_DOING);
//                allocation.setExeResult(lastLog.getExeResult());
//            }
//        }
        PageInfo<AllocationResp> info = new PageInfo<>(list);
        Page<AllocationResp> page = new Page<>();
        BeanUtil.copyProperties(info, page);
        page.setResult(info.getList());
        return page;
    }

    @Override
    public Long saveAllocation(AllocationReq req) {
        Allocation info = new Allocation();
        BeanUtil.copyProperties(req, info);
        allocationMapper.insert(info);
        return info.getId();
    }

    @Override
    public void editAllocation(AllocationReq req) {
        // update info
        Allocation detail = allocationMapper.selectByPrimaryKey(req.getId());
        detail.setAllocationId(req.getAllocationId());
        if(req.getIsLoop() !=null) {
        	detail.setIsLoop(req.getIsLoop());
        }
        if(StringUtils.isNotBlank(req.getSelectWeek())) {
        	detail.setSelectWeek(req.getSelectWeek());
        }
        if(StringUtils.isNotBlank(req.getSpaceIds())) {
        	detail.setSpaceIds(req.getSpaceIds());
        }
        if(StringUtils.isNotBlank(req.getSpaceName())) {
        	detail.setSpaceName(req.getSpaceName());
        }
        if(StringUtils.isNotBlank(req.getParamInfo())) {
        	detail.setParamInfo(req.getParamInfo());
        }
        if(req.getDeviceInterval() !=null) {
        	detail.setDeviceInterval(req.getDeviceInterval());
        }
        if(req.getUpdateBy() !=null) {
        	detail.setUpdateBy(req.getUpdateBy());
        }
        detail.setUpdateTime(new java.util.Date());
        if(req.getExeResult() !=null) {
        	detail.setExeResult(req.getExeResult());
        }
        if(req.getExeStatus() !=null) {
        	detail.setExeStatus(req.getExeStatus());
        }
        if(req.getConcurrent() !=null) {
        	detail.setConcurrent(req.getConcurrent());
        }
        if(StringUtils.isNotBlank(req.getExeTime())) {
        	detail.setExeTime(req.getExeTime());
        }
        allocationMapper.updateByPrimaryKey(detail);
    }

    @Override
    public void deleteAllocation(Long tenantId,Long orgId, Long id) {
        allocationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public AllocationResp selectById(Long tenantId,Long orgId, Long id) {
        Allocation allocation = allocationMapper.selectByPrimaryKey(id);
        AllocationResp resp = new AllocationResp();
        BeanUtil.copyProperties(allocation, resp);
        return resp;
    }

    @Override
    public void saveExecuteLog(ExecuteLogReq req) {
        ExecuteLog executeLog = new ExecuteLog();
        log.info("req info = " + JSON.toJSONString(req));
        BeanUtil.copyProperties(req, executeLog);
        log.info("executeLog info = " + JSON.toJSONString(executeLog));
        logMapper.insert(executeLog);
    }

    @Override
    public List<ExecuteLog> queryLogList(Long tenantId,Long locationId, Long functionId) {
        return logMapper.queryByFunctionId(tenantId,locationId,functionId);
    }

    @Override
    public ExecuteLogReq queryErrorLog(Long tenantId,Long orgId,Long locationId, Long id) {
        List<ExecuteLog> logs = queryLogList(tenantId,locationId, id);
        if (CollectionUtils.isEmpty(logs)) {
            return null;
        }
        ExecuteLogReq req = new ExecuteLogReq();
        BeanUtil.copyProperties(logs.get(0), req);
        return req;
    }

    @Override
    public void executeIssue(AllocationReq data) throws Exception {
    	new Thread(()->{
    		try {
    			editAllocation(data);
        		Long allocationId = data.getAllocationId();
        		if (allocationId == null) {
        			return;
        		}
        		// sence初始化
        		if (allocationId.intValue() == AllocationEnum.SCENE.getValue()) {
        			log.info("sence初始化---------------------------------");
        			senceInit(data);
        			// OTA下发
        		}else if (allocationId.intValue() == AllocationEnum.OTA.getValue()) {
        			log.info("OTA下发---------------------------------");
        			otaUpdate(data);
        			// 同步(全部楼层)
        		}else if (allocationId.intValue() == AllocationEnum.SYNC.getValue()) {
        			log.info("同步(全部楼层)---------------------------------");
        			initDeviceProperty(data);
        			//IFTTT模板下发
        		}else if (allocationId.intValue() == AllocationEnum.IFTTT.getValue()) {
        			log.info("IFTTT模板下发---------------------------------");
        			iftttTemplateInit(data);
        			//空开事件时间参数
        		}else if (allocationId.intValue() == AllocationEnum.AIR_SWITCH_EVENT.getValue()) {
        			log.info("空开事件时间参数---------------------------------");
        			airSwitchRTVI(data);
        			//空开自动检测参数
        		}else if (allocationId.intValue() == AllocationEnum.AIR_SWITCH_REPORT.getValue()) {
        			log.info("空开自动检测参数---------------------------------");
        			airSwitchCheck(data); 
        			//分组下发
        		}else if (allocationId.intValue() == AllocationEnum.GOURP.getValue()) {
        			log.info("分组下发---------------------------------");
        			groupLoad(data); 
        		}
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    	}).start();
        
    }
    
    private void groupLoad(AllocationReq data) {
    	List<Long> spaceIds=getSpaceIds(data.getSpaceIds());
    	if(CollectionUtils.isNotEmpty(spaceIds)) {
    		for(Long spaceId:spaceIds) {
    			SpaceReq spaceReq=new SpaceReq();
    			spaceReq.setTenantId(data.getTenantId());
    			spaceReq.setLocationId(data.getLocationId());
    			spaceReq.setId(spaceId);
    			ThreadPoolUtil.instance().execute(new Runnable() {
    				@Override
    				public void run() {
    					groupService.initGroupBySpaceId(spaceReq);
    				}
    			});
    		}
    	}
    }
    
    private List<Long> getSpaceIds(String spaceIds){
    	List<Long> spaceList=Lists.newArrayList();
    	String[] spaceIdArry=spaceIds.split(",");
    	for(String idStr:spaceIdArry) {
    		spaceList.add(Long.valueOf(idStr));
    	}
    	return spaceList;
    }
    
    private void airSwitchRTVI(AllocationReq data) {
   	 List<String> deviecIds=airSwitchDeviceIds(data);
   	 if(CollectionUtils.isNotEmpty(deviecIds)) {
   		 deviecIds.forEach(deviceId->{
   			 airSwitchApi.setRTVI(deviceId,data.getDeviceInterval(),data.getTenantId());
   		 });
   	 }
   }
    /**
     * 空开自建
     * @param data
     */
    private void airSwitchCheck(AllocationReq data) {
    	 List<String> deviecIds=airSwitchDeviceIds(data);
    	 if(CollectionUtils.isNotEmpty(deviecIds)) {
    		 deviecIds.forEach(deviceId->{
    			 airSwitchApi.selfCheck(deviceId, data.getTenantId());
    		 });
    	 }
    }
    
    private List<String> airSwitchDeviceIds(AllocationReq data) {
    	 List<Long> spaceIds = getSpaceIds(data.getSpaceIds());
    	 List<String> deviecIds=Lists.newArrayList();
    	 SpaceAndSpaceDeviceVo req=new SpaceAndSpaceDeviceVo();
    	 req.setSpaceIds(spaceIds);
    	 req.setTenantId(data.getTenantId());
    	 List<SpaceDeviceResp> respList=spaceDeviceApi.findSpaceDeviceBySpaceIdsOrDeviceIds(req);
    	 if(CollectionUtils.isNotEmpty(respList)) {
    		 for(SpaceDeviceResp resp:respList) {
    			 if(resp.getDeviceTypeId()==null) {
    				 continue;
    			 }
    			 GetDeviceTypeInfoRespVo vo = deviceTypeApi.get(resp.getDeviceTypeId());
    			 if(vo.getVenderFlag().equals(APIType.ManTunSci.name())) {
    				 deviecIds.add(resp.getDeviceId());
    			 }
    		 }
    	 }
    	 return deviecIds;
    }
    
    private void initDeviceProperty(AllocationReq req) throws Exception{
    	DevicePropertyInitJob job=new DevicePropertyInitJob(req.getTenantId(),req.getLocationId());
    	job.start();
    }

    /**
     * ifttt模板执行
     * @param req
     */
    private void iftttTemplateInit(AllocationReq req) throws Exception{//模板集合
    	if(StringUtils.isNotBlank(req.getSpaceIds())) {
    		String[] idsArry=req.getSpaceIds().split(",");
    		for(String idsStr:idsArry) {
    			Long id=Long.valueOf(idsStr);
    			SpaceResp space=spaceApi.findSpaceInfoBySpaceId(req.getTenantId(), id);
    			Long deployId=space.getDeployId();
    			configurationDeliveryIfttt(req,deployId);
    	    	Thread.sleep(1000*req.getDeviceInterval());
    		}
    	}
    }
    
    
    private void configurationDeliveryIfttt(AllocationReq req,Long deployId) throws Exception{
		List<Long> spaceIds=getSpaceIds(req.getSpaceIds());
    	if(CollectionUtils.isNotEmpty(spaceIds)) {
    		spaceIds.forEach(spaceId->{//循环房间产创建所有模板类型的ifttt
    			iftttTemplateToIftttBySpace(req.getTenantId(), req.getOrgId(),deployId,spaceId);
    		});
    	}
    	
    }

	public void iftttTemplateToIftttBySpace(Long tenantId,Long orgId, Long deployId,Long spaceId) {
		RuleListReq ruleReq = new RuleListReq();
		ruleReq.setTenantId(tenantId);
		ruleReq.setTemplateFlag(Byte.valueOf("1"));
		ruleReq.setShowTime(false);
		ruleReq.setDeployMentId(deployId);
		//ruleReq.setSpaceId(spaceId);
		List<RuleResp> templateList=ruleService.listNoPage(ruleReq);
		if(CollectionUtils.isNotEmpty(templateList)) {
			templateList.forEach(resp->{
				//1.先删除模板生成的情景
				deleteIftttByTemplate(tenantId, spaceId, resp);
				//2.添加模板生成情景
				saveIftttByTemplate(spaceId, resp);
			});
		}
	}

	//2.添加模板生成情景
	private void saveIftttByTemplate(Long spaceId, RuleResp resp) {
		RuleResp respBack=iftttService.get(resp.getId());
		log.info("*********************start*****************************");
		SaveIftttReq iftttReq = ruleRespChangeToIftttReq(respBack,spaceId);
		autoTobService.saveLowerHair(iftttReq);
		log.info("*********************end*****************************");
	}

	//删除模板生成的情景
	private void deleteIftttByTemplate(Long tenantId, Long spaceId, RuleResp resp) {
		RuleListReq req =new RuleListReq();
		req.setTenantId(tenantId);
		req.setSpaceId(spaceId);
		req.setTemplateId(resp.getId());
		List<Rule> ruleList=ruleTobMapper.findSimpleList(req);
		if(CollectionUtils.isNotEmpty(ruleList)) {
			ruleList.forEach(rule->{
				autoTobService.delete(tenantId,rule.getOrgId(),rule.getId(),true);
			});
		}
	}
  
	

	private SaveIftttReq ruleRespChangeToIftttReq(RuleResp resp,Long spaceId) {
		SaveIftttReq iftttReq=new SaveIftttReq();
		iftttReq.setSpaceId(spaceId);
		iftttReq.setAppletId(resp.getAppletId());
		iftttReq.setDelay(resp.getDelay());
		iftttReq.setDirectId(resp.getDirectId());
		iftttReq.setActuators(resp.getActuators());
		iftttReq.setIcon(resp.getIcon());
		iftttReq.setId(resp.getId());
		iftttReq.setIftttType(resp.getIftttType());
		iftttReq.setIsMulti(resp.getIsMulti());
		iftttReq.setLocationId(resp.getLocationId());
		iftttReq.setName(resp.getName());
		iftttReq.setProductId(resp.getProductId());
		iftttReq.setRelations(resp.getRelations());
		iftttReq.setRuleType(resp.getRuleType());
		iftttReq.setSecurityType(resp.getSecurityType());
		iftttReq.setSensors(resp.getSensors());
		//iftttReq.setSpaceId(resp.getSpaceId());
		iftttReq.setStatus(resp.getStatus());
		iftttReq.setTemplateFlag(resp.getTemplateFlag());
		iftttReq.setTemplateId(resp.getTemplateId());
		iftttReq.setTenantId(resp.getTenantId());
		iftttReq.setTriggers(resp.getTriggers());
		iftttReq.setType(resp.getType());
		iftttReq.setUserId(resp.getUserId());
		return iftttReq;
	}

    private List<Long> getSpaceList(String spaceIds) {
        List<Long> spaces = Lists.newArrayList();
        if (Strings.isNullOrEmpty(spaceIds)) {
            return spaces;
        }
        List<String> ids = Arrays.asList(spaceIds.split(","));
        spaces = Lists.transform(ids, new Function<String, Long>() {
            @Override
            public Long apply(String s) {
                return Long.valueOf(s);
            }
        });

        return spaces;
    }

    /**
     * OTA 版本升级
     * 根据产品ID查询下面的设备，再更新OTA
     */
    private void otaUpdate(AllocationReq req) throws Exception{
		List<GetDeviceInfoRespVo> directVoList=centralControlDeviceApi.
				findDirectDeviceListByVenderCode(req.getTenantId(), req.getLocationId(), 
						APIType.MultiProtocolGateway.name(),1);
		List<GetDeviceInfoRespVo> unDirectVoList=centralControlDeviceApi.
				findDirectDeviceListByVenderCode(req.getTenantId(), req.getLocationId(), 
						APIType.MultiProtocolGateway.name(),0);
		directVoList.addAll(unDirectVoList);
		List<String> deviceIds=Lists.newArrayList();
		for(GetDeviceInfoRespVo vo:directVoList) {
			deviceIds.add(vo.getUuid());
		}
		int concurrent=req.getConcurrent()==null?10:req.getConcurrent();
		int interval=req.getDeviceInterval()==null?60*5:req.getDeviceInterval();
		OTABatch(deviceIds,concurrent,interval);
    }
    
    /**
     * ota 根据并发的数量和时间间隔批量下发
     * @param strList
     * @param concurrent 并发数量
     * @param interval  并发时间间隔
     */
    private static void OTABatch(List<String> strList,int concurrent,int interval) {
		List<String> removeList=new ArrayList<>();//需要移除的配置
		int count=strList.size()>concurrent?concurrent:strList.size();
		for(int i=0;i<count;i++) {
			removeList.add(strList.get(i));
			ThreadPoolUtil.instance().execute(new Runnable() {
				@Override
				public void run() {
					AllocationOTAJob otaJob = new AllocationOTAJob(strList);
					otaJob.start();
				}
			});
		}
		try {
			Thread.sleep(1000*interval);//睡眠时间
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//剔除上次的
		removeList.forEach(deviceId->{
			strList.remove(deviceId);
		}); 
		if(strList.size()>0) {//递归循环
			OTABatch(strList,concurrent,interval);
		}
 }

    /**
     * sence 初始化
     */
    private static Long locationId=null;
    private static Long userId=null;
    private void senceInit(AllocationReq req) throws Exception{
        String spaceIds = req.getSpaceIds();
        if(StringUtils.isBlank(spaceIds)) {
        	throw new Exception("&&&&&&&spaceIds &&&&&&&spaceIds &&&&&&&spaceIds 没有传值 &&&&&&&spaceIds&&&&&&&spaceIds");
        }
        log.info("&&&&&&&spaceIds======================"+spaceIds);
        Long tenantId = req.getTenantId();
        int interval = req.getDeviceInterval() == null ? 30 : req.getDeviceInterval();
        List<Long> spaceList = getSpaceList(spaceIds);
        SpaceAndSpaceDeviceVo spaceReq=new SpaceAndSpaceDeviceVo();
    	spaceReq.setTenantId(tenantId);
    	spaceReq.setSpaceIds(spaceList);
        List<SpaceResp> spaces = spaceApi.findSpaceInfoBySpaceIds(spaceReq);
        // 根据floor分组 并行处理
        Multimap<Long, Long> spaceMap = ArrayListMultimap.create();
        for (SpaceResp space : spaces) {
            if (Constants.SPACE_ROOM.equals(space.getType())) {
                // key -> floorId  value -> roomId
            	locationId=locationId==null?space.getLocationId():locationId;
            	userId=userId==null?space.getUserId():userId;
                spaceMap.put(space.getParentId(), space.getId());
            }
        }
        
        for (Long floorId : spaceMap.keySet()) {
    		List<Long> spaceIds_do=Lists.newArrayList();
            for(Long spaceId:spaceMap.get(floorId)) {
            	spaceIds_do.add(spaceId);
            	log.info("&&&&&&&scene初始化++++======================"+spaceId);
            }
    		SceneTemplateManualReq templateReq =new SceneTemplateManualReq();
    		templateReq.setLocationId(locationId);
    		templateReq.setSpaceIdList(spaceIds_do);
    		templateReq.setTenantId(tenantId);
    		templateReq.setUserId(userId);
    		templateReq.setInterval(interval);
            sceneService.sceneTemplateManualInit(templateReq);
        }
    }

}
