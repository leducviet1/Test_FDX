package libs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;

public class DBConnectionPool {
    private static ArrayBlockingQueue<Connection> pool;

    public DBConnectionPool(int size) throws SQLException {
        pool = new ArrayBlockingQueue<>(size);
        for (int i = 0; i < size; i++) {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test", "root", "Ldviet04@"
            );
            pool.add(conn);
        }
    }


    public Connection getConnection() throws InterruptedException {
        Connection realConn = pool.take();
        return new PoolConnection(realConn, this);

    }

    public  void releaseConnection(Connection conn) throws SQLException {
        pool.offer(conn);
    }

}
