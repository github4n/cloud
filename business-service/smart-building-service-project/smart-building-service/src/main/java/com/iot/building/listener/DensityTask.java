package com.iot.building.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DensityTask {

    private static final Logger log = LoggerFactory.getLogger(DensityTask.class);

    public void run() {

        while (true){
            try {
                log.info("DensityTask====密度心跳检测:" + ((WebSocketDenSityUtil.heartbeat == 1) ? "连接" : "未连接"));
                log.info("WebSocketUtil.heartbeat :" + WebSocketUtil.heartbeat);
                String ask = "{\"ASK\":\"1\"}";
                if (WebSocketDenSityUtil.heartbeat == 0) {
                    log.info("DensityTask====准备密度重新连接");
                    WebSocketDenSityUtil.densityconnect();
                } else {
                    log.info("DensityTask====密度发送消息");
                    WebSocketDenSityUtil.send(ask);
                    WebSocketUtil.heartbeat=1;
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