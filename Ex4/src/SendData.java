import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class SendData implements Runnable{
    private PrintWriter out;
    private String name;
    public SendData(PrintWriter out, String name){
        this.out = out;
        this.name = name;
    }
    @Override
    public void run(){
        Scanner sc = new Scanner(System.in);

        while (true){
            String message = sc.nextLine();
            out.println(message);
            System.out.println(name + "send:" + message);
        }
    }
}
