import java.sql.Connection;
import java.sql.DriverManager;

public class TestDB {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test",
                    "root",
                    "Ldviet04@"
            );

            System.out.println("Kết nối thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}