package com.yushchenkoaleksey.edu.quiz;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.*;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.beans.binding.Bindings.createBooleanBinding;

public class AuthorizationController implements Initializable {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Button enterButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BooleanBinding loginFieldValid = createBooleanBinding(() -> loginField.textProperty().getValue().length() > 0, loginField.textProperty());
        BooleanBinding passwordFieldValid = createBooleanBinding(() -> passwordField.textProperty().getValue().length() > 0, passwordField.textProperty());
        enterButton.disableProperty().bind(loginFieldValid.not().or(passwordFieldValid.not()));

        EventHandler<KeyEvent> eventHandler = keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER && !enterButton.isDisabled()){
                enterButton.fire();
                keyEvent.consume(); //что делает consume?
            }};
        loginField.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
        passwordField.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
    }

    public void login(ActionEvent event) {
        DBUtils.signInUser(event, loginField.getText(), passwordField.getText());
    }

    public void createAccount(ActionEvent event) {
        SceneController.switchTo("registration", event);
    }
}
