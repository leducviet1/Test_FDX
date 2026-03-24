package services;

import libs.DBConnectionPool;
import model.Order;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderService {
     private BlockingQueue<Order> orderQueue = new ArrayBlockingQueue<Order>(1000);
     private ExecutorService executor = Executors.newFixedThreadPool(20);
     private DBConnectionPool connectionPool;
     public OrderService(DBConnectionPool connectionPool) {
         this.connectionPool = connectionPool;
     }
     public void addOrder(Order order) throws InterruptedException {
         orderQueue.put(order);   //nếu đầy thì đợi
         System.out.println("Đã nhận được order:" + order.getOrderName());
     }
     public void startWork(){
         for(int i = 0; i < 20; i++){
             OrderWorker orderWorker = new OrderWorker(orderQueue, connectionPool);
             executor.submit(orderWorker);
         }
     }
}
