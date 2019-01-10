package com.iot.schedule.job;

import com.iot.common.enums.APIType;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.ifttt.api.IftttApi;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.control.ifttt.vo.req.ExecIftttReq;
import com.iot.control.ifttt.vo.req.GetSensorReq;
import com.iot.control.ifttt.vo.res.RuleResp;
import com.iot.control.ifttt.vo.res.SensorResp;
import com.iot.schedule.common.ScheduleConstants;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：定时任务运行（反射出来的类）
 * 创建人：chenshaofeng
 * 创建时间：2018年1月18日 下午3:04:57
 * 修改人： chenshaofeng
 * 修改时间：2018年1月18日 下午3:04:57
 */
@DisallowConcurrentExecution
@Service
public class TimingJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(TimingJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info("-------- task -------- start --------");
        try {
            IftttApi iftttApi = ApplicationContextHelper.getBean(IftttApi.class);
            //ScheduleJob任务运行时具体参数，可自定义  
            SensorVo sensorMapperVo = (SensorVo) context.getMergedJobDataMap().get(ScheduleConstants.JOB_DATA_KEY);
            RuleResp ruleResp = iftttApi.get(sensorMapperVo.getRuleId());
            GetSensorReq req = new GetSensorReq();
            req.setId(sensorMapperVo.getId());
            /*List<SensorResp> sensors = iftttApi.getSensorByParams(req);
            ExecIftttReq execReq = new ExecIftttReq();
            if (CollectionUtils.isNotEmpty(sensors)) {
                SensorResp sensorResp = sensors.get(0);
                SensorVo sensorVo = new SensorVo();
                BeanUtils.copyProperties(sensorResp, sensorVo);
                execReq.setSensor(sensorVo);
            }
            execReq.setRuleResp(ruleResp);
            execReq.setCallbackMap(null);
            execReq.setApiType(APIType.MultiProtocolGateway);
            iftttApi.doActuator(null, execReq);*/
        } catch (Exception e) {
            logger.info("catch exception ======= : " + e);
        }
        logger.info("-------- task -------- end --------");
    }
}  
