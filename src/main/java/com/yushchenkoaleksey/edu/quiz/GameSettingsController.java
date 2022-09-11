package com.yushchenkoaleksey.edu.quiz;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yushchenkoaleksey.edu.quiz.model.Category;
import com.yushchenkoaleksey.edu.quiz.model.CategoryInfo;
import com.yushchenkoaleksey.edu.quiz.repository.CategoryRepository;
import com.yushchenkoaleksey.edu.quiz.repository.ResultRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameSettingsController implements Initializable {

    @FXML private Spinner<Integer> numOfQuestionsSpinner;
    @FXML private ChoiceBox<String> difficultyChoiceBox;
    @FXML private ChoiceBox<String> typeChoiceBox;
    @FXML private ChoiceBox<Category> categoriesChoiceBox;
    @FXML private Label selectDifficultyLabel;
    @FXML private Button startButton;
    @FXML private Button saveSettingsButton;

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
            this.difficultyChoiceBox.setValue(difficultyList.get(0));

            ArrayList<String> typeList = new ArrayList<>(Arrays.asList("Any Type", "Multiple Choice", "True / False"));
            this.typeChoiceBox.setItems(FXCollections.observableList(typeList));
            this.typeChoiceBox.setValue(typeList.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(ActionEvent event) throws IOException {
        ResultRepository resultRepository = getQuestionRepository();
        Integer responseCode = resultRepository.getResponseCode();
        if (responseCode == 0) {
            GameController.setRepository(resultRepository);
            SceneController.rootMap.put("game", FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("Game.fxml"))));
            SceneController.switchTo("game", event);
        } else {
            CategoryInfo categoryInfo = getCategoryInto(categoriesChoiceBox.getValue().getId());
            showAlert(Alert.AlertType.ERROR, "Oops", "We have just so many questions:", categoryInfo.toString());
        }
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
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(csvFilter);
        fileChooser.getExtensionFilters().add(jsonFilter);
        fileChooser.setInitialDirectory(new File(AppPreferences.prefs.get("default_folder", AppPreferences.DEFAULT_SAVING_FOLDER)));

        File file = fileChooser.showSaveDialog(null);

        if (file == null) return;
        try {
            if (file.toString().endsWith(".json")) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(file, getQuestionRepository());
            }else if (file.toString().endsWith(".csv")){
                Files.writeString(file.toPath(), getQuestionRepository().toCsv());
            }

            if (!file.getAbsolutePath().equals(AppPreferences.DEFAULT_SAVING_FOLDER)) {
                AppPreferences.prefs.put("default_folder", file.getParentFile().getAbsolutePath());
            }
            showAlert(Alert.AlertType.INFORMATION, "Success", "Questions were successfully saved.", null);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "ERROR", "Something went wrong!", "File was NOT saved.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }
}