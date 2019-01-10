package com.iot.schedule.job;

import com.google.common.collect.Maps;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.ifttt.api.IftttApi;
import com.iot.control.ifttt.constant.IftttConstants;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.LocationSceneDetailReq;
import com.iot.control.scene.vo.rsp.LocationSceneDetailResp;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.vo.SpaceResp;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.helper.LoopEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LocationJob implements Job {

    private String SCHEDULE_SCENE = "scene";
    private String SCHEDULE_IFTTT = "ifttt";


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("========================= template job start ======================");
        try{
            Map<String, Object> data =(Map<String, Object>) context.getMergedJobDataMap().get(ScheduleConstants.JOB_DATA_KEY);
            List<Object> dataIds = (List<Object>) data.get("dataIds");
            //String tenantId = data.get("tenantId").toString();
            LocationSceneDetailReq locationSceneDetailReq = new LocationSceneDetailReq();
            SceneApi sceneApi = ApplicationContextHelper.getBean(SceneApi.class);
            SpaceApi spaceApi = ApplicationContextHelper.getBean(SpaceApi.class);
            if(dataIds!=null){
                for(Object dataIdObj : dataIds){
                    long a = Long.valueOf(dataIdObj.toString());
                    //获取scene_id
                    locationSceneDetailReq.setLocationSceneId(a);
//                    List<LocationSceneDetailResp> str = sceneApi.findLocationSceneDetailList(locationSceneDetailReq);
                    List<LocationSceneDetailResp> str = null;
                    for(LocationSceneDetailResp locationSceneDetailResp:str){
                        Long dataId = locationSceneDetailResp.getSceneId();
//                        sceneApi.sceneExecute(dataId);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("========================= template job end ======================");
    }

    public void run(Long dataId, Boolean start){
        IftttApi iftttApi = ApplicationContextHelper.getBean(IftttApi.class);
        //iftttApi.run(dataId, start);
    }

    public Date parseDate(String datetime, String formatter){
        SimpleDateFormat sdf = null;
        if(StringUtils.isBlank(formatter)){
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }else{
            sdf = new SimpleDateFormat(formatter);
        }
        try {
            return sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean checkTime(Date startDate, Date endDate){
        boolean flag = false;
        Date now = new Date();
        if(now.compareTo(startDate)>0&&now.compareTo(endDate)<0){
            flag = true;
        }
        return flag;
    }

}
