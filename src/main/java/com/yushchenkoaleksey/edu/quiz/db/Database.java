package com.yushchenkoaleksey.edu.quiz.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yushchenkoaleksey.edu.quiz.model.UserStatistics;
import com.yushchenkoaleksey.edu.quiz.util.Utils;
import javafx.scene.control.Alert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class Database {
    private static final String USERS_FULL_PATH = "C:\\Users\\User\\IdeaProjects\\Quiz\\src\\main\\resources\\com\\yushchenkoaleksey\\edu\\quiz\\usersDB";
    private static final String STATISTICS_FULL_PATH = "C:\\Users\\User\\IdeaProjects\\Quiz\\src\\main\\resources\\com\\yushchenkoaleksey\\edu\\quiz\\statisticsDB";
    private static HashMap<String, String> users = new HashMap<>();
    private static HashMap<String, UserStatistics> statistics = new HashMap<>();
    private static String currentUser = "";

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(USERS_FULL_PATH))){
            users = objectMapper.readValue(bis, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(STATISTICS_FULL_PATH))){
            statistics = objectMapper.readValue(bis, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Database(){}

    public static UserStatistics getUserStatistics(String username){
         return statistics.getOrDefault(username, new UserStatistics());
    }

    public static boolean checkUser(String username){
        return users.get(username) != null;
    }

    public static boolean checkLoginPasswordCorrect(String username, String pass){
        String password = users.get(username);
        if (password == null){
            Utils.showAlert(Alert.AlertType.WARNING, "ERROR", "Specified account does not exist.", null);
            return false;
        }
        if (!pass.equals(password)){
            Utils.showAlert(Alert.AlertType.WARNING, "ERROR", "The username or password is incorrect", null);
            return false;
        }
        return true;
    }

    public static void addUser(String username, String password){
        users.put(username, password);
        updateUsers();
    }

    private static void updateUsers() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(USERS_FULL_PATH), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateStatistics(UserStatistics userStatistics) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            statistics.put(currentUser, userStatistics);
            objectMapper.writeValue(new File(STATISTICS_FULL_PATH), statistics);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setCurrentUser(String username){
        currentUser = username;
    }

    public static String getCurrentUser(){
        return currentUser;
    }
}