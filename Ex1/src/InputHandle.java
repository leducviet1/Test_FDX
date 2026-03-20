import java.util.Scanner;

public class InputHandle implements Runnable{
    private RandomWriter randomWriter;

    public InputHandle(RandomWriter randomWriter){
        this.randomWriter = randomWriter;

    }
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(true){
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("stop")){
                randomWriter.stop();
                break;
            }
        }
        scanner.close();
        System.out.println("Thread stopped");
    }
}
