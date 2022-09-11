package com.yushchenkoaleksey.edu.quiz.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;
import java.util.Objects;

public class Result {

    private String category;
    private String type;
    private String difficulty;
    private String question;
    @JsonAlias({"correct_answer", "correctAnswer"})
    private String correctAnswer;
    @JsonAlias({"incorrect_answers", "incorrectAnswers"})
    private List<String> incorrectAnswers = null;

    public Result() {
    }

    /**
     * @param difficulty
     * @param incorrectAnswers
     * @param question
     * @param category
     * @param type
     * @param correctAnswer
     */
    public Result(String category, String type, String difficulty, String question, String correctAnswer, List<String> incorrectAnswers) {
        super();
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result1 = (Result) o;
        return Objects.equals(category, result1.category) && Objects.equals(type, result1.type) && Objects.equals(difficulty, result1.difficulty) && Objects.equals(question, result1.question) && Objects.equals(correctAnswer, result1.correctAnswer) && Objects.equals(incorrectAnswers, result1.incorrectAnswers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, type, difficulty, question, correctAnswer, incorrectAnswers);
    }

    @Override
    public String toString() {
        return "Result{" +
                "category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", question='" + question + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", incorrectAnswers=" + incorrectAnswers +
                '}';
    }
}