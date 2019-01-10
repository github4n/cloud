package com.iot.building.template.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.allocation.service.ActivityRecordService;
import com.iot.building.allocation.vo.ActivityRecordReq;
import com.iot.building.allocation.vo.ActivityRecordResp;
import com.iot.building.helper.Constants;
import com.iot.building.index.vo.IndexScheduleVo;
import com.iot.building.template.mapper.SpaceTemplateMapper;
import com.iot.building.template.service.SpaceTemplateService;
import com.iot.building.template.vo.ScheduledDetailVo;
import com.iot.building.template.vo.ScheduledVo;
import com.iot.building.template.vo.req.SpaceTemplateReq;
import com.iot.building.template.vo.rsp.SpaceTemplateResp;
import com.iot.common.helper.Page;
import com.iot.common.helper.PageHelper;

@Service
public class SpaceTemplateServiceImpl implements SpaceTemplateService {
	
	private static final Logger logger = LoggerFactory.getLogger(SpaceTemplateServiceImpl.class);
	
	@Autowired
	private SpaceTemplateMapper spaceTemplateMapper;
	@Autowired
	private ActivityRecordService activityRecordService;
	
	private final static Long ONE_DAY_TIMES=86400000l;
	private final static Long END_DAY_MIDDLE_TIMES=86399000l;

	@Override
	public Page<SpaceTemplateResp> list(SpaceTemplateReq spaceTemplateReq) {
		PageHelper.startPage(spaceTemplateReq.getPageNum(), spaceTemplateReq.getPageSize());
		spaceTemplateMapper.findByCondition(spaceTemplateReq);
		return (Page<SpaceTemplateResp>) PageHelper.endPage();
	}

	@Override
	public List<SpaceTemplateResp> findByCondition(SpaceTemplateReq spaceTemplateReq) {
		List<SpaceTemplateResp> list = spaceTemplateMapper.findByCondition(spaceTemplateReq);
		return list;
	}
	
	@Override
	public List<Long> findTemplateIdListByCondition(SpaceTemplateReq spaceTemplateReq) {
		List<Long> list = spaceTemplateMapper.findTemplateIdListByCondition(spaceTemplateReq);
		return list;
	}

	@Override
	public Long spaceTemplateSave(SpaceTemplateReq spaceTemplateReq) {
		spaceTemplateMapper.spaceTemplateSave(spaceTemplateReq);
		return spaceTemplateReq.getId();
	}

	@Override
	public Integer spaceTemplateDelete(SpaceTemplateReq spaceTemplateReq) {
		int rec = spaceTemplateMapper.spaceTemplateDelete(spaceTemplateReq);
		return rec;
	}

	@Override
	public Integer spaceTemplateUpdate(SpaceTemplateReq spaceTemplateReq) {
		int rec = spaceTemplateMapper.spaceTemplateUpdateTemplateId(spaceTemplateReq);
		return rec;
	}
	
