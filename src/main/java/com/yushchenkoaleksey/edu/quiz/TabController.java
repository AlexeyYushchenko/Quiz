package com.yushchenkoaleksey.edu.quiz;

import com.yushchenkoaleksey.edu.quiz.model.Result;
import com.yushchenkoaleksey.edu.quiz.repository.ResultRepository;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabController{
    @FXML private Label questionField;
    @FXML private ToggleGroup radioButtonGroup;
    @FXML private RadioButton radioButtonA;
    @FXML private RadioButton radioButtonB;
    @FXML private RadioButton radioButtonC;
    @FXML private RadioButton radioButtonD;
    private ResultRepository repository;
    private boolean isChoiceMade = false;
    private boolean isCorrectAnswerShown = false;
    private Tab tab;

    public Tab getTab() {
        return tab;
    }

    public boolean isCorrectAnswerShown() {
        return isCorrectAnswerShown;
    }

    public boolean isChoiceMade() {
        return isChoiceMade;
    }

    public void setChoiceMade(boolean choiceMade) {
        isChoiceMade = choiceMade;
    }

    public void initData(ResultRepository resultRepository, int id, boolean isCorrectAnswerShown, Tab tab) {
        this.repository = resultRepository;
        this.isCorrectAnswerShown = isCorrectAnswerShown;
        this.tab = tab;
        loadQuestion(id);
    }

    public ToggleGroup getRadioButtonGroup() {
        return radioButtonGroup;
    }

    public void loadQuestion(int id) {
        List<Result> results = repository.getResults();
        Result result = results.get(id);
        questionField.setText(Jsoup.parse(result.getQuestion()).text());
        questionField.setAlignment(Pos.CENTER);

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
    }
}
