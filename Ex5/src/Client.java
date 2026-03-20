import config.Config;
import log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

public class Client {
    public static void main(String[] args)  {
        Properties config = Config.load();
        int port = Integer.parseInt(config.getProperty("server.port"));
        try {
            Socket socket = new Socket("localhost", port);
            System.out.println("Connected to the server");


            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Thread receive = new Thread(new Receive(in));
            Thread send = new Thread(new Send(out));
            receive.start();
            send.start();
        } catch (IOException e) {
            Logger.log("ERROR", e.getMessage());
        }
    }
}
