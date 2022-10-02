package com.yushchenkoaleksey.edu.quiz.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yushchenkoaleksey.edu.quiz.model.Result;
import com.yushchenkoaleksey.edu.quiz.util.Utils;
import org.jsoup.Jsoup;

public class ResultRepository {

    @JsonAlias({"response_code", "responseCode"})
    private Integer responseCode;
    private List<Result> results = new ArrayList<>();

    public ResultRepository() {}

    public static ResultRepository getFromJson(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        ResultRepository resultRepository = null;
        try (BufferedReader bis = new BufferedReader(new FileReader(file))) {
            resultRepository = objectMapper.readValue(bis, new TypeReference<>() {
            });
        } catch (Exception e) {
            Utils.showLoadFailedAlert();
            e.printStackTrace();
        }
        if (resultRepository != null) cipherAnswers(resultRepository);
        return resultRepository;
    }

    public static ResultRepository getFromCsv(File file){
        ResultRepository resultRepository = null;
        try {
            resultRepository = new ResultRepository();
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
        } catch (Exception e) {
            Utils.showLoadFailedAlert();
            e.printStackTrace();
            return null;
        }
        cipherAnswers(resultRepository);
        return resultRepository;
    }

    public static void saveAsJson(File file, ResultRepository questionRepository) {
        try {
            cipherAnswers(questionRepository);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(file, questionRepository);
            Utils.showSaveSuccessfulAlert();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showSaveFailedAlert(e.getMessage());
        }
    }

    private static void cipherAnswers(ResultRepository questionRepository) {
        String key = "E19F1B";
        int n = Integer.parseInt(key, 16);
        String keyword = Integer.toBinaryString(n);
        List<Result> results = questionRepository.getResults();
        for (int i = 0; i < results.size(); i++) {
            String correctAnswer = results.get(i).getCorrectAnswer();
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < correctAnswer.length(); j++) {
                sb.append((char) (correctAnswer.charAt(j) ^ keyword.charAt(j % keyword.length()))); //encrypt or decrypt data via XOR cipher and key.
            }
            results.get(i).setCorrectAnswer(sb.toString());

            List<String> incorrectAnswersCiphered = results.get(i).getIncorrectAnswers().stream().map(incorrectAnswer -> {
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < incorrectAnswer.length(); j++) {
                    stringBuilder.append((char) (incorrectAnswer.charAt(j) ^ keyword.charAt(j % keyword.length()))); //encrypt or decrypt data via XOR cipher and key.
                }
                return stringBuilder.toString();
            }).collect(Collectors.toList());
            results.get(i).setIncorrectAnswers(incorrectAnswersCiphered);
        }
    }

    public static void saveAsCsv(File file, ResultRepository resultRepository) {
        try {
            cipherAnswers(resultRepository);
            Files.writeString(file.toPath(), resultRepository.toCsv());
            Utils.showSaveSuccessfulAlert();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showSaveFailedAlert(e.getLocalizedMessage());
        }
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
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