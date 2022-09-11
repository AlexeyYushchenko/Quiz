package com.yushchenkoaleksey.edu.quiz.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class CategoryQuestionCount {

    @JsonAlias({"total_question_count", "totalQuestionCount"})
    private Integer totalQuestionCount;
    @JsonAlias({"total_easy_question_count", "totalEasyQuestionCount"})
    private Integer totalEasyQuestionCount;
    @JsonAlias({"total_medium_question_count", "totalMediumQuestionCount"})
    private Integer totalMediumQuestionCount;
    @JsonAlias({"total_hard_question_count", "totalHardQuestionCount"})
    private Integer totalHardQuestionCount;

    public CategoryQuestionCount() {
    }

    /**
     * @param totalQuestionCount
     * @param totalEasyQuestionCount
     * @param totalMediumQuestionCount
     * @param totalHardQuestionCount
     */

    public CategoryQuestionCount(Integer totalQuestionCount, Integer totalEasyQuestionCount, Integer totalMediumQuestionCount, Integer totalHardQuestionCount) {
        super();
        this.totalQuestionCount = totalQuestionCount;
        this.totalEasyQuestionCount = totalEasyQuestionCount;
        this.totalMediumQuestionCount = totalMediumQuestionCount;
        this.totalHardQuestionCount = totalHardQuestionCount;
    }

    public Integer getTotalQuestionCount() {
        return totalQuestionCount;
    }

    public void setTotalQuestionCount(Integer totalQuestionCount) {
        this.totalQuestionCount = totalQuestionCount;
    }

    public Integer getTotalEasyQuestionCount() {
        return totalEasyQuestionCount;
    }

    public void setTotalEasyQuestionCount(Integer totalEasyQuestionCount) {
        this.totalEasyQuestionCount = totalEasyQuestionCount;
    }

    public Integer getTotalMediumQuestionCount() {
        return totalMediumQuestionCount;
    }

    public void setTotalMediumQuestionCount(Integer totalMediumQuestionCount) {
        this.totalMediumQuestionCount = totalMediumQuestionCount;
    }

    public Integer getTotalHardQuestionCount() {
        return totalHardQuestionCount;
    }

    public void setTotalHardQuestionCount(Integer totalHardQuestionCount) {
        this.totalHardQuestionCount = totalHardQuestionCount;
    }

    @Override
    public String toString() {
        return "Total questions in the category: " + totalQuestionCount +
                "\nTotal Easy questions: " + totalEasyQuestionCount +
                "\nTotal Medium question: " + totalMediumQuestionCount +
                "\nTotal Hard question: " + totalHardQuestionCount;
    }
}