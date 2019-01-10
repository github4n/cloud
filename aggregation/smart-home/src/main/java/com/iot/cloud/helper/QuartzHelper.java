package com.iot.cloud.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * 定时辅助
 *
 * @author fenglijian
 */
@Component
public class QuartzHelper {
    private static final Log LOGGER = LogFactory.getLog(QuartzHelper.class);

    public static void init() {
        //定时
        new Thread(() -> {
			/*TimeTaskHelper helper = new TimeTaskHelper();
			helper.init();*/
        }).start();
        //天气
        //new Thread(()->LoadTask.weatherTask("0 0 * * * ?", 00001L, Object.class, "weather")).start();
    }
}
