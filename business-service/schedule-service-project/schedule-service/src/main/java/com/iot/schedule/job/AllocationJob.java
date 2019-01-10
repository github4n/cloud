package com.iot.schedule.job;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.building.allocation.api.AllocationApi;
import com.iot.building.allocation.vo.AllocationReq;
import com.iot.building.allocation.vo.ExecuteLogReq;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.service.ScheduleService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.Map;

/**
 * @Author: Xieby
 * @Date: 2018/9/4
 * @Description: 下发配置的定时任务
 */
public class AllocationJob implements Job {

	@Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		AllocationReq allocation=null;
		ExecuteLogReq logReq=null;
		System.out.println("========================= allocation job start ======================");
        Map<String, Object> dataMap =(Map<String, Object>) context.getMergedJobDataMap().get(ScheduleConstants.JOB_DATA_KEY);
        allocation = JSON.parseObject(JSON.toJSONString(dataMap), AllocationReq.class);
        System.out.println("data = " + JSON.toJSONString(allocation));
        allocation.setExeStatus(2);//修改当前的运行状态
        allocation.setExeResult(0);
        // execute issue
    	AllocationApi allocationApi = ApplicationContextHelper.getBean(AllocationApi.class);
    	try {
			allocationApi.executeIssue(allocation);
			logReq = getSuccessLog(allocation);
            allocation.setExeResult(0);//正常
		} catch (Exception e) {
			e.printStackTrace();
            commonExcepiton(allocation,logReq,e);
		}finally {
        	allocation.setExeStatus(3);//修改当前的运行状态
            allocationApi.saveExeLog(logReq);
            allocationApi.editAllocation(allocation);
        }
        System.out.println("========================= allocation job end ======================");
    }

	private void commonExcepiton(AllocationReq allocation,ExecuteLogReq logReq,Exception e) {
		String message=e.getMessage().length()>1200?e.getMessage().substring(0, 1199):e.getMessage();
		 allocation.setParamInfo(message);
		 allocation.setExeResult(1);
		 logReq = getFailLog(allocation);
	}

    private ExecuteLogReq getFailLog(AllocationReq data) {
        ExecuteLogReq req = new ExecuteLogReq();
        String message=data.getParamInfo().length()>2000?data.getParamInfo().substring(0, 1999):data.getParamInfo();
        req.setExeContent(message);
        req.setExeResult(ScheduleConstants.EXE_RESULT_FAIL);
        req.setFunctionId(data.getId());
        req.setCreateTime(new Date());
        return req;
    }

    private ExecuteLogReq getSuccessLog(AllocationReq data) {
        ExecuteLogReq req = new ExecuteLogReq();
        req.setFunctionId(data.getId());
        req.setExeResult(ScheduleConstants.EXE_RESULT_SUCCESS);
        req.setExeContent("execute success.");
        req.setCreateTime(new Date());
        return req;
    }

    public static void main(String[] args) {
        Map map = Maps.newConcurrentMap();
        map.put("id", 100L);
        map.put("allocationId", 200L);
        AllocationReq req = JSON.parseObject(JSON.toJSONString(map), AllocationReq.class);
        System.out.println(JSON.toJSONString(req));

        Integer te = null;
        int i = te;
        System.out.println(i);
    }

}
