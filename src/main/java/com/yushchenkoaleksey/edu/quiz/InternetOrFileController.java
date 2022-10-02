package com.yushchenkoaleksey.edu.quiz;

import com.yushchenkoaleksey.edu.quiz.model.FXML_FILES;
import com.yushchenkoaleksey.edu.quiz.repository.ResultRepository;
import com.yushchenkoaleksey.edu.quiz.util.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InternetOrFileController {

    @FXML private CheckBox showAnswersCheckBox;
    @FXML private Button fromInternetButton;
    @FXML private Button fromFileButton;

    public void fromInternet() {
        SceneController.switchTo(FXML_FILES.SETTINGS.filename, showAnswersCheckBox);
    }

    public void fromFile() throws IOException {
        File file = Utils.getOpenFileLocation();
        if (file != null) {
            ResultRepository resultRepository = null;
            if (file.getName().endsWith(".json")) {
                resultRepository = ResultRepository.getFromJson(file);
            } else if (file.getName().endsWith(".csv")) {
                resultRepository = ResultRepository.getFromCsv(file);
            }
            if (resultRepository != null) {
                launchGame(resultRepository);
            }
        }
    }

    private void launchGame(ResultRepository resultRepository) throws IOException {

        BorderPane borderPane = new BorderPane();

        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.TOP);
        tabPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        List<TabController> tabs = new ArrayList<>();

//      Creating tabs with questions
        for (int i = 0; i < resultRepository.getResults().size(); i++) {
            Tab tab = new Tab("Q" + (i + 1));
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILES.TAB.filename));
            Parent parent = loader.load();
            TabController controller = loader.getController();
            tabs.add(controller);
            controller.initData(resultRepository, i, showAnswersCheckBox.selectedProperty().get(), tab);
            tab.setContent(parent);
            tab.setClosable(false);
            tabPane.getTabs().add(tab);
        }

//      Final tab with the check option.
        Tab tab = new Tab("CHECK");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILES.CHECK.filename));
        Parent parent = loader.load();
        CheckController controller = loader.getController();
        controller.initData(resultRepository, tabs);
        tab.setContent(parent);
        tab.setClosable(false);
        tabPane.getTabs().add(tab);

        borderPane.setCenter(tabPane);
        Scene scene = new Scene(borderPane, 760, 600);

        Stage stage = new Stage();
        stage.setTitle("Quiz!");
        Utils.setIcon(stage);
        stage.setScene(scene);
        stage.show();

        stage = (Stage) showAnswersCheckBox.getScene().getWindow();
        stage.close();
    }
}