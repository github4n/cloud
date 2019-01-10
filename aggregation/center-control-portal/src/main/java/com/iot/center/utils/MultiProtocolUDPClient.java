package com.iot.center.utils;
import com.alibaba.fastjson.JSONObject;
import com.iot.center.vo.AirConditionVO;
import com.iot.common.util.StringUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * UDP通信协议
 */
public class MultiProtocolUDPClient {
    public static void main(String[] args){
        System.out.println("------开始------");
        String test = receive();
        System.out.println("测试："+test);
        System.out.println("sssssss");
        //System.out.println("\\n");
        //String str =  "[{\"atuId\":\"7CDD90A494C9\",\"atuName\":\"空调2\",\"atuPwd\":\"jqgbkzki\",\"atuRoom\":\"办公区\",\"sortLetters\":\"BK\"}]";
        /*String str = "[{\"atuId\":\"7CDD90A46619\",\"atuName\":\"空调1\",\"atuPwd\":\"gmjqrhmm\",\"atuRoom\":\"502\",\"sortLetters\":\"#K\"},{\"atuId\":\"7CDD90A494C9\",\"atuName\":\"空调2\",\"atuPwd\":\"jqgbkzki\",\"atuRoom\":\"办公区\",\"sortLetters\":\"BK\"}][{\"atuId\":\"7CDD90A46619\",\"atuName\":\"空调1\",\"atuPwd\":\"gmjqrhmm\",\"atuRoom\":\"502\",\"sortLetters\":\"#K\"},{\"atuId\":\"7CDD90A494C9\",\"atuName\":\"空调2\",\"atuPwd\":\"jqgbkzki\",\"atuRoom\":\"办公区\",\"sortLetters\":\"BK\"}][{\"atuId\":\"7CDD90A46619\",\"atuName\":\"空调1\",\"atuPwd\":\"gmjqrhmm\",\"atuRoom\":\"502\",\"sortLetters\":\"#K\"},{\"atuId\":\"7CDD90A494C9\",\"atuName\":\"空调2\",\"atuPwd\":\"jqgbkzki\",\"atuRoom\":\"办公区\",\"sortLetters\":\"BK\"}]";
        Map<String,String> map = new HashMap<String,String>();
        Set<String> set = (Set<String>) new HashSet<String>();
        List<String> list = new ArrayList<String>();
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
            obj.add(air);
        }*/
        //String aaaa = map.get("atuId");
        //sendMessage();
        //recover();
        System.out.println("------结束------");
    }
    private static String gatewayMsg = "";
    private static final Integer MAX_LENGTH = 1024; // 最大接收字节长度
    private static String sendMessage = "InCloud,7CDD90A494C9,admin_jqgbkzki,rdRoom,办公区,\n";//客户端发送的消息
    private static String netAddress = "120.24.48.245";//服务端的ip地址
    private static String clientAddress = "127.0.0.1";//客户端的地址
    private static Integer netPortNum = 11093;//服务端的端口
    private static Integer clientPortNum = 19998;//客户端的端口

    public static Boolean  recover(byte[] sendBuf) {
        //接收数据
        try {
            //初始化DatagramSocket
            //final DatagramSocket detectSocket = new DatagramSocket(clientPortNum);
            final DatagramSocket detectSocket = new DatagramSocket();
            new Thread(() -> {
                System.out.println("Receive thread started.");
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
                    System.out.println(gatewayMsg);
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
            System.out.println("Send to " + hostAddress);
            // 使用DatagramPacket(byte buf[], int length, InetAddress address, int port)函数组装发送UDP数据报
            DatagramPacket out = new DatagramPacket(sendBuf, sendBuf.length, hostAddress, netPortNum);
            detectSocket.send(out);
            Thread.sleep(10000L);
            if(StringUtil.isNotBlank(gatewayMsg)){
                detectSocket.close();
                return true;
            }else {
                detectSocket.close();
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
            System.out.println("Receive started.");
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
                        System.out.println("Receive finished.");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            Thread.sleep(10000);
            detectSocket.close();
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

}
