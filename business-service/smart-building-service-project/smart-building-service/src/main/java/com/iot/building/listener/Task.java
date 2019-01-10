package com.iot.building.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Task{

    private static final Logger log = LoggerFactory.getLogger(Task.class);

    public void run() {
        while (true){
            try {
                log.info("Task===人脸心跳检测:"+((WebSocketUtil.heartbeat == 1)?"连接":"未连接"));
                log.info("WebSocketUtil.heartbeat :"+ WebSocketUtil.heartbeat);
                if (WebSocketUtil.heartbeat ==0 ) {
                    log.info("Task=====准备人脸重新连接");
                    WebSocketUtil.connect();
                }else {
                    log.info("Task======人脸连接上了");
                    WebSocketUtil.heartbeat = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                try {
                    Thread.sleep(10000*6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}