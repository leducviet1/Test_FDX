import java.sql.Connection;

public class ConnectionHolder {
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();
    public static void setConnection(Connection connection) {
        connectionHolder.set(connection);
    }
    public static Connection getConnection() {
        return connectionHolder.get();
    }
    public static void removeConnection() {
        connectionHolder.remove();
    }
}
