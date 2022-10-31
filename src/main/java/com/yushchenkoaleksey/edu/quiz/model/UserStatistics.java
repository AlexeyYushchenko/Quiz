package com.yushchenkoaleksey.edu.quiz.model;

public class UserStatistics {
    private int totalCorrect;
    private int totalIncorrect;

    public UserStatistics() {}

    public UserStatistics(int totalCorrect, int totalIncorrect) {
        this.totalCorrect = totalCorrect;
        this.totalIncorrect = totalIncorrect;
    }

    public int getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(int totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public int getTotalIncorrect() {
        return totalIncorrect;
    }

    public void setTotalIncorrect(int totalIncorrect) {
        this.totalIncorrect = totalIncorrect;
    }
}
