package com.iot.building.listener;

import com.alibaba.fastjson.JSON;
import com.iot.building.camera.service.ICameraService;
import com.iot.building.camera.vo.req.CameraPropertyReq;
import com.iot.building.helper.Constants;
import com.iot.building.mqtt.BusinessDispatchMqttHelper;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.mqttsdk.common.MqttMsg;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DensityClient extends WebSocketClient{
    private static final Logger log = LoggerFactory.getLogger(DensityClient.class);

    public DensityClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

   private static ICameraService iCameraService = ApplicationContextHelper.getBean(ICameraService.class);
    @Autowired
    private static Environment environment = ApplicationContextHelper.getBean(Environment.class);
    public static String userId = environment.getProperty(Constants.AGENT_MQTT_USERNAME);

    @Override
    public void onOpen(ServerHandshake arg0) {
        log.info("人群密度打开链接");
        WebSocketUtil.heartbeat=1;
    }

    @Override
    public void onMessage(String arg0) {
        if(arg0!=null){
            log.info("人群密度收到消息" + arg0);
            //一般在这执行你需要的业务,通常返回JSON串然后解析做其他操作
            //解析json字符串
            CameraPropertyReq cameraPropertyReq = JSON.parseObject(arg0, CameraPropertyReq.class);
            cameraPropertyReq.setCreateTime(new Date());
            //保存密度对象到region_record
            iCameraService.saveDensity(cameraPropertyReq);

            //保存/修改密度对象到region_hour_record
            iCameraService.saveRegionHourRecord(cameraPropertyReq);

            //发送mqtt给前端
            log.info("====人群密度发送mqtt准备开始====");
            MqttMsg msg = new MqttMsg();
            String topic = "iot/v1/c/"+userId+"/center/peopleDensity";
            msg.setTopic(topic);
            msg.setService("center");
            msg.setMethod("peopleDensity");
            msg.setSeq("123456");
            msg.setSrcAddr("123456789");
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("ch1",cameraPropertyReq.getCh1());
            map.put("ch2",cameraPropertyReq.getCh2());
            map.put("ch3",cameraPropertyReq.getCh3());
            map.put("ch4",cameraPropertyReq.getCh4());
            map.put("time",cameraPropertyReq.getTime());
            msg.setPayload(map);
            BusinessDispatchMqttHelper.sendPeopleFaceResp(msg);
            log.info("====人群密度发送mqtt结束====");
        }else {
            log.info("====人群密度没有收到消息====");
            WebSocketUtil.heartbeat=0;
        }
    }

    @Override
    public void onError(Exception arg0) {
        //log.info("人群密度发生错误已关闭,正在重连");
        WebSocketUtil.heartbeat=0;
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        //log.info("人群密度链接已关闭,正在重连");
        WebSocketUtil.heartbeat=0;
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        try {
            log.info(new String(bytes.array(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.info("出现异常");
            WebSocketUtil.heartbeat=0;
        }
    }

}
