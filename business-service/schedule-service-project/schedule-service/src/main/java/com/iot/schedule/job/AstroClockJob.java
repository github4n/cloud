package com.iot.schedule.job;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.ifttt.api.IftttApi;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IFTTT 天文定时触发
 *
 * @author chenhongqiao
 */
public class AstroClockJob implements Job {

    private final static Logger logger = LoggerFactory.getLogger(AstroClockJob.class);

    private IftttApi iftttApi = ApplicationContextHelper.getBean(IftttApi.class);

    @Override
    public void execute(JobExecutionContext context) {
        logger.info("*************** 获取天文定时 start ***************");
        try {
            iftttApi.countAstroClockJob();
        } catch (Exception e) {
            logger.error("天文定时添加失败！", e);
            e.printStackTrace();
        }
        logger.info("*************** 获取天文定时 end ***************");
    }
}
