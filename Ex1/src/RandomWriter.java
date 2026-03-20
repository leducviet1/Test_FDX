import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomWriter implements Runnable {
    private  boolean running = true;
    public void stop(){
        running = false;
    }
    @Override
    public void run() {
        Random random = new Random();
        try(FileWriter fileWriter = new FileWriter("output.txt",true);) {
            while (running) {
                int number  = random.nextInt(100);
                fileWriter.write(String.valueOf(number));
                fileWriter.flush();
                System.out.println("Wrote number " + number);
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Thread stopped");

    }
}