	public List<ScheduledVo> findByValidityDate(SpaceTemplateReq req){
		List<ScheduledVo> voList=Lists.newArrayList();
		try {
		    Long firstDateLong=req.getStartTime();
		    Long endDaetLong=req.getEndTime();
		    
		    while(firstDateLong.compareTo(endDaetLong)<0) {
		    	 ScheduledVo vo =new ScheduledVo();
		    	 vo.setDate(String.valueOf(firstDateLong));
		    	 //传参
		    	 req.setStartTime(firstDateLong);
		    	 req.setEndTime(firstDateLong+END_DAY_MIDDLE_TIMES);
		    	 List<SpaceTemplateResp> list = getScheduledLog(req);
		    	 vo.setBackList(list);
		    	 voList.add(vo);
		    	 //赋值 准备下次循环
		    	 firstDateLong=firstDateLong+ONE_DAY_TIMES;
		    	 increaseWeek(req);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return voList;
	}
	
	private void increaseWeek(SpaceTemplateReq req) {
		if(StringUtils.isNotBlank(req.getWeek())){
			int weekNum=Integer.valueOf(req.getWeek());
			weekNum++;
			weekNum=weekNum>7?1:weekNum;
			req.setWeek(String.valueOf(weekNum));
		}
	}

	private List<SpaceTemplateResp> getScheduledLog(SpaceTemplateReq req) {
		List<SpaceTemplateResp> list=spaceTemplateMapper.findByValidityDate(req);
		List<SpaceTemplateResp> backList=getRightWeekList(list,req.getWeek());//过滤这天是否符合设置的周
		if(CollectionUtils.isNotEmpty(backList)) {
			list.forEach(resp->{
				UnifiedTime(req, resp);//统一时间差
				setScheduleRunOrEnd(req, resp);//设置是否运行标识
			});
		}
		return backList;
	}

	/**
	 * 统一时间差
	 * @param req
	 * @param resp
	 */
	private void UnifiedTime(SpaceTemplateReq req, SpaceTemplateResp resp) {
		if(req.getStartTime().compareTo(req.getEndTime())==0
				 || resp.getLoopType().equals(Constants.LOOP_TYPE_ONCE)) {
			req.setEndTime(req.getEndTime()+ONE_DAY_TIMES);//开始时间和结束时间一样为单次，无法通过时间段查询 延迟结束时间
		}
		req.setStartTime(req.getStartTime()-req.getTimeDiff());//用户时间和0时区时间的差值
		req.setEndTime(req.getEndTime()-req.getTimeDiff());//用户时间和0时区时间的差值
	}

	/**
	 * 判断该空间schedule是否已经运行过
	 * @param req
	 * @param resp
	 */
	private void setScheduleRunOrEnd(SpaceTemplateReq req, SpaceTemplateResp resp) {
		List<ActivityRecordResp> logList=activityRecordService.queryByValidityDate(resp.getSpaceId(),resp.getTemplateId().toString(),"SCHEDULE",
				req.getStartTime(),req.getEndTime(),resp.getId());
		if(CollectionUtils.isNotEmpty(logList)) {
			ActivityRecordResp active=logList.get(0);
			resp.setReason(active.getActivity());
			int flag=-2;//1 成功运行  0 未运行  -1存在假期日历不执行  -2运行失败
			if(active.getActivity().equals("success")) {
				flag=1;
			}else if(active.getActivity().equals("exist calendar")){
				flag=-1;
			}
			resp.setFlag(flag);//已经运行
		}else {
			resp.setFlag(0);//未运行
		}
	}
	
	//判断自循环的星期几是否在当前schedule范围
	private List<SpaceTemplateResp> getRightWeekList(List<SpaceTemplateResp> list,String week){
		List<SpaceTemplateResp> backList=Lists.newArrayList();
		backList.addAll(list);
		if(CollectionUtils.isNotEmpty(list) && StringUtils.isNotBlank(week)) {
			list.forEach(resp->{
				if(resp.getLoopType().equals(Constants.LOOP_TYPE_LOOP) &&
						resp.getWeek() !=null && !resp.getWeek().contains(week)) {
					backList.remove(resp);
				}
			});
		}
		return backList;
	}
	
	@Override
	public ScheduledDetailVo findScheduledDetalAndLog(SpaceTemplateReq spaceTemplateReq) {
		SpaceTemplateResp resp=spaceTemplateMapper.findById(spaceTemplateReq);
		List<ActivityRecordResp> logList=activityRecordService.queryByValidityDate(resp.getSpaceId(),resp.getTemplateId().toString(),
				"SCHEDULE",null,null,null);
		ScheduledDetailVo vo =new ScheduledDetailVo();
		vo.setActiveList(logList);
		vo.setSpaceTemplateResp(resp);
		return vo;
	}
	
	public Map<String,Object> beforeAndAfterSchedule(Long locationId,Long currentTime,Long endTime,String week,Long spaceId){
		Map<String,Object> map=Maps.newHashMap();
		//1.在当前时间执行过最近的schedule
		IndexScheduleVo firstVo = getBeforeVo(currentTime, week, spaceId);
		map.put("before", firstVo);
		//2.距离当前时间最近的么有执行的schedule
		List<IndexScheduleVo> afterList=getAfter(locationId,currentTime,endTime,week,spaceId);
		map.put("after",afterList);
		return map;
	}

	private IndexScheduleVo getBeforeVo(Long currentTime, String week, Long spaceId) {
		SpaceTemplateReq spaceTemplateReq=new SpaceTemplateReq();
		spaceTemplateReq.setStartTime(currentTime);
		spaceTemplateReq.setSpaceId(spaceId);
		spaceTemplateReq.setWeek(week);
		SpaceTemplateResp resp=spaceTemplateMapper.findHistoryByCondition(spaceTemplateReq);
		IndexScheduleVo firstVo =new IndexScheduleVo();
		if(resp !=null) {
			//查询执行记录寻找时间
			ActivityRecordReq re=new ActivityRecordReq();
			re.setSpaceId(spaceId);
			re.setSpaceTemplateId(resp.getId());
			re.setTenantId(resp.getTenantId());
			re.setLocationId(resp.getLocationId());
			re.setType("SCHEDULE");
			re.setForeignId(resp.getTemplateId().toString());
			List<ActivityRecordResp> recordList=activityRecordService.queryActivityRecordByCondition(re);
			if(CollectionUtils.isNotEmpty(recordList)) {
				firstVo.setTime(Long.valueOf(recordList.get(0).getSetTime()));
				firstVo.setRunTime(resp.getRunTime());
				firstVo.setName(resp.getTemplateName());
			}
		}
		
		return firstVo;
	}
	
	
	private List<IndexScheduleVo> getAfter(Long locationId,Long currentTime,Long endTime,String week,Long spaceId){
		int flag=0,total=0; 
		Long startTime=currentTime;
		Long lastTime=currentTime+3600*24*30*1000L;//
		Map<Long,List<SpaceTemplateResp>> mapList=Maps.newHashMap();
		SpaceTemplateReq req=new SpaceTemplateReq();
		req.setStartTime(startTime);//
		req.setEndTime(endTime);//
		req.setSpaceId(spaceId);
		req.setWeek(week);
		req.setLocationId(locationId);
		while(total<2) {//循环两条数据
			if(endTime.compareTo(lastTime)>0) {
				break;//防止没有找到数据无限循环
			}
			if(flag>0) {
				req.setStartTime(endTime+1000L);//跨第二天
				endTime+=ONE_DAY_TIMES;
				req.setEndTime(endTime);//当前天结束
			}
			List<SpaceTemplateResp> list=spaceTemplateMapper.findByValidityDate(req);
			List<SpaceTemplateResp> backList=getRightWeekList(list,req.getWeek());//过滤这天是否符合设置的周
			increaseWeek(req);//循环增加星期
			if(CollectionUtils.isNotEmpty(backList)) {
				mapList.put(req.getStartTime(),backList);
				total+=mapList.size();
			}
			flag++;
		}
		//过滤最前面的两个
		List<IndexScheduleVo> backList=Lists.newArrayList();
		if(MapUtils.isNotEmpty(mapList)) {
			int status=0;
			for(Long key:mapList.keySet()) {
				if(status==0) {
					for(SpaceTemplateResp resp:firstList(mapList.get(key))){
						IndexScheduleVo vo=new IndexScheduleVo();
						vo.setTime(key);
						vo.setName(resp.getTemplateName());
						vo.setRunTime(resp.getRunTime());
						backList.add(vo);
					}
					if(backList.size()==2) {
						break;
					}
				}else if(status==1) {
					for(SpaceTemplateResp resp:secondList(mapList.get(key))){
						IndexScheduleVo vo=new IndexScheduleVo();
						vo.setTime(key);
						vo.setName(resp.getTemplateName());
						vo.setRunTime(resp.getRunTime());
						backList.add(vo);
					}
				}
				status++;
			}
		}
		return backList;
	}

	private List<SpaceTemplateResp> firstList(List<SpaceTemplateResp> list) {
		 List<SpaceTemplateResp> backlist=Lists.newArrayList();
		 Comparator<SpaceTemplateResp> comparator = (r1,r2) ->getASCII(r1.getRunTime()).compareTo(getASCII(r2.getRunTime()));
		 list.sort(comparator);//comparator.reversed()
		 if(list.size()>2) {
			 backlist.add(list.get(0));
			 backlist.add(list.get(1));
		 }
		 return CollectionUtils.isEmpty(backlist)?list:backlist;
	}
	
	private List<SpaceTemplateResp> secondList(List<SpaceTemplateResp> list) {
		 List<SpaceTemplateResp> backlist=Lists.newArrayList();
		 Comparator<SpaceTemplateResp> comparator = (r1,r2) ->getASCII(r1.getRunTime()).compareTo(getASCII(r2.getRunTime()));
		 list.sort(comparator);
		 backlist.add(list.get(0));
		 return backlist;
	}
	
	private static Long getASCII(String runTime) {
		String total="";
		char[] ch=runTime.toCharArray();
		for(int i=0;i<ch.length;i++) {
			total+=Integer.valueOf(ch[i])+"";
		}
		return Long.valueOf(total);
	}
	
	public static void main(String[] args) {
		System.out.println(weekByDate(2018,12,1));
	}
	
	public static int weekByDate (int year,int month,int day) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd MM yyyy");
        Date d = null;
	    try {
	        d = fmt.parse(day+" "+month+" "+year);
	    }catch (ParseException e){
	    	e.printStackTrace();
	    }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        return weekDay;
    }
	
}
