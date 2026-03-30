import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CRUDRepository {
    private Connection connection;
    public  CRUDRepository(Connection connection){
        this.connection = connection;
    }
    public <T> List<T> findAll(Class<T> tClass) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();

        if(!tClass.isAnnotationPresent(Entity.class)){
            throw new RuntimeException(tClass.getSimpleName() + " is not an Entity  ");
        }
        Entity entity =  tClass.getAnnotation(Entity.class);
        String tableName = entity.tableName();
        String SQL =  "SELECT * FROM " + tableName + " LIMIT 100";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL);

        while(resultSet.next()){
            T obj = tClass.getDeclaredConstructor().newInstance();   //Tạo đối tượng
            for (Field field : tClass.getDeclaredFields()) {
                field.setAccessible(true);    //mở quyền truy cập (private)
                field.set(obj, resultSet.getObject(field.getName()));
            }
            list.add(obj);
        }
        return list;
    }
    public <T> void save(T obj) throws IllegalAccessException, SQLException {
        Class<?> clazz = obj.getClass();
        Entity annotation = clazz.getAnnotation(Entity.class);
        String tableName = annotation.tableName();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            columns.append(field.getName()).append(",");
            values.append("'").append(field.get(obj)).append("',");
        }
        columns.deleteCharAt(columns.length() - 1);
        values.deleteCharAt(values.length() - 1);
        String SQL = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
        //insert into students(id,name) values ("4","a")

        Statement statement = connection.createStatement();
        statement.executeUpdate(SQL);
    }
    public <T> void update(T obj) throws IllegalAccessException, SQLException {
        Class<?> clazz = obj.getClass();
        Entity annotation = clazz.getAnnotation(Entity.class);
        String tableName = annotation.tableName();

        StringBuilder columns = new StringBuilder();
        Object id = null;
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if(field.getName().equals("id")){
                id = field.get(obj);
            }else{
                columns.append(field.getName()).append("='").append(field.get(obj)).append("',");
                //set columns = ..., columns2 = ....
            }
        }
        columns.deleteCharAt(columns.length() - 1);
        String SQL = "UPDATE " + tableName + " SET " + columns + "='" + id + "' WHERE id=" + id;
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQL);
    }
    public <T> void delete(Class<T> tClass , Object id) throws IllegalAccessException, SQLException {
        Entity entity = tClass.getAnnotation(Entity.class);
        String tableName = entity.tableName();
        String SQL =  "DELETE FROM " + tableName + " WHERE id=" + id;
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQL);
    }
}
