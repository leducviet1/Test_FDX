import libs.DBConnectionPool;
import model.Order;
import services.OrderService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws SQLException {
        DBConnectionPool connectionPool = new DBConnectionPool(20);
        OrderService orderService = new OrderService(connectionPool);
        orderService.startWork();
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        for (int i = 1; i <= 2; i++) {
            int index = i;

            executorService.submit(() -> {
                Order order = new Order(
                        "Order-" + index,
                        "Order-" + "trà sữa " + index,
                        "Customer-" + index,
                        new BigDecimal("120.0"),
                        "Ha Noi"
                );

                try {
                    orderService.addOrder(order);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        // 5. Đóng client pool (không nhận thêm task)
        executorService.shutdown();
        System.out.println("Đã gửi xong 300 orders ");

    }
}