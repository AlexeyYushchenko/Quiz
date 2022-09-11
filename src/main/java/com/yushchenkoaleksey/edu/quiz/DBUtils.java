package com.yushchenkoaleksey.edu.quiz;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.sql.*;

public class DBUtils {
    private static final String SERVER_ADDRESS = "jdbc:mysql://localhost:3306/jdbc_quiz";
    private static final String SERVER_USER = "root";
    private static final String SERVER_PASSWORD = "root";
    public static final String ERROR = "ERROR!";

    private DBUtils(){}

    public static void testMethod() {
        try (Connection connection = DriverManager.getConnection(SERVER_ADDRESS, SERVER_USER, SERVER_PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from users");

            while (resultSet.next()) {
                System.out.println(resultSet.getString("username") + ":" + resultSet.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void signUpUser(ActionEvent event, String username, String password) {
        username = username.toLowerCase();
        try (Connection connection = DriverManager.getConnection(SERVER_ADDRESS, SERVER_USER, SERVER_PASSWORD);
             PreparedStatement psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
             PreparedStatement psInsert = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            psCheckUserExists.setString(1, username);
            ResultSet resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(ERROR);
                alert.setHeaderText("Such user already exists!");
                alert.show();
            } else {
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.executeUpdate();

                SceneController.switchTo("authorization", event);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Congratulations!");
                alert.setHeaderText("You've successfully created QUIZ account.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void signInUser(ActionEvent event, String username, String password) {
        username = username.toLowerCase();
        try (Connection connection = DriverManager.getConnection(SERVER_ADDRESS, SERVER_USER, SERVER_PASSWORD);
             PreparedStatement psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
        ) {
            psCheckUserExists.setString(1, username);
            ResultSet resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                boolean usernameIsValid = resultSet.getString("username").equals(username);
                boolean passwordIsValid = resultSet.getString("password").equals(password);
                if (usernameIsValid && passwordIsValid) {
                    SceneController.switchTo("internetOrFile", event);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(ERROR);
                    alert.setHeaderText("The username or password is incorrect");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(ERROR);
                alert.setHeaderText("Specified account does not exist.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}














