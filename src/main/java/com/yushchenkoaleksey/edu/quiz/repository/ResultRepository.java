package com.yushchenkoaleksey.edu.quiz.repository;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.yushchenkoaleksey.edu.quiz.model.Result;
import org.jsoup.Jsoup;

public class ResultRepository {

    @JsonAlias({"response_code", "responseCode"})
    private Integer responseCode;
    private List<Result> results = new ArrayList<>();

    public ResultRepository() {
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        if (responseCode != 0) throw new IllegalStateException("Error while creating ResultRepository: response code " + responseCode);
        this.responseCode = responseCode;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String toCsv() {
        StringBuilder sb = new StringBuilder("category;type;difficulty;question;correct_answer;incorrect_answers").append("\n");
        for (Result result : results) {
            sb.append(result.getCategory()).append(";");
            sb.append(result.getType()).append(";");
            sb.append(result.getDifficulty()).append(";");
            sb.append(Jsoup.parse(result.getQuestion()).text()).append(";");
            sb.append(Jsoup.parse(result.getCorrectAnswer()).text()).append(";");
            sb.append(Jsoup.parse(result.getIncorrectAnswers().get(0)).text());
            for (int i = 1; i < result.getIncorrectAnswers().size(); i++) {
                sb.append(";").append(Jsoup.parse(result.getIncorrectAnswers().get(i)).text());
            }
            sb.append("\n");
        }
        sb.append("0");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ResultRepository{" +
                "response_code=" + responseCode +
                ", results=" + results +
                '}';
    }
}