import java.io.IOException;
import java.net.DatagramPacket;
import java.net.URI;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;

public class WebSocketUtil {
    /*public static void main(String[] args) throws Exception {
        Timer timer = new Timer();
        Task task = new Task();
        timer.schedule(task, new Date(), 5000);
    }*/

    public static WebSocketClient client;

    public static int heartbeat = 0;// 0代表链接断开或者异常 1代表链接中.2代表正在连接
    public static String url="ws://172.25.11.74:5921";//请求的路径地址包括端口

    /*public  void webSocketConnect(){
        Timer timer = new Timer();
        Task task = new Task();
        timer.schedule(task, new Date(), 5000);
    }*/

    public static void close() throws Exception {
        client.close();
    }

    public static void connect() throws Exception {
        client = new Client(new URI(url), new Draft_17(), null, 0);
        client.connect();
        int count = 0;
        heartbeat=2;
        while (!client.getReadyState().equals(READYSTATE.OPEN)) {
            count++;
            if (count % 1000000000 == 0) {
                System.out.println("还没有打开");
            }
        }
        client.send("发送给服务器端的信息");
    }

    public static void reconnect() throws Exception {
        Thread.currentThread().sleep(15000);// 毫秒
        System.out.println("再次启动尝试连接");
        connect();
    }

    public static void send(byte[] bytes) {
        client.send(bytes);
    }

    public static void ttt() throws Exception {
        new Thread(() -> {
            try {
                System.out.println("心跳检测:"+((WebSocketUtil.heartbeat == 1)?"连接中":"未连接中"));
                if (WebSocketUtil.heartbeat ==0 ) {
                    connect();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }).start();
    }
}

