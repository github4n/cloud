package com.iot.airswitch;

import com.google.common.primitives.Bytes;
import com.iot.airswitch.util.HexUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

/**
 * @Author: Xieby
 * @Date: 2018/10/18
 * @Description: *
 */
public class UDPTest {

//    private static String netAddress = "255.255.255.255";//服务端的ip地址
    private static String netAddress = "172.16.28.230";//服务端的ip地址
    private static Integer netPortNum = 5918;//服务端的端口
    private static String gatewayMsg = "";
    private static final Integer MAX_LENGTH = 1024; // 最大接收字节长度

    public static void main(String[] args) {

//        String cmd = "F1 B3 01 00 00 7C 00 01 98 CC 4D 10 00 B9 10 80 00 8A 54 33 10 05 98 CC 4D 10 00 B9 00 07 C0 A8 89 F0 FF FF FF 00 C0 A8 89 01 78 4D E2 52 1E 4E DF 06 06 06 00 78 17 1E 10 FF FF FF FF 7A 57 A5 A7 43 89 4A E4 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 63 65 73 68 69 32 00 FF FF FF FF FF FF FF FF FF FF FF FF FF 6D 61 6E 64 75 6E 39 39 39 39 00 FF FF FF FF FF FF FF FF FF E9 F5 7B 05";
//        String cmd = "F1 B1 00 00 00 00 00 01 5B 39 67 5E FF FF FF 80 54 8C 17 FF";
//        String cmd = "F1 D3 D7 00 00 06 00 01 5B CF DB 7D FF FF FF 80 C0 A8 01 2E 1F 96 EF A9 78 BC";
//        String cmd = "F1D3D700000600015B39675EFFFFFF80C0A801291F963E0715C4";
//        String cmd = "F1 B1 00 00 00 00 00 01 5c 05 d5 26 FF FF FF 80 AF 2A C8 B0";
        String cmd = "F1D3A100000900ce5c1b62b5FFFFFF00000000000000000002439086f4";
//        String cmd = "F1D3A2000009000b5c1b62afFFFFFF00000000000000000001e8e5ff82";
        String result = udpSend(cmd);
        System.out.println("result = " + result);

    }

    public static String udpSend(String cmd) {

        String result = "0";

//        while (result.equals("0")) {
            //传入0表示让操作系统分配一个端口号
            try (DatagramSocket socket = new DatagramSocket(10002)) {
                socket.setSoTimeout(2000);
                InetAddress host = InetAddress.getByName(netAddress);
                //指定包要发送的目的地
//            String tS = "f1 b1 00 00 00 00 00 01 5b 39 67 5e ff ff ff 80 54 8c 17 ff";
//                String tS = "F1 B1 00 00 00 00 00 01 5B 39 67 5E FF FF FF 80 54 8C 17 FF";
//                byte[] buf = new byte[0];
//                for (String t : cmd.split(" ")) {
//                    buf = Bytes.concat(buf, HexUtil.toBytes(t));
//                }
                byte[] buf = HexUtil.cmdToByte(cmd);
                DatagramPacket request = new DatagramPacket(buf, buf.length, host, netPortNum);
                //为接受的数据包创建空间
                DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
                socket.send(request);
                System.out.println("send message >>>>>>>>>>>" + new Date());
                socket.receive(response);
                System.out.println("response ip >>>>>>>>>>>" + response.getAddress().getHostAddress());
                byte[] resultByte = response.getData();
                result = HexUtil.bytesToHex(resultByte);
//                return HexUtil.bytesToHex(resultByte);
//            String result = new String(response.getData(), 0, response.getLength(), "ASCII");
            } catch (IOException e) {
                System.out.println("error............");
                result = "0";
            }
//        }

        return result;

    }

//    public static String receive(){
//        try {
//            InetAddress address = InetAddress.getByName(netAddress);
//            final DatagramSocket detectSocket = new DatagramSocket(59118);
//            System.out.println("Receive started.");
//            new Thread(() -> {
////                byte[] buf = new byte[MAX_LENGTH];
//                String tS = "f1 b1 00 00 00 00 00 01 5b 39 67 5e ff ff ff 80 54 8c 17 ff";
//                byte[] buf = new byte[0];
//                for (String t : tS.split(" ")) {
//                    buf = Bytes.concat(buf, HexUtil.toBytes(t));
//                }
//                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 5918);
//                int a = 0;
//                try {
//                    while (true) {
//                        detectSocket.receive(packet);
//                        gatewayMsg = new String(packet.getData(), 0, packet.getLength(), "GBK");
//                        System.out.println(gatewayMsg);
//                        a++;
//                        if (a == 1) {
//                            break;
//                        }
//                        System.out.println("Receive finished.");
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//
//            Thread.sleep(10000);
//            detectSocket.close();
//            return gatewayMsg;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return null;
//    }
}
