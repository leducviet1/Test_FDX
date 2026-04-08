import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentServiceImpl implements StudentService {
    private CRUDRepository crudRepo = new CRUDRepository();

    @Override
    @Transactional
    public List<Student> findAll() throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Student> studentList = crudRepo.findAll(Student.class);
        return studentList;
    }

    @Override
    @Transactional
    public void create(Student student) throws SQLException, IllegalAccessException {
        crudRepo.save(student);
    }

    @Override
    @Transactional
    public void delete(int id) throws SQLException {
        crudRepo.delete(Student.class, id);
    }


}
