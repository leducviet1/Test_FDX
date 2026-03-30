import java.sql.*;

public class Main {

    public static void main(String[] args) throws Exception {

        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/students", "root", "Ldviet04@"
        );

        StudentService service = new StudentServiceImpl(conn);

        // tạo proxy
        StudentService proxy = ProxyFactory.createProxy(service, conn);

        Student s = new Student();
        s.setId(1);
        s.setName("Việt");

        proxy.create(s); // sẽ rollback

        try {
            proxy.delete(10);
        } catch (Exception e) {
            System.out.println("Đã rollback delete");
        }
    }
}