import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.Connection;

public class ProxyFactory {
    public static <T> T createProxy(T target, DataSource dataSource) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), //implement interface : StudentService
                new TransactionProxy(target,dataSource)
        );
    }
}
