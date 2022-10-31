package com.yushchenkoaleksey.edu.quiz;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yushchenkoaleksey.edu.quiz.model.Category;
import com.yushchenkoaleksey.edu.quiz.model.CategoryInfo;
import com.yushchenkoaleksey.edu.quiz.model.FXML_FILES;
import com.yushchenkoaleksey.edu.quiz.repository.CategoryRepository;
import com.yushchenkoaleksey.edu.quiz.repository.ResultRepository;
import com.yushchenkoaleksey.edu.quiz.util.Utils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class GameSettingsController implements Initializable {

    @FXML private Spinner<Integer> numOfQuestionsSpinner;
    @FXML private ChoiceBox<String> difficultyChoiceBox;
    @FXML private ChoiceBox<String> typeChoiceBox;
    @FXML private ChoiceBox<Category> categoriesChoiceBox;
    @FXML private Label selectDifficultyLabel;
    @FXML private Button startButton;
    @FXML private Button saveSettingsButton;
    @FXML private CheckBox showAnswerCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 10);
            this.numOfQuestionsSpinner.setValueFactory(factory);
            this.numOfQuestionsSpinner.setEditable(true);

            CategoryRepository categoryRepository = new CategoryRepository("https://opentdb.com/api_category.php");
            this.categoriesChoiceBox.setItems(FXCollections.observableList(categoryRepository.getCategories()));
            this.categoriesChoiceBox.setValue(categoryRepository.getCategories().get(0)); //"Any Category"

            ArrayList<String> difficultyList = new ArrayList<>(Arrays.asList("Any Difficulty", "Easy", "Medium", "Hard"));
            this.difficultyChoiceBox.setItems(FXCollections.observableList(difficultyList));
            this.difficultyChoiceBox.setValue(difficultyList.get(0)); //"Any Type"

            ArrayList<String> typeList = new ArrayList<>(Arrays.asList("Any Type", "Multiple Choice", "True / False"));
            this.typeChoiceBox.setItems(FXCollections.observableList(typeList));
            this.typeChoiceBox.setValue(typeList.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initData(CheckBox checkBox){
        this.showAnswerCheckBox = checkBox;
    }

    public void start(ActionEvent event) throws IOException, ClassNotFoundException {
        ResultRepository resultRepository = getQuestionRepository();
        if (resultRepository.getResponseCode() == 0) {
            launchGame(resultRepository);
        } else {
            CategoryInfo categoryInfo = getCategoryInto(categoriesChoiceBox.getValue().getId());
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "We only have so many questions:", categoryInfo.toString());
        }
    }

    void launchGame(ResultRepository resultRepository) throws IOException {

        BorderPane borderPane = new BorderPane();

        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.TOP);
        tabPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        List<TabController> tabs = new ArrayList<>();

        for (int i = 0; i < numOfQuestionsSpinner.getValue(); i++) {
            Tab tab = new Tab("Q" + (i + 1));

            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILES.TAB.filename));
            Parent parent = loader.load();
            TabController controller = loader.getController();
            tabs.add(controller);
            controller.initData(resultRepository, i, showAnswerCheckBox.selectedProperty().get(), tab);
            tab.setContent(parent);
            tab.setClosable(false);
            tabPane.getTabs().add(tab);
        }
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

        stage = (Stage) typeChoiceBox.getScene().getWindow();
        stage.close();
    }

    private ResultRepository getQuestionRepository() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        URL url = new URL(getRequest());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        ResultRepository resultRepository = null;
        try (BufferedInputStream bis = new BufferedInputStream(connection.getInputStream())) {
            resultRepository = objectMapper.readValue(bis, new TypeReference<>() {
            });
        }
        return resultRepository;
    }

    private CategoryInfo getCategoryInto(int categoryId) throws IOException {
        URL url = new URL("https://opentdb.com/api_count.php?category=" + categoryId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        CategoryInfo categoryInfo = null;
        try (BufferedInputStream bis = new BufferedInputStream(connection.getInputStream())) {
            categoryInfo = objectMapper.readValue(bis, new TypeReference<>() {
            });
        }
        return categoryInfo;
    }

    private String getRequest() {
        int amount = numOfQuestionsSpinner.getValue();
        int category = categoriesChoiceBox.getValue().getId();
        String difficulty = difficultyChoiceBox.getValue();
        String type = typeChoiceBox.getValue();

        StringBuilder request = new StringBuilder("https://opentdb.com/api.php?");
        request.append("amount=").append(amount);
        if (category != 0) request.append("&category=").append(category);
        if (!difficulty.equals("Any Difficulty")) request.append("&difficulty=").append(difficulty.toLowerCase());
        if (type.equals("Multiple Choice")) {
            request.append("&type=").append("multiple");
        } else if (type.equals("True / False")) {
            request.append("&type=").append("boolean");
        }
        return request.toString();
    }

    public void saveSettings(ActionEvent event) throws IOException {
        ResultRepository questionRepository = getQuestionRepository();
        if (questionRepository.getResponseCode() == 0) {
            File file = Utils.getSaveFileLocation();
            if (file != null) {
                if (file.toString().endsWith(".json")) {
                    ResultRepository.saveAsJson(file, questionRepository);
                } else if (file.toString().endsWith(".csv")) {
                    ResultRepository.saveAsCsv(file, questionRepository);
                }
            }
        } else if (questionRepository.getResponseCode() == 1) {
            CategoryInfo categoryInfo = getCategoryInto(categoriesChoiceBox.getValue().getId());
            Utils.showAlert(Alert.AlertType.ERROR, "Oops", "We only have so many questions:", categoryInfo.toString());
        }else {
            Utils.showAlert(Alert.AlertType.ERROR, "ERROR", String.valueOf(questionRepository.getResponseCode()), null);
        }
    }
}