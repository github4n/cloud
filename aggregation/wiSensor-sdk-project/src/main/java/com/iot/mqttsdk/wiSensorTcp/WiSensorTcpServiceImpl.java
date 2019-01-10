package com.iot.mqttsdk.wiSensorTcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.Socket;

@Service("WiSensorTcpService")
public class WiSensorTcpServiceImpl implements WiSensorTcpService{

    private static final Logger logger = LoggerFactory.getLogger(WiSensorTcpServiceImpl.class);

    @Autowired
    private TcpClient tcpClient;

    @Override
    public String recevie(String ip, int port) {
        String receiveMsg = null;
        try {
            Socket socket = tcpClient.getConnection(ip,port);
            if (socket !=null){
                receiveMsg = tcpClient.receive(socket);
                return receiveMsg;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
