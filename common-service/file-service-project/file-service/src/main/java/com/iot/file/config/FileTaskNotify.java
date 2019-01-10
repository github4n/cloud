package com.iot.file.config;

import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.vo.AddJobReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 项目名称：cloud
 * 功能描述：文件服务任务通知
 * 创建人： yeshiyuan
 * 创建时间：2018/11/7 9:55
 * 修改人： yeshiyuan
 * 修改时间：2018/11/7 9:55
 * 修改描述：
 */
@Component
public class FileTaskNotify implements CommandLineRunner{

    @Autowired
    private ScheduleApi scheduleApi;

    @Value("${job.cron.dealUnUploadFileTask:0 0 0/1 * * ?}")
    private String dealUnUploadFileCron;

    private final static String taskName = "dealUnUploadFileInfoTask";

    private static Logger logger = LoggerFactory.getLogger(FileTaskNotify.class);

    /**
     *
      */
    @Override
    public void run(String... strings) {
        try {
            if (!scheduleApi.checkJobIsExists(taskName)) {
                System.out.println("***************** add dealUnUploadFileInfoTask start*****************************");
                AddJobReq jobReq = new AddJobReq();
                jobReq.setJobClass(ScheduleConstants.DEAL_FILE_JOB);
                jobReq.setJobName(taskName);
                jobReq.setCron(dealUnUploadFileCron);
                scheduleApi.addJob(jobReq);
                System.out.println("***************** add dealUnUploadFileInfoTask end*****************************");
            }
        } catch (Exception e) {
            logger.error("FileTaskNotify run error:", e);
        }
    }


}
