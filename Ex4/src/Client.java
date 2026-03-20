import config.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

public class Client {
    public static void main(String[] args) throws IOException {
        Properties config  = Config.load();
        int port = Integer.parseInt(config.getProperty("server.port"));
        int receiveTime  = Integer.parseInt(config.getProperty("receive.timeout"));
        Socket socket = new Socket("localhost", port);
        System.out.println("Connected to the server");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        socket.setSoTimeout(receiveTime);
        Thread receiveData = new Thread(new ReceiveData(in,"Client"));
        receiveData.start();
        Thread sendData = new Thread(new SendData(out,"Client"));
        sendData.start();
    }
}
