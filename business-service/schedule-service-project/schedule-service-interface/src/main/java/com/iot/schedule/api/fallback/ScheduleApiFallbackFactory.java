package com.iot.schedule.api.fallback;

import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.vo.AddJobReq;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduleApiFallbackFactory implements FallbackFactory<ScheduleApi> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleApiFallbackFactory.class);

    public ScheduleApi create(Throwable throwable) {
        return new ScheduleApi() {
            public void addJob(AddJobReq req) {

            }

            public void delJob(String jobName) {

            }

            public void updateJob(String jobName, String cron) {

            }

            public void startJobs() {

            }

            public void execute(String jobClass) {

            }

            public boolean checkJobIsExists(String jobName) {
                return false;
            }
        };
    }
}
