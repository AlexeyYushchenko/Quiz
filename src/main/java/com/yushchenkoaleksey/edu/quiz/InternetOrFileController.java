package com.yushchenkoaleksey.edu.quiz;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yushchenkoaleksey.edu.quiz.model.Result;
import com.yushchenkoaleksey.edu.quiz.repository.ResultRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InternetOrFileController {

    public void fromInternet(ActionEvent event) {
        SceneController.switchTo("gameSettings", event);
    }

    public void fromFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(csvFilter);
        fileChooser.getExtensionFilters().add(jsonFilter);
        fileChooser.setInitialDirectory(new File(AppPreferences.prefs.get("default_folder", AppPreferences.DEFAULT_LOADING_FOLDER)));

        File file = fileChooser.showOpenDialog(null);
        if (file == null) return;

        if (!file.getAbsolutePath().equals(AppPreferences.DEFAULT_LOADING_FOLDER)) {
            AppPreferences.prefs.put("default_folder", file.getParentFile().getAbsolutePath());
        }
        if (file.getName().endsWith(".json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ResultRepository resultRepository = null;
            try (BufferedReader bis = new BufferedReader(new FileReader(file))) {
                resultRepository = objectMapper.readValue(bis, new TypeReference<>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            Integer responseCode = resultRepository.getResponseCode();
            if (responseCode == 0) {
                GameController.setRepository(resultRepository);
                try {
                    SceneController.rootMap.put("game", FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("Game.fxml"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SceneController.switchTo("game", event);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("NULL POINTER EXCEPTION");
                alert.setHeaderText("There's no question repository.");
                alert.setContentText("Wrong file.");
                alert.show();
            }
        } else if (file.getName().endsWith(".csv")) {
            try {
                ResultRepository resultRepository = new ResultRepository();
                List<Result> results = resultRepository.getResults();
                List<String> lines = Files.readAllLines(file.toPath());
                for (int i = 1; i < lines.size() - 1; i++) {
                    String[] split = lines.get(i).split(";");
                    String category = split[0];
                    String type = split[1];
                    String difficulty = split[2];
                    String question = split[3];
                    String correctAnswer = split[4];
                    List<String> incorrectAnswers = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(split, 5, split.length)));

                    results.add(new Result(category, type, difficulty, question, correctAnswer, incorrectAnswers));
                }
                resultRepository.setResponseCode(Integer.parseInt(lines.get(lines.size() - 1)));

                Integer responseCode = resultRepository.getResponseCode();
                if (responseCode == 0) {
                    GameController.setRepository(resultRepository);
                    try {
                        SceneController.rootMap.put("game", FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("Game.fxml"))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SceneController.switchTo("game", event);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("NULL POINTER EXCEPTION");
                    alert.setHeaderText("There's no question repository.");
                    alert.setContentText("Wrong file.");
                    alert.show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ERROR");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Wrong file type.");
            alert.setContentText("Filetype is not supported. File type must be either '.csv' or '.json'.");
            alert.show();
        }
    }
}
