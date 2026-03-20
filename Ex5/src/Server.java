import config.Config;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Server {
    public static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Properties prop = Config.load();
        int portServer = Integer.parseInt(prop.getProperty("server.port"));
        ServerSocket serverSocket = new ServerSocket(portServer);
        System.out.println("Socket is waiting");
        while(true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client :" + clientSocket);
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            Thread thread = new Thread(clientHandler);
            thread.start();
        }

    }
}
