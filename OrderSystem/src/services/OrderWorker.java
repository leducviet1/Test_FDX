package services;

import libs.DBConnectionPool;
import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

public class OrderWorker implements Runnable {
    private BlockingQueue<Order> orderQueue;
    private  DBConnectionPool connectionPool;
    public OrderWorker(BlockingQueue<Order> orderQueue, DBConnectionPool connectionPool) {
        this.orderQueue = orderQueue;
        this.connectionPool = connectionPool;
    }
    @Override
    public void run() {
        while (true) {
            System.out.println(Thread.currentThread().getName() + "đang chờ order");

            try {
                //Lấy phần tử, nếu k có thì wait
                Order order = orderQueue.take();
                System.out.println(Thread.currentThread().getName() + "đang xử lý" + order.getOrderId());
                saveOrder(order);
            } catch (InterruptedException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
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
        }
        finally{
            if(conn != null){
                connectionPool.releaseConnection(conn);
            }
        }

    }
}
