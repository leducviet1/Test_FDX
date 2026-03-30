import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws Exception {

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/students");
        dataSource.setUser("root");
        dataSource.setPassword("Ldviet04@");

        StudentService studentService = new StudentServiceImpl();
        // tạo proxy
        StudentService proxy = ProxyFactory.createProxy(studentService, dataSource);

        Student s = new Student();
        s.setId(5);
        s.setName("Việt");

        proxy.create(s); // sẽ rollback

//        try {
//            proxy.delete(10);
//        } catch (Exception e) {
//            System.out.println("Đã rollback delete");
//        }
    }
}