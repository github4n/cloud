package com.iot.schedule.job;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.enums.APIType;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.ifttt.api.IftttApi;
import com.iot.control.ifttt.constant.IftttConstants;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.control.ifttt.vo.req.ExecIftttReq;
import com.iot.control.ifttt.vo.req.GetSensorReq;
import com.iot.control.ifttt.vo.res.RuleResp;
import com.iot.control.ifttt.vo.res.SensorResp;
import com.iot.schedule.common.ScheduleConstants;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * IFTTT天气触发
 *
 * @author fenglijian
 */
public class WeatherJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(WeatherJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String result = "";
        try {
            String ip = java.net.URLEncoder.encode("US3290092", "utf-8");
            //拼地址
            String apiUrl = "https://free-api.heweather.com/s6/weather/now?location=" + ip + "&key=744556359df645d8bc082b071dd9423c&lang=en&unit=m";
            //开始请求
            URL url = new URL(apiUrl);
            URLConnection open = url.openConnection();
            InputStream input = open.getInputStream();
            //这里转换为String
            result = org.apache.commons.io.IOUtils.toString(input, "utf-8");
            //System.out.println("*******************************"+result); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> data = (Map<String, Object>) context.getMergedJobDataMap().get(ScheduleConstants.JOB_DATA_KEY);
        Long tenantId = Long.valueOf(data.get("tenantId") + "");
        JSONObject jsonObject = JSONObject.parseObject(result);
        Map<String, Object> maps = jsonObject;
        List<Map<String, Object>> firstList = (List<Map<String, Object>>) maps.get("HeWeather6");
        Map<String, Object> map1 = firstList.get(0);
        Map<String, Object> now = (Map<String, Object>) map1.get("now");
        String cond_code = (String) now.get("cond_code");
        IftttApi iftttApi = ApplicationContextHelper.getBean(IftttApi.class);
        GetSensorReq getSensorReq = new GetSensorReq();
        getSensorReq.setStatus(IftttConstants.RUNNING);
        getSensorReq.setTenantId(tenantId);
        getSensorReq.setProperties(cond_code);
        /*List<SensorResp> sensorVOs = iftttApi.getSensorByParams(getSensorReq);
        for (SensorResp sensorVO : sensorVOs) {
            try {
                RuleResp ruleResp = iftttApi.get(sensorVO.getRuleId());
                ExecIftttReq execReq = new ExecIftttReq();
                SensorVo vo = new SensorVo();
                BeanUtils.copyProperties(sensorVO, vo);
                execReq.setSensor(vo);
                execReq.setRuleResp(ruleResp);
                execReq.setCallbackMap(null);
                execReq.setApiType(APIType.MultiProtocolGateway);
                iftttApi.doActuator(null, execReq);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

}
