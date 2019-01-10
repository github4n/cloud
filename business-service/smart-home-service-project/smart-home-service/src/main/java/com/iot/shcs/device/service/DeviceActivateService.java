package com.iot.shcs.device.service;

import com.iot.common.util.CalendarUtil;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.redis.RedisCacheUtil;
import com.iot.report.api.DeviceActivateApi;
import com.iot.report.dto.req.DevActivatedVo;
import com.iot.report.entity.DeviceActiveInfo;
import com.iot.shcs.contants.ModuleConstants;
import com.iot.shcs.device.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @Description:    iot
* @Author:         nongchongwei
* @CreateDate:     2019/1/7 9:56
* @UpdateUser:     nongchongwei
* @UpdateDate:     2019/1/7 9:56
* @UpdateRemark:   
* @Version:        1.0
*/
@Slf4j
@Component("deviceActivateService")
public class DeviceActivateService{

    @Autowired
    private DeviceActivateApi deviceActivateApi;

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

    public void recordActiveInfo(Long tenantId, String subDevId, String deviceId,Long deviceTypeId) {
        if(null == tenantId || StringUtil.isEmpty(deviceId) || StringUtil.isEmpty(subDevId) || null==deviceTypeId ){
            return;
        }
        try{
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    //查询设备是否已经激活，查缓存，缓存没有再查mongodb
                    String activatedStatusKey = RedisKeyUtils.getDevActivatedStatusKey(subDevId);
                    String activatedStatus = RedisCacheUtil.valueGet(activatedStatusKey);
                    if(null == activatedStatus || ModuleConstants.DEV_NOT_ACTIVATED.equals(activatedStatus)){
                        DeviceActiveInfo deviceActiveInfo = deviceActivateApi.getDeviceActiveInfoByUuid(subDevId);
                        if(null == deviceActiveInfo){
                            //已激活不处理，未激活则将设备信息放入缓存待定时任务取出存入mongodb
                            String key = RedisKeyUtils.getDevActivatedKey(CalendarUtil.getNowDate(),tenantId);
                            RedisCacheUtil.listPush(key, new DevActivatedVo(subDevId,new Date(),deviceId,deviceTypeId));
                        }
                    }
                }
            });
        }catch (Exception e){
            log.error("",e);
        }
    }


    public void recordActiveInfo(Long tenantId, List<String> subDeviceIdList, String deviceId,Map<String, GetProductInfoRespVo> deviceToProductMap) {
        if(null == tenantId || StringUtil.isEmpty(deviceId) || CommonUtil.isEmpty(subDeviceIdList) || null==deviceToProductMap || deviceToProductMap.isEmpty()){
            return;
        }
        try{
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    //查询设备是否已经激活，查缓存，缓存没有再查mongodb
                    if(null != subDeviceIdList && subDeviceIdList.size() > 0){
                        for(String  subDevId :  subDeviceIdList ){
                            String activatedStatusKey = RedisKeyUtils.getDevActivatedStatusKey(subDevId);
                            String activatedStatus = RedisCacheUtil.valueGet(activatedStatusKey);
                            if(null == activatedStatus || ModuleConstants.DEV_NOT_ACTIVATED.equals(activatedStatus)){
                                DeviceActiveInfo deviceActiveInfo = deviceActivateApi.getDeviceActiveInfoByUuid(subDevId);
                                if(null == deviceActiveInfo){
                                    //已激活不处理，未激活则将设备信息放入缓存待定时任务取出存入mongodb
                                    String key = RedisKeyUtils.getDevActivatedKey(CalendarUtil.getNowDate(),tenantId);
                                    GetProductInfoRespVo getProductInfoRespVo = deviceToProductMap.get(subDevId);
                                    if (null != getProductInfoRespVo && CommonUtil.isEmpty(getProductInfoRespVo.getDeviceTypeId())){
                                        RedisCacheUtil.listPush(key, new DevActivatedVo(subDevId,new Date(),deviceId,getProductInfoRespVo.getDeviceTypeId()));
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }catch (Exception e){
            log.error("",e);
        }
    }
}
