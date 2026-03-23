package services;

import libs.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessengerDAO {
    DBConnection dbConnection = new DBConnection();
    public void saveMessage(String userName,String message){
        String sql = "INSERT INTO messages(username,content) VALUES(?,?)";
        try(Connection con = dbConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql)){
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, message);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
