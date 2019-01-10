package com.iot.ifttt.channel.base;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.ifttt.channel.astronomical.AstronomicalLogic;
import com.iot.ifttt.channel.devstatus.DevStatusCalculator;
import com.iot.ifttt.channel.devstatus.DevStatusLogic;
import com.iot.ifttt.channel.mqmsg.MqExecutor;
import com.iot.ifttt.channel.timer.TimerLogic;
import com.iot.ifttt.channel.timerange.TimerRangeCalculator;
import com.iot.ifttt.common.IftttServiceEnum;
import com.iot.ifttt.common.IftttTypeEnum;
import com.iot.ifttt.service.IAppletItemService;
import com.iot.ifttt.service.IAppletRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 描述：适配器
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/27 20:24
 */
@Service
public class Adapter {

    @Autowired
    private IAppletRelationService appletRelationService;
    @Autowired
    private IAppletItemService appletItemService;

    /**
     * 获取校验器
     *
     * @param service
     * @return
     */
    public static ICalculator getCalculator(IftttServiceEnum service) {
        ICalculator iCalculator;
        switch (service) {
            case TIME_RANGE:
                iCalculator = ApplicationContextHelper.getBean(TimerRangeCalculator.class);
                break;
            case DEV_STATUS:
                iCalculator = ApplicationContextHelper.getBean(DevStatusCalculator.class);
                break;
            default:
                //抛异常
                iCalculator = null;
                break;
        }
        return iCalculator;

    }


    /**
     * 获取逻辑器
     *
     * @param service
     * @return
     */
    public static ILogic getLogic(IftttServiceEnum service) {
        ILogic iLogic;
        switch (service) {
            case TIMER:
                iLogic = ApplicationContextHelper.getBean(TimerLogic.class);
                break;
            case ASTRONOMICAL:
                iLogic = ApplicationContextHelper.getBean(AstronomicalLogic.class);
                break;
            case DEV_STATUS:
                iLogic = ApplicationContextHelper.getBean(DevStatusLogic.class);
                break;
            default:
                //抛异常
                iLogic = null;
                break;
        }
        return iLogic;
    }

    /**
     * 获取执行器
     *
     * @param service
     * @return
     */
    public static IExecutor getExecutor(IftttServiceEnum service) {
        IExecutor iExecutor;
        switch (service) {
            case MQ:
                iExecutor = ApplicationContextHelper.getBean(MqExecutor.class);
                break;
            default:
                //抛异常
                iExecutor = null;
                break;
        }
        return iExecutor;
    }


    /**
     * 获取关联applet
     *
     * @param type
     * @param msg
     * @return
     */
    public List<Long> getApplet(IftttTypeEnum type, String msg) {
        List<Long> appletIds = Lists.newArrayList();
        Map<Object, Object> maps = JSON.parseObject(msg, Map.class);

        switch (type) {
            case TIMER:
                Long itemId = Long.valueOf(maps.get("itemId").toString());
                Long appletId = appletItemService.getAppletIdByItem(itemId);
                if (appletId != null) {
                    appletIds.add(appletId);
                }
                break;
            case DEV_STATUS:
                String devId = (String) maps.get("devId");
                List<Long> list = appletRelationService.getAppletByKey(IftttTypeEnum.DEV_STATUS.getType(), devId);
                appletIds.addAll(list);
                break;
            default:
                break;
        }

        return appletIds;
    }

}
