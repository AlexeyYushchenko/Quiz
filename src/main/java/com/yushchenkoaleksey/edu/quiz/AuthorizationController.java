package com.yushchenkoaleksey.edu.quiz;

import com.yushchenkoaleksey.edu.quiz.db.Database;
import com.yushchenkoaleksey.edu.quiz.model.FXML_FILES;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static javafx.beans.binding.Bindings.createBooleanBinding;

public class AuthorizationController implements Initializable {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Button enterButton;
    @FXML private TextField textPassword;
    @FXML private CheckBox showPasswordCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Setting a dependency: if both fields, login and password, are filled in according to
//        the conditions (here text length must be greater than 0),
//        then the "Enter" button becomes active.
        BooleanBinding loginFieldValid = createBooleanBinding(
                () -> loginField.textProperty().getValue().length() > 0, loginField.textProperty()
        );
        BooleanBinding passwordFieldValid = createBooleanBinding(
                () -> passwordField.textProperty().getValue().length() > 0, passwordField.textProperty()
        );
        enterButton.disableProperty().bind(loginFieldValid.not().or(passwordFieldValid.not()));

//      Event Handler listens to key input. If the enter key on the keyboard is pressed,
//      login() function is called via enterButton.fire().
        EventHandler<KeyEvent> eventHandler = keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER && !enterButton.isDisabled()) {
                enterButton.fire();
            }
        };
        loginField.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
        passwordField.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);

        textPassword.textProperty().addListener((observableValue, s, t1) -> passwordField.setText(textPassword.getText()));
        passwordField.textProperty().addListener((observableValue, s, t1) -> textPassword.setText(passwordField.getText()));

        showPasswordCheckBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (Boolean.FALSE.equals(aBoolean)){
                textPassword.setVisible(true);
                passwordField.setVisible(false);
            }else {
                textPassword.setVisible(false);
                passwordField.setVisible(true);
            }
        });
    }

    public void login(ActionEvent event) {
        if (Database.checkLoginPasswordCorrect(loginField.getText(), passwordField.getText())) {
            SceneController.switchTo(FXML_FILES.INTERNET_OR_FILE.filename, loginField);
            Database.setCurrentUser(loginField.getText());
        }
    }

    public void createAccount(ActionEvent event) throws IOException {
        SceneController.switchTo(FXML_FILES.REGISTRATION.filename, enterButton);
    }
}
