package com.yushchenkoaleksey.edu.quiz.model;

public enum FXML_FILES {
    REGISTRATION("Registration.fxml"),
    AUTHORIZATION ("Authorization.fxml"),
    INTERNET_OR_FILE ("InternetOrFile.fxml"),
    SETTINGS ("GameSettings.fxml"),
    GAME ("Game.fxml"),
    CHECK ("Check.fxml"),
    TAB ("Tab.fxml");

    public final String filename;

    private FXML_FILES(String filename){
        this.filename = filename;
    }
}
