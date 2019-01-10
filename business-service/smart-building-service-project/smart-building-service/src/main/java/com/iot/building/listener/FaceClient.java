package com.iot.building.listener;

import com.alibaba.fastjson.JSON;
import com.iot.building.camera.service.ICameraService;
import com.iot.building.camera.vo.req.CameraReq;
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

public class FaceClient extends WebSocketClient{

    private static final Logger log = LoggerFactory.getLogger(FaceClient.class);

    public FaceClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

   private static ICameraService iCameraService = ApplicationContextHelper.getBean(ICameraService.class);
    @Autowired
    private static Environment environment = ApplicationContextHelper.getBean(Environment.class);
    public static String userId = environment.getProperty(Constants.AGENT_MQTT_USERNAME);
    String picUrlPath = environment.getProperty(Constants.LOCAL_FILE_URL_PATH);
    String sceneId = environment.getProperty(Constants.LOCAL_FILE_SCENEID);

    @Override
    public void onOpen(ServerHandshake arg0) {
        log.info("人脸打开链接");
        WebSocketUtil.heartbeat=1;
    }

    @Override
    public void onMessage(String arg0) {
        if(arg0!=null){
            log.info("人脸收到消息" + arg0);
            //一般在这执行你需要的业务,通常返回JSON串然后解析做其他操作
            //解析json字符串
            CameraReq cameraReq = JSON.parseObject(arg0, CameraReq.class);
            //保存对象到camera_record
            cameraReq.setCreateTime(new Date());
            String url = "";
            try {
                url = iCameraService.save(cameraReq);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //发送mqtt给前端
            log.info("====人脸发送mqtt准备开始====");
            MqttMsg msg = new MqttMsg();
            String topic = "iot/v1/c/"+userId+"/center/peopleFace";
            msg.setTopic(topic);
            msg.setService("center");
            msg.setMethod("peopleFace");
            msg.setSeq("123456");
            msg.setSrcAddr("123456789");
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id",cameraReq.getId());
            map.put("name",cameraReq.getName());
            map.put("title",cameraReq.getTime());
            map.put("time",cameraReq.getTime());
            map.put("register",cameraReq.getRegister());
            //map.put("url",picUrlPath);
            map.put("url",url);
            msg.setPayload(map);
            BusinessDispatchMqttHelper.sendPeopleFaceResp(msg);
            log.info("====人脸发送mqtt结束====");
        }else {
            log.info("====人脸没有收到消息====");
            WebSocketUtil.heartbeat=0;
        }
    }

    @Override
    public void onError(Exception arg0) {
        log.info("人脸发生错误已关闭,正在重连");
        WebSocketUtil.heartbeat=0;
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        log.info("人脸链接已关闭,正在重连");
        WebSocketUtil.heartbeat=0;
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        try {
            log.info(new String(bytes.array(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("出现异常");
            WebSocketUtil.heartbeat=0;
        }
    }

}
