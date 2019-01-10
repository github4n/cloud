package com.iot.schedule.job;


import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.ifttt.api.IftttApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.controller.ScheduleController;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 描述：ifttt执行任务
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/28 10:29
 */
@Service
public class SensorJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(ScheduleController.class);
    //不能使用注解方式获取bean
    private IftttApi iftttApi = ApplicationContextHelper.getBean(IftttApi.class);

    @Override
    public void execute(JobExecutionContext context) {
        logger.info("执行IFTTT任务开始");
        try {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            Map<String, Object> data = (Map<String, Object>) dataMap.get(ScheduleConstants.JOB_DATA_KEY);
            //logger.info("执行IFTTT任务：" + data.toString());

            Long ruleId = Long.parseLong(data.get("ruleId").toString());
            String type = String.valueOf(data.get("type"));
            String tenantId = String.valueOf(data.get("tenantId"));

            //调用ifttt服务方法
            if(StringUtils.isEmpty(tenantId)){
                logger.warn("tenantId 没有值，执行IFTTT任务失败"+ruleId);
            }else{
                //iftttApi.execCronIftttByRule(ruleId, Long.parseLong(tenantId), type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("执行IFTTT任务结束");
    }
}
