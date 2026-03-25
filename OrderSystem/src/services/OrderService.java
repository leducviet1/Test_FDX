package services;

import libs.DBConnectionPool;
import libs.PoolConnection;
import model.Order;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderService {
    private ExecutorService executor = Executors.newFixedThreadPool(20);
    private DBConnectionPool connectionPool;
    private PoolConnection poolConnection;
    private OrderFileBackup orderFile = new OrderFileBackup();
    public OrderService(DBConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
//        this.orderFile = new OrderFileBackup();
    }

    public void addOrder(Order order) throws InterruptedException, IOException {
        System.out.println("Đã nhận được order:" + order.getOrderName());

        //Ghi file trước
        orderFile.writeOrderFile("PENDING" + "|" + order.toLine());

        executor.execute(() -> {
            try {
                saveOrder(order);
                orderFile.writeOrderFile("DONE" + "|" + order.toLine());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void saveOrder(Order order) throws InterruptedException, SQLException {
        String SQL = "INSERT INTO orders(order_name,customer_name,price,address) VALUES(?,?,?,?)";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(SQL);
            preparedStatement.setString(1, order.getOrderName());
            preparedStatement.setString(2, order.getCustomerName());
            preparedStatement.setBigDecimal(3, order.getPrice());
            preparedStatement.setString(4, order.getAddress());
            preparedStatement.executeUpdate();
            System.out.println(Thread.currentThread().getName() + "đã thêm vào database");
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
//    public void recover() throws IOException {
//        List<String> lines = orderFile.readAllOrders();
//        Set<String> doneOrders = new HashSet<>();
//        List<Order> pendingOrders = new ArrayList<>();
//        for (String line : lines) {
//            if(lines.)
//        }
//
//
//    }
}
