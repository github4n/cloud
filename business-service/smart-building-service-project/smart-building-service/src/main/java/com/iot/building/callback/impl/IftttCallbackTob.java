package com.iot.building.callback.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.iot.building.callback.ListenerCallback;
import com.iot.building.helper.Constants;
import com.iot.building.ifttt.service.IAutoTobService;
import com.iot.common.enums.APIType;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.api.DeviceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;

@Service
public class IftttCallbackTob implements ListenerCallback {

	private static final Logger log = LoggerFactory.getLogger(IftttCallbackTob.class);

	private Environment dnvironment=ApplicationContextHelper.getBean(Environment.class);

	private IAutoTobService iAutoTobService = ApplicationContextHelper.getBean(IAutoTobService.class);

	public static final String CALLBACK_KEY_PREFIX = "iftttListenerKey_";

	@Autowired
	private DeviceApi deviceApi = ApplicationContextHelper.getBean(DeviceApi.class);
	
	@Autowired
	private DeviceCoreApi deviceCoreApi = ApplicationContextHelper.getBean(DeviceCoreApi.class);

	@Autowired
	private DeviceTypeApi deviceTypeApi = ApplicationContextHelper.getBean(DeviceTypeApi.class);

	@Override
	/**
	 * device : 设备属性上报的设备
	 * map : 设备属性上报后得到的当前属性值
	 */
	public void callback(GetDeviceInfoRespVo device, Map<String, Object> map, APIType apiType) {
		try {
			log.info("==============iftttcallback============");
            //筛选功能类型Door,OnOff,Alarm,Occupancy,目前就先这些，以后有新的就在配置文件中添加
			String functionType = dnvironment.getProperty(Constants.functionType);
			log.info("==============iftttcallback=======functionType====="+functionType);
			String[] functionTypeStr = functionType.split(",");
			Map<String, Object> back =new HashMap<String, Object>();
			if(MapUtils.isNotEmpty(map)) {
				for(String key:map.keySet()) {
					if(map.get(key)==null || key.equals("tenantId")) {
						continue;
					}
					back.put(key, map.get(key));
				}
			}
			for(String str : back.keySet()){
				if(str.equals("tenantId")){
					continue;
				}
				boolean functionFlag = Arrays.asList(functionTypeStr).contains(str);
				if(!functionFlag){
					return;
				}
			}
			//判断是不是sensor,是的话就继续执行，不是的就return
			//配置文件中写的sensor类型
			String isSensorType = dnvironment.getProperty(Constants.isSensorType);
			log.info("==============iftttcallback=======isSensorType====="+isSensorType);
			String[] isSensorTypeStr = isSensorType.split(",");
			//获取该设备的设备类型，通过该设备的deviceId 查找对应的 deviceTypeId,再通过deviceTypeId去device_type表查找type
			Long deviceTypeId = deviceCoreApi.get(device.getUuid()).getDeviceTypeId();
			log.info("==============iftttcallback=======deviceTypeId====="+deviceTypeId);
			String devType = deviceTypeApi.getDeviceTypeById(deviceTypeId).getType();
			log.info("==============iftttcallback======devType======"+devType);
			log.info("==============iftttcallback======isSensorTypeStr============"+isSensorTypeStr.toString());
			boolean deviceTypeFlag = Arrays.asList(isSensorTypeStr).contains(devType);
			log.info("==============iftttcallback======deviceTypeFlag======"+deviceTypeFlag);
			if(!deviceTypeFlag){//不是的就return
				return;
			}else {//判断是不是sensor,是的话就继续执行
				log.info("==============iftttcallback======check======");
				Long start = System.currentTimeMillis();
				log.info("============start========= "+start);
				if (device == null || MapUtils.isEmpty(back)) {
					log.warn("ifttt callback returned, param error");
					return;
				}
				String deviceId = device.getUuid();
				log.info("==============iftttcallback======deviceId======"+deviceId);
                //条件判断
				iAutoTobService.checkByDevice(deviceId,back);
				Long end = System.currentTimeMillis();
				log.info("============en ========= "+end+" : "+(end-start)+" : "+deviceId);
			}
		} catch (BusinessException e) {
			log.error("ifttt callback failed", e);
		} catch (Exception e) {
			log.error("ifttt callback failed", e);
		}
	}

}
