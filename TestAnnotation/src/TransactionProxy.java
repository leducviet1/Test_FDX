import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

public class TransactionProxy implements InvocationHandler {
    private DataSource dataSource;
    private ConnectionHolder connectionHolder;
    private Object target;
    public TransactionProxy( Object target,DataSource dataSource) {
        this.dataSource = dataSource;
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method realMethod = target.getClass().getMethod(method.getName(),method.getParameterTypes());
        boolean isTransactional = realMethod.isAnnotationPresent(Transactional.class);
        if(connectionHolder.getConnection() != null){
            return method.invoke(target,args);
        }
        if(isTransactional){
            Connection connection = dataSource.getConnection();
            try{
                connection.setAutoCommit(false);
                connectionHolder.setConnection(connection);
                System.out.println("Begin Transaction");
                Object result = realMethod.invoke(target,args);
                connection.commit();
                return result;
            }catch (InvocationTargetException e){
                connection.rollback();
                System.out.println("Rollback Transaction");
                throw e.getTargetException();
            }finally {
                connectionHolder.removeConnection();
                connection.close();
            }
        }
        return realMethod.invoke(target,args);

    }

}
