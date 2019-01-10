import java.util.TimerTask;

public class Task extends TimerTask {
    @Override
    public void run() {
        try {
            System.out.println("心跳检测:"+((WebSocketUtil.heartbeat == 1)?"连接中":"未连接中"));
            if (WebSocketUtil.heartbeat ==0 ) {
                WebSocketUtil.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}