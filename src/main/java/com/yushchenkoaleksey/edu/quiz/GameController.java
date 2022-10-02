package com.yushchenkoaleksey.edu.quiz;

import com.yushchenkoaleksey.edu.quiz.model.FXML_FILES;
import com.yushchenkoaleksey.edu.quiz.model.Result;
import com.yushchenkoaleksey.edu.quiz.repository.ResultRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import org.jsoup.Jsoup;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameController implements Initializable {
    @FXML private Label questionField;
    @FXML private Button checkButton;
    @FXML private ToggleGroup radioButtonGroup;
    @FXML private RadioButton radioButtonA;
    @FXML private RadioButton radioButtonB;
    @FXML private RadioButton radioButtonC;
    @FXML private RadioButton radioButtonD;
    @FXML private Button logoutButton;
    @FXML private Label percentLable;
    @FXML private Label correctIncorrectLable;
    @FXML private ListView<String> listView;
    @FXML private Label questionNum;
    protected ResultRepository repository;
    private int counter = 0;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionField.setAlignment(Pos.CENTER);
        radioButtonGroup.selectedToggleProperty().addListener(
                (observableValue, toggle, t1) -> checkButton.disableProperty().set(observableValue.getValue() == null));
    }

    public void initData(ResultRepository resultRepository){
        repository = resultRepository;
        next();
    }

    public void next() {
        List<Result> results = repository.getResults();
        if (counter >= results.size()){
            gameOver();
            return;
        }

        Result result = results.get(counter);
        questionField.setText(Jsoup.parse(result.getQuestion()).text());

        List<String> options = new ArrayList<>(result.getIncorrectAnswers());
        options.add(result.getCorrectAnswer());
        options = options.stream().map(answer -> Jsoup.parse(answer).text()).collect(Collectors.toList());
        Collections.shuffle(options);

        if (result.getType().equals("multiple")) {
            radioButtonC.visibleProperty().set(true);
            radioButtonD.visibleProperty().set(true);
            radioButtonA.setText(options.get(0));
            radioButtonB.setText(options.get(1));
            radioButtonC.setText(options.get(2));
            radioButtonD.setText(options.get(3));
        } else {
            radioButtonA.setText(options.get(0));
            radioButtonB.setText(options.get(1));
            radioButtonC.visibleProperty().set(false);
            radioButtonD.visibleProperty().set(false);
        }
        counter++;
        questionNum.setText(counter + " out of " + repository.getResults().size());
    }

    private void gameOver() {
        checkButton.setDisable(true);
        radioButtonA.setVisible(false);
        radioButtonB.setVisible(false);
        radioButtonC.setVisible(false);
        radioButtonD.setVisible(false);
        questionField.setAlignment(Pos.TOP_CENTER);
        questionField.setText("the end".toUpperCase());
    }

    public void check() {
        List<Result> results = repository.getResults();
        Result result = results.get(counter - 1);
        RadioButton selectedRadioButton = (RadioButton) radioButtonGroup.getSelectedToggle();
        if (selectedRadioButton.getText().equals(result.getCorrectAnswer())) {
            listView.getItems().add(0, "question #" + counter + ": +");
            correctAnswers++;

        } else {
            listView.getItems().add(0, "question #" + counter + ": -");
            incorrectAnswers++;
        }
        correctIncorrectLable.setText(correctAnswers + "/" + incorrectAnswers);
        double percent = (double) correctAnswers / counter * 100;
        percentLable.setText(Math.round(percent * 100) / 100.0 + "%");
        selectedRadioButton.setSelected(false);
        next();
    }

    public void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Are you sure?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            SceneController.switchTo(FXML_FILES.AUTHORIZATION.filename, logoutButton);
        }
    }

    public void playAgain(ActionEvent event) throws IOException {
        reload();
        SceneController.switchTo(FXML_FILES.INTERNET_OR_FILE.filename, logoutButton);
    }

    private void reload(){
        SceneController.reload();
        counter = 0;
        correctAnswers = 0;
        incorrectAnswers = 0;
    }
}
