package com.iot.schedule.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.iot.building.allocation.api.ActivityRecordApi;
import com.iot.building.allocation.vo.ActivityRecordReq;
import com.iot.building.common.util.ToolUtil;
import com.iot.building.shortcut.api.ShortcutApi;
import com.iot.building.shortcut.vo.ShortcutVo;
import com.iot.building.template.vo.req.SpaceTemplateReq;
import com.iot.common.helper.ApplicationContextHelper;


public class TemplateJob implements Job {
	
	private final static Logger logger = LoggerFactory.getLogger(TemplateJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("========================= template job start ======================");
		ShortcutApi shortcutApi= ApplicationContextHelper.getBean(ShortcutApi.class);
		ActivityRecordApi service = ApplicationContextHelper.getBean(ActivityRecordApi.class);
		Map<String, Object> dataMap =(Map<String, Object>) context.getMergedJobDataMap().get("job_data");
        SpaceTemplateReq req = JSON.parseObject(JSON.toJSONString(dataMap), SpaceTemplateReq.class);
		ActivityRecordReq recordReq = assemble(req);
		try{
			//执行schedule 
			ShortcutVo vo=new ShortcutVo();
			vo.setId(req.getTemplateId());
			vo.setTenantId(req.getTenantId());
			vo.setType(req.getTemplateType());
			vo.setFlag(req.getBusiness().equals("1")?1:0);
			vo.setLocationId(req.getLocationId());
			shortcutApi.scheduleExcute(vo);
			logger.info("========================= template excute end ======================");
		}catch(Exception e){
			e.printStackTrace();
			recordReq.setActivity(e.getMessage());
		}finally {
			service.saveActivityRecordToB(recordReq);
		}
		logger.info("========================= template job end ======================");
	}

	private ActivityRecordReq assemble(SpaceTemplateReq req) {
		ActivityRecordReq recordReq = new ActivityRecordReq();
		recordReq.setTenantId(req.getTenantId());
		recordReq.setLocationId(req.getLocationId());
		recordReq.setType(req.getTemplateType());
		Date date=new Date();
		recordReq.setTime(date);
		recordReq.setSetTime(String.valueOf(ToolUtil.localToUTC()));
		recordReq.setUserId(1L);
		recordReq.setUserName("Admin");
		recordReq.setActivity("success");
		recordReq.setForeignId(req.getTemplateId().toString());
		recordReq.setType("SCHEDULE");
		recordReq.setStartTime(req.getStartTime().toString());
		recordReq.setTemplateName(req.getTemplateName());
		recordReq.setSpaceId(req.getSpaceId());
		recordReq.setSpaceTemplateId(req.getId());
		return recordReq;
	}
	
	

	public static void main(String[] args) {
		System.out.println(ToolUtil.localToUTC());
	}

}
