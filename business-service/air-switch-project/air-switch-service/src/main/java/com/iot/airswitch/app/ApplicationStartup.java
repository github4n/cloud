package com.iot.airswitch.app;

import com.iot.airswitch.service.AirSwitchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: Xieby
 * @Date: 2018/11/15
 * @Description: *
 */
@Configuration
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private Logger log = LoggerFactory.getLogger(ApplicationStartup.class);

    @Autowired
    private AirSwitchService airSwitchService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        // 开始计算电量和事件统计Job
        airSwitchService.startSchedule();

        // 统计空开的在线状态
        airSwitchService.startOnlineSchedule();

        // 启动UDP服务端
        airSwitchService.startup(7758);
//        airSwitchService.startup(6000);
    }
}
