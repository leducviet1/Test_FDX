import libs.DBConnectionPool;
import model.Order;
import services.OrderFileBackup;
import services.OrderService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws SQLException {
        DBConnectionPool connectionPool = new DBConnectionPool(20);
//        OrderFileBackup orderFileBackup = new OrderFileBackup();
        OrderService orderService = new OrderService(connectionPool);
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        for (int i = 1; i <= 100; i++) {
            int index = i;
            executorService.submit(() -> {
                Order order = new Order(
                        "Order-" + index,
                        "Order-" + "trà sữa " + index,
                        "Customer-" + index,
                        new BigDecimal("180.0"),
                        "Ha Noi"
                );
                try {
                    orderService.addOrder(order);
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executorService.shutdown();

    }
}