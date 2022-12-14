package com.yushchenkoaleksey.edu.quiz;

import com.yushchenkoaleksey.edu.quiz.db.JSON_DB;
import com.yushchenkoaleksey.edu.quiz.model.FXML_FILES;
import com.yushchenkoaleksey.edu.quiz.model.Result;
import com.yushchenkoaleksey.edu.quiz.model.UserStatistics;
import com.yushchenkoaleksey.edu.quiz.repository.ResultRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jsoup.Jsoup;

import java.util.List;

public class CheckController {
    @FXML private Button checkButton;
    @FXML private Button logoutButton;
    @FXML private Label percentLable;
    @FXML private Label correctIncorrectLable;
    @FXML private Label totalPercentLabel;
    @FXML private Label totalQuestions;
    @FXML private Label totalCorrectIncorrectLabel;
    @FXML private ListView<String> listView;
    protected ResultRepository repository;
    private List<TabController> tabControllers;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    public int answersReceived = 0;

    void initData(ResultRepository resultRepository, List<TabController> tabControllers) {
        this.repository = resultRepository;
        this.tabControllers = tabControllers;

        for (TabController tabController : tabControllers) {
            tabController.getRadioButtonGroup().selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
                if (!tabController.isChoiceMade()) {
                    tabController.getTab().setStyle("-fx-background-color: \"80FD00\";");
                    answersReceived++;
                    tabController.setChoiceMade(true);
                    if (answersReceived >= repository.getResults().size()) checkButton.setDisable(false);
                }
            });
        }
    }

    private void disableCheckButton() {
        checkButton.setDisable(true);
    }

    public void check() {
        disableCheckButton();

        for (int i = 0; i < tabControllers.size(); i++) {
            TabController controller = tabControllers.get(i);
            Result result = repository.getResults().get(i);
            RadioButton selectedRadioButton = (RadioButton) controller.getRadioButtonGroup().getSelectedToggle();
            if (selectedRadioButton.getText().equals(result.getCorrectAnswer())) {
                listView.getItems().add("question #" + (i + 1) + ": correct. ");
                correctAnswers++;

            } else {
                listView.getItems().add("question #" + (i + 1) + ": wrong "
                        + (controller.isCorrectAnswerShown()
                        ? "(right answer: " + Jsoup.parse(result.getCorrectAnswer()).text() + ")"
                        : ""));
                incorrectAnswers++;
            }
        }
        displayStatistics();
    }

    private void displayStatistics() {
        correctIncorrectLable.setText(correctAnswers + "/" + incorrectAnswers);
        double percent = (double) correctAnswers / repository.getResults().size() * 100;
        percentLable.setText(Math.round(percent * 100) / 100.0 + "%");

        UserStatistics userStatistics = JSON_DB.getUserStatistics(JSON_DB.getCurrentUser());
        userStatistics.setTotalCorrect(userStatistics.getTotalCorrect() + correctAnswers);
        userStatistics.setTotalIncorrect(userStatistics.getTotalIncorrect() + incorrectAnswers);
        totalCorrectIncorrectLabel.setText(userStatistics.getTotalCorrect() + "/" + userStatistics.getTotalIncorrect());
        int total = userStatistics.getTotalCorrect() + userStatistics.getTotalIncorrect();
        double totalPercent = (double) userStatistics.getTotalCorrect() / total * 100;
        totalPercentLabel.setText(Math.round(totalPercent * 100) / 100.0 + "%");
        totalQuestions.setText(String.valueOf(total));
        JSON_DB.updateStatistics(userStatistics);
    }

    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Are you sure?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            SceneController.switchTo(FXML_FILES.AUTHORIZATION.filename, logoutButton);
        }
    }

    public void playAgain() {
        reload();
        SceneController.switchTo(FXML_FILES.INTERNET_OR_FILE.filename, logoutButton);
    }

    private void reload() {
        SceneController.reload();
        correctAnswers = 0;
        incorrectAnswers = 0;
    }
}
