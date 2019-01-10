package com.iot.building.gateway;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.iot.building.common.vo.AirConditionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.iot.common.util.StringUtil;

/**
 * UDP通信协议
 */
public class MultiProtocolUDPClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiProtocolUDPClient.class);
    public static void main(String[] args){
        System.out.println("------开始------");
        String test = "Command=空调2,1A,1B,20,10,20,10,01,62,51,02,00,18,00,00,20,20,;";
        String aaa = test.substring(8,test.length());
        getMap(test);
        System.out.println("sssssss");
        System.out.println("------结束------");
    }
    private static String gatewayMsg = "";
    private static final Integer MAX_LENGTH = 1024; // 最大接收字节长度
    private static String sendMessage = "InCloud,7CDD90A494C9,admin_jqgbkzki,rdRoom,办公区,\n";//客户端发送的消息
    private static String netAddress = "120.24.48.245";//服务端的ip地址
    private static String clientAddress = "127.0.0.1";//客户端的地址
    private static Integer netPortNum = 11093;//服务端的端口
    private static Integer clientPortNum = 19998;//客户端的端口

    public static Map<String,Object>  recoverAirCondition(byte[] sendBuf) {
        //接收数据
        try {
            //初始化DatagramSocket
            //final DatagramSocket detectSocket = new DatagramSocket(clientPortNum);
            final DatagramSocket detectSocket = new DatagramSocket();
            new Thread(() -> {
                LOGGER.info("Receive thread started.");
                int a = 0;
                while (true) {
                    byte[] buf = new byte[MAX_LENGTH];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    try {
                        detectSocket.receive(packet);
                        gatewayMsg = new String(packet.getData(), 0, packet.getLength(),"GBK");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LOGGER.info(gatewayMsg);
                    //gatewayMsg = gatewayMsg + "->==->";
                    a++;
                    if (a ==10) {
                        break;
                    }
                }
            }).start();


            // 发送数据
            InetAddress hostAddress = InetAddress.getByName(netAddress);
            //byte[] sendBuf = new byte[MAX_LENGTH];
            //sendBuf = "InCloud,7CDD90A494C9,admin_jqgbkzki,rdRoom,办公区,\n".getBytes("GBK");
            LOGGER.info("Send to " + hostAddress);
            // 使用DatagramPacket(byte buf[], int length, InetAddress address, int port)函数组装发送UDP数据报
            DatagramPacket out = new DatagramPacket(sendBuf, sendBuf.length, hostAddress, netPortNum);
            detectSocket.send(out);
            Thread.sleep(10000L);
        } catch (SocketException e1) {
            e1.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    return getMap(gatewayMsg.substring(8,gatewayMsg.length()));

}


    public static Boolean  recover(byte[] sendBuf) {
        //接收数据
        try {
            //初始化DatagramSocket
            //final DatagramSocket detectSocket = new DatagramSocket(clientPortNum);
            final DatagramSocket detectSocket = new DatagramSocket();
            new Thread(() -> {
                LOGGER.info("Receive thread started.");
                int a = 0;
                while (true) {
                    byte[] buf = new byte[MAX_LENGTH];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    try {
                        detectSocket.receive(packet);
                        gatewayMsg = new String(packet.getData(), 0, packet.getLength(),"GBK");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LOGGER.info(gatewayMsg);
                    gatewayMsg = gatewayMsg + "->==->";
                    a++;
                    if (a > 100) {
                        break;
                    }
                }
            }).start();


            // 发送数据
            InetAddress hostAddress = InetAddress.getByName(netAddress);
            //byte[] sendBuf = new byte[MAX_LENGTH];
            //sendBuf = "InCloud,7CDD90A494C9,admin_jqgbkzki,rdRoom,办公区,\n".getBytes("GBK");
            LOGGER.info("Send to " + hostAddress);
            // 使用DatagramPacket(byte buf[], int length, InetAddress address, int port)函数组装发送UDP数据报
            DatagramPacket out = new DatagramPacket(sendBuf, sendBuf.length, hostAddress, netPortNum);
            detectSocket.send(out);
            Thread.sleep(10000L);
            if(StringUtil.isNotBlank(gatewayMsg)){
                //detectSocket.close();
                return true;
            }else {
                //detectSocket.close();
                return false;
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }


    public static String receive(){
        try {
            final DatagramSocket detectSocket = new DatagramSocket(clientPortNum);
            LOGGER.info("Receive started.");
            new Thread(() -> {
                byte[] buf = new byte[MAX_LENGTH];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                int a = 0;
                try {
                    while (true) {
                        detectSocket.receive(packet);
                        gatewayMsg = new String(packet.getData(), 0, packet.getLength(), "GBK");
                        System.out.println(gatewayMsg);
                        a++;
                        if (a ==1) {
                            break;
                        }
                        LOGGER.info("Receive finished.");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            //detectSocket.close();
            Thread.sleep(10000);
            return gatewayMsg;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static List<AirConditionVO> toJavaBean(String str){
        Map<String,String> map = new HashMap<String,String>();
        Set<String> set = (Set<String>) new HashSet<String>();
        List<String> list = new ArrayList<String>();
        List<AirConditionVO> obj = new ArrayList<AirConditionVO>();
        String a = str.replace("[","");
        String[] b = a.split("]");
        for (int i = 0;i<b.length;i++){
            String[] c = b[i].split("},");
            for(int j = 0;j<c.length;j++){
                if(j==0){
                    list.add(c[j] + "}");
                }else {
                    list.add(c[j]);
                }
            }
        }
        set.addAll(list);
        for(String strTest:set){
            JSONObject jsonObject = JSONObject.parseObject(strTest);
            //JSONArray jsonArray = JSONArray.parseArray(strTest);
            //JSONObject jsonObject = jsonArray.getJSONObject(0);
            map = JSONObject.toJavaObject(jsonObject,Map.class);
            AirConditionVO air = new AirConditionVO(map.get("atuId"),map.get("atuName"),map.get("atuPwd"),map.get("atuPwd"),map.get("sortLetters"));
            obj.add(air);
        }
        return obj;
    }

    public static Map<String,Object> getMap(String str){
        String[] a = str.split(",");
        String b1 = a[1].toLowerCase();
        /**
         * 16   17   18   19  20  21  22   23   24   25   26  27   28  29   30    十进制

           10   11   12   13  14  15  16   17   18   19   1a  1b   1c  1d   1e    十六进制
         */
        if(b1.equals("10")){
             b1 = "16";
        }else if(b1.equals("11")){
            b1 = "17";
        }else if(b1.equals("12")){
            b1 = "18";
        }else if(b1.equals("13")){
            b1 = "19";
        }else if(b1.equals("14")){
            b1 = "20";
        }else if(b1.equals("15")){
            b1 = "21";
        }else if(b1.equals("16")){
            b1 = "22";
        }else if(b1.equals("17")){
            b1 = "23";
        }else if(b1.equals("18")){
            b1 = "24";
        }else if(b1.equals("19")){
            b1 = "25";
        }else if(b1.equals("1a")){
            b1 = "26";
        }else if(b1.equals("1b")){
            b1 = "27";
        }else if(b1.equals("1c")){
            b1 = "28";
        }else if(b1.equals("1d")){
            b1 = "29";
        }else if(b1.equals("1e")){
            b1 = "30";
        }
        String b2 = a[7];
        String b3 = a[8];
        String b4 = a[9].substring(0,1);
        String b5 = a[9].substring(1);
        Map<String,Object> ww = new HashMap<String,Object>();
        ww.put("OnOffStatus",b2);
        ww.put("temperature",b1);
        ww.put("mode",b3);
        ww.put("windSpeed",b4);
        ww.put("windStatus",b5);
        return  ww;
    }

}
