import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomInput implements Runnable {
    private boolean isActive = true;
    public void stop(){
        isActive = false;
    }
    @Override
    public void run() {
        Random rand = new Random();
        try(FileWriter fileWriter = new FileWriter("output.txt")){
            while (isActive) {
                int numberRandom = rand.nextInt(100);
                fileWriter.write(numberRandom);
                fileWriter.write("\n");
                fileWriter.flush(); //D
                System.out.println("Wrote: "+ numberRandom);
                Thread.sleep(1500);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
