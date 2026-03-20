import config.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {
    public static void main(String[] args) throws IOException {
        Properties config = Config.load();
        int port = Integer.parseInt(config.getProperty("server.port"));
        int receiveTime = Integer.parseInt(config.getProperty("receive.timeout"));

        ServerSocket socketServer = new ServerSocket(port);
        System.out.println("Socket is waiting");
        Socket socket = socketServer.accept();
        System.out.println("Client connected to server");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        socket.setSoTimeout(receiveTime);
        Thread receiveData = new Thread(new ReceiveData(in,"Server"));
        receiveData.start();

        Thread sendData = new Thread(new SendData(out,"Server"));
        sendData.start();
    }
}
