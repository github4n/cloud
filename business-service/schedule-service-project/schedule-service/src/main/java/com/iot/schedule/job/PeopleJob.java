package com.iot.schedule.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.iot.common.enums.APIType;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.ifttt.api.IftttApi;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.control.ifttt.vo.req.ExecIftttReq;
import com.iot.control.ifttt.vo.req.GetSensorReq;
import com.iot.control.ifttt.vo.res.RuleResp;
import com.iot.control.ifttt.vo.res.SensorResp;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.util.HttpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * IFTTT 人数触发
 *
 * @author fenglijian
 */
public class PeopleJob implements Job {

    private final static Logger logger = LoggerFactory.getLogger(PeopleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("*************** 获取签到APP人数 start ***************");
        Map<String, Object> data = (Map<String, Object>) context.getMergedJobDataMap().get(ScheduleConstants.JOB_DATA_KEY);
        Long tenantId = Long.valueOf(data.get("tenantId") + "");
        String peopleUrl = String.valueOf(data.get("peopleUrl"));
        if (tenantId != null) {
            checkPeopleAmountIfttt(tenantId, peopleUrl);
        }
        logger.info("*************** 获取签到APP人数 end ***************");

    }

    public void checkPeopleAmountIfttt(Long tenantId, String peopleUrl) {
        String param = "tenantId=" + tenantId;
        try {
            String result = HttpUtil.sendPost(peopleUrl, param);

            if (StringUtils.isNotBlank(result)) {
                JSONObject resultObj = JSON.parseObject(result);
                JSONArray floorPeopleList = resultObj.getJSONArray("data");
                if (CollectionUtils.isNotEmpty(floorPeopleList)) {
                    for (Object obj : floorPeopleList) {
                        JSONObject floorPeople = (JSONObject) obj;
                        Integer usedAmount = floorPeople.getInteger("usedAmount");
                        String spaceId = floorPeople.getString("spaceId");
                        checkSpacePeopleAmountIfttt(spaceId, usedAmount);
                    }
                }
            } else {
                logger.info("*************** 获取签到APP 签到人数为空 ***************");
            }
        } catch (Exception e) {
            logger.info("*************** 调用签到APP人数接口异常 ***************");
            e.printStackTrace();
        }

    }

    public void checkSpacePeopleAmountIfttt(String spaceId, Integer usedAmount) {
        Long id = Long.valueOf(spaceId);
        IftttApi iftttApi = ApplicationContextHelper.getBean(IftttApi.class);
        GetSensorReq getSensorReq = new GetSensorReq();
        getSensorReq.setProperties(String.valueOf(usedAmount));
        getSensorReq.setSpaceId(id);
        getSensorReq.setType("people");
        /*List<SensorResp> sensorVOs = iftttApi.getSensorByParams(getSensorReq);
        if (CollectionUtils.isNotEmpty(sensorVOs)) {
            for (SensorResp sensorVO : sensorVOs) {
                try {
                    RuleResp ruleResp = iftttApi.get(sensorVO.getRuleId());
                    Map<String, Object> callbackMap = Maps.newHashMap();
                    callbackMap.put("usedAmount", usedAmount);

                    ExecIftttReq execReq = new ExecIftttReq();
                    SensorVo sensorVo = new SensorVo();
                    BeanUtils.copyProperties(sensorVO, sensorVo);
                    execReq.setSensor(sensorVo);
                    execReq.setRuleResp(ruleResp);
                    execReq.setCallbackMap(callbackMap);
                    execReq.setApiType(APIType.MultiProtocolGateway);
                    iftttApi.doActuator(null, execReq);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        //}
    }

}
