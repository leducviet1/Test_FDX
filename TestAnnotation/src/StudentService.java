import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface StudentService {
    List<Student> findAll() throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    void create(Student student) throws SQLException, IllegalAccessException;
    void delete(int id) throws SQLException, IllegalAccessException;
}
