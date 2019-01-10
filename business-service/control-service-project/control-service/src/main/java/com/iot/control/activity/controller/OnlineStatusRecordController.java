package com.iot.control.activity.controller;

import com.iot.control.activity.api.OnlineStatusRecordApi;
import com.iot.control.activity.service.OnlineStatusRecordService;
import com.iot.control.activity.vo.req.OnlineStatusRecordReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/27 15:03
 * 修改人:
 * 修改时间：
 */
@Slf4j
@RestController
public class OnlineStatusRecordController implements OnlineStatusRecordApi {

    @Autowired
    private OnlineStatusRecordService onlineStatusRecordService;

    /**
     * 线程池
     */
    private static ExecutorService excutor = Executors.newCachedThreadPool();

    public int saveOnlineStatusRecord(@RequestBody OnlineStatusRecordReq record) {
        excutor.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    onlineStatusRecordService.insert(record);
                }catch (Exception e){
                    log.error("saveBatchOnlineStatusRecord.error", e);
                }
            }
        });
        return 1;
    }

    @Override
    public int saveBatchOnlineStatusRecord(@RequestBody List<OnlineStatusRecordReq> recordReqList) {
        if (!CollectionUtils.isEmpty(recordReqList)) {
            excutor.submit(new Runnable() {
                @Override
                public void run() {
                    try{
                        for (OnlineStatusRecordReq record : recordReqList) {
                            onlineStatusRecordService.insert(record);
                        }
                    }catch (Exception e){
                        log.error("saveBatchOnlineStatusRecord.error", e);
                    }
                }
            });

        }
        return 1;
    }
}
