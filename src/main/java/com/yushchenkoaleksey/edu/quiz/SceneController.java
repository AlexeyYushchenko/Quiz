package com.yushchenkoaleksey.edu.quiz;

import com.yushchenkoaleksey.edu.quiz.model.FXML_FILES;
import com.yushchenkoaleksey.edu.quiz.util.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    //метод для переключения сцен в одном окне.
//    @FXML
//    public static void switchTo(String sceneName, ActionEvent event) {
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        if (stage.getScene() != null) stage.setScene(null);
//        if (scene != null) scene.setRoot(rootMap.get(sceneName));
//        else scene = new Scene(rootMap.get(sceneName));
//        stage.setScene(scene);
//    }

    public static void switchTo(String fileName, Control controlElement){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fileName));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(loader.load()));
            Utils.setIcon(stage);
            stage.show();
            if (fileName.equals(FXML_FILES.SETTINGS.filename)){
                GameSettingsController controller = loader.getController();
                controller.initData((CheckBox) controlElement);
            }
            closeWindow(controlElement);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeWindow(Control controlElement) {
        Stage stage = (Stage) controlElement.getScene().getWindow();
        stage.close();
    }
}
