package com.iot.building.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import com.iot.building.callback.ListenerCallback;
import com.iot.building.helper.Constants;
import com.iot.building.helper.ListenerInitCallBack;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.saas.SaaSContextHolder;
import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.vo.AddJobReq;

public class SystembufferInit {

	private static Environment environment = ApplicationContextHelper.getBean(Environment.class);
	private static ScheduleApi scheduleApi = ApplicationContextHelper.getBean(ScheduleApi.class);
	private static IBuildingSpaceService buildingSpaceService = ApplicationContextHelper.getBean(IBuildingSpaceService.class);

	private static Map<String, Object> sdkMap = new HashMap<>();
	private static Map<String, ListenerCallback> listenerMap = new HashMap<>();

	public static void addListerner(String listenerId, ListenerCallback listenerCallback) {
		listenerMap.put(listenerId, listenerCallback);
	}

	public static void removeListenerMap(String listenerId) {
		listenerMap.remove(listenerId);
	}

	public static Map<String, ListenerCallback> getListenerMap() {
		return listenerMap;
	}

	public static void setListenerMap(Map<String, ListenerCallback> listenerMap) {
		SystembufferInit.listenerMap = listenerMap;
	}

	public static Map<String, Object> getSdkMap() {
		return sdkMap;
	}

	public static void init() {
//		String taskSwitch = environment.getProperty("task.switch");
//		String peopleUrl = environment.getProperty("task.url.pull-people-url");
//		String tenantId = environment.getProperty("center-control.tenantId");
		// 初始化callback
		new Thread(() -> ListenerInitCallBack.intitCallBack()).start();
		initBusinessTypeStatistic();
		// 多协议网关 初始化
		//new Thread(() -> new MultiProtocolGatewayInit().initGatewayClient()).start();
		// 定时任务
//		new Thread(() -> new TimeBuffer().init()).start();
		//人群密度
//		new Thread(() -> new DensityTask().run()).start();
		//人脸识别
//		new Thread(() -> new Task().run()).start();
		// 天气
//		ScheduleApi scheduleApi = ApplicationContextHelper.getBean(ScheduleApi.class);
//		new Thread(() -> {
//			Map<String, Object> data = Maps.newHashMap();
//			data.put("tenantId", tenantId);
//
//			AddJobReq req = new AddJobReq();
//			req.setCron("0 0 * * * ?");
//			req.setData(data);
//			req.setJobClass(ScheduleConstants.WEATHER_JOB);
//			req.setJobName(ScheduleConstants.WEATHER_JOB + 00001L);
//			scheduleApi.addJob(req);
//		}).start();

//		if (Constants.TASK_SWITCH_ON.equals(taskSwitch)) {
//			// 联动拉取签到app数据
//			new Thread(() -> {
//				String cron = environment.getProperty(Constants.TASK_LOAD_COUNT_QUARTZ);
//				if (StringUtils.isNotBlank(cron)) {
//					Map<String, Object> data = Maps.newHashMap();
//					data.put("tenantId", tenantId);
//					data.put("peopleUrl", peopleUrl);
//
//					AddJobReq req = new AddJobReq();
//					req.setCron(cron);
//					req.setData(data);
//					req.setJobClass(ScheduleConstants.PEOPLE_JOB);
//					req.setJobName(ScheduleConstants.PEOPLE_JOB + 00001L);
//
//					scheduleApi.addJob(req);
//				}
//			}).start();
			// 每天固定时间主动调用签到app
			/*
			 * new Thread(()->{ String cron =
			 * environment.getProperty(Constants.TASK_PUSH_SPACE_QUARTZ);
			 * if(StringUtils.isNotBlank(cron)) { LoadTask.pushSpaceStruct(cron, 00001L,
			 * Object.class, Constants.TASK_TYPE_SPACE_STRUCT); } }).start();
			 */
//		}
		
	}
	
	/**
	 * 设备用途统计初始化
	 */
	private static void initBusinessTypeStatistic() {
		String value=environment.getProperty(Constants.SCHEDULE_STATISTIC);
		if(StringUtils.isNotBlank(value)) {
			List<Long> tenantIds=buildingSpaceService.getLocationTenant();//获取区域的租户信息
		    if(CollectionUtils.isNotEmpty(tenantIds)) {
		    	//统计会循环所有数据，这里必须获取租户，所以有个租户即可。
		    	SaaSContextHolder.setCurrentTenantId(tenantIds.get(0));
		    	scheduleApi.delJob(ScheduleConstants.BUSINESSTYPE_STATISTIC_JOB);
		        AddJobReq jobReq = new AddJobReq();
		        jobReq.setCron(value);//"0 0/10 * * * ?"
		        jobReq.setData(null);
		        jobReq.setJobClass(ScheduleConstants.BUSINESSTYPE_STATISTIC_JOB);
		        jobReq.setJobName(ScheduleConstants.BUSINESSTYPE_STATISTIC_JOB);
		        scheduleApi.addJob(jobReq); // 保存到GRTZ_CRON_TRIGGERS
		    }
		}
	}

}
