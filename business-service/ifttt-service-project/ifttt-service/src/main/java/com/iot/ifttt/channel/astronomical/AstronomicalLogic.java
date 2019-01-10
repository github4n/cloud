package com.iot.ifttt.channel.astronomical;

import com.iot.ifttt.channel.base.ILogic;
import com.iot.ifttt.channel.timer.TimerService;
import com.iot.ifttt.entity.AppletItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：天文定时业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/28 16:21
 */
@Service
public class AstronomicalLogic implements ILogic {

    @Autowired
    private AstroClockService astroClockService;

    @Autowired
    private TimerService timerService;

    @Override
    public void add(AppletItem item) {
        //计算一次天文定时,并添加到定时服务器
        astroClockService.addSubJob(item, 0);
    }

    @Override
    public void delete(AppletItem item) {
        //从定时服务器上删除计算任务和删除子任务
        timerService.delJob(item.getId());
    }


}
