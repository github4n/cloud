import java.util.Date;
import java.util.Timer;
public class Test {

    public static void main(String[] args) {
        Timer timer = new Timer();
        Task task = new Task();
        timer.schedule(task, new Date(), 5000);
    }
}
