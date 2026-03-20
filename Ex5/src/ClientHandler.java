import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String name;
    public ClientHandler(Socket clientSocket) {

        this.clientSocket = clientSocket;
    }
    public void broadcast(String message){
        for(ClientHandler clientHandler : Server.clients){
            clientHandler.out.println(message);
        }
    }
    @Override
    public void run(){
       try{
           // Luồng vào ra từ socket
           this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
           this.out = new PrintWriter(clientSocket.getOutputStream(),true);
           name = in.readLine();
           Server.clients.add(this);
           broadcast("Welcome " + name + " to the Chat!");

           String message;
           while((message = in.readLine()) != null){
                broadcast(name + ": " + message);
           }
       } catch (IOException e) {
           System.out.println("Lost Connection");
       }

    }
}
