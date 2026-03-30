import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

public class TransactionProxy implements InvocationHandler {
    private Connection connection;
    private Object target;
    public TransactionProxy( Object target,Connection connection) {
        this.connection = connection;
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method realMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        if(realMethod.isAnnotationPresent(Transactional.class)){
            try{
                connection.setAutoCommit(false);
                System.out.println("begin transaction");
                Object result = method.invoke(target, args);
                //method : create(),...
                //target : object : StudentServiceImpl
                //args : student
                connection.commit();
                System.out.println("end transaction");
                return result;
            }catch (InvocationTargetException e){
                connection.rollback();
                System.out.println("begin rollback");
                throw e.getTargetException();
            }
        }
        return method.invoke(target, args);
    }

}
