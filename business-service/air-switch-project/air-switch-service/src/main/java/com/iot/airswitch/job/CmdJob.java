package com.iot.airswitch.job;

import com.google.common.base.Strings;
import com.iot.airswitch.util.AnalysisDataUtil;
import com.iot.airswitch.util.HexUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Author: Xieby
 * @Date: 2018/11/6
 * @Description: *
 */
public class CmdJob implements Callable<String> {

//    private static ThreadLocal<String> result = new ThreadLocal<>();
    // 重试次数5次
//    private static ThreadLocal<Integer> retryTimes = new ThreadLocal<>();
//    private static ThreadLocal<Integer> retryTimes = ThreadLocal.withInitial( () -> 5);

    private int retryTimes = 100;
    private String result;
    private String ip;
    private Integer port;
    private String cmd;

    public CmdJob(String ip, Integer port, String cmd) {
        this.ip = ip;
        this.port = port;
        this.cmd = cmd;
    }

    @Override
    public String call() throws Exception {

        System.out.println("ip = " + ip + " & port = " + port + " & cmd = " + cmd);
//        // 设置重试次数
//        retryTimes.set(5);
        // 发送指令
        doCmd(ip, port, cmd);
        System.out.println("return result ......" + result);
        return result;
    }

    private void doCmd(String ip, Integer port, String cmd) {
        System.out.println("do cmd result = " + result + " & try retry times = " + (100-retryTimes));
        if (Strings.isNullOrEmpty(result) && retryTimes > 0) {
            retryTimes --;
            result = sendCmd(ip, port, cmd);
            doCmd(ip, port, cmd);
        }
    }

    private String sendCmd(String ip, Integer port, String cmd) {
        try (DatagramSocket socket = new DatagramSocket(10002)) {
            // 超时时间100ms
            socket.setSoTimeout(100);
            InetAddress host = InetAddress.getByName(ip);
            //指定包要发送的目的地
            byte[] buf = HexUtil.cmdToByte(cmd);
            DatagramPacket request = new DatagramPacket(buf, buf.length, host, port);
            //为接受的数据包创建空间
            DatagramPacket response = new DatagramPacket(new byte[10240], 10240);
            socket.send(request);
            System.out.println("send message >>>>>>>>>>>" + new Date());
            socket.receive(response);
            System.out.println("response ip >>>>>>>>>>>" + response.getAddress().getHostAddress());
            byte[] resultByte = response.getData();
            String resultData = HexUtil.bytesToUdpCmd(resultByte);

            System.out.println("response result >>>>>>>>" + resultData);

            List<String> dataList = AnalysisDataUtil.comminuteData(resultData);
            if (dataList.size() > 5 && dataList.get(3).equals("00")) {
                return resultData;
            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
