package com.yushchenkoaleksey.edu.quiz;

import com.yushchenkoaleksey.edu.quiz.util.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Authorization.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Quiz!");
        Utils.setIcon(stage);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            logout(stage);
        });

        Utils.setIcon(stage);
    }

    private void logout(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("EXIT");
        alert.setHeaderText("You're about to exit the game!");
        alert.setContentText("Are you sure?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
