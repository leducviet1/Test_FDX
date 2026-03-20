import log.Logger;

import java.io.BufferedReader;
import java.io.IOException;

public class Receive implements Runnable {
    BufferedReader in;
    String name;
    public Receive( BufferedReader in) {
        this.in = in;


    }
    @Override
    public void run() {
        while (true) {
            String message = null;
                try {
                    message = in.readLine();
                } catch (IOException e) {
                    Logger.log("ERROR", "Mất kết nối");
                }
                if(message==null){
                    break;
                }
                System.out.println(  message);
        }
    }
}
