package com.iot.center.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.iot.building.allocation.api.AllocationApi;
import com.iot.building.allocation.vo.AllocationNameReq;
import com.iot.building.allocation.vo.AllocationNameResp;
import com.iot.building.allocation.vo.AllocationReq;
import com.iot.building.allocation.vo.AllocationResp;
import com.iot.building.allocation.vo.ExecuteLogReq;
import com.iot.center.utils.CronDateUtils;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.saas.SaaSContextHolder;
import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.vo.AddJobReq;
import com.iot.user.vo.LoginResp;

/**
 * @Author: Xieby
 * @Date: 2018/9/3
 * @Description: *
 */
@Service
public class AllocationService {

    @Autowired
    private ScheduleApi scheduleApi;
    @Autowired
    private AllocationApi allocationApi;


    public CommonResponse<Page<AllocationNameResp>> queryList(AllocationNameReq req) {
        Page<AllocationNameResp> page = allocationApi.queryAllocationNameList(req);
        return CommonResponse.success(page);
    }

    public CommonResponse<Page<AllocationResp>> queryConfigList(AllocationReq req) {
        Page<AllocationResp> page = allocationApi.queryAllocationList(req);
        return CommonResponse.success(page);
    }

    /**
     * 保存配置信息
     */
    public CommonResponse saveAllocation(AllocationReq req) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            // 保存配置
            req.setCreateTime(new Date());
            req.setUpdateTime(new Date());
            if (user != null) {
                req.setCreateBy(String.valueOf(user.getUserId()));
                req.setUpdateBy(String.valueOf(user.getUserId()));
            }

            req.setTenantId(user.getTenantId());
            req.setLocationId(user.getLocationId());
            String cronInfo;
            if(req.getAllocationId()==4) {//设备属性同步做特殊处理
            	cronInfo=getEveryTime(req.getDeviceInterval());
            }else {
            	cronInfo= getCron(req.getExeTime(), req.getIsLoop(), req.getSelectWeek());
            }
            req.setParamInfo(cronInfo);
            req.setExeResult(0);//最后一次执行结果：0：正常 1；异常
            req.setExeStatus(1);//执行状态：1：未开始 2；运行中 3；已结束
            Long id = allocationApi.saveAllocation(req);
            req.setId(id);
            // 新增schedule
            addAllocationSchedule(req);

