package com.yushchenkoaleksey.edu.quiz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class SceneController {
    protected static HashMap<String, Pane> rootMap;
    protected static Scene scene;

    private SceneController(){}

    static {
        try {
            rootMap = new HashMap<>();
            rootMap.put("internetOrFile", FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("InternetOrFile.fxml"))));
            rootMap.put("gameSettings", FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("GameSettings.fxml"))));
            rootMap.put("registration", FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("Registration.fxml"))));
            rootMap.put("authorization", FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("Authorization.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload() {
        try {
            rootMap.put("gameSettings", FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("GameSettings.fxml"))));
            rootMap.put("game", FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("Game.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public static void switchTo(String sceneName, ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (stage.getScene() != null) stage.setScene(null);
        if (scene != null) scene.setRoot(rootMap.get(sceneName));
        else scene = new Scene(rootMap.get(sceneName));
        stage.setScene(scene);
    }
}
