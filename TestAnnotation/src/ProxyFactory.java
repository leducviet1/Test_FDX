import java.lang.reflect.Proxy;
import java.sql.Connection;

public class ProxyFactory {
    public static <T> T createProxy(T target, Connection connection) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), //implement interface : StudentService
                new TransactionProxy(target,connection)
        );
    }
}
