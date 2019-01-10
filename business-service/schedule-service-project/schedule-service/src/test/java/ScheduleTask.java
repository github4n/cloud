import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Date;

/**
 * @Author: Xieby
 * @Date: 2018/8/23
 * @Description: *
 */
public class ScheduleTask {

    public void run() throws Exception {
        Logger log = LoggerFactory.getLogger(ScheduleTask.class);

        log.info("------- Initializing -------------------");

        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        log.info("------- Initialization Complete --------");

        log.info("------- Scheduling Jobs ----------------");

        // jobs can be scheduled before sched.start() has been called

        // job 1 will run every 20 seconds
        JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1").build();
        //每20秒执行一次
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?"))
                .build();

        Date ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());


        log.info("------- Starting Scheduler ----------------");

        // All of the jobs have been added to the scheduler, but none of the
        // jobs
        // will run until the scheduler has been started
        sched.start();

        log.info("------- Started Scheduler -----------------");

    }

    public static void main(String[] args) throws Exception {

        ScheduleTask example = new ScheduleTask();
        example.run();
    }

}
