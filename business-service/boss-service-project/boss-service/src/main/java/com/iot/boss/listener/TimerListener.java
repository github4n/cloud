package com.iot.boss.listener;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.iot.boss.service.malf.MalfRecordService;
import com.iot.boss.service.malf.WarningDispatchService;
import com.iot.common.util.QuartzManager;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：定时器监听器
 * 功能描述：定时器监听器
 * 创建人： 李帅
 * 创建时间：2018年5月17日 下午6:56:09
 * 修改人：李帅
 * 修改时间：2018年5月17日 下午6:56:09
 */
@Configuration
public class TimerListener{
	
	@Autowired
    private WarningDispatchService warningDispatchService;
	
	@Autowired
    private MalfRecordService malfRecordService;
	
	@PostConstruct
    public void onApplicationEvent() {
    	 QuartzManager quartzManager= QuartzManager.getInstall();
    	 Map<String,Object> paramMap=new HashMap<String,Object>();
    	 paramMap.put("warningDispatchService",warningDispatchService);
    	 paramMap.put("malfRecordService",malfRecordService);
    	 //报障处理进度定时处理
    	 quartzManager.addJob("malfRecord","boss",MalfRecordScheduleJob.class,paramMap,"0 12/5 * * * ? ");
    }
    
}