            return CommonResponse.success();
        } catch (BusinessException e) {
            throw e;
        }
    }

    public CommonResponse editAllocation(AllocationReq req) {
        try {

            // delete job
            scheduleApi.delJob(ScheduleConstants.ALLOCATION_JOB + "_" + req.getId());

            // update allocation
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            if (user != null) {
                req.setUpdateBy(String.valueOf(user.getUserId()));
            }
            req.setUpdateTime(new Date());
            req.setTenantId(user.getTenantId());
            String cronInfo;
            if(req.getAllocationId()==4) {//设备属性同步做特殊处理
            	cronInfo=getEveryTime(req.getDeviceInterval());
            }else {
            	cronInfo= getCron(req.getExeTime(), req.getIsLoop(), req.getSelectWeek());
            }
            req.setParamInfo(cronInfo);
            req.setExeResult(0);//最后一次执行结果：0：正常 1；异常
            req.setExeStatus(1);//执行状态：1：未开始 2；运行中 3；已结束
            allocationApi.editAllocation(req);
            addAllocationSchedule(req);
            return CommonResponse.success();
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 将上传时间转换跟中央时区时间
     */
    private Date transferDateToZone(String exeTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            ZoneId defaultZone = ZoneId.systemDefault();
            Instant instant = Instant.now();
            // 获取当前时区值
            int hour = defaultZone.getRules().getStandardOffset(instant).getTotalSeconds()/3600;
            // 与中央时区的小时间距
            int current = 0 - hour;
            sdf.setTimeZone(TimeZone.getTimeZone("GMT" + (current >= 0 ? "+" : "") + current));
            Date d = sdf.parse(exeTime);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CommonResponse deleteAllocation(Long tenantId,Long orgId,Long id) {
        try {
            // delete schedule
        	SaaSContextHolder.setCurrentTenantId(tenantId);
            scheduleApi.delJob(ScheduleConstants.ALLOCATION_JOB + "_" + id);
            // delete allocation
            allocationApi.deleteAllocation(tenantId,orgId,id);
            return CommonResponse.success();
        } catch (BusinessException e) {
            throw e;
        }
    }

    public CommonResponse<ExecuteLogReq> queryErrorLog(Long tenantId,Long orgId,Long locationId,Long id) {
        try {
            ExecuteLogReq log = allocationApi.queryErrorLog(tenantId,orgId,locationId,id);
            return CommonResponse.success(log);
        } catch (BusinessException e) {
            throw e;
        }
    }

    private String getCron(String exeTime, Integer isLoop, String selectWeek) {
        String[] times = CronDateUtils.splitDateTime(exeTime);
        String weekStart = times[3] + ":" + times[4];
        String weekEnd = times[3] + ":" + times[4];
        Map<String, String> cronMap = CronDateUtils.generateCron(exeTime, exeTime, String.valueOf(isLoop),
                selectWeek, weekStart, weekEnd);
        return cronMap.get("startCron");
    }
    
    private String getEveryTime(Integer exeTime) {
       Long time=Long.valueOf(exeTime);
       String startCron = "0/30 * * * * ?";
       if(0<time && time<60) {
    	   //每x秒
    	   startCron = "0/"+time+" * * * * ?";//从0秒开始每time秒执行一次
       }else if(time>=60 && time<3600) {
    	   //每x分钟
    	   time=time/60;
    	   startCron = "0 0/"+time+" * * * ?";//从0分开始每time分钟执行一次
       }else if(time>=3600 && time<3600*60) {
    	   //每小时
    	   time=time/3600;
    	   startCron = "0 0 0/"+time+" * * ?";//从0点开始每time小时执行一次
	   }else if(time>=3600*60 && time<3600*60*24) {
	    	//每天
		   time=time/(3600*60);
		   startCron = "0 0 0 1/"+time+" * ?";//从1天开始每time天执行一次
	   }
       return startCron;
    }

    public CommonResponse<AllocationResp> getAllocationById(Long tenantId,Long orgId,Long id) {
        AllocationResp resp = allocationApi.selectById(tenantId,orgId,id);
        return CommonResponse.success(resp);
    }

    /**
     * 添加配置的定时任务
     */
    private void addAllocationSchedule(AllocationReq info) {

        String cron = info.getParamInfo();
        Map dataMap = Maps.newHashMap();
        dataMap.put("id", info.getId());
        dataMap.put("spaceIds", info.getSpaceIds());
        dataMap.put("deviceInterval", info.getDeviceInterval());
        dataMap.put("allocationId", info.getAllocationId());
        dataMap.put("tenantId", info.getTenantId());
        dataMap.put("locationId", info.getLocationId());
        dataMap.put("concurrent", info.getConcurrent());
        AddJobReq jobReq = new AddJobReq();
        jobReq.setCron(cron);
        jobReq.setData(dataMap);
        jobReq.setJobClass(ScheduleConstants.ALLOCATION_JOB);
        jobReq.setJobName(ScheduleConstants.ALLOCATION_JOB + "_" + info.getId());
        scheduleApi.addJob(jobReq); // 保存到GRTZ_CRON_TRIGGERS
    }

    public static void main(String[] args) throws ParseException {

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        sdf.setTimeZone(TimeZone.getTimeZone("GMT+4"));
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//
//        ZoneId defaultZone = ZoneId.systemDefault();
//        Instant instant = Instant.now();
//        int hour = defaultZone.getRules().getStandardOffset(instant).getTotalSeconds()/3600;
//        int current = 0 - hour;
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT" + (current >= 0 ? "+" : "") + current));
//        Date d = sdf.parse("2018-01-01 16:16:00");
//        System.out.println(d);
    	
    	Long a=(long) (3600*60*23.5);
		System.out.println(a/(3600*60));
    }
}
