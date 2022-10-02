package com.yushchenkoaleksey.edu.quiz;

import com.yushchenkoaleksey.edu.quiz.util.Utils;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.sql.*;

public class DBUtils {
    private static final String SERVER_ADDRESS = "jdbc:mysql://localhost:3306/jdbc_quiz";
    private static final String SERVER_USER = "root";
    private static final String SERVER_PASSWORD = "root";
    public static final String ERROR = "ERROR!";
    public static String activeUserToken;
    private static final String SQL_USERS_WHERE_USERNAME = "SELECT * FROM users WHERE username = ?";

    private DBUtils(){}

    public static void signUpUser(ActionEvent event, String username, String password) {
        username = username.toLowerCase();
        try (Connection connection = DriverManager.getConnection(SERVER_ADDRESS, SERVER_USER, SERVER_PASSWORD);
             PreparedStatement psCheckUserExists = connection.prepareStatement(SQL_USERS_WHERE_USERNAME);
             PreparedStatement psInsert = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            psCheckUserExists.setString(1, username);
            ResultSet resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isBeforeFirst()) {
                Utils.showAlert(Alert.AlertType.ERROR, ERROR, "Such user already exists.", null);
            } else {
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.executeUpdate();

//                SceneController.switchTo("authorization", event);
                Utils.showAlert(Alert.AlertType.INFORMATION, "Congratulations!", "You've successfully created QUIZ account", null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void signInUser(ActionEvent event, String username, String password) {
        username = username.toLowerCase();
        try (Connection connection = DriverManager.getConnection(SERVER_ADDRESS, SERVER_USER, SERVER_PASSWORD);
             PreparedStatement psCheckUserExists = connection.prepareStatement(SQL_USERS_WHERE_USERNAME);
             PreparedStatement psGetToken = connection.prepareStatement(SQL_USERS_WHERE_USERNAME);
        ) {
            psCheckUserExists.setString(1, username);
            ResultSet resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                boolean usernameIsValid = resultSet.getString("username").equals(username);
                boolean passwordIsValid = resultSet.getString("password").equals(password);
                if (usernameIsValid && passwordIsValid) {
                    psGetToken.setString(1, username);
                    resultSet = psGetToken.executeQuery();
                    if (resultSet.next()) activeUserToken = resultSet.getString("token");
//                    SceneController.switchTo("internetOrFile", event);
                } else {
                    Utils.showAlert(Alert.AlertType.WARNING, ERROR, "The username or password is incorrect", null);
                }
            } else {
                Utils.showAlert(Alert.AlertType.WARNING, ERROR, "Specified account does not exist.", null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}














