import java.sql.SQLException;

public interface StudentService {
    void create(Student student) throws SQLException, IllegalAccessException;
    void delete(int id) throws SQLException, IllegalAccessException;
}
