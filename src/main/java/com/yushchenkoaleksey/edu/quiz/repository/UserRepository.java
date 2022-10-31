package com.yushchenkoaleksey.edu.quiz.repository;

import com.yushchenkoaleksey.edu.quiz.util.Utils;
import javafx.scene.control.Alert;

import java.sql.*;

public class UserRepository implements AutoCloseable{

    private static final String SERVER_ADDRESS = "jdbc:sqlite:C:\\Users\\User\\IdeaProjects\\Quiz\\src\\main\\resources\\com\\yushchenkoaleksey\\edu\\quiz\\sqliteDB.db";
    private final Connection connection;

    public UserRepository() throws SQLException {
        this.connection = DriverManager.getConnection(SERVER_ADDRESS);
    }

    public boolean add(String username, String password) {
        username = username.toLowerCase();
        String statement = "insert into users(username, password) values (?, ?)";
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(statement)){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected <= 0) return false;
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean signIn(String username, String password){
        username = username.toLowerCase();
        String statement = "select * from users where username = ?";
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(statement)){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()){
                resultSet.next();
                boolean isUsernameValid = resultSet.getString("username").equals(username);
                boolean isPasswordValid = resultSet.getString("password").equals(password);
                if (isUsernameValid && isPasswordValid){
                    return true;
                }else {
                    Utils.showAlert(Alert.AlertType.WARNING, "ERROR", "The username or password is incorrect", null);
                }
            }else {
                Utils.showAlert(Alert.AlertType.WARNING, "ERROR", "Specified account does not exist.", null);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) connection.close();
    }

}
