package com.iot.building.listener;

import com.iot.building.helper.Constants;
import com.iot.common.helper.ApplicationContextHelper;
import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import java.net.URI;

public class WebSocketDenSityUtil {
    private static final Logger log = LoggerFactory.getLogger(WebSocketDenSityUtil.class);
    @Autowired
    private static Environment environment = ApplicationContextHelper.getBean(Environment.class);
    public static WebSocketClient client;
    public static int heartbeat = 0;// 0代表链接断开或者异常 1代表链接中.2代表正在连接
    public static String densityUrl =  environment.getProperty(Constants.DENSITY_URL);//请求的路径地址包括端口,密度

    public static void close() throws Exception {
        client.close();
    }

    public static void densityconnect() throws Exception {
        try{
            client = new DensityClient(new URI(densityUrl), new Draft_17(), null, 0);
            client.connect();
            //int count = 0;
            //heartbeat=2;
            log.info("密度+"+"client.getReadyState():"+client.getReadyState());
            if(client.getReadyState().equals(READYSTATE.OPEN)){
                log.info("密度+"+client.getReadyState()+"+已经打开");
                heartbeat=1;
            }else {
                log.info("密度+"+client.getReadyState()+"+没打开");
                heartbeat =0;
            }
            String ask = "{\"ASK\":\"1\"}";
            log.info("=======================================ask===========:"+ask);
            client.send(ask);
        }catch (Exception e){
            heartbeat=0;
        }
    }

    public static void reconnect() throws Exception {
        Thread.currentThread().sleep(15000);// 毫秒
        log.info("再次启动尝试连接");
        densityconnect();
    }

    public static void send(String txt) {
        client.send(txt);
    }

    public static void send(byte[] bytes) {
        client.send(bytes);
    }
}

