package com.iot.ifttt.controller;

import com.iot.ifttt.api.IftttApi;
import com.iot.ifttt.channel.astronomical.AstroClockService;
import com.iot.ifttt.service.impl.IftttServiceImpl;
import com.iot.ifttt.vo.AppletVo;
import com.iot.ifttt.vo.CheckAppletReq;
import com.iot.ifttt.vo.SetEnableReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class IftttController implements IftttApi {

    private final Logger logger = LoggerFactory.getLogger(IftttController.class);

    @Autowired
    private IftttServiceImpl iftttService;

    @Autowired
    private AstroClockService astroClockService;

    /**
     * 线程池
     */
    private static ExecutorService excutor = Executors.newCachedThreadPool();


    @Override
    public void check(@RequestBody CheckAppletReq req) {
        excutor.submit(new Runnable() {
            @Override
            public void run() {
                Long st = System.currentTimeMillis();
                logger.info("=== check applet req ===" + req.toString());
                iftttService.check(req);
                Long et = System.currentTimeMillis();
                logger.debug("IFTTT检测时间用时：" + (et - st) + "毫秒！");
            }
        });

    }

    @Override
    public Long save(@RequestBody AppletVo req) {
        logger.info("=== save applet req ===" + req.toString());
        return iftttService.save(req);
    }

    @Override
    public void setEnable(@RequestBody SetEnableReq req) {
        logger.info("=== set applet enable req ===" + req.toString());
        iftttService.setEnable(req);
    }

    @Override
    public AppletVo get(@RequestParam("id") Long id) {
        logger.info("=== get applet req ===" + id);
        return iftttService.get(id);
    }

    @Override
    public Boolean delete(@RequestParam("id") Long id) {
        logger.info("=== delete applet req ===" + id);
        return iftttService.delete(id);
    }

    @Override
    public void countAstroClockJob() {
        logger.info("=== count astroClock job req ===");
        astroClockService.countAstroClockJob();
    }

    @Override
    public void delItem(@RequestParam("itemId") Long itemId) {
        logger.info("=== delete applet item req ===" + itemId);
        iftttService.delItem(itemId);
    }
}
