import java.io.BufferedReader;
import java.io.IOException;

public class ReceiveData implements Runnable{
    private BufferedReader in;
    private String name;
    public ReceiveData(BufferedReader in,String name){
        this.in=in;
        this.name=name;
    }
    @Override
    public void run(){
        while (true){
            String message = null;
            try {
                message = in.readLine();
            } catch (IOException e) {
                System.out.println(name + "lost connection");
            }
            if(message==null){
                break;
            }else{
                System.out.println(name + "receive:" + message);
            }
        }
    }

}
