package com.iot.airswitch;

import com.alibaba.fastjson.JSON;
import com.iot.airswitch.util.AnalysisDataUtil;
import com.iot.airswitch.util.HexUtil;
import com.iot.airswitch.util.PackageCmdUtil;
import com.iot.airswitch.vo.CommToServerVo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Date;
import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/10/19
 * @Description: *
 */
public class UDPServer {

    private final static int PORT = 8086;

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            while (true) {
                try {
                    DatagramPacket request = new DatagramPacket(new byte[512], 512);
                    socket.receive(request);
//                    System.out.println("start date = " + new Date());
                    System.out.println("start data = " + HexUtil.bytesToUdpCmd(request.getData()));
                    List<String> dataList = AnalysisDataUtil.comminuteData(HexUtil.bytesToUdpCmd(request.getData()));
                    CommToServerVo vo = AnalysisDataUtil.handleData(HexUtil.bytesToUdpCmd(request.getData()));
//                    System.out.println("vo = " + JSON.toJSONString(vo));
                    String currentDate = HexUtil.getCurrentDateToHex();
                    System.out.println("ip = "+ request.getAddress().getHostAddress() +" & message type = "
                            + dataList.get(1) + " & date = " + currentDate + " & vo =" + JSON.toJSONString(vo));
//                    StringBuffer sb = new StringBuffer();
//                    sb.append(pre).append(messageId).append(currentDate).append(ped);
//                    String crc = CRCUtil.crc32Cal(HexUtil.cmdToByte(sb.toString()));
//                    sb.append(crc);
                    String cmdResp = PackageCmdUtil.packageResponse(dataList);
                    byte[] data = HexUtil.cmdToByte(cmdResp);
                    DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
                    socket.send(response);
                    System.out.println("end >>>>>>>>>>>>>>>>" + HexUtil.bytesToHex(response.getData()) + " & date" + new Date());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
