package services;

import model.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderFileBackup {
    private final String filePath = "orders.log";
    public OrderFileBackup() {
    }
    public void writeOrderFile(String line) throws IOException {
       BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath,true)); //Không có true sẽ bị ghi đè
       bufferedWriter.write(line);
       bufferedWriter.newLine();
       bufferedWriter.flush();
   }
   public List<String> readAllOrders() throws IOException {
        List<String> ordersList = new ArrayList<>();
        File file = new File(filePath);
        if(!file.exists()){
            return ordersList;
        }
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String line;
        while((line = bufferedReader.readLine()) != null){
            ordersList.add(line);
        }
        return ordersList;
   }
}
