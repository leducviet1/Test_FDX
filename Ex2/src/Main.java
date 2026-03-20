import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        RandomInput randomInput = new RandomInput();
        Thread thread = new Thread(randomInput);
        thread.start();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                randomInput.stop();
                System.out.println("Stopped");
            }
        },10000);
    }
}