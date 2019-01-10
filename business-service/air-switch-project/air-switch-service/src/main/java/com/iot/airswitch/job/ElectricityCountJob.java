package com.iot.airswitch.job;

import com.iot.airswitch.service.AirSwitchService;
import com.iot.common.helper.ApplicationContextHelper;

import java.util.Date;

/**
 * @Author: Xieby
 * @Date: 2018/11/15
 * @Description: *
 */
public class ElectricityCountJob implements Runnable {

    private String info;

    public ElectricityCountJob(String info) {
        this.info = info;
    }

    @Override
    public void run() {
        System.out.println("----------------- save electricity start -------------------" + new Date());
        AirSwitchService airSwitchService = ApplicationContextHelper.getBean(AirSwitchService.class);
        airSwitchService.saveElectricity(info);
        System.out.println("----------------- save electricity end -------------------" + new Date());
    }
}
