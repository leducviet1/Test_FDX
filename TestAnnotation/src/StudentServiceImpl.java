import java.sql.Connection;
import java.sql.SQLException;

public class StudentServiceImpl implements StudentService {
    private CRUDRepository crudRepo;
    public StudentServiceImpl(Connection connection) {
        this.crudRepo = new CRUDRepository(connection);
    }
    @Override
    @Transactional
    public void create(Student student) throws SQLException, IllegalAccessException {
        crudRepo.save(student);
    }
    @Override
    public void delete(int id) throws SQLException, IllegalAccessException {
        crudRepo.delete(Student.class,id);
        if (true) {
            throw new RuntimeException("Lỗi khi delete!");
        }
    }

}
