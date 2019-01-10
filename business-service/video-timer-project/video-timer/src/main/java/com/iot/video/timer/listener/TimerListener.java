package com.iot.video.timer.listener;

import com.iot.common.util.QuartzManager;
import com.iot.device.api.DeviceCoreApi;
import com.iot.file.api.FileApi;
import com.iot.file.api.VideoFileApi;
import com.iot.message.api.MessageApi;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.shcs.ipc.api.IpcApi;
import com.iot.system.api.LangApi;
import com.iot.user.api.UserApi;
import com.iot.video.api.VideoManageApi;
import com.iot.video.api.VideoTimerApi;
import com.iot.video.timer.config.JobTimeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：视频模块
 * 功能描述：定时器监听器
 * 创建人： zhouzongwei
 * 创建时间：2017年10月31日 下午6:14:31
 * 修改人： zhouzongwei
 * 修改时间：2017年10月31日 下午6:14:31
 */
@Configuration
public class TimerListener{// implements ApplicationListener<ContextRefreshedEvent> {
	
	private static int count=0;

	@Autowired
    private VideoTimerApi videoTimerApi;
	
	@Autowired
    private MessageApi messageApi;
	
	@Autowired
    private FileApi fileApi;

	@Autowired
	private VideoFileApi videoFileApi;
    
	@Autowired
	private IpcApi ipcApi;

	@Autowired
    private UserApi userApiService;

	@Autowired
    private DeviceCoreApi deviceCoreApi;

	@Autowired
	private VideoManageApi videoManageApi;

	@Autowired
	private GoodsServiceApi goodsServiceApi;

	@Autowired
	private LangApi langApi;

	@Value("${home.page.url}")
	private String homePageUrl;

	@Resource
	private JobTimeConfig jobTimeConfig;
	
	@PostConstruct
    public void onApplicationEvent() {
    	 QuartzManager quartzManager= QuartzManager.getInstall();
    	 Map<String,Object> paramMap=new HashMap<String,Object>();
    	 paramMap.put("videoTimerApi",videoTimerApi);
    	 paramMap.put("messageApi",messageApi);
    	 paramMap.put("fileApi",fileApi);
    	 paramMap.put("ipcApi",ipcApi);
		 paramMap.put("videoManageApi",videoManageApi);
    	 paramMap.put("userApiService",userApiService);
    	 paramMap.put("deviceCoreApi",deviceCoreApi);
		 paramMap.put("homePageUrl",homePageUrl);
		 paramMap.put("videoFileApi",videoFileApi);
		 paramMap.put("goodsServiceApi", goodsServiceApi);
		 paramMap.put("langApi", langApi);
		 //已使用schedule-service服务  后面将废弃此服务
    	// quartzManager.addJob("updateEvent","video",UpdateEventScheduleJob.class,paramMap,jobTimeConfig.getUpdateEvent());
    	 //quartzManager.addJob("updateHour","video",UpdateHourScheduleJob.class,paramMap,jobTimeConfig.getUpdateHour());
    	 //quartzManager.addJob("updatePlan","video",UpdatePlanScheduleJob.class,paramMap,jobTimeConfig.getUpdatePlan());
    	 //quartzManager.addJob("dealInvalidVideoData","video",DealInvalidVideoDataScheduleJob.class,paramMap,jobTimeConfig.getDealInvalidVideoData());
		 //quartzManager.addJob("dealUnUploadFileInfo","video",DealUnUploadFileInfoScheduleJob.class,paramMap,jobTimeConfig.getDealUnUploadFileInfo());
    	 System.out.println("cout="+ (++count));
    }
    
}
