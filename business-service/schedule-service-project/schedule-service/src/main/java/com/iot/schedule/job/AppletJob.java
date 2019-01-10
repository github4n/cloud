package com.iot.schedule.job;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.ifttt.api.IftttApi;
import com.iot.ifttt.common.IftttTypeEnum;
import com.iot.ifttt.vo.CheckAppletReq;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.controller.ScheduleController;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 描述：IFTTT程序任务
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/27 19:13
 */
public class AppletJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(ScheduleController.class);
    //不能使用注解方式获取bean
    private IftttApi iftttApi = ApplicationContextHelper.getBean(IftttApi.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("执行IFTTT任务开始");
        try {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            Map<String, Object> data = (Map<String, Object>) dataMap.get(ScheduleConstants.JOB_DATA_KEY);
            //logger.info("执行IFTTT任务：" + data.toString());

            String itemIdStr = data.get("itemId").toString();
            //调用ifttt服务方法
            if (StringUtils.isEmpty(itemIdStr)) {
                logger.warn("itemId 没有值，执行IFTTT任务失败");
            } else {
                Long itemId = Long.parseLong(itemIdStr);
                CheckAppletReq req = new CheckAppletReq();
                req.setType(IftttTypeEnum.TIMER.getType());
                req.setMsg("{\"itemId\":" + itemId + "}");
                iftttApi.check(req);
            }
        } catch (Exception e) {
            logger.error("执行IFTTT任务失败！", e);
        }
        logger.info("执行IFTTT任务结束");
    }
}
