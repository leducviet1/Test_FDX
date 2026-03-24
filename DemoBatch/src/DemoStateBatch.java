import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemoStateBatch {
    public static List<User> init(){
        List<User> users = new ArrayList<>();
        for(int i = 0; i < 500; i++){
            users.add(new User("name"+i,"address"+i));
        }
        return users;
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        demoInsertWithoutBatch();
        System.out.println("________________________________");
        demoInsertWithBatch(500);

    }
    public static void demoInsertWithoutBatch() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo_batch","root","Ldviet04@");
        Statement statement = conn.createStatement();
        long startTime = System.currentTimeMillis();
        List<User> listUser = init();
        System.out.println("Without Batch ....");

        for(User user : listUser){
            String SQL = "INSERT INTO user_info (name, address) VALUES ('" +user.getName()+"', '" + user.getAddress()+ "');";
            statement.executeUpdate(SQL);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total time taken: " + (endTime - startTime));
        conn.close();
    }
    public static void demoInsertWithBatch(int batchSize) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo_batch","root","Ldviet04@");

        long startTime = System.currentTimeMillis();
        List<User> listUser = init();
        String sql = "INSERT INTO user_info (name, address) VALUES (?,?);";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        conn.setAutoCommit(false);

//        System.out.println("With Batch ....");
//        for (int i = 0; i< listUser.size(); i++) {
//            String SQL = "INSERT INTO user_info (name, address) VALUES ('"+listUser.get(i).getName()+"', '"+listUser.get(i).getAddress()+"');";
//            preparedStatement.addBatch(SQL);
//            if (i % batchSize == 0) {
//                preparedStatement.executeBatch();
//            }
//        }
//        preparedStatement.executeBatch();
//        conn.commit();
        int count  = 1;
        for(User user : listUser){
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getAddress());
            preparedStatement.addBatch();
            if (count % batchSize == 0) {
                preparedStatement.executeBatch();
                conn.commit();
            }
            count++;
        }
        //Nếu còn dư ra data -> gửi nốt
        preparedStatement.executeBatch();
        conn.commit();
        long endTime = System.currentTimeMillis();
        System.out.println("Total time: " + (endTime - startTime));
        conn.close();
        }
    }



