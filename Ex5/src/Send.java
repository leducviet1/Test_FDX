import java.io.PrintWriter;
import java.util.Scanner;

public class Send implements Runnable{
    PrintWriter out;


    public Send(PrintWriter out){
        this.out = out;
    }
  @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        while(true){
            String message = in.nextLine();
            out.println(message);
            out.flush(); // Printwriter nên có autoflush --> đẩy đi ngay
        }
    }

}
