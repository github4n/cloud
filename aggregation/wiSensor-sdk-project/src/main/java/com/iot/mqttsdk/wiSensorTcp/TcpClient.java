package com.iot.mqttsdk.wiSensorTcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

@Component
public class TcpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClient.class);
    static String ip = "192.168.8.8";
    static int port = 6666;
    public static void main(String[] args) throws InterruptedException {
        //getConnection(ip,port);
    }

    /*
  * 获取socket连接
  */
    public  Socket getConnection(String ip, int port) throws InterruptedException {
        Socket socket = null;
        boolean flag = false;
        try {
            LOGGER.info("==========================准备连接tcp==========================");
            socket = new Socket(ip, port);
            socket.setSoTimeout(30000);
            flag= true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }finally {
            LOGGER.info("=======================flag=============================:"+flag);
            if(flag){
                LOGGER.info("====================连接tcp成功============================");
                return socket;
            }else{
                LOGGER.info("====================连接tcp失败============================");
                LOGGER.info("=====================睡眠20秒=============================");
                Thread.sleep(20000);
                LOGGER.info("=====================开始重新连接tcp========================");
                getConnection(ip,port);
            }
        }
        return null;
    }

    /**
     * 只接收服务端的信息
     * @param socket
     * @return
     */
    public  String receive(Socket socket){
        InputStream in = null;
        BufferedReader br = null;
        try {
            //接收服务端信息并打印
            in = socket.getInputStream();
            //byte[] bytes = new byte[1024];
            String info=null;
            br = new BufferedReader(new InputStreamReader(in));
            //in.read(bytes);
            LOGGER.info("===================br.readLine()===================："+br.readLine());
            while((info=br.readLine())!=null){
                LOGGER.info("===================林北是客户端，服务器说================："+info);
             }
            return info;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /*
  * 发送报文并接收信息
  */
    public String send(Socket socket, String cmd) {
        InputStream in = null;
        OutputStream out = null;
        BufferedReader br = null;
        try {
            // in代表服务器到客户端的流
            in = socket.getInputStream();
            // 代表客户端到服务器的流
            out = socket.getOutputStream();
            // 输入执行命令
            PrintWriter printWriter = new PrintWriter(out, true);
            printWriter.println(cmd);
            printWriter.flush();
            // 接收执行命令结果
            br = new BufferedReader(new InputStreamReader(in));
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /*
  * 关闭socket连接
   */
    public static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
